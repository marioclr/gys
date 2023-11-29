package gob.issste.gys.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gob.issste.gys.JdbcTemplateDemo01Application;
import gob.issste.gys.model.Beneficiario;
import gob.issste.gys.repository.IBeneficiarioRepository;
import gob.issste.gys.response.ResponseHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class BeneficiarioController {

	Logger logger = LoggerFactory.getLogger(JdbcTemplateDemo01Application.class);

	@Autowired
	IBeneficiarioRepository beneficiarioRepository;

	@Operation(summary = "Agregar un Beneficiario de pensión alimenticia en el Sistema", description = "Agregar un Beneficiario de pensión alimenticia en el Sistema", tags = { "Beneficiario" })
	@PostMapping("/Beneficiario")
	public ResponseEntity<Object> createBeneficiario(@RequestBody Beneficiario beneficiario) {

		int idBenef;

		try {

			if(beneficiarioRepository.suma_porc_beneficiario(beneficiario) + beneficiario.getPorcentaje() > 100) {
				return ResponseHandler.generateResponse("La suma de porcentajes a beneficiarios del títular en bolsa de trabajo excede el 100 %", HttpStatus.INTERNAL_SERVER_ERROR, null);
			}

			idBenef = beneficiarioRepository.save(new Beneficiario( beneficiario.getIdBolsa(), beneficiario.getNombre(), beneficiario.getApellidoPaterno(),
													beneficiario.getApellidoMaterno(), beneficiario.getPorcentaje(), beneficiario.getNumeroBenef(),
													beneficiario.getId_centro_trab(), beneficiario.getRfc(), beneficiario.getFec_inicio(), beneficiario.getFec_fin(),
													beneficiario.getCons_benef(), beneficiario.getId_usuario() ));

			return ResponseHandler.generateResponse("El beneficiario de pensión alimenticia ha sido creado de manera exitosa, con ID " + idBenef, HttpStatus.OK, null);
		} catch (Exception e) {

			return ResponseHandler.generateResponse("Error al agregar nuevo beneficiario de pensión alimenticia al Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Actualiza la información del beneficiario del Sistema", description = "Actualiza la información del beneficiario del Sistema", tags = { "Beneficiario" })
	@PutMapping("/Beneficiario/{id}")
	public ResponseEntity<Object> updateBenef(@PathVariable("id") Integer id, @RequestBody Beneficiario beneficiario) {
		Beneficiario _beneficiario = beneficiarioRepository.findById(id);

		if(beneficiarioRepository.suma_porc_beneficiario_upd(beneficiario) + beneficiario.getPorcentaje() > 100) {
			return ResponseHandler.generateResponse("La suma de porcentajes a beneficiarios del títular en bolsa de trabajo excede el 100 %", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}

		if (_beneficiario != null) {
			_beneficiario.setNombre(beneficiario.getNombre());
			_beneficiario.setApellidoPaterno(beneficiario.getApellidoPaterno());
			_beneficiario.setApellidoMaterno(beneficiario.getApellidoMaterno());
			_beneficiario.setNumeroBenef(beneficiario.getNumeroBenef());
			_beneficiario.setPorcentaje(beneficiario.getPorcentaje());
			_beneficiario.setId_centro_trab(beneficiario.getId_centro_trab());
			_beneficiario.setRfc(beneficiario.getRfc());
			_beneficiario.setFec_inicio(beneficiario.getFec_inicio());
			_beneficiario.setFec_fin(beneficiario.getFec_fin());
			_beneficiario.setCons_benef(beneficiario.getCons_benef());
			_beneficiario.setId_usuario(beneficiario.getId_usuario());

			beneficiarioRepository.update(_beneficiario);

			return ResponseHandler.generateResponse("La información del beneficiario ha sido modificada de manera exitosa", HttpStatus.OK, null);
		} else {

			return ResponseHandler.generateResponse("No se pudo encontrar la información del beneficiario con id=" + id, HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Elimina información del beneficiario del Sistema", description = "Elimina información del beneficiario del Sistema", tags = { "Beneficiario" })
	@DeleteMapping("/Beneficiario/{id}")
	public ResponseEntity<Object> deletePaga(@PathVariable("id") Integer id) {
		try {
			int result = beneficiarioRepository.deleteById(id);

			if (result == 0) {

				return ResponseHandler.generateResponse("No se pudo encontrar la información del beneficiario en el Sistema, con el ID =" + id, HttpStatus.NOT_FOUND, null);
			}

			return ResponseHandler.generateResponse("La información del beneficiario fué eliminada exitósamente.", HttpStatus.OK, null);
		} catch (Exception e) {

			return ResponseHandler.generateResponse("Error al obtener la información del beneficiario en el Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Obtiene información de los beneficiarios de un trabajador en el Sistema", description = "Obtiene información de los beneficiarios de un trabajador en el Sistema", tags = { "Beneficiario" })
	@GetMapping("/Beneficiario")
	public ResponseEntity<Object> getBenef(
			@Parameter(description = "Parámetro opcional para indicar el nombre del beneficiario del que se desea obtener la información", required = false) @RequestParam(required = false) String desc) {
		try {
			List<Beneficiario> benef = new ArrayList<Beneficiario>();

			if (desc == null)
				beneficiarioRepository.findAll().forEach(benef::add);
			else
				beneficiarioRepository.findByName(desc).forEach(benef::add);

			if (benef.isEmpty()) {

				return ResponseHandler.generateResponse("No se encontró la información del beneficiario en el Sistema", HttpStatus.NOT_FOUND, null);
			}

			return ResponseHandler.generateResponse("Se pudo obtener la información del beneficiario en el Sistema de manera exitosa", HttpStatus.OK, benef);
		} catch (Exception e) {

			return ResponseHandler.generateResponse("Error al obtener la información del beneficiario en el Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Obtiene información de los beneficiario del Sistema", description = "Obtiene información de fechas de control de pagos de GyS en el Sistema", tags = { "Beneficiario" })
	@GetMapping("/BeneficiarioXTrabajador")
	public ResponseEntity<Object> getBenef(
			@Parameter(description = "Parámetro requerido para indicar el ID del trabajador en Bolsa de trabajo del que se desea obtener la información", required = true) @RequestParam(required = true) Integer id) {
		try {
			List<Beneficiario> benef = new ArrayList<Beneficiario>();

			beneficiarioRepository.findByTrab(id).forEach(benef::add);

			if (benef.isEmpty()) {

				return ResponseHandler.generateResponse("No se encontró la información del beneficiario en el Sistema", HttpStatus.NOT_FOUND, null);
			}

			return ResponseHandler.generateResponse("Se pudo obtener la información del beneficiario en el Sistema de manera exitosa", HttpStatus.OK, benef);
		} catch (Exception e) {

			return ResponseHandler.generateResponse("Error al obtener la información del beneficiario en el Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

}

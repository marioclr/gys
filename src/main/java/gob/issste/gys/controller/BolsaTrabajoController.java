package gob.issste.gys.controller;

import java.util.ArrayList;
import java.util.List;

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

import gob.issste.gys.model.BolsaTrabajo;
import gob.issste.gys.repository.IBolsaTrabajoRepository;
import gob.issste.gys.response.ResponseHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
@Tag(name = "Bolsa de Trabajo", description = "API de bolsa de trabajo")
public class BolsaTrabajoController {

	@Autowired
	IBolsaTrabajoRepository bolsaTrabajoRepository;

	@Operation(summary = "Obtener elementos a la bolsa de trabajo en el Sistema", description = "Obtener elementos a la bolsa de trabajo en el Sistema", tags = { "Bolsa de Trabajo" })
	@GetMapping("/bolsatrabajo")
	public ResponseEntity<Object> getBolsaTrabajo(
			@Parameter(description = "ID de la delegación para obtener la bolsa de trabajo asociada", required = false) @RequestParam(required = false) String idDeleg ) {

		List<BolsaTrabajo> bolsa = new ArrayList<BolsaTrabajo>();

		if (idDeleg == null) {
			bolsa = bolsaTrabajoRepository.findAll();
		} else {
			bolsa = bolsaTrabajoRepository.findForDeleg(idDeleg);
		}

		if (bolsa.isEmpty()) {

			return ResponseHandler.generateResponse("No existen elementos de Bolsa de Trabajo", HttpStatus.NOT_FOUND, null);
		}

		return ResponseHandler.generateResponse("Se han obtenido elementos de Bolsa de Trabajo del Sistema", HttpStatus.OK, bolsa);
	}

	@Operation(summary = "Obtiene un elemento de Bolsa de Trabajo del Sistema", description = "Obtiene un elemento de Bolsa de Trabajo del Sistema", tags = { "Bolsa de Trabajo" })
	@GetMapping("/bolsatrabajo/{id}")
	public ResponseEntity<Object> getBolsaTrabajoById(
			@Parameter(description = "ID del tipo de movimiento del que se desea obtener la información", required = true) @PathVariable("id") Integer id) {

		BolsaTrabajo BolsasDeTrabajo = bolsaTrabajoRepository.getElementById(id);

		if (BolsasDeTrabajo != null) {

			return ResponseHandler.generateResponse("Se ha obtiene un elemento de Bolsa de Trabajo del Sistema", HttpStatus.OK, BolsasDeTrabajo);
		} else {

			return ResponseHandler.generateResponse("No se pudo encontrar el elemento de Bolsa de Trabajo con el ID =" + id, HttpStatus.NOT_FOUND, null);
		}
	}

	@Operation(summary = "Agrega un nuevo elemento a la bolsa de trabajo en el Sistema", description = "Agrega un nuevo elemento a la bolsa de trabajo en el Sistema", tags = { "Bolsa de Trabajo" })
	@PostMapping("/bolsatrabajo")
	public ResponseEntity<Object> agregarElemento(
			@Parameter(description = "Elemento de la bolsa de trabajo a crear en el Sistema") @RequestBody BolsaTrabajo elemento) {

		try {

			BolsaTrabajo bolsasDeTrabajo = bolsaTrabajoRepository.findByRFCDel(elemento.getRfc(), elemento.getDelegacion().getId_div_geografica());

			if (bolsasDeTrabajo != null)
				return ResponseHandler.generateResponse("Ya existe un elemento en la bolsa de trabajo del sistema con el RFC y la Of. de representación indicados", HttpStatus.INTERNAL_SERVER_ERROR, null);

			int idBolsa = bolsaTrabajoRepository.save(elemento);

			return ResponseHandler.generateResponse("Se ha creado elemento en bolsa de trabajo con ID " + idBolsa, HttpStatus.OK, null);

		} catch (Exception ex) {

			return ResponseHandler.generateResponse("Error al agregar un nuevo elemento a la bolsa de trabajo en el sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Actualizar un nuevo elemento a la bolsa de trabajo en el Sistema", description = "Actualizar un nuevo elemento a la bolsa de trabajo en el Sistema", tags = { "Bolsa de Trabajo" })
	@PutMapping("/bolsatrabajo/{id}")
	public ResponseEntity<Object> updateBolsaDeTrabajo(@PathVariable("id") int id, @RequestBody BolsaTrabajo elemento) {
		BolsaTrabajo _elemento = bolsaTrabajoRepository.getElementById(id); 
		if (_elemento != null) {
			_elemento.setRfc(elemento.getRfc());
			_elemento.setNombre(elemento.getNombre());
			_elemento.setApellidoPat(elemento.getApellidoPat());
			_elemento.setApellidoMat(elemento.getApellidoMat());
			_elemento.setDelegacion(elemento.getDelegacion());
			_elemento.setCodigoPostal(elemento.getCodigoPostal());
			_elemento.setCurp(elemento.getCurp());
			_elemento.setId_beneficiario(elemento.getId_beneficiario());
			bolsaTrabajoRepository.update(_elemento);

			return ResponseHandler.generateResponse("El elemento de la bolsa de trabajo ha sido modificado de manera exitosa", HttpStatus.OK, null);
		} else {

			return ResponseHandler.generateResponse("No se pudo encontrar el elemento de la bolsa de trabajo con el ID =" + id, HttpStatus.NOT_FOUND, null);
		}
	}

	@Operation(summary = "Eliminar un nuevo elemento a la bolsa de trabajo en el Sistema", description = "Eliminar un nuevo elemento a la bolsa de trabajo en el Sistema", tags = { "Bolsa de Trabajo" })
	@DeleteMapping("/bolsatrabajo/{id}")
	public ResponseEntity<Object> eliminarBolsaDeTrabajo(@PathVariable("id") int id) {
		try {
			int result = bolsaTrabajoRepository.deleteById(id);
			if (result == 0) {

				return ResponseHandler.generateResponse("No se pudo encontrar el elemento de la bolsa de trabajo con el ID =" + id, HttpStatus.NOT_FOUND, null);
			}

			return ResponseHandler.generateResponse("El elemento de la bolsa de trabajo fué eliminado exitosamente.", HttpStatus.OK, null);
		} catch (Exception e) {

			return ResponseHandler.generateResponse("No se borró el elemento de la bolsa de trabajo", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Obtener elementos a la bolsa de trabajo en el Sistema", description = "Obtener elementos a la bolsa de trabajo en el Sistema", tags = { "Bolsa de Trabajo" })
	@GetMapping("/bolsatrabajo/rfc")
	public ResponseEntity<Object> getBolsaTrabajoXRfc(
			@Parameter(description = "RFC o parte del RFC para filtrar elementos", required = true) @RequestParam(required = true) String rfc) {

		try {
			BolsaTrabajo bolsa = bolsaTrabajoRepository.findByRFC(rfc);
			if (bolsa == null) {			

				return ResponseHandler.generateResponse("No existen elementos a la bolsa de trabajo en el Sistema", HttpStatus.NOT_FOUND, null);
			}

			return ResponseHandler.generateResponse("Existen elementos a la bolsa de trabajo en el Sistema", HttpStatus.OK, bolsa);
		} catch (Exception e) {

			return ResponseHandler.generateResponse("Error al obtener elementos a la bolsa de trabajo en el Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Obtener elementos a la bolsa de trabajo en el Sistema", description = "Obtener elementos a la bolsa de trabajo en el Sistema", tags = { "Bolsa de Trabajo" })
	@GetMapping("/bolsatrabajo/likeRfc")
	public ResponseEntity<Object> getBolsaTrabajoLikeRfc(
			@Parameter(description = "RFC o parte del RFC para filtrar elementos", required = true) @RequestParam(required = true) String rfc,
			@Parameter(description = "Parámetro opcional para indicar la delegación del usuario", required = false) @RequestParam(required = false) String idDeleg) {

		List<String> rfcs = new ArrayList<String>();

		try {
			if (idDeleg == null)
				rfcs = bolsaTrabajoRepository.findLikeRFC(rfc);
			else
				rfcs = bolsaTrabajoRepository.findLikeRFC(rfc, idDeleg);

			if (rfcs == null) {

				return ResponseHandler.generateResponse("No existen elementos a la bolsa de trabajo en el Sistema", HttpStatus.NOT_FOUND, null);
			}

			return ResponseHandler.generateResponse("Existen elementos a la bolsa de trabajo en el Sistema", HttpStatus.OK, rfcs);
		} catch (Exception e) {

			return ResponseHandler.generateResponse("Error al obtener elementos a la bolsa de trabajo en el Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Obtener elementos a la bolsa de trabajo en el Sistema", description = "Obtener elementos a la bolsa de trabajo en el Sistema", tags = { "Bolsa de Trabajo" })
	@GetMapping("/bolsatrabajo/rfcDisponible")
	public ResponseEntity<Object> validaRfcDisponible(
			@Parameter(description = "RFC o parte del RFC para filtrar elementos", required = true) @RequestParam(required = true) String rfc) {

		try {
			BolsaTrabajo bolsa = bolsaTrabajoRepository.findByRFC(rfc);
			if (bolsa == null) {			

				return ResponseHandler.generateResponse("El RFC está disponible para registrarse en el sistema.", HttpStatus.OK, false);
			} else {

				return ResponseHandler.generateResponse("El RFC ya está registrado en el sistema.", HttpStatus.INTERNAL_SERVER_ERROR, true);
			}

		} catch (Exception e) {

			return ResponseHandler.generateResponse("Error al obtener elementos a la bolsa de trabajo en el Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

}
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
import gob.issste.gys.model.PresupuestoGlobal;
import gob.issste.gys.repository.IPresupuestoGlobalRepository;
import gob.issste.gys.response.ResponseHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class PresupuestoGlobalController {

	Logger logger = LoggerFactory.getLogger(JdbcTemplateDemo01Application.class);

//	@Autowired
//	UsuarioRepository usuarioRepository;

	@Autowired
	IPresupuestoGlobalRepository presGlobalRepository;

	@Operation(summary = "Agregar un elemento de Presupuesto global en el Sistema", description = "Agregar un elemento de Presupuesto global en el Sistema", tags = { "Presupuesto Global" })
	@PostMapping("/PresupuestoGlobal")
	public ResponseEntity<Object> createPresupuestoGlobal(@RequestBody PresupuestoGlobal presupuestoGlobal) {
		try {

			if(presGlobalRepository.existe_presupuesto(presupuestoGlobal)>0) {
				return ResponseHandler.generateResponse("Existe un registro de presupuesto en este mismo periodo", HttpStatus.INTERNAL_SERVER_ERROR, null);
			}

			presGlobalRepository.save(new PresupuestoGlobal(presupuestoGlobal.getAnio(), presupuestoGlobal.getDelegacion(), 
					presupuestoGlobal.getSaldo(), presupuestoGlobal.getComentarios(), presupuestoGlobal.getId_usuario()));

			return ResponseHandler.generateResponse("El elemento de Presupuesto global ha sido creado de manera exitosa", HttpStatus.OK, null);
		} catch (Exception e) {

			return ResponseHandler.generateResponse("Error al agregar nuevo elemento de Presupuesto global al Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Agregar lista de elementos de Presupuestos globales en el Sistema", description = "Agregar lista de elementos de Presupuestos globales en el Sistema", tags = { "Presupuesto Global" })
	@PostMapping("/PresupuestosGlobales")
	public ResponseEntity<Object> createPresupuestosGlobales(@RequestBody List<PresupuestoGlobal> presupuestosGlobales) {

		List<String> messages = new ArrayList<String>();

		try {
			for (PresupuestoGlobal presGlob : presupuestosGlobales) {
				try {

					if(presGlobalRepository.existe_presupuesto(presGlob) > 0) {
						messages.add("Existe un registro de presupuesto en este mismo periodo" + presGlob);
						continue;
					}

					presGlobalRepository.save(new PresupuestoGlobal(presGlob.getAnio(), presGlob.getDelegacion(), 
							presGlob.getSaldo(), presGlob.getComentarios(), presGlob.getId_usuario()));

					messages.add("El elemento de Presupuesto global ha sido creado de manera exitosa" + presGlob);

				} catch (Exception e) {

					messages.add("Error al agregar nuevo elemento de Presupuesto global al Sistema" + presGlob);
					continue;
				}
			}
			return ResponseHandler.generateResponse("El elemento de Presupuesto global ha sido creado de manera exitosa", HttpStatus.OK, messages);
		} catch (Exception e) {

			return ResponseHandler.generateResponse("Error al agregar nuevo elemento de Presupuesto global al Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Actualiza la información de un elemento de Presupuesto global en el Sistema", description = "Actualiza la información de un elemento de Presupuesto global en el Sistema", tags = { "Presupuesto Global" })
	@PutMapping("/PresupuestoGlobal/{id}")
	public ResponseEntity<Object> updatePresupuestoGlobal(@PathVariable("id") Integer id, @RequestBody PresupuestoGlobal presupuestoGlobal) {
		PresupuestoGlobal _presGlobal = presGlobalRepository.findById(id);

		if (_presGlobal != null) {
			_presGlobal.setSaldo(presupuestoGlobal.getSaldo());
			_presGlobal.setComentarios(presupuestoGlobal.getComentarios());
			_presGlobal.setId_usuario(presupuestoGlobal.getId_usuario());

			presGlobalRepository.update(_presGlobal);

			return ResponseHandler.generateResponse("El elemento de Presupuesto global ha sido modificado de manera exitosa", HttpStatus.OK, null);
		} else {

			return ResponseHandler.generateResponse("No se pudo encontrar el elemento de Presupuesto global con id=" + id, HttpStatus.NOT_FOUND, null);
		}
	}

	@Operation(summary = "Obtiene los elementos del Presupuesto global en el Sistema", description = "Obtiene los elementos del Presupuesto global en el Sistema", tags = { "Presupuesto Global" })
	@GetMapping("/PresupuestoGlobal")
	public ResponseEntity<Object> getPresupuestoGlobal(
			@Parameter(description = "Texto en comentarios del que se desea obtener la información", required = false) @RequestParam(required = false) String coment) {
		try {
			List<PresupuestoGlobal> presupGlobal = new ArrayList<PresupuestoGlobal>();

			if (coment == null)
				presGlobalRepository.findAll().forEach(presupGlobal::add);
			else
				presGlobalRepository.findByComent(coment).forEach(presupGlobal::add);

			if (presupGlobal.isEmpty()) {

				return ResponseHandler.generateResponse("No se pudo obtener la información de elementos de Presupuesto global en el Sistema", HttpStatus.NOT_FOUND, null);
			}

			return ResponseHandler.generateResponse("Se obtuvo la información de elementos de Presupuesto global en el Sistema", HttpStatus.OK, presupGlobal);

		} catch (Exception e) {

			return ResponseHandler.generateResponse("Error al obtener la información de elementos de Presupuesto global en el Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Obtiene información de un elemento de Presupuesto global del Sistema", description = "Obtiene información de un elemento de Presupuesto global del Sistema", tags = { "Presupuesto Global" })
	@GetMapping("/PresupuestoGlobal/{id}")
	public ResponseEntity<Object> getPresupuestoGlobalById(
			@Parameter(description = "ID del elemento de Presupuesto global del que se desea obtener la información", required = true) @PathVariable("id") Integer id) {

		PresupuestoGlobal presupuestosGlobal = presGlobalRepository.findById(id);

		if (presupuestosGlobal != null) {

			return ResponseHandler.generateResponse("Se obtuvo la información de un elemento de Presupuesto global en el Sistema", HttpStatus.OK, presupuestosGlobal);
		} else {

			return ResponseHandler.generateResponse("No se encontró el elemento de Presupuesto global en el Sistema", HttpStatus.NOT_FOUND, null);
		}
	}

	@Operation(summary = "Elimina información de un elemento de Presupuesto global en el Sistema", description = "Elimina información de un elemento de Presupuesto global en el Sistema", tags = { "Presupuesto Global" })
	@DeleteMapping("/PresupuestoGlobal/{id}")
	public ResponseEntity<Object> deletePresupuestoGlobal(@PathVariable("id") Integer id) {
		try {
			int result = presGlobalRepository.deleteById(id);
			if (result == 0) {

				return ResponseHandler.generateResponse("No se pudo encontrar el elemento de Presupuesto global con el ID =" + id, HttpStatus.NOT_FOUND, null);
			}

			return ResponseHandler.generateResponse("El elemento de Presupuesto global fué eliminado exitósamente.", HttpStatus.OK, null);
		} catch (Exception e) {

			return ResponseHandler.generateResponse("No se borró el elemento de Presupuesto global.", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}
}
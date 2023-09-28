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
import gob.issste.gys.model.Opcion;
import gob.issste.gys.repository.OpcionRepository;
import gob.issste.gys.response.ResponseHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
@Tag(name = "Opciones", description = "API de gestión de Opciones del sistema")
public class OpcionController {

	Logger logger = LoggerFactory.getLogger(JdbcTemplateDemo01Application.class);

	@Autowired
	OpcionRepository opcionRepository;

	@Operation(summary = "Agregar una Opción en el Sistema", description = "Agregar una Opción en el Sistema", tags = { "Opciones" })
	@PostMapping("/opciones")
	//public ResponseEntity<String> createOpcion(@RequestBody Opcion opcion) {
	public ResponseEntity<Object> createOpcion(@RequestBody Opcion opcion) {
		try {
			opcionRepository.save(new Opcion(opcion.getDescripcion(), opcion.getComponente(), opcion.getId_usuario()));
			//return new ResponseEntity<>("La opcion ha sido creada de manera exitosa", HttpStatus.OK);
			return ResponseHandler.generateResponse("La opcion ha sido creada de manera exitosa", HttpStatus.OK, null);
		} catch (Exception e) {
			//return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			return ResponseHandler.generateResponse("Error al crear la opcion de manera exitosa", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Actualiza una Opción en el Sistema", description = "Actualiza una Opción en el Sistema", tags = { "Opciones" })
	@PutMapping("/opciones/{id}")
	//public ResponseEntity<String> updateOpcion(@PathVariable("id") int id, @RequestBody Opcion opcion) {
	public ResponseEntity<Object> updateOpcion(@PathVariable("id") int id, @RequestBody Opcion opcion) {
		Opcion _opcion = opcionRepository.findById(id);

		if (_opcion != null) {
			_opcion.setIdOpcion(id);
			_opcion.setDescripcion(opcion.getDescripcion());
			_opcion.setComponente(opcion.getComponente());
			_opcion.setId_usuario(opcion.getId_usuario());

			opcionRepository.update(_opcion);
			//return new ResponseEntity<>("La opción ha sido modificada de manera exitosa", HttpStatus.OK);
			return ResponseHandler.generateResponse("La opción ha sido modificada de manera exitosa", HttpStatus.OK, null);
		} else {
			//return new ResponseEntity<>("No se pudo encontrar la opción con id=" + id, HttpStatus.NOT_FOUND);
			return ResponseHandler.generateResponse("No se pudo encontrar la opción con id=" + id, HttpStatus.NOT_FOUND, null);
		}
	}

	@Operation(summary = "Obtener las Opciones del Sistema", description = "Obtener las Opciones del Sistema", tags = { "Opciones" })
	@GetMapping("/opciones")
	//public ResponseEntity<List<Opcion>> getOpciones(@RequestParam(required = false) String desc) {
	public ResponseEntity<Object> getOpciones(@RequestParam(required = false) String desc) {
		try {
			List<Opcion> opciones = new ArrayList<Opcion>();

			if (desc == null)
				opcionRepository.findAll().forEach(opciones::add);
			else
				opcionRepository.findByDesc(desc).forEach(opciones::add);

			if (opciones.isEmpty()) {
				//return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				return ResponseHandler.generateResponse("No se obtuvieron las Opciones del Sistema", HttpStatus.NOT_FOUND, null);
			}

			//return new ResponseEntity<>(opciones, HttpStatus.OK);
			return ResponseHandler.generateResponse("Se obtuvieron las Opciones del Sistema", HttpStatus.OK, opciones);
		} catch (Exception e) {
			//return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			return ResponseHandler.generateResponse("Erro al obtener las Opciones del Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Obtener una Opción del Sistema mediante el ID", description = "Obtener una Opción del Sistema mediante el ID", tags = { "Opciones" })
	@GetMapping("/opciones/{id}")
	//public ResponseEntity<Opcion> getOpcionById(@PathVariable("id") int id) {
	public ResponseEntity<Object> getOpcionById(@PathVariable("id") int id) {
		Opcion opcion = opcionRepository.findById(id);

		if (opcion != null) {
			//return new ResponseEntity<>(opcion, HttpStatus.OK);
			return ResponseHandler.generateResponse("Se encontro la Opción del Sistema", HttpStatus.OK, opcion);
		} else {
			//return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return ResponseHandler.generateResponse("Error al obtener una Opción del Sistema mediante el ID", HttpStatus.NOT_FOUND, null);
		}
	}

	@Operation(summary = "Eliminar una Opción del Sistema mediante el ID", description = "Eliminar una Opción del Sistema mediante el ID", tags = { "Opciones" })
	@DeleteMapping("/opciones/{id}")
	//public ResponseEntity<String> deleteOpcion(@PathVariable("id") int id) {
	public ResponseEntity<Object> deleteOpcion(@PathVariable("id") int id) {
		try {
			int result = opcionRepository.deleteById(id);
			if (result == 0) {
				//return new ResponseEntity<>("No se pudo encontrar la opción con el ID =" + id, HttpStatus.NOT_FOUND);
				return ResponseHandler.generateResponse("No se pudo encontrar la opción con el ID =" + id, HttpStatus.NOT_FOUND, null);
			}
			//return new ResponseEntity<>("La Opción fué eliminada exitosamente.", HttpStatus.OK);
			return ResponseHandler.generateResponse("La Opción fué eliminada exitosamente.", HttpStatus.OK, null);
		} catch (Exception e) {
			//return new ResponseEntity<>("No se borró la Opción.", HttpStatus.INTERNAL_SERVER_ERROR);
			return ResponseHandler.generateResponse("Error al eliminar la opción mediante el ID.", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

}

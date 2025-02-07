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
import gob.issste.gys.model.Perfil;
import gob.issste.gys.repository.PerfilRepository;
import gob.issste.gys.response.ResponseHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

//@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class PerfilController {

	Logger logger = LoggerFactory.getLogger(JdbcTemplateDemo01Application.class);

	@Autowired
	PerfilRepository perfilRepository;

	@Operation(summary = "Agrega un nuevo Perfil al Sistema", description = "Agrega un nuevo Perfil al Sistema", tags = { "Perfiles" })
	@PostMapping("/perfiles")
	//public ResponseEntity<String> createPerfil(@RequestBody Perfil perfil) {
	public ResponseEntity<Object> createPerfil(@RequestBody Perfil perfil) {
		try {
			perfilRepository.save(new Perfil(perfil.getDescripcion(), perfil.getId_usuario()));
			//return new ResponseEntity<>("La opcion ha sido creada de manera exitosa", HttpStatus.OK);
			return ResponseHandler.generateResponse("La opcion ha sido creada de manera exitosa", HttpStatus.OK, null);
		} catch (Exception e) {
			//return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			return ResponseHandler.generateResponse("No se pudo crear la opcion de manera exitosa", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Guardar las opciones del Perfil en el Sistema", description = "Guardar las opciones del Perfil en el Sistema", tags = { "Perfiles" })
	@PostMapping("/perfiles/opcionesXperfil")
	//public ResponseEntity<String> saveOpcionesPorPerfil(@RequestBody Perfil perfil) {
	public ResponseEntity<Object> saveOpcionesPorPerfil(@RequestBody Perfil perfil) {
		try {
			if (perfil.getIdPerfil() == 0)
				perfil.setIdPerfil( perfilRepository.save(new Perfil(perfil.getDescripcion(), perfil.getId_usuario())));
			for(Opcion o:perfil.getOpciones()) {
				perfilRepository.addOpcionToPerfil(perfil.getIdPerfil(), o.getIdOpcion());
			}
			//return new ResponseEntity<>("Las opciones y el perfil han sido creada de manera exitosa", HttpStatus.OK);
			return ResponseHandler.generateResponse("Las opciones y el perfil han sido creada de manera exitosa", HttpStatus.OK, null);
		} catch (Exception e) {
			//return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			return ResponseHandler.generateResponse("Error al guardar las opciones del perfil en el Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Actualizar las opciones del Perfil en el Sistema", description = "Actualizar las opciones del Perfil en el Sistema", tags = { "Perfiles" })
	@PutMapping("/perfiles/opcionesXperfil")
	//public ResponseEntity<String> updateOpcionesPorPerfil(@RequestBody Perfil perfil) {
	public ResponseEntity<Object> updateOpcionesPorPerfil(@RequestBody Perfil perfil) {
		try {
			if (perfil.getIdPerfil() != 0) {
				perfilRepository.update(perfil);
				perfilRepository.deleteOptToPerfil(perfil.getIdPerfil());
				for(Opcion o:perfil.getOpciones()) {
					perfilRepository.addOpcionToPerfil(perfil.getIdPerfil(), o.getIdOpcion());
				}
				//return new ResponseEntity<>("Las opciones y el perfil han sido actualizadas de manera exitosa", HttpStatus.OK);
				return ResponseHandler.generateResponse("Las opciones y el perfil han sido actualizadas de manera exitosa", HttpStatus.OK, null);
			} else {
				//return new ResponseEntity<>("No se indicó el ID del Perfil", HttpStatus.NOT_FOUND);
				return ResponseHandler.generateResponse("No se indicó el ID del Perfil", HttpStatus.NOT_FOUND, null);
			}
		} catch (Exception e) {
			//return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			return ResponseHandler.generateResponse("Error al actualizar las opciones del Perfil en el Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Actualizar un Perfil en el Sistema", description = "Actualizar un Perfil en el Sistema", tags = { "Perfiles" })
	@PutMapping("/perfiles/{id}")
	//public ResponseEntity<String> updatePerfil(@PathVariable("id") int id, @RequestBody Perfil perfil) {
	public ResponseEntity<Object> updatePerfil(@PathVariable("id") int id, @RequestBody Perfil perfil) {
		Perfil _perfil = perfilRepository.findById(id);

		if (_perfil != null) {
			_perfil.setIdPerfil(id);
			_perfil.setDescripcion(perfil.getDescripcion());
			_perfil.setId_usuario(perfil.getId_usuario());

			perfilRepository.update(_perfil);
			//return new ResponseEntity<>("El Perfil ha sido modificada de manera exitosa", HttpStatus.OK);
			return ResponseHandler.generateResponse("El Perfil ha sido modificada de manera exitosa", HttpStatus.OK, null);
		} else {
			//return new ResponseEntity<>("No se pudo encontrar el Perfil con ID = " + id, HttpStatus.NOT_FOUND);
			return ResponseHandler.generateResponse("No se pudo encontrar el Perfil con ID = " + id, HttpStatus.NOT_FOUND, null);
		}
	}

	@Operation(summary = "Obtener los Perfiles del Sistema", description = "Obtener los Perfiles del Sistema", tags = { "Perfiles" })
	@GetMapping("/perfiles")
	//public ResponseEntity<List<Perfil>> getPerfiles(
	public ResponseEntity<Object> getPerfiles(
			@Parameter(description = "Parámetro opcional para obtener los perfiles que coincidan con esta descripción", required = false) @RequestParam(required = false) String desc) {
		try {
			List<Perfil> perfiles = new ArrayList<Perfil>();

			if (desc == null)
				perfilRepository.findAll().forEach(perfiles::add);
			else
				perfilRepository.findByDesc(desc).forEach(perfiles::add);

			if (perfiles.isEmpty()) {
				//return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				return ResponseHandler.generateResponse("No se pudieron obtener los Perfiles del Sistema", HttpStatus.NOT_FOUND, null);
			}
			for (Perfil perfil : perfiles) {
				List<Opcion> opciones = perfilRepository.getOpcionesForPerfil(perfil);
				perfil.setOpciones(opciones);
			}
			//return new ResponseEntity<>(perfiles, HttpStatus.OK);
			return ResponseHandler.generateResponse("Se pudieron obtener los Perfiles del Sistema", HttpStatus.OK, perfiles);
		} catch (Exception e) {
			//return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			return ResponseHandler.generateResponse("Error al obtener los Perfiles del Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Obtener un Perfil del Sistema mediante el ID", description = "Obtener un Perfil del Sistema mediante el ID", tags = { "Perfiles" })
	@GetMapping("/perfiles/{id}")
	//public ResponseEntity<Perfil> getPerfilById(@PathVariable("id") int id, @RequestParam(required = true) Boolean conOpciones) {
	public ResponseEntity<Object> getPerfilById(@PathVariable("id") int id, @RequestParam(required = true) Boolean conOpciones) {
		Perfil perfil = perfilRepository.findById(id);

		if (perfil != null) {
			if (conOpciones == true) {
				List<Opcion> opciones = perfilRepository.getOpcionesForPerfil(perfil);
				perfil.setOpciones(opciones);
			}
			//return new ResponseEntity<>(perfil, HttpStatus.OK);
			return ResponseHandler.generateResponse("Se pudo obtener un Perfil del Sistema", HttpStatus.OK, perfil);
		} else {
			//return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return ResponseHandler.generateResponse("No se pudo obtener el Perfil del Sistema", HttpStatus.NOT_FOUND, null);
		}
	}

	@Operation(summary = "Eliminar un Perfil del Sistema mediante el ID", description = "Eliminar un Perfil del Sistema mediante el ID", tags = { "Perfiles" })
	@DeleteMapping("/perfiles/{id}")
	//public ResponseEntity<String> deletePerfil(@PathVariable("id") int id) {
	public ResponseEntity<Object> deletePerfil(@PathVariable("id") int id) {
		String mensaje = "";
		try {
			int result = perfilRepository.deleteById(id);
			if (result == 0) {
				//return new ResponseEntity<>("No se pudo encontrar el Perfil con el ID = " + id, HttpStatus.OK);
				return ResponseHandler.generateResponse("No se pudo encontrar el Perfil con el ID = " + id, HttpStatus.NOT_FOUND, null);
			}
			//return new ResponseEntity<>("El Perfil fué eliminado exitosamente", HttpStatus.OK);
			return ResponseHandler.generateResponse("El Perfil fué eliminado exitosamente", HttpStatus.OK, null);
		} catch (Exception e) {
			if (e.getMessage().contains("-245"))
				System.out.println(e.getCause());
			 mensaje = " debido a que existen opciones asignadas al mismo.";
			return ResponseHandler.generateResponse("No se borró el perfil" + mensaje, HttpStatus.INTERNAL_SERVER_ERROR, e.getCause());
		}
	}

}

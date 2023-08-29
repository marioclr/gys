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

import gob.issste.gys.model.Paga;
import gob.issste.gys.repository.IPagaRepository;
import gob.issste.gys.response.ResponseHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class PagaController {

	@Autowired
	IPagaRepository pagaRepository;

	@Operation(summary = "Crea registro de la fecha de control de pagos de GyS en el Sistema", description = "Crea registro de la fecha de control de pagos de GyS en el Sistema", tags = { "Control de fechas de pago" })
	@PostMapping("/Paga")
	//public ResponseEntity<String> createPaga(@RequestBody Paga paga) {
	public ResponseEntity<Object> createPaga(@RequestBody Paga paga) {
		try {
			pagaRepository.save(new Paga(paga.getFec_pago(), paga.getDescripcion(), paga.getEstatus(), paga.getFec_inicio(), paga.getFec_fin(), paga.getAnio_ejercicio()));
			//return new ResponseEntity<>("El tipo de movimiento ha sido creado de manera exitosa", HttpStatus.CREATED);
			return ResponseHandler.generateResponse("La fecha de control de pagos ha sido creado de manera exitosa", HttpStatus.CREATED, null);
		} catch (Exception e) {
			//return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			return ResponseHandler.generateResponse("Error al obtener la fecha de control de pagos del Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}


	@Operation(summary = "Actualiza la información de la fecha de control de pagos de GyS del Sistema", description = "Actualiza la información de la fecha de control de pagos de GyS del Sistema", tags = { "Control de fechas de pago" })
	@PutMapping("/Paga/{id}")
	//public ResponseEntity<String> updatePaga(@PathVariable("id") Integer id, @RequestBody Paga paga) {
	public ResponseEntity<Object> updatePaga(@PathVariable("id") Integer id, @RequestBody Paga paga) {
		Paga _paga = pagaRepository.findById(id);

		if (_paga != null) {
			_paga.setDescripcion(paga.getDescripcion());
			_paga.setEstatus(paga.getEstatus());
			_paga.setFec_inicio(paga.getFec_inicio());
			_paga.setFec_fin(paga.getFec_fin());
			_paga.setAnio_ejercicio(paga.getAnio_ejercicio());

			pagaRepository.update(_paga);
			//return new ResponseEntity<>("La fecha de control de pagos de GyS ha sido modificado de manera exitosa", HttpStatus.OK);
			return ResponseHandler.generateResponse("La fecha de control de pagos de GyS ha sido modificado de manera exitosa", HttpStatus.OK, null);
		} else {
			//return new ResponseEntity<>("No se pudo encontrar el tipo de movimiento ha sido con id=" + id, HttpStatus.NOT_FOUND);
			return ResponseHandler.generateResponse("No se pudo encontrar fecha de control de pagos con id=" + id, HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}


	@Operation(summary = "Obtiene información de fechas de control de pagos de GyS en el Sistema", description = "Obtiene información de fechas de control de pagos de GyS en el Sistema", tags = { "Control de fechas de pago" })
	@GetMapping("/Paga")
	//public ResponseEntity<List<Paga>> getPagas(
	public ResponseEntity<Object> getPagas(
			@Parameter(description = "Descripcion de la fecha de control de pagos de la que se desea obtener la información", required = false) @RequestParam(required = false) String desc) {
		try {
			List<Paga> pagas = new ArrayList<Paga>();

			if (desc == null)
				pagaRepository.findAll().forEach(pagas::add);
			else
				pagaRepository.findByDesc(desc).forEach(pagas::add);

			if (pagas.isEmpty()) {
				//return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				return ResponseHandler.generateResponse("No se pudo obtener la información de fechas de control de pagos en el Sistema", HttpStatus.NOT_FOUND, null);
			}

			//return new ResponseEntity<>(pagas, HttpStatus.OK);
			return ResponseHandler.generateResponse("Se pudo obtener la información de fechas de control de pagos en el Sistema de manera exitosa", HttpStatus.OK, pagas);
		} catch (Exception e) {
			//return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			return ResponseHandler.generateResponse("Error al obtener la información de fechas de control de pagos en el Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}


	@Operation(summary = "Obtiene información de una fecha de control de pagos de GyS en el Sistema", description = "Obtiene información de una fecha de control de pagos de GyS en el Sistema", tags = { "Control de fechas de pago" })
	@GetMapping("/Paga/{id}")
	//public ResponseEntity<Paga> getPagaPorId(
	public ResponseEntity<Object> getPagaPorId(
			@Parameter(description = "ID del tipo de movimiento del que se desea obtener la información", required = true) @PathVariable("id") Integer id) {

		Paga paga = pagaRepository.findById(id);

		if (paga != null) {
			//return new ResponseEntity<>(paga, HttpStatus.OK);
			return ResponseHandler.generateResponse("Se pudo obtener la información de fecha de control de pagos en el Sistema de manera exitosa", HttpStatus.OK, paga);
		} else {
			//return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return ResponseHandler.generateResponse("No se pudo obtener la información de fecha de control de pagos en el Sistema", HttpStatus.NOT_FOUND, null);
		}
	}


	@Operation(summary = "Elimina información de una fecha de control de pagos de GyS en el Sistema", description = "Elimina información de una fecha de control de pagos de GyS en el Sistema", tags = { "Control de fechas de pago" })
	@DeleteMapping("/Paga/{id}")
	//public ResponseEntity<String> deletePaga(@PathVariable("id") Integer id) {
	public ResponseEntity<Object> deletePaga(@PathVariable("id") Integer id) {
		try {
			int result = pagaRepository.deleteById(id);
			if (result == 0) {
				//return new ResponseEntity<>("No se pudo encontrar la fecha de control de pagos de GyS con el ID =" + id, HttpStatus.NOT_FOUND);
				return ResponseHandler.generateResponse("No se pudo encontrar la fecha de control de pagos de GyS con el ID =" + id, HttpStatus.NOT_FOUND, null);
			}
			//return new ResponseEntity<>("La fecha de control de pagos de GyS fué eliminado exitósamente.", HttpStatus.OK);
			return ResponseHandler.generateResponse("La fecha de control de pagos de GyS fué eliminado exitósamente.", HttpStatus.OK, null);
		} catch (Exception e) {
			//return new ResponseEntity<>("No se borró la fecha de control de pagos de GyS.", HttpStatus.INTERNAL_SERVER_ERROR);
			return ResponseHandler.generateResponse("Error al obtener la información de la fecha de control de pagos en el Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Actualiza la información de la fecha de control de pagos de GyS del Sistema", description = "Actualiza la información de la fecha de control de pagos de GyS del Sistema", tags = { "Control de fechas de pago" })
	@PutMapping("/Paga/activate/{id}")
	//public ResponseEntity<String> activatePaga(@PathVariable("id") Integer id, @RequestParam boolean activate) {
	public ResponseEntity<Object> activatePaga(@PathVariable("id") Integer id, @RequestParam boolean activate) {
		Paga _paga = pagaRepository.findById(id);
		int valor;

		if (activate) 
			valor=1;
		else
			valor=0;

		if (_paga != null) {
			_paga.setEstatus(valor);

			pagaRepository.activate(_paga);
			//return new ResponseEntity<>("La fecha de control de pagos de GyS ha sido modificado de manera exitosa", HttpStatus.OK);
			return ResponseHandler.generateResponse("La fecha de control de pagos de GyS ha sido modificado de manera exitosa", HttpStatus.OK, null);
		} else {
			//return new ResponseEntity<>("No se pudo encontrar el tipo de movimiento ha sido con id=" + id, HttpStatus.NOT_FOUND);
			return ResponseHandler.generateResponse("No se pudo encontrar la fecha de control de pagos de GyS con el ID =" + id, HttpStatus.NOT_FOUND, null);
		}
	}

	@Operation(summary = "Obtiene información de fechas de control de pagos de GyS activas en el Sistema", description = "Obtiene información de fechas de control de pagos de GyS activas en el Sistema", tags = { "Control de fechas de pago" })
	@GetMapping("/Paga/activas")
	//public ResponseEntity<List<Paga>> getActivePagas() {
	public ResponseEntity<Object> getActivePagas() {

		try {
			List<Paga> pagas = pagaRepository.findActivePagas();

			if (pagas.isEmpty()) {
				//return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				return ResponseHandler.generateResponse("No se pudo obtener la información de fechas de control de pagos de GyS activas en el Sistema", HttpStatus.NOT_FOUND, null);
			}

			//return new ResponseEntity<>(pagas, HttpStatus.OK);
			return ResponseHandler.generateResponse("Se obtuvo la información de fechas de control de pagos de GyS activas en el Sistema", HttpStatus.OK, pagas);
		} catch (Exception e) {
			//return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			return ResponseHandler.generateResponse("Error al obtener la información de fechas de control de pagos de GyS activas en el Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}	
	}
}

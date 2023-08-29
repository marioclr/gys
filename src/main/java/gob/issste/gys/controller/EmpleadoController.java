package gob.issste.gys.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gob.issste.gys.model.DatosEmpleado;
import gob.issste.gys.model.Empleado;
import gob.issste.gys.repository.IEmpleadoRepository;
import gob.issste.gys.response.ResponseHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
@Tag(name = "Empleados", description = "API de Empleados consultados de Meta4")
public class EmpleadoController {

	@Autowired
	IEmpleadoRepository empleadoRepository;

	@Operation(summary = "Obtener porcentaje de riesgos profesionales del empleado en el Sistema", description = "Obtener porcentaje de riesgos profesionales del empleado en el Sistema", tags = { "Empleados" })
	@GetMapping("/empleados/datos")
	public ResponseEntity<Object> getEmpleado(
			@Parameter(description = "Número de empleado para obtener los datos del empleado", required = true) @RequestParam(required = true) String idEmpleado,
			@Parameter(description = "Fecha en la que se desea obtener los datos del empleado", required = false) @RequestParam(required = false) String fecha,
			@Parameter(description = "Delegación del Usuario para validar si el Empleado pertenece a su Delegación", required = false) @RequestParam(required = false) String idDelegacion) {

		if (fecha == null) {
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			fecha = formatter.format(new Date());
		}

		try {
			DatosEmpleado empleado = null;

			empleado = empleadoRepository.getDatosEmpleado(fecha, idEmpleado);

//			if (idDelegacion != null) {
//				if(!idDelegacion.equals(empleado.getId_delegacion()))
//				//return new ResponseEntity<>(HttpStatus.CONFLICT);
//					return ResponseHandler.generateResponse("El empleado consultado pertenece a una Delegación diferente a la del Usuario", HttpStatus.CONFLICT, null);
//			}

			if (empleado == null) {
				//return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				return ResponseHandler.generateResponse("El empleado consultado pertenece a una Delegación diferente a la del Usuario", HttpStatus.NO_CONTENT, null);
			}

			//return new ResponseEntity<>(empleado, HttpStatus.OK);
			return ResponseHandler.generateResponse("Se encontro el empleado y pertenece a la Delegación del Usuario", HttpStatus.OK, empleado);
		} catch (EmptyResultDataAccessException e) {
			//return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			return ResponseHandler.generateResponse("No se encontró el empleado indicado", HttpStatus.NO_CONTENT, null);
		} catch (Exception e) {
			//return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			return ResponseHandler.generateResponse("Error al buscar al empleado indicado", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Obtener porcentaje de riesgos profesionales del empleado en el Sistema", description = "Obtener porcentaje de riesgos profesionales del empleado en el Sistema", tags = { "Empleados" })
	@GetMapping("/guardias/riesgos")
	//public ResponseEntity<Integer> obtenerRiesgos(
	public ResponseEntity<Object> obtenerRiesgos(
			@Parameter(description = "Clave de empleado a consultar", required = true) @RequestParam(required = true) String empleado,
			@Parameter(description = "Fecha de quincena para consultar", required = true) @RequestParam(required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date quincena) {
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String strQuincena = dateFormat.format(quincena);
			int riesgos = empleadoRepository.ConsultaRiesgosEmp(empleado, strQuincena);
			//return new ResponseEntity<>(riesgos, HttpStatus.CREATED);
			return ResponseHandler.generateResponse("Se encontró el porcentaje de riesgos profesionales del empleado en el Sistema", HttpStatus.OK, riesgos);
		} catch (Exception e) {
			//return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			return ResponseHandler.generateResponse("Error al obtener porcentaje de riesgos profesionales del empleado en el Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Obtener el listado de empleados", description = "Obtener el listado de empleados", tags = { "Empleados" })
	@GetMapping("/empleados")
	//public ResponseEntity<List<Empleado>> getAllEmpleados(@RequestParam(required = false) String nombre) {
	public ResponseEntity<Object> getAllEmpleados(@RequestParam(required = false) String nombre) {
		try {
			List<Empleado> empleados = new ArrayList<Empleado>();

			if (nombre == null)
				empleadoRepository.findAll().forEach(empleados::add);
			else
				empleadoRepository.findByNombre(nombre).forEach(empleados::add);

			if (empleados.isEmpty()) {
				//return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				return ResponseHandler.generateResponse("No exixten registros de empleados en el sistema", HttpStatus.NO_CONTENT, null);
			}

			//return new ResponseEntity<>(empleados, HttpStatus.OK);
			return ResponseHandler.generateResponse("Se encontró el listado de empleados", HttpStatus.OK, empleados);
		} catch (Exception e) {
			//return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			return ResponseHandler.generateResponse("Error al obtener el listado de empleados", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Obtener un empleado mediante su ID", description = "Obtener un empleado mediante su ID", tags = { "Empleados" })
	@GetMapping("/empleados/{id}")
	//public ResponseEntity<Empleado> getOpcionById(@PathVariable("id") String id) {
	public ResponseEntity<Object> getEmpleadoById(@PathVariable("id") String id) {
		Empleado empleado= empleadoRepository.findById(id);

		if (empleado != null) {
			//return new ResponseEntity<>(empleado, HttpStatus.OK);
			return ResponseHandler.generateResponse("Se encontró el empleado mediante su ID", HttpStatus.OK, empleado);
		} else {
			//return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return ResponseHandler.generateResponse("No se encontró el empleado mediante su ID", HttpStatus.NO_CONTENT, null);
		}
	}
}

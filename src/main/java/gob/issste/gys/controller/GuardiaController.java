package gob.issste.gys.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gob.issste.gys.JdbcTemplateDemo01Application;
import gob.issste.gys.model.DatosGuardia;
import gob.issste.gys.model.Presupuesto;
import gob.issste.gys.repository.GuardiaRepository;
import gob.issste.gys.repository.IPagaRepository;
import gob.issste.gys.repository.IPresupuestoRepository;
import gob.issste.gys.repository.UsuarioRepository;
import gob.issste.gys.response.ResponseHandler;
import gob.issste.gys.service.IGuardiaInternaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
@Tag(name = "Guardia", description = "API de gestión de Guardias")
public class GuardiaController {

	Logger logger = LoggerFactory.getLogger(JdbcTemplateDemo01Application.class);

	@Autowired
	GuardiaRepository guardiaRepository;

	@Autowired
	IGuardiaInternaService guardiaInternaService;

	@Autowired
	IPresupuestoRepository presupuestoRepository;

	@Autowired
	UsuarioRepository usuarioRepository;

	@Autowired
	IPagaRepository pagaRepository;

	@Operation(summary = "Obtener el importe de una guardia conforme a los criterios establecidos", description = "Agrega un nuevo presupuesto al Sistema", tags = { "Guardia" })
	@GetMapping("/guardias/importe")
	//public ResponseEntity<BigDecimal> getImporteGuardia(
	public ResponseEntity<Object> getImporteGuardia(
			@Parameter(description = "Clave del tipo de tabulador para obtener el importe de la guardia", required = true) @RequestParam(required = true) String tipo_tabulador, 
			@Parameter(description = "Clave de la zona para obtener el importe de la guardia", required = true) @RequestParam(required = true) String zona, 
			@Parameter(description = "Clave del nivel para obtener el importe de la guardia", required = true) @RequestParam(required = true) String nivel, 
			@Parameter(description = "Clave del subnivel para obtener el importe de la guardia", required = true) @RequestParam(required = true) String subnivel,
			@Parameter(description = "Clave del tipo de jornada para obtener el importe de la guardia", required = true) @RequestParam(required = true) String tipo_jornada, 
			@Parameter(description = "Tipo de riesgos para obtener el importe de la guardia", required = false) @RequestParam(required = false) Integer riesgos, 
			@Parameter(description = "Tipo de guardia para calcular el importe de la guardia", required = true) @RequestParam(required = true) String tipo, 
			@Parameter(description = "Número de horas para obtener el importe de la guardia", required = true) @RequestParam(required = true) Double horas, 
			@Parameter(description = "Fecha de quincena para el cálculo del importe de la guardia", required = true) @RequestParam(required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date quincena ) 
	{
		if (riesgos == null) riesgos = Integer.valueOf(0);
		Double importe = 0.0;
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String strDate = dateFormat.format(quincena);
			importe = guardiaInternaService.CalculaImporteGuardia( tipo_tabulador, zona, nivel, subnivel, tipo_jornada, riesgos, tipo, horas, strDate );
			BigDecimal importe2 = new BigDecimal(importe).setScale(2, RoundingMode.HALF_UP);
			//return new ResponseEntity<>(importe2, HttpStatus.OK);
			return ResponseHandler.generateResponse("Se encontró el importe de la guardia conforme a los criterios establecidos", HttpStatus.OK, importe2);
		} catch (SQLException e) {
			logger.info(e.toString());
			return ResponseHandler.generateResponse(e.toString(), HttpStatus.NO_CONTENT, null);
		} catch (Exception e) {
			logger.info(e.toString());
			return ResponseHandler.generateResponse("Error al consultar el importe de la guardia conforme a los criterios establecidos", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Obtener los registros de guardias del empleado en el Sistema", description = "Obtener los registros de guardias del empleado en el Sistema", tags = { "Guardia" })
	@GetMapping("/guardias")
	//public ResponseEntity<List<DatosGuardia>> getGuardiasEmpleado(
	public ResponseEntity<Object> getGuardiasEmpleado(
			@Parameter(description = "Número de empleado para obtener las guardias del empleado", required = true) @RequestParam(required = true) String claveEmpleado,
			@Parameter(description = "Tipo para obtener las guardias del empleado (internas o externas)", required = true) @RequestParam(required = true) String tipoGuardia ) {

		try {
			List<DatosGuardia> guardias = new ArrayList<DatosGuardia>();

			if (tipoGuardia.equals(String.valueOf("I"))) {
				guardias = guardiaRepository.ConsultaGuardiasInternas(claveEmpleado);
			} else {
				guardias = guardiaRepository.ConsultaGuardiasExternas(claveEmpleado);
			}

			if (guardias.isEmpty()) {
				//return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				return ResponseHandler.generateResponse("No exixten guardias para la clave y el tipo asignado", HttpStatus.NO_CONTENT, null);
			}

			//return new ResponseEntity<>(guardias, HttpStatus.OK);
			return ResponseHandler.generateResponse("Se encontraron guardias para la clave y el tipo asignado", HttpStatus.OK, guardias);

		} catch (Exception e) {
			logger.info(e.toString());
			//return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			return ResponseHandler.generateResponse("Error al consultar guardias para la clave y el tipo asignado", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Obtener una Guardia del Sistema mediante el ID", description = "Obtener una Guardia del Sistema mediante el ID", tags = { "Guardia" })
	@GetMapping("/guardiaPorId")
	//public ResponseEntity<DatosGuardia> getGuardiaById(
	public ResponseEntity<Object> getGuardiaById(
			@Parameter(description = "ID de la guardia del empleado", required = true) @RequestParam(required = true) Integer idGuardia,
			@Parameter(description = "Tipo para obtener las guardidas del empleado (internas o externas)", required = true) @RequestParam(required = true) String tipoGuardia) {

		DatosGuardia guardia = null;

		if (tipoGuardia.equals(String.valueOf("I")))
			guardia = guardiaRepository.findById(idGuardia);
		else
			guardia = guardiaRepository.findByIdExterno(idGuardia);


		if (guardia != null) {
			//return new ResponseEntity<>(guardia, HttpStatus.OK);
			return ResponseHandler.generateResponse("Se encontró la guardia para la clave y el tipo asignado", HttpStatus.OK, guardia);
		} else {
			//return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return ResponseHandler.generateResponse("Error al consultar la guardia con las condiciones indicadas", HttpStatus.NOT_FOUND, null);
		}
	}

	@Operation(summary = "Obtener el saldo utilizado por registro de guardias para una delegación en un año determinado", description = "Obtener el saldo utilizado por registro de guardias para una delegación en un año determinado", tags = { "Guardia" })
	@GetMapping("/guardias/saldo")
	public ResponseEntity<Object> getSaldoGuardia(
			@Parameter(description = "ID de la Delegación que se consulta el saldo utilizado", required = true) @RequestParam(required = true) String idDelegacion,
			@Parameter(description = "Año del ejercicio del que se consulta el saldo utilizado", required = true) @RequestParam(required = true) Integer anio_ejercicio,
			@Parameter(description = "Tipo para obtener las guardidas del empleado (internas o externas)", required = true) @RequestParam(required = true) String tipoGuardia,
			@Parameter(description = "ID del Centro de Trabajo del que se consulta el saldo utilizado", required = false) @RequestParam(required = false) String idCentroTrab) {

		double saldo=0;

		if (tipoGuardia.equals(String.valueOf("GI"))) {
			if (idCentroTrab == null) {
				saldo = guardiaRepository.ObtenerSaldoUtilizado(idDelegacion, anio_ejercicio);
			} else {
				saldo = guardiaRepository.ObtenerSaldoUtilizado_ct(idCentroTrab, anio_ejercicio);
			}
		} else {
			if (idCentroTrab == null) {					
				saldo = guardiaRepository.ObtenerSaldoUtilizadoExt(idDelegacion, anio_ejercicio);
			} else {
				saldo = guardiaRepository.ObtenerSaldoUtilizadoExt_ct(idCentroTrab, anio_ejercicio);
			}
		}
		return ResponseHandler.generateResponse("Se obtuvo el saldo utilizado para guardias para las condiciones indicadas", HttpStatus.OK, saldo);
	}

	@Operation(summary = "Agrega un nuevo registro de guardia al Sistema", description = "Agrega un nuevo registro de guardia al Sistema", tags = { "Guardia" })
	@PostMapping("/guardias")
	//public ResponseEntity<String> agregarGuardia(
	public ResponseEntity<Object> agregarGuardia(	
			@Parameter(description = "Objeto de la guardia a crearse en el Sistema") @RequestBody DatosGuardia guardia) {
		try {

			int id = 0, idTipoPresup, anio;
			double saldo_utilizado=0, saldo=0;
			Presupuesto presup;

			//String idDeleg = usuarioRepository.findByName(guardia.getId_usuario()).getDelegacion().getId_div_geografica();
			String idCentroTrab = guardia.getId_centro_trabajo();
			String fec_pago = guardia.getFec_paga();

			if (guardia.getTipo_guardia().equals(String.valueOf("GI"))) {
				idTipoPresup = 1;
			} else {
				idTipoPresup = 2;
			}

			try {
				anio = pagaRepository.findByFecha(fec_pago).getAnio_ejercicio();
			} catch (EmptyResultDataAccessException e) {
				return ResponseHandler.generateResponse("No existe la fecha de pago indicada", HttpStatus.INTERNAL_SERVER_ERROR, null);
			}
			try {			
				presup = presupuestoRepository.getElementByType_ct(idCentroTrab, idTipoPresup, anio);
			} catch (EmptyResultDataAccessException e) {
				return ResponseHandler.generateResponse("No existe presupuesto registrado para realizar este tipo de movimiento", HttpStatus.INTERNAL_SERVER_ERROR, null);
			}

			saldo = (presup != null) ? presup.getSaldo(): 0; 

			if (guardia.getTipo_guardia().equals(String.valueOf("GI"))) {
				saldo_utilizado = guardiaRepository.ObtenerSaldoUtilizado_ct(idCentroTrab, presup.getAnio());
			} else {
				saldo_utilizado = guardiaRepository.ObtenerSaldoUtilizadoExt_ct(idCentroTrab, presup.getAnio());
			}

			//saldo_utilizado = guardiaRepository.ObtenerSaldoUtilizado(idDeleg, presup.getAnio()) + guardiaRepository.ObtenerSaldoUtilizadoExt(idDeleg, presup.getAnio());

			double importe = guardiaInternaService.CalculaImporteGuardia(guardia.getId_tipo_tabulador(), guardia.getId_zona(), 
					guardia.getId_nivel(), guardia.getId_sub_nivel(), guardia.getId_tipo_jornada(), guardia.getRiesgos(), 
					guardia.getTipo_guardia(), guardia.getHoras(), guardia.getFec_paga());

			if (importe + saldo_utilizado <= saldo) {

				id = guardiaInternaService.guardarGuardia(guardia, importe);
			} else {
				//return new ResponseEntity<>("No existe presupuesto suficiente para realizar este tipo de registro", HttpStatus.INTERNAL_SERVER_ERROR);
				return ResponseHandler.generateResponse("No existe presupuesto suficiente para realizar este tipo de registro", HttpStatus.INTERNAL_SERVER_ERROR, null);
			}

			//return new ResponseEntity<>("El registro de guardia ha sido guardado de manera exitosa, con ID = " + id, HttpStatus.CREATED);
			return ResponseHandler.generateResponse("El registro de guardia ha sido guardado de manera exitosa, con ID = " + id, HttpStatus.CREATED, null);
		} catch (Exception e) {
			//return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			return ResponseHandler.generateResponse("Error al realizar el registro de guardia indicado", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Actualizar importes de guardias en el Sistema", description = "Actualizar importes de guardias en el Sistema", tags = { "Guardia" })
	@PutMapping("/guardias/actualiza")
	//public ResponseEntity<String> actualizaImportes(
	public ResponseEntity<Object> actualizaImportes(
			@Parameter(description = "Fecha de quincena para el cálculo del importe de la guardia", required = true) @RequestParam(required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date quincena,
			@Parameter(description = "Tipo para obtener las guardidas del empleado ('I': Internas o 'E': Externas)", required = true) @RequestParam(required = true) String tipoGuardia ) {
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String strQuincena = dateFormat.format(quincena);
			guardiaInternaService.actualizaImportesGuardias(strQuincena, tipoGuardia);
			//return new ResponseEntity<>("Los importes de las guardias se actualizaron de manera exitósa.", HttpStatus.CREATED);
			return ResponseHandler.generateResponse("Los importes de las guardias se actualizaron de manera exitósa.", HttpStatus.CREATED, null);
		} catch (Exception e) {
			//return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			return ResponseHandler.generateResponse("Error al realizar la actualización del registro de guardia indicado", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Actualizar importes de guardias en el Sistema", description = "Actualizar importes de guardias en el Sistema", tags = { "Guardia" })
	@PutMapping("/guardias/actualiza2")
	//public ResponseEntity<String> actualizaImportes2(
	public ResponseEntity<Object> actualizaImportes2(
			@Parameter(description = "Fecha de quincena para el cálculo del importe de la Suplencia", required = true) @RequestParam(required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date quincena,
			@Parameter(description = "Tipo para obtener las guardidas del empleado ('I': Internas o 'E': Externas)", required = true) @RequestParam(required = true) String tipoSuplencia ) {
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String strQuincena = dateFormat.format(quincena);
			guardiaInternaService.actualizaImportesGuardias2(strQuincena, tipoSuplencia);
			//return new ResponseEntity<>("Los importes de las guardidas se actualizaron de manera exitósa", HttpStatus.CREATED);
			return ResponseHandler.generateResponse("Los importes de las guardidas se actualizaron de manera exitósa", HttpStatus.OK, null);
		} catch (Exception e) {
			//return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			return ResponseHandler.generateResponse("Error al actualizar importes de guardias en el Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Actualiza un registro de guardia en el Sistema", description = "Actualiza un registro de guardia en el Sistema", tags = { "Guardia" })
	@PutMapping("/guardias")
	//public ResponseEntity<String> actualizarGuardia(
	public ResponseEntity<Object> actualizarGuardia(
			@Parameter(description = "Objeto de la guardia a actualizarse en el Sistema") @RequestBody DatosGuardia guardia) {
		try {

			double importe = guardiaInternaService.CalculaImporteGuardia(guardia.getId_tipo_tabulador(), guardia.getId_zona(), 
					guardia.getId_nivel(), guardia.getId_sub_nivel(), guardia.getId_tipo_jornada(), guardia.getRiesgos(), 
					guardia.getTipo_guardia(), guardia.getHoras(), guardia.getFec_paga());

			guardiaInternaService.actualizaGuardia(guardia, importe);

			//return new ResponseEntity<>("El registro de guardia ha sido actualizado de manera exitosa", HttpStatus.CREATED);
			return ResponseHandler.generateResponse("Los importes de las guardias se actualizaron de manera exitósa.", HttpStatus.CREATED, null);
		} catch (Exception e) {
			//return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			return ResponseHandler.generateResponse("Error al realizar la actualización del registro de guardia indicado", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Eliminar un registro de guardia del Sistema", description = "Eliminar un registro de guardia del Sistema", tags = { "Guardia" })
	@DeleteMapping("/guardias")
	//public ResponseEntity<String> eliminarGuardia(
	public ResponseEntity<Object> eliminarGuardia(
			@Parameter(description = "Número de ID para obtener la guardia del empleado", required = true) @RequestParam(required = true) Integer idGuardia,
			@Parameter(description = "Tipo para obtener la guardia del empleado (I-Interna o E-Externa)", required = true) @RequestParam(required = true) String tipoGuardia) {
		try {
			guardiaInternaService.eliminarGuardia(idGuardia, tipoGuardia);
			//return new ResponseEntity<>("El un registro de guardia fué eliminado exitosamente.", HttpStatus.OK);
			return ResponseHandler.generateResponse("El un registro de guardia fué eliminado exitosamente.", HttpStatus.OK, null);
		} catch (Exception e) {
			//return new ResponseEntity<>("No se borró el registro de guardia.", HttpStatus.INTERNAL_SERVER_ERROR);
			return ResponseHandler.generateResponse("No se borró satisfactoriamente el registro de la guardia.", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Obtener los registros de guardias del empleado en el Sistema", description = "Obtener los registros de guardias del empleado en el Sistema", tags = { "Guardia" })
	@GetMapping("/guardias/consulta")
	//public ResponseEntity<List<DatosGuardia>> getDynamicGuardias(
	public ResponseEntity<Object> getDynamicGuardias(
			@Parameter(description = "Fecha de quincena para la consulta de guardias", required = false) @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date quincena,
			@Parameter(description = "Tipo para obtener las guardias (I-Internas o E-Externas)", required = true) @RequestParam(required = true) String tipoGuardia,
			@Parameter(description = "ID de empleado o RFC para obtener las guardias", required = false) @RequestParam(required = false) String claveEmpleado,
			@Parameter(description = "Importe mínimo para obtener las guardias", required = false) @RequestParam(required = false) Double importe_min,
			@Parameter(description = "Importe máximo para obtener las guardias", required = false) @RequestParam(required = false) Double importe_max,
			@Parameter(description = "ID de la delegación para obtener las guardias", required = false) @RequestParam(required = false) String idDelegacion,
			@Parameter(description = "ID del centro de trabajo para obtener las guardias", required = false) @RequestParam(required = false) String idCentroTrab,
			@Parameter(description = "Clave del servicio para obtener las guardias", required = false) @RequestParam(required = false) String claveServicio,
			@Parameter(description = "Clave del puesto para obtener las guardias", required = false) @RequestParam(required = false) String puesto ) {

		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String strQuincena = null;
			
			if (quincena != null)
				strQuincena = dateFormat.format(quincena);

			List<DatosGuardia> guardias = new ArrayList<DatosGuardia>();

			guardias = guardiaRepository.ConsultaDynamicGuardias(strQuincena, tipoGuardia, claveEmpleado, importe_min, importe_max, 
																idDelegacion, idCentroTrab, claveServicio, puesto);

			if (guardias.isEmpty()) {
				//return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				return ResponseHandler.generateResponse("No se encontraron guardias al consultar bajo las condiciones indicadas", HttpStatus.NOT_FOUND, null);
			}

			//return new ResponseEntity<>(guardias, HttpStatus.OK);
			return ResponseHandler.generateResponse("Se encontraron guardias al consultar bajo las condiciones indicadas", HttpStatus.OK, guardias);

		} catch (Exception e) {
			logger.info(e.toString());
			//return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			return ResponseHandler.generateResponse("Error al consultar guardias bajo las condiciones indicadas.", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

}
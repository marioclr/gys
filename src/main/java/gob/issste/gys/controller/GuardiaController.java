package gob.issste.gys.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import gob.issste.gys.model.Programatica;
import gob.issste.gys.service.ParamsValidatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import gob.issste.gys.model.Paga;
import gob.issste.gys.model.Presupuesto;
import gob.issste.gys.repository.GuardiaRepository;
import gob.issste.gys.repository.IPagaRepository;
import gob.issste.gys.repository.IPresupuestoRepository;
import gob.issste.gys.repository.UsuarioRepository;
import gob.issste.gys.response.ResponseHandler;
import gob.issste.gys.service.IGuardiaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

//@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
@Tag(name = "Guardia", description = "API de gestión de Guardias")
public class GuardiaController {

	Logger logger = LoggerFactory.getLogger(JdbcTemplateDemo01Application.class);

	ParamsValidatorService paramsValidatorService = new ParamsValidatorService();

	@Autowired
	GuardiaRepository guardiaRepository;

	@Autowired
	IGuardiaService guardiaService;

	@Autowired
	IPresupuestoRepository presupuestoRepository;

	@Autowired
	UsuarioRepository usuarioRepository;

	@Autowired
	IPagaRepository pagaRepository;

	@Operation(summary = "Obtener el importe de una guardia conforme a los criterios establecidos", description = "Agrega un nuevo presupuesto al Sistema", tags = {
			"Guardia" })
	@GetMapping("/guardias/importe")
	public ResponseEntity<Object> getImporteGuardia(
			@Parameter(description = "Clave del tipo de tabulador para obtener el importe de la guardia", required = true) @RequestParam(required = true) String tipo_tabulador,
			@Parameter(description = "Clave de la zona para obtener el importe de la guardia", required = true) @RequestParam(required = true) String zona,
			@Parameter(description = "Clave del nivel para obtener el importe de la guardia", required = true) @RequestParam(required = true) String nivel,
			@Parameter(description = "Clave del subnivel para obtener el importe de la guardia", required = true) @RequestParam(required = true) String subnivel,
			@Parameter(description = "Clave del tipo de jornada para obtener el importe de la guardia", required = true) @RequestParam(required = true) String tipo_jornada,
			@Parameter(description = "Tipo de riesgos para obtener el importe de la guardia", required = false) @RequestParam(required = false) Integer riesgos,
			@Parameter(description = "Tipo de guardia para calcular el importe de la guardia", required = true) @RequestParam(required = true) String tipo,
			@Parameter(description = "Número de horas para obtener el importe de la guardia", required = true) @RequestParam(required = true) Double horas,
			@Parameter(description = "Fecha de quincena para el cálculo del importe de la guardia", required = true) @RequestParam(required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date quincena) {
		if (riesgos == null)
			riesgos = Integer.valueOf(0);
		Double importe = 0.0;
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String strDate = dateFormat.format(quincena);
			importe = guardiaService.CalculaImporteGuardia(tipo_tabulador, zona, nivel, subnivel, tipo_jornada, riesgos,
					tipo, horas, strDate);
			BigDecimal importe2 = new BigDecimal(importe).setScale(2, RoundingMode.HALF_UP);
			return ResponseHandler.generateResponse(
					"Se encontró el importe de la guardia conforme a los criterios establecidos", HttpStatus.OK,
					importe2);
		} catch (SQLException e) {
			logger.info(e.toString());
			return ResponseHandler.generateResponse(e.toString(), HttpStatus.NOT_FOUND, null);
		} catch (Exception e) {
			logger.info(e.toString());
			return ResponseHandler.generateResponse(
					"Error al consultar el importe de la guardia conforme a los criterios establecidos",
					HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Obtener los registros de guardias del empleado en el Sistema", description = "Obtener los registros de guardias del empleado en el Sistema", tags = {
			"Guardia" })
	@GetMapping("/guardias")
	public ResponseEntity<Object> getGuardiasEmpleado(
			@Parameter(description = "Número de empleado para obtener las guardias del empleado", required = true) @RequestParam(required = true) String claveEmpleado,
			@Parameter(description = "Tipo para obtener las guardias del empleado ('GI': Internas o 'GE': Externas)", required = true) @RequestParam(required = true) String tipoGuardia) {

		try {

			List<DatosGuardia> guardias = new ArrayList<DatosGuardia>();

			switch (tipoGuardia) {

				case "GI":
					guardias = guardiaRepository.ConsultaGuardiasInternas(claveEmpleado);
					break;
	
				case "GE":
					guardias = guardiaRepository.ConsultaGuardiasExternas(claveEmpleado);
					break;
	
				default:
					return ResponseHandler.generateResponse("No se indicó el tipo de guardia correctamente ('GI': Internas o 'GE': Externas)", HttpStatus.INTERNAL_SERVER_ERROR, null);

			}

			if (guardias.isEmpty()) {
				return ResponseHandler.generateResponse("No existen guardias para la clave y el tipo asignado",
						HttpStatus.NOT_FOUND, null);
			}

			return ResponseHandler.generateResponse("Se encontraron guardias para la clave y el tipo asignado",
					HttpStatus.OK, guardias);

		} catch (Exception e) {
			logger.info(e.toString());
			return ResponseHandler.generateResponse("Error al consultar guardias para la clave y el tipo asignado",
					HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Obtener una Guardia del Sistema mediante el ID", description = "Obtener una Guardia del Sistema mediante el ID", tags = {
			"Guardia" })
	@GetMapping("/guardiaPorId")
	public ResponseEntity<Object> getGuardiaById(
			@Parameter(description = "ID de la guardia del empleado", required = true) @RequestParam(required = true) Integer idGuardia,
			@Parameter(description = "Tipo para obtener las guardidas del empleado ('GI': Internas o 'GE': Externas)", required = true) @RequestParam(required = true) String tipoGuardia) {

		DatosGuardia guardia = null;

		switch (tipoGuardia) {

			case "GI":
				guardia = guardiaRepository.findById(idGuardia);
				break;

			case "GE":
				guardia = guardiaRepository.findByIdExterno(idGuardia);
				break;

			default:
				return ResponseHandler.generateResponse("No se indicó el tipo de guardia correctamente ('GI': Internas o 'GE': Externas)", HttpStatus.INTERNAL_SERVER_ERROR, null);

		}

		if (guardia != null) {
			return ResponseHandler.generateResponse("Se encontró la guardia para la clave y el tipo asignado",
					HttpStatus.OK, guardia);
		} else {
			return ResponseHandler.generateResponse("Error al consultar la guardia con las condiciones indicadas",
					HttpStatus.NOT_FOUND, null);
		}
	}

	@Operation(summary = "Obtener el saldo utilizado por registro de guardias para una delegación en un año determinado", description = "Obtener el saldo utilizado por registro de guardias para una delegación en un año determinado", tags = {
			"Guardia" })
	@GetMapping("/guardias/saldo")
	public ResponseEntity<Object> getSaldoGuardia(
			@Parameter(description = "ID de la Delegación que se consulta el saldo utilizado", required = true) @RequestParam(required = true) String idDelegacion,
			@Parameter(description = "Año del ejercicio del que se consulta el saldo utilizado", required = true) @RequestParam(required = true) Integer anio_ejercicio,
			@Parameter(description = "Mes del ejercicio del que se consulta el saldo utilizado", required = true) @RequestParam(required = true) Integer mes_ejercicio,
			@Parameter(description = "Quincena del ejercicio del que se consulta el saldo utilizado", required = false) @RequestParam(required = true) Integer quincena,
			@Parameter(description = "Tipo para obtener las guardidas del empleado ('GI': Internas o 'GE': Externas)", required = true) @RequestParam(required = true) String tipoGuardia,
			@Parameter(description = "ID del Centro de Trabajo del que se consulta el saldo utilizado", required = false) @RequestParam(required = false) String idCentroTrab) {

		double saldo = 0;

		try{

			switch (tipoGuardia) {

				case "GI":
					if (idCentroTrab == null) {
						saldo = guardiaRepository.ObtenerSaldoUtilizado(idDelegacion, anio_ejercicio, mes_ejercicio);
					} else {
						saldo = guardiaRepository.ObtenerSaldoUtilizado_ct(0, idCentroTrab, anio_ejercicio, mes_ejercicio, quincena);
					}
					break;

				case "GE":
					if (idCentroTrab == null) {
						saldo = guardiaRepository.ObtenerSaldoUtilizadoExt(idDelegacion, anio_ejercicio, mes_ejercicio);
					} else {
						saldo = guardiaRepository.ObtenerSaldoUtilizadoExt_ct(0, idCentroTrab, anio_ejercicio, mes_ejercicio, quincena);
					}
					break;

				default:
					return ResponseHandler.generateResponse("No se indicó el tipo de guardia correctamente ('GI': Internas o 'GE': Externas)", HttpStatus.INTERNAL_SERVER_ERROR, null);

			}

		}catch (Exception e ){
			return ResponseHandler.generateResponse(
					"No se pudo consulta el saldo", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}



		return ResponseHandler.generateResponse(
				"Se obtuvo el saldo utilizado para guardias para las condiciones indicadas", HttpStatus.OK, saldo);
	}

	@Operation(summary = "Agrega un nuevo registro de guardia al Sistema", description = "Agrega un nuevo registro de guardia al Sistema", tags = {
			"Guardia" })
	@PostMapping("/guardias")
	public ResponseEntity<Object> agregarGuardia(
			@Parameter(description = "Objeto de la guardia a crearse en el Sistema") @RequestBody DatosGuardia guardia) {
		try {

			int id = 0, anio, mes, quincena;
			int horasEnDiaInt = 0,    horasEnDiaExt = 0,
				maxHorasEnQnaInt = 0, maxHorasEnQnaExt = 0, 
				maxDiasEnQnaInt = 0,  maxDiasEnQnaExt = 0;
			double saldo_utilizado = 0, saldo = 0;
			Presupuesto presup;
			Paga paga;

			String idCentroTrab = guardia.getId_centro_trabajo();
			String fec_pago = guardia.getFec_paga();
			String inicio, fin;

			try {
				paga   = pagaRepository.findByFecha(fec_pago);
				anio   = paga.getAnio_ejercicio();
				mes    = paga.getMes_ejercicio();
				quincena = paga.getQuincena();
				inicio = paga.getFec_inicio();
				fin    = paga.getFec_fin();
			} catch (EmptyResultDataAccessException e) {
				return ResponseHandler.generateResponse("No existe la fecha de pago indicada",
						HttpStatus.INTERNAL_SERVER_ERROR, null);
			}

			switch (guardia.getTipo_guardia()) {

				case "GI":
					if (guardiaRepository.existe_guardia(guardia) > 0)
						return ResponseHandler.generateResponse(
//								"Existe un registro de Guardia en ese mismo horario",
								"Existe un registro de guardia en este mismo horario",
								HttpStatus.INTERNAL_SERVER_ERROR, null);

					horasEnDiaInt    = guardiaRepository.get_horas_guardia(guardia.getClave_empleado(), guardia.getFec_inicio(), guardia.getFec_fin());
					maxHorasEnQnaInt = guardiaRepository.get_horas_guardia(guardia.getClave_empleado(), inicio, fin);
					maxDiasEnQnaInt  = guardiaRepository.get_dias_guardia(guardia.getClave_empleado(), inicio, fin, guardia.getFec_inicio());

					switch (guardia.getId_clave_movimiento()) {

						case "06", "08", "09":
							// Valida horas día
							if (horasEnDiaInt + guardia.getHoras() > 11)
								return ResponseHandler.generateResponse("No se puede exceder de 11 horas al día en el registro de guardias. Acualmente cuenta con " + horasEnDiaInt
										+ " más " + guardia.getHoras() + " del registro actual, exceden ese límite.",
										HttpStatus.INTERNAL_SERVER_ERROR, null);
							// Valida horas por quincena
							if (maxHorasEnQnaInt + guardia.getHoras() > 121)
								return ResponseHandler.generateResponse("No se puede exceder de 121 horas a la quincena en el registro de guardias. Acualmente cuenta con " + maxHorasEnQnaInt
										+ " más " + guardia.getHoras() + " del registro actual, exceden ese límite.",
										HttpStatus.INTERNAL_SERVER_ERROR, null);
							// Valida días por quincena
							if(maxDiasEnQnaInt + 1 > 11)
								return ResponseHandler.generateResponse("No se puede exceder de 11 días a la quincena en el registro de guardias. Acualmente cuenta con " + maxDiasEnQnaInt
										+ " más el del registro actual, exceden ese límite.",
										HttpStatus.INTERNAL_SERVER_ERROR, null);
							break;

						case "07":
							// Valida horas día
							if (horasEnDiaInt + guardia.getHoras() > 24)
								return ResponseHandler.generateResponse("No se puede exceder de 24 horas al día en el registro de guardias. Acualmente cuenta con " + horasEnDiaInt
										+ " más " + guardia.getHoras() + " del registro actual, exceden ese límite.",
										HttpStatus.INTERNAL_SERVER_ERROR, null);
							// Valida horas por quincena
							if (maxHorasEnQnaInt + guardia.getHoras() > 144)
								return ResponseHandler.generateResponse("No se puede exceder de 144 horas a la quincena en el registro de guardias. Acualmente cuenta con " + maxHorasEnQnaInt
										+ " más " + guardia.getHoras() + " del registro actual, exceden ese límite.",
										HttpStatus.INTERNAL_SERVER_ERROR, null);
							// Valida días por quincena
							if(maxDiasEnQnaInt + 1 > 6)
								return ResponseHandler.generateResponse("No se puede exceder de 6 días a la quincena en el registro de guardias. Acualmente cuenta con " + maxDiasEnQnaInt
										+ " más el del registro actual, exceden ese límite.",
										HttpStatus.INTERNAL_SERVER_ERROR, null);
							break;

						default:
							return ResponseHandler.generateResponse("No se indicó el tipo de guardia correctamente ('GI': Internas o 'GE': Externas)", HttpStatus.INTERNAL_SERVER_ERROR, null);
					}
					break;

				case "GE":
					if (guardiaRepository.existe_guardia_ext(guardia) > 0)
						return ResponseHandler.generateResponse("Existe un registro de Guardia en ese mismo horario",
								HttpStatus.INTERNAL_SERVER_ERROR, null);

					horasEnDiaExt    = guardiaRepository.get_horas_guardia_ext(guardia.getClave_empleado(), guardia.getFec_inicio(), guardia.getFec_fin());
					maxHorasEnQnaExt = guardiaRepository.get_horas_guardia_ext(guardia.getClave_empleado(), inicio, fin);
					maxDiasEnQnaExt  = guardiaRepository.get_dias_guardia_ext(guardia.getClave_empleado(), inicio, fin, guardia.getFec_inicio());

					switch (guardia.getId_clave_movimiento()) {

						case "06", "08", "09":
							// Valida horas por día
							if (horasEnDiaExt + guardia.getHoras() > 11)
								return ResponseHandler.generateResponse("No se puede exceder de 11 horas al día en el registro de guardias. Acualmente cuenta con " + horasEnDiaExt
										+ " más " + guardia.getHoras() + " del registro actual, exceden ese límite.",
										HttpStatus.INTERNAL_SERVER_ERROR, null);
							// Valida horas por quincena
							if (maxHorasEnQnaExt + guardia.getHoras() > 121)
								return ResponseHandler.generateResponse("No se puede exceder de 121 horas a la quincena en el registro de guardias. Acualmente cuenta con " + maxHorasEnQnaExt
										+ " más " + guardia.getHoras() + " del registro actual, exceden ese límite.",
										HttpStatus.INTERNAL_SERVER_ERROR, null);
							// Valida días por quincena
							if(maxDiasEnQnaExt + 1 > 11)
								return ResponseHandler.generateResponse("Existe un registro de Guardia en ese mismo horario. Acualmente cuenta con " + maxDiasEnQnaExt
										+ " más el del registro actual, exceden ese límite.",
										HttpStatus.INTERNAL_SERVER_ERROR, null);	
							break;

						case "07":
							// Valida horas por día
							if (horasEnDiaExt + guardia.getHoras() > 24)
								return ResponseHandler.generateResponse("No se puede exceder de 24 horas al día en el registro de guardias. Acualmente cuenta con " + horasEnDiaExt
										+ " más " + guardia.getHoras() + " del registro actual, exceden ese límite.",
										HttpStatus.INTERNAL_SERVER_ERROR, null);
							// Valida horas por quincena
							if (maxHorasEnQnaExt + guardia.getHoras() > 144)
								return ResponseHandler.generateResponse("No se puede exceder de 144 horas a la quincena en el registro de guardias. Acualmente cuenta con " + maxHorasEnQnaExt
										+ " más " + guardia.getHoras() + " del registro actual, exceden ese límite.",
										HttpStatus.INTERNAL_SERVER_ERROR, null);
							// Valida días por quincena
							if(maxDiasEnQnaExt + 1 > 6)
								return ResponseHandler.generateResponse("No se puede exceder de 6 días a la quincena en el registro de guardias. Acualmente cuenta con " + maxDiasEnQnaExt
										+ " más el del registro actual, exceden ese límite.",
										HttpStatus.INTERNAL_SERVER_ERROR, null);
							break;

						default:
							return ResponseHandler.generateResponse("No se indicó el tipo de guardia correctamente ('GI': Internas o 'GE': Externas)", HttpStatus.INTERNAL_SERVER_ERROR, null);
					}
					break;

				default:
					return ResponseHandler.generateResponse("No se indicó el tipo de guardia correctamente ('GI': Internas o 'GE': Externas)", HttpStatus.INTERNAL_SERVER_ERROR, null);
			}

			try {
				presup = presupuestoRepository.getElementByType_ct(idCentroTrab, guardia.getTipo_guardia(), anio, mes, quincena);
//				System.out.println(presup);
			} catch (EmptyResultDataAccessException e) {
				return ResponseHandler.generateResponse(
//						"No existe presupuesto registrado para realizar este tipo de movimiento",
						"No se encontro presupuesto distribuido para este centro de trabajo",
						HttpStatus.INTERNAL_SERVER_ERROR, null);
			}

			saldo = (presup != null) ? presup.getSaldo() : 0;

			switch (guardia.getTipo_guardia()) {

				case "GI":
					saldo_utilizado = guardiaRepository.ObtenerSaldoUtilizado_ct(0, idCentroTrab, presup.getAnio(),
							presup.getMes(), presup.getQuincena());
					break;

				case "GE":
					saldo_utilizado = guardiaRepository.ObtenerSaldoUtilizadoExt_ct(0, idCentroTrab, presup.getAnio(),
							presup.getMes(), presup.getQuincena());
					break;

				default:
					return ResponseHandler.generateResponse("No se indicó el tipo de guardia correctamente ('GI': Internas o 'GE': Externas)", HttpStatus.INTERNAL_SERVER_ERROR, null);
			}

			double importe = guardiaService.CalculaImporteGuardia(guardia.getId_tipo_tabulador(), guardia.getId_zona(),
					guardia.getId_nivel(), guardia.getId_sub_nivel(), guardia.getId_tipo_jornada(),
					guardia.getRiesgos(), guardia.getTipo_guardia(), guardia.getHoras(), guardia.getFec_paga());

			if (importe + saldo_utilizado <= saldo) {

				id = guardiaService.guardarGuardia(guardia, importe);

			} else {

				return ResponseHandler.generateResponse(
						"No existe presupuesto suficiente para realizar este tipo de registro",
						HttpStatus.INTERNAL_SERVER_ERROR, null);
			}

			return ResponseHandler.generateResponse(
//					"El registro de guardia ha sido guardado de manera exitosa.",
					"Registro guardado correctamente",
					HttpStatus.OK, null);
		} catch (Exception e) {

			return ResponseHandler.generateResponse("Error al realizar el registro de guardia indicado",
					HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Actualiza un registro de guardia en el Sistema", description = "Actualiza un registro de guardia en el Sistema", tags = {"Guardia" })
	@PutMapping("/guardias")
	public ResponseEntity<Object> actualizarGuardia(
			@Parameter(description = "Objeto de la guardia a actualizarse en el Sistema") @RequestBody DatosGuardia guardia) {
		try {

			int anio, mes, quincena;
			int horasEnDiaInt = 0,    horasEnDiaExt = 0,
					maxHorasEnQnaInt = 0, maxHorasEnQnaExt = 0, 
					maxDiasEnQnaInt = 0,  maxDiasEnQnaExt = 0;
			double saldo_utilizado = 0, saldo = 0;
			Presupuesto presup;
			Paga paga;

			String idCentroTrab = guardia.getId_centro_trabajo();
			String fec_pago = guardia.getFec_paga();
			String inicio, fin;

			try {
				paga   = pagaRepository.findByFecha(fec_pago);
				anio   = paga.getAnio_ejercicio();
				mes    = paga.getMes_ejercicio();
				quincena = paga.getQuincena();
				inicio = paga.getFec_inicio();
				fin    = paga.getFec_fin();
//				System.out.println("=============>"+guardia.getId() +" "+ guardia.getImporte());
			} catch (EmptyResultDataAccessException e) {
				return ResponseHandler.generateResponse("No existe la fecha de pago indicada",
						HttpStatus.INTERNAL_SERVER_ERROR, null);
			}

			switch (guardia.getTipo_guardia()) {

				case "GI":
					if (guardiaRepository.existe_guardia_upd(guardia)>0)
						return ResponseHandler.generateResponse("Existe un registro de Guardia en ese mismo periodo", HttpStatus.INTERNAL_SERVER_ERROR, null);

					horasEnDiaInt    = guardiaRepository.get_horas_guardia_upd(guardia.getClave_empleado(), guardia.getId(), guardia.getFec_inicio(), guardia.getFec_fin());
					maxHorasEnQnaInt = guardiaRepository.get_horas_guardia_upd(guardia.getClave_empleado(), guardia.getId(), inicio, fin);
					maxDiasEnQnaInt  = guardiaRepository.get_dias_guardia_upd(guardia.getClave_empleado(), guardia.getId(), inicio, fin, guardia.getFec_inicio());

					switch (guardia.getId_clave_movimiento()) {

						case "06", "08", "09":
							// Valida horas por día
							if (horasEnDiaInt + guardia.getHoras() > 11)
								return ResponseHandler.generateResponse("No se puede exceder de 11 horas al día en el registro de guardias. Acualmente cuenta con " + horasEnDiaInt
										+ " más " + guardia.getHoras() + " del registro actual, exceden ese límite.",
										HttpStatus.INTERNAL_SERVER_ERROR, null);
							// Valida horas por quincena
							if (maxHorasEnQnaInt + guardia.getHoras() > 121)
								return ResponseHandler.generateResponse("No se puede exceder de 121 horas a la quincena en el registro de guardias. Acualmente cuenta con " + maxHorasEnQnaInt
										+ " más " + guardia.getHoras() + " del registro actual, exceden ese límite.",
										HttpStatus.INTERNAL_SERVER_ERROR, null);
							// Valida días por quincena
							if(maxDiasEnQnaInt + 1 > 11)
								return ResponseHandler.generateResponse("No se puede exceder de 11 días a la quincena en el registro de guardias. Acualmente cuenta con " + maxDiasEnQnaInt
										+ " más " + guardia.getHoras() + " del registro actual, exceden ese límite.",
										HttpStatus.INTERNAL_SERVER_ERROR, null);	
							break;

						case "07":
							// Valida horas por día
							if (horasEnDiaInt + guardia.getHoras() > 24)
								return ResponseHandler.generateResponse("No se puede exceder de 24 horas al día en el registro de guardias. Acualmente cuenta con " + horasEnDiaInt
										+ " más " + guardia.getHoras() + " del registro actual, exceden ese límite.",
										HttpStatus.INTERNAL_SERVER_ERROR, null);
							// Valida horas por quincena
							if (maxHorasEnQnaInt + guardia.getHoras() > 144)
								return ResponseHandler.generateResponse("No se puede exceder de 144 horas a la quincena en el registro de guardias. Acualmente cuenta con " + maxHorasEnQnaInt
										+ " más " + guardia.getHoras() + " del registro actual, exceden ese límite.",
										HttpStatus.INTERNAL_SERVER_ERROR, null);
							// Valida días por quincena
							if(maxDiasEnQnaInt + 1 > 6)
								return ResponseHandler.generateResponse("No se puede exceder de 6 días a la quincena en el registro de guardias. Acualmente cuenta con " + maxDiasEnQnaInt
										+ " más " + guardia.getHoras() + " del registro actual, exceden ese límite.",
										HttpStatus.INTERNAL_SERVER_ERROR, null);
							break;

						default:
							return ResponseHandler.generateResponse("No se indicó el tipo de guardia correctamente ('GI': Internas o 'GE': Externas)", HttpStatus.INTERNAL_SERVER_ERROR, null);
					}
					break;

				case "GE":
					if (guardiaRepository.existe_guardia_ext_upd(guardia)>0)
						return ResponseHandler.generateResponse("Existe un registro de Guardia en ese mismo periodo", HttpStatus.INTERNAL_SERVER_ERROR, null);

					horasEnDiaExt    = guardiaRepository.get_horas_guardia_ext_upd(guardia.getClave_empleado(), guardia.getId(), guardia.getFec_inicio(), guardia.getFec_fin());
					maxHorasEnQnaExt = guardiaRepository.get_horas_guardia_ext_upd(guardia.getClave_empleado(), guardia.getId(), inicio, fin);
					maxDiasEnQnaExt  = guardiaRepository.get_dias_guardia_ext_upd(guardia.getClave_empleado(), guardia.getId(), inicio, fin, guardia.getFec_inicio());

					switch (guardia.getId_clave_movimiento()) {

						case "06", "08", "09":
							// Valida horas por día
							if (horasEnDiaExt + guardia.getHoras() > 11)
								return ResponseHandler.generateResponse("No se puede exceder de 11 horas al día en el registro de guardias. Acualmente cuenta con " + horasEnDiaExt
										+ " más " + guardia.getHoras() + " del registro actual, exceden ese límite.",
										HttpStatus.INTERNAL_SERVER_ERROR, null);
							// Valida horas por quincena
							if (maxHorasEnQnaExt + guardia.getHoras() > 121)
								return ResponseHandler.generateResponse("No se puede exceder de 121 horas a la quincena en el registro de guardias. Acualmente cuenta con " + maxHorasEnQnaExt
										+ " más " + guardia.getHoras() + " del registro actual, exceden ese límite.",
										HttpStatus.INTERNAL_SERVER_ERROR, null);
							// Valida días por quincena
							if(maxDiasEnQnaExt + 1 > 11)
								return ResponseHandler.generateResponse("No se puede exceder de 11 días a la quincena en el registro de guardias. Acualmente cuenta con " + maxDiasEnQnaExt
										+ " más " + guardia.getHoras() + " del registro actual, exceden ese límite.",
										HttpStatus.INTERNAL_SERVER_ERROR, null);	
							break;

						case "07":
							// Valida horas por día
							if (horasEnDiaExt + guardia.getHoras() > 24)
								return ResponseHandler.generateResponse("No se puede exceder de 24 horas al día en el registro de guardias. Acualmente cuenta con " + horasEnDiaExt
										+ " más " + guardia.getHoras() + " del registro actual, exceden ese límite.",
										HttpStatus.INTERNAL_SERVER_ERROR, null);
							// Valida horas por quincena
							if (maxHorasEnQnaExt + guardia.getHoras() > 144)
								return ResponseHandler.generateResponse("No se puede exceder de 144 horas a la quincena en el registro de guardias. Acualmente cuenta con " + maxHorasEnQnaExt
										+ " más " + guardia.getHoras() + " del registro actual, exceden ese límite.",
										HttpStatus.INTERNAL_SERVER_ERROR, null);
							// Valida días por quincena
							if(maxDiasEnQnaExt + 1 > 6)
								return ResponseHandler.generateResponse("No se puede exceder de 6 días a la quincena en el registro de guardias. Acualmente cuenta con " + maxDiasEnQnaExt
										+ " más " + guardia.getHoras() + " del registro actual, exceden ese límite.",
										HttpStatus.INTERNAL_SERVER_ERROR, null);
							break;

						default:
							return ResponseHandler.generateResponse("No se indicó el tipo de guardia correctamente ('GI': Internas o 'GE': Externas)", HttpStatus.INTERNAL_SERVER_ERROR, null);
					}
					break;

				default:
					return ResponseHandler.generateResponse("No se indicó el tipo de guardia correctamente ('GI': Internas o 'GE': Externas)", HttpStatus.INTERNAL_SERVER_ERROR, null);
			}

			try {
				presup = presupuestoRepository.getElementByType_ct(idCentroTrab, guardia.getTipo_guardia(), anio, mes, quincena);
			} catch (EmptyResultDataAccessException e) {
				return ResponseHandler.generateResponse(
//						"No existe presupuesto registrado para realizar este tipo de movimiento",
						"No se encontro presupuesto distribuido para este centro de trabajo",
						HttpStatus.INTERNAL_SERVER_ERROR, null);
			}

			saldo = (presup != null) ? presup.getSaldo() : 0;

			switch (guardia.getTipo_guardia()) {

				case "GI":
					saldo_utilizado = guardiaRepository.ObtenerSaldoUtilizado_ct(guardia.getId(), idCentroTrab,
							presup.getAnio(), presup.getMes(), presup.getQuincena());
					break;

				case "GE":
					saldo_utilizado = guardiaRepository.ObtenerSaldoUtilizadoExt_ct(guardia.getId(), idCentroTrab,
							presup.getAnio(), presup.getMes(), presup.getQuincena());
					break;

				default:
					return ResponseHandler.generateResponse("No se indicó el tipo de guardia correctamente ('GI': Internas o 'GE': Externas)", HttpStatus.INTERNAL_SERVER_ERROR, null);
			}

			double importe = guardiaService.CalculaImporteGuardia(guardia.getId_tipo_tabulador(), guardia.getId_zona(),
					guardia.getId_nivel(), guardia.getId_sub_nivel(), guardia.getId_tipo_jornada(),
					guardia.getRiesgos(), guardia.getTipo_guardia(), guardia.getHoras(), guardia.getFec_paga());

			if (importe + saldo_utilizado <= saldo) {

				guardiaService.actualizaGuardia(guardia, importe);
			} else {

				return ResponseHandler.generateResponse(
						"No existe presupuesto suficiente para realizar la actualización del registro",
						HttpStatus.INTERNAL_SERVER_ERROR, null);
			}

			return ResponseHandler.generateResponse(
//					"El registro de guardia ha sido actualizado de manera exitosa.",
					"Modificacion de registro realizada",
					HttpStatus.OK, null);
		} catch (Exception e) {

			return ResponseHandler.generateResponse(
					"Error al realizar la actualización del registro de guardia indicado",
					HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Actualizar importes de guardias en el Sistema", description = "Actualizar importes de guardias en el Sistema", tags = { "Guardia" })
	@PutMapping("/guardias/actualiza")
	public ResponseEntity<Object> actualizaImportes(
			@Parameter(description = "Fecha de quincena para el cálculo del importe de la guardia", required = true) @RequestParam(required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date quincena,
			@Parameter(description = "Tipo para obtener las guardidas del empleado ('GI': Internas o 'GE': Externas)", required = true) @RequestParam(required = true) String tipoGuardia) {
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String strQuincena = dateFormat.format(quincena);
			guardiaService.actualizaImportesGuardias(strQuincena, tipoGuardia);

			return ResponseHandler.generateResponse("Los importes de las guardias se actualizaron de manera exitósa.",
					HttpStatus.OK, null);
		} catch (Exception e) {

			return ResponseHandler.generateResponse(
					"Error al realizar la actualización del registro de guardia indicado",
					HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Actualizar importes de guardias en el Sistema", description = "Actualizar importes de guardias en el Sistema", tags = { "Guardia" })
	@PutMapping("/guardias/actualiza2")
	public ResponseEntity<Object> actualizaImportes2(
			@Parameter(description = "Fecha de quincena para el cálculo del importe de la Suplencia", required = true) @RequestParam(required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date quincena,
			@Parameter(description = "Tipo para obtener las guardidas del empleado ('GI': Internas o 'GE': Externas)", required = true) @RequestParam(required = true) String tipoSuplencia) {
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String strQuincena = dateFormat.format(quincena);
			guardiaService.actualizaImportesGuardias2(strQuincena, tipoSuplencia);

			return ResponseHandler.generateResponse("Los importes de las guardidas se actualizaron de manera exitósa",
					HttpStatus.OK, null);
		} catch (Exception e) {

			return ResponseHandler.generateResponse("Error al actualizar importes de guardias en el Sistema",
					HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Eliminar un registro de guardia del Sistema", description = "Eliminar un registro de guardia del Sistema", tags = { "Guardia" })
	@DeleteMapping("/guardias")
	public ResponseEntity<Object> eliminarGuardia(
			@Parameter(description = "Número de ID para obtener la guardia del empleado", required = true) @RequestParam(required = true) Integer idGuardia,
			@Parameter(description = "Tipo para obtener la guardia del empleado (I-Interna o E-Externa)", required = true) @RequestParam(required = true) String tipoGuardia) {
		try {
			guardiaService.eliminarGuardia(idGuardia, tipoGuardia);

			return ResponseHandler.generateResponse("El registro de guardia fué eliminado exitosamente.",
					HttpStatus.OK, null);
		} catch (Exception e) {

			return ResponseHandler.generateResponse("No se borró satisfactoriamente el registro de la guardia.",
					HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Actualizar Estatus de Guardias en el Sistema", description = "Actualizar Estatus de Suplencias en el Sistema", tags = { "Guardia" })
	@PutMapping("/guardias/actualizaEstatus")
	public ResponseEntity<Object> actualizaEstatus(
			@Parameter(description = "ID de la Suplencia", required = true) @RequestParam(required = true) Integer idGuardia,
			@Parameter(description = "Estatus de la suplencia", required = true) @RequestParam(required = true) Integer estatus,
			@Parameter(description = "Tipo de Suplencia para actualizar el estatus", required = true) @RequestParam(required = true) String tipo,
			@Parameter(description = "ID del usaurio que realiza la actualización del estatus", required = true) @RequestParam(required = true) Integer idUsuario,
			@Parameter(description = "Comentarios correspondientes a la actualización del estatus", required = false) @RequestParam(required = false) String comentarios) {

		if (comentarios == null) comentarios = "";

		try {

			switch (estatus) {

				case 1, 2:

					guardiaRepository.updateAuthStatusGuardia1(estatus, idGuardia, tipo, comentarios, idUsuario);
					break;

				case 3, 4:

					guardiaRepository.updateAuthStatusGuardia2(estatus, idGuardia, tipo, comentarios, idUsuario);
					break;

				default:
					return ResponseHandler.generateResponse("No se indicó el tipo de guardia correctamente ('GI': Internas o 'GE': Externas)", HttpStatus.INTERNAL_SERVER_ERROR, null);

			}

			return ResponseHandler.generateResponse("El estatus de la guardia se actualizó de manera exitósa", HttpStatus.OK, null);
		} catch (Exception e) {

			return ResponseHandler.generateResponse("Error al actualizar el estatus de las guardias", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	@Operation(summary = "Actualizar Estatus de Guardias en el Sistema", description = "Actualizar Guardias de Suplencias en el Sistema", tags = { "Guardia" })
	@PutMapping("/guardias/validacion_guardias")
	public ResponseEntity<Object> validacionGuardias(
			@Parameter(description = "Fecha de control para validar las guardias", required = true) @RequestParam(required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fec_pago,
			@Parameter(description = "Estatus a actualizar de las guardias (1 - Autorización, 3 - Confirmación)", required = true) @RequestParam(required = true) Integer estatus,
			@Parameter(description = "Tipo de Guardias para realizar la validación", required = true) @RequestParam(required = true) String tipo,
			@Parameter(description = "ID del usaurio que realiza la actualización del estatus", required = true) @RequestParam(required = true) Integer idUsuario,
			@Parameter(description = "Delegacion a la que pertenece el usuario que autoriza", required = true) @RequestParam(required = true) String idDeleg) {

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String strFecha = dateFormat.format(fec_pago);

		try {

			switch (estatus) {

				case 1:

					switch (tipo) {
					
						case "GI":
							guardiaRepository.updateAuthStatusGuardias1(tipo, strFecha, idDeleg, idDeleg, idUsuario);
							break;

						case "GE":
							guardiaRepository.updateAuthStatusGuardias1Ext(tipo, strFecha, idDeleg, idDeleg, idUsuario);
							break;
					}
					break;

				case 3:

					switch (tipo) {
					
						case "GI":
//							int contGuardsAuthInt = guardiaRepository.countAuthGuardiasStatusInt(strFecha, idDeleg);
//
//							if(contGuardsAuthInt > 1){
//								return ResponseHandler.generateResponse("Para confirmar es necesario pasar la fase de autorización de guardias", HttpStatus.INTERNAL_SERVER_ERROR, null);
//							}

							guardiaRepository.updateAuthStatusGuardias2(tipo, strFecha, idDeleg, idDeleg, idUsuario);

							break;
	
						case "GE":
//							int contGuardsAuthExt = guardiaRepository.countAuthGuardiasStatusExt(strFecha, idDeleg);
//							if(contGuardsAuthExt > 1){
//								return ResponseHandler.generateResponse("Para confirmar es necesario pasar la fase de autorización de guardias", HttpStatus.INTERNAL_SERVER_ERROR, null);
//							}

							guardiaRepository.updateAuthStatusGuardias2Ext(tipo, strFecha, idDeleg, idDeleg, idUsuario);

							break;
					}
					break;

				default:
					return ResponseHandler.generateResponse("No se indicó el tipo de guardia correctamente ('GI': Internas o 'GE': Externas)", HttpStatus.INTERNAL_SERVER_ERROR, null);

			}

			return ResponseHandler.generateResponse("El estatus de las guardias se actualizó de manera exitósa", HttpStatus.OK, null);
		} catch (Exception e) {

			return ResponseHandler.generateResponse("Error al actualizar el estatus en las guardias", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	@Operation(summary = "Obtener los registros de guardias del empleado en el Sistema", description = "Obtener los registros de guardias del empleado en el Sistema", tags = { "Guardia" })
	@GetMapping("/guardias/consulta")
	public ResponseEntity<Object> getDynamicGuardias(
			@Parameter(description = "Fecha de quincena para la consulta de guardias", required = false) @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date quincena,
			@Parameter(description = "Tipo para obtener las guardias (GI-Internas o GE-Externas)", required = true) @RequestParam(required = true) String tipoGuardia,
			@Parameter(description = "ID de empleado o RFC para obtener las guardias", required = false) @RequestParam(required = false) String claveEmpleado,
			@Parameter(description = "Importe mínimo para obtener las guardias", required = false) @RequestParam(required = false) Double importe_min,
			@Parameter(description = "Importe máximo para obtener las guardias", required = false) @RequestParam(required = false) Double importe_max,
			@Parameter(description = "ID de la delegación para obtener las guardias", required = false) @RequestParam(required = false) String idDelegacion,
			@Parameter(description = "ID del centro de trabajo para obtener las guardias", required = false) @RequestParam(required = false) String idCentroTrab,
			@Parameter(description = "Clave del servicio para obtener las guardias", required = false) @RequestParam(required = false) String claveServicio,
			@Parameter(description = "Clave del puesto para obtener las guardias", required = false) @RequestParam(required = false) String puesto,
			@Parameter(description = "Estatus de las guardias", required = false) @RequestParam(required = false) Integer estatus) {

		try {

			List<String> params = new ArrayList<>();
			List<String> regexList = new ArrayList<>();
			String message = "";

			final String regexFecha_y_m_d = "^((19|20)\\d\\d)-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$";
			final String regexRfc = "^([A-ZÑ&]{3,4}) ?(\\d{2}(0[1-9]|1[0-2])(0[1-9]|1\\d|2[0-9]|3[01]))?([A-Z\\d]{2})([A\\d])?$";
			final String regexNumEmp = "^[0-9]{6}$";
			final String regexDel = "^[0-9]{2}$";
			final String regexCt = "^[0-9]{5}$";
			final String regexServicio = "^[a-zA-Z0-9]{5}$";
			final String regexPuesto = "^[a-zA-Z0-9]{6,7}$";


			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String strQuincena = null;

			if (quincena != null)
				strQuincena = dateFormat.format(quincena);

			List<DatosGuardia> guardias = new ArrayList<DatosGuardia>();


			if (claveEmpleado != null) {
				if (tipoGuardia.equals(String.valueOf("GI"))) {
					params.add(claveEmpleado);
					regexList.add(regexNumEmp);

				}
				else if(tipoGuardia.equals(String.valueOf("GE"))){
//					params.add(claveEmpleado);
//					regexList.add(regexRfc);
//
				}
				else{
					throw new SQLException("El numero de empleado es incorrecto");
				}
			}

			if (idDelegacion != null) {
				params.add(idDelegacion);
				regexList.add(regexDel);
			}

			if (idCentroTrab != null) {
				params.add(idCentroTrab);
				regexList.add(regexCt);
			}

			if (claveServicio != null) {
				params.add(claveServicio);
				regexList.add(regexServicio);
			}

			if (puesto != null) {
				params.add(puesto);
				regexList.add(regexPuesto);
			}

			boolean regexResult = paramsValidatorService.validate(regexList,params);
			boolean injection = paramsValidatorService.sqlInjectionObjectValidator(params);

			if(regexResult && !injection){
				guardias = guardiaRepository.ConsultaDynamicGuardias(strQuincena, tipoGuardia, claveEmpleado, importe_min, importe_max,
						idDelegacion, idCentroTrab, claveServicio, puesto, estatus);

				if (guardias.isEmpty()) {
//					return ResponseHandler.generateResponse("No se encontraron los registros de guardias del empleado en el Sistema", HttpStatus.NOT_FOUND, null);
					return ResponseHandler.generateResponse(
							"No se encontraron registros previos",
							HttpStatus.NOT_FOUND, null);
				}

				return ResponseHandler.generateResponse(
						"Se encontraron guardias al consultar bajo las condiciones indicadas", HttpStatus.OK, guardias);

			}else{
				message = "Revise los campos de consulta";
				return ResponseHandler.generateResponse(message, HttpStatus.NOT_FOUND, null);
			}


		} catch (Exception e) {
			logger.info(e.toString());

			return ResponseHandler.generateResponse("Error al consultar guardias bajo las condiciones indicadas.",
					HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Busca regsitros de forma dinamica en la consulta de guardias",
			description = "Método que extrae los registros de guardias internas",
			tags = { "Consulta de guardias" })
	@GetMapping("/guardias/consultaPaginada")
	public ResponseEntity<Object> getAllGuardiasPage(
			@Parameter(description = "Fecha de quincena para la consulta de guardias", required = false) @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date quincena,
			@Parameter(description = "Tipo para obtener las guardias (GI-Internas o GE-Externas)", required = true) @RequestParam(required = true) String tipoGuardia,
			@Parameter(description = "ID de empleado o RFC para obtener las guardias", required = false) @RequestParam(required = false) String claveEmpleado,
			@Parameter(description = "Importe mínimo para obtener las guardias", required = false) @RequestParam(required = false) Double importe_min,
			@Parameter(description = "Importe máximo para obtener las guardias", required = false) @RequestParam(required = false) Double importe_max,
			@Parameter(description = "ID de la delegación para obtener las guardias", required = false) @RequestParam(required = false) String idDelegacion,
			@Parameter(description = "ID del centro de trabajo para obtener las guardias", required = false) @RequestParam(required = false) String idCentroTrab,
			@Parameter(description = "Clave del servicio para obtener las guardias", required = false) @RequestParam(required = false) String claveServicio,
			@Parameter(description = "Clave del puesto para obtener las guardias", required = false) @RequestParam(required = false) String puesto,
			@Parameter(description = "Estatus de las guardias", required = false) @RequestParam(required = false) Integer estatus,
			@Parameter(description = "Paginacion de la consulta", required = false) @RequestParam(required = true) Integer page,
			@Parameter(description = "Tamaño de la consulta", required = false) @RequestParam(required = false) Integer size
	){
		try {

			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String strQuincena = null;

			if (quincena != null)
				strQuincena = dateFormat.format(quincena);

			Map<String, Object> dataAndSize = new HashMap<>();
			List<DatosGuardia> guardiasList = new ArrayList<DatosGuardia>();
			guardiasList = guardiaRepository.ConsultaDynamicGuardiasPage(strQuincena, tipoGuardia, claveEmpleado, importe_min, importe_max,
					idDelegacion, idCentroTrab, claveServicio, puesto, estatus, page, size);

			dataAndSize.put("guardias",guardiasList);
			dataAndSize.put("size", guardiaRepository.ConsultaDynamicGuardiasCount(strQuincena, tipoGuardia, claveEmpleado, importe_min, importe_max,
					idDelegacion, idCentroTrab, claveServicio, puesto, estatus));

			return ResponseHandler.generateResponse("Se encontraron guardias al consultar bajo las condiciones indicadas", HttpStatus.OK, dataAndSize);
		}catch (Exception e){
			return ResponseHandler.generateResponse("No se encontraron registros previos" + e, HttpStatus.CONFLICT, null);
		}
	}

	@Operation(summary = "Obtener los registros de guardias del empleado en el Sistema", description = "Obtener los registros de guardias del empleado en el Sistema", tags = { "Guardia" })
	@GetMapping("/guardias/consulta_x_auth")
	public ResponseEntity<Object> getDynamicGuardiasAuth(
			@Parameter(description = "Fecha de quincena para la consulta de guardias", required = true) @RequestParam(required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date quincena,
			@Parameter(description = "Tipo para obtener las guardias (GI-Internas o GE-Externas)", required = true) @RequestParam(required = true) String tipoGuardia,
			@Parameter(description = "ID de la delegación para obtener las guardias", required = false) @RequestParam(required = false) String idDelegacion,
			@Parameter(description = "ID del centro de trabajo para obtener las guardias", required = false) @RequestParam(required = false) String idCentroTrab,
			@Parameter(description = "Estatus de las guardias", required = false) @RequestParam(required = false) Integer estatus) {

		try {

			List<String> params = new ArrayList<>();
			List<String> regexList = new ArrayList<>();
			String message = "";

			final String regexTipoGuardia = "^(GI|GE)$";
			final String regexDel = "^[0-9]{2}$";
			final String regexCt = "^[0-9]{5}$";


			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String strQuincena = null;

			if (quincena != null)
				strQuincena = dateFormat.format(quincena);


			if (idDelegacion != null) {
				params.add(idDelegacion);
				regexList.add(regexDel);
			}

			if (idCentroTrab != null) {
				params.add(idCentroTrab);
				regexList.add(regexCt);
			}

			boolean regexResult = paramsValidatorService.validate(regexList,params);
			boolean injection = paramsValidatorService.sqlInjectionObjectValidator(params);

			List<DatosGuardia> guardias = new ArrayList<DatosGuardia>();

			if(regexResult && !injection){

				guardias = guardiaRepository.ConsultaDynamicAuthGuardias(strQuincena, tipoGuardia,idDelegacion, idCentroTrab, estatus);

				if (guardias.isEmpty()) {
					message = "No se encontraron los registros de suplencias del empleado en el Sistema";
					return ResponseHandler.generateResponse(message, HttpStatus.NOT_FOUND, null);
				}

				return ResponseHandler.generateResponse(
						"Se encontraron guardias al consultar bajo las condiciones indicadas", HttpStatus.OK, guardias);

			}else{
				message = "Revise los campos de consulta";
				return ResponseHandler.generateResponse(message, HttpStatus.NOT_FOUND, null);
			}


		} catch (Exception e) {
			logger.info(e.toString());

			return ResponseHandler.generateResponse("Error al consultar guardias bajo las condiciones indicadas.",
					HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

}
package gob.issste.gys.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import gob.issste.gys.model.BolsaTrabajo;
import gob.issste.gys.model.DatosEmpleado;
import gob.issste.gys.model.DatosSuplencia;
import gob.issste.gys.model.Paga;
import gob.issste.gys.model.Presupuesto;
import gob.issste.gys.repository.IBolsaTrabajoRepository;
import gob.issste.gys.repository.IEmpleadoRepository;
import gob.issste.gys.repository.IPagaRepository;
import gob.issste.gys.repository.IPresupuestoRepository;
import gob.issste.gys.repository.ISuplenciaRepository;
import gob.issste.gys.response.ResponseHandler;
import gob.issste.gys.service.ISuplenciaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

//@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
@Tag(name = "Suplencia", description = "API de Suplencias")
public class SuplenciaController {

	Logger logger = LoggerFactory.getLogger(JdbcTemplateDemo01Application.class);
	ParamsValidatorService paramsValidatorService = new ParamsValidatorService();

	@Autowired
	ISuplenciaRepository suplenciaRepository;

	@Autowired
	ISuplenciaService suplenciaService;

	@Autowired
	IEmpleadoRepository empleadoRepository; 

	@Autowired
	IBolsaTrabajoRepository bolsaTrabajoRepository;

	@Autowired
	IPagaRepository pagaRepository;

	@Autowired
	IPresupuestoRepository presupuestoRepository;

	@Operation(summary = "Obtener el importe de una Suplencia conforme a los criterios establecidos", description = "Se calcula el importe de una Suplencia conforme a los criterios establecidos", tags = { "Suplencia" })
	@GetMapping("/suplencias/importe")
	public ResponseEntity<Object> getImporteSuplencia(
			@Parameter(description = "Fecha de quincena para el cálculo del importe de la Suplencia", required = true) @RequestParam(required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date quincena,
			@Parameter(description = "Clave del empleado para obtener el importe de la Suplencia", required = true) @RequestParam(required = true) String clave_empleado, 
			@Parameter(description = "Tipo de riesgos para obtener el importe de la Suplencia", required = false) @RequestParam(required = false) Integer dias, 
			@Parameter(description = "Tipo de Suplencia para calcular el importe de la Suplencia", required = true) @RequestParam(required = true) String tipo ) 
	{
		if (dias == null) dias = Integer.valueOf(0);
		Double importe = 0.0;
		try {

			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String strDate = dateFormat.format(quincena);
			DatosEmpleado empleado = empleadoRepository.getDatosEmpleado(strDate, clave_empleado);
			int riesgos = empleadoRepository.ConsultaRiesgosEmp(clave_empleado, strDate);

			importe = suplenciaService.CalculaImporteSuplencia( strDate, empleado.getClave_empleado(), dias, tipo, riesgos );
			BigDecimal importe2 = new BigDecimal(importe).setScale(2, RoundingMode.HALF_UP);

			return ResponseHandler.generateResponse("Se encontró el importe de la suplencia conforme a los criterios establecidos", HttpStatus.OK, importe2);
		} catch (Exception e) {
			logger.info(e.toString());

			return ResponseHandler.generateResponse("Error al consultar el importe de la suplencia conforme a los criterios establecidos", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Obtener los registros de Suplencias del empleado en el Sistema", description = "Obtener los registros de Suplencias del empleado en el Sistema", tags = { "Suplencia" })
	@GetMapping("/suplencias")
	public ResponseEntity<Object> getSuplenciasEmpleado(
			@Parameter(description = "Número de empleado para obtener las guardidas del empleado", required = true) @RequestParam(required = true) String claveEmpleado,
			@Parameter(description = "Tipo para obtener las guardidas del empleado ('SI': Internas o 'SE': Externas)", required = true) @RequestParam(required = true) String tipoSuplencia,
			@Parameter(description = "Boolean para indicar si se incluyen los datos de los empleados", required = true) @RequestParam(value = "conDatosEmpleados", required = true) Boolean conEmpleados ) {

		try {
			List<DatosSuplencia> suplencias = new ArrayList<DatosSuplencia>();
			DatosEmpleado empleado, empleado_sup;

			switch (tipoSuplencia) {
			
				case "SI":
					suplencias = suplenciaRepository.ConsultaSuplenciasInternas(claveEmpleado);
					if (conEmpleados) {
						for(DatosSuplencia suplencia:suplencias) {
							logger.info("Emp: " + suplencia.getClave_empleado() + " Sup: " + suplencia.getClave_empleado() + " Fecha: " + suplencia.getFec_paga());
							try {
								empleado = empleadoRepository.getDatosEmpleado(suplencia.getFec_paga(), suplencia.getClave_empleado());
							} catch (Exception ex) {
								empleado = new DatosEmpleado();
							}
							suplencia.setEmpleado(empleado);
							try {
								empleado_sup = empleadoRepository.getDatosEmpleado(suplencia.getFec_paga(), suplencia.getClave_empleado_suplir());						
							} catch (Exception ex) {
								empleado_sup = new DatosEmpleado();
							}
							suplencia.setEmpleado_suplir(empleado_sup);
						}
					}
					break;
			
				case "SE":
					suplencias = suplenciaRepository.ConsultaSuplenciasExternas(claveEmpleado);
					if (conEmpleados) {
						for(DatosSuplencia suplencia:suplencias) {
							logger.info("Emp: " + suplencia.getClave_empleado() + " Sup: " + suplencia.getClave_empleado() + " Fecha: " + suplencia.getFec_paga());

							try {
								empleado_sup = empleadoRepository.getDatosEmpleado(suplencia.getFec_paga(), suplencia.getClave_empleado_suplir());						
							} catch (Exception ex) {
								empleado_sup = new DatosEmpleado();
							}
							suplencia.setEmpleado_suplir(empleado_sup);

							try {
								BolsaTrabajo bolsa = bolsaTrabajoRepository.findByRFC(suplencia.getClave_empleado(), empleado_sup.getId_delegacion());
								empleado = new DatosEmpleado();
								empleado.setClave_empleado(bolsa.getRfc());
								empleado.setNombre(bolsa.getNombre());
								empleado.setApellido_1(bolsa.getApellidoPat());
								empleado.setApellido_2(bolsa.getApellidoMat());
							} catch (Exception ex) {
								empleado = new DatosEmpleado();
							}
							suplencia.setEmpleado(empleado);

						}
					}
					break;

				default:
					return ResponseHandler.generateResponse("No se indicó el tipo de suplencia correctamente ('SI': Internas o 'SE': Externas)", HttpStatus.INTERNAL_SERVER_ERROR, null);

			}

			if (suplencias.isEmpty()) {

				return ResponseHandler.generateResponse("No existen Suplencias del empleado en el Sistema", HttpStatus.NOT_FOUND, null);
			}

			return ResponseHandler.generateResponse("Se encontraron los registros de Suplencias del empleado en el Sistema", HttpStatus.OK, suplencias);

		} catch (Exception e) {
			logger.info(e.toString());

			return ResponseHandler.generateResponse("Error al obtener los registros de Suplencias del empleado en el Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Obtener una suplencia en el sistema mediante el ID", description = "Obtener una suplencia en el sistema mediante el ID", tags = { "Suplencia" })
	@GetMapping("/suplenciaPorId")
	public ResponseEntity<Object> getSuplenciaById(
			@Parameter(description = "ID de la guardia del empleado", required = true) @RequestParam(required = true) Integer idSuplencia,
			@Parameter(description = "Tipo para obtener las guardidas del empleado ('SI': Internas o 'SE': Externas)", required = true) @RequestParam(required = true) String tipoSuplencia) {

		DatosSuplencia suplencia = null;
		DatosEmpleado empleado, empleado_sup;

		try {

			switch (tipoSuplencia) {

				case "SI":
					suplencia = suplenciaRepository.findById(idSuplencia);
					suplencia.setTipo_suplencia("SI");
					try {
						empleado = empleadoRepository.getDatosEmpleado(suplencia.getFec_paga(), suplencia.getClave_empleado());
					} catch (Exception ex) {
						empleado = new DatosEmpleado();
					}
					suplencia.setEmpleado(empleado);
					try {
						empleado_sup = empleadoRepository.getDatosEmpleado(suplencia.getFec_paga(), suplencia.getClave_empleado_suplir());						
					} catch (Exception ex) {
						empleado_sup = new DatosEmpleado();
					}
					suplencia.setEmpleado_suplir(empleado_sup);
					break;

				case "SE":
					suplencia = suplenciaRepository.findByIdExt(idSuplencia);
					suplencia.setTipo_suplencia("SE");

					try {
						empleado_sup = empleadoRepository.getDatosEmpleado(suplencia.getFec_paga(), suplencia.getClave_empleado_suplir());						
					} catch (Exception ex) {
						empleado_sup = new DatosEmpleado();
					}
					suplencia.setEmpleado_suplir(empleado_sup);

					try {
						BolsaTrabajo bolsa = bolsaTrabajoRepository.findByRFC(suplencia.getClave_empleado(), empleado_sup.getId_delegacion());
						empleado = new DatosEmpleado();
						empleado.setClave_empleado(bolsa.getRfc());
						empleado.setNombre(bolsa.getNombre());
						empleado.setApellido_1(bolsa.getApellidoPat());
						empleado.setApellido_2(bolsa.getApellidoMat());
					} catch (Exception ex) {
						empleado = new DatosEmpleado();
					}
					suplencia.setEmpleado(empleado);

					break;

				default:
					return ResponseHandler.generateResponse("No se indicó el tipo de suplencia correctamente ('SI': Internas o 'SE': Externas)", HttpStatus.INTERNAL_SERVER_ERROR, null);
			
			}

			return ResponseHandler.generateResponse("Se encontró la suplencia en el sistema mediante el ID", HttpStatus.OK, suplencia);
		} catch ( Exception ex ) {
			logger.info(ex.toString());

			return ResponseHandler.generateResponse("No se encontró la suplencia en el sistema mediante el ID", HttpStatus.NOT_FOUND, null);
		}
	}

	@Operation(summary = "Obtener el saldo utilizado por registro de suplencias para una delegación en un año determinado", description = "Obtener el saldo utilizado por registro de suplencias para una delegación en un año determinado", tags = { "Suplencia" })
	@GetMapping("/suplencias/saldo")
	public ResponseEntity<Object> getSaldoSuplencia(
			@Parameter(description = "ID de la Delegación que se consulta el presupuesto", required = true) @RequestParam(required = true) String idDelegacion,
			@Parameter(description = "Año del ejercicio en que se consulta el presupuesto", required = true) @RequestParam(required = true) Integer anio_ejercicio,
			@Parameter(description = "Mes del ejercicio en que se consulta el presupuesto", required = true) @RequestParam(required = true) Integer mes_ejercicio,
			@Parameter(description = "Quincena del ejercicio del que se consulta el saldo utilizado", required = false) @RequestParam(required = true) Integer quincena,
			@Parameter(description = "Tipo para obtener las suplencias del empleado ('SI': Internas o 'SE': Externas)", required = false) @RequestParam(required = false) String tipoSuplencia,
			@Parameter(description = "ID del Centro de Trabajo del que se consulta el saldo utilizado", required = false) @RequestParam(required = false) String idCentroTrab) {

		double saldo=0;

		if (tipoSuplencia != null) {

			switch (tipoSuplencia) {

				case "SI":
					if (idCentroTrab == null) {
						saldo = suplenciaRepository.ObtenerSaldoUtilizado(idDelegacion, anio_ejercicio, mes_ejercicio);
					} else {
						saldo = suplenciaRepository.ObtenerSaldoUtilizado_ct(0, idCentroTrab, anio_ejercicio, mes_ejercicio, quincena);
					}
					break;

				case "SE":
					if (idCentroTrab == null) {
						saldo = suplenciaRepository.ObtenerSaldoUtilizadoExt(idDelegacion, anio_ejercicio, mes_ejercicio);
					} else {
						saldo = suplenciaRepository.ObtenerSaldoUtilizadoExt_ct(0, idCentroTrab, anio_ejercicio, mes_ejercicio, quincena);
					}
					break;

				default:
					return ResponseHandler.generateResponse("No se indicó el tipo de suplencia correctamente ('SI': Internas o 'SE': Externas)", HttpStatus.INTERNAL_SERVER_ERROR, null);

			}

		} else {
			saldo = suplenciaRepository.ObtenerSaldoUtilizado(idDelegacion, anio_ejercicio, mes_ejercicio) + suplenciaRepository.ObtenerSaldoUtilizadoExt(idDelegacion, anio_ejercicio, mes_ejercicio); 
		}
		return ResponseHandler.generateResponse("Se obtuvo el saldo utilizado para guardias para las condiciones indicadas", HttpStatus.OK, saldo);
	}

	@Operation(summary = "Agrega un nuevo registro de Suplencia al Sistema", description = "Agrega un nuevo registro de Suplencia al Sistema", tags = { "Suplencia" })
	@PostMapping("/suplencias")
	public ResponseEntity<Object> agregarSuplencia(
			@Parameter(description = "Objeto de Suplencia que se creará en el Sistema") @RequestBody DatosSuplencia suplencia) {
		try {

			int id = 0, anio, mes;
			int maxHorasEnQnaInt = 0, maxHorasEnQnaExt = 0,
				maxDiasEnQnaInt = 0,  maxDiasEnQnaExt = 0;
			double saldo_utilizado=0, saldo=0;
			Presupuesto presup;
			Paga paga;

			String fec_pago = suplencia.getFec_paga();
			String inicio, fin;

			try {
				paga   = pagaRepository.findByFecha(fec_pago);
				anio   = paga.getAnio_ejercicio();
				mes    = paga.getMes_ejercicio();
				inicio = paga.getFec_inicio();
				fin    = paga.getFec_fin();
			} catch (EmptyResultDataAccessException e) {
				return ResponseHandler.generateResponse("No existe la fecha de pago indicada", HttpStatus.INTERNAL_SERVER_ERROR, null);
			}

			DatosEmpleado empleado = empleadoRepository.getDatosEmpleado(suplencia.getFec_paga(), suplencia.getEmpleado_suplir().getClave_empleado());

			switch (suplencia.getTipo_suplencia()) {

				case "SI":
					if (suplenciaRepository.existe_suplencia(suplencia) > 0)
						return ResponseHandler.generateResponse("Existe un registro de Suplencia del suplente, en ese mismo periodo, favor de validar", HttpStatus.INTERNAL_SERVER_ERROR, null);
					if (suplenciaRepository.existe_suplente(suplencia) > 0)
						return ResponseHandler.generateResponse("Existe un registro de Suplencia del empleado a suplir, en ese mismo periodo, favor de validar", HttpStatus.INTERNAL_SERVER_ERROR, null);

					maxHorasEnQnaInt = suplenciaRepository.get_horas_suplencia(suplencia.getClave_empleado(), inicio, fin);
					maxDiasEnQnaInt  = suplenciaRepository.get_dias_suplencia(suplencia.getClave_empleado(), inicio, fin);

					switch (suplencia.getId_clave_movimiento()) {

						case "01", "02", "05":
							// Valida horas por quincena
							if (maxHorasEnQnaInt + (int) (suplencia.getDias() * Double.parseDouble(empleado.getId_tipo_jornada())) > 88)
								return ResponseHandler.generateResponse("No se puede exceder de 88 horas a la quincena en el registro de suplencias. Acualmente cuenta con " + maxHorasEnQnaInt
										+ " más " + suplencia.getDias() * (int) Double.parseDouble(empleado.getId_tipo_jornada()) + " del registro actual, exceden ese límite.",
										HttpStatus.INTERNAL_SERVER_ERROR, null);
							// Valida días por quincena
							if(maxDiasEnQnaInt + suplencia.getDias() > 12)
								return ResponseHandler.generateResponse("No se puede exceder de 12 días a la quincena en el registro de suplencias. Acualmente cuenta con " + maxDiasEnQnaInt
										+ " más " + suplencia.getDias() * (int) Double.parseDouble(empleado.getId_tipo_jornada()) + " del registro actual, exceden ese límite.",
										HttpStatus.INTERNAL_SERVER_ERROR, null);
							break;

						case "03":
							// Valida horas por quincena
							if (maxHorasEnQnaInt + (int) (suplencia.getDias() * Double.parseDouble(empleado.getId_tipo_jornada())) > 96)
								return ResponseHandler.generateResponse("No se puede exceder de 96 horas a la quincena en el registro de suplencias. Acualmente cuenta con " + maxHorasEnQnaInt
										+ " más " + suplencia.getDias() * (int) Double.parseDouble(empleado.getId_tipo_jornada()) + " del registro actual, exceden ese límite.",
										HttpStatus.INTERNAL_SERVER_ERROR, null);
							// Valida días por quincena
							if(maxDiasEnQnaInt + suplencia.getDias() > 4)
								return ResponseHandler.generateResponse("No se puede exceder de 4 días a la quincena en el registro de suplencias. Acualmente cuenta con " + maxDiasEnQnaInt
										+ " más " + suplencia.getDias() * (int) Double.parseDouble(empleado.getId_tipo_jornada()) + " del registro actual, exceden ese límite.",
										HttpStatus.INTERNAL_SERVER_ERROR, null);
							break;

						case "04":
							// Valida horas por quincena
							if (maxHorasEnQnaInt + (int) (suplencia.getDias() * Double.parseDouble(empleado.getId_tipo_jornada())) > 84)
								return ResponseHandler.generateResponse("No se puede exceder de 84 horas a la quincena en el registro de suplencias. Acualmente cuenta con " + maxHorasEnQnaInt
										+ " más " + suplencia.getDias() * (int) Double.parseDouble(empleado.getId_tipo_jornada()) + " del registro actual, exceden ese límite.",
										HttpStatus.INTERNAL_SERVER_ERROR, null);
							// Valida días por quincena
							if(maxDiasEnQnaInt + suplencia.getDias() > 7)
								return ResponseHandler.generateResponse("No se puede exceder de 7 días a la quincena en el registro de suplencias. Acualmente cuenta con " + maxDiasEnQnaInt
										+ " más " + suplencia.getDias() * (int) Double.parseDouble(empleado.getId_tipo_jornada()) + " del registro actual, exceden ese límite.",
										HttpStatus.INTERNAL_SERVER_ERROR, null);
							break;

						default:
							return ResponseHandler.generateResponse("No se indicó el tipo de guardia correctamente ('GI': Internas o 'GE': Externas)", HttpStatus.INTERNAL_SERVER_ERROR, null);
					}
					break;

				case "SE":
					if (suplenciaRepository.existe_suplenciaExt(suplencia) > 0)
						return ResponseHandler.generateResponse("Existe un registro de Suplencia del suplente, en ese mismo periodo, favor de validar", HttpStatus.INTERNAL_SERVER_ERROR, null);
					if (suplenciaRepository.existe_suplenteExt(suplencia) > 0)
						return ResponseHandler.generateResponse("Existe un registro de Suplencia del empleado a suplir, en ese mismo periodo, favor de validar", HttpStatus.INTERNAL_SERVER_ERROR, null);

					maxHorasEnQnaExt = suplenciaRepository.get_horas_suplencia_ext(suplencia.getClave_empleado(), inicio, fin);
					maxDiasEnQnaExt  = suplenciaRepository.get_dias_suplencia_ext(suplencia.getClave_empleado(), inicio, fin);

					switch (suplencia.getId_clave_movimiento()) {

						case "01", "02", "05":
							// Valida horas por quincena
							if (maxHorasEnQnaExt + (int) (suplencia.getDias() * Double.parseDouble(empleado.getId_tipo_jornada())) > 88)
								return ResponseHandler.generateResponse("No se puede exceder de 88 horas a la quincena en el registro de suplencias. Acualmente cuenta con " + maxHorasEnQnaExt
										+ " más " + suplencia.getDias() * (int) Double.parseDouble(empleado.getId_tipo_jornada()) + " del registro actual, exceden ese límite.",
										HttpStatus.INTERNAL_SERVER_ERROR, null);
							// Valida días por quincena
							if(maxDiasEnQnaExt + suplencia.getDias() > 12)
								return ResponseHandler.generateResponse("No se puede exceder de 12 días a la quincena en el registro de suplencias. Acualmente cuenta con " + maxDiasEnQnaExt
										+ " más " + suplencia.getDias() * (int) Double.parseDouble(empleado.getId_tipo_jornada()) + " del registro actual, exceden ese límite.",
										HttpStatus.INTERNAL_SERVER_ERROR, null);
							break;

						case "03":
							// Valida horas por quincena
							if (maxHorasEnQnaExt + (int) (suplencia.getDias() * Double.parseDouble(empleado.getId_tipo_jornada())) > 96)
								return ResponseHandler.generateResponse("No se puede exceder de 96 horas a la quincena en el registro de suplencias. Acualmente cuenta con " + maxHorasEnQnaExt
										+ " más " + suplencia.getDias() * (int) Double.parseDouble(empleado.getId_tipo_jornada()) + " del registro actual, exceden ese límite.",
										HttpStatus.INTERNAL_SERVER_ERROR, null);
							// Valida días por quincena
							if(maxDiasEnQnaExt + suplencia.getDias() > 4)
								return ResponseHandler.generateResponse("No se puede exceder de 4 días a la quincena en el registro de suplencias. Acualmente cuenta con " + maxDiasEnQnaExt
										+ " más " + suplencia.getDias() * (int) Double.parseDouble(empleado.getId_tipo_jornada()) + " del registro actual, exceden ese límite.",
										HttpStatus.INTERNAL_SERVER_ERROR, null);
							break;

						case "04":
							// Valida horas por quincena
							if (maxHorasEnQnaExt + (int) (suplencia.getDias() * Double.parseDouble(empleado.getId_tipo_jornada())) > 84)
								return ResponseHandler.generateResponse("No se puede exceder de 84 horas a la quincena en el registro de suplencias. Acualmente cuenta con " + maxHorasEnQnaExt
										+ " más " + suplencia.getDias() * (int) Double.parseDouble(empleado.getId_tipo_jornada()) + " del registro actual, exceden ese límite.",
										HttpStatus.INTERNAL_SERVER_ERROR, null);
							// Valida días por quincena
							if(maxDiasEnQnaExt + suplencia.getDias() > 7)
								return ResponseHandler.generateResponse("No se puede exceder de 7 días a la quincena en el registro de suplencias. Acualmente cuenta con " + maxDiasEnQnaExt
										+ " más " + suplencia.getDias() * (int) Double.parseDouble(empleado.getId_tipo_jornada()) + " del registro actual, exceden ese límite.",
										HttpStatus.INTERNAL_SERVER_ERROR, null);
							break;

						default:
							return ResponseHandler.generateResponse("No se indicó el tipo de guardia correctamente ('GI': Internas o 'GE': Externas)", HttpStatus.INTERNAL_SERVER_ERROR, null);
					}
					break;

				default:
					return ResponseHandler.generateResponse("No se indicó el tipo de suplencia correctamente ('SI': Internas o 'SE': Externas)", HttpStatus.INTERNAL_SERVER_ERROR, null);
			}

			int riesgos = empleadoRepository.ConsultaRiesgosEmp(suplencia.getEmpleado_suplir().getClave_empleado(), suplencia.getFec_paga());
			suplencia.setRiesgos(riesgos);

			String idCentroTrab = empleado.getId_centro_trabajo();

			// Validaciones presupuestales
			try {			
				presup = presupuestoRepository.getElementByType_ct(idCentroTrab, suplencia.getTipo_suplencia(), anio, mes);
			} catch (EmptyResultDataAccessException e) {
				return ResponseHandler.generateResponse("No existe presupuesto registrado para realizar este tipo de movimiento", HttpStatus.INTERNAL_SERVER_ERROR, null);
			}

			saldo = (presup != null) ? presup.getSaldo(): 0; 

			switch (suplencia.getTipo_suplencia()) {
				case "SI":
					saldo_utilizado = suplenciaRepository.ObtenerSaldoUtilizado_ct(0, idCentroTrab, presup.getAnio(), presup.getMes(), presup.getQuincena() );
					break;
				case "SE":
					saldo_utilizado = suplenciaRepository.ObtenerSaldoUtilizadoExt_ct(0, idCentroTrab, presup.getAnio(), presup.getMes(), presup.getQuincena());
					break;
				default:
					return ResponseHandler.generateResponse("No se indicó el tipo de suplencia correctamente ('SI': Internas o 'SE': Externas)", HttpStatus.INTERNAL_SERVER_ERROR, null);
			}

			// Fin de las Validaciones presupuestales

			double importe = suplenciaService.CalculaImporteSuplencia(suplencia.getFec_paga(), 
					empleado, suplencia.getDias(), suplencia.getTipo_suplencia(), riesgos);

			if (importe + saldo_utilizado <= saldo) {

				id = suplenciaService.GuardarSuplencia(suplencia, importe, empleado);

			} else {

				return ResponseHandler.generateResponse("No existe presupuesto suficiente para realizar este tipo de registro", HttpStatus.INTERNAL_SERVER_ERROR, null);
			}

			return ResponseHandler.generateResponse("El registro de Suplencia ha sido guardado de manera exitosa, con ID = " + id, HttpStatus.OK, null);
		} catch (Exception e) {

			return ResponseHandler.generateResponse("Error al agregar un nuevo registro de Suplencia al Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Actualizar importes de Suplencias en el Sistema", description = "Actualizar importes de Suplencias en el Sistema", tags = { "Suplencia" })
	@PutMapping("/suplencias/actualiza")
	public ResponseEntity<Object> actualizaImportes(
			@Parameter(description = "Fecha de quincena para el cálculo del importe de la Suplencia", required = true) @RequestParam(required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date quincena,
			@Parameter(description = "Tipo para obtener las guardidas del empleado ('I': Internas o 'E': Externas)", required = true) @RequestParam(required = true) String tipoSuplencia ) {
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String strQuincena = dateFormat.format(quincena);
			suplenciaService.ActualizaImportesSuplencias(strQuincena, tipoSuplencia);

			return ResponseHandler.generateResponse("Los importes de las Suplencias se actualizaron de manera exitósa", HttpStatus.OK, null);
		} catch (Exception e) {

			return ResponseHandler.generateResponse("Error al Actualizar los importes de la Suplencia en el Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Actualiza un registro de suplencia en el Sistema", description = "Actualiza un registro de suplencia en el Sistema", tags = { "Suplencia" })
	@PutMapping("/suplencias")
	public ResponseEntity<Object> actualizarSuplencia(
			@Parameter(description = "Objeto de la guardia a actualizarse en el Sistema") @RequestBody DatosSuplencia suplencia) {
		try {

			int anio, mes;
			int maxHorasEnQnaInt = 0, maxHorasEnQnaExt = 0,
					maxDiasEnQnaInt = 0,  maxDiasEnQnaExt = 0;
			double saldo_utilizado=0, saldo=0;
			Presupuesto presup;
			Paga paga;

			String fec_pago = suplencia.getFec_paga();
			String inicio, fin;

			try {
				paga   = pagaRepository.findByFecha(fec_pago);
				anio   = paga.getAnio_ejercicio();
				mes    = paga.getMes_ejercicio();
				inicio = paga.getFec_inicio();
				fin    = paga.getFec_fin();
			} catch (EmptyResultDataAccessException e) {
				return ResponseHandler.generateResponse("No existe la fecha de pago indicada", HttpStatus.INTERNAL_SERVER_ERROR, null);
			}

			DatosEmpleado empleado = empleadoRepository.getDatosEmpleado(suplencia.getFec_paga(), suplencia.getEmpleado_suplir().getClave_empleado());

			switch (suplencia.getTipo_suplencia()) {

				case "SI":
					if (suplenciaRepository.existe_suplencia_upd(suplencia)>0)
						return ResponseHandler.generateResponse("Existe un registro de Suplencia en ese mismo periodo", HttpStatus.INTERNAL_SERVER_ERROR, null);

					if (suplenciaRepository.existe_suplente_upd(suplencia) > 0)
						return ResponseHandler.generateResponse("Existe un registro de Suplencia del empleado a suplir, en ese mismo periodo, favor de validar", HttpStatus.INTERNAL_SERVER_ERROR, null);

					maxHorasEnQnaInt = suplenciaRepository.get_horas_suplencia_upd(suplencia.getClave_empleado(), suplencia.getId(), inicio, fin);
					maxDiasEnQnaInt  = suplenciaRepository.get_dias_suplencia_upd(suplencia.getClave_empleado(), suplencia.getId(), inicio, fin);

					switch (suplencia.getId_clave_movimiento()) {

						case "01", "02", "05":
							// Valida horas por quincena
							if (maxHorasEnQnaInt + (int) (suplencia.getDias() * Double.parseDouble(empleado.getId_tipo_jornada())) > 88)
								return ResponseHandler.generateResponse("No se puede exceder de 88 horas a la quincena en el registro de suplencias. Acualmente cuenta con " + maxHorasEnQnaInt 
										+ " más " + suplencia.getDias() * Integer.parseInt(empleado.getId_tipo_jornada()) + " del registro actual, exceden ese límite.",
										HttpStatus.INTERNAL_SERVER_ERROR, null);
							// Valida días por quincena
							if(maxDiasEnQnaInt + suplencia.getDias() > 12)
								return ResponseHandler.generateResponse("No se puede exceder de 12 días a la quincena en el registro de suplencias. Acualmente cuenta con " + maxDiasEnQnaInt 
										+ " más " + suplencia.getDias() + " del registro actual, exceden ese límite.",
										HttpStatus.INTERNAL_SERVER_ERROR, null);
							break;

						case "03":
							// Valida horas por quincena
							if (maxHorasEnQnaInt + (int) (suplencia.getDias() * Double.parseDouble(empleado.getId_tipo_jornada())) > 96)
								return ResponseHandler.generateResponse("No se puede exceder de 96 horas a la quincena en el registro de suplencias. Acualmente cuenta con " + maxHorasEnQnaInt 
										+ " más " + suplencia.getDias() * Integer.parseInt(empleado.getId_tipo_jornada()) + " del registro actual, exceden ese límite.",
										HttpStatus.INTERNAL_SERVER_ERROR, null);
							// Valida días por quincena
							if(maxDiasEnQnaInt + suplencia.getDias() > 4)
								return ResponseHandler.generateResponse("No se puede exceder de 4 días a la quincena en el registro de suplencias. Acualmente cuenta con " + maxDiasEnQnaInt 
										+ " más " + suplencia.getDias() + " del registro actual, exceden ese límite.",
										HttpStatus.INTERNAL_SERVER_ERROR, null);
							break;

						case "04":
							// Valida horas por quincena
							if (maxHorasEnQnaInt + (int) (suplencia.getDias() * Double.parseDouble(empleado.getId_tipo_jornada())) > 84)
								return ResponseHandler.generateResponse("No se puede exceder de 84 horas a la quincena en el registro de suplencias. Acualmente cuenta con " + maxHorasEnQnaInt 
										+ " más " + suplencia.getDias() * Integer.parseInt(empleado.getId_tipo_jornada()) + " del registro actual, exceden ese límite.",
										HttpStatus.INTERNAL_SERVER_ERROR, null);
							// Valida días por quincena
							if(maxDiasEnQnaInt + suplencia.getDias() > 7)
								return ResponseHandler.generateResponse("No se puede exceder de 7 días a la quincena en el registro de suplencias. Acualmente cuenta con " + maxDiasEnQnaInt 
										+ " más " + suplencia.getDias() + " del registro actual, exceden ese límite.",
										HttpStatus.INTERNAL_SERVER_ERROR, null);
							break;

						default:
							return ResponseHandler.generateResponse("No se indicó el tipo de guardia correctamente ('GI': Internas o 'GE': Externas)", HttpStatus.INTERNAL_SERVER_ERROR, null);
					}

					break;

				case "SE":
					if (suplenciaRepository.existe_suplenciaExt_upd(suplencia) > 0)
						return ResponseHandler.generateResponse("Existe un registro de Suplencia del suplente, en ese mismo periodo, favor de validar", HttpStatus.INTERNAL_SERVER_ERROR, null);
					if (suplenciaRepository.existe_suplenteExt_upd(suplencia) > 0)
						return ResponseHandler.generateResponse("Existe un registro de Suplencia del empleado a suplir, en ese mismo periodo, favor de validar", HttpStatus.INTERNAL_SERVER_ERROR, null);

					maxHorasEnQnaExt = suplenciaRepository.get_horas_suplencia_ext_upd(suplencia.getClave_empleado(), suplencia.getId(), inicio, fin);
					maxDiasEnQnaExt  = suplenciaRepository.get_dias_suplencia_ext_upd(suplencia.getClave_empleado(), suplencia.getId(), inicio, fin);

					switch (suplencia.getId_clave_movimiento()) {

						case "01", "02", "05":
							// Valida horas por quincena
							if (maxHorasEnQnaExt + (int) (suplencia.getDias() * Double.parseDouble(empleado.getId_tipo_jornada())) > 88)
								return ResponseHandler.generateResponse("No se puede exceder de 88 horas a la quincena en el registro de suplencias. Acualmente cuenta con " + maxHorasEnQnaExt 
										+ " más " + suplencia.getDias() * Integer.parseInt(empleado.getId_tipo_jornada()) + " del registro actual, exceden ese límite.",
										HttpStatus.INTERNAL_SERVER_ERROR, null);
							// Valida días por quincena
							if(maxDiasEnQnaExt + suplencia.getDias() > 12)
								return ResponseHandler.generateResponse("No se puede exceder de 12 días a la quincena en el registro de suplencias. Acualmente cuenta con " + maxDiasEnQnaExt 
										+ " más " + suplencia.getDias() + " del registro actual, exceden ese límite.",
										HttpStatus.INTERNAL_SERVER_ERROR, null);
							break;

						case "03":
							// Valida horas por quincena
							if (maxHorasEnQnaExt + (int) (suplencia.getDias() * Double.parseDouble(empleado.getId_tipo_jornada())) > 96)
								return ResponseHandler.generateResponse("No se puede exceder de 96 horas a la quincena en el registro de suplencias. Acualmente cuenta con " + maxHorasEnQnaExt 
										+ " más " + suplencia.getDias() * Integer.parseInt(empleado.getId_tipo_jornada()) + " del registro actual, exceden ese límite.",
										HttpStatus.INTERNAL_SERVER_ERROR, null);
							// Valida días por quincena
							if(maxDiasEnQnaExt + suplencia.getDias() > 4)
								return ResponseHandler.generateResponse("No se puede exceder de 4 días a la quincena en el registro de suplencias. Acualmente cuenta con " + maxDiasEnQnaExt 
										+ " más " + suplencia.getDias() + " del registro actual, exceden ese límite.",
										HttpStatus.INTERNAL_SERVER_ERROR, null);
							break;

						case "04":
							// Valida horas por quincena
							if (maxHorasEnQnaExt + (int) (suplencia.getDias() * Double.parseDouble(empleado.getId_tipo_jornada())) > 84)
								return ResponseHandler.generateResponse("No se puede exceder de 84 horas a la quincena en el registro de suplencias. Acualmente cuenta con " + maxHorasEnQnaExt 
										+ " más " + suplencia.getDias() * Integer.parseInt(empleado.getId_tipo_jornada()) + " del registro actual, exceden ese límite.",
										HttpStatus.INTERNAL_SERVER_ERROR, null);
							// Valida días por quincena
							if(maxDiasEnQnaExt + suplencia.getDias() > 7)
								return ResponseHandler.generateResponse("No se puede exceder de 7 días a la quincena en el registro de suplencias. Acualmente cuenta con " + maxDiasEnQnaExt 
										+ " más " + suplencia.getDias() + " del registro actual, exceden ese límite.",
										HttpStatus.INTERNAL_SERVER_ERROR, null);
							break;

						default:
							return ResponseHandler.generateResponse("No se indicó el tipo de guardia correctamente ('GI': Internas o 'GE': Externas)", HttpStatus.INTERNAL_SERVER_ERROR, null);
					}
					
					break;

				default:
					return ResponseHandler.generateResponse("No se indicó el tipo de suplencia correctamente ('SI': Internas o 'SE': Externas)", HttpStatus.INTERNAL_SERVER_ERROR, null);
			}

			int riesgos = empleadoRepository.ConsultaRiesgosEmp(suplencia.getEmpleado_suplir().getClave_empleado(), suplencia.getFec_paga());

			String idCentroTrab = empleado.getId_centro_trabajo();

			// Validaciones presupuestales
			try {
				anio = pagaRepository.findByFecha(fec_pago).getAnio_ejercicio();
				mes = pagaRepository.findByFecha(fec_pago).getMes_ejercicio();
			} catch (EmptyResultDataAccessException e) {
				return ResponseHandler.generateResponse("No existe la fecha de pago indicada", HttpStatus.INTERNAL_SERVER_ERROR, null);
			}
			try {			
				presup = presupuestoRepository.getElementByType_ct(idCentroTrab, suplencia.getTipo_suplencia(), anio, mes);
			} catch (EmptyResultDataAccessException e) {
				return ResponseHandler.generateResponse("No existe presupuesto registrado para realizar este tipo de movimiento", HttpStatus.INTERNAL_SERVER_ERROR, null);
			}

			saldo = (presup != null) ? presup.getSaldo(): 0; 

			switch (suplencia.getTipo_suplencia()) {

				case "SI":
					saldo_utilizado = suplenciaRepository.ObtenerSaldoUtilizado_ct(suplencia.getId(), idCentroTrab, presup.getAnio(), presup.getMes(), presup.getQuincena());
					break;

				case "SE":
					saldo_utilizado = suplenciaRepository.ObtenerSaldoUtilizadoExt_ct(suplencia.getId(), idCentroTrab, presup.getAnio(), presup.getMes(), presup.getQuincena());
					break;

				default:
					return ResponseHandler.generateResponse("No se indicó el tipo de suplencia correctamente ('SI': Internas o 'SE': Externas)", HttpStatus.INTERNAL_SERVER_ERROR, null);
			}
			// Fin de las Validaciones presupuestales

			double importe = suplenciaService.CalculaImporteSuplencia(suplencia.getFec_paga(), 
					suplencia.getEmpleado_suplir().getClave_empleado(), suplencia.getDias(), suplencia.getTipo_suplencia(), riesgos);

			if (importe + saldo_utilizado <= saldo) {
				suplenciaService.actualizaSuplencia(suplencia, importe);
			} else {
				return ResponseHandler.generateResponse("No existe presupuesto suficiente para realizar la actualización del registro", HttpStatus.INTERNAL_SERVER_ERROR, null);
			}

			return ResponseHandler.generateResponse("El registro de guardia ha sido actualizado de manera exitosa", HttpStatus.OK, null);
		} catch (Exception e) {

			return ResponseHandler.generateResponse("Error al actualizar un registro de suplencia en el Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Eliminar un registro de suplencia del Sistema", description = "Eliminar un registro de suplencia del Sistema", tags = { "Suplencia" })
	@DeleteMapping("/suplencias")
	public ResponseEntity<Object> eliminarSuplencia(
			@Parameter(description = "Número de ID para obtener la suplencia del empleado", required = true) @RequestParam(required = true) Integer idGuardia,
			@Parameter(description = "Tipo para obtener la suplencia del empleado (I-Interna o E-Externa)", required = true) @RequestParam(required = true) String tipoGuardia) {
		try {
			suplenciaService.eliminarSuplencia(idGuardia, tipoGuardia);

			return ResponseHandler.generateResponse("El un registro de suplencia fué eliminado exitosamente.", HttpStatus.OK, null);
		} catch (Exception e) {

			return ResponseHandler.generateResponse("Error al consultar el importe de la suplencia conforme a los criterios establecidos", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Actualizar importes de Suplencias en el Sistema", description = "Actualizar importes de Suplencias en el Sistema", tags = { "Suplencia" })
	@PutMapping("/suplencias/actualiza2")
	public ResponseEntity<Object> actualizaImportes2(
			@Parameter(description = "Fecha de quincena para el cálculo del importe de la Suplencia", required = true) @RequestParam(required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date quincena,
			@Parameter(description = "Tipo para obtener las guardidas del empleado ('I': Internas o 'E': Externas)", required = true) @RequestParam(required = true) String tipoSuplencia ) {
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String strQuincena = dateFormat.format(quincena);
			suplenciaService.ActualizaImportesSuplencias2(strQuincena, tipoSuplencia);

			return ResponseHandler.generateResponse("Los importes de las Suplencias se actualizaron de manera exitósa", HttpStatus.OK, null);
		} catch (Exception e) {

			return ResponseHandler.generateResponse("Error al actualizar el importe de la suplencia", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Actualizar Estatus de Suplencias en el Sistema", description = "Actualizar Estatus de Suplencias en el Sistema", tags = { "Suplencia" })
	@PutMapping("/suplencias/actualizaEstatus")
	public ResponseEntity<Object> actualizaEstatus(
			@Parameter(description = "ID de la Suplencia", required = true) @RequestParam(required = true) Integer idSuplencia,
			@Parameter(description = "Estatus de la suplencia", required = true) @RequestParam(required = true) Integer estatus,
			@Parameter(description = "Tipo de Suplencia para calcular el importe de la Suplencia", required = true) @RequestParam(required = true) String tipo,
			@Parameter(description = "ID del usaurio que realiza la actualización del estatus", required = true) @RequestParam(required = true) Integer idUsuario,
			@Parameter(description = "Comentarios correspondientes a la actualización del estatus", required = false) @RequestParam(required = false) String comentarios) {

		if (comentarios == null) comentarios = "";

		try {

			switch (estatus) {

				case 1, 2:

					suplenciaRepository.updateAuthStatusSuplencia1(estatus, idSuplencia, tipo, comentarios, idUsuario);
					break;

				case 3, 4:

					suplenciaRepository.updateAuthStatusSuplencia2(estatus, idSuplencia, tipo, comentarios, idUsuario);
					break;

				default:
					return ResponseHandler.generateResponse("No se indicó el tipo de guardia correctamente ('GI': Internas o 'GE': Externas)", HttpStatus.INTERNAL_SERVER_ERROR, null);

			}

			return ResponseHandler.generateResponse("El estatus de la suplencia se actualizó de manera exitósa", HttpStatus.OK, null);
		} catch (Exception e) {

			return ResponseHandler.generateResponse("Error al Actualizar los importes de la Suplencia en el Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Actualizar Estatus de Suplencias en el Sistema", description = "Actualizar Estatus de Suplencias en el Sistema", tags = { "Suplencia" })
	@PutMapping("/suplencias/validacion_suplencias")
	public ResponseEntity<Object> validacionSuplencias(
			@Parameter(description = "Fecha de control para validar las suplencias", required = true) @RequestParam(required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fec_pago,
			@Parameter(description = "Estatus a actualizar de las suplencias (1 - Autorización, 3 - Confirmación)", required = true) @RequestParam(required = true) Integer estatus,
			@Parameter(description = "Tipo de Suplencia para realizar la validación", required = true) @RequestParam(required = true) String tipo,
			@Parameter(description = "ID del usaurio que realiza la actualización del estatus", required = true) @RequestParam(required = true) Integer idUsuario ) {

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String strFecha = dateFormat.format(fec_pago);

		try {

			switch (estatus) {

				case 1:

					switch (tipo) {
					
						case "SI":
								suplenciaRepository.updateAuthStatusSuplencias1(tipo, strFecha, idUsuario);
							break;

						case "SE":
								suplenciaRepository.updateAuthStatusSuplencias1Ext(tipo, strFecha, idUsuario);
							break;

						default:
							return ResponseHandler.generateResponse("No se indicó el tipo de suplencia correctamente ('SI': Internas o 'SE': Externas)", HttpStatus.INTERNAL_SERVER_ERROR, null);
					}
					break;

				case 3:

					switch (tipo) {
					
						case "SI":
								suplenciaRepository.updateAuthStatusSuplencias2(tipo, strFecha, idUsuario);
							break;
	
						case "SE":
								suplenciaRepository.updateAuthStatusSuplencias2Ext(tipo, strFecha, idUsuario);
							break;

						default:
							return ResponseHandler.generateResponse("No se indicó el tipo de suplencia correctamente ('SI': Internas o 'SE': Externas)", HttpStatus.INTERNAL_SERVER_ERROR, null);
					}
					break;

				default:
					return ResponseHandler.generateResponse("No se indicó el estatus de validación de la suplencia correctamente ('1': Autorizado o '3': Confirmado)", HttpStatus.INTERNAL_SERVER_ERROR, null);

			}

			return ResponseHandler.generateResponse("El estatus de la suplencia se actualizó de manera exitósa", HttpStatus.OK, null);
		} catch (Exception e) {

			return ResponseHandler.generateResponse("Error al Actualizar los importes de la Suplencia en el Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Obtener los registros de suplencias del empleado en el Sistema", description = "Obtener los registros de suplencias del empleado en el Sistema", tags = { "Suplencia" })
	@GetMapping("/suplencias/consulta")
	public ResponseEntity<Object> getDynamicSuplencias(
			@Parameter(description = "Fecha de quincena para la consulta de suplencias", required = false) @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date quincena,
			@Parameter(description = "Tipo para obtener las suplencias (SI-Internas o SE-Externas)", required = true) @RequestParam(required = true) String tipoSuplencia,
			@Parameter(description = "ID de empleado o RFC para obtener las suplencias", required = false) @RequestParam(required = false) String claveEmpleado,
			@Parameter(description = "Importe mínimo para obtener las suplencias", required = false) @RequestParam(required = false) Double importe_min,
			@Parameter(description = "Importe máximo para obtener las suplencias", required = false) @RequestParam(required = false) Double importe_max,
			@Parameter(description = "ID de la delegación para obtener las suplencias", required = false) @RequestParam(required = false) String idDelegacion,
			@Parameter(description = "ID del centro de trabajo para obtener las suplencias", required = false) @RequestParam(required = false) String idCentroTrab,
			@Parameter(description = "Clave del servicio para obtener las suplencias", required = false) @RequestParam(required = false) String claveServicio,
			@Parameter(description = "Clave del puesto para obtener las suplencias", required = false) @RequestParam(required = false) String puesto,
			@Parameter(description = "Clave del empleado a suplir para obtener las suplencias", required = false) @RequestParam(required = false) String emp_suplir,
			@Parameter(description = "Estatus de las suplencias", required = false) @RequestParam(required = false) Integer estatus ) {

		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String strQuincena = null;
			String message = "";
			String[] parameters = new String[] {tipoSuplencia, claveEmpleado, idDelegacion, idCentroTrab,claveServicio,puesto, emp_suplir};
			List<String> params = new ArrayList<>();
			List<String> regexList = new  ArrayList<>();
//			List<String> regexList = List.of("^(SI|SE)$", "^[0-9]{5}$" ,"^[0-9]{2}$", "^[0-9]{5}$", "^[A-Za-z]{2}\\d{3}$", "^[A-Za-z]{1}\\d{5}");
			for(String param : parameters){
				if (param != null){
					params.add(param);
				}
			}
			if(tipoSuplencia != null){
				regexList.add("^(SI|SE)$");
			}
			if(claveEmpleado != null){
				if(tipoSuplencia.equals(String.valueOf("SI"))){
					regexList.add("^[0-9]{6}$");
				}else {
					regexList.add("^[A-ZÑ&]{3,4}([0-9]{2}(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[01]))?([A-Z\\d]{2}[A\\d])$");
				}
			}

			if(idDelegacion != null){
				regexList.add("^[0-9]{2}$");
			}
			if (emp_suplir != null){
				regexList.add("^[0-9]{6}$");
			}

			boolean regexValidation = paramsValidatorService.validate(regexList, params);
			boolean injectableValues = paramsValidatorService.sqlInjectionObjectValidator(params);


			if (quincena != null)
				strQuincena = dateFormat.format(quincena);

			List<DatosSuplencia> guardias = new ArrayList<DatosSuplencia>();

			if(injectableValues || !regexValidation){
				if (injectableValues){
					message = "Intento de inyeccion detectado";
				} else {
					message = "Valor rechazado por la expresion regular";
				}
				return ResponseHandler.generateResponse(message, HttpStatus.NOT_ACCEPTABLE, null);
			} else {
				guardias = suplenciaRepository.ConsultaDynamicSuplencias(strQuincena, tipoSuplencia, claveEmpleado, importe_min, importe_max,
						idDelegacion, idCentroTrab, claveServicio, puesto, emp_suplir, estatus);
			}

			if (guardias.isEmpty()) {

				return ResponseHandler.generateResponse("No se encontraron los registros de suplencias del empleado en el Sistema", HttpStatus.NOT_FOUND, null);
			}

			return ResponseHandler.generateResponse("Se obtuvieron los registros de suplencias del empleado en el Sistema", HttpStatus.OK, guardias);

		} catch (Exception e) {
			logger.info(e.toString());

			return ResponseHandler.generateResponse("Error al consultar los registros de suplencias del empleado en el Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Obtener los registros de suplencias del empleado en el Sistema", description = "Obtener los registros de suplencias del empleado en el Sistema", tags = { "Suplencia" })
	@GetMapping("/suplencias/consulta_x_auth")
	public ResponseEntity<Object> getDynamicSuplenciasAuth(
			@Parameter(description = "Fecha de quincena para la consulta de suplencias", required = true) @RequestParam(required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date quincena,
			@Parameter(description = "Tipo para obtener las suplencias (SI-Internas o SE-Externas)", required = true) @RequestParam(required = true) String tipoSuplencia,
			@Parameter(description = "ID de la delegación para obtener las suplencias", required = false) @RequestParam(required = false) String idDelegacion,
			@Parameter(description = "ID del centro de trabajo para obtener las suplencias", required = false) @RequestParam(required = false) String idCentroTrab,
			@Parameter(description = "Estatus de las suplencias", required = false) @RequestParam(required = false) Integer estatus ) {

		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String strQuincena = null;
			String message = "";
			String[] parameters = new String[] {tipoSuplencia, idDelegacion, idCentroTrab};
//			List<String> regexList = List.of("^(SI|SE)$", "^[0-9]{2}$", "^[0-9]{5}$");
			List<String> regexList = new ArrayList<>();
			List<String> params = new ArrayList<>();
			for(String param : parameters){
				if (param != null){
					params.add(param);
				}
			}
			if(tipoSuplencia != null){
				regexList.add("^(SI|SE)$");
			}
			if(idDelegacion != null){
				regexList.add("^[0-9]{2}$");
			}

			boolean regexValidation = paramsValidatorService.validate(regexList, params);
			boolean injectableValues = paramsValidatorService.sqlInjectionObjectValidator(params);

			if (quincena != null)
				strQuincena = dateFormat.format(quincena);

			List<DatosSuplencia> suplencias = new ArrayList<DatosSuplencia>();

			if(injectableValues || !regexValidation){
				if (injectableValues){
					message = "Intento de inyeccion detectado";
				} else {
					message = "Valor rechazado por la expresion regular";
				}
				return ResponseHandler.generateResponse(message, HttpStatus.NOT_ACCEPTABLE, null);
			} else {
				suplencias = suplenciaRepository.ConsultaDynamicAuthSuplencias(strQuincena, tipoSuplencia, idDelegacion, idCentroTrab, estatus);
			}

			if (suplencias.isEmpty()) {

				return ResponseHandler.generateResponse("No se encontraron los registros de suplencias del empleado en el Sistema", HttpStatus.NOT_FOUND, null);
			}

			return ResponseHandler.generateResponse("Se obtuvieron los registros de suplencias del empleado en el Sistema", HttpStatus.OK, suplencias);

		} catch (Exception e) {
			logger.info(e.toString());

			return ResponseHandler.generateResponse("Error al consultar los registros de suplencias del empleado en el Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

}
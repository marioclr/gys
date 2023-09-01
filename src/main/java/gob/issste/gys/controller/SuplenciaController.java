package gob.issste.gys.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import gob.issste.gys.model.BolsaTrabajo;
import gob.issste.gys.model.DatosEmpleado;
import gob.issste.gys.model.DatosSuplencia;
import gob.issste.gys.repository.IBolsaTrabajoRepository;
import gob.issste.gys.repository.IEmpleadoRepository;
import gob.issste.gys.repository.ISuplenciaRepository;
import gob.issste.gys.response.ResponseHandler;
import gob.issste.gys.service.ISuplenciaInternaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
@Tag(name = "Suplencia", description = "API de Suplencias")
public class SuplenciaController {

	Logger logger = LoggerFactory.getLogger(JdbcTemplateDemo01Application.class);

	@Autowired
	ISuplenciaRepository suplenciaRepository;

	@Autowired
	ISuplenciaInternaService suplenciaInternaService;

	@Autowired
	IEmpleadoRepository empleadoRepository; 

	@Autowired
	IBolsaTrabajoRepository bolsaTrabajoRepository;

	@Operation(summary = "Obtener el importe de una Suplencia conforme a los criterios establecidos", description = "Se calcula el importe de una Suplencia conforme a los criterios establecidos", tags = { "Suplencia" })
	@GetMapping("/suplencias/importe")
	//public ResponseEntity<BigDecimal> getImporteSuplencia(
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

			importe = suplenciaInternaService.CalculaImporteSuplencia( strDate, empleado, dias, tipo );
			BigDecimal importe2 = new BigDecimal(importe).setScale(2, RoundingMode.HALF_UP);
			//return new ResponseEntity<>(importe2, HttpStatus.OK);
			return ResponseHandler.generateResponse("Se encontró el importe de la suplencia conforme a los criterios establecidos", HttpStatus.OK, importe2);
		} catch (Exception e) {
			logger.info(e.toString());
			//return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			return ResponseHandler.generateResponse("Error al consultar el importe de la suplencia conforme a los criterios establecidos", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Obtener los registros de Suplencias del empleado en el Sistema", description = "Obtener los registros de Suplencias del empleado en el Sistema", tags = { "Suplencia" })
	@GetMapping("/suplencias")
	//public ResponseEntity<List<DatosSuplencia>> getSuplenciasEmpleado(
	public ResponseEntity<Object> getSuplenciasEmpleado(
			@Parameter(description = "Número de empleado para obtener las guardidas del empleado", required = true) @RequestParam(required = true) String claveEmpleado,
			@Parameter(description = "Tipo para obtener las guardidas del empleado (internas o externas)", required = true) @RequestParam(required = true) String tipoSuplencia,
			@Parameter(description = "Boolean para indicar si se incluyen los datos de los empleados", required = true) @RequestParam(value = "conDatosEmpleados", required = true) Boolean conEmpleados ) {

		try {
			List<DatosSuplencia> suplencias = new ArrayList<DatosSuplencia>();
			DatosEmpleado empleado, empleado_sup;

			if (tipoSuplencia.equals(String.valueOf("I"))) {
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
			} else {
				suplencias = suplenciaRepository.ConsultaSuplenciasExternas(claveEmpleado);
				if (conEmpleados) {
					for(DatosSuplencia suplencia:suplencias) {
						logger.info("Emp: " + suplencia.getClave_empleado() + " Sup: " + suplencia.getClave_empleado() + " Fecha: " + suplencia.getFec_paga());
						try {
							BolsaTrabajo bolsa = bolsaTrabajoRepository.findByRFC(suplencia.getClave_empleado());
							empleado = new DatosEmpleado();
							empleado.setClave_empleado(bolsa.getRfc());
							empleado.setNombre(bolsa.getNombre());
							empleado.setApellido_1(bolsa.getApellidoPat());
							empleado.setApellido_2(bolsa.getApellidoMat());
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
			}

			if (suplencias.isEmpty()) {
				//return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				return ResponseHandler.generateResponse("No exixten Suplencias del empleado en el Sistema", HttpStatus.NO_CONTENT, null);
			}

			//return new ResponseEntity<>(suplencias, HttpStatus.OK);
			return ResponseHandler.generateResponse("Se encontraron los registros de Suplencias del empleado en el Sistema", HttpStatus.OK, suplencias);

		} catch (Exception e) {
			logger.info(e.toString());
			//return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			return ResponseHandler.generateResponse("Error al obtener los registros de Suplencias del empleado en el Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Obtener una suplencia en el sistema mediante el ID", description = "Obtener una suplencia en el sistema mediante el ID", tags = { "Suplencia" })
	@GetMapping("/suplenciaPorId")
	//public ResponseEntity<DatosSuplencia> getSuplenciaById(
	public ResponseEntity<Object> getSuplenciaById(
			@Parameter(description = "ID de la guardia del empleado", required = true) @RequestParam(required = true) Integer idSuplencia,
			@Parameter(description = "Tipo para obtener las guardidas del empleado (internas o externas)", required = true) @RequestParam(required = true) String tipoSuplencia) {

		DatosSuplencia suplencia = null;
		DatosEmpleado empleado, empleado_sup;

		try {

			if (tipoSuplencia.equals(String.valueOf("I"))) {
				suplencia = suplenciaRepository.findById(idSuplencia);
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
			} else {
				suplencia = suplenciaRepository.findByIdExt(idSuplencia);
				try {
					BolsaTrabajo bolsa = bolsaTrabajoRepository.findByRFC(suplencia.getClave_empleado());
					empleado = new DatosEmpleado();
					empleado.setClave_empleado(bolsa.getRfc());
					empleado.setNombre(bolsa.getNombre());
					empleado.setApellido_1(bolsa.getApellidoPat());
					empleado.setApellido_2(bolsa.getApellidoMat());
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
	
			//return new ResponseEntity<>(suplencia, HttpStatus.OK);
			return ResponseHandler.generateResponse("Se encontró la suplencia en el sistema mediante el ID", HttpStatus.OK, suplencia);
		} catch ( Exception ex ) {
			logger.info(ex.toString());
			//return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return ResponseHandler.generateResponse("No se encontró la suplencia en el sistema mediante el ID", HttpStatus.NO_CONTENT, null);
		}
	}

	@Operation(summary = "Obtener el saldo utilizado por registro de suplencias para una delegación en un año determinado", description = "Obtener el saldo utilizado por registro de suplencias para una delegación en un año determinado", tags = { "Suplencia" })
	@GetMapping("/suplencias/saldo")
	public ResponseEntity<Object> getSaldoSuplencia(
			@Parameter(description = "ID de la Delegación que se consulta el presupuesto", required = true) @RequestParam(required = true) String idDelegacion,
			@Parameter(description = "ID de la Delegación que se consulta el presupuesto", required = true) @RequestParam(required = true) Integer anio_ejercicio,
			@Parameter(description = "Tipo para obtener las suplencias del empleado (internas o externas)", required = false) @RequestParam(required = false) String tipoGuardia) {

		double saldo=0;

		if (tipoGuardia != null) {
			if (tipoGuardia.equals(String.valueOf("I")))
				saldo = suplenciaRepository.ObtenerSaldoUtilizado(idDelegacion, anio_ejercicio);
			else
				saldo = suplenciaRepository.ObtenerSaldoUtilizadoExt(idDelegacion, anio_ejercicio);
		} else {
			saldo = suplenciaRepository.ObtenerSaldoUtilizado(idDelegacion, anio_ejercicio) + suplenciaRepository.ObtenerSaldoUtilizadoExt(idDelegacion, anio_ejercicio); 
		}
		return ResponseHandler.generateResponse("Se obtuvo el saldo utilizado para guardias para las condiciones indicadas", HttpStatus.OK, saldo);
	}

	@Operation(summary = "Agrega un nuevo registro de Suplencia al Sistema", description = "Agrega un nuevo registro de Suplencia al Sistema", tags = { "Suplencia" })
	@PostMapping("/suplencias")
	//public ResponseEntity<String> agregarSuplencia(
	public ResponseEntity<Object> agregarSuplencia(
			@Parameter(description = "Objeto de Suplencia que se creará en el Sistema") @RequestBody DatosSuplencia suplencia) {
		try {

			if (suplencia.getTipo_suplencia().equals(String.valueOf("I"))) {
				if (suplenciaRepository.existe_suplencia(suplencia)>0)
					//return new ResponseEntity<>("Existe un registro de Suplencia en ese mismo periodo", HttpStatus.INTERNAL_SERVER_ERROR);
					return ResponseHandler.generateResponse("Existe un registro de Suplencia en ese mismo periodo", HttpStatus.INTERNAL_SERVER_ERROR, null);
			} else {
				if (suplenciaRepository.existe_suplenciaExt(suplencia)>0)
					//return new ResponseEntity<>("Existe un registro de Suplencia en ese mismo periodo", HttpStatus.INTERNAL_SERVER_ERROR);
					return ResponseHandler.generateResponse("Existe un registro de Suplencia en ese mismo periodo", HttpStatus.INTERNAL_SERVER_ERROR, null);
			}

			DatosEmpleado empleado = empleadoRepository.getDatosEmpleado(suplencia.getFec_paga(), suplencia.getEmpleado_suplir().getClave_empleado());
			double importe = suplenciaInternaService.CalculaImporteSuplencia(suplencia.getFec_paga(), 
					empleado, suplencia.getDias(), suplencia.getTipo_suplencia());

			int id = suplenciaInternaService.GuardarSuplencia(suplencia, importe);

			//return new ResponseEntity<>("El registro de Suplencia ha sido guardado de manera exitosa, con ID = " + id, HttpStatus.CREATED);
			return ResponseHandler.generateResponse("El registro de Suplencia ha sido guardado de manera exitosa, con ID = " + id, HttpStatus.OK, null);
		} catch (Exception e) {
			//return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			return ResponseHandler.generateResponse("Error al agregar un nuevo registro de Suplencia al Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Actualizar importes de Suplencias en el Sistema", description = "Actualizar importes de Suplencias en el Sistema", tags = { "Suplencia" })
	@PutMapping("/suplencias/actualiza")
	//public ResponseEntity<String> actualizaImportes(
	public ResponseEntity<Object> actualizaImportes(
			@Parameter(description = "Fecha de quincena para el cálculo del importe de la Suplencia", required = true) @RequestParam(required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date quincena,
			@Parameter(description = "Tipo para obtener las guardidas del empleado ('I': Internas o 'E': Externas)", required = true) @RequestParam(required = true) String tipoSuplencia ) {
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String strQuincena = dateFormat.format(quincena);
			suplenciaInternaService.ActualizaImportesSuplencias(strQuincena, tipoSuplencia);
			//return new ResponseEntity<>("Los importes de las Suplencias se actualizaron de manera exitósa", HttpStatus.CREATED);
			return ResponseHandler.generateResponse("Los importes de las Suplencias se actualizaron de manera exitósa", HttpStatus.OK, null);
		} catch (Exception e) {
			//return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			return ResponseHandler.generateResponse("Error al Actualizar los importes de la Suplencia en el Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Actualiza un registro de suplencia en el Sistema", description = "Actualiza un registro de suplencia en el Sistema", tags = { "Suplencia" })
	@PutMapping("/suplencias")
	//public ResponseEntity<String> actualizarSuplencia(
	public ResponseEntity<Object> actualizarSuplencia(
			@Parameter(description = "Objeto de la guardia a actualizarse en el Sistema") @RequestBody DatosSuplencia suplencia) {
		try {

			if (suplencia.getTipo_suplencia().equals(String.valueOf("I"))) {
				if (suplenciaRepository.existe_suplencia_upd(suplencia)>0)
					//return new ResponseEntity<>("Existe un registro de Suplencia en ese mismo periodo", HttpStatus.INTERNAL_SERVER_ERROR);
					return ResponseHandler.generateResponse("Existe un registro de Suplencia en ese mismo periodo", HttpStatus.INTERNAL_SERVER_ERROR, null);
			} else {
				if (suplenciaRepository.existe_suplenciaExt_upd(suplencia)>0)
					//return new ResponseEntity<>("Existe un registro de Suplencia en ese mismo periodo", HttpStatus.INTERNAL_SERVER_ERROR);
					return ResponseHandler.generateResponse("Existe un registro de Suplencia en ese mismo periodo", HttpStatus.INTERNAL_SERVER_ERROR, null);
			}

			//DatosEmpleado empleado = empleadoRepository.getDatosEmpleado(suplencia.getFec_paga(), suplencia.getEmpleado_suplir().getClave_empleado());
			double importe = suplenciaInternaService.CalculaImporteSuplencia(suplencia.getFec_paga(), 
					suplencia.getEmpleado_suplir().getClave_empleado(), suplencia.getDias(), suplencia.getTipo_suplencia());

			suplenciaInternaService.actualizaSuplencia(suplencia, importe);

			//return new ResponseEntity<>("El registro de guardia ha sido actualizado de manera exitosa", HttpStatus.CREATED);
			return ResponseHandler.generateResponse("El registro de guardia ha sido actualizado de manera exitosa", HttpStatus.OK, null);
		} catch (Exception e) {
			//return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			return ResponseHandler.generateResponse("Error al actualizar un registro de suplencia en el Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Eliminar un registro de suplencia del Sistema", description = "Eliminar un registro de suplencia del Sistema", tags = { "Suplencia" })
	@DeleteMapping("/suplencias")
	//public ResponseEntity<String> eliminarSuplencia(
	public ResponseEntity<Object> eliminarSuplencia(
			@Parameter(description = "Número de ID para obtener la suplencia del empleado", required = true) @RequestParam(required = true) Integer idGuardia,
			@Parameter(description = "Tipo para obtener la suplencia del empleado (I-Interna o E-Externa)", required = true) @RequestParam(required = true) String tipoGuardia) {
		try {
			suplenciaInternaService.eliminarSuplencia(idGuardia, tipoGuardia);
			//return new ResponseEntity<>("El un registro de suplencia fué eliminado exitosamente.", HttpStatus.OK);
			return ResponseHandler.generateResponse("El un registro de suplencia fué eliminado exitosamente.", HttpStatus.OK, null);
		} catch (Exception e) {
			//return new ResponseEntity<>("No se borró el registro de suplencia.", HttpStatus.INTERNAL_SERVER_ERROR);
			return ResponseHandler.generateResponse("Error al consultar el importe de la suplencia conforme a los criterios establecidos", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Actualizar importes de Suplencias en el Sistema", description = "Actualizar importes de Suplencias en el Sistema", tags = { "Suplencia" })
	@PutMapping("/suplencias/actualiza2")
	//public ResponseEntity<String> actualizaImportes2(
	public ResponseEntity<Object> actualizaImportes2(
			@Parameter(description = "Fecha de quincena para el cálculo del importe de la Suplencia", required = true) @RequestParam(required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date quincena,
			@Parameter(description = "Tipo para obtener las guardidas del empleado ('I': Internas o 'E': Externas)", required = true) @RequestParam(required = true) String tipoSuplencia ) {
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String strQuincena = dateFormat.format(quincena);
			suplenciaInternaService.ActualizaImportesSuplencias2(strQuincena, tipoSuplencia);
			//return new ResponseEntity<>("Los importes de las Suplencias se actualizaron de manera exitósa.", HttpStatus.CREATED);
			return ResponseHandler.generateResponse("Los importes de las Suplencias se actualizaron de manera exitósa", HttpStatus.OK, null);
		} catch (Exception e) {
			//return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			return ResponseHandler.generateResponse("Error al actualizar el importe de la suplencia", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Obtener los registros de suplencias del empleado en el Sistema", description = "Obtener los registros de suplencias del empleado en el Sistema", tags = { "Suplencia" })
	@GetMapping("/suplencias/consulta")
	//public ResponseEntity<List<DatosSuplencia>> getDynamicSuplencias(
	public ResponseEntity<Object> getDynamicSuplencias(
			@Parameter(description = "Fecha de quincena para la consulta de suplencias", required = false) @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date quincena,
			@Parameter(description = "Tipo para obtener las suplencias (I-Internas o E-Externas)", required = true) @RequestParam(required = true) String tipoSuplencia,
			@Parameter(description = "ID de empleado o RFC para obtener las suplencias", required = false) @RequestParam(required = false) String claveEmpleado,
			@Parameter(description = "Importe mínimo para obtener las suplencias", required = false) @RequestParam(required = false) Double importe_min,
			@Parameter(description = "Importe máximo para obtener las suplencias", required = false) @RequestParam(required = false) Double importe_max,
			@Parameter(description = "ID de la delegación para obtener las suplencias", required = false) @RequestParam(required = false) String idDelegacion,
			@Parameter(description = "ID del centro de trabajo para obtener las suplencias", required = false) @RequestParam(required = false) String idCentroTrab,
			@Parameter(description = "Clave del servicio para obtener las suplencias", required = false) @RequestParam(required = false) String claveServicio,
			@Parameter(description = "Clave del puesto para obtener las suplencias", required = false) @RequestParam(required = false) String puesto,
			@Parameter(description = "Clave del empleado a suplir para obtener las suplencias", required = false) @RequestParam(required = false) String emp_suplir) {

		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String strQuincena = null;

			if (quincena != null)
				strQuincena = dateFormat.format(quincena);

			List<DatosSuplencia> guardias = new ArrayList<DatosSuplencia>();

			guardias = suplenciaRepository.ConsultaDynamicSuplencias(strQuincena, tipoSuplencia, claveEmpleado, importe_min, importe_max, 
																idDelegacion, idCentroTrab, claveServicio, puesto, emp_suplir);

			if (guardias.isEmpty()) {
				//return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				return ResponseHandler.generateResponse("No se encontraron los registros de suplencias del empleado en el Sistema", HttpStatus.NO_CONTENT, null);
			}

			//return new ResponseEntity<>(guardias, HttpStatus.OK);
			return ResponseHandler.generateResponse("Se obtuvieron los registros de suplencias del empleado en el Sistema", HttpStatus.OK, guardias);

		} catch (Exception e) {
			logger.info(e.toString());
			//return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			return ResponseHandler.generateResponse("Error al consultar los registros de suplencias del empleado en el Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}
}
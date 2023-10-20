package gob.issste.gys.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gob.issste.gys.JdbcTemplateDemo01Application;
import gob.issste.gys.model.DatosAdscripcion;
import gob.issste.gys.model.DatosJornada;
import gob.issste.gys.model.DatosNivel;
import gob.issste.gys.model.DatosPuesto;
import gob.issste.gys.model.DatosServicio;
import gob.issste.gys.model.Delegacion;
import gob.issste.gys.model.Horario;
import gob.issste.gys.model.Incidencia;
import gob.issste.gys.model.Paga;
import gob.issste.gys.model.Usuario;
import gob.issste.gys.repository.IDatosRepository;
import gob.issste.gys.repository.UsuarioRepository;
import gob.issste.gys.response.ResponseHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
@Tag(name = "Datos", description = "API de Datos consultados de Meta4")
public class DatosController {

	Logger logger = LoggerFactory.getLogger(JdbcTemplateDemo01Application.class);

	@Autowired
	IDatosRepository datosRepository;
	@Autowired
	UsuarioRepository usuarioRepository;

	@Operation(summary = "Obtener el catálogo de adscripciones", description = "Obtener el catálogo de adscripciones", tags = { "Datos" })
	@GetMapping("/datos/adsc")
	//public ResponseEntity<List<DatosAdscripcion>> getAdscripciones(
	public ResponseEntity<Object> getAdscripciones(
			@Parameter(description = "ID del usuario obtener las adscripciones que coincidan con su clave", required = false) @RequestParam(required = false) Integer idUsuario ) {

		try {
			List<DatosAdscripcion> adscripciones = new ArrayList<DatosAdscripcion>();

			if (idUsuario==null) {
				adscripciones = datosRepository.getDatosAdscripciones();
			} else {
				Usuario _usuario = usuarioRepository.findById(idUsuario);
				if (_usuario.getNivelVisibilidad().getIdNivelVisibilidad()==3) {
					adscripciones = datosRepository.getDatosAdscripciones_ct(idUsuario);
				} else {
					adscripciones = datosRepository.getDatosAdscripciones(idUsuario);
				}
			}
			if (adscripciones.isEmpty()) {
				//return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				return ResponseHandler.generateResponse("No existen registros en el catálogo de adscripciones", HttpStatus.NOT_FOUND, null);
			}
			//return new ResponseEntity<>(adscripciones, HttpStatus.OK);
			return ResponseHandler.generateResponse("Se encontró el catálogo de adscripciones", HttpStatus.OK, adscripciones);
		} catch (Exception e) {
			//return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			return ResponseHandler.generateResponse("Error al obtener el catálogo de adscripciones", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Obtener las adscripciones correspondientes a la delegación indicada", description = "Obtener las adscripciones correspondientes a la delegación indicada", tags = { "Datos" })
	@GetMapping("/datos/adscForDeleg")
	public ResponseEntity<Object> getAdscripcionesForDeleg(
			@Parameter(description = "ID de la delegación para obtener las adscripciones asociadas", required = true) @RequestParam(required = true) String idDeleg ) {

		try {
			List<DatosAdscripcion> adscripciones = new ArrayList<DatosAdscripcion>();

			adscripciones = datosRepository.getDatosAdscForDeleg(idDeleg);

			if (adscripciones.isEmpty()) {
				return ResponseHandler.generateResponse("No existen registros en el catálogo de adscripciones", HttpStatus.NOT_FOUND, null);
			}
			return ResponseHandler.generateResponse("Se encontró el catálogo de adscripciones", HttpStatus.OK, adscripciones);
		} catch (Exception e) {
			return ResponseHandler.generateResponse("Error al obtener el catálogo de adscripciones", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Obtener el catálogo de puestos", description = "Obtener el catálogo de puestos", tags = { "Datos" })
	@GetMapping("/datos/puestos")
	//public ResponseEntity<List<DatosPuesto>> getPuestos(
	public ResponseEntity<Object> getPuestos(
			@Parameter(description = "Adscripción para obtener los puestos en que coincida con su clave", required = false) @RequestParam(required = false) String adsc ) {

		try {
			List<DatosPuesto> puestos = new ArrayList<DatosPuesto>();

			if (adsc == null)
				puestos = datosRepository.getDatosPuestosGuardia();
			else
				puestos = datosRepository.getDatosPuestosGuardia(adsc);

			if (puestos.isEmpty()) {
				//return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				return ResponseHandler.generateResponse("No existen registros en el catálogo de puestos", HttpStatus.NOT_FOUND, null);
			}

			//return new ResponseEntity<>(puestos, HttpStatus.OK);
			return ResponseHandler.generateResponse("Se encontró el catálogo de puestos", HttpStatus.OK, puestos);
		} catch (Exception e) {
			//return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			return ResponseHandler.generateResponse("Error al consultar el catálogo de puestos", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Obtener el catálogo de servicios", description = "Obtener el catálogo de servicios", tags = { "Datos" })
	@GetMapping("/datos/servicios")
	//public ResponseEntity<List<DatosServicio>> getServicios(
	public ResponseEntity<Object> getServicios(
			@Parameter(description = "Adscripción para obtener los servicios en que coincida con su clave", required = false) @RequestParam(required = false) String adsc,
			@Parameter(description = "Puesto para obtener los servicios en que coincida con su clave", required = false) @RequestParam(required = false) String puesto ) {

		try {
			List<DatosServicio> servicios = new ArrayList<DatosServicio>();

			if (adsc == null && puesto == null)
				servicios = datosRepository.getDatosServiciosGuardia();
			else
				servicios = datosRepository.getDatosServiciosGuardia(adsc, puesto);

			if (servicios.isEmpty()) {
				//return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				return ResponseHandler.generateResponse("No existen registros en el catálogo de servicios", HttpStatus.NOT_FOUND, null);
			}

			//return new ResponseEntity<>(servicios, HttpStatus.OK);
			return ResponseHandler.generateResponse("Se encontró el catálogo de servicios", HttpStatus.OK, servicios);
		} catch (Exception e) {
			//return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			return ResponseHandler.generateResponse("Error al consultar el catálogo de servicios", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Obtener el catálogo de niveles", description = "Obtener el catálogo de niveles", tags = { "Datos" })
	@GetMapping("/datos/niveles")
	//public ResponseEntity<List<DatosNivel>> getNiveles( 
	public ResponseEntity<Object> getNiveles(
			@Parameter(description = "Adscripción para obtener los niveles en que coincida con su clave", required = false) @RequestParam(required = false) String adsc,
			@Parameter(description = "Puesto para obtener los niveles en que coincida con su clave", required = false) @RequestParam(required = false) String puesto,
			@Parameter(description = "Servicio para obtener los niveles en que coincida con su clave", required = false) @RequestParam(required = false) String servicio ) {

		try {
			List<DatosNivel> niveles = new ArrayList<DatosNivel>();

			if (adsc == null && puesto == null && servicio == null)
				niveles = datosRepository.getDatosNivelesGuardia();
			else
				niveles = datosRepository.getDatosNivelesGuardia(adsc, puesto, servicio);

			if (niveles.isEmpty()) {
				//return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				return ResponseHandler.generateResponse("No existen registros en el catálogo de niveles", HttpStatus.NOT_FOUND, null);
			}

			//return new ResponseEntity<>(niveles, HttpStatus.OK);
			return ResponseHandler.generateResponse("Se encontró el catálogo de niveles", HttpStatus.OK, niveles);
		} catch (Exception e) {
			//return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			return ResponseHandler.generateResponse("Error al consultar el catálogo de niveles", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Obtener el catálogo de jornadas", description = "Obtener el catálogo de jornadas", tags = { "Datos" })
	@GetMapping("/datos/jornadas")
	//public ResponseEntity<List<DatosJornada>> getJornadas(
	public ResponseEntity<Object> getJornadas(
			@Parameter(description = "Adscripción para obtener las jornadas en que coincida con su clave", required = false) @RequestParam(required = false) String adsc,
			@Parameter(description = "Puesto para obtener las jornadas en que coincida con su clave", required = false) @RequestParam(required = false) String puesto,
			@Parameter(description = "Servicio para obtener las jornadas en que coincida con su clave", required = false) @RequestParam(required = false) String servicio,
			@Parameter(description = "Niveles para obtener las jornadas en que coincida con su clave", required = false) @RequestParam(required = false) String niveles ) {

		try {
			List<DatosJornada> jornadas = new ArrayList<DatosJornada>();

			if (adsc == null && puesto == null && servicio == null)
				jornadas = datosRepository.getDatosJornadasGuardia();
			else
				jornadas = datosRepository.getDatosJornadasGuardia(adsc, puesto, servicio, niveles);

			if (jornadas.isEmpty()) {
				//return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				return ResponseHandler.generateResponse("No existen registros en el catálogo de jornadas", HttpStatus.NOT_FOUND, null);
			}

			//return new ResponseEntity<>(jornadas, HttpStatus.OK);
			return ResponseHandler.generateResponse("Se encontró el catálogo de jornadas", HttpStatus.OK, jornadas);
		} catch (Exception e) {
			//return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			return ResponseHandler.generateResponse("Error al consultar el catálogo de jornadas", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Obtener el catálogo de delegaciones", description = "Obtener el catálogo de delegaciones", tags = { "Datos" })
	@GetMapping("/datos/deleg")
	//public ResponseEntity<List<Delegacion>> getDelegaciones() {
	public ResponseEntity<Object> getDelegaciones() {

		try {
			List<Delegacion> delegaciones = new ArrayList<Delegacion>();
			delegaciones = datosRepository.getDatosDelegaciones();
			if (delegaciones.isEmpty()) {
				//return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				return ResponseHandler.generateResponse("No existen registros en el catálogo de delegaciones", HttpStatus.NOT_FOUND, null);
			}
			//return new ResponseEntity<>(delegaciones, HttpStatus.OK);
			return ResponseHandler.generateResponse("Se encontró el catálogo de delegaciones", HttpStatus.OK, delegaciones);
		} catch (Exception e) {
			//return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			return ResponseHandler.generateResponse("Error al consultar el catálogo de delegaciones", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Obtener el catálogo de horarios", description = "Obtener el catálogo de horarios", tags = { "Datos" })
	@GetMapping("/datos/horarios")
	//public ResponseEntity<List<Horario>> getHorarios() {
	public ResponseEntity<Object> getHorarios() {

		try {
			List<Horario> horarios = new ArrayList<Horario>();
			horarios = datosRepository.getHorarios();
			if (horarios.isEmpty()) {
				//return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				return ResponseHandler.generateResponse("No existen registros en el catálogo de horarios", HttpStatus.NOT_FOUND, null);
			}
			//return new ResponseEntity<>(horarios, HttpStatus.OK);
			return ResponseHandler.generateResponse("Se encontró el catálogo de horarios", HttpStatus.OK, horarios);
		} catch (Exception e) {
			//return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			return ResponseHandler.generateResponse("Error al consultar el catálogo de horarios", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Obtener el catálogo de incidencias", description = "Obtener el catálogo de incidencias", tags = { "Datos" })
	@GetMapping("/datos/incidencia")
	//public ResponseEntity<List<Incidencia>> getIncidencia() {
	public ResponseEntity<Object> getIncidencia() {

		try {
			List<Incidencia> incidencias = new ArrayList<Incidencia>();
			incidencias = datosRepository.getIncidencia();
			if (incidencias.isEmpty()) {
				//return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				return ResponseHandler.generateResponse("No existen registros en el catálogo de incidencias", HttpStatus.NOT_FOUND, null);
			}
			//return new ResponseEntity<>(incidencias, HttpStatus.OK);
			return ResponseHandler.generateResponse("Se encontró el catálogo de incidencias", HttpStatus.OK, incidencias);
		} catch (Exception e) {
			//return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			return ResponseHandler.generateResponse("Error al consultar el catálogo de incidencias", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Obtener el catálogo de fechas", description = "Obtener el catálogo de fechas", tags = { "Datos" })
	@GetMapping("/datos/pagas")
	//public ResponseEntity<List<Paga>> getPagas() {
	public ResponseEntity<Object> getPagas() {

		try {
			List<Paga> pagas = new ArrayList<Paga>();
			pagas = datosRepository.getPagas();
			if (pagas.isEmpty()) {
				//return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				return ResponseHandler.generateResponse("No existen registros en el catálogo de fechas", HttpStatus.NOT_FOUND, null);
			}
			//return new ResponseEntity<>(pagas, HttpStatus.OK);
			return ResponseHandler.generateResponse("Se encontró el catálogo de fechas", HttpStatus.OK, pagas);
		} catch (Exception e) {
			//return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			return ResponseHandler.generateResponse("Error al consultar el catálogo de fechas", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Valida sí las características son autarizadas para guardias o suplencias", description = "Valida sí las características son autarizadas para guardias o suplencias", tags = { "Datos" })
	@GetMapping("/datos/ValidaPuesto")
	//public ResponseEntity<Long> getValidaPuesto(
	public ResponseEntity<Object> getValidaPuesto(
			@Parameter(description = "Tipo Centro trabajo para validar que el puesto este autorizado", required = false) @RequestParam(required = false) String tipo_ct,
			@Parameter(description = "Servicio para validar que el puesto este autorizado", required = false) @RequestParam(required = false) String clave_servicio,
			@Parameter(description = "Puesto para validar que el puesto este autorizado", required = false) @RequestParam(required = false) String puesto,
			@Parameter(description = "Nivel para validar que el puesto este autorizado", required = false) @RequestParam(required = false) String nivel,
			@Parameter(description = "Sub nivel para validar que el puesto este autorizado", required = false) @RequestParam(required = false) String subnivel,
			@Parameter(description = "Tipo jornada para validar que el puesto este autorizado", required = false) @RequestParam(required = false) String tipo_jornada,
			@Parameter(description = "Tipo guardia para validar que el puesto este autorizado", required = false) @RequestParam(required = false) String tipo_guardia ) {

		try {
			long valida = 0;

			valida = datosRepository.ValidaPuestoAutorizado(tipo_ct, clave_servicio, puesto, nivel, subnivel, tipo_jornada, tipo_guardia.substring(1));

			if (valida == 0) {
				//return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				return ResponseHandler.generateResponse("Estas características no son autarizadas para guardias o suplencias", HttpStatus.NOT_FOUND, null);
			}

			//return new ResponseEntity<>(valida, HttpStatus.OK);
			return ResponseHandler.generateResponse("Las características son autarizadas para guardias o suplencias", HttpStatus.OK, valida);
		} catch (Exception e) {
			//return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			return ResponseHandler.generateResponse("Error al validar sí las características son autarizadas para guardias o suplencias", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}
}

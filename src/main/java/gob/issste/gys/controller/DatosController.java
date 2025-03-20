package gob.issste.gys.controller;

import java.util.ArrayList;
import java.util.List;

import gob.issste.gys.model.*;
import gob.issste.gys.service.ParamsValidatorService;
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
import gob.issste.gys.repository.IDatosRepository;
import gob.issste.gys.repository.UsuarioRepository;
import gob.issste.gys.response.ResponseHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

//@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
@Tag(name = "Datos", description = "API de Datos consultados de Meta4")
public class DatosController {

	Logger logger = LoggerFactory.getLogger(JdbcTemplateDemo01Application.class);
	ParamsValidatorService paramsValidatorService = new ParamsValidatorService();

	@Autowired
	IDatosRepository datosRepository;
	@Autowired
	UsuarioRepository usuarioRepository;

	@Operation(summary = "Obtener el catálogo de adscripciones", description = "Obtener el catálogo de adscripciones", tags = { "Datos" })
	@GetMapping("/datos/adsc")
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
				return ResponseHandler.generateResponse("No existen registros en el catálogo de adscripciones", HttpStatus.NOT_FOUND, null);
			}
			return ResponseHandler.generateResponse("Se encontró el catálogo de adscripciones", HttpStatus.OK, adscripciones);
		} catch (Exception e) {
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
	public ResponseEntity<Object> getPuestos(
			@Parameter(description = "Adscripción para obtener los puestos en que coincida con su clave", required = false) @RequestParam(required = false) String adsc ) {

		try {
			List<DatosPuesto> puestos = new ArrayList<DatosPuesto>();

			if (adsc == null)
				puestos = datosRepository.getDatosPuestosGuardia();
			else
				puestos = datosRepository.getDatosPuestosGuardia(adsc);

			if (puestos.isEmpty()) {
				return ResponseHandler.generateResponse("No existen registros en el catálogo de puestos", HttpStatus.NOT_FOUND, null);
			}

			return ResponseHandler.generateResponse("Se encontró el catálogo de puestos", HttpStatus.OK, puestos);
		} catch (Exception e) {
			return ResponseHandler.generateResponse("Error al consultar el catálogo de puestos", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Obtener el catálogo de servicios", description = "Obtener el catálogo de servicios", tags = { "Datos" })
	@GetMapping("/datos/servicios")
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
				return ResponseHandler.generateResponse("No existen registros en el catálogo de servicios", HttpStatus.NOT_FOUND, null);
			}

			return ResponseHandler.generateResponse("Se encontró el catálogo de servicios", HttpStatus.OK, servicios);
		} catch (Exception e) {
			return ResponseHandler.generateResponse("Error al consultar el catálogo de servicios", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Obtener el catálogo de niveles", description = "Obtener el catálogo de niveles", tags = { "Datos" })
	@GetMapping("/datos/niveles")
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
				return ResponseHandler.generateResponse("No existen registros en el catálogo de niveles", HttpStatus.NOT_FOUND, null);
			}

			return ResponseHandler.generateResponse("Se encontró el catálogo de niveles", HttpStatus.OK, niveles);
		} catch (Exception e) {
			return ResponseHandler.generateResponse("Error al consultar el catálogo de niveles", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Obtener el catálogo de jornadas", description = "Obtener el catálogo de jornadas", tags = { "Datos" })
	@GetMapping("/datos/jornadas")
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
				return ResponseHandler.generateResponse("No existen registros en el catálogo de jornadas", HttpStatus.NOT_FOUND, null);
			}

			return ResponseHandler.generateResponse("Se encontró el catálogo de jornadas", HttpStatus.OK, jornadas);
		} catch (Exception e) {
			return ResponseHandler.generateResponse("Error al consultar el catálogo de jornadas", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Obtener el catálogo de delegaciones", description = "Obtener el catálogo de delegaciones", tags = { "Datos" })
	@GetMapping("/datos/deleg")
	public ResponseEntity<Object> getDelegaciones() {

		try {
			List<Delegacion> delegaciones = new ArrayList<Delegacion>();
			delegaciones = datosRepository.getDatosDelegaciones();
			if (delegaciones.isEmpty()) {
				return ResponseHandler.generateResponse("No existen registros en el catálogo de delegaciones", HttpStatus.NOT_FOUND, null);
			}
			return ResponseHandler.generateResponse("Se encontró el catálogo de delegaciones", HttpStatus.OK, delegaciones);
		} catch (Exception e) {
			return ResponseHandler.generateResponse("Error al consultar el catálogo de delegaciones", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Obtener el catálogo de delegaciones", description = "Obtener el catálogo de delegaciones", tags = { "Datos" })
	@GetMapping("/datos/delegPorFecha")
	public ResponseEntity<Object> getDelegacionesPorFecha(
			@Parameter(description = "Id de la fecha", required = false) @RequestParam(required = false) int idFecha
	) {

		try {
			List<DelegacionPorFecha> delegaciones = new ArrayList<DelegacionPorFecha>();
			delegaciones = datosRepository.getDatosDelegacionesPorFecha(idFecha);
			if (delegaciones.isEmpty()) {
				return ResponseHandler.generateResponse("No existen registros en el catálogo de delegaciones por fecha", HttpStatus.NOT_FOUND, null);
			}
			return ResponseHandler.generateResponse("Se encontró el catálogo de delegaciones por fecha", HttpStatus.OK, delegaciones);
		} catch (Exception e) {
			return ResponseHandler.generateResponse("Error al consultar el catálogo de delegaciones por fecha", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Obtener el catálogo de horarios", description = "Obtener el catálogo de horarios", tags = { "Datos" })
	@GetMapping("/datos/horarios")
	public ResponseEntity<Object> getHorarios() {

		try {
			List<Horario> horarios = new ArrayList<Horario>();
			horarios = datosRepository.getHorarios();
			if (horarios.isEmpty()) {
				return ResponseHandler.generateResponse("No existen registros en el catálogo de horarios", HttpStatus.NOT_FOUND, null);
			}
			return ResponseHandler.generateResponse("Se encontró el catálogo de horarios", HttpStatus.OK, horarios);
		} catch (Exception e) {
			return ResponseHandler.generateResponse("Error al consultar el catálogo de horarios", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Obtener el catálogo de incidencias", description = "Obtener el catálogo de incidencias", tags = { "Datos" })
	@GetMapping("/datos/incidencia")
	public ResponseEntity<Object> getIncidencia() {

		try {
			List<Incidencia> incidencias = new ArrayList<Incidencia>();
			incidencias = datosRepository.getIncidencia();
			if (incidencias.isEmpty()) {
				return ResponseHandler.generateResponse("No existen registros en el catálogo de incidencias", HttpStatus.NOT_FOUND, null);
			}
			return ResponseHandler.generateResponse("Se encontró el catálogo de incidencias", HttpStatus.OK, incidencias);
		} catch (Exception e) {
			return ResponseHandler.generateResponse("Error al consultar el catálogo de incidencias", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Obtener el catálogo de fechas", description = "Obtener el catálogo de fechas", tags = { "Datos" })
	@GetMapping("/datos/pagas")
	public ResponseEntity<Object> getPagas() {

		try {
			List<Paga> pagas = new ArrayList<Paga>();
			pagas = datosRepository.getPagas();
			if (pagas.isEmpty()) {
				return ResponseHandler.generateResponse("No existen registros en el catálogo de fechas", HttpStatus.NOT_FOUND, null);
			}
			return ResponseHandler.generateResponse("Se encontró el catálogo de fechas", HttpStatus.OK, pagas);
		} catch (Exception e) {
			return ResponseHandler.generateResponse("Error al consultar el catálogo de fechas", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Valida sí las características son autarizadas para guardias o suplencias", description = "Valida sí las características son autarizadas para guardias o suplencias", tags = { "Datos" })
	@GetMapping("/datos/ValidaPuesto")
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
				return ResponseHandler.generateResponse("Estas características no son autarizadas para guardias o suplencias", HttpStatus.NOT_FOUND, null);
			}

			return ResponseHandler.generateResponse("Las características son autarizadas para guardias o suplencias", HttpStatus.OK, valida);
		} catch (Exception e) {
			return ResponseHandler.generateResponse("Error al validar sí las características son autarizadas para guardias o suplencias", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Valida sí las características son autarizadas para guardias o suplencias", description = "Valida sí las características son autarizadas para guardias o suplencias", tags = { "Datos" })
	@GetMapping("/datos/MatrizPuestos")
	public ResponseEntity<Object> getMatrizPuestos(
			@Parameter(description = "Tipo Centro trabajo para validar que el puesto este autorizado", required = false) @RequestParam(required = false) String tipo_ct,
			@Parameter(description = "Servicio para validar que el puesto este autorizado", required = false) @RequestParam(required = false) String clave_servicio,
			@Parameter(description = "Puesto para validar que el puesto este autorizado", required = false) @RequestParam(required = false) String puesto,
			@Parameter(description = "Nivel para validar que el puesto este autorizado", required = false) @RequestParam(required = false) String nivel,
			@Parameter(description = "Sub nivel para validar que el puesto este autorizado", required = false) @RequestParam(required = false) String subnivel,
			@Parameter(description = "Tipo jornada para validar que el puesto este autorizado", required = false) @RequestParam(required = false) String tipo_jornada,
			@Parameter(description = "Tipo de guardia o suplencia para validar que el puesto este autorizado", required = false) @RequestParam(required = false) String tipo_guardia ) {

		try {
			List<DatosMatrizPuestos> matrix = new ArrayList<DatosMatrizPuestos>();

			final String regexAlfanumerico6_7 = "^[a-zA-Z0-9]{6,7}$";
			final String regexAlfanumerico_5 = "^[a-zA-Z0-9]{5}$";
			final String regexNumerico_1 = "^\\d{1}$";
			final String regexNumerico_2 = "^\\d{2}$";
			final String regexDecimal = "^\\d+\\.\\d+$";

			final String regexNiv = "^[0-9]{2}$";
			final String regexSubniv = "^[0-9]{1}$";
			final String regexPuntoDecimal = "^[0-9]*\\.?[0-9]+$";

//			String regexCURP = "^[A-Z]{4}\\d{6}[HM][A-Z]{2}[B-DF-HJ-NP-TV-Z]{3}[A-Z\\d]$";

			String message = "";
			List<String> params = new ArrayList<>();
			List<String> regexList = new ArrayList<>();


			if(clave_servicio != null){
				params.add(clave_servicio);
				regexList.add(regexAlfanumerico_5);
			}

			if(puesto != null){
				params.add(puesto);
				regexList.add(regexAlfanumerico6_7);
			}

			if(nivel != null){
				params.add(nivel);
				regexList.add(regexNiv);
			}

			if(subnivel != null){
				params.add(subnivel);
				regexList.add(regexSubniv);
			}

			if(tipo_jornada != null){
				params.add(tipo_jornada);

				if(tipo_jornada.length() == 1){
					regexList.add(regexSubniv);
				}else{
					regexList.add(regexPuntoDecimal);
				}

			}

			boolean regexResult = paramsValidatorService.validate(regexList,params);
			boolean injection = paramsValidatorService.sqlInjectionObjectValidator(params);

//			System.out.println(regexResult+" "+ injection);

			if(regexResult && !injection){
				matrix = datosRepository.ConsultaPuestoAutorizado(tipo_ct, clave_servicio, puesto, nivel, subnivel, tipo_jornada, tipo_guardia.substring(1));

				if (matrix.isEmpty()) {
					return ResponseHandler.generateResponse("Estas características no son autarizadas para guardias o suplencias", HttpStatus.NOT_FOUND, null);
				}

				return ResponseHandler.generateResponse("Las características son autarizadas para guardias o suplencias", HttpStatus.OK, matrix);

			}else{
				message = "Revise los campos de consulta";
				return ResponseHandler.generateResponse(message, HttpStatus.NOT_FOUND, null);
			}

		} catch (Exception e) {
			return ResponseHandler.generateResponse("Error al validar sí las características son autarizadas para guardias o suplencias", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}


	@Operation(summary = "Obtener el catálogo de GF", description = "Obtener el catálogo de GF", tags = { "Datos" })
	@GetMapping("/datos/gf")
	public ResponseEntity<Object> getGf(
			@Parameter(description = "Tipo (G- Guardia) ó (S -Suplencia)", required = false) @RequestParam(required = false) String tipo,
			@Parameter(description = "Unidad Responsable", required = false) @RequestParam(required = false) String ur,
			@Parameter(description = "Centro de trabajo", required = false) @RequestParam(required = false) String ct,
			@Parameter(description = "Auxiliar", required = false) @RequestParam(required = false) String aux
	) {

		List<String> gfs = new ArrayList<String>();

		try {

			gfs = datosRepository.getGf(tipo, ur, ct, aux);

			if(gfs.isEmpty()){
				return ResponseHandler.generateResponse("No existen registros en el catálogo de GF", HttpStatus.NOT_FOUND, null);
			}

			return ResponseHandler.generateResponse("Se encontró el catálogo de GF", HttpStatus.OK, gfs);

		} catch (Exception e) {
			return ResponseHandler.generateResponse("Error al consultar el catálogo de GF", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Obtener el catálogo de FN", description = "Obtener el catálogo de FN", tags = { "Datos" })
	@GetMapping("/datos/fn")
	public ResponseEntity<Object> getFn(
			@Parameter(description = "Tipo (G- Guardia) ó (S -Suplencia)", required = false) @RequestParam(required = false) String tipo,
			@Parameter(description = "Unidad Responsable", required = false) @RequestParam(required = false) String ur,
			@Parameter(description = "Centro de trabajo", required = false) @RequestParam(required = false) String ct,
			@Parameter(description = "Auxiliar", required = false) @RequestParam(required = false) String aux
	) {

		List<String> fns = new ArrayList<String>();

		try {

			fns = datosRepository.getFn(tipo, ur, ct, aux);

			if(fns.isEmpty()){
				return ResponseHandler.generateResponse("No existen registros en el catálogo de FN", HttpStatus.NOT_FOUND, null);
			}

			return ResponseHandler.generateResponse("Se encontró el catálogo de FN", HttpStatus.OK, fns);

		} catch (Exception e) {
			return ResponseHandler.generateResponse("Error al consultar el catálogo de FN", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Obtener el catálogo de SF", description = "Obtener el catálogo de SF", tags = { "Datos" })
	@GetMapping("/datos/sf")
	public ResponseEntity<Object> getSf(
			@Parameter(description = "Tipo (G- Guardia) ó (S -Suplencia)", required = false) @RequestParam(required = false) String tipo,
			@Parameter(description = "Unidad Responsable", required = false) @RequestParam(required = false) String ur,
			@Parameter(description = "Centro de trabajo", required = false) @RequestParam(required = false) String ct,
			@Parameter(description = "Auxiliar", required = false) @RequestParam(required = false) String aux
	) {

		List<String> sfs = new ArrayList<String>();

		try {

			sfs = datosRepository.getSf(tipo, ur, ct, aux);

			if(sfs.isEmpty()){
				return ResponseHandler.generateResponse("No existen registros en el catálogo de SF", HttpStatus.NOT_FOUND, null);
			}

			return ResponseHandler.generateResponse("Se encontró el catálogo de SF", HttpStatus.OK, sfs);

		} catch (Exception e) {
			return ResponseHandler.generateResponse("Error al consultar el catálogo de SF", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Obtener el catálogo de PG", description = "Obtener el catálogo de PG", tags = { "Datos" })
	@GetMapping("/datos/pg")
	public ResponseEntity<Object> getPg(
			@Parameter(description = "Tipo (G- Guardia) ó (S -Suplencia)", required = false) @RequestParam(required = false) String tipo,
			@Parameter(description = "Unidad Responsable", required = false) @RequestParam(required = false) String ur,
			@Parameter(description = "Centro de trabajo", required = false) @RequestParam(required = false) String ct,
			@Parameter(description = "Auxiliar", required = false) @RequestParam(required = false) String aux
	) {

		List<String> pgs = new ArrayList<String>();

		try {

			pgs = datosRepository.getPg(tipo, ur, ct, aux);

			if(pgs.isEmpty()){
				return ResponseHandler.generateResponse("No existen registros en el catálogo de PG", HttpStatus.NOT_FOUND, null);
			}

			return ResponseHandler.generateResponse("Se encontró el catálogo de PG", HttpStatus.OK, pgs);

		} catch (Exception e) {
			return ResponseHandler.generateResponse("Error al consultar el catálogo de PG", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Obtener el catálogo de FF", description = "Obtener el catálogo de FF", tags = { "Datos" })
	@GetMapping("/datos/ff")
	public ResponseEntity<Object> getFf(
			@Parameter(description = "Tipo (G- Guardia) ó (S -Suplencia)", required = false) @RequestParam(required = false) String tipo,
			@Parameter(description = "Unidad Responsable", required = false) @RequestParam(required = false) String ur,
			@Parameter(description = "Centro de trabajo", required = false) @RequestParam(required = false) String ct,
			@Parameter(description = "Auxiliar", required = false) @RequestParam(required = false) String aux
	) {

		List<String> ffs = new ArrayList<String>();

		try {

			ffs = datosRepository.getFf(tipo, ur, ct, aux);

			if(ffs.isEmpty()){
				return ResponseHandler.generateResponse("No existen registros en el catálogo de FF", HttpStatus.NOT_FOUND, null);
			}

			return ResponseHandler.generateResponse("Se encontró el catálogo de FF", HttpStatus.OK, ffs);

		} catch (Exception e) {
			return ResponseHandler.generateResponse("Error al consultar el catálogo de FF", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Obtener el catálogo de MUN", description = "Obtener el catálogo de MUN", tags = { "Datos" })
	@GetMapping("/datos/mun")
	public ResponseEntity<Object> getMun(
			@Parameter(description = "Tipo (G- Guardia) ó (S -Suplencia)", required = false) @RequestParam(required = false) String tipo,
			@Parameter(description = "Unidad Responsable", required = false) @RequestParam(required = false) String ur,
			@Parameter(description = "Centro de trabajo", required = false) @RequestParam(required = false) String ct,
			@Parameter(description = "Auxiliar", required = false) @RequestParam(required = false) String aux
	) {

		List<String> mun = new ArrayList<String>();

		try {

			mun = datosRepository.getMun(tipo, ur, ct, aux);

			if(mun.isEmpty()){
				return ResponseHandler.generateResponse("No existen registros en el catálogo de MUN", HttpStatus.NOT_FOUND, null);
			}

			return ResponseHandler.generateResponse("Se encontró el catálogo de MUN", HttpStatus.OK, mun);

		} catch (Exception e) {
			return ResponseHandler.generateResponse("Error al consultar el catálogo de MUN", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Obtener el catálogo de FD", description = "Obtener el catálogo de FD", tags = { "Datos" })
	@GetMapping("/datos/fd")
	public ResponseEntity<Object> getFd(
			@Parameter(description = "Tipo (G- Guardia) ó (S -Suplencia)", required = false) @RequestParam(required = false) String tipo,
			@Parameter(description = "Unidad Responsable", required = false) @RequestParam(required = false) String ur,
			@Parameter(description = "Centro de trabajo", required = false) @RequestParam(required = false) String ct,
			@Parameter(description = "Auxiliar", required = false) @RequestParam(required = false) String aux
	) {

		List<String> fds = new ArrayList<String>();

		try {

			fds = datosRepository.getFd(tipo, ur, ct, aux);

			if(fds.isEmpty()){
				return ResponseHandler.generateResponse("No existen registros en el catálogo de FD", HttpStatus.NOT_FOUND, null);
			}

			return ResponseHandler.generateResponse("Se encontró el catálogo de FD", HttpStatus.OK, fds);

		} catch (Exception e) {
			return ResponseHandler.generateResponse("Error al consultar el catálogo de FD", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Obtener el catálogo de PTDA", description = "Obtener el catálogo de PTDA", tags = { "Datos" })
	@GetMapping("/datos/ptda")
	public ResponseEntity<Object> getPtda(
			@Parameter(description = "Tipo (G- Guardia) ó (S -Suplencia)", required = false) @RequestParam(required = false) String tipo,
			@Parameter(description = "Unidad Responsable", required = false) @RequestParam(required = false) String ur,
			@Parameter(description = "Centro de trabajo", required = false) @RequestParam(required = false) String ct,
			@Parameter(description = "Auxiliar", required = false) @RequestParam(required = false) String aux
	) {

		List<String> ptdas = new ArrayList<String>();

		try {

			ptdas = datosRepository.getPtda(tipo, ur, ct, aux);

			if(ptdas.isEmpty()){
				return ResponseHandler.generateResponse("No existen registros en el catálogo de PTDA", HttpStatus.NOT_FOUND, null);
			}

			return ResponseHandler.generateResponse("Se encontró el catálogo de PTDA", HttpStatus.OK, ptdas);

		} catch (Exception e) {
			return ResponseHandler.generateResponse("Error al consultar el catálogo de PTDA", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Obtener el catálogo de SBPTD", description = "Obtener el catálogo de SBPTD", tags = { "Datos" })
	@GetMapping("/datos/sbptd")
	public ResponseEntity<Object> getSbptd(
			@Parameter(description = "Tipo (G- Guardia) ó (S -Suplencia)", required = false) @RequestParam(required = false) String tipo,
			@Parameter(description = "Unidad Responsable", required = false) @RequestParam(required = false) String ur,
			@Parameter(description = "Centro de trabajo", required = false) @RequestParam(required = false) String ct,
			@Parameter(description = "Auxiliar", required = false) @RequestParam(required = false) String aux
	) {

		List<String> sbptds = new ArrayList<String>();

		try {

			sbptds = datosRepository.getSbptd(tipo, ur, ct, aux);

			if(sbptds.isEmpty()){
				return ResponseHandler.generateResponse("No existen registros en el catálogo de SBPTD", HttpStatus.NOT_FOUND, null);
			}

			return ResponseHandler.generateResponse("Se encontró el catálogo de SBPTD", HttpStatus.OK, sbptds);

		} catch (Exception e) {
			return ResponseHandler.generateResponse("Error al consultar el catálogo de SBPTD", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Obtener el catálogo de TP", description = "Obtener el catálogo de TP", tags = { "Datos" })
	@GetMapping("/datos/tp")
	public ResponseEntity<Object> getTp(
			@Parameter(description = "Tipo (G- Guardia) ó (S -Suplencia)", required = false) @RequestParam(required = false) String tipo,
			@Parameter(description = "Unidad Responsable", required = false) @RequestParam(required = false) String ur,
			@Parameter(description = "Centro de trabajo", required = false) @RequestParam(required = false) String ct,
			@Parameter(description = "Auxiliar", required = false) @RequestParam(required = false) String aux
	) {

		List<String> tps = new ArrayList<String>();

		try {

			tps = datosRepository.getTp(tipo, ur, ct, aux);

			if(tps.isEmpty()){
				return ResponseHandler.generateResponse("No existen registros en el catálogo de TP", HttpStatus.NOT_FOUND, null);
			}

			return ResponseHandler.generateResponse("Se encontró el catálogo de TP", HttpStatus.OK, tps);

		} catch (Exception e) {
			return ResponseHandler.generateResponse("Error al consultar el catálogo de TP", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Obtener el catálogo de TPP", description = "Obtener el catálogo de TPP", tags = { "Datos" })
	@GetMapping("/datos/tpp")
	public ResponseEntity<Object> getTpp(
			@Parameter(description = "Tipo (G- Guardia) ó (S -Suplencia)", required = false) @RequestParam(required = false) String tipo,
			@Parameter(description = "Unidad Responsable", required = false) @RequestParam(required = false) String ur,
			@Parameter(description = "Centro de trabajo", required = false) @RequestParam(required = false) String ct,
			@Parameter(description = "Auxiliar", required = false) @RequestParam(required = false) String aux
	) {

		List<String> tpps = new ArrayList<String>();

		try {

			tpps = datosRepository.getTpp(tipo, ur, ct, aux);

			if(tpps.isEmpty()){
				return ResponseHandler.generateResponse("No existen registros en el catálogo de TPP", HttpStatus.NOT_FOUND, null);
			}

			return ResponseHandler.generateResponse("Se encontró el catálogo de TPP", HttpStatus.OK, tpps);

		} catch (Exception e) {
			return ResponseHandler.generateResponse("Error al consultar el catálogo de TPP", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Obtener el catálogo de FDO", description = "Obtener el catálogo de FDO", tags = { "Datos" })
	@GetMapping("/datos/fdo")
	public ResponseEntity<Object> getFdo(
			@Parameter(description = "Tipo (G- Guardia) ó (S -Suplencia)", required = false) @RequestParam(required = false) String tipo,
			@Parameter(description = "Unidad Responsable", required = false) @RequestParam(required = false) String ur,
			@Parameter(description = "Centro de trabajo", required = false) @RequestParam(required = false) String ct,
			@Parameter(description = "Auxiliar", required = false) @RequestParam(required = false) String aux
	) {

		List<String> fdos = new ArrayList<String>();

		try {

			fdos = datosRepository.getFdo(tipo, ur, ct, aux);

			if(fdos.isEmpty()){
				return ResponseHandler.generateResponse("No existen registros en el catálogo de FDO", HttpStatus.NOT_FOUND, null);
			}

			return ResponseHandler.generateResponse("Se encontró el catálogo de FDO", HttpStatus.OK, fdos);

		} catch (Exception e) {
			return ResponseHandler.generateResponse("Error al consultar el catálogo de FDO", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Obtener el catálogo de AREA", description = "Obtener el catálogo de AREA", tags = { "Datos" })
	@GetMapping("/datos/area")
	public ResponseEntity<Object> getArea(
			@Parameter(description = "Tipo (G- Guardia) ó (S -Suplencia)", required = false) @RequestParam(required = false) String tipo,
			@Parameter(description = "Unidad Responsable", required = false) @RequestParam(required = false) String ur,
			@Parameter(description = "Centro de trabajo", required = false) @RequestParam(required = false) String ct,
			@Parameter(description = "Auxiliar", required = false) @RequestParam(required = false) String aux
	) {

		List<String> areas = new ArrayList<String>();

		try {

			areas = datosRepository.getArea(tipo, ur, ct, aux);

			if(areas.isEmpty()){
				return ResponseHandler.generateResponse("No existen registros en el catálogo de AREA", HttpStatus.NOT_FOUND, null);
			}

			return ResponseHandler.generateResponse("Se encontró el catálogo de AREA", HttpStatus.OK, areas);

		} catch (Exception e) {
			return ResponseHandler.generateResponse("Error al consultar el catálogo de AREA", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Obtener el catálogo de AREA", description = "Obtener el catálogo de AI", tags = { "Datos" })
	@GetMapping("/datos/ai")
	public ResponseEntity<Object> getAi(
			@Parameter(description = "Tipo (G- Guardia) ó (S -Suplencia)", required = false) @RequestParam(required = false) String tipo,
			@Parameter(description = "Unidad Responsable", required = false) @RequestParam(required = false) String ur,
			@Parameter(description = "Centro de trabajo", required = false) @RequestParam(required = false) String ct,
			@Parameter(description = "Auxiliar", required = false) @RequestParam(required = false) String aux
	) {

		List<String> ai = new ArrayList<String>();

		try {

			ai = datosRepository.getAi(tipo, ur, ct, aux);

			if(ai.isEmpty()){
				return ResponseHandler.generateResponse("No existen registros en el catálogo de AREA", HttpStatus.NOT_FOUND, null);
			}

			return ResponseHandler.generateResponse("Se encontró el catálogo de AREA", HttpStatus.OK, ai);

		} catch (Exception e) {
			return ResponseHandler.generateResponse("Error al consultar el catálogo de AREA", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Obtener el catálogo de AREA", description = "Obtener el catálogo de AP", tags = { "Datos" })
	@GetMapping("/datos/ap")
	public ResponseEntity<Object> getAp(
			@Parameter(description = "Tipo (G- Guardia) ó (S -Suplencia)", required = false) @RequestParam(required = false) String tipo,
			@Parameter(description = "Unidad Responsable", required = false) @RequestParam(required = false) String ur,
			@Parameter(description = "Centro de trabajo", required = false) @RequestParam(required = false) String ct,
			@Parameter(description = "Auxiliar", required = false) @RequestParam(required = false) String aux
	) {

		List<String> ap = new ArrayList<String>();

		try {

			ap = datosRepository.getAp(tipo, ur, ct, aux);

			if(ap.isEmpty()){
				return ResponseHandler.generateResponse("No existen registros en el catálogo de AP", HttpStatus.NOT_FOUND, null);
			}

			return ResponseHandler.generateResponse("Se encontró el catálogo de AP", HttpStatus.OK, ap);

		} catch (Exception e) {
			return ResponseHandler.generateResponse("Error al consultar el catálogo de AP", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Obtener el catálogo de AREA", description = "Obtener el catálogo de SP", tags = { "Datos" })
	@GetMapping("/datos/sp")
	public ResponseEntity<Object> getSp(
			@Parameter(description = "Tipo (G- Guardia) ó (S -Suplencia)", required = false) @RequestParam(required = false) String tipo,
			@Parameter(description = "Unidad Responsable", required = false) @RequestParam(required = false) String ur,
			@Parameter(description = "Centro de trabajo", required = false) @RequestParam(required = false) String ct,
			@Parameter(description = "Auxiliar", required = false) @RequestParam(required = false) String aux
	) {

		List<String> sp = new ArrayList<String>();

		try {

			sp = datosRepository.getSp(tipo, ur, ct, aux);

			if(sp.isEmpty()){
				return ResponseHandler.generateResponse("No existen registros en el catálogo de SP", HttpStatus.NOT_FOUND, null);
			}

			return ResponseHandler.generateResponse("Se encontró el catálogo de SP", HttpStatus.OK, sp);

		} catch (Exception e) {
			return ResponseHandler.generateResponse("Error al consultar el catálogo de SP", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Obtener el catálogo de AREA", description = "Obtener el catálogo de R", tags = { "Datos" })
	@GetMapping("/datos/r")
	public ResponseEntity<Object> getR(
			@Parameter(description = "Tipo (G- Guardia) ó (S -Suplencia)", required = false) @RequestParam(required = false) String tipo,
			@Parameter(description = "Unidad Responsable", required = false) @RequestParam(required = false) String ur,
			@Parameter(description = "Centro de trabajo", required = false) @RequestParam(required = false) String ct,
			@Parameter(description = "Auxiliar", required = false) @RequestParam(required = false) String aux
	) {

		List<String> r = new ArrayList<String>();

		try {

			r = datosRepository.getR(tipo, ur, ct, aux);

			if(r.isEmpty()){
				return ResponseHandler.generateResponse("No existen registros en el catálogo de R", HttpStatus.NOT_FOUND, null);
			}

			return ResponseHandler.generateResponse("Se encontró el catálogo de R", HttpStatus.OK, r);

		} catch (Exception e) {
			return ResponseHandler.generateResponse("Error al consultar el catálogo de R", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

}
package gob.issste.gys.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import gob.issste.gys.model.*;
import gob.issste.gys.repository.*;
import gob.issste.gys.service.ParamsValidatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
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
import gob.issste.gys.response.ResponseHandler;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

//@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class PresupuestoController {

	Logger logger = LoggerFactory.getLogger(JdbcTemplateDemo01Application.class);

	@Autowired
	ITipoMovPresupuestoRepository tipMovPresupRepository;
	@Autowired
	IDatosProgramaticaRepository datosProgRepository;
	@Autowired
	IMovimientosPresupuestoRepository movPresupuestoRepository;
	@Autowired
	ITiposPresupuestoRepository tiposPresupuestoRepository;
	@Autowired
	IPresupuestoRepository presupuestoRepository;
	@Autowired
	IPresupuestoGlobalRepository presupuestoGlobalRepository;
	@Autowired
	GuardiaRepository guardiaRepository;
	@Autowired
	ISuplenciaRepository suplenciaRepository;
	@Autowired
	IDatosRepository datosRepository;
	@Autowired
	UsuarioRepository usuarioRepository;

	@Autowired
	private PlatformTransactionManager platformTransactionManager;

	/* Presupuesto */

	@Operation(summary = "Agrega un nuevo presupuesto al Sistema", description = "Agrega un nuevo presupuesto al Sistema", tags = { "Presupuesto" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successful operation", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Presupuesto.class)) }),
			@ApiResponse(responseCode = "405", description = "Invalid input")
	})
	@PostMapping("/Presupuesto")
	public ResponseEntity<Object> createPresupuesto(
			@Parameter(description = "Objeto de Presupuesto a crear en el Sistema")
			@RequestBody Presupuesto presupuesto,
			@Parameter(description = "Comentarios del registro de presupuesto.", required = false)
			@RequestParam(name = "comentarios", required = false, defaultValue = "false") String comentarios) {

		DefaultTransactionDefinition paramTransactionDefinition = new DefaultTransactionDefinition();
		TransactionStatus status = platformTransactionManager.getTransaction(paramTransactionDefinition);
		int idPresup;
		double saldo_global=0, saldo_utilizado=0;
		List<String> listOfParams = new ArrayList<>();
		try {

			if( presupuesto.getCentroTrabajo() == null ) {
				saldo_global = presupuestoGlobalRepository.saldo_presup_global(presupuesto.getAnio(), presupuesto.getDelegacion().getId_div_geografica());
				saldo_utilizado = presupuestoRepository.suma_presupuestos(presupuesto.getAnio(), presupuesto.getDelegacion().getId_div_geografica());

				// TODO Validar que haya registro del Global
				if (saldo_global == 0) {
					platformTransactionManager.rollback(status);
					return ResponseHandler.generateResponse("No existe presupuesto global para la delegación en el año indicado", HttpStatus.INTERNAL_SERVER_ERROR, null);
				}

				// TODO Validar que el saldo a ingresar + saldo utilizado no exceda el saldo global
				if (presupuesto.getSaldo() + saldo_utilizado > saldo_global) {
					platformTransactionManager.rollback(status);
					return ResponseHandler.generateResponse("La suma del importe a registrar, más los demas presupuestos de la delegación, excede el presupuesto global de la delegación.", HttpStatus.INTERNAL_SERVER_ERROR, null);
				}

				if(presupuestoRepository.existe_presupuesto(presupuesto)>0) {
					platformTransactionManager.rollback(status);
					return ResponseHandler.generateResponse("Existe un registro de presupuesto en este mismo periodo", HttpStatus.INTERNAL_SERVER_ERROR, null);
				}
			} else {
				if(presupuestoRepository.existe_presupuesto_ct(presupuesto)>0) {
					platformTransactionManager.rollback(status);
					return ResponseHandler.generateResponse("Existe un registro de presupuesto en este mismo periodo", HttpStatus.INTERNAL_SERVER_ERROR, null);
				}
				List<Presupuesto> presupuestos= presupuestoRepository.get_dynamic_regs(presupuesto.getDelegacion().getId_div_geografica(), 
						presupuesto.getTipoPresup().getClave(), presupuesto.getAnio(), presupuesto.getMes(), null, true);
				if (presupuestos == null || presupuestos.size() == 0) {
					platformTransactionManager.rollback(status);
					return ResponseHandler.generateResponse("No existe presupuesto asignado a la Delegación para asignar al centro de trabajo indicado", HttpStatus.INTERNAL_SERVER_ERROR, null);
				}

				double suma_dist_ct = presupuestoRepository.validaSumaPresupuestal(presupuesto);
				if( suma_dist_ct > presupuestos.get(0).getSaldo() ) {
					platformTransactionManager.rollback(status);
					return ResponseHandler.generateResponse("La suma del importe asignado al centro de trabajo indicado excede el presupuesto asignado a la Delegación", HttpStatus.INTERNAL_SERVER_ERROR, null);
				}
			}

			if( presupuesto.getCentroTrabajo() == null ) {
				listOfParams.addAll(List.of(
						presupuesto.getDelegacion().getId_div_geografica(),
						presupuesto.getDelegacion().getN_div_geografica(),
						presupuesto.getTipoPresup().getClave(),
						presupuesto.getTipoPresup().getDescripcion()
				));
				if(ParamsValidatorService.csvInjectionObjectValidator(listOfParams) || ParamsValidatorService.csvInjectionValidator(comentarios)){
					return ResponseHandler.generateResponse("Algunos datos contienen caracteres no permitidos",
							HttpStatus.INTERNAL_SERVER_ERROR, null);
				}else{
					idPresup = presupuestoRepository.save(new Presupuesto(presupuesto.getAnio(), presupuesto.getMes(), presupuesto.getQuincena(), presupuesto.getDelegacion(), presupuesto.getCentroTrabajo()
							, null ,presupuesto.getTipoPresup(), presupuesto.getSaldo()));
				}
			} else {
				listOfParams.addAll(List.of(
						presupuesto.getDelegacion().getId_div_geografica(),
						presupuesto.getDelegacion().getN_div_geografica(),
						presupuesto.getTipoPresup().getDescripcion(),
						presupuesto.getDatosProgramatica().getGf(),
						presupuesto.getDatosProgramatica().getFn(),
						presupuesto.getDatosProgramatica().getSf(),
						presupuesto.getDatosProgramatica().getPg(),
						presupuesto.getDatosProgramatica().getFf(),
						presupuesto.getDatosProgramatica().getAi(),
						presupuesto.getDatosProgramatica().getAp(),
						presupuesto.getDatosProgramatica().getSp(),
						presupuesto.getDatosProgramatica().getR(),
						presupuesto.getDatosProgramatica().getMun(),
						presupuesto.getDatosProgramatica().getFd(),
						presupuesto.getDatosProgramatica().getPtda(),
						presupuesto.getDatosProgramatica().getSbptd(),
						presupuesto.getDatosProgramatica().getTp(),
						presupuesto.getDatosProgramatica().getTpp(),
						presupuesto.getDatosProgramatica().getFdo(),
						presupuesto.getDatosProgramatica().getArea(),
						presupuesto.getDatosProgramatica().getTipo()
				));
				idPresup = presupuestoRepository.save_ct(new Presupuesto(presupuesto.getAnio(), presupuesto.getMes(), presupuesto.getQuincena(), presupuesto.getDelegacion(), presupuesto.getCentroTrabajo()
						, presupuesto.getDatosProgramatica() ,presupuesto.getTipoPresup(), presupuesto.getSaldo()));
				int datosProg = datosProgRepository.save(new DatosProgramatica(
						idPresup, presupuesto.getDatosProgramatica().getGf(), presupuesto.getDatosProgramatica().getFn(), presupuesto.getDatosProgramatica().getSf(), presupuesto.getDatosProgramatica().getPg(), presupuesto.getDatosProgramatica().getFf(),
						presupuesto.getDatosProgramatica().getAi(), presupuesto.getDatosProgramatica().getAp(), presupuesto.getDatosProgramatica().getSp(), presupuesto.getDatosProgramatica().getR(), presupuesto.getDatosProgramatica().getMun(), presupuesto.getDatosProgramatica().getFd(), presupuesto.getDatosProgramatica().getPtda(),
						presupuesto.getDatosProgramatica().getSbptd(), presupuesto.getDatosProgramatica().getTp(), presupuesto.getDatosProgramatica().getTpp(), presupuesto.getDatosProgramatica().getFdo(), presupuesto.getDatosProgramatica().getArea(), presupuesto.getDatosProgramatica().getTipo()
				));
			}
			if (ParamsValidatorService.csvInjectionObjectValidator(listOfParams)
					|| ParamsValidatorService.csvInjectionValidator(comentarios)){
				return ResponseHandler.generateResponse("Algunos datos contienen caracteres no permitidos",
						HttpStatus.INTERNAL_SERVER_ERROR, null);
			} else {
				int idMovPresup = movPresupuestoRepository.save(new MovimientosPresupuesto(idPresup, presupuesto.getSaldo(), comentarios, 1));
				platformTransactionManager.commit(status);
				return ResponseHandler.generateResponse("El Presupuesto con ID " + idPresup + " ha sido creado de manera exitosa. Con número de movimiento: " + idMovPresup, HttpStatus.OK, null);
			}
		} catch (Exception e) {
			platformTransactionManager.rollback(status);
			return ResponseHandler.generateResponse("Error al realizar el registro de presupuesto", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}
	@Operation(summary = "Agrega un nuevo presupuesto al Sistema", description = "Agrega un nuevo presupuesto al Sistema", tags = { "Presupuesto" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successful operation", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Presupuesto.class)) }),
			@ApiResponse(responseCode = "405", description = "Invalid input")
	})
	@PostMapping("/Presupuestos")
	public ResponseEntity<Object> createPresupuestos(
			@Parameter(description = "Objeto de Presupuesto a crear en el Sistema") @RequestBody PresupuestosGlobal presupuestos) {

		DefaultTransactionDefinition paramTransactionDefinition = new DefaultTransactionDefinition();
		TransactionStatus status = platformTransactionManager.getTransaction(paramTransactionDefinition);
		//int idPresup;

		try {

			List<Presupuesto> presup = presupuestos.getPresupuestos();
			List<String> coments = presupuestos.getComentarios();

			for (int i = 0; i < presup.size(); i ++) {
	            System.out.println("Presupuesto " + presup.get(i) + " is located at index " + i);
	            System.out.println("Comentario " + coments.get(i) + " is located at index " + i);
	        }

//			if( presupuesto.getCentroTrabajo() == null ) {
//				if(presupuestoRepository.existe_presupuesto(presupuesto)>0) {
//					return ResponseHandler.generateResponse("Existe un registro de presupuesto en este mismo periodo", HttpStatus.INTERNAL_SERVER_ERROR, null);
//				}
//			} else {
//				if(presupuestoRepository.existe_presupuesto_ct(presupuesto)>0) {
//					return ResponseHandler.generateResponse("Existe un registro de presupuesto en este mismo periodo", HttpStatus.INTERNAL_SERVER_ERROR, null);
//				}
//				List<Presupuesto> presupuestos= presupuestoRepository.get_dynamic_regs(presupuesto.getDelegacion().getId_div_geografica(), 
//						presupuesto.getTipoPresup().getClave(), presupuesto.getAnio(), presupuesto.getMes(), null, true);
//				if (presupuestos == null || presupuestos.size() == 0)
//					return ResponseHandler.generateResponse("No existe presupuesto asignado a la Delegación para asignar al centro de trabajo indicado", HttpStatus.INTERNAL_SERVER_ERROR, null);
//
//				double suma_dist_ct = presupuestoRepository.validaSumaPresupuestal(presupuesto);
//				if( suma_dist_ct > presupuestos.get(0).getSaldo() ) {
//					return ResponseHandler.generateResponse("La suma del importe asignado al centro de trabajo indicado excede el presupuesto asignado a la Delegación", HttpStatus.INTERNAL_SERVER_ERROR, null);
//				}
//			}
//
//			if( presupuesto.getCentroTrabajo() == null ) {
//				idPresup = presupuestoRepository.save(new Presupuesto(presupuesto.getAnio(), presupuesto.getMes(), presupuesto.getDelegacion(), presupuesto.getCentroTrabajo(),
//						presupuesto.getTipoPresup(), presupuesto.getSaldo()));				
//			} else {				
//				idPresup = presupuestoRepository.save_ct(new Presupuesto(presupuesto.getAnio(), presupuesto.getMes(), presupuesto.getDelegacion(), presupuesto.getCentroTrabajo(),
//						presupuesto.getTipoPresup(), presupuesto.getSaldo()));
//			}
//			int idMovPresup = movPresupuestoRepository.save(new MovimientosPresupuesto(idPresup, presupuesto.getSaldo(), comentarios, 1));
//			platformTransactionManager.commit(status);
			return ResponseHandler.generateResponse("El Presupuesto con ID ha sido creado de manera exitosa. Con número de movimiento: ", HttpStatus.OK, null);
		} catch (Exception e) {
			platformTransactionManager.rollback(status);
			return ResponseHandler.generateResponse("Error al realizar el registro de presupuesto", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}
	@Operation(summary = "Consulta elementos de presupuesto del Sistema", description = "Consulta elementos de presupuesto del Sistema", tags = { "Presupuesto" })
	@GetMapping("/Presupuesto")
	public ResponseEntity<Object> getPresupuesto(
			@Parameter(description = "Bandera para indicar si se requiere incluir los movimientos presupuestales", required = true) @RequestParam(name = "movim", required = true, defaultValue = "false") boolean movim,
			@Parameter(description = "Bandera para indicar si se requiere incluir los centros de trabajo de la delegación", required = true) @RequestParam(name = "solo_deleg", required = true, defaultValue = "true") boolean solo_deleg,
			@Parameter(description = "Parámetro opcional para indicar el anio del que se desea consultar el presupuesto", required = false) @RequestParam(required = false) Integer anio,
			@Parameter(description = "Parámetro opcional para indicar el mes del que se desea consultar el presupuesto", required = false) @RequestParam(required = false) Integer mes,
			@Parameter(description = "Parámetro opcional para indicar el ID de la delegación del que se desea consultar el presupuesto", required = false) @RequestParam(required = false) String idDelegacion,
			@Parameter(description = "Parámetro opcional para indicar el ID del Centro de trabajo del que se desea consultar el presupuesto", required = false) @RequestParam(required = false) String idCentTrab,
			@Parameter(description = "Parámetro opcional para indicar el ID del Tipo de presupuesto del que se desea consultar el presupuesto", required = false) @RequestParam(required = false) String claveTipoPresup) {
		try {
			List<Presupuesto> presupuestos = new ArrayList<Presupuesto>();

			if ( (idDelegacion != null) || (claveTipoPresup != null) || (anio != null) || (idCentTrab != null) ) {
				presupuestos= presupuestoRepository.get_dynamic_regs(idDelegacion, claveTipoPresup, anio, mes, idCentTrab, solo_deleg);
			} else {
				presupuestos = presupuestoRepository.findAll();
			}
			if (movim) {
				for (Presupuesto p:presupuestos)
					p.setMovimientos(movPresupuestoRepository.findByPresupuesto(p.getId()));
			}
			if (presupuestos.isEmpty()) {
				return ResponseHandler.generateResponse("No existen elementos de presupuesto en el Sistema", HttpStatus.NOT_FOUND, presupuestos);
			}

			return ResponseHandler.generateResponse("Se obtienen los elementos de presupuesto del Sistema", HttpStatus.OK, presupuestos);
		} catch (Exception e) {
			return ResponseHandler.generateResponse("Error al consultar elementos de presupuesto del Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}
	@Operation(summary = "Consulta elementos de presupuesto del Sistema", description = "Consulta elementos de presupuesto del Sistema", tags = { "Presupuesto" })
	@GetMapping("/Presupuesto/ct")
	public ResponseEntity<Object> getPresupuestoCt(
			@Parameter(description = "Parámetro para indicar el ID del Usuario del que se desea consultar el presupuesto", required = true) @RequestParam(required = true) Integer idUsuario,
			@Parameter(description = "Parámetro para indicar el anio del que se desea consultar el presupuesto", required = true) @RequestParam(required = true) Integer anio,
			@Parameter(description = "Parámetro para indicar el mes del que se desea consultar el presupuesto", required = true) @RequestParam(required = true) Integer mes,
			@Parameter(description = "Parámetro para indicar la quincena de la que se desea consultar el presupuesto", required = true) @RequestParam(required = true) Integer quincena,
			@Parameter(description = "Parámetro para indicar el ID de la delegación del que se desea consultar el presupuesto", required = true) @RequestParam(required = true) String idDelegacion,
			@Parameter(description = "Parámetro opcional para indicar el ID del Tipo de presupuesto del que se desea consultar el presupuesto", required = true) @RequestParam(required = true) String claveTipoPresup) {
		try {
			final String regexDeleg = "^[0-9]{2}$";
			String message = "";
			List<Presupuesto> presupuestos = new ArrayList<Presupuesto>();
			List<DatosAdscripcion> adsc; // = datosRepository.getDatosAdscripciones(idUsuario);
			TiposPresupuesto tipoPresup = tiposPresupuestoRepository.findByClave(claveTipoPresup);
			Usuario usuario = usuarioRepository.findById(idUsuario);
			Delegacion delegacion = datosRepository.getDatosDelegacionById(idDelegacion);
			Presupuesto presupuesto;
			double saldoDelegCt;

			final String[] parameters = new String[] {idDelegacion, claveTipoPresup};
			List<String> params = new ArrayList<>();
			for(String param : parameters){
				if (param != null){
					params.add(param);
				}
			}

			ParamsValidatorService paramsValidatorService = new ParamsValidatorService();
			final boolean regexValidation = paramsValidatorService.regexValidation(regexDeleg,idDelegacion);
			final boolean injectableValues = paramsValidatorService.sqlInjectionObjectValidator(params);

			if(injectableValues || !regexValidation){
				if (injectableValues){
					message = "Intento de inyeccion detectado";
				} else {
					message = "Valor rechazado por la expresion regular";
				}
				return ResponseHandler.generateResponse(message, HttpStatus.NOT_ACCEPTABLE, null);
			}

			if ( usuario.getNivelVisibilidad().getIdNivelVisibilidad()== 1 ) {
				try {
					presupuesto = presupuestoRepository.getDatosPresup(delegacion.getId_div_geografica(), claveTipoPresup, anio, mes);
					presupuesto.setDelegacion(usuario.getDelegacion());
					presupuesto.setTipoPresup(tipoPresup);
				} catch (EmptyResultDataAccessException e) {
					saldoDelegCt = presupuestoRepository.getSaldoDelegCt(delegacion.getId_div_geografica(), claveTipoPresup, anio, mes, null);
					presupuesto = new Presupuesto(anio, mes, quincena, usuario.getDelegacion(), null, null, tipoPresup, saldoDelegCt);
				}
				presupuestos.add(presupuesto);
				adsc = datosRepository.getDatosAdscForDeleg(delegacion.getId_div_geografica());

				for(DatosAdscripcion ct:adsc) {

					try {
						presupuesto = presupuestoRepository.getDatosPresupCt(delegacion.getId_div_geografica(), claveTipoPresup, anio, mes, quincena, ct.getClave());
						DatosProgramatica programatica = datosProgRepository.getProgDataByIdPresupuesto(presupuesto.getId());
						presupuesto.setDatosProgramatica(programatica);
						presupuesto.setDelegacion(usuario.getDelegacion());
						presupuesto.setTipoPresup(tipoPresup);
						presupuesto.setCentroTrabajo(ct);
					} catch (EmptyResultDataAccessException e) {
						saldoDelegCt = presupuestoRepository.getSaldoDelegCt(usuario.getDelegacion().getId_div_geografica(), claveTipoPresup, anio, mes, ct.getClave());
						presupuesto = new Presupuesto(anio, mes, quincena, usuario.getDelegacion(), ct, null, tipoPresup, saldoDelegCt);
					}
					presupuestos.add(presupuesto);
				}
			} else if ( usuario.getNivelVisibilidad().getIdNivelVisibilidad()==2 ) {
				if ( usuario.getDelegacion().getId_div_geografica().compareTo(idDelegacion) != 0 ) {
					return ResponseHandler.generateResponse("El usuario tiene visibilidad por Delegación y la Delegación indicada no corresponde con la que tiene asignada", HttpStatus.NOT_FOUND, null);
				}
				try {
					presupuesto = presupuestoRepository.getDatosPresup(delegacion.getId_div_geografica(), claveTipoPresup, anio, mes);
					presupuesto.setDelegacion(usuario.getDelegacion());
					presupuesto.setTipoPresup(tipoPresup);
				} catch (EmptyResultDataAccessException e) {
					saldoDelegCt = presupuestoRepository.getSaldoDelegCt(delegacion.getId_div_geografica(), claveTipoPresup, anio, mes, null);
					presupuesto = new Presupuesto(anio, mes, quincena, usuario.getDelegacion(), null, null, tipoPresup, saldoDelegCt);
				}
				presupuestos.add(presupuesto);
				adsc = datosRepository.getDatosAdscForDeleg(usuario.getDelegacion().getId_div_geografica());
				logger.info(adsc.toString());
				for(DatosAdscripcion ct:adsc) {
					try {
						presupuesto = presupuestoRepository.getDatosPresupCt(delegacion.getId_div_geografica(), claveTipoPresup, anio, mes, quincena, ct.getClave());
						DatosProgramatica programatica = datosProgRepository.getProgDataByIdPresupuesto(presupuesto.getId());
						presupuesto.setDatosProgramatica(programatica);
						presupuesto.setDelegacion(usuario.getDelegacion());
						presupuesto.setTipoPresup(tipoPresup);
						presupuesto.setCentroTrabajo(ct);
					} catch (EmptyResultDataAccessException e) {
						saldoDelegCt = presupuestoRepository.getSaldoDelegCt(usuario.getDelegacion().getId_div_geografica(), claveTipoPresup, anio, mes, ct.getClave());
						presupuesto = new Presupuesto(anio, mes, quincena, usuario.getDelegacion(), ct, null, tipoPresup, saldoDelegCt);
					}
					presupuestos.add(presupuesto);
				}
			} else if ( usuario.getNivelVisibilidad().getIdNivelVisibilidad()==3 ) {
				try {
					presupuesto = presupuestoRepository.getDatosPresup(delegacion.getId_div_geografica(), claveTipoPresup, anio, mes);
					presupuesto.setDelegacion(usuario.getDelegacion());
					presupuesto.setTipoPresup(tipoPresup);
				} catch (EmptyResultDataAccessException e) {
					saldoDelegCt = presupuestoRepository.getSaldoDelegCt(delegacion.getId_div_geografica(), claveTipoPresup, anio, mes, null);
					presupuesto = new Presupuesto(anio, mes, quincena, usuario.getDelegacion(), null, null, tipoPresup, saldoDelegCt);
				}
				presupuestos.add(presupuesto);
				adsc = datosRepository.getDatosAdscripciones_ct(idUsuario);
				for(DatosAdscripcion ct:adsc) {
					try {
						presupuesto = presupuestoRepository.getDatosPresupCt(delegacion.getId_div_geografica(), claveTipoPresup, anio, mes, quincena, ct.getClave());
						DatosProgramatica programatica = datosProgRepository.getProgDataByIdPresupuesto(presupuesto.getId());
						presupuesto.setDatosProgramatica(programatica);
						presupuesto.setDelegacion(usuario.getDelegacion());
						presupuesto.setTipoPresup(tipoPresup);
						presupuesto.setCentroTrabajo(ct);
					} catch (EmptyResultDataAccessException e) {
						saldoDelegCt = presupuestoRepository.getSaldoDelegCt(usuario.getDelegacion().getId_div_geografica(), claveTipoPresup, anio, mes, ct.getClave());
						presupuesto = new Presupuesto(anio, mes, quincena, usuario.getDelegacion(), ct,null, tipoPresup, saldoDelegCt);
					}
					presupuestos.add(presupuesto);
				}
			}

			return ResponseHandler.generateResponse("Se obtienen los elementos de presupuesto del Sistema", HttpStatus.OK, presupuestos);

		} catch (Exception e) {
			return ResponseHandler.generateResponse("Error al consultar elementos de presupuesto del Sistema" + e, HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}
	@Operation(summary = "Consulta elementos de presupuesto del Sistema junto con el saldo utilizado", description = "Consulta elementos de presupuesto del Sistema junto con el saldo utilizado", tags = { "Presupuesto" })
	@GetMapping("/Presupuesto/utilizado")
	public ResponseEntity<Object> getPresupuestoUtilizado(
			@Parameter(description = "Bandera para indicar si se requiere incluir los movimientos presupuestales", required = true) @RequestParam(name = "movim", required = true, defaultValue = "false") boolean movim,
			@Parameter(description = "Bandera para indicar si se requiere incluir sólo la delegación en los resultados", required = true) @RequestParam(name = "solo_deleg", required = true, defaultValue = "true") boolean solo_deleg,
			@Parameter(description = "Parámetro opcional para indicar el anio del que se desea consultar el presupuesto", required = false) @RequestParam(required = false) Integer anio,
			@Parameter(description = "Parámetro opcional para indicar el mes del que se desea consultar el presupuesto", required = false) @RequestParam(required = false) Integer mes,
			@Parameter(description = "Parámetro opcional para indicar el ID de la delegación del que se desea consultar el presupuesto", required = false) @RequestParam(required = false) String idDelegacion,
			@Parameter(description = "Parámetro opcional para indicar el ID del Centro de trabajo del que se desea consultar el presupuesto", required = false) @RequestParam(required = false) String idCentTrab,
			@Parameter(description = "Parámetro opcional para indicar el ID del Tipo de presupuesto del que se desea consultar el presupuesto", required = false) @RequestParam(required = false) String claveTipoPresup) {
		try {
			List<Presupuesto> presupuestos = new ArrayList<Presupuesto>();
			List<PresupuestoUtilizado> presupuestosUtilizado = new ArrayList<PresupuestoUtilizado>();
			double saldo_utilizado=0;

			if ( (idDelegacion != null) || (claveTipoPresup != null) || (anio != null) || (idCentTrab != null) ) {
				presupuestos= presupuestoRepository.get_dynamic_regs(idDelegacion, claveTipoPresup, anio, mes, idCentTrab, solo_deleg);
			} else {
				presupuestos = presupuestoRepository.findAll();
			}
			if (movim) {
				for (Presupuesto p:presupuestos)
					p.setMovimientos(movPresupuestoRepository.findByPresupuesto(p.getId()));
			}
			if (presupuestos.isEmpty()) {
				return ResponseHandler.generateResponse("No existen elementos de presupuesto en el Sistema", HttpStatus.NOT_FOUND, null);
			}
			logger.info(presupuestos.toString());

			for(Presupuesto p:presupuestos) {
				saldo_utilizado=0;
				PresupuestoUtilizado pu = new PresupuestoUtilizado();
				pu.setPresupuesto(p);
				if (p.getTipoPresup().getClave().equals(String.valueOf("GI"))) {
					if (p.getCentroTrabajo() != null)
						saldo_utilizado = guardiaRepository.ObtenerSaldoUtilizado_ct(0, p.getCentroTrabajo().getClave(), p.getAnio(), p.getMes());
				} else if (p.getTipoPresup().getClave().equals(String.valueOf("GE"))) {
					if (p.getCentroTrabajo() != null)
						saldo_utilizado = guardiaRepository.ObtenerSaldoUtilizadoExt_ct(0, p.getCentroTrabajo().getClave(), p.getAnio(), p.getMes());
				} else if (p.getTipoPresup().getClave().equals(String.valueOf("SI"))) {
					if (p.getCentroTrabajo() != null)
						saldo_utilizado = suplenciaRepository.ObtenerSaldoUtilizado_ct(0, p.getCentroTrabajo().getClave(), p.getAnio(), p.getMes());
				} else if (p.getTipoPresup().getClave().equals(String.valueOf("SE"))) {
					if (p.getCentroTrabajo() != null)
						saldo_utilizado = suplenciaRepository.ObtenerSaldoUtilizadoExt_ct(0, p.getCentroTrabajo().getClave(), p.getAnio(), p.getMes());
				}
				pu.setSaldo(saldo_utilizado);
				presupuestosUtilizado.add(pu);
			}
			return ResponseHandler.generateResponse("Se obtienen los elementos de presupuesto del Sistema", HttpStatus.OK, presupuestosUtilizado);
		} catch (Exception e) {
			return ResponseHandler.generateResponse("Error al consultar elementos de presupuesto del Sistema", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}
	@Operation(summary = "Obtener el saldo utilizado por registro de guardias para una delegación en un año determinado", description = "Obtener el saldo utilizado por registro de guardias para una delegación en un año determinado", tags = { "Presupuesto" })
	@GetMapping("/Presupuesto/saldo")
	public ResponseEntity<Object> getSaldoPresupuesto(
			@Parameter(description = "Año del ejercicio del que se consulta el saldo utilizado", required = true) @RequestParam(required = true) Integer anio_ejercicio,
			@Parameter(description = "Mes del ejercicio del que se consulta el saldo utilizado", required = true) @RequestParam(required = true) Integer mes_ejercicio,
			@Parameter(description = "ID de la Delegación que se consulta el saldo utilizado", required = true) @RequestParam(required = true) String idDelegacion,
			@Parameter(description = "Tipo para obtener las guardidas del empleado (internas o externas)", required = true) @RequestParam(required = true) String claveTipoPresupuesto) {

		double saldo=0;

		try {

			saldo = presupuestoRepository.getSaldoDistribuido(anio_ejercicio, mes_ejercicio, idDelegacion, claveTipoPresupuesto);

			return ResponseHandler.generateResponse("Se obtuvo el saldo presupuestal distribuido para las condiciones indicadas", HttpStatus.OK, saldo);

		} catch (EmptyResultDataAccessException e) {

			return ResponseHandler.generateResponse("No existe presupuesto desglozado a la delegación seleccionada", HttpStatus.NOT_FOUND, 0);

		} catch (Exception e) {

			return ResponseHandler.generateResponse("Error al consultar elementos de presupuesto del Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);

		}
	}
	@Operation(summary = "Obtener la suma del saldo utilizado para una delegación en un año determinado", description = "Obtener la suma del saldo utilizado para una delegación en un año determinado", tags = { "Presupuesto" })
	@GetMapping("/Presupuesto/suma_saldo")
	public ResponseEntity<Object> getSaldoPresupuesto(
			@Parameter(description = "Año del ejercicio del que se consulta el saldo utilizado", required = true) @RequestParam(required = true) Integer anio_ejercicio,
			@Parameter(description = "ID de la Delegación que se consulta el saldo utilizado", required = true) @RequestParam(required = true) String idDelegacion) {

		double saldo=0;

		try {

			saldo = presupuestoRepository.suma_presupuestos(anio_ejercicio, idDelegacion);

			return ResponseHandler.generateResponse("Se obtuvo el saldo presupuestal para las condiciones indicadas", HttpStatus.OK, saldo);
		} catch (Exception e) {

			return ResponseHandler.generateResponse("Error al consultar elementos de presupuesto del Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	/* Tipos de Movimientos Presupuestales */

	@Operation(summary = "Agrega un nuevo elemento de tipos de presupuesto al Sistema", description = "Agrega un nuevo elemento de tipos de presupuesto al Sistema", tags = { "Tipos de movimiento de presupuesto" })
	@PostMapping("/TipoMovPresup")
	public ResponseEntity<Object> createTipMovPresup(@RequestBody TipoMovPresupuesto TipoMovPresupuesto) {
		try {
			tipMovPresupRepository.save(new TipoMovPresupuesto(TipoMovPresupuesto.getClave(), TipoMovPresupuesto.getDescripcion()));
			//return new ResponseEntity<>("El tipo de movimiento ha sido creado de manera exitosa", HttpStatus.OK);
			return ResponseHandler.generateResponse("El tipo de movimiento ha sido creado de manera exitosa", HttpStatus.OK, null);
		} catch (Exception e) {
			//return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			return ResponseHandler.generateResponse("Error al agregar nuevo elemento de tipos de presupuesto al Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Modifica un elemento de tipo de presupuesto al Sistema", description = "Modifica un elemento de tipo de presupuesto al Sistema", tags = { "Tipos de movimiento de presupuesto" })
	@PutMapping("/TipoMovPresup/{id}")
	public ResponseEntity<Object> updateTipMovPresup(@PathVariable("id") Integer id, @RequestBody TipoMovPresupuesto TipoMovPresupuesto) {
		TipoMovPresupuesto _tipo = tipMovPresupRepository.findById(id);

		if (_tipo != null) {
			_tipo.setClave(TipoMovPresupuesto.getClave());
			_tipo.setDescripcion(TipoMovPresupuesto.getDescripcion());

			tipMovPresupRepository.update(_tipo);
			return ResponseHandler.generateResponse("El tipo de movimiento ha sido modificado de manera exitosa", HttpStatus.OK, null);
		} else {
			return ResponseHandler.generateResponse("Error al agregar nuevo elemento de tipos de presupuesto al Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Consulta elementos de tipo de presupuesto al Sistema", description = "Consulta elementos de tipo de presupuesto al Sistema", tags = { "Tipos de movimiento de presupuesto" })
	@GetMapping("/TipoMovPresup")
	public ResponseEntity<Object> getTipMovPresup(
			@Parameter(description = "Descripcion del tipo de movimiento del que se desea obtener la información", required = false) @RequestParam(required = false) String desc) {
		try {
			List<TipoMovPresupuesto> opciones = new ArrayList<TipoMovPresupuesto>();

			if (desc == null)
				tipMovPresupRepository.findAll().forEach(opciones::add);
			else
				tipMovPresupRepository.findByDesc(desc).forEach(opciones::add);

			if (opciones.isEmpty()) {
				return ResponseHandler.generateResponse("No existen elementos del tipo de movimiento del que se desea obtener la información", HttpStatus.NOT_FOUND, null);
			}

			return ResponseHandler.generateResponse("Existen elementos del tipo de movimiento del que se desea obtener la información", HttpStatus.OK, opciones);
		} catch (Exception e) {
			return ResponseHandler.generateResponse("Error al consultar elementos de tipo de presupuesto al Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Obtiene un elemento de tipo de presupuesto del Sistema", description = "Obtiene un elemento de tipo de presupuesto del Sistema", tags = { "Tipos de movimiento de presupuesto" })
	@GetMapping("/TipoMovPresup/{id}")
	public ResponseEntity<Object> getTipMovPresupById(
			@Parameter(description = "ID del tipo de movimiento del que se desea obtener la información", required = true) @PathVariable("id") Integer id) {

		TipoMovPresupuesto TipoMovPresupuesto = tipMovPresupRepository.findById(id);

		if (TipoMovPresupuesto != null) {
			return ResponseHandler.generateResponse("Existe el elemento de tipo de movimiento de presupuesto del Sistema", HttpStatus.OK, TipoMovPresupuesto);
		} else {
			return ResponseHandler.generateResponse("Error al obtener un elemento de tipo de movimiento de presupuesto del Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Elimina un elemento de tipo de presupuesto del Sistema", description = "Elimina un elemento de tipo de presupuesto del Sistema", tags = { "Tipos de movimiento de presupuesto" })
	@DeleteMapping("/TipoMovPresup/{id}")
	public ResponseEntity<Object> deleteTipMovPresup(@PathVariable("id") Integer id) {
		try {
			int result = tipMovPresupRepository.deleteById(id);
			if (result == 0) {
				return ResponseHandler.generateResponse("No se pudo encontrar el tipo de movimiento con el ID =" + id, HttpStatus.NOT_FOUND, null);
			}
			return ResponseHandler.generateResponse("Existe el elemento de tipo de movimiento de presupuesto del Sistema", HttpStatus.OK, null);
		} catch (Exception e) {
			return ResponseHandler.generateResponse("Error al eliminar un elemento de tipo de presupuesto del Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}


	/* Movimientos Presupuestales */

	@Operation(summary = "Obtiene movimientos del presupuesto en el Sistema", description = "Obtiene movimientos del presupuesto en el Sistema", tags = { "Movimientos de presupuesto" })
	@GetMapping("/MovimientosPresup")
	//public ResponseEntity<List<MovimientosPresupuesto>> getMovimPresup(
	public ResponseEntity<Object> getMovimPresup(
			@Parameter(description = "Descripcion del tipo de movimiento del que se desea obtener la información", required = false) @RequestParam(required = false) Integer tipo) {
		try {
			List<MovimientosPresupuesto> movimientos = new ArrayList<MovimientosPresupuesto>();

			if (tipo == null) {
				movPresupuestoRepository.findAll().forEach(movimientos::add);
			} else {
				movPresupuestoRepository.findByTipo(tipo);
			}

			if (movimientos.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}

			//return new ResponseEntity<>(movimientos, HttpStatus.OK);
			return ResponseHandler.generateResponse("Existen movimientos del presupuesto en el Sistema", HttpStatus.OK, movimientos);
		} catch (Exception e) {
			//return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			return ResponseHandler.generateResponse("Error al obtener movimientos del presupuesto en el Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Consulta presupuesto por Id",
			description = "Consulta de presupuesto junto con su función programatica",
			tags = { "Presupuesto" })
	@GetMapping("/Presupuesto/dpbyid")
	public ResponseEntity<Object> getDatosProgByIdPresup(@RequestParam int idPresup){
		Presupuesto presupuesto = null;
		try {
			 presupuesto = datosProgRepository.getElementByIdWProgData(idPresup);
		}catch (Exception e){
			logger.error("Fucking error: " + e);
			return ResponseHandler.generateResponse("Error al consultar el presupuesto con el id: " + idPresup,
					HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
		return ResponseHandler.generateResponse("Presupuesto con id " + idPresup +" consultado cón éxito", HttpStatus.OK, presupuesto);
	}

	@Operation(summary = "Agrega un movimiento al presupuesto en el Sistema", description = "Agrega un movimiento al presupuesto en el Sistema", tags = { "Movimientos de presupuesto" })
	@PostMapping("/MovimientoPresupuesto")
	public ResponseEntity<Object> createMovimPresup(@RequestBody MovimientosPresupuesto movimPresupuesto) {
		DefaultTransactionDefinition paramTransactionDefinition = new DefaultTransactionDefinition();
		TransactionStatus status = platformTransactionManager.getTransaction(paramTransactionDefinition);

		Presupuesto presup = presupuestoRepository.getElementById(movimPresupuesto.getIdPresup());
		double saldo=0, saldo_dist=0;

		try {

			// Si es por CT
			//   Validar saldo utilizado del CT y si el Saldo + Importe < Saldo actual: Continuar
			//   Si no Mandar la excepción

			// Si es por DEL
			//   Validar saldo utilizado de todos los CT de la DEL y si el Saldo + Importe < Saldo actual: Continuar
			//   Si no Mandar la excepción

			switch (presup.getTipoPresup().getClave()) {
				case "GI":
					if (presup.getCentroTrabajo() == null) {
						saldo = guardiaRepository.ObtenerSaldoUtilizado(presup.getDelegacion().getId_div_geografica(),
								presup.getAnio(), presup.getMes());
					} else {
						saldo = guardiaRepository.ObtenerSaldoUtilizado_ct(0, presup.getCentroTrabajo().getClave(),
								presup.getAnio(), presup.getMes());
					}
					break;
				case "GE":
					if (presup.getCentroTrabajo() == null) {
						saldo = guardiaRepository.ObtenerSaldoUtilizadoExt(presup.getDelegacion().getId_div_geografica(),
								presup.getAnio(), presup.getMes());
					} else {
						saldo = guardiaRepository.ObtenerSaldoUtilizadoExt_ct(0, presup.getCentroTrabajo().getClave(),
								presup.getAnio(), presup.getMes());
					}
					break;
				case "SI":
					if (presup.getCentroTrabajo() == null) {
						saldo = suplenciaRepository.ObtenerSaldoUtilizado(presup.getDelegacion().getId_div_geografica(),
								presup.getAnio(), presup.getMes());
					} else {
						saldo = suplenciaRepository.ObtenerSaldoUtilizado_ct(0, presup.getCentroTrabajo().getClave(),
								presup.getAnio(), presup.getMes());
					}
					break;
				case "SE":
					if (presup.getCentroTrabajo() == null) {
						saldo = suplenciaRepository.ObtenerSaldoUtilizadoExt(presup.getDelegacion().getId_div_geografica(),
								presup.getAnio(), presup.getMes());
					} else {
						saldo = suplenciaRepository.ObtenerSaldoUtilizadoExt_ct(0, presup.getCentroTrabajo().getClave(),
								presup.getAnio(), presup.getMes());
					}
					break;
				default:
					platformTransactionManager.rollback(status);
					return ResponseHandler.generateResponse("No se identificó el tipo de presupuesto del movimiento indicado", HttpStatus.INTERNAL_SERVER_ERROR, null);
			}

			if ( presup.getSaldo() + movimPresupuesto.getImporte() < saldo ) {
				platformTransactionManager.rollback(status);
				return ResponseHandler.generateResponse("No se puede realizar la modificación solicitada, debido a que al realizar la modificación se afectan recursos comprometidos para pago ($ " + saldo + "). Sí es necesario, elimine registros ya capturados.", HttpStatus.INTERNAL_SERVER_ERROR, null);
			}

			if (presup.getCentroTrabajo() == null) {
				saldo_dist = presupuestoRepository.getSaldoDistribuido(presup.getAnio(), presup.getMes(),
						presup.getDelegacion().getId_div_geografica(), presup.getTipoPresup().getClave());
	
				if ( presup.getSaldo() + movimPresupuesto.getImporte() < saldo_dist ) {
					platformTransactionManager.rollback(status);
					return ResponseHandler.generateResponse("No se puede realizar la modificación solicitada, debido a que ya se han distribuido $ " + saldo_dist + ", y quedaria un importe de $ " + ((presup.getSaldo() + movimPresupuesto.getImporte()) - saldo_dist) + ". Sí es necesario, modifique la distribución realizada.", HttpStatus.INTERNAL_SERVER_ERROR, null);
				}
			}

			presupuestoRepository.update(movimPresupuesto.getIdPresup(), movimPresupuesto.getImporte());
			movPresupuestoRepository.save(new MovimientosPresupuesto(movimPresupuesto.getIdPresup(), movimPresupuesto.getImporte(), movimPresupuesto.getComentarios(), movimPresupuesto.getTipMovPresup().getId()));
			platformTransactionManager.commit(status);
			return ResponseHandler.generateResponse("El movimiento ha sido creado de manera exitosa", HttpStatus.OK, null);
		} catch (Exception e) {
			platformTransactionManager.rollback(status);
			return ResponseHandler.generateResponse("Error al agregar nuevo movimiento al presupuesto en el Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Modifica información de un movimiento del presupuesto en el Sistema", description = "Modifica información de un movimiento del presupuesto en el Sistema", tags = { "Movimientos de presupuesto" })
	@PutMapping("/MovimientoPresupuesto/{id}")
	public ResponseEntity<Object> updateTipMovPresup(@PathVariable("id") Integer id, @RequestBody MovimientosPresupuesto movimPresupuesto) {
		MovimientosPresupuesto _movim = movPresupuestoRepository.findById(id);

		if (_movim != null) {
			_movim.setComentarios(movimPresupuesto.getComentarios());
			movPresupuestoRepository.update(_movim);
			return ResponseHandler.generateResponse("El tipo de movimiento ha sido modificado de manera exitosa", HttpStatus.OK, null);
		} else {
			return ResponseHandler.generateResponse("No se pudo encontrar el tipo de movimiento ha sido con id=" + id, HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Obtiene información de un movimiento del presupuesto en el Sistema", description = "Obtiene información de un movimiento del presupuesto en el Sistema", tags = { "Movimientos de presupuesto" })
	@GetMapping("/MovimientoPresupuesto/{id}")
	public ResponseEntity<Object> getMovimPresupById(
			@Parameter(description = "ID del tipo de movimiento del que se desea obtener la información", required = true) @PathVariable("id") Integer id) {

		MovimientosPresupuesto MovimPresupuesto = movPresupuestoRepository.findById(id);

		if (MovimPresupuesto != null) {
			return ResponseHandler.generateResponse("Se obtuvo la información de un movimiento del presupuesto de manera exitosa", HttpStatus.OK, MovimPresupuesto);
		} else {
			return ResponseHandler.generateResponse("Error al obtener información de un movimiento del presupuesto en el Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	/* Tipos de Presupuesto */

	@Operation(summary = "Agregar un tipo de presupuesto en el Sistema", description = "Agregar un tipo de presupuesto en el Sistema", tags = { "Tipos de presupuesto" })
	@PostMapping("/TipoPresup")
	public ResponseEntity<Object> createTipoPresup(@RequestBody TiposPresupuesto tipoPresupuesto) {
		try {
			tiposPresupuestoRepository.save(new TiposPresupuesto(tipoPresupuesto.getClave(), tipoPresupuesto.getDescripcion()));
			return ResponseHandler.generateResponse("El tipo de movimiento ha sido creado de manera exitosa", HttpStatus.OK, null);
		} catch (Exception e) {
			return ResponseHandler.generateResponse("Error al agregar nuevo elemento de tipos de presupuesto al Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Actualiza la información de un tipo de presupuesto en el Sistema", description = "Actualiza la información de un tipo de presupuesto en el Sistema", tags = { "Tipos de presupuesto" })
	@PutMapping("/TipoPresup/{id}")
	public ResponseEntity<Object> updateTipoPresup(@PathVariable("id") Integer id, @RequestBody TiposPresupuesto TipoMovPresupuesto) {
		TiposPresupuesto _tipo = tiposPresupuestoRepository.findById(id);

		if (_tipo != null) {
			_tipo.setClave(TipoMovPresupuesto.getClave());
			_tipo.setDescripcion(TipoMovPresupuesto.getDescripcion());

			tiposPresupuestoRepository.update(_tipo);
			return ResponseHandler.generateResponse("El tipo de presupuesto ha sido modificado de manera exitosa", HttpStatus.OK, null);
		} else {
			return ResponseHandler.generateResponse("No se pudo encontrar el tipo de presupuesto con id=" + id, HttpStatus.NOT_FOUND, null);
		}
	}

	@Operation(summary = "Obtiene información del tipo de presupuesto en el Sistema", description = "Obtiene información del tipo de presupuesto en el Sistema", tags = { "Tipos de presupuesto" })
	@GetMapping("/TipoPresup")
	public ResponseEntity<Object> getTipoPresup(
			@Parameter(description = "Descripcion del tipo de presupuesto del que se desea obtener la información", required = false) @RequestParam(required = false) String desc) {
		try {
			List<TiposPresupuesto> tiposPresup = new ArrayList<TiposPresupuesto>();

			if (desc == null)
				tiposPresupuestoRepository.findAll().forEach(tiposPresup::add);
			else
				tiposPresupuestoRepository.findByDesc(desc).forEach(tiposPresup::add);

			if (tiposPresup.isEmpty()) {
				return ResponseHandler.generateResponse("No se pudo obtener la información del tipo de presupuesto en el Sistema", HttpStatus.NOT_FOUND, null);
			}

			return ResponseHandler.generateResponse("Se obtuvo la información del tipo de presupuesto en el Sistema", HttpStatus.OK, tiposPresup);

		} catch (Exception e) {
			return ResponseHandler.generateResponse("Error al obtener la información del tipo de presupuesto en el Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Obtiene información de un tipo de presupuesto del Sistema", description = "Obtiene información de un tipo de presupuesto del Sistema", tags = { "Tipos de presupuesto" })
	@GetMapping("/TipoPresup/{id}")
	public ResponseEntity<Object> getTipoPresupById(
			@Parameter(description = "ID del tipo de movimiento del que se desea obtener la información", required = true) @PathVariable("id") Integer id) {

		TiposPresupuesto TipoMovPresupuesto = tiposPresupuestoRepository.findById(id);

		if (TipoMovPresupuesto != null) {

			return ResponseHandler.generateResponse("Se obtuvo la información de un tipo de presupuesto en el Sistema", HttpStatus.OK, TipoMovPresupuesto);
		} else {

			return ResponseHandler.generateResponse("No se encontró el tipo de presupuesto en el Sistema", HttpStatus.NOT_FOUND, null);
		}
	}

	@Operation(summary = "Elimina información de un tipo de presupuesto en el Sistema", description = "Elimina información de un tipo de presupuesto en el Sistema", tags = { "Tipos de presupuesto" })
	@DeleteMapping("/TipoPresup/{id}")
	public ResponseEntity<Object> deleteTipoPresup(@PathVariable("id") Integer id) {
		try {
			int result = tiposPresupuestoRepository.deleteById(id);
			if (result == 0) {

				return ResponseHandler.generateResponse("No se pudo encontrar el tipo de movimiento con el ID =" + id, HttpStatus.NOT_FOUND, null);
			}

			return ResponseHandler.generateResponse("El el tipo de movimiento fué eliminado exitósamente.", HttpStatus.OK, null);
		} catch (Exception e) {

			return ResponseHandler.generateResponse("No se borró el tipo de movimiento.", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

}
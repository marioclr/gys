package gob.issste.gys.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import gob.issste.gys.model.MovimientosPresupuesto;
import gob.issste.gys.model.Presupuesto;
import gob.issste.gys.model.TipoMovPresupuesto;
import gob.issste.gys.model.TiposPresupuesto;
import gob.issste.gys.repository.IMovimientosPresupuestoRepository;
import gob.issste.gys.repository.IPresupuestoRepository;
import gob.issste.gys.repository.ITipoMovPresupuestoRepository;
import gob.issste.gys.repository.ITiposPresupuestoRepository;
import gob.issste.gys.response.ResponseHandler;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class PresupuestoController {

	Logger logger = LoggerFactory.getLogger(JdbcTemplateDemo01Application.class);

	@Autowired
	ITipoMovPresupuestoRepository tipMovPresupRepository;
	@Autowired
	IMovimientosPresupuestoRepository movPresupuestoRepository;
	@Autowired
	ITiposPresupuestoRepository tiposPresupuestoRepository;
	@Autowired
	IPresupuestoRepository presupuestoRepository;

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
			@Parameter(description = "Objeto de Presupuesto a crear en el Sistema") @RequestBody Presupuesto presupuesto, 
			 @Parameter(description = "Comentarios del registro de presupuesto.", required = false) @RequestParam(name = "comentarios", required = false, defaultValue = "false") String comentario) {

		DefaultTransactionDefinition paramTransactionDefinition = new DefaultTransactionDefinition();
		TransactionStatus status = platformTransactionManager.getTransaction(paramTransactionDefinition);
		int idPresup;

		try {

			if( presupuesto.getCentroTrabajo() == null ) {
				if(presupuestoRepository.existe_presupuesto(presupuesto)>0) {
					return ResponseHandler.generateResponse("Existe un registro de presupuesto en este mismo periodo", HttpStatus.INTERNAL_SERVER_ERROR, null);
				}
			} else {
				if(presupuestoRepository.existe_presupuesto_ct(presupuesto)>0) {
					return ResponseHandler.generateResponse("Existe un registro de presupuesto en este mismo periodo", HttpStatus.INTERNAL_SERVER_ERROR, null);
				}
				List<Presupuesto> presupuestos= presupuestoRepository.get_dynamic_regs(presupuesto.getDelegacion().getId_div_geografica(), 
						presupuesto.getTipoPresup().getId(), presupuesto.getAnio(), null, true);
				if (presupuestos == null || presupuestos.size() == 0)
					return ResponseHandler.generateResponse("No exixte presupuesto asignado a la Delegación para asignar al centro de trabajo indicado", HttpStatus.INTERNAL_SERVER_ERROR, null);

				double suma_dist_ct = presupuestoRepository.validaSumaPresupuestal(presupuesto);
				if( suma_dist_ct > presupuestos.get(0).getSaldo() ) {
					return ResponseHandler.generateResponse("No exixte presupuesto asignado a la Delegación para asignar al centro de trabajo indicado", HttpStatus.INTERNAL_SERVER_ERROR, null);
				}
			}

			if( presupuesto.getCentroTrabajo() == null ) {
				idPresup = presupuestoRepository.save(new Presupuesto(presupuesto.getAnio(), presupuesto.getDelegacion(), presupuesto.getCentroTrabajo(),
						presupuesto.getTipoPresup(), presupuesto.getSaldo()));				
			} else {				
				idPresup = presupuestoRepository.save_ct(new Presupuesto(presupuesto.getAnio(), presupuesto.getDelegacion(), presupuesto.getCentroTrabajo(),
						presupuesto.getTipoPresup(), presupuesto.getSaldo()));
			}
			int idMovPresup = movPresupuestoRepository.save(new MovimientosPresupuesto(idPresup, presupuesto.getSaldo(), comentario, 1));
			platformTransactionManager.commit(status);
			return ResponseHandler.generateResponse("El Presupuesto con ID " + idPresup + " ha sido creado de manera exitosa. Con número de movimiento: " + idMovPresup, HttpStatus.CREATED, null);
		} catch (Exception e) {
			platformTransactionManager.rollback(status);
			return ResponseHandler.generateResponse("Error al realizar el registro de presupuesto", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Consulta elementos de presupuesto del Sistema", description = "Consulta elementos de presupuesto del Sistema", tags = { "Presupuesto" })
	@GetMapping("/Presupuesto")
	//public ResponseEntity<List<Presupuesto>> getPresupuesto(
	public ResponseEntity<Object> getPresupuesto(
			@Parameter(description = "Bandera para indicar si se requiere incluir los movimientos presupuestales", required = true) @RequestParam(name = "movim", required = true, defaultValue = "false") boolean movim,
			@Parameter(description = "Bandera para indicar si se requiere incluir los movimientos presupuestales", required = true) @RequestParam(name = "solo_deleg", required = true, defaultValue = "true") boolean solo_deleg,
			@Parameter(description = "Parámetro opcional para indicar el anio del que se desea consultar el presupuesto", required = false) @RequestParam(required = false) Integer anio,
			@Parameter(description = "Parámetro opcional para indicar el ID de la delegación del que se desea consultar el presupuesto", required = false) @RequestParam(required = false) String idDelegacion,
			@Parameter(description = "Parámetro opcional para indicar el ID del Centro de trabajo del que se desea consultar el presupuesto", required = false) @RequestParam(required = false) String idCentTrab,
			@Parameter(description = "Parámetro opcional para indicar el ID del Tipo de presupuesto del que se desea consultar el presupuesto", required = false) @RequestParam(required = false) Integer idTipoPresup) {
		try {
			List<Presupuesto> presupuestos = new ArrayList<Presupuesto>();

			if ( (idDelegacion != null) || (idTipoPresup != null) || (anio != null) || (idCentTrab != null) ) {
				presupuestos= presupuestoRepository.get_dynamic_regs(idDelegacion, idTipoPresup, anio, idCentTrab, solo_deleg);
			} else {
				presupuestos = presupuestoRepository.findAll();
			}
			if (movim) {
				for (Presupuesto p:presupuestos)
					p.setMovimientos(movPresupuestoRepository.findByPresupuesto(p.getId()));
			}
			if (presupuestos.isEmpty()) {
				//return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				return ResponseHandler.generateResponse("No existen elementos de presupuesto en el Sistema", HttpStatus.NO_CONTENT, null);
			}

			//return new ResponseEntity<>(presupuestos, HttpStatus.OK);
			return ResponseHandler.generateResponse("Se obtienen los elementos de presupuesto del Sistema", HttpStatus.OK, presupuestos);
		} catch (Exception e) {
			//return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			return ResponseHandler.generateResponse("Error al consultar elementos de presupuesto del Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Consulta elementos de presupuesto del Sistema", description = "Consulta elementos de presupuesto del Sistema", tags = { "Presupuesto" })
	@GetMapping("/Presupuesto/user")
	//public ResponseEntity<List<Presupuesto>> getPresupuesto(
	public ResponseEntity<Object> getPresupuestoByUser(
			@Parameter(description = "Bandera para indicar si se requiere incluir los movimientos presupuestales", required = false) @RequestParam(name = "movim", required = false, defaultValue = "false") boolean movim, 
			@Parameter(description = "ID del usuario para consultar el presupuesto asignado a su delegación y centros de trabajo", required = false) @RequestParam(required = false) int id,
			@Parameter(description = "ID de la delegación para obtener el presupuesto asignado", required = false) @RequestParam(required = false) String idDeleg ) {
		try {
			List<Presupuesto> presupuestos = new ArrayList<Presupuesto>();

			if ( idDeleg == null ) {
				presupuestos = presupuestoRepository.findAllByUser(id);
			} else {
				presupuestos = presupuestoRepository.findAll();
			}
			if (movim) {
				for (Presupuesto p:presupuestos)
					p.setMovimientos(movPresupuestoRepository.findByPresupuesto(p.getId()));
			}
			if (presupuestos.isEmpty()) {
				//return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				return ResponseHandler.generateResponse("No existen elementos de presupuesto en el Sistema", HttpStatus.NO_CONTENT, null);
			}

			//return new ResponseEntity<>(presupuestos, HttpStatus.OK);
			return ResponseHandler.generateResponse("Se obtienen los elementos de presupuesto del Sistema", HttpStatus.OK, presupuestos);
		} catch (Exception e) {
			//return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			return ResponseHandler.generateResponse("Error al consultar elementos de presupuesto del Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	/* Tipos de Movimientos Presupuestales */

	@Operation(summary = "Agrega un nuevo elemento de tipos de presupuesto al Sistema", description = "Agrega un nuevo elemento de tipos de presupuesto al Sistema", tags = { "Tipos de movimiento de presupuesto" })
	@PostMapping("/TipoMovPresup")
	//public ResponseEntity<String> createTipMovPresup(@RequestBody TipoMovPresupuesto TipoMovPresupuesto) {
	public ResponseEntity<Object> createTipMovPresup(@RequestBody TipoMovPresupuesto TipoMovPresupuesto) {
		try {
			tipMovPresupRepository.save(new TipoMovPresupuesto(TipoMovPresupuesto.getClave(), TipoMovPresupuesto.getDescripcion()));
			//return new ResponseEntity<>("El tipo de movimiento ha sido creado de manera exitosa", HttpStatus.CREATED);
			return ResponseHandler.generateResponse("El tipo de movimiento ha sido creado de manera exitosa", HttpStatus.CREATED, null);
		} catch (Exception e) {
			//return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			return ResponseHandler.generateResponse("Error al agregar nuevo elemento de tipos de presupuesto al Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Modifica un elemento de tipo de presupuesto al Sistema", description = "Modifica un elemento de tipo de presupuesto al Sistema", tags = { "Tipos de movimiento de presupuesto" })
	@PutMapping("/TipoMovPresup/{id}")
	//public ResponseEntity<String> updateTipMovPresup(@PathVariable("id") Integer id, @RequestBody TipoMovPresupuesto TipoMovPresupuesto) {
	public ResponseEntity<Object> updateTipMovPresup(@PathVariable("id") Integer id, @RequestBody TipoMovPresupuesto TipoMovPresupuesto) {
		TipoMovPresupuesto _tipo = tipMovPresupRepository.findById(id);

		if (_tipo != null) {
			_tipo.setClave(TipoMovPresupuesto.getClave());
			_tipo.setDescripcion(TipoMovPresupuesto.getDescripcion());

			tipMovPresupRepository.update(_tipo);
			//return new ResponseEntity<>("El tipo de movimiento ha sido modificado de manera exitosa", HttpStatus.OK);
			return ResponseHandler.generateResponse("El tipo de movimiento ha sido modificado de manera exitosa", HttpStatus.OK, null);
		} else {
			//return new ResponseEntity<>("No se pudo encontrar el tipo de movimiento ha sido con id=" + id, HttpStatus.NOT_FOUND);
			return ResponseHandler.generateResponse("Error al agregar nuevo elemento de tipos de presupuesto al Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Consulta elementos de tipo de presupuesto al Sistema", description = "Consulta elementos de tipo de presupuesto al Sistema", tags = { "Tipos de movimiento de presupuesto" })
	@GetMapping("/TipoMovPresup")
	//public ResponseEntity<List<TipoMovPresupuesto>> getTipMovPresup(
	public ResponseEntity<Object> getTipMovPresup(
			@Parameter(description = "Descripcion del tipo de movimiento del que se desea obtener la información", required = false) @RequestParam(required = false) String desc) {
		try {
			List<TipoMovPresupuesto> opciones = new ArrayList<TipoMovPresupuesto>();

			if (desc == null)
				tipMovPresupRepository.findAll().forEach(opciones::add);
			else
				tipMovPresupRepository.findByDesc(desc).forEach(opciones::add);

			if (opciones.isEmpty()) {
				//return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				return ResponseHandler.generateResponse("No existen elementos del tipo de movimiento del que se desea obtener la información", HttpStatus.NO_CONTENT, null);
			}

			//return new ResponseEntity<>(opciones, HttpStatus.OK);
			return ResponseHandler.generateResponse("Existen elementos del tipo de movimiento del que se desea obtener la información", HttpStatus.OK, opciones);
		} catch (Exception e) {
			//return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			return ResponseHandler.generateResponse("Error al consultar elementos de tipo de presupuesto al Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Obtiene un elemento de tipo de presupuesto del Sistema", description = "Obtiene un elemento de tipo de presupuesto del Sistema", tags = { "Tipos de movimiento de presupuesto" })
	@GetMapping("/TipoMovPresup/{id}")
	//public ResponseEntity<TipoMovPresupuesto> getTipMovPresupById(
	public ResponseEntity<Object> getTipMovPresupById(
			@Parameter(description = "ID del tipo de movimiento del que se desea obtener la información", required = true) @PathVariable("id") Integer id) {

		TipoMovPresupuesto TipoMovPresupuesto = tipMovPresupRepository.findById(id);

		if (TipoMovPresupuesto != null) {
			//return new ResponseEntity<>(TipoMovPresupuesto, HttpStatus.OK);
			return ResponseHandler.generateResponse("Existe el elemento de tipo de movimiento de presupuesto del Sistema", HttpStatus.OK, TipoMovPresupuesto);
		} else {
			//return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return ResponseHandler.generateResponse("Error al obtener un elemento de tipo de movimiento de presupuesto del Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Elimina un elemento de tipo de presupuesto del Sistema", description = "Elimina un elemento de tipo de presupuesto del Sistema", tags = { "Tipos de movimiento de presupuesto" })
	@DeleteMapping("/TipoMovPresup/{id}")
	//public ResponseEntity<String> deleteTipMovPresup(@PathVariable("id") Integer id) {
	public ResponseEntity<Object> deleteTipMovPresup(@PathVariable("id") Integer id) {
		try {
			int result = tipMovPresupRepository.deleteById(id);
			if (result == 0) {
				//return new ResponseEntity<>("No se pudo encontrar el tipo de movimiento con el ID =" + id, HttpStatus.NOT_FOUND);
				return ResponseHandler.generateResponse("No se pudo encontrar el tipo de movimiento con el ID =" + id, HttpStatus.NO_CONTENT, null);
			}
			//return new ResponseEntity<>("El el tipo de movimiento fué eliminado exitósamente.", HttpStatus.OK);
			return ResponseHandler.generateResponse("Existe el elemento de tipo de movimiento de presupuesto del Sistema", HttpStatus.OK, null);
		} catch (Exception e) {
			//return new ResponseEntity<>("No se borró el tipo de movimiento.", HttpStatus.INTERNAL_SERVER_ERROR);
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
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			//return new ResponseEntity<>(movimientos, HttpStatus.OK);
			return ResponseHandler.generateResponse("Existen movimientos del presupuesto en el Sistema", HttpStatus.OK, movimientos);
		} catch (Exception e) {
			//return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			return ResponseHandler.generateResponse("Error al obtener movimientos del presupuesto en el Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}


	@Operation(summary = "Agrega un movimiento al presupuesto en el Sistema", description = "Agrega un movimiento al presupuesto en el Sistema", tags = { "Movimientos de presupuesto" })
	@PostMapping("/MovimientoPresupuesto")
	//public ResponseEntity<String> createMovimPresup(@RequestBody MovimientosPresupuesto movimPresupuesto) {
	public ResponseEntity<Object> createMovimPresup(@RequestBody MovimientosPresupuesto movimPresupuesto) {
		DefaultTransactionDefinition paramTransactionDefinition = new DefaultTransactionDefinition();
		TransactionStatus status = platformTransactionManager.getTransaction(paramTransactionDefinition);

		try {
			presupuestoRepository.update(movimPresupuesto.getIdPresup(), movimPresupuesto.getImporte());
			movPresupuestoRepository.save(new MovimientosPresupuesto(movimPresupuesto.getIdPresup(), movimPresupuesto.getImporte(), movimPresupuesto.getComentarios(), movimPresupuesto.getTipMovPresup().getId()));
			platformTransactionManager.commit(status);
			//return new ResponseEntity<>("El movimiento ha sido creado de manera exitosa", HttpStatus.CREATED);
			return ResponseHandler.generateResponse("El movimiento ha sido creado de manera exitosa", HttpStatus.CREATED, null);
		} catch (Exception e) {
			platformTransactionManager.rollback(status);
			//return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			return ResponseHandler.generateResponse("Error al agregar nuevo movimiento al presupuesto en el Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Modifica información de un movimiento del presupuesto en el Sistema", description = "Modifica información de un movimiento del presupuesto en el Sistema", tags = { "Movimientos de presupuesto" })
	@PutMapping("/MovimientoPresupuesto/{id}")
	//public ResponseEntity<String> updateTipMovPresup(@PathVariable("id") Integer id, @RequestBody MovimientosPresupuesto movimPresupuesto) {
	public ResponseEntity<Object> updateTipMovPresup(@PathVariable("id") Integer id, @RequestBody MovimientosPresupuesto movimPresupuesto) {
		MovimientosPresupuesto _movim = movPresupuestoRepository.findById(id);

		if (_movim != null) {
			_movim.setComentarios(movimPresupuesto.getComentarios());
			movPresupuestoRepository.update(_movim);
			//return new ResponseEntity<>("El tipo de movimiento ha sido modificado de manera exitosa", HttpStatus.OK);
			return ResponseHandler.generateResponse("El tipo de movimiento ha sido modificado de manera exitosa", HttpStatus.OK, null);
		} else {
			//return new ResponseEntity<>("No se pudo encontrar el tipo de movimiento ha sido con id=" + id, HttpStatus.NOT_FOUND);
			return ResponseHandler.generateResponse("No se pudo encontrar el tipo de movimiento ha sido con id=" + id, HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Obtiene información de un movimiento del presupuesto en el Sistema", description = "Obtiene información de un movimiento del presupuesto en el Sistema", tags = { "Movimientos de presupuesto" })
	@GetMapping("/MovimientoPresupuesto/{id}")
	//public ResponseEntity<MovimientosPresupuesto> getMovimPresupById(
	public ResponseEntity<Object> getMovimPresupById(
			@Parameter(description = "ID del tipo de movimiento del que se desea obtener la información", required = true) @PathVariable("id") Integer id) {

		MovimientosPresupuesto MovimPresupuesto = movPresupuestoRepository.findById(id);

		if (MovimPresupuesto != null) {
			//return new ResponseEntity<>(MovimPresupuesto, HttpStatus.OK);
			return ResponseHandler.generateResponse("Se obtuvo la información de un movimiento del presupuesto de manera exitosa", HttpStatus.OK, MovimPresupuesto);
		} else {
			//return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return ResponseHandler.generateResponse("Error al obtener información de un movimiento del presupuesto en el Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}


	/* Tipos de Presupuesto */

	@Operation(summary = "Agregar un tipo de presupuesto en el Sistema", description = "Agregar un tipo de presupuesto en el Sistema", tags = { "Tipos de presupuesto" })
	@PostMapping("/TipoPresup")
	//public ResponseEntity<String> createTipoPresup(@RequestBody TiposPresupuesto tipoPresupuesto) {
	public ResponseEntity<Object> createTipoPresup(@RequestBody TiposPresupuesto tipoPresupuesto) {
		try {
			tiposPresupuestoRepository.save(new TiposPresupuesto(tipoPresupuesto.getClave(), tipoPresupuesto.getDescripcion()));
			//return new ResponseEntity<>("El tipo de movimiento ha sido creado de manera exitosa", HttpStatus.CREATED);
			return ResponseHandler.generateResponse("El tipo de movimiento ha sido creado de manera exitosa", HttpStatus.CREATED, null);
		} catch (Exception e) {
			//return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			return ResponseHandler.generateResponse("Error al agregar nuevo elemento de tipos de presupuesto al Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}


	@Operation(summary = "Actualiza la información de un tipo de presupuesto en el Sistema", description = "Actualiza la información de un tipo de presupuesto en el Sistema", tags = { "Tipos de presupuesto" })
	@PutMapping("/TipoPresup/{id}")
	//public ResponseEntity<String> updateTipoPresup(@PathVariable("id") Integer id, @RequestBody TiposPresupuesto TipoMovPresupuesto) {
	public ResponseEntity<Object> updateTipoPresup(@PathVariable("id") Integer id, @RequestBody TiposPresupuesto TipoMovPresupuesto) {
		TiposPresupuesto _tipo = tiposPresupuestoRepository.findById(id);

		if (_tipo != null) {
			_tipo.setClave(TipoMovPresupuesto.getClave());
			_tipo.setDescripcion(TipoMovPresupuesto.getDescripcion());

			tiposPresupuestoRepository.update(_tipo);
			//return new ResponseEntity<>("El tipo de presupuesto ha sido modificado de manera exitosa", HttpStatus.OK);
			return ResponseHandler.generateResponse("El tipo de presupuesto ha sido modificado de manera exitosa", HttpStatus.OK, null);
		} else {
			//return new ResponseEntity<>("No se pudo encontrar el tipo de presupuesto con id=" + id, HttpStatus.NOT_FOUND);
			return ResponseHandler.generateResponse("No se pudo encontrar el tipo de presupuesto con id=" + id, HttpStatus.NOT_FOUND, null);
		}
	}


	@Operation(summary = "Obtiene información del tipo de presupuesto en el Sistema", description = "Obtiene información del tipo de presupuesto en el Sistema", tags = { "Tipos de presupuesto" })
	@GetMapping("/TipoPresup")
	//public ResponseEntity<List<TiposPresupuesto>> getTipoPresup(
	public ResponseEntity<Object> getTipoPresup(
			@Parameter(description = "Descripcion del tipo de presupuesto del que se desea obtener la información", required = false) @RequestParam(required = false) String desc) {
		try {
			List<TiposPresupuesto> tiposPresup = new ArrayList<TiposPresupuesto>();

			if (desc == null)
				tiposPresupuestoRepository.findAll().forEach(tiposPresup::add);
			else
				tiposPresupuestoRepository.findByDesc(desc).forEach(tiposPresup::add);

			if (tiposPresup.isEmpty()) {
				//return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				return ResponseHandler.generateResponse("No se pudo obtener la información del tipo de presupuesto en el Sistema", HttpStatus.NO_CONTENT, null);
			}

			//return new ResponseEntity<>(opciones, HttpStatus.OK);
			return ResponseHandler.generateResponse("Se obtuvo la información del tipo de presupuesto en el Sistema", HttpStatus.OK, tiposPresup);

		} catch (Exception e) {
			//return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			return ResponseHandler.generateResponse("Error al obtener la información del tipo de presupuesto en el Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}


	@Operation(summary = "Obtiene información de un tipo de presupuesto del Sistema", description = "Obtiene información de un tipo de presupuesto del Sistema", tags = { "Tipos de presupuesto" })
	@GetMapping("/TipoPresup/{id}")
	//public ResponseEntity<TiposPresupuesto> getTipoPresupById(
	public ResponseEntity<Object> getTipoPresupById(
			@Parameter(description = "ID del tipo de movimiento del que se desea obtener la información", required = true) @PathVariable("id") Integer id) {

		TiposPresupuesto TipoMovPresupuesto = tiposPresupuestoRepository.findById(id);

		if (TipoMovPresupuesto != null) {
			//return new ResponseEntity<>(TipoMovPresupuesto, HttpStatus.OK);
			return ResponseHandler.generateResponse("Se obtuvo la información de un tipo de presupuesto en el Sistema", HttpStatus.OK, TipoMovPresupuesto);
		} else {
			//return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return ResponseHandler.generateResponse("No se encontró el tipo de presupuesto en el Sistema", HttpStatus.NOT_FOUND, null);
		}
	}


	@Operation(summary = "Elimina información de un tipo de presupuesto en el Sistema", description = "Elimina información de un tipo de presupuesto en el Sistema", tags = { "Tipos de presupuesto" })
	@DeleteMapping("/TipoPresup/{id}")
	//public ResponseEntity<String> deleteTipoPresup(@PathVariable("id") Integer id) {
	public ResponseEntity<Object> deleteTipoPresup(@PathVariable("id") Integer id) {
		try {
			int result = tiposPresupuestoRepository.deleteById(id);
			if (result == 0) {
				//return new ResponseEntity<>("No se pudo encontrar el tipo de movimiento con el ID =" + id, HttpStatus.NO_CONTENT);
				return ResponseHandler.generateResponse("No se pudo encontrar el tipo de movimiento con el ID =" + id, HttpStatus.NO_CONTENT, null);
			}
			//return new ResponseEntity<>("El el tipo de movimiento fué eliminado exitósamente.", HttpStatus.OK);
			return ResponseHandler.generateResponse("El el tipo de movimiento fué eliminado exitósamente.", HttpStatus.OK, null);
		} catch (Exception e) {
			//return new ResponseEntity<>("No se borró el tipo de movimiento.", HttpStatus.INTERNAL_SERVER_ERROR);
			return ResponseHandler.generateResponse("No se borró el tipo de movimiento.", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

}
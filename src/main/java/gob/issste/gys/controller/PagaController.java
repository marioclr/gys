package gob.issste.gys.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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

import gob.issste.gys.model.Delegacion;
import gob.issste.gys.model.Paga;
import gob.issste.gys.model.Usuario;
import gob.issste.gys.repository.IPagaRepository;
import gob.issste.gys.repository.UsuarioRepository;
import gob.issste.gys.response.ResponseHandler;
import gob.issste.gys.service.PagaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

//@CrossOrigin(origins = "*")
@CrossOrigin(origins = {"https://sigysdev.issste.gob.mx:8443, https://sigys.issste.gob.mx:8443, http://localhost:4200"})
@RestController
@RequestMapping("/api")
public class PagaController {

	@Autowired
	IPagaRepository pagaRepository;
	@Autowired
	PagaService pagaService;
	@Autowired
	UsuarioRepository usuarioRepository;
	@Autowired
	private PlatformTransactionManager platformTransactionManager;

	@Operation(summary = "Crea registro de la fecha de control de pagos de GyS en el Sistema", description = "Crea registro de la fecha de control de pagos de GyS en el Sistema", tags = { "Control de fechas de pago" })
	@PostMapping("/Paga")
	public ResponseEntity<Object> createPaga(@RequestBody Paga paga) {
		DefaultTransactionDefinition paramTransactionDefinition = new DefaultTransactionDefinition();
		TransactionStatus status = platformTransactionManager.getTransaction(paramTransactionDefinition);

		try {

			try {

				if(pagaService.existe_fecha_en_paga(paga)) {
					platformTransactionManager.rollback(status);
					return ResponseHandler.generateResponse("Existen fechas posteriores a la fecha ya indicada", HttpStatus.INTERNAL_SERVER_ERROR, null);
				}
				if(pagaService.validaPagasAbiertasAlCrear(paga)) {
					platformTransactionManager.rollback(status);
					return ResponseHandler.generateResponse("Existen fechas de control del mismo tipo, que no han sido cerradas. Se sugiere realizar los registros en alguna de estas.", HttpStatus.INTERNAL_SERVER_ERROR, null);
				}
				if(pagaService.existe_fecha_en_isr(paga)) {
					platformTransactionManager.rollback(status);
					return ResponseHandler.generateResponse("La fecha que se desea crear esta integrada en los cálculos de ISR y procesos posteriores.", HttpStatus.INTERNAL_SERVER_ERROR, null);
				}
				if (pagaService.existe_anterior_sin_terminar(paga)) {
					platformTransactionManager.rollback(status);
					return ResponseHandler.generateResponse("Existe por lo menos una fecha anterior, sin concluir el proceso completo. Por lo que primero se tienen que terminar esos procesos.", HttpStatus.INTERNAL_SERVER_ERROR, null);
				}
				int idPaga = pagaRepository.save(paga);

				for(Delegacion deleg:paga.getDelegaciones()) {
					pagaRepository.saveDelegForFecha(idPaga, deleg.getId_div_geografica(), paga.getId_usuario());
				}

				platformTransactionManager.commit(status);
				return ResponseHandler.generateResponse("La fecha de control de pagos ha sido creado de manera exitosa con ID " + idPaga, HttpStatus.OK, null);
			} catch (Exception e) {

				platformTransactionManager.rollback(status);
				return ResponseHandler.generateResponse("Error al obtener la fecha de control de pagos del Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
			}
		} catch (Exception e) {
			platformTransactionManager.rollback(status);
			return ResponseHandler.generateResponse("Error al agregar un nuevo usuario al Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);

		}
	}

	@Operation(summary = "Actualiza la información de la fecha de control de pagos de GyS del Sistema", description = "Actualiza la información de la fecha de control de pagos de GyS del Sistema", tags = { "Control de fechas de pago" })
	@PutMapping("/Paga/{id}")
	public ResponseEntity<Object> updatePaga(@PathVariable("id") Integer id, @RequestBody Paga paga) {
		Paga _paga = pagaRepository.findById(id);

		if (_paga != null) {
			_paga.setDescripcion(paga.getDescripcion());
			_paga.setEstatus(paga.getEstatus());
			_paga.setFec_inicio(paga.getFec_inicio());
			_paga.setFec_fin(paga.getFec_fin());
			_paga.setAnio_ejercicio(paga.getAnio_ejercicio());
			_paga.setMes_ejercicio(paga.getMes_ejercicio());
			_paga.setQuincena(paga.getQuincena());
			_paga.setId_tipo_paga(paga.getId_tipo_paga());
			_paga.setIdnivelvisibilidad(paga.getIdnivelvisibilidad());
			_paga.setProgramas(paga.getProgramas());
			_paga.setId_usuario(paga.getId_usuario());

			pagaRepository.update(_paga);

			pagaRepository.removeDelegForFecha(id);
			for(Delegacion deleg:paga.getDelegaciones()) {
				pagaRepository.saveDelegForFecha(id, deleg.getId_div_geografica(), paga.getId_usuario());
			}

			switch (paga.getEstatus()) {

				case 2: // Cambio a estatus de abierta a cerrada 

					pagaRepository.BorraAuthGuardias(paga);
					pagaRepository.AuthGuardiasInt(paga);
					pagaRepository.AuthGuardiasExt(paga);

					pagaRepository.BorraAuthSuplencias(paga);
					pagaRepository.AuthSuplenciasInt(paga);
					pagaRepository.AuthSuplenciasExt(paga);

					break;

			}
			return ResponseHandler.generateResponse(
//					"La fecha de control de pagos de GyS ha sido modificado de manera exitosa",
					"La fecha control ha sido modificada correctamente",
					HttpStatus.OK,
					null);
		} else {

			return ResponseHandler.generateResponse("No se pudo encontrar fecha de control de pagos con id=" + id, HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Realiza la apertura de la fecha de control de pagos de GyS del Sistema", description = "Realiza la apertura de la fecha de control de pagos de GyS del Sistema", tags = { "Control de fechas de pago" })
	@PutMapping("/Paga/open/{id}")
	public ResponseEntity<Object> openPaga(@PathVariable("id") Integer id) {
		Paga _paga = pagaRepository.findById(id);

		if (pagaService.validaPagasAlAbrir(_paga)) {

			return ResponseHandler.generateResponse("No se puede abrir la fecha ya que existen fechas de control abiertas con estos mismos criterios", HttpStatus.NOT_FOUND, false);
		} else {

			return ResponseHandler.generateResponse("Se puede abrir la fecha, ya que no existen fechas de control abiertas con estos mismos criterios", HttpStatus.OK, true);
		}
	}

	@Operation(summary = "Actualiza la información de la fecha de control de pagos de GyS del Sistema", description = "Actualiza la información de la fecha de control de pagos de GyS del Sistema", tags = { "Control de fechas de pago" })
	@PutMapping("/Paga/close/{id}")
	public ResponseEntity<Object> closePaga(@PathVariable("id") Integer id) {
		Paga _paga = pagaRepository.findById(id);

		if (pagaService.validaPagasAlCerrar(_paga)) {

			return ResponseHandler.generateResponse("Existen fechas de control en este mismo periodo, que no han sido cerradas", HttpStatus.NOT_FOUND, false);
		} else {

			return ResponseHandler.generateResponse("No Existen fechas de control en este mismo periodo, que no han sido cerradas", HttpStatus.OK, true);
		}
	}

	@Operation(summary = "Actualiza la información de la fecha de control de pagos de GyS del Sistema", description = "Actualiza la información de la fecha de control de pagos de GyS del Sistema", tags = { "Control de fechas de pago" })
	@PutMapping("/Paga/validate/{id}")
	public ResponseEntity<Object> validatePaga(@PathVariable("id") Integer id) {
		Paga _paga = pagaRepository.findById(id);

		if (pagaService.validaPagasAlCerrar(_paga)) {

			return ResponseHandler.generateResponse("Existen fechas de control en este mismo periodo, que no han sido cerradas", HttpStatus.NOT_FOUND, false);
		} else {

			return ResponseHandler.generateResponse("No Existen fechas de control en este mismo periodo, que no han sido cerradas", HttpStatus.OK, true);
		}
	}

	@Operation(summary = "Obtiene información de fechas de control de pagos de GyS en el Sistema", description = "Obtiene información de fechas de control de pagos de GyS en el Sistema", tags = { "Control de fechas de pago" })
	@GetMapping("/Paga")
	public ResponseEntity<Object> getPagas(
			@Parameter(description = "Descripcion de la fecha de control de pagos de la que se desea obtener la información", required = false) @RequestParam(required = false) String desc) {
		try {
			List<Paga> pagas = new ArrayList<Paga>();

			if (desc == null)
				pagaRepository.findAll().forEach(pagas::add);
			else
				pagaRepository.findByDesc(desc).forEach(pagas::add);

			if (pagas.isEmpty()) {

				return ResponseHandler.generateResponse("No se pudo obtener la información de fechas de control de pagos en el Sistema", HttpStatus.NOT_FOUND, null);
			}

			return ResponseHandler.generateResponse("Se pudo obtener la información de fechas de control de pagos en el Sistema de manera exitosa", HttpStatus.OK, pagas);
		} catch (Exception e) {

			return ResponseHandler.generateResponse("Error al obtener la información de fechas de control de pagos en el Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Obtiene información de una fecha de control de pagos de GyS en el Sistema", description = "Obtiene información de una fecha de control de pagos de GyS en el Sistema", tags = { "Control de fechas de pago" })
	@GetMapping("/Paga/{id}")
	public ResponseEntity<Object> getPagaPorId(
			@Parameter(description = "ID de la fecha de control de la que se desea obtener la información", required = true) @PathVariable("id") Integer id) {

		Paga paga = pagaRepository.findById(id);

		if (paga != null) {

			paga.setDelegaciones(pagaRepository.getDelegForFecha(id));

			return ResponseHandler.generateResponse("Se pudo obtener la información de fecha de control de pagos en el Sistema de manera exitosa", HttpStatus.OK, paga);
		} else {

			return ResponseHandler.generateResponse("No se pudo obtener la información de fecha de control de pagos en el Sistema", HttpStatus.NOT_FOUND, null);
		}
	}

	@Operation(summary = "Elimina información de una fecha de control de pagos de GyS en el Sistema", description = "Elimina información de una fecha de control de pagos de GyS en el Sistema", tags = { "Control de fechas de pago" })
	@DeleteMapping("/Paga/{id}")
	public ResponseEntity<Object> deletePaga(@PathVariable("id") Integer id) {
		try {

			pagaRepository.removeDelegForFecha(id);
			int result2 = pagaRepository.deleteById(id);

			if (result2 == 0) {

				return ResponseHandler.generateResponse("No se pudo encontrar la fecha de control de pagos de GyS con el ID =" + id, HttpStatus.NOT_FOUND, null);
			}

			return ResponseHandler.generateResponse(
//					"La fecha de control de pagos de GyS fué eliminado exitósamente.",
					"La fecha control se eliminó correctamente",
					HttpStatus.OK,
					null);

		} catch (Exception e) {

			return ResponseHandler.generateResponse("Error al obtener la información de la fecha de control de pagos en el Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Actualiza la información de la fecha de control de pagos de GyS del Sistema", description = "Actualiza la información de la fecha de control de pagos de GyS del Sistema", tags = { "Control de fechas de pago" })
	@PutMapping("/Paga/activate/{id}")
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

			return ResponseHandler.generateResponse("La fecha de control de pagos de GyS ha sido modificado de manera exitosa", HttpStatus.OK, null);
		} else {

			return ResponseHandler.generateResponse("No se pudo encontrar la fecha de control de pagos de GyS con el ID =" + id, HttpStatus.NOT_FOUND, null);
		}
	}

	@Operation(summary = "Obtiene información de fechas de control de pagos de GyS activas en el Sistema", description = "Obtiene información de fechas de control de pagos de GyS activas en el Sistema", tags = { "Control de fechas de pago" })
	@GetMapping("/Paga/activas")
	public ResponseEntity<Object> getActivePagas(
			@Parameter(description = "Parámetro para indicar el ID del usuario para consultar las fechas de control de pagos", required = false) @RequestParam(required = false) Integer idUsuario ) {

		List<Paga> pagas;

		try {

			if (idUsuario != null) {
				Usuario usuario = usuarioRepository.findById(idUsuario);
				pagas = new ArrayList<>();

				if (usuario != null) {
					switch(usuario.getNivelVisibilidad().getIdNivelVisibilidad()) {

						case 1:
							pagas = pagaRepository.findActivePagas();
							break;

						case 2,3:
							pagas = pagaRepository.findActivePagasByUser(idUsuario);
							break;

					}

				} else {
					return ResponseHandler.generateResponse("No se pudo obtener la información del usuario indicado para consultar las fechas de control de pagos de GyS activas en el Sistema", HttpStatus.NOT_FOUND, null);
				}

			} else {				
				pagas = pagaRepository.findActivePagas();
			}

			if (pagas.isEmpty()) {

				return ResponseHandler.generateResponse("No exixten fechas de control de pagos de GyS abiertas, en el Sistema", HttpStatus.NOT_FOUND, null);
			}

			return ResponseHandler.generateResponse("Se obtuvo la información de fechas de control de pagos de GyS activas en el Sistema", HttpStatus.OK, pagas);
		} catch (Exception e) {

			return ResponseHandler.generateResponse("Error al obtener la información de fechas de control de pagos de GyS activas en el Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}	
	}

	@Operation(summary = "Obtiene información de fechas de control de pagos de GyS, filtradas por su estatus, en el Sistema", description = "Obtiene información de fechas de control de pagos de GyS, filtradas por su estatus, en el Sistema", tags = { "Control de fechas de pago" })
	@GetMapping("/Paga/byStatus")
	public ResponseEntity<Object> getPagas(
			@Parameter(description = "Parámetro para indicar el Año del ejercicio de las fechas de control de pagos", required = true) @RequestParam(required = true) Integer anio,
			@Parameter(description = "Parámetro para indicar el Mes del ejercicio de las fechas de control de pagos", required = true) @RequestParam(required = true) Integer mes,
			@Parameter(description = "Parámetro para indicar el Tipo de fecha de control (Diferente a fin de mes: 4 o Igual a fin de mes: 1) de las fechas de control de pagos", required = true) @RequestParam(required = true) Integer tipoFechaControl,
			@Parameter(description = "ID del Sstatus del que se desea obtener las fechas de control de pagos", required = true) @RequestParam(required = true) Integer status) {

		try {
			List<Paga> pagas = pagaRepository.findByStatus(anio, mes, tipoFechaControl, status);

			if (pagas.isEmpty()) {

				return ResponseHandler.generateResponse("No existen fechas de control de pagos de GyS con las condiciones indicadas", HttpStatus.NOT_FOUND, null);
			}

			return ResponseHandler.generateResponse("Se obtuvo la información de fechas de control de pagos de GyS activas en el Sistema", HttpStatus.OK, pagas);
		} catch (Exception e) {

			return ResponseHandler.generateResponse("Error al obtener la información de fechas de control de pagos de GyS activas en el Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}	
	}

	@Operation(summary = "Validar si una Fecha de control se encuantra en estatus de cerrada", description = "Validar si una Fecha de control se encuantra en estatus de cerrada", tags = { "Control de fechas de pago" })
	@PutMapping("/Paga/is_closed")
	public ResponseEntity<Object> validatePagaIsClosed(@Parameter(description = "Fecha de control que se valida si estatus esta cerrado", required = true) @RequestParam(required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecha) {

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String strQuincena = dateFormat.format(fecha);

		if (!pagaService.validaSigueAbierta(strQuincena)) {

			return ResponseHandler.generateResponse("La Fecha de control se encuantra en estatus de cerrada u otro estatus posterior", HttpStatus.NOT_FOUND, true);
		} else {

			return ResponseHandler.generateResponse("La Fecha de control no se encuantra en estatus de cerrada u otro estatus posterior", HttpStatus.OK, false);
		}
	}	

}
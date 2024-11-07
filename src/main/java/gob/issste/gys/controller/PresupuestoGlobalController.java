package gob.issste.gys.controller;

import java.util.ArrayList;
import java.util.List;

import gob.issste.gys.service.ParamsValidatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import gob.issste.gys.model.PresupuestoGlobal;
import gob.issste.gys.model.PresupuestoGlobalDesglozado;
import gob.issste.gys.repository.IPresupuestoGlobalRepository;
import gob.issste.gys.repository.IPresupuestoRepository;
import gob.issste.gys.response.ResponseHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

//@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class PresupuestoGlobalController {

	Logger logger = LoggerFactory.getLogger(JdbcTemplateDemo01Application.class);

//	@Autowired
//	UsuarioRepository usuarioRepository;

	@Autowired
	IPresupuestoGlobalRepository presGlobalRepository;
	@Autowired
	IPresupuestoRepository presupuestoRepository;

	@Operation(summary = "Agregar un elemento de Presupuesto global en el Sistema", description = "Agregar un elemento de Presupuesto global en el Sistema", tags = { "Presupuesto Global" })
	@PostMapping("/PresupuestoGlobal")
	public ResponseEntity<Object> createPresupuestoGlobal(@RequestBody PresupuestoGlobal presupuestoGlobal) {
		try {

			if(presGlobalRepository.existe_presupuesto(presupuestoGlobal)>0) {
				return ResponseHandler.generateResponse("Existe un registro de presupuesto en este mismo periodo", HttpStatus.INTERNAL_SERVER_ERROR, null);
			}

			presGlobalRepository.save(new PresupuestoGlobal(presupuestoGlobal.getAnio(), presupuestoGlobal.getDelegacion(), 
					presupuestoGlobal.getSaldo(), presupuestoGlobal.getComentarios(), presupuestoGlobal.getId_usuario()));

			return ResponseHandler.generateResponse("El elemento de Presupuesto global ha sido creado de manera exitosa", HttpStatus.OK, null);
		} catch (Exception e) {

			return ResponseHandler.generateResponse("Error al agregar nuevo elemento de Presupuesto global al Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Agregar lista de elementos de Presupuestos globales en el Sistema", description = "Agregar lista de elementos de Presupuestos globales en el Sistema", tags = { "Presupuesto Global" })
	@PostMapping("/PresupuestosGlobales")
	public ResponseEntity<Object> createPresupuestosGlobales(@RequestBody List<PresupuestoGlobal> presupuestosGlobales) {

		List<String> messages = new ArrayList<String>();

		try {
			for (PresupuestoGlobal presGlob : presupuestosGlobales) {
				try {

					if(presGlobalRepository.existe_presupuesto(presGlob) > 0) {
						messages.add("Existe un registro de presupuesto en este mismo periodo" + presGlob);
						continue;
					}

					presGlobalRepository.save(new PresupuestoGlobal(presGlob.getAnio(), presGlob.getDelegacion(), 
							presGlob.getSaldo(), presGlob.getComentarios(), presGlob.getId_usuario()));

					messages.add("El elemento de Presupuesto global ha sido creado de manera exitosa" + presGlob);

				} catch (Exception e) {

					messages.add("Error al agregar nuevo elemento de Presupuesto global al Sistema" + presGlob);
					continue;
				}
			}
			return ResponseHandler.generateResponse("El elemento de Presupuesto global ha sido creado de manera exitosa", HttpStatus.OK, messages);
		} catch (Exception e) {

			return ResponseHandler.generateResponse("Error al agregar nuevo elemento de Presupuesto global al Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Actualiza la información de un elemento de Presupuesto global en el Sistema", description = "Actualiza la información de un elemento de Presupuesto global en el Sistema", tags = { "Presupuesto Global" })
	@PutMapping("/PresupuestoGlobal/{id}")
	public ResponseEntity<Object> updatePresupuestoGlobal(@PathVariable("id") Integer id, @RequestBody PresupuestoGlobal presupuestoGlobal) {
		PresupuestoGlobal _presGlobal = presGlobalRepository.findById(id);

		if (_presGlobal != null) {
			_presGlobal.setSaldo(presupuestoGlobal.getSaldo());
			_presGlobal.setComentarios(presupuestoGlobal.getComentarios());
			_presGlobal.setId_usuario(presupuestoGlobal.getId_usuario());

			presGlobalRepository.update(_presGlobal);

			return ResponseHandler.generateResponse("El elemento de Presupuesto global ha sido modificado de manera exitosa", HttpStatus.OK, null);
		} else {

			return ResponseHandler.generateResponse("No se pudo encontrar el elemento de Presupuesto global con id=" + id, HttpStatus.NOT_FOUND, null);
		}
	}

	@Operation(summary = "Obtiene los elementos del Presupuesto global en el Sistema", description = "Obtiene los elementos del Presupuesto global en el Sistema", tags = { "Presupuesto Global" })
	@GetMapping("/PresupuestoGlobal")
	public ResponseEntity<Object> getPresupuestoGlobal(
			@Parameter(description = "Parámetro opcional para indicar el anio del que se desea consultar el presupuesto", required = false) @RequestParam(required = false) Integer anio,
			@Parameter(description = "Parámetro opcional para indicar el ID de la delegación del que se desea consultar el presupuesto", required = false) @RequestParam(required = false) String idDelegacion,
			@Parameter(description = "Frase o palagra incluida en los comentarios del que se desea obtener la información", required = false) @RequestParam(required = false) String coment,
			@Parameter(description = "Parámetro opcional para indicar sí se incluye el saldo desglozado por meses y tipos de presupuesto", required = true) @RequestParam(required = true, defaultValue = "false") boolean conDesgloce ) {
		try {

			List<PresupuestoGlobal> presupuestosGlobal = new ArrayList<PresupuestoGlobal>();
			final String regex = "^[0-9]{2}$";
			String message = "";
			List<String> params = new ArrayList<String>();
				if ( (idDelegacion != null) || (anio != null) || (coment != null) ) {
					if(idDelegacion != null){
						String[] parameters = new String[] {idDelegacion, coment};
						for(String param : parameters){
							if (param != null){
								params.add(param);
							}
						}
						ParamsValidatorService paramsValidatorService = new ParamsValidatorService();
						boolean regexValue = paramsValidatorService.regexValidation(regex, idDelegacion);
						boolean injection = paramsValidatorService.sqlInjectionObjectValidator(params);
						if(injection || !regexValue){
							if (injection){
								message = "Intento de inyeccion detectado";
							} else {
								message = "Valor rechazado por la expresion regular";
							}
							return ResponseHandler.generateResponse(message, HttpStatus.NOT_ACCEPTABLE, null);
						}
					}
					presupuestosGlobal = presGlobalRepository.get_dynamic_regs(idDelegacion, anio, coment);
				} else {
					presupuestosGlobal = presGlobalRepository.findAll();
				}


			if (presupuestosGlobal.isEmpty()) {

				return ResponseHandler.generateResponse("No se obtuvieron registros de Presupuesto global en el Sistema", HttpStatus.NOT_FOUND, null);
			}
			
			if (conDesgloce == true) {
				List<PresupuestoGlobalDesglozado> presupuestosGlobalDesglozado = new ArrayList<PresupuestoGlobalDesglozado>();
				for(PresupuestoGlobal p:presupuestosGlobal) {
					double saldo = presupuestoRepository.suma_presupuestos(anio, p.getDelegacion().getId_div_geografica());
					PresupuestoGlobalDesglozado pd = new PresupuestoGlobalDesglozado();
					pd.setPresupuestoGlobal(p);
					pd.setSaldoDesglozado(saldo);
					presupuestosGlobalDesglozado.add(pd);
				}

				return ResponseHandler.generateResponse("Se obtuvo la información de elementos de Presupuesto global en el Sistema", HttpStatus.OK, presupuestosGlobalDesglozado);

			} else {

				return ResponseHandler.generateResponse("Se obtuvo la información de elementos de Presupuesto global en el Sistema", HttpStatus.OK, presupuestosGlobal);

			}

		} catch (Exception e) {

			return ResponseHandler.generateResponse("Error al obtener la información de elementos de Presupuesto global en el Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Obtiene información de un elemento de Presupuesto global del Sistema", description = "Obtiene información de un elemento de Presupuesto global del Sistema", tags = { "Presupuesto Global" })
	@GetMapping("/PresupuestoGlobal/{id}")
	public ResponseEntity<Object> getPresupuestoGlobalById(
			@Parameter(description = "ID del elemento de Presupuesto global del que se desea obtener la información", required = true) @PathVariable("id") Integer id) {

		PresupuestoGlobal presupuestosGlobal = presGlobalRepository.findById(id);

		if (presupuestosGlobal != null) {

			return ResponseHandler.generateResponse("Se obtuvo la información de un elemento de Presupuesto global en el Sistema", HttpStatus.OK, presupuestosGlobal);
		} else {

			return ResponseHandler.generateResponse("No se encontró el elemento de Presupuesto global en el Sistema", HttpStatus.NOT_FOUND, null);
		}
	}

	@Operation(summary = "Elimina información de un elemento de Presupuesto global en el Sistema", description = "Elimina información de un elemento de Presupuesto global en el Sistema", tags = { "Presupuesto Global" })
	@DeleteMapping("/PresupuestoGlobal/{id}")
	public ResponseEntity<Object> deletePresupuestoGlobal(@PathVariable("id") Integer id) {
		try {
			PresupuestoGlobal presupuestosGlobal = presGlobalRepository.findById(id);
			double saldo = presupuestoRepository.suma_presupuestos(presupuestosGlobal.getAnio(),
					presupuestosGlobal.getDelegacion().getId_div_geografica());
			PresupuestoGlobalDesglozado pd = new PresupuestoGlobalDesglozado();
			pd.setPresupuestoGlobal(presupuestosGlobal);
			pd.setSaldoDesglozado(saldo);

			if(pd.getSaldoDesglozado() > 0.0){
				return ResponseHandler.generateResponse(
						"No se puede borrar un presupuesto con saldo desglozado.",
						HttpStatus.INTERNAL_SERVER_ERROR,
						null);
			} else {
				int result = presGlobalRepository.deleteById(id);
				return ResponseHandler.generateResponse(
						"El elemento de presupuesto global fué eliminado exitósamente.",
						HttpStatus.OK,
						null);
			}
		} catch (Exception e) {

			return ResponseHandler.generateResponse("No se borró el elemento de presupuesto global.", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}
}
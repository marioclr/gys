package gob.issste.gys.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gob.issste.gys.model.CifrasDeImpuestos;
import gob.issste.gys.model.DetalleCifrasDeImpuestos;
import gob.issste.gys.repository.IAdminRepository;
import gob.issste.gys.repository.IPagaRepository;
import gob.issste.gys.response.ResponseHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class AdminController {

	@Autowired
	IAdminRepository adminRepository;
	@Autowired
	IPagaRepository pagaRepository;

	@Operation(summary = "Realiza el proceso de cálculo de impuestos a guardias o suplencias", description = "Realiza el proceso de cálculo de impuestos a guardias o suplencias", tags = { "Admin" })
	@PostMapping("/calculo_isr")
	public ResponseEntity<Object> calculoISR(
			@Parameter(description = "Parámetro para indicar el Año del ejercicio para el cálculo de ISR", required = true) @RequestParam(required = true) Integer anio,
			@Parameter(description = "Parámetro para indicar el Mes del ejercicio para el cálculo de ISR", required = true) @RequestParam(required = true) Integer mes,
			@Parameter(description = "Parámetro para indicar el Tipo de fecha de control (Diferente a fin de mes: 4 o Igual a fin de mes: 1)", required = true) @RequestParam(required = true) Integer tipoFechaControl ) {

		List<CifrasDeImpuestos> cifras;

//		if (! tipoConcepto.equals(String.valueOf("GE")) && ! tipoConcepto.equals(String.valueOf("SE"))) {
//			return ResponseHandler.generateResponse("El tipo de concepto debe ser GE: Guardias externas o SE: Suplencias externas", HttpStatus.INTERNAL_SERVER_ERROR, null);
//		}
		try {

			adminRepository.elimina_cifras_impuesto(anio, mes, tipoFechaControl);

			if (tipoFechaControl == 4) {
				adminRepository.calcula_isr_non(anio, mes);
			} else {
				//adminRepository.calcula_isr_guardia_par(anio, mes);
				//adminRepository.calcula_isr_suplencia_par(anio, mes);
			}

			//Se cambia el estadus a las fechas a 4 (calculo ISR)

			cifras = adminRepository.consultaCifrasDeImpuestos(anio, mes, tipoFechaControl);
			pagaRepository.updateStatus(4, anio, mes, tipoFechaControl);

			return ResponseHandler.generateResponse("El cálculo de impuestos finalizó de manera exitósa.", HttpStatus.OK, cifras);
		} catch (EmptyResultDataAccessException e) {
			cifras = new ArrayList<CifrasDeImpuestos>();
			return ResponseHandler.generateResponse("No se generó calculo de impuestos con las condiciones seleccionadas", HttpStatus.NOT_FOUND, null);
		} catch (Exception e) {

			return ResponseHandler.generateResponse("Error al realizar el cálculo de impuestos", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Realiza el proceso de cálculo de impuestos a guardias o suplencias", description = "Realiza el proceso de cálculo de impuestos a guardias o suplencias", tags = { "Admin" })
	@PostMapping("/re_calculo_isr")
	public ResponseEntity<Object> re_calculoISR(
			@Parameter(description = "Parámetro para indicar el Año del ejercicio para el cálculo de ISR", required = true) @RequestParam(required = true) Integer anio,
			@Parameter(description = "Parámetro para indicar el Mes del ejercicio para el cálculo de ISR", required = true) @RequestParam(required = true) Integer mes,
			@Parameter(description = "Parámetro para indicar el Tipo de fecha de control (Diferente a fin de mes: 4 o Igual a fin de mes: 1)", required = true) @RequestParam(required = true) Integer tipoFechaControl,
			@Parameter(description = "Fecha mínima del cálculo de ISR a recalcular", required = true) @RequestParam(required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaMin,
			@Parameter(description = "Fecha máxima del cálculo de ISR a recalcular", required = true) @RequestParam(required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaMax) {

		List<CifrasDeImpuestos> cifras;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String strMinDate = dateFormat.format(fechaMin);
		String strMaxDate = dateFormat.format(fechaMax);

		try {

			adminRepository.elimina_cifras_impuesto_x_rec(anio, mes, tipoFechaControl, strMinDate, strMaxDate);

			if (tipoFechaControl == 4) {
				//adminRepository.re_calcula_isr_guardia_non(anio, mes, strMinDate, strMaxDate);
				//adminRepository.calcula_isr_suplencia_non(anio, mes);
				adminRepository.re_calcula_isr_non(anio, mes, strMinDate, strMaxDate);
			} else {
				adminRepository.calcula_isr_guardia_par(anio, mes);
				adminRepository.calcula_isr_suplencia_par(anio, mes);
			}

			cifras = adminRepository.consultaCifrasDeImpuestos(anio, mes, tipoFechaControl);
			pagaRepository.updateStatus(4, anio, mes, tipoFechaControl);

			return ResponseHandler.generateResponse("El cálculo de impuestos finalizó de manera exitósa.", HttpStatus.OK, cifras);
		} catch (EmptyResultDataAccessException e) {
			cifras = new ArrayList<CifrasDeImpuestos>();
			return ResponseHandler.generateResponse("No se generó calculo de impuestos con las condiciones seleccionadas", HttpStatus.NOT_FOUND, null);
		} catch (Exception e) {

			return ResponseHandler.generateResponse("Error al realizar el cálculo de impuestos", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Realiza el proceso de cálculo de impuestos a guardias o suplencias", description = "Realiza el proceso de cálculo de impuestos a guardias o suplencias", tags = { "Admin" })
	@PostMapping("/complementa_calculo_isr")
	public ResponseEntity<Object> complementa_calculoISR(
			@Parameter(description = "Parámetro para indicar el Año del ejercicio para el cálculo de ISR", required = true) @RequestParam(required = true) Integer anio,
			@Parameter(description = "Parámetro para indicar el Mes del ejercicio para el cálculo de ISR", required = true) @RequestParam(required = true) Integer mes,
			@Parameter(description = "Parámetro para indicar el Tipo de fecha de control (Diferente a fin de mes: 4 o Igual a fin de mes: 1)", required = true) @RequestParam(required = true) Integer tipoFechaControl,
			@Parameter(description = "Parámetro para indicar el ID ordinal a complementar en el cálculo de ISR", required = true) @RequestParam(required = true) Integer id_ordinal,
			@Parameter(description = "Fecha de pago a complementar en el cálculo de ISR a recalcular", required = true) @RequestParam(required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaPago) {

		CifrasDeImpuestos cifra;
		List<CifrasDeImpuestos> cifras;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String strPagoDate = dateFormat.format(fechaPago);

		try {

			try {
				cifra = adminRepository.consultaCifrasDeImpuestosByOrdinal(anio, mes, tipoFechaControl, id_ordinal);
			} catch (Exception e) {
				return ResponseHandler.generateResponse("No se pudo obtener el cálculo previo de ISR para obtener complementar los nuevos registros", HttpStatus.NOT_FOUND, null);
			}

			Date fechaMax = dateFormat.parse(cifra.getFec_max());

			if( fechaPago.compareTo(fechaMax) <= 0) {
				return ResponseHandler.generateResponse("La fecha que quiere complementar al cálculo de ISR es menor o igual a las del cálculo, no es posible complementar sus registros", HttpStatus.NOT_FOUND, null);
			}

			adminRepository.elimina_cifras_impuesto_x_rec(anio, mes, tipoFechaControl, cifra.getFec_min(), cifra.getFec_max());

			if (tipoFechaControl == 4) {
				adminRepository.re_calcula_isr_non(anio, mes, cifra.getFec_min(), strPagoDate);
				//adminRepository.calcula_isr_suplencia_non(anio, mes);
			} else {
				//adminRepository.calcula_isr_guardia_par(anio, mes);
				//adminRepository.calcula_isr_suplencia_par(anio, mes);
			}

			cifras = adminRepository.consultaCifrasDeImpuestos(anio, mes, tipoFechaControl);
			pagaRepository.updateStatus(4, anio, mes, tipoFechaControl);

			return ResponseHandler.generateResponse("El cálculo de impuestos finalizó de manera exitósa.", HttpStatus.OK, cifras);
		} catch (EmptyResultDataAccessException e) {
			cifras = new ArrayList<CifrasDeImpuestos>();
			return ResponseHandler.generateResponse("No se generó calculo de impuestos con las condiciones seleccionadas", HttpStatus.NOT_FOUND, null);
		} catch (Exception e) {

			return ResponseHandler.generateResponse("Error al realizar el cálculo de impuestos", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Consulta las cifras de cálculo de impuestos a guardias o suplencias", description = "Consulta las cifras de cálculo de impuestos a guardias o suplencias", tags = { "Admin" })
	@GetMapping("/cifras_isr")
	public ResponseEntity<Object> getCifrasISR(
			@Parameter(description = "Parámetro para indicar el Año del ejercicio para el cálculo de ISR", required = true) @RequestParam(required = true) Integer anio,
			@Parameter(description = "Parámetro para indicar el Mes del ejercicio para el cálculo de ISR", required = true) @RequestParam(required = true) Integer mes,
			@Parameter(description = "Parámetro para indicar el Tipo de fecha de control (Diferente a fin de mes: 4 o Igual a fin de mes: 1)", required = true) @RequestParam(required = true) Integer tipoFechaControl ) {

		List<CifrasDeImpuestos> cifras = new ArrayList<CifrasDeImpuestos>();

		try {

			cifras = adminRepository.consultaCifrasDeImpuestos(anio, mes, tipoFechaControl);

			if (cifras.isEmpty()) {
				cifras = new ArrayList<CifrasDeImpuestos>();
				return ResponseHandler.generateResponse("No existe cálculo de impuestos con las condiciones seleccionadas", HttpStatus.NOT_FOUND, cifras);
			}

		} catch (EmptyResultDataAccessException e) {

			cifras = new ArrayList<CifrasDeImpuestos>();
			return ResponseHandler.generateResponse("No existe cálculo de impuestos con las condiciones seleccionadas", HttpStatus.NOT_FOUND, cifras);

		} catch (Exception e) {

			return ResponseHandler.generateResponse("Error al obtener las cifras de ISR del Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}

		return ResponseHandler.generateResponse("Se obtubieron las cifras de ISR de manera exitosa", HttpStatus.OK, cifras);
	}

	@Operation(summary = "Consulta las cifras de cálculo de impuestos a guardias o suplencias", description = "Consulta las cifras de cálculo de impuestos a guardias o suplencias", tags = { "Admin" })
	@GetMapping("/detalle_cifras_isr")
	public ResponseEntity<Object> getDetalleCifrasISR(
			@Parameter(description = "Parámetro para indicar el Año del ejercicio para obtener el detalle del ISR", required = false) @RequestParam(required = false) Integer anio,
			@Parameter(description = "Parámetro para indicar el Mes del ejercicio para obtener el detalle del ISR", required = false) @RequestParam(required = false) Integer mes,
			@Parameter(description = "Parámetro para indicar el Tipo de fecha de control (Diferente a fin de mes: 4 o Igual a fin de mes: 1)", required = false) @RequestParam(required = false) Integer tipoFechaControl,
			@Parameter(description = "Parámetro para indicar el ID ordinal para obtener el detalle del ISR", required = false) @RequestParam(required = false) Integer id_ordinal ) {

		List<DetalleCifrasDeImpuestos> cifras = new ArrayList<DetalleCifrasDeImpuestos>();

		try {

			cifras = adminRepository.getDetalleCifrasDeImpuestos( anio, mes, tipoFechaControl, id_ordinal );

			if (cifras.isEmpty()) {
				cifras = new ArrayList<DetalleCifrasDeImpuestos>();
				return ResponseHandler.generateResponse("No existe cálculo de impuestos con las condiciones seleccionadas", HttpStatus.NOT_FOUND, cifras);
			}

		} catch (Exception e) {

			return ResponseHandler.generateResponse("Error al obtener las cifras de ISR del Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}

		return ResponseHandler.generateResponse("Se obtubieron las cifras de ISR de manera exitosa", HttpStatus.OK, cifras);
	}

	@Operation(summary = "Realiza el proceso de generación de archivo de carga al SPEP para guardias o suplencias", description = "Realiza el proceso de generación de archivo de carga al SPEP para guardias o suplencias", tags = { "Admin" })
	@GetMapping("/spep")
	public ResponseEntity<Object> archivoSPEP(
			@Parameter(description = "Fecha de control para el cálculo de ISR de la guardia", required = true) @RequestParam(required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaControl,
			@Parameter(description = "Parámetro para indicar el tipo de concepto (Guardias externas: GE o Suplencias externas: SE)", required = true) @RequestParam(required = true) String tipoConcepto ) {

		List<String> layout;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String strDate = dateFormat.format(fechaControl);

		try {

			layout = adminRepository.consultaLayoutSPEP(strDate, tipoConcepto);
			//pagaRepository.updateStatus(4);

			return ResponseHandler.generateResponse("Generación de archivo de carga al SPEP para guardias o suplencias de manera exitósa", HttpStatus.OK, layout);

		} catch (Exception e) {

			return ResponseHandler.generateResponse("Error al obtener la fecha de control de pagos del Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Realiza el proceso de generación de archivo de timbrado para guardias o suplencias", description = "Realiza el proceso de generación de archivo de timbrado para guardias o suplencias", tags = { "Admin" })
	@GetMapping("/timbrado")
	public ResponseEntity<Object> archivoTimbrado(
			@Parameter(description = "Fecha de control para el cálculo de ISR de la guardia", required = true) @RequestParam(required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaControl,
			@Parameter(description = "Parámetro para indicar el tipo de concepto (Guardias externas: GE o Suplencias externas: SE)", required = true) @RequestParam(required = true) String tipoConcepto ) {

		try {

			return ResponseHandler.generateResponse("La fecha de control de pagos ha sido creado de manera exitosa con ID ", HttpStatus.OK, null);
		} catch (Exception e) {

			return ResponseHandler.generateResponse("Error al obtener la fecha de control de pagos del Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

}
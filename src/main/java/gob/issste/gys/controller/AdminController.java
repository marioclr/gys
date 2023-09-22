package gob.issste.gys.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gob.issste.gys.model.CifrasImpuesto;
import gob.issste.gys.repository.IAdminRepository;
import gob.issste.gys.response.ResponseHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class AdminController {

	@Autowired
	IAdminRepository adminRepository;

	@Operation(summary = "Realiza el proceso de cálculo de impuestos a guardias o suplencias", description = "Realiza el proceso de cálculo de impuestos a guardias o suplencias", tags = { "Admin" })
	@PostMapping("/isr")
	public ResponseEntity<Object> calculaISR(
			@Parameter(description = "Fecha de control para el cálculo de ISR de la guardia", required = true) @RequestParam(required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaControl,
			@Parameter(description = "Parámetro para indicar el Tipo de concepto (Guardias externas: GE o Suplencias externas: SE)", required = true) @RequestParam(required = true) String tipoConcepto ) {

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String strDate = dateFormat.format(fechaControl);

		CifrasImpuesto cifras;

		if (! tipoConcepto.equals(String.valueOf("GE")) && ! tipoConcepto.equals(String.valueOf("SE"))) {
			return ResponseHandler.generateResponse("El tipo de concepto debe ser GE: Guardias externas o SE: Suplencias externas", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
		try {

			if (tipoConcepto.equals(String.valueOf("GE"))) {
				adminRepository.elimina_calculo_isr_guardia(strDate);
				adminRepository.calcula_isr_gua(strDate);
			} else if (tipoConcepto.equals(String.valueOf("SE"))) {
				adminRepository.elimina_calculo_isr_suplencia(strDate);
				adminRepository.calcula_isr_sup(strDate);
			}
			cifras = adminRepository.consultaCifras(strDate, tipoConcepto);
			return ResponseHandler.generateResponse("El cálculo de impuestos finalizó de manera exitósa.", HttpStatus.CREATED, cifras);
		} catch (Exception e) {

			return ResponseHandler.generateResponse("Error al realizar el cálculo de impuestos", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Consulta las cifras de cálculo de impuestos a guardias o suplencias", description = "Consulta las cifras de cálculo de impuestos a guardias o suplencias", tags = { "Admin" })
	@GetMapping("/isr")
	public ResponseEntity<Object> getCalculoISR(
			@Parameter(description = "Fecha de control para el cálculo de ISR de la guardia", required = true) @RequestParam(required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaControl,
			@Parameter(description = "Parámetro para indicar el tipo de concepto (Guardias externas: GE o Suplencias externas: SE)", required = true) @RequestParam(required = true) String tipoConcepto ) {

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String strDate = dateFormat.format(fechaControl);

		CifrasImpuesto cifras;

		if (! tipoConcepto.equals(String.valueOf("GE")) && ! tipoConcepto.equals(String.valueOf("SE"))) {
			return ResponseHandler.generateResponse("El tipo de concepto debe ser GE: Guardias externas o SE: Suplencias externas", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
		try {

			cifras = adminRepository.consultaCifras(strDate, tipoConcepto);

			if (cifras == null) {
				return ResponseHandler.generateResponse("No existe cálculo de impuestos con las condiciones seleccionadas", HttpStatus.NOT_FOUND, null);
			}

			return ResponseHandler.generateResponse("Se obtubieron las cifras de ISR de manera exitosa", HttpStatus.OK, cifras);

		} catch (Exception e) {

			return ResponseHandler.generateResponse("Error al obtener las cifras de ISR del Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
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

			return ResponseHandler.generateResponse("Generación de archivo de carga al SPEP para guardias o suplencias de manera exitósa", HttpStatus.CREATED, layout);

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

			return ResponseHandler.generateResponse("La fecha de control de pagos ha sido creado de manera exitosa con ID ", HttpStatus.CREATED, null);
		} catch (Exception e) {

			return ResponseHandler.generateResponse("Error al obtener la fecha de control de pagos del Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}
}

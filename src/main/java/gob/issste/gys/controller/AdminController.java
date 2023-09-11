package gob.issste.gys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gob.issste.gys.model.CifrasImpuesto;
import gob.issste.gys.repository.IAdminRepository;
import gob.issste.gys.response.ResponseHandler;
import io.swagger.v3.oas.annotations.Operation;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class AdminController {
	
	@Autowired
	IAdminRepository adminRepository;

	@Operation(summary = "Realiza el proceso de cálculo de impuestos a guardias o suplencias", description = "Realiza el proceso de cálculo de impuestos a guardias o suplencias", tags = { "Admin" })
	@PostMapping("/isr")
	public ResponseEntity<Object> calculaISR() {

		try {

			return ResponseHandler.generateResponse("La fecha de control de pagos ha sido creado de manera exitosa con ID ", HttpStatus.CREATED, null);
		} catch (Exception e) {

			return ResponseHandler.generateResponse("Error al obtener la fecha de control de pagos del Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Realiza el proceso de cálculo de impuestos a guardias o suplencias", description = "Realiza el proceso de cálculo de impuestos a guardias o suplencias", tags = { "Admin" })
	@GetMapping("/isr")
	public ResponseEntity<Object> getCalculoISR() {

		CifrasImpuesto cifras;
		try {

			cifras = adminRepository.consultaCifras("2023-05-15", "GE");

			return ResponseHandler.generateResponse("Se obtubieron las cifras de ISR de manera exitosa", HttpStatus.OK, cifras);

		} catch (Exception e) {

			return ResponseHandler.generateResponse("Error al obtener la fecha de control de pagos del Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Realiza el proceso de generación de archivo de carga al SPEP para guardias o suplencias", description = "Realiza el proceso de generación de archivo de carga al SPEP para guardias o suplencias", tags = { "Admin" })
	@PostMapping("/spep")
	public ResponseEntity<Object> archivoSPEP() {

		try {

			return ResponseHandler.generateResponse("La fecha de control de pagos ha sido creado de manera exitosa con ID ", HttpStatus.CREATED, null);
		} catch (Exception e) {

			return ResponseHandler.generateResponse("Error al obtener la fecha de control de pagos del Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Realiza el proceso de generación de archivo de timbrado para guardias o suplencias", description = "Realiza el proceso de generación de archivo de timbrado para guardias o suplencias", tags = { "Admin" })
	@PostMapping("/timbrado")
	public ResponseEntity<Object> archivoTimbrado() {

		try {

			return ResponseHandler.generateResponse("La fecha de control de pagos ha sido creado de manera exitosa con ID ", HttpStatus.CREATED, null);
		} catch (Exception e) {

			return ResponseHandler.generateResponse("Error al obtener la fecha de control de pagos del Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}
}

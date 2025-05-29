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
import gob.issste.gys.model.CifrasDeImpuestosPorRepresentacion;
import gob.issste.gys.model.DatosPensionAlimenticia;
import gob.issste.gys.model.DetalleCifrasDeImpuestos;
import gob.issste.gys.model.DetalleCifrasDeImpuestosConPA;
import gob.issste.gys.model.DetalleCifrasDeImpuestosPorRep;
import gob.issste.gys.model.LayoutSpep;
import gob.issste.gys.repository.IAdminRepository;
import gob.issste.gys.repository.IPagaRepository;
import gob.issste.gys.response.ResponseHandler;
import gob.issste.gys.service.PagaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

//@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class AdminController {

	@Autowired
	IAdminRepository adminRepository;
	@Autowired
	IPagaRepository pagaRepository;
	@Autowired
	PagaService pagaService;

	@Operation(summary = "Realiza el proceso de cálculo de impuestos a guardias y suplencias", description = "Realiza el proceso de cálculo de impuestos a guardias y suplencias", tags = { "Admin" })
	@PostMapping("/calculo_isr")
	public ResponseEntity<Object> calculoISR(
			@Parameter(description = "Parámetro para indicar el Año del ejercicio para el cálculo de ISR", required = true)
			@RequestParam(required = true) Integer anio,
			@Parameter(description = "Parámetro para indicar el Mes del ejercicio para el cálculo de ISR", required = true)
			@RequestParam(required = true) Integer mes,
			@Parameter(description = "Parámetro para indicar el Tipo de fecha de control (Diferente a fin de mes: 4 o Igual a fin de mes: 1)", required = true)
			@RequestParam(required = true) Integer tipoFechaControl,
			@Parameter(description = "Parámetro para indicar id de fecha control por delegacion", required = false)
			@RequestParam(required = false) Integer idFechaByDeleg,
			@Parameter(description = "Parámetro para indicar la of. de representacion", required = false)
			@RequestParam(required = false) String idDeleg
	) {

		List<CifrasDeImpuestos> cifras;
		try {
			if(idDeleg == null) {
				adminRepository.elimina_cifras_impuesto(anio, mes, tipoFechaControl);

				if (tipoFechaControl == 4) {
					adminRepository.calcula_isr_non(anio, mes);
				} else {
					adminRepository.calcula_isr_par(anio, mes);
					//adminRepository.calcula_isr_guardia_par(anio, mes);
					//adminRepository.calcula_isr_suplencia_par(anio, mes);
				}

				//Se cambia el estadus a las fechas a 4 (calculo ISR)

				cifras = adminRepository.consultaCifrasDeImpuestos(anio, mes, tipoFechaControl);
				pagaRepository.updateStatus(4, anio, mes, tipoFechaControl);
				List<String> representaciones = pagaRepository.listaDeRepresentacionesByEstatus(idFechaByDeleg,3);
				representaciones.forEach(delegacion -> {
					pagaRepository.changeEstatusByIdDeleg(idFechaByDeleg, delegacion, 4);
						}
				);
//				return ResponseHandler.generateResponse("El cálculo de impuesto se realizó correctamente", HttpStatus.OK, cifras);
			} else {
				adminRepository.elimina_cifras_impuesto_by_deleg(anio, mes, tipoFechaControl, idDeleg);

				if (tipoFechaControl == 4) {
					adminRepository.calcula_isr_non_by_deleg(anio, mes, idDeleg);
				} else {
					adminRepository.calcula_isr_par_by_deleg(anio, mes, idDeleg);
				}

				//Se cambia el estadus a las fechas a 4 (calculo ISR)
				cifras = adminRepository.consultaCifrasDeImpuestosByIdDeleg(anio, mes, tipoFechaControl, idDeleg);
				pagaRepository.changeEstatusByIdDeleg(idFechaByDeleg, idDeleg, 4);
//				return ResponseHandler.generateResponse("El cálculo de impuesto se realizó correctamente", HttpStatus.OK, cifras);
			}
			return ResponseHandler.generateResponse("El cálculo de impuesto se realizó correctamente", HttpStatus.OK, cifras);

		} catch (EmptyResultDataAccessException e) {
			return ResponseHandler.generateResponse("No se generó el calculo de impuesto con las condiciones especificadas", HttpStatus.NOT_FOUND, null);
		} catch (Exception e) {
			return ResponseHandler.generateResponse("Error al realizar el cálculo de impuesto", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Realiza el proceso de recálculo de impuestos a guardias y suplencias", description = "Realiza el proceso de recálculo de impuestos a guardias y suplencias", tags = { "Admin" })
	@PostMapping("/re_calculo_isr")
	public ResponseEntity<Object> re_calculoISR(
			@Parameter(description = "Parámetro para indicar el Año del ejercicio para el cálculo de ISR", required = true) @RequestParam(required = true) Integer anio,
			@Parameter(description = "Parámetro para indicar el Mes del ejercicio para el cálculo de ISR", required = true) @RequestParam(required = true) Integer mes,
			@Parameter(description = "Parámetro para indicar el Tipo de fecha de control (Diferente a fin de mes: 4 o Igual a fin de mes: 1)", required = true) @RequestParam(required = true) Integer tipoFechaControl,
			@Parameter(description = "Fecha mínima del cálculo de ISR a recalcular", required = true) @RequestParam(required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaMin,
			@Parameter(description = "Fecha máxima del cálculo de ISR a recalcular", required = true) @RequestParam(required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaMax,
			@Parameter(description = "Parámetro para indicar el ID ordinal a complementar en el cálculo de ISR", required = false) @RequestParam(required = false) Integer id_ordinal) {

		List<CifrasDeImpuestos> cifras;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String strMinDate = dateFormat.format(fechaMin);
		String strMaxDate = dateFormat.format(fechaMax);

		try {

			//adminRepository.elimina_cifras_impuesto_x_rec(anio, mes, tipoFechaControl, strMinDate, strMaxDate);
			if (id_ordinal != null)
				adminRepository.elimina_cifras_impuesto_x_ord(anio, mes, tipoFechaControl, id_ordinal);

			if (tipoFechaControl == 4) {
				if (id_ordinal != null)
					adminRepository.re_calcula_isr_ord_non(anio, mes, strMinDate, strMaxDate, id_ordinal);
				else
					adminRepository.re_calcula_isr_non(anio, mes, strMinDate, strMaxDate);
			} else {
				//adminRepository.calcula_isr_guardia_par(anio, mes);
				//adminRepository.calcula_isr_suplencia_par(anio, mes);

				if (id_ordinal != null)
					adminRepository.re_calcula_isr_ord_par(anio, mes, strMinDate, strMaxDate, id_ordinal);
				else
					adminRepository.re_calcula_isr_par(anio, mes, strMinDate, strMaxDate);
			}

			cifras = adminRepository.consultaCifrasDeImpuestos(anio, mes, tipoFechaControl);

			pagaRepository.updateStatus(4, anio, mes, tipoFechaControl, strMinDate, strMaxDate);

			return ResponseHandler.generateResponse("El cálculo de impuesto se realizó correctamente", HttpStatus.OK, cifras);
		} catch (EmptyResultDataAccessException e) {

			cifras = new ArrayList<CifrasDeImpuestos>();
			return ResponseHandler.generateResponse("No se generó el calculo de impuesto con las condiciones especificadas", HttpStatus.NOT_FOUND, null);
		} catch (Exception e) {

			return ResponseHandler.generateResponse("Error al realizar el cálculo de impuesto", HttpStatus.INTERNAL_SERVER_ERROR, null);
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
				return ResponseHandler.generateResponse("No se realizó el cálculo de impuesto de los nuevos registros complementados", HttpStatus.NOT_FOUND, null);
			}

			Date fechaMax = dateFormat.parse(cifra.getFec_max());

			if( fechaPago.compareTo(fechaMax) <= 0) {
				return ResponseHandler.generateResponse(
						"No se pudo realizar el cálculo de impuesto de los nuevos registros complementados debido a que la fecha es igual o menor",
						HttpStatus.NOT_FOUND,
						null
				);
			}

			if(pagaService.validaPagasAlComplementar(strPagoDate, anio, mes, tipoFechaControl, id_ordinal)) {
				return ResponseHandler.generateResponse("Esta fecha actualmente se encuentra calculada", HttpStatus.INTERNAL_SERVER_ERROR, null);
			}

			//adminRepository.elimina_cifras_impuesto_x_rec(anio, mes, tipoFechaControl, cifra.getFec_min(), cifra.getFec_max());
			adminRepository.elimina_cifras_impuesto_x_ord(anio, mes, tipoFechaControl, id_ordinal);

			if (tipoFechaControl == 4) {
				//adminRepository.re_calcula_isr_non(anio, mes, cifra.getFec_min(), strPagoDate);
				adminRepository.re_calcula_isr_ord_non(anio, mes, cifra.getFec_min(), strPagoDate, id_ordinal);

			} else {
				//adminRepository.calcula_isr_guardia_par(anio, mes);

			}

			cifras = adminRepository.consultaCifrasDeImpuestos(anio, mes, tipoFechaControl);

			pagaRepository.updateStatus(4, anio, mes, tipoFechaControl, cifra.getFec_min(), strPagoDate);

			return ResponseHandler.generateResponse("El cálculo de impuesto se realizó correctamente", HttpStatus.OK, cifras);
		} catch (EmptyResultDataAccessException e) {
			cifras = new ArrayList<CifrasDeImpuestos>();
			return ResponseHandler.generateResponse("No se generó el calculo de impuesto con las condiciones especificadas", HttpStatus.NOT_FOUND, null);
		} catch (Exception e) {

			return ResponseHandler.generateResponse("Error al realizar el cálculo de impuesto", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Consulta las cifras de cálculo de impuestos a guardias o suplencias", description = "Consulta las cifras de cálculo de impuestos a guardias o suplencias", tags = { "Admin" })
	@GetMapping("/cifras_isr")
	public ResponseEntity<Object> getCifrasISR(
			@Parameter(description = "Parámetro para indicar el Año del ejercicio para el cálculo de ISR", required = true)
			@RequestParam(required = true) Integer anio,
			@Parameter(description = "Parámetro para indicar el Mes del ejercicio para el cálculo de ISR", required = true)
			@RequestParam(required = true) Integer mes,
			@Parameter(description = "Parámetro para indicar el Tipo de fecha de control (Diferente a fin de mes: 4 o Igual a fin de mes: 1)", required = true)
			@RequestParam(required = true) Integer tipoFechaControl,
			@Parameter(description = "Parámetro para especificar delegacion)", required = false)
			@RequestParam(required = false) String idDeleg
	) {

		List<CifrasDeImpuestos> cifras = new ArrayList<CifrasDeImpuestos>();

		try {
			if(idDeleg == null) {
				cifras = adminRepository.consultaCifrasDeImpuestos(anio, mes, tipoFechaControl);
			} else {
				cifras = adminRepository.consultaCifrasDeImpuestosByIdDeleg(anio, mes, tipoFechaControl, idDeleg);
			}
			if (cifras.isEmpty()) {
				cifras = new ArrayList<CifrasDeImpuestos>();
				return ResponseHandler.generateResponse("No existe cálculo de impuestos", HttpStatus.NOT_FOUND, cifras);
			}

		} catch (EmptyResultDataAccessException e) {

			cifras = new ArrayList<CifrasDeImpuestos>();
			return ResponseHandler.generateResponse("No existe cálculo de impuestos", HttpStatus.NOT_FOUND, cifras);

		} catch (Exception e) {

			return ResponseHandler.generateResponse("Error al obtener las cifras del cálculo de impuesto", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}

		return ResponseHandler.generateResponse("Se obtubieron las cifras del cálculo de impuesto correctamente", HttpStatus.OK, cifras);
	}

	@Operation(summary = "Consulta las cifras de cálculo de impuestos a guardias o suplencias", description = "Consulta las cifras de cálculo de impuestos a guardias o suplencias", tags = { "Admin" })
	@GetMapping("/cifras_isr_represent")
	public ResponseEntity<Object> getCifrasISRXRepres(
			@Parameter(description = "Parámetro para indicar el Año del ejercicio para el cálculo de ISR", required = true) @RequestParam(required = true) Integer anio,
			@Parameter(description = "Parámetro para indicar el Mes del ejercicio para el cálculo de ISR", required = true) @RequestParam(required = true) Integer mes,
			@Parameter(description = "Parámetro para indicar el Tipo de fecha de control (Diferente a fin de mes: 4 o Igual a fin de mes: 1)", required = true) @RequestParam(required = true) Integer tipoFechaControl ) {

		List<CifrasDeImpuestosPorRepresentacion> cifrasPorRepresentacion = new ArrayList<CifrasDeImpuestosPorRepresentacion>();

		try {

			cifrasPorRepresentacion = adminRepository.consultaCifrasDeImpuestosPorRep(anio, mes, tipoFechaControl);

			if (cifrasPorRepresentacion.isEmpty()) {
				cifrasPorRepresentacion = new ArrayList<CifrasDeImpuestosPorRepresentacion>();
				return ResponseHandler.generateResponse("No existe cálculo de impuestos con las condiciones seleccionadas", HttpStatus.NOT_FOUND, cifrasPorRepresentacion);
			}

		} catch (EmptyResultDataAccessException e) {

			cifrasPorRepresentacion = new ArrayList<CifrasDeImpuestosPorRepresentacion>();
			return ResponseHandler.generateResponse("No existe cálculo de impuestos con las condiciones seleccionadas", HttpStatus.NOT_FOUND, cifrasPorRepresentacion);

		} catch (Exception e) {

			return ResponseHandler.generateResponse("Error al obtener las cifras de ISR del Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}

		return ResponseHandler.generateResponse("Se obtubieron las cifras de ISR de manera exitosa", HttpStatus.OK, cifrasPorRepresentacion);
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

	@Operation(summary = "Consulta las cifras de cálculo de impuestos a guardias o suplencias", description = "Consulta las cifras de cálculo de impuestos a guardias o suplencias", tags = { "Admin" })
	@GetMapping("/detalle_cifras_isr_rep")
	public ResponseEntity<Object> getDetalleCifrasISRXrep(
			@Parameter(description = "Parámetro para indicar el Año del ejercicio para obtener el detalle del ISR", required = false) @RequestParam(required = false) Integer anio,
			@Parameter(description = "Parámetro para indicar el Mes del ejercicio para obtener el detalle del ISR", required = false) @RequestParam(required = false) Integer mes,
			@Parameter(description = "Parámetro para indicar el Tipo de fecha de control (Diferente a fin de mes: 4 o Igual a fin de mes: 1)", required = false) @RequestParam(required = false) Integer tipoFechaControl,
			@Parameter(description = "Parámetro para indicar el ID ordinal para obtener el detalle del ISR", required = false) @RequestParam(required = false) Integer id_ordinal,
			@Parameter(description = "ID de la representación para obtener las adscripciones asociadas", required = false) @RequestParam(required = false) String idRepresentacion ) {

		List<DetalleCifrasDeImpuestosPorRep> cifras = new ArrayList<DetalleCifrasDeImpuestosPorRep>();

		try {

			cifras = adminRepository.getDetalleCifrasDeImpuestosXRep( anio, mes, tipoFechaControl, id_ordinal, idRepresentacion );

			if (cifras.isEmpty()) {
				cifras = new ArrayList<DetalleCifrasDeImpuestosPorRep>();
				return ResponseHandler.generateResponse("No existe cálculo de impuestos con las condiciones seleccionadas", HttpStatus.NOT_FOUND, cifras);
			}

		} catch (Exception e) {

			return ResponseHandler.generateResponse("Error al obtener las cifras de ISR del Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}

		return ResponseHandler.generateResponse("Se obtubieron las cifras de ISR de manera exitosa", HttpStatus.OK, cifras);
	}

	@Operation(summary = "Consulta las cifras de cálculo de impuestos a guardias o suplencias", description = "Consulta las cifras de cálculo de impuestos a guardias o suplencias", tags = { "Admin" })
	@GetMapping("/detalle_cifras_pa")
	public ResponseEntity<Object> getDetalleCifrasPA(
			@Parameter(description = "Parámetro para indicar el Año del ejercicio para obtener el detalle del ISR", required = false) @RequestParam(required = false) Integer anio,
			@Parameter(description = "Parámetro para indicar el Mes del ejercicio para obtener el detalle del ISR", required = false) @RequestParam(required = false) Integer mes,
			@Parameter(description = "Parámetro para indicar el Tipo de fecha de control (Diferente a fin de mes: 4 o Igual a fin de mes: 1)", required = false) @RequestParam(required = false) Integer tipoFechaControl,
			@Parameter(description = "Parámetro para indicar el ID ordinal para obtener el detalle del ISR", required = false) @RequestParam(required = false) Integer id_ordinal ) {

		List<DetalleCifrasDeImpuestosConPA> cifras = new ArrayList<DetalleCifrasDeImpuestosConPA>();

		try {

			cifras = adminRepository.getDetalleCifrasDeImpuestosPA( anio, mes, tipoFechaControl, id_ordinal );

			if (cifras.isEmpty()) {
				cifras = new ArrayList<DetalleCifrasDeImpuestosConPA>();
				return ResponseHandler.generateResponse("No existe cálculo de impuestos con las condiciones seleccionadas", HttpStatus.NOT_FOUND, cifras);
			}

		} catch (Exception e) {

			return ResponseHandler.generateResponse("Error al obtener las cifras de ISR del Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}

		return ResponseHandler.generateResponse("Se obtubieron las cifras de ISR de manera exitosa", HttpStatus.OK, cifras);
	}

//	@Operation(summary = "Realiza el proceso de generación de archivo de carga al SPEP para guardias o suplencias", description = "Realiza el proceso de generación de archivo de carga al SPEP para guardias o suplencias", tags = { "Admin" })
//	@GetMapping("/spep")
//	public ResponseEntity<Object> archivoSPEP(
//			@Parameter(description = "Fecha de control para el cálculo de ISR de la guardia", required = true) @RequestParam(required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaControl,
//			@Parameter(description = "Parámetro para indicar el tipo de concepto (Guardias externas: GE o Suplencias externas: SE)", required = true) @RequestParam(required = true) String tipoConcepto ) {
//
//		List<String> layout;
//		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//		String strDate = dateFormat.format(fechaControl);
//
//		try {
//
//			layout = adminRepository.consultaLayoutSPEP(strDate, tipoConcepto); 
//			//pagaRepository.updateStatus(4);
//
//			return ResponseHandler.generateResponse("Generación de archivo de carga al SPEP para guardias o suplencias de manera exitósa", HttpStatus.OK, layout);
//
//		} catch (Exception e) {
//
//			return ResponseHandler.generateResponse("Error al obtener la fecha de control de pagos del Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
//		}
//	}

	@Operation(summary = "Realiza el proceso de generación de archivo de carga al SPEP para guardias o suplencias", description = "Realiza el proceso de generación de archivo de carga al SPEP para guardias o suplencias", tags = { "Admin" })
	@GetMapping("/layout_spep")
	public ResponseEntity<Object> archivoSPEP(
			@Parameter(description = "Parámetro para indicar el Año del ejercicio para el cálculo de ISR", required = true) @RequestParam(required = true) Integer anio,
			@Parameter(description = "Parámetro para indicar el Mes del ejercicio para el cálculo de ISR", required = true) @RequestParam(required = true) Integer mes,
			@Parameter(description = "Parámetro para indicar el Tipo de fecha de control (Diferente a fin de mes: 4 o Igual a fin de mes: 1)", required = true) @RequestParam(required = true) Integer tipoFechaControl,
			@Parameter(description = "Parámetro para indicar el ID ordinal a complementar en el cálculo de ISR", required = true) @RequestParam(required = true) Integer id_ordinal ) {

		List<LayoutSpep> layout = new ArrayList<LayoutSpep>();
		//List<String> layoutStr = new ArrayList<String>();

		try {

			layout = adminRepository.consultaLayoutSPEP(anio, mes, tipoFechaControl, id_ordinal);
//			for (LayoutSpep reg : layout) {
//				layoutStr.add(reg.toString());
//			}

			if (layout.isEmpty()) {
				layout = new ArrayList<LayoutSpep>();
				return ResponseHandler.generateResponse("No existen registros con las condiciones seleccionadas", HttpStatus.NOT_FOUND, layout);
			}

		} catch (EmptyResultDataAccessException e) {

			layout = new ArrayList<LayoutSpep>();
			return ResponseHandler.generateResponse("No existe cálculo de impuestos con las condiciones seleccionadas", HttpStatus.NOT_FOUND, layout);

		} catch (Exception e) {

			return ResponseHandler.generateResponse("Error al obtener la fecha de control de pagos del Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}

		return ResponseHandler.generateResponse("Generación de archivo de carga al SPEP para guardias o suplencias de manera exitósa", HttpStatus.OK, layout);

	}

	@Operation(summary = "Realiza el proceso de generación de archivo de carga al SPEP para guardias o suplencias", description = "Realiza el proceso de generación de archivo de carga al SPEP para guardias o suplencias", tags = { "Admin" })
	@GetMapping("/layout_spep_rep")
	public ResponseEntity<Object> archivoSPEPxRep(
			@Parameter(description = "Parámetro para indicar el Año del ejercicio para el cálculo de ISR", required = true) @RequestParam(required = true) Integer anio,
			@Parameter(description = "Parámetro para indicar el Mes del ejercicio para el cálculo de ISR", required = true) @RequestParam(required = true) Integer mes,
			@Parameter(description = "Parámetro para indicar el Tipo de fecha de control (Diferente a fin de mes: 4 o Igual a fin de mes: 1)", required = true) @RequestParam(required = true) Integer tipoFechaControl,
			@Parameter(description = "Parámetro para indicar el ID ordinal a complementar en el cálculo de ISR", required = true) @RequestParam(required = true) Integer id_ordinal,
			@Parameter(description = "ID de la representación para obtener las adscripciones asociadas", required = true) @RequestParam(required = true) String idRepresentacion ) {

		List<LayoutSpep> layout;
		List<DatosPensionAlimenticia> listaDeDatosPA;

		try {

			layout = adminRepository.consultaLayoutSPEP_X_Rep(anio, mes, tipoFechaControl, id_ordinal, idRepresentacion);

			for (LayoutSpep reg : layout) {
				listaDeDatosPA = new ArrayList<DatosPensionAlimenticia>();
				if (reg.getSuma_pension()>0) {
					listaDeDatosPA = adminRepository.consultaDatosPA(anio, mes, tipoFechaControl, id_ordinal, reg.getRfc());
				}
				if (!listaDeDatosPA.isEmpty()) {
					for (DatosPensionAlimenticia dpa:listaDeDatosPA) {
						switch(dpa.getCons_benef()) {
							case "1":
									dpa.setPago_pension(reg.getPension_1());
								break;
							case "2":
									dpa.setPago_pension(reg.getPension_2());
								break;
							case "3":
									dpa.setPago_pension(reg.getPension_3());
								break;
							case "4":
									dpa.setPago_pension(reg.getPension_4());
								break;
						}
					}
					reg.setBeneficiarios(listaDeDatosPA);
				}
			}

			if (layout.isEmpty()) {
				layout = new ArrayList<LayoutSpep>();
				return ResponseHandler.generateResponse("No existen registros con las condiciones seleccionadas", HttpStatus.NOT_FOUND, layout);
			}

		} catch (EmptyResultDataAccessException e) {

			layout = new ArrayList<LayoutSpep>();
			return ResponseHandler.generateResponse("No existen registros con las condiciones seleccionadas", HttpStatus.NOT_FOUND, layout);

		} catch (Exception e) {

			return ResponseHandler.generateResponse("Error al obtener registros con las condiciones seleccionadas en el Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}

		return ResponseHandler.generateResponse("Generación de archivo de carga al SPEP para guardias o suplencias de manera exitósa", HttpStatus.OK, layout);
	}

	@Operation(summary = "Realiza el proceso de generación de archivo de timbrado para guardias o suplencias", description = "Realiza el proceso de generación de archivo de timbrado para guardias o suplencias", tags = { "Admin" })
	@GetMapping("/timbrado")
	public ResponseEntity<Object> archivoTimbrado(
			@Parameter(description = "Fecha de control para el cálculo de ISR de la guardia", required = true) @RequestParam(required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaControl,
			@Parameter(description = "Parámetro para indicar el tipo de concepto (Guardias externas: GE o Suplencias externas: SE)", required = true) @RequestParam(required = true) String tipoConcepto ) {

		try {

			return ResponseHandler.generateResponse("La fecha de control de pagos ha sido creado de manera exitosa con ID ", HttpStatus.OK, null);
		} catch (Exception e) {

			return ResponseHandler.generateResponse("Error al obtener registros con las condiciones seleccionadas en el Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	@Operation(summary = "Realiza el proceso de cálculo de pensión alimenticia para guardias o suplencias", description = "Realiza el proceso de generación de archivo de carga al SPEP para guardias o suplencias", tags = { "Admin" })
	@PostMapping("/pension")
	public ResponseEntity<Object> calculo_pa(
			@Parameter(description = "Parámetro para indicar el Año del ejercicio para el cálculo de ISR", required = true) @RequestParam(required = true) Integer anio,
			@Parameter(description = "Parámetro para indicar el Mes del ejercicio para el cálculo de ISR", required = true) @RequestParam(required = true) Integer mes,
			@Parameter(description = "Parámetro para indicar el Tipo de fecha de control (Diferente a fin de mes: 4 o Igual a fin de mes: 1)", required = true) @RequestParam(required = true) Integer tipoFechaControl,
			@Parameter(description = "Parámetro para indicar el ID ordinal a complementar en el cálculo de ISR", required = true) @RequestParam(required = true) Integer id_ordinal ) {

		try {

			int a = adminRepository.calculaPensionAlimenticia(anio, mes, tipoFechaControl, id_ordinal);

			return ResponseHandler.generateResponse("Ejecución del proceso de cálculo de pensión alimenticia para guardias o suplencias de manera exitósa", HttpStatus.OK, a);

		} catch (Exception e) {

			return ResponseHandler.generateResponse("Error del proceso de cálculo de pensión alimenticia para guardias o suplencias", HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

//	@Operation(summary = "Realiza el proceso de generación de archivo de carga al SPEP para guardias o suplencias", description = "Realiza el proceso de generación de archivo de carga al SPEP para guardias o suplencias", tags = { "Admin" })
//	@GetMapping("/datos_pa")
//	public ResponseEntity<Object> getDatosPA(
//			@Parameter(description = "Parámetro para indicar el Año del ejercicio para el cálculo de ISR", required = true) @RequestParam(required = true) Integer anio,
//			@Parameter(description = "Parámetro para indicar el Mes del ejercicio para el cálculo de ISR", required = true) @RequestParam(required = true) Integer mes,
//			@Parameter(description = "Parámetro para indicar el Tipo de fecha de control (Diferente a fin de mes: 4 o Igual a fin de mes: 1)", required = true) @RequestParam(required = true) Integer tipoFechaControl,
//			@Parameter(description = "Parámetro para indicar el ID ordinal a complementar en el cálculo de ISR", required = true) @RequestParam(required = true) Integer id_ordinal,
//			@Parameter(description = "ID de la representación para obtener las adscripciones asociadas", required = true) @RequestParam(required = true) String idRepresentacion ) {
//
//		List<LayoutSpep> layout;
//		List<DatosPensionAlimenticia> listaDeDatosPA;
//
//		try {
//
//			layout = adminRepository.consultaLayoutSPEP_X_Rep(anio, mes, tipoFechaControl, id_ordinal, idRepresentacion);
//
//			for (LayoutSpep reg : layout) {
//				listaDeDatosPA = new ArrayList<DatosPensionAlimenticia>();
//				if (reg.getSuma_pension()>0) {
//					listaDeDatosPA = adminRepository.consultaDatosPA(anio, mes, tipoFechaControl, id_ordinal, reg.getRfc());
//				}
//				if (!listaDeDatosPA.isEmpty()) {
//					reg.setBeneficiarios(listaDeDatosPA);
//				}
//			}
//
//			if (layout.isEmpty()) {
//				layout = new ArrayList<LayoutSpep>();
//				return ResponseHandler.generateResponse("No existen registros con las condiciones seleccionadas", HttpStatus.NOT_FOUND, layout);
//			}
//
//		} catch (EmptyResultDataAccessException e) {
//
//			layout = new ArrayList<LayoutSpep>();
//			return ResponseHandler.generateResponse("No existen registros con las condiciones seleccionadas", HttpStatus.NOT_FOUND, layout);
//
//		} catch (Exception e) {
//
//			return ResponseHandler.generateResponse("Error al obtener registros con las condiciones seleccionadas en el Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
//		}
//
//		return ResponseHandler.generateResponse("Generación de archivo de carga al SPEP para guardias o suplencias de manera exitósa", HttpStatus.OK, layout);
//	}

}
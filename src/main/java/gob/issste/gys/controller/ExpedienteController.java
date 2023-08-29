package gob.issste.gys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import gob.issste.gys.model.DocumentoDigital;
import gob.issste.gys.repository.IImagenBolsaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
@Tag(name = "Expediente", description = "API de administración del expediente digital.")
public class ExpedienteController {

	@Autowired
	IImagenBolsaRepository imagenBolsaRepository; 

	@Operation(summary = "Agrega un nuevo documento digital al Sistema", description = "Agrega un nuevo documento digital al Sistema", tags = { "Expediente" })
	@PostMapping("/expediente")
	public ResponseEntity<String> cargaFoto(
			@Parameter(description = "ID del elemento de la bolsa de trabajo", required = true) @RequestParam(required = true) Integer idBolsa, 
			@Parameter(description = "Archivo a agregar en el expediente digital", required = true) @RequestParam(required = true) MultipartFile archivo) {

		DocumentoDigital documento = new DocumentoDigital();

		try {
			if (archivo != null) {
				documento.setId_asociado(0);
				documento.setImagen(archivo.getBytes());
			}
			int idImgBolsa = imagenBolsaRepository.save(documento);
			return new ResponseEntity<>("El registro de la imaben ha sido guardado de manera exitosa, con ID = " + idImgBolsa, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Operation(summary = "Obtener un nuevo documento digital del Sistema mediante el ID", description = "Obtener una Opción del Sistema mediante el ID", tags = { "Expediente" })
	@GetMapping("/expediente/{id}")
	public ResponseEntity<?> verFoto(@PathVariable Integer id) {
		DocumentoDigital imgBolsa = imagenBolsaRepository.getElementById(id);
		
		Resource imagen = new ByteArrayResource(imgBolsa.getImagen());
		
		return ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_PDF)
				.body(imagen);
	}

	@Operation(summary = "Agrega un nuevo documento digital al Sistema", description = "Agrega un nuevo documento digital al Sistema", tags = { "Expediente" })
	@PostMapping("/expedientes")
	public ResponseEntity<String> cargarFoto(
			@Parameter(description = "Objeto de Usuario a crear en el Sistema") @RequestBody DocumentoDigital doc
			) {

		DocumentoDigital documento = new DocumentoDigital();

		try {
			if (documento != null) {
				
			}
			int idImgBolsa = imagenBolsaRepository.save(documento);
			return new ResponseEntity<>("El registro de la imaben ha sido guardado de manera exitosa, con ID = " + idImgBolsa, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}

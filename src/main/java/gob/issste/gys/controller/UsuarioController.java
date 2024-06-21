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
import gob.issste.gys.model.DatosAdscripcion;
import gob.issste.gys.model.Empleado;
import gob.issste.gys.model.NivelVisibilidad;
import gob.issste.gys.model.Opcion;
import gob.issste.gys.model.Perfil;
import gob.issste.gys.model.Usuario;
import gob.issste.gys.repository.IEmpleadoRepository;
import gob.issste.gys.repository.PerfilRepository;
import gob.issste.gys.repository.UsuarioRepository;
import gob.issste.gys.response.ResponseHandler;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
@Tag(name = "Usuario", description = "API de Usuario")
public class UsuarioController {

	Logger logger = LoggerFactory.getLogger(JdbcTemplateDemo01Application.class);

	@Autowired
	UsuarioRepository usuarioRepository;
	@Autowired
	PerfilRepository perfilRepository;
	@Autowired
	IEmpleadoRepository empleadoRepository;
	@Autowired
	private PlatformTransactionManager platformTransactionManager;

	@Operation(summary = "Agrega un nuevo usuario al Sistema", description = "Agrega un nuevo usuario al Sistema", tags = { "Usuario" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successful operation", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class)) }),
			@ApiResponse(responseCode = "405", description = "Invalid input")
	})
	@PostMapping("/usuarios")
	//public ResponseEntity<String> createUsuario(
	public ResponseEntity<Object> createUsuario(
			@Parameter(description = "Objeto de Usuario a crear en el Sistema") @RequestBody Usuario usuario) {

		System.out.println("Estos datos llegan: "
				+"1.- "+usuario.getClave() + "\n"
				+"2.- "+usuario.getPassword() + "\n"
				+"3.- "+usuario.getEmpleado().getId_empleado() + "\n"
				+"4.- "+usuario.getDelegacion().getId_div_geografica() + "\n"
				+"5.- "+usuario.getNivelVisibilidad().getIdNivelVisibilidad() + "\n"
				+"6.- "+usuario.getIdTipoUsuario() + "\n"
				+"7.- "+usuario.getSistema() + "\n"
				+"8.- "+usuario.getId_usuario() + "\n");
		DefaultTransactionDefinition paramTransactionDefinition = new DefaultTransactionDefinition();
		TransactionStatus status = platformTransactionManager.getTransaction(paramTransactionDefinition);

		try {
			int idUsuario = usuarioRepository.save(new Usuario(usuario.getClave(), usuario.getPassword(),
					usuario.getEmpleado(),
					usuario.getDelegacion(), usuario.getCentrosTrabajo(), usuario.getNivelVisibilidad(),
					usuario.getIdTipoUsuario(), usuario.getSistema(), usuario.getId_usuario()));
			for(Perfil p:usuario.getPerfiles()) {
				perfilRepository.addPerfilToUser(idUsuario, p.getIdPerfil());
				for(Opcion o:p.getOpciones()) {
					usuarioRepository.savePermissions(idUsuario, p.getIdPerfil(), o.getIdOpcion(), o.getIdNivelAcceso());
				}
			}
			for(DatosAdscripcion ct:usuario.getCentrosTrabajo()) {
				usuarioRepository.saveCentTrabForUsu(idUsuario, ct.getClave(), usuario.getId_usuario());
			}
			platformTransactionManager.commit(status);
			//return new ResponseEntity<>("El Usuario ha sido creado de manera exitosa", HttpStatus.OK);
			return ResponseHandler.generateResponse("El Usuario ha sido creado de manera exitosa", HttpStatus.OK, null);
			
		} catch (Exception e) {
			platformTransactionManager.rollback(status);
			//return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			return ResponseHandler.generateResponse("Error al agregar un nuevo usuario al Sistema", HttpStatus.INTERNAL_SERVER_ERROR, null);
			
		}
	}

	@Operation(summary = "Actualiza un usuario del Sistema", description = "Actualiza un usuario del Sistema", tags = { "Usuario" })
	@PutMapping("/usuarios/{id}")
	public ResponseEntity<Object> updateUsuario(
			@Parameter(description = "El ID del usuario a modificar.", required = true) @PathVariable("id") int id, 
			@Parameter(description = "Actualizar un Usuario existente del Sistema") @RequestBody Usuario usuario) {

		Usuario _usuario = usuarioRepository.findById(id);

		DefaultTransactionDefinition paramTransactionDefinition = new DefaultTransactionDefinition();
		TransactionStatus status = platformTransactionManager.getTransaction(paramTransactionDefinition);

		try {

			if (_usuario != null) {
				_usuario.setIdUsuario(id);
				_usuario.setClave(usuario.getClave());
				_usuario.setPassword(usuario.getPassword());
				_usuario.setEmpleado(usuario.getEmpleado());
				_usuario.setDelegacion(usuario.getDelegacion());
				_usuario.setId_usuario(usuario.getId_usuario());
				_usuario.setNivelVisibilidad(usuario.getNivelVisibilidad());
				_usuario.setIdTipoUsuario(usuario.getIdTipoUsuario());
				_usuario.setSistema(usuario.getSistema());
				usuarioRepository.update(_usuario);
				// Delete Permissions
				usuarioRepository.removePermissions(id);
				perfilRepository.removePerfilesToUser(id);
				usuarioRepository.removeCentTrabForUsu(id);
				// Add Permissions
				for(Perfil p:usuario.getPerfiles()) {
					perfilRepository.addPerfilToUser(id, p.getIdPerfil());
					for(Opcion o:p.getOpciones()) {
						usuarioRepository.savePermissions(id, p.getIdPerfil(), o.getIdOpcion(), o.getIdNivelAcceso());
					}
				}
				for(DatosAdscripcion ct:usuario.getCentrosTrabajo()) {
					usuarioRepository.saveCentTrabForUsu(id, ct.getClave(), usuario.getId_usuario());
				}
				platformTransactionManager.commit(status);
				return ResponseHandler.generateResponse("El Usuario ha sido modificado de manera exitosa", HttpStatus.OK, null);
				
			} else {
				return ResponseHandler.generateResponse("No se pudo encontrar el usuario con ID = " + id, HttpStatus.NOT_FOUND, null);
			}
		} catch (Exception e) {
			platformTransactionManager.rollback(status);
			return ResponseHandler.generateResponse("Error al actualizar un usuario del Sistema de manera exitosa", HttpStatus.INTERNAL_SERVER_ERROR, null);			
		}
	}

	@Operation(summary = "Obtiene todos los usuarios del Sistema", description = "Obtiene todos los usuarios del Sistema", tags = { "Usuario" })
	@GetMapping("/usuarios")
	//public ResponseEntity<List<Usuario>> getUsuarios(
	public ResponseEntity<Object> getUsuarios(
			@Parameter(description = "Cadena para obtener los usuarios en que coincida con su clave", required = false) @RequestParam(required = false) String clave,
			@Parameter(description = "Boolean para indicar si se incluyen los Perfiles", required = true) @RequestParam(value = "conPerfiles", required = true) Boolean conPerfiles) {

		try {
			List<Usuario> usuarios = new ArrayList<Usuario>();

			if (clave == null)
				usuarioRepository.findAll(conPerfiles).forEach(usuarios::add);
			else
				usuarioRepository.findByClave(clave, conPerfiles).forEach(usuarios::add);

			if (usuarios.isEmpty()) {
				//return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				return ResponseHandler.generateResponse("No se obtuvieron los usuarios del Sistema", HttpStatus.NOT_FOUND, null);
				
			}

			//return new ResponseEntity<>(usuarios, HttpStatus.OK);
			return ResponseHandler.generateResponse("Obtiene todos los usuarios del Sistema de manera exitosa", HttpStatus.OK, usuarios);
			
		} catch (Exception e) {
			//return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			return ResponseHandler.generateResponse("Error al obtener todos los usuarios del Sistema de manera exitosa", HttpStatus.INTERNAL_SERVER_ERROR, null);
			
		}
	}

	@Operation(summary = "Obtiene el usuario mediante el ID del Sistema", description = "Obtiene el usuario mediante el ID del Sistema", tags = { "Usuario" })
	@GetMapping("/usuarios/{id}")
	//public ResponseEntity<Usuario> getUsuarioById(
	public ResponseEntity<Object> getUsuarioById(
			@Parameter(description = "El ID del usuario consultado.", required = true) @PathVariable("id") int id, 
			@Parameter(description = "Boolean para indicar si se incluyen los Perfiles", required = true) @RequestParam(value = "conPerfiles", required = true) Boolean conPerfiles) {

		Usuario usuario = usuarioRepository.findById(id);

		if (usuario != null) {
			Empleado empleado = empleadoRepository.findById(usuario.getEmpleado().getId_empleado());
			usuario.setEmpleado(empleado);
			if (conPerfiles == true) {
				List<Perfil> perfiles = usuarioRepository.getPerfilesForUsuario(usuario);
				for (Perfil perfil : perfiles) {
					List<Opcion> opciones = perfilRepository.getOpcionesForPerfil(perfil);
					perfil.setOpciones(opciones);
				}
				usuario.setPerfiles(perfiles);
			}
			usuario.setCentrosTrabajo(usuarioRepository.getCentTrabForUsu(id));
			usuario = usuarioRepository.getPermissionsForUser(usuario);
			//return new ResponseEntity<>(usuario, HttpStatus.OK);
			return ResponseHandler.generateResponse("Se obtuvo el usuario mediante ID de manera exitosa", HttpStatus.OK, usuario);
			
		} else {
			//return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return ResponseHandler.generateResponse("Error al obtener el usuario mediante ID del Sistema", HttpStatus.NOT_FOUND, null);
			
		}
	}

	@Operation(summary = "Elimina el Usuarios con el ID indicado del Sistema", description = "Elimina el Usuarios con el ID indicado del Sistema", tags = { "Usuario" })
	@DeleteMapping("/usuarios/{id}")
	//public ResponseEntity<String> deleteUsuario(
	public ResponseEntity<Object> deleteUsuario(
			@Parameter(description = "El ID del usuario a eliminar", required = true) @PathVariable("id") int id) {

		try {
			int result = usuarioRepository.deleteById(id);
			if (result == 0) {
				//return new ResponseEntity<>("No se pudo encontrar el Usuario con el ID = " + id, HttpStatus.NOT_FOUND);
				return ResponseHandler.generateResponse("No se pudo encontrar el Usuario con el ID = " + id, HttpStatus.NOT_FOUND, null);
			}
			//return new ResponseEntity<>("El Usuario fué eliminada exitosamente", HttpStatus.OK);
			return ResponseHandler.generateResponse("El Usuario fué eliminada exitosamente", HttpStatus.OK, null);
			
		} catch (Exception e) {
			//return new ResponseEntity<>("No se borró el Usuario", HttpStatus.INTERNAL_SERVER_ERROR);
			return ResponseHandler.generateResponse("No se borró el Usuario", HttpStatus.INTERNAL_SERVER_ERROR, null);
			
		}
	}

	@Operation(summary = "Logea Usuario en el Sistema", tags = { "Usuario" })
//	@ApiResponses(value = {
//			@ApiResponse(responseCode = "200", headers = {
//					@Header(name = "X-Rate-Limit", description = "calls per hour allowed by the user", schema = @Schema(type = "integer", format = "int32")),
//					@Header(name = "X-Expires-After", description = "date in UTC when toekn expires", schema = @Schema(type = "string", format = "date-time")) },
//					description = "successful operation", content = @Content(schema = @Schema(implementation = Usuario.class))),
//			@ApiResponse(responseCode = "400", description = "Invalid username/password supplied", content = @Content) })
	@GetMapping(value = "/usuarios/login")//, produces = { "application/xml", "application/json" })
	//public ResponseEntity<Usuario> loginUser(
	public ResponseEntity<Object> loginUser(
			@Parameter(description = "Clave de Usuario a logear", required = true) @RequestParam(value = "clave", required = true) String clave,
			@Parameter(description = "Password de Usuario a logear", required = true) @RequestParam(value = "password", required = true) String password) {

		try {

			Usuario usuario = usuarioRepository.loginUser(clave, password);
			
			if (usuario == null) {
				//return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				return ResponseHandler.generateResponse("No se pudieron validar las credenciales del usuario", HttpStatus.NOT_FOUND, null);
				
			}
			usuario.setCentrosTrabajo(usuarioRepository.getCentTrabForUsu(usuario.getIdUsuario()));
			usuario = usuarioRepository.getPermissionsForUser(usuario);
			//return new ResponseEntity<>(usuario, HttpStatus.OK);
			return ResponseHandler.generateResponse("Logea Usuario en el Sistema de manera exitosa", HttpStatus.OK, usuario);
			
		} catch (Exception e) {
			//return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			return ResponseHandler.generateResponse("No se pudo crear la opcion de manera exitosa", HttpStatus.INTERNAL_SERVER_ERROR, null);
			
		}
		
	}

	@Operation(summary = "Obtiene los permisos del usuarios del Sistema", description = "Obtiene los permisos del usuarios del Sistema", tags = { "Usuario" })
	@GetMapping("/usuarios/permisos_usuario")
	//public ResponseEntity<Usuario> getPermisosDeUsuario(
	public ResponseEntity<Object> getPermisosDeUsuario(
			@Parameter(description = "ID del usuario para consultar sus permisos", required = true) @RequestParam(required = true) int id) {

		Usuario usuario = usuarioRepository.findById(id);

		if (usuario != null) {
			usuario.setCentrosTrabajo(usuarioRepository.getCentTrabForUsu(usuario.getIdUsuario()));
			usuario = usuarioRepository.getPermissionsForUser(usuario);
			//return new ResponseEntity<>(usuario, HttpStatus.OK);
			return ResponseHandler.generateResponse("Se obtienen los permisos del usuarios del Sistema", HttpStatus.OK, usuario);
			
		} else {
			//return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return ResponseHandler.generateResponse("Error al obtener los permisos del usuarios del Sistema", HttpStatus.NOT_FOUND, null);
			
		}
	}

	@Operation(summary = "Obtiene los niveles de visibilidad para los usuarios del Sistema", description = "Obtiene los niveles de visibilidad para los usuarios del Sistema", tags = { "Usuario" })
	@GetMapping("/usuarios/niveles_visibilidad")
	public ResponseEntity<Object> getNivelesVisibilidadDeUsuario() {

		List<NivelVisibilidad> niveles = usuarioRepository.getNivelVisibilidadUsuarios();

		if (niveles != null) {
			return ResponseHandler.generateResponse("Existen los niveles de visibilidad de los usuarios del Sistema", HttpStatus.OK, niveles);
		} else {
			return ResponseHandler.generateResponse("No existen los niveles de visibilidad de los usuarios del Sistema", HttpStatus.NOT_FOUND, null);
		}
	}

}
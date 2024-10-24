package gob.issste.gys.controller;

import gob.issste.gys.model.Login;
import gob.issste.gys.model.Opcion;
import gob.issste.gys.model.Perfil;
import gob.issste.gys.model.Usuario;
import gob.issste.gys.repository.PerfilRepository;
import gob.issste.gys.repository.UsuarioRepository;
import gob.issste.gys.response.ResponseHandler;
import gob.issste.gys.security.JWTAuthenticationConfig;
import gob.issste.gys.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class LoginController {
    @Autowired
    JWTAuthenticationConfig jwtAuthenticationConfig;
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    PerfilRepository perfilRepository;

    @Autowired
    private SecurityService securityService;
    @PostMapping("/login")
    public ResponseEntity<Object> login(
            @RequestBody Login login) {
        try {
            Usuario user = usuarioRepository.findByName(login.getClaveUser());
            boolean validate = securityService.getPwdValidation(login.getPwd(), user.getPassword());
            if (!validate){
                return  ResponseHandler.generateResponse(
                        "No autorizado, usuario y/o contraseña invalidos", HttpStatus.UNAUTHORIZED, null);
            } else {
                List<Perfil> perfiles = usuarioRepository.getPerfilesForUsuario(user);
                for (Perfil perfil : perfiles) {
                    List<Opcion> opciones = perfilRepository.getOpcionesForPerfil(perfil);
                    perfil.setOpciones(opciones);
                }
                user.setPerfiles(perfiles);
                Map<String, Object> map = new HashMap<String, Object>();
                Usuario constructedUser = new Usuario(
                        user.getIdUsuario(),
                        user.getClave(),
                        user.getDelegacion(),
                        user.getPerfiles(),
                        user.getCentrosTrabajo(),
                        user.getNivelVisibilidad(),
                        user.getIdTipoUsuario(),
                        user.getId_usuario()
                );
                map.put("info", constructedUser);
                String token = jwtAuthenticationConfig.getJWTToken(login.getClaveUser());
                map.put("auth", token);
                return ResponseHandler.generateResponse("Autorizado", HttpStatus.OK, map);
            }
        }catch (NullPointerException e) {
            return ResponseHandler.generateResponse(
                    "El usuario no existe en la base de datos", HttpStatus.NOT_FOUND, null);
        }
    }
}

package gob.issste.gys.controller;

import gob.issste.gys.model.Login;
import gob.issste.gys.model.Usuario;
import gob.issste.gys.repository.UsuarioRepository;
import gob.issste.gys.response.ResponseHandler;
import gob.issste.gys.security.JWTAuthenticationConfig;
import gob.issste.gys.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.sql.SQLException;
import java.util.*;
@RestController
public class LoginController {
    @Autowired
    JWTAuthenticationConfig jwtAuthenticationConfig;
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    private SecurityService securityService;
    @PostMapping("/login")
    public ResponseEntity<Object> login(
            @RequestBody Login login) {
        try {
            Usuario user = usuarioRepository.findByName(login.getClaveUser());

            if (Objects.isNull( user )){
                return  ResponseHandler.generateResponse(
                        "Usuario no encontrado", HttpStatus.NOT_FOUND, null);
            } else if(user.isActivo()) {
                boolean validate = securityService.getPwdValidation(login.getPwd(), user.getPassword());
                if (!validate){
                    System.out.println(user.getIntentos());
                    if(user.getIntentos() < 3 ) {
                        usuarioRepository.updateAttemps(user.getIntentos()+1, user.getIdUsuario());
                    } else if(user.getIntentos() == 3){
                        usuarioRepository.updateActive(false, user.getIdUsuario());
                        return  ResponseHandler.generateResponse(
                                "Usuario bloqueado, contacte a un administrador", HttpStatus.FORBIDDEN, null);
                    }
                    return  ResponseHandler.generateResponse(
                            "Usuario y/o contraseña incorrectos", HttpStatus.FORBIDDEN, null);
                } else {
                    user.setCentrosTrabajo(usuarioRepository.getCentTrabForUsu(user.getIdUsuario()));
                    user = usuarioRepository.getPermissionsForUser(user);
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
                    usuarioRepository.updateAttemps(0, user.getIdUsuario());
                    return ResponseHandler.generateResponse("Autorizado", HttpStatus.OK, map);
                }
            }else{
                return ResponseHandler.generateResponse("El usuario está bloqueado, contactar a un administrador", HttpStatus.FORBIDDEN, null);
            }
        }catch (NullPointerException e) {
            return ResponseHandler.generateResponse(
                    "Error al realizar inicio de sesión", HttpStatus.INTERNAL_SERVER_ERROR, null);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

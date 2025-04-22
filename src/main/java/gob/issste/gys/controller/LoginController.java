package gob.issste.gys.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gob.issste.gys.model.Login;
import gob.issste.gys.model.Usuario;
import gob.issste.gys.repository.UsuarioRepository;
import gob.issste.gys.response.ResponseHandler;
import gob.issste.gys.security.JWTAuthenticationConfig;
import gob.issste.gys.service.EncryptionService;
import gob.issste.gys.service.SecurityService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController
public class LoginController {
    @Autowired
    JWTAuthenticationConfig jwtAuthenticationConfig;
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    private SecurityService securityService;

    @Autowired
    EncryptionService encryptionService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(
            @RequestParam String data) {
        try {
            String originalData = encryptionService.decryptGCM(data, EncryptionService.BINDING_KEY, EncryptionService.BINDING_IV);
//            String originalData = encryptionService.decrypt(data, EncryptionService.BINDING_KEY);
            ObjectMapper objectMapper = new ObjectMapper();
            Login login = objectMapper.readValue(originalData, Login.class);
            Usuario user = usuarioRepository.findByName(login.getClaveUser());
            if (Objects.isNull( user )){
                return  ResponseHandler.generateResponse(
                        "Usuario no encontrado", HttpStatus.NOT_FOUND, null);
            } else if(user.isActivo()) {
                boolean validate = securityService.getPwdValidation(login.getPwd(), user.getPassword());
                if (!validate){
                    if(user.getIntentos() < 3 ) {
                        usuarioRepository.updateAttemps(user.getIntentos()+1, user.getIdUsuario());
                    } else if(user.getIntentos() == 3){
                        usuarioRepository.updateActive(false, user.getIdUsuario());
                        return  ResponseHandler.generateResponse(
                                "Usuario bloqueado, contacte a un administrador", HttpStatus.INTERNAL_SERVER_ERROR, null);
                    }
                    return  ResponseHandler.generateResponse(
                            "Usuario y/o contraseña incorrectos", HttpStatus.INTERNAL_SERVER_ERROR, null);
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
                    JSONObject jsonArray = new JSONObject(map);
                    String jsonString = jsonArray.toString();
//                    String encryptedUser = encryptionService.encrypt(jsonString, EncryptionService.BINDING_KEY);
                    String encryptedUser = encryptionService.encryptGCM(jsonString, EncryptionService.BINDING_KEY, EncryptionService.BINDING_IV);
                    usuarioRepository.updateAttemps(0, user.getIdUsuario());
                    return ResponseHandler.generateResponse("Autorizado", HttpStatus.OK, encryptedUser);
                }
            }else{
                return ResponseHandler.generateResponse("El usuario está bloqueado, contactar a un administrador", HttpStatus.FORBIDDEN, null);
            }
        }catch (NullPointerException e) {
            return ResponseHandler.generateResponse(
                    "Error al realizar inicio de sesión", HttpStatus.INTERNAL_SERVER_ERROR, null);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(
                    e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }
}

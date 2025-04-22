package gob.issste.gys.controller;

import gob.issste.gys.security.JWTAuthenticationConfig;
import gob.issste.gys.service.EncryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class KeyController {

    @Autowired
    JWTAuthenticationConfig jwtAuthenticationConfig;
    @Autowired
    EncryptionService encryptionService;
    @GetMapping("/gs3c73t")
    public String generateAESKey() {
        try {
            return jwtAuthenticationConfig.getKeyJWTToken(EncryptionService.BINDING_KEY, EncryptionService.BINDING_IV);
        } catch (Exception e) {
            return "Error al enviar la llave AES";
        }
    }

}

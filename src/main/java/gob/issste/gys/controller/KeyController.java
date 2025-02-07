package gob.issste.gys.controller;

import gob.issste.gys.security.JWTAuthenticationConfig;
import gob.issste.gys.service.EncryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.util.Base64;

@RestController
public class KeyController {

    @Autowired
    JWTAuthenticationConfig jwtAuthenticationConfig;
    @Autowired
    EncryptionService encryptionService;
    @GetMapping("/gs3c73t")
    public String generateAESKey() {
        try {
            // Generar la llave AES
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128); // Usar 128 bits para una clave de 24 bytes
            SecretKey secretKey = keyGen.generateKey();
            // Convertir la llave a formato Base64 para que sea legible
            String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
            byte[] iv = new byte[12];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);
            String encodedIv = Base64.getEncoder().encodeToString(iv);
            EncryptionService.BINDING_KEY = encodedKey.replaceAll("\\s", ""); // Eliminar espacios en blanco
            EncryptionService.BINDING_IV = encodedIv.replaceAll("\\s", ""); // Eliminar espacios en blanco
            return jwtAuthenticationConfig.getKeyJWTToken(encodedKey, encodedIv);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al generar la llave AES";
        }
    }

}

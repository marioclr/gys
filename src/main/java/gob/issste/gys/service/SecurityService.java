package gob.issste.gys.service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;
import gob.issste.gys.JdbcTemplateDemo01Application;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
@Service
@EnableAsync
@EnableScheduling
public class SecurityService {

    public static final String LOGIN_URL = "/login"; // Api de login
    public static final String API_URL = "/api/**"; //Api general
    public static final String KEY_URL = "/gs3c73t"; // Api que entrega llave
    public static final String DEV_SERVER_URL = "https://sigysdev.issste.gob.mx"; // PRE PRODUCCIÓN
    public static final String PROD_SERVER_URL = "https://sigys.issste.gob.mx";// PRODUCCIÓN
    public static final String LOCAL_SERVER_URL = "http://localhost:4200"; // LOCAL
    public static final long TOKEN_EXPIRATION_TIME = 3_600_000; // 1 DE EXPIRACIÓN
//    public static final long TOKEN_EXPIRATION_TIME = 120_000; //2 minutos
    public static final String HEADER_AUTHORIZATION_KEY = "Authorization";
    public static final String TOKEN_BEARER_PREFIX = "Bearer ";
    public static String GENERATED_KEY = ""; //Llave AES
    public static String SUPER_SECRET_KEY = "";
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    private Set<String> invalidatedTokens = new HashSet<>();
    Logger logger = LoggerFactory.getLogger(JdbcTemplateDemo01Application.class);

    public static String generateSecretKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA512");
        keyGenerator.init(512); // Usando una clave de 256 bits
        SecretKey secretKey = keyGenerator.generateKey();
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    public static Key getSigningKey(String secret) {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getEncryptedPwd(String pwd) {
        return bCryptPasswordEncoder.encode(pwd);
    }

    public boolean getPwdValidation(String userPwd, String encryptedPwd) {
        try {
            return bCryptPasswordEncoder.matches(
                    userPwd,
                    encryptedPwd);
        }catch (Exception e){
            return false;
        }

    }

    /**
     * Generación AES para modo GCM con Iv
     * Modificvación: Tamaño de 256 bits
     */

    public static void securityAESkeyGenerator(){
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(256); // Usar 256 bits para una clave de 32 bytes
            SecretKey secretKey = keyGen.generateKey();// Convertir la llave a formato Base64 para que sea legible
            String encodedKey = Base64.getUrlEncoder().encodeToString(secretKey.getEncoded()); //Usar URLEncoder para evitar los caracteres no permitidos + y /
            byte[] iv = new byte[32]; //Usar 32 bits para generar el vector
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);
            String encodedIv = Base64.getUrlEncoder().encodeToString(iv); //Usar URLEncoder para evitar los caracteres no permitidos + y /
            EncryptionService.BINDING_KEY = encodedKey.replaceAll("\\s", ""); // Eliminar espacios en blanco
            EncryptionService.BINDING_IV = encodedIv.replaceAll("\\s", ""); // Eliminar espacios en blanco
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Generación de llave  AES anterior
     */

//    public static String securityAESkeyGenerator(){
//        try {
//            // Generar la llave AES
//            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
//            keyGen.init(128); // Usar 128 bits para una clave de 24 bytes
//            SecretKey secretKey = keyGen.generateKey();
//            // Convertir la llave a formato Base64 para que sea legible
//            String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
//            byte[] iv = new byte[12];
//            SecureRandom random = new SecureRandom();
//            random.nextBytes(iv);
//            String encodedIv = Base64.getEncoder().encodeToString(iv);
//            EncryptionService.BINDING_KEY = encodedKey.replaceAll("\\s", ""); // Eliminar espacios en blanco
//            EncryptionService.BINDING_IV = encodedIv.replaceAll("\\s", ""); // Eliminar espacios en blanco
//            return EncryptionService.BINDING_KEY;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "Error al generar la llave AES";
//        }
//    }

    /**
     * Invalida los token metiendolos dentro una blacklist
     * @param token token de la sesión cerrada
     */
    public void invalidateToken(String token) {
        invalidatedTokens.add(token);
    }

    /**
     * Valida el token contra la balcklist
     * @param token token de solicitud
     * @return booleano
     */
    public boolean isTokenValid(String token) {
        return !invalidatedTokens.contains(token);
    }

    /**
     * Se encarga de limpiar el balcklist cada hora
     */

    @Scheduled(cron = "*/3600 * * * * *")
    @Async
    public void scheduleTokenValidation() {                         
        if(invalidatedTokens.toArray().length != 0) {
            int tokensBorrados = invalidatedTokens.toArray().length;
            invalidatedTokens.clear();
            logger.info("Lista de tokens invalidos depurada con éxito, " + tokensBorrados + " borrados con éxito" );
        }
    }

}

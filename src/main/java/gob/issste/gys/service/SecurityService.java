package gob.issste.gys.service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import gob.issste.gys.model.Usuario;
import gob.issste.gys.response.ResponseHandler;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

@Service
public class SecurityService {

    // Spring Security
    public static final String LOGIN_URL = "/login";
    public static final String API_URL = "/api/**";
    public static final String DEV_SERVER_URL = "https://sigysdev.issste.gob.mx";
    public static final String PROD_SERVER_URL = "https://sigys.issste.gob.mx";
    public static final String LOCAL_SERVER_URL = "http://localhost:4200";
    public static final String HEADER_AUTHORIZACION_KEY = "Authorization";
    public static final String TOKEN_BEARER_PREFIX = "Bearer ";
    public static String SUPER_SECRET_KEY = "";
    public static final long TOKEN_EXPIRATION_TIME = 43_200_000; // 1 day
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public static String generateSecretKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA512");
        keyGenerator.init(512); // Usando una clave de 256 bits
        SecretKey secretKey = keyGenerator.generateKey();
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    public Key getSigningKeyB64(String secret) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
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

}

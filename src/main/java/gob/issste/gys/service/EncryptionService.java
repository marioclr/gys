package gob.issste.gys.service;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Service
public class EncryptionService {
    public static String BINDING_KEY = "";
    public static String BINDING_IV = "";

    public String encrypt(String plainText, String encodedKey) {
        try {
            byte[] key = Base64.getDecoder().decode(encodedKey);
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al cifrar el valor";
        }
    }

    public String decrypt(String encrypted, String secretKey) throws Exception {
        try {
            byte[] keyBytes = secretKey.getBytes("UTF-8");
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            // Reemplaza los caracteres URL-safe de vuelta a los originales
            String base64Encrypted = encrypted.replace('-', '+').replace('_', '/');
            byte[] decoded = Base64.getDecoder().decode(base64Encrypted);
            byte[] decrypted = cipher.doFinal(decoded);
            return new String(decrypted, "UTF-8");
        }catch (Exception e){
            return "Error al descifrar el valor: " + e.getMessage();
        }

    }
}


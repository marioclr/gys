package gob.issste.gys.service;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Service
public class EncryptionService {
    public static String BINDING_KEY = "";
    public static String BINDING_IV = "";
    private static final String AES = "AES";
    private static final String AES_GCM_NO_PADDING = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH = 16;

    public String encryptGCM(String plainText, String key, String iv) throws Exception {
        byte[] keyBytes = Base64.getUrlDecoder().decode(key);
        byte[] ivBytes = Base64.getUrlDecoder().decode(iv);

        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, AES);
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, ivBytes);

        Cipher cipher = Cipher.getInstance(AES_GCM_NO_PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmParameterSpec);

        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());

        return Base64.getUrlEncoder().encodeToString(encryptedBytes);
    }

    public String decryptGCM(String encryptedText, String key, String iv) throws Exception {
        byte[] keyBytes = Base64.getUrlDecoder().decode(key);
        byte[] ivBytes = Base64.getUrlDecoder().decode(iv);
        byte[] encryptedBytes = Base64.getUrlDecoder().decode(encryptedText);

        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, AES);
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, ivBytes);

        Cipher cipher = Cipher.getInstance(AES_GCM_NO_PADDING);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmParameterSpec);

        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        return new String(decryptedBytes);
    }

//----------------------------------Encriptación AES/ECB con Padding------------------------------------------------------

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


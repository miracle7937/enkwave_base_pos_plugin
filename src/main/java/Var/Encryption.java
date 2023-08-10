package Var;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class Encryption {

    private static final String SECRET_KEY = "J/PYjc1ftDFK5+77U1PB80v2TamokGap5yCIP2YI6tQ=";
    private static final String INIT_VECTOR = "gaOr3uvhZEwFeSbRHwlHcg==";



    public static String encrypt(String strToEncrypt) {
        try {
            byte[]  key = Base64.getDecoder().decode(SECRET_KEY);
            SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            IvParameterSpec ivspec = new IvParameterSpec(Base64.getDecoder().decode(INIT_VECTOR));
            Cipher cipher = Cipher.getInstance("AES/CFB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } catch (Exception e) {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }

    public static String decrypt(String encryptedText) {
        try {
            byte[] key = Base64.getDecoder().decode(SECRET_KEY);
            SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            IvParameterSpec ivspec = new IvParameterSpec(Base64.getDecoder().decode(INIT_VECTOR));
            Cipher cipher = Cipher.getInstance("AES/CFB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
            return new String(decryptedBytes, "UTF-8");
        } catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }

}

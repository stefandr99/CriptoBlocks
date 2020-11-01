package com.encryption;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;


public class AES {
    private static final KeyGenerator generatedKey = new KeyGenerator();
    StringBuilder decryptedText = new StringBuilder();
    String iv = generateString();
    String key1 = "nSVB42NVkfzc2";
    String auxIV = iv;

    public static String encrypt(String text, String key) throws NoSuchPaddingException, NoSuchAlgorithmException,
            UnsupportedEncodingException, BadPaddingException,
            IllegalBlockSizeException, InvalidKeyException {

        generatedKey.generateKey(key);
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, generatedKey.getSecretKey());
        return Base64.getEncoder().encodeToString(cipher.doFinal(text.getBytes(StandardCharsets.UTF_8)));
    }

    public static String decrypt(String ciphertext, String key) throws NoSuchPaddingException, NoSuchAlgorithmException,
            BadPaddingException, IllegalBlockSizeException, InvalidKeyException {

        generatedKey.generateKey(key);
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, generatedKey.getSecretKey());
        return new String(cipher.doFinal(Base64.getDecoder().decode(ciphertext)));
    }


}

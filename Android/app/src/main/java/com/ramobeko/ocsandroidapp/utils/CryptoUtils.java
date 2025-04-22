package com.ramobeko.ocsandroidapp.utils;

import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

public class CryptoUtils {
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int IV_SIZE = 12;
    private static final int TAG_LENGTH = 128;

    public static EncryptionResult encrypt(String plaintext) {
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            SecretKey key = KeyStoreUtil.getSecretKey();
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] iv = cipher.getIV(); // Android'in ürettiği IV
            byte[] encrypted = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

            return new EncryptionResult(encrypted, iv);
        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }


    public static String decrypt(byte[] encryptedData, byte[] iv) {
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            SecretKey key = KeyStoreUtil.getSecretKey();
            GCMParameterSpec gcmSpec = new GCMParameterSpec(TAG_LENGTH, iv);
            cipher.init(Cipher.DECRYPT_MODE, key, gcmSpec);
            byte[] decryptedBytes = cipher.doFinal(encryptedData);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Decryption failed", e);
        }
    }

    public static String toBase64(byte[] data) {
        return Base64.encodeToString(data, Base64.DEFAULT);
    }

    public static byte[] fromBase64(String base64) {
        return Base64.decode(base64, Base64.DEFAULT);
    }
}

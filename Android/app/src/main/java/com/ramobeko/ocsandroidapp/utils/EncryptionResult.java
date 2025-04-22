package com.ramobeko.ocsandroidapp.utils;

public class EncryptionResult {
    public final byte[] ciphertext;
    public final byte[] iv;

    public EncryptionResult(byte[] ciphertext, byte[] iv) {
        this.ciphertext = ciphertext;
        this.iv = iv;
    }
}

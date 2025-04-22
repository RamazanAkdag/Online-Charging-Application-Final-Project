package com.ramobeko.ocsandroidapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

public class SecurePrefs {
    private static final String PREFS = "secure";
    private static final String TOKEN_KEY = "token";
    private static final String IV_KEY = "iv";


    public static void saveToken(Context ctx, String token) {
        KeyStoreUtil.generateSecretKeyIfNeeded();
        EncryptionResult result = CryptoUtils.encrypt(token);
        storeInPrefs(ctx, result.ciphertext, result.iv);
    }


    public static String getToken(Context ctx) {
        byte[] encrypted = getFromPrefs(ctx);
        byte[] iv = getIV(ctx);
        if (encrypted == null || iv == null) return null;
        return CryptoUtils.decrypt(encrypted, iv);
    }

    private static void storeInPrefs(Context ctx, byte[] encrypted, byte[] iv) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        prefs.edit()
                .putString(TOKEN_KEY, Base64.encodeToString(encrypted, Base64.DEFAULT))
                .putString(IV_KEY, Base64.encodeToString(iv, Base64.DEFAULT))
                .apply();
    }

    private static byte[] getFromPrefs(Context ctx) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        String encoded = prefs.getString(TOKEN_KEY, null);
        return encoded != null ? Base64.decode(encoded, Base64.DEFAULT) : null;
    }

    private static byte[] getIV(Context ctx) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        String encoded = prefs.getString(IV_KEY, null);
        return encoded != null ? Base64.decode(encoded, Base64.DEFAULT) : null;
    }
}

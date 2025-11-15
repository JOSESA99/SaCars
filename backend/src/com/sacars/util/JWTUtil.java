package com.sacars.util;

import java.util.*;

public class JWTUtil {
    private static final String SECRET = "sacars_secret_key_2025";
    
    public static String generateToken(int userId) {
        // Implementación simple para propósitos educativos
        long timestamp = System.currentTimeMillis() / 1000;
        String payload = userId + ":" + timestamp;
        return Base64.getEncoder().encodeToString(payload.getBytes());
    }
    
    public static int verificarToken(String token) {
        if (token == null) {
            return -1;
        }
        try {
            String payload = new String(Base64.getDecoder().decode(token));
            String[] parts = payload.split(":");
            return Integer.parseInt(parts[0]);
        } catch (Exception e) {
            return -1;
        }
    }
}

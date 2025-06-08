package com.petcare.staff.utils;

import android.util.Base64;

import org.json.JSONObject;

public class JwtUtils {

    public static JSONObject decodePayload(String jwtToken) {
        try {
            String[] parts = jwtToken.split("\\.");
            if (parts.length < 2) return null;

            String payload = parts[1];
            byte[] decodedBytes = Base64.decode(payload, Base64.URL_SAFE);
            String decodedPayload = new String(decodedBytes);

            return new JSONObject(decodedPayload);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
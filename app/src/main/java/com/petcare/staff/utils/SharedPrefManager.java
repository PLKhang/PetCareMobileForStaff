package com.petcare.staff.utils;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

public class SharedPrefManager {
    public static final String PREF_NAME = "app_prefs";
    public static final String KEY_TOKEN = "auth_token";
    public static final String KEY_BRANCHID = "branch_id";
    public static final String KEY_REMEMBER = "remember";

    public static void saveToken(Context context, String token) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_TOKEN, token).apply();
    }

    public static String getToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_TOKEN, null);
    }

    public static void clearToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().remove(KEY_TOKEN).apply();
    }

    public static String getUserId(Context context) throws JSONException {
        JSONObject payload = JwtUtils.decodePayload(getToken(context));
        assert payload != null;
        return payload.getString("user_id");
    }

    public static int getBranchId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(KEY_BRANCHID, -1);
    }

    public static boolean isActiveToken(Context context) throws JSONException {
        JSONObject payload = JwtUtils.decodePayload(getToken(context));
        assert payload != null;

        long expiredAt = payload.getLong("expiredAt");
        long now = System.currentTimeMillis() / 1000;

        return expiredAt > now;
    }

}

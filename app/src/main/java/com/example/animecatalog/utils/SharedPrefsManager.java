package com.example.animecatalog.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefsManager {
    private static final String PREF_NAME = "AnimeCatalogPrefs";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_EMAIL = "email";

    private SharedPreferences prefs;

    public SharedPrefsManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveToken(String token) {
        prefs.edit().putString(KEY_TOKEN, token).apply();
    }

    public String getToken() {
        return prefs.getString(KEY_TOKEN, null);
    }

    public void saveUserEmail(String email) {
        prefs.edit().putString(KEY_EMAIL, email).apply();
    }

    public String getUserEmail() {
        return prefs.getString(KEY_EMAIL, null);
    }

    public void clearToken() {
        prefs.edit().remove(KEY_TOKEN).remove(KEY_EMAIL).apply();
    }
}
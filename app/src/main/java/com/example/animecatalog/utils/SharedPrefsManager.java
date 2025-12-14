package com.example.animecatalog.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefsManager {
    private static final String PREF_NAME = "AnimeCatalogPrefs";
    private SharedPreferences prefs;

    public SharedPrefsManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
    
    // Методы для работы с токенами и пользователями удалены, так как аутентификация больше не используется.
    // Вы можете добавить сюда методы для сохранения настроек приложения (тема, фильтры и т.д.)
}
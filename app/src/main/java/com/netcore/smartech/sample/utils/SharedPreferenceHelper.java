package com.netcore.smartech.sample.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferenceHelper {
    private static SharedPreferences sharedPreferences;
    private static Editor sharedPreferencesEditor;

    private static SharedPreferences getSharedPreferences(Context context) {
        if (sharedPreferences == null)
            sharedPreferences = context.getApplicationContext().getSharedPreferences(Keys.SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
        return sharedPreferences;
    }

    private static Editor getSharedPreferencesEditor(Context context) {
        if (sharedPreferencesEditor == null)
            sharedPreferencesEditor = getSharedPreferences(context).edit();
        return sharedPreferencesEditor;
    }

    public static void putString(Context context, String key, String value) {
        getSharedPreferencesEditor(context).putString(key, value).apply();
    }

    public static String getString(Context context, String key, String defaultValue) {
        return getSharedPreferences(context).getString(key, defaultValue);
    }

    public static void putInt(Context context, String key, int value) {
        getSharedPreferencesEditor(context).putInt(key, value).apply();
    }

    public static int getInt(Context context, String key, int defaultValue) {
        return getSharedPreferences(context).getInt(key, defaultValue);
    }

    public static void putLong(Context context, String key, long value) {
        getSharedPreferencesEditor(context).putLong(key, value).apply();
    }

    public static long getLong(Context context, String key, long defaultValue) {
        return getSharedPreferences(context).getLong(key, defaultValue);
    }

    public static void putFloat(Context context, String key, float value) {
        getSharedPreferencesEditor(context).putFloat(key, value).apply();
    }

    public static float getFloat(Context context, String key, float defaultValue) {
        return getSharedPreferences(context).getFloat(key, defaultValue);
    }

    public static void putBoolean(Context context, String key, boolean value) {
        getSharedPreferencesEditor(context).putBoolean(key, value).apply();
    }

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        return getSharedPreferences(context).getBoolean(key, defaultValue);
    }
}
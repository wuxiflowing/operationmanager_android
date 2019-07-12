package com.qyt.om.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.qyt.om.BuildConfig;

import java.lang.reflect.Type;

/**
 * Created 任瑞刚
 * <p>
 * Android SharedPreferences数据存储和读取方法
 */
public class SharedPref {
    private SharedPreferences preferences;

    public SharedPref(Context context) {
        preferences = context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
    }

    public void setJsonInfo(String key, Object o) {
        if (o != null)
            preferences.edit().putString(key, new Gson().toJson(o)).commit();
        else
            preferences.edit().remove(key).commit();
    }

    public <T> T getJsonInfo(String key, Class<T> classOfT) {
        String value = preferences.getString(key, "");
        if (!TextUtils.isEmpty(value)) {
            return new Gson().fromJson(value, classOfT);
        }
        return null;
    }

    public <T> T getJsonInfo(String key, Type type) {
        String value = preferences.getString(key, "");
        if (!TextUtils.isEmpty(value)) {
            return new Gson().fromJson(value, type);
        }
        return null;
    }

    public void setInt(String key, int value) {
        preferences.edit().putInt(key, value).commit();
    }

    public int getInt(String key) {
        return preferences.getInt(key, 0);
    }

    public void setString(String key, String value) {
        preferences.edit().putString(key, value).commit();
    }

    public String getString(String key) {
        return preferences.getString(key, "");
    }

    public void setBoolean(String key, boolean value) {
        preferences.edit().putBoolean(key, value).commit();
    }

    public boolean getBoolean(String key) {
        return preferences.getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean value) {
        return preferences.getBoolean(key, value);
    }

    public void removeKey(String key) {
        preferences.edit().remove(key).commit();
    }
}

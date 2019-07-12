package com.bangqu.lib.volley;

import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Created by Administrator on 2018-6-13 0013.
 */

public class BaseModel {
    public boolean success;
    public String message;
    public JsonElement data;
    public int total;
    public int code;

    public BaseModel(JsonObject jsonObject, String key) {
        try {
            success = jsonObject.get("success").getAsBoolean();
            message = jsonObject.get("message").getAsString();
            data = jsonObject.get(key);
            code = jsonObject.get("code").getAsInt();
            total = jsonObject.get("total").getAsInt();
        } catch (Exception e) {
            Log.e("Base", e.getMessage());
        }
    }
}

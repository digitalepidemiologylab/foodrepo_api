package com.salathelab.helper;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONHelper {
    public static String getStringIfExists(JSONObject obj, String key) throws JSONException {
        return getStringIfExists(obj, key, null);
    }
    public static String getStringIfExists(JSONObject obj, String key, String defaultValue) throws JSONException {
        if (obj.isNull(key)) {
            return defaultValue;
        }
        return obj.getString(key);
    }

    public static float getFloatIfExists(JSONObject obj, String key) {
        return getFloatIfExists(obj, key, 0.0f);
    }
    public static float getFloatIfExists(JSONObject obj, String key, float defaultValue) {
        try {
            return (float)obj.getDouble(key);
        } catch (JSONException e) {
            return defaultValue;
        }
    }
}

package com.salathelab.openfoodapidemo.helper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dj on 2/10/17.
 */

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
}

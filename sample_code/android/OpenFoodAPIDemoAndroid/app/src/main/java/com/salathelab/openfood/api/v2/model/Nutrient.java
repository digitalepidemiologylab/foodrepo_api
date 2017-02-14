package com.salathelab.openfood.api.v2.model;

import com.salathelab.helper.JSONHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Nutrient {
    // fields
    protected float perHundred;
    protected float perDay;
    protected float perPortion;

    // getters
    public float getPerHundred() { return perHundred; }
    public float getPerDay() { return perDay; }
    public float getPerPortion() { return perPortion; }

    //ctor
    public Nutrient(JSONObject jsonObject) {
        perHundred = JSONHelper.getFloatIfExists(jsonObject, "per-hundred", Float.NaN);
        perDay     = JSONHelper.getFloatIfExists(jsonObject, "per-day"    , Float.NaN);
        perPortion = JSONHelper.getFloatIfExists(jsonObject, "per-portion", Float.NaN);
    }

    public static Map<String, Nutrient> parseToMap(JSONArray jsonArray) throws JSONException{
        final int numNutrients = jsonArray.length();
        Map<String, Nutrient> ret = new HashMap<String, Nutrient>();
        for (int i = 0; i < numNutrients; ++i) {
            JSONObject jsonNutrient = jsonArray.getJSONObject(i);
            String name = jsonNutrient.getString("name").toLowerCase().replaceAll("[ -]", "_").replaceAll("[\\[\\]\\(\\)]", "");
            ret.put(name, new Nutrient(jsonNutrient));
        }
        return ret;
    }
}

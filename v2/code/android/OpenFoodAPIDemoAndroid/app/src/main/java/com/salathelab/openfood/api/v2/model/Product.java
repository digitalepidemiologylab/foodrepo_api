package com.salathelab.openfood.api.v2.model;

import com.salathelab.helper.JSONHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class Product {
    // fields
    protected int id;
    protected String name;
    protected String barcode;
    protected Map<String,Nutrient> nutrients;

    // getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getBarcode() { return barcode; }

    // setters
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setBarcode(String barcode) { this.barcode = barcode; }

    // ctor
    public Product(JSONObject jsonObject) throws JSONException {
        setId(jsonObject.getInt("id"));
        JSONObject productAttrs = jsonObject.getJSONObject("attributes");
        setBarcode(productAttrs.getString("barcode"));
        setName(JSONHelper.getStringIfExists(productAttrs, "name", ""));
        nutrients = Nutrient.parseToMap(productAttrs.getJSONArray("nutrients"));
    }

    public String getShortDescription() {
        String ret = "id: " + id + ", barcode: " + barcode + ", name: \"" + name + "\"";
        if (nutrients.containsKey("sugars")) {
            ret += " (" + Math.round(nutrients.get("sugars").getPerHundred()) + "% Sugar)";
        }
        return ret;
    }

    public static Product[] parse(JSONArray jsonArray) throws JSONException {
        final int numProducts = jsonArray.length();
        Product[] ret = new Product[numProducts];
        for (int i = 0; i < numProducts; ++i) {
            JSONObject jsonProduct = jsonArray.getJSONObject(i);
            ret[i] = new Product(jsonProduct);
        }
        return ret;
    }
}

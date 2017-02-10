package com.salathelab.openfoodapidemo.async;

import android.os.AsyncTask;

import com.salathelab.openfoodapidemo.helper.JSONHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;

public class ProductsDownloader extends AsyncTask<ProductsDownloader.DownloaderParamsStart, ProductsDownloader.DownloaderParamsProgress, Void> {
    public static class DownloaderParamsStart {
        final String apiKey;
        final ProgressCallback progressCallback;
        final SuccessCallback successCallback;
        final CancelCallback cancelCallback;

        public DownloaderParamsStart(String apiKey, ProgressCallback progressCallback, SuccessCallback successCallback, CancelCallback cancelCallback) {
            this.apiKey = apiKey;
            this.progressCallback = progressCallback;
            this.successCallback = successCallback;
            this.cancelCallback = cancelCallback;
        }
    }

    public static class DownloaderParamsProgress {
        final String status;
        final String data;


        public DownloaderParamsProgress(String status, String data) {
            this.status = status;
            this.data = data;
        }
    }

    public interface ProgressCallback {
        void onStatusChanged(CharSequence status);
        void onDataAdded(CharSequence data);
    }
    public interface SuccessCallback {
        void onSuccess();
    }
    public interface CancelCallback {
        void onCancel();
    }


    final String ORIGIN = "https://www.openfood.ch";
    final String PRODUCTS_ENDPOINT = "/api/v2/products";
    long startTaskNanos;
    DownloaderParamsStart startParams;

    @Override
    protected Void doInBackground(DownloaderParamsStart... params) {
        startTaskNanos = System.nanoTime();
        startParams = params[0];
        {
            String displayKey = params[0].apiKey;
            if (displayKey == null || displayKey.length() == 0) {
                displayKey = "<No key provided>";
            }
            publishProgress(new DownloaderParamsProgress("Got API Key: " + displayKey, null));
        }
        String address = ORIGIN + PRODUCTS_ENDPOINT;
        boolean shouldGetData = true;
        while (shouldGetData) {
            long startPageNanos = System.nanoTime();
            shouldGetData = false;
            {
                String displayAddress = address;
                try {
                    displayAddress = URLDecoder.decode(address, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                }
                publishProgress(new DownloaderParamsProgress("Connecting to '" + displayAddress + "'...", null));
            }

            InputStream stream = null;
            try {
                URL url = new URL(address);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Authorization", "Token token=" + startParams.apiKey);
                try {
                    stream = conn.getInputStream();
                } catch (FileNotFoundException e) {
                    stream = conn.getErrorStream();
                }
                publishProgress(new DownloaderParamsProgress("Connected with response code '" + conn.getResponseCode() + "'. Receiving data...", null));
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(stream, "UTF-8"),
                        1024
                );
                String strData = "";
                {
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    strData = sb.toString();
                }
                publishProgress(new DownloaderParamsProgress("Received data in " + (System.nanoTime() - startPageNanos)/1000000000.0f + " seconds. Parsing...", null));
                try {
                    JSONObject jsonObj = new JSONObject(strData);
                    JSONArray products = jsonObj.getJSONArray("data");
                    int numProducts = products.length();
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < numProducts; ++i) {
                        JSONObject product = products.getJSONObject(i);
                        int pId = product.getInt("id");
                        JSONObject productAttrs = product.getJSONObject("attributes");
                        String pBarcode = productAttrs.getString("barcode");
                        String pName = JSONHelper.getStringIfExists(productAttrs, "name", "");
                        sb.append("id: " + pId + ", barcode: " + pBarcode + ", name: \"" + pName + "\"\n");
                    }
                    publishProgress(new DownloaderParamsProgress("Parsed " + numProducts + " products", sb.toString()));
                    JSONObject links = jsonObj.getJSONObject("links");
                    address = JSONHelper.getStringIfExists(links, "next");
                    if (numProducts > 0 && address != null) {
                        shouldGetData = true;
                        publishProgress(new DownloaderParamsProgress("Found link to next page of data", null));
                    }
                } catch (JSONException e) {
                    publishProgress(new DownloaderParamsProgress("Error parsing JSON", strData));
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(DownloaderParamsProgress... values) {
        if (values[0].status != null) {
            startParams.progressCallback.onStatusChanged(values[0].status);
        }
        if (values[0].data != null) {
            startParams.progressCallback.onDataAdded(values[0].data);
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        onProgressUpdate(new DownloaderParamsProgress(
                "Cancelled after " + (System.nanoTime() - startTaskNanos)/1000000000.0f + " seconds",
                null
        ));
        if (startParams.cancelCallback != null) {
            startParams.cancelCallback.onCancel();
        }
    }

    @Override
    protected void onPostExecute(Void result) {
        onProgressUpdate(new DownloaderParamsProgress(
                "Completed after " + (System.nanoTime() - startTaskNanos)/1000000000.0f + " seconds",
                null
        ));
        if (startParams.successCallback != null) {
            startParams.successCallback.onSuccess();
        }
    }
}

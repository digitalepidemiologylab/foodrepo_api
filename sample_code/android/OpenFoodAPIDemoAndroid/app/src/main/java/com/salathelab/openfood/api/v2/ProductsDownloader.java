package com.salathelab.openfood.api.v2;

import android.os.AsyncTask;

import com.salathelab.helper.JSONHelper;
import com.salathelab.openfood.api.v2.endpoint.ProductsEndpoint;
import com.salathelab.openfood.api.v2.model.Product;

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
import java.net.MalformedURLException;
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

        ProductsEndpoint nextEndpoint = new ProductsEndpoint(params[0].apiKey, 50, 0);
        while (nextEndpoint != null) {
            long startPageNanos = System.nanoTime();
            ProductsEndpoint endpoint = nextEndpoint;
            nextEndpoint = null;

            publishProgress(new DownloaderParamsProgress("Connecting to '" + endpoint.getAddressDecoded() + "'...", null));
            InputStream stream = null;
            try {
                stream = endpoint.getStream();
                publishProgress(new DownloaderParamsProgress("Connected with response code '" + endpoint.getResponseCode() + "'. Receiving data...", null));
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
                    int numProducts = parseAndPrintProducts(jsonObj);
                    JSONObject links = jsonObj.getJSONObject("links");
                    String nextAddress = JSONHelper.getStringIfExists(links, "next");
                    if (numProducts > 0 && nextAddress != null) {
                        nextEndpoint = new ProductsEndpoint(params[0].apiKey, nextAddress);
                        publishProgress(new DownloaderParamsProgress("Found link to next page of data", null));
                    }
                } catch (JSONException e) {
                    publishProgress(new DownloaderParamsProgress("Error parsing JSON", strData));
                }
            } catch (MalformedURLException e) {
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

    private int parseAndPrintProducts(JSONObject jsonObj) throws JSONException {
        JSONArray jsonProducts = jsonObj.getJSONArray("data");
        Product[] products = Product.parse(jsonProducts);

        StringBuilder sb = new StringBuilder();
        for (Product p : products) {
            sb.append(p.getShortDescription() + "\n");
        }
        publishProgress(new DownloaderParamsProgress("Parsed " + products.length + " products", sb.toString()));
        return products.length;
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

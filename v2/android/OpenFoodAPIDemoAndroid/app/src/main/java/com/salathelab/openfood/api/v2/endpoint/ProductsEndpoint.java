package com.salathelab.openfood.api.v2.endpoint;

import android.net.Uri;

import com.salathelab.helper.UriHelper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;

public class ProductsEndpoint {
    public static final String SCHEME       = "https";
    public static final String AUTHORITY    = "www.openfood.ch";
    public static final String PATH         = "api/v2/products";
    public static final String DEFAULT_SPEC = SCHEME + "://" + AUTHORITY + "/" + PATH;

    protected final URL url;
    protected final String key;

    protected int responseCode = 0;
    public int getResponseCode() { return responseCode; }

    // ctors
    public ProductsEndpoint(String key) {
        this.key = key;
        URL url;
        try {
            url = new URL(DEFAULT_SPEC);
        } catch (MalformedURLException e) {
            url = null;
        }
        this.url = url;
    }
    public ProductsEndpoint(String key, int pageSize, int pageNumber) {
        if (pageSize < 1) {
            throw new IllegalArgumentException("Cannot have page size below 1");
        }
        this.key = key;
        URL url;
        try {
            url = new URL(ProductsEndpoint.getSpec(pageSize, pageNumber));
        } catch (MalformedURLException e) {
            url = null;
        }
        this.url = url;
    }
    public ProductsEndpoint(String key, String customAddress) throws MalformedURLException {
        this.key = key;
        this.url = new URL(customAddress);
    }

    public String getAddressDecoded() {
        if (url == null) {
            return "<No address>";
        }
        try {
            return URLDecoder.decode(url.toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return url.toString();
        }
    }

    public InputStream getStream() throws IOException {
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Token token=" + this.key);
        InputStream ret;
        try {
            ret = conn.getInputStream();
        } catch (FileNotFoundException e) {
            ret = conn.getErrorStream();
        }
        this.responseCode = conn.getResponseCode();
        return ret;
    }

    private static String getSpec(int pageSize, int pageNumber) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(SCHEME).authority(AUTHORITY);
        UriHelper.appendSeparatedPath(builder, PATH);
        builder.appendQueryParameter("page[size]", String.valueOf(pageSize));
        if (pageNumber > 0) {
            builder.appendQueryParameter("page[number]", String.valueOf(pageNumber));
        }
        return builder.build().toString();
    }
}

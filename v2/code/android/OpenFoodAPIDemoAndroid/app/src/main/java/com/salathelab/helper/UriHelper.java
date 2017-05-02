package com.salathelab.helper;

import android.net.Uri;

public class UriHelper {
    public static void appendSeparatedPath(Uri.Builder uriBuilder, String path) {
        String[] paths = path.split("/");
        for (String s : paths) {
            uriBuilder.appendPath(s);
        }
    }
}

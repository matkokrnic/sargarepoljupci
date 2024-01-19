package com.progi.sargarepoljupci.Utilities;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class URLcheck {

    public static boolean isPNGImage(String imageUrl) {
        String contentType;
        try {
            URI uri = new URI(imageUrl);
            URL url = uri.toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");

            // Get the content type (MIME type) of the image
            contentType = connection.getContentType();


            // Check if the content type matches the MIME type for PNG images

        } catch (IOException | URISyntaxException e) {

            return false; // Return false in case of exceptions or invalid URL
        }
        return contentType != null && contentType.equals("image/png");
    }


}

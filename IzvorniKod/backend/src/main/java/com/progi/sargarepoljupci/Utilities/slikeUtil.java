package com.progi.sargarepoljupci.Utilities;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Base64;

import static org.springframework.http.client.observation.ClientHttpObservationDocumentation.LowCardinalityKeyNames.URI;

public class slikeUtil {
    public static byte[] slikaToByteArray(String urlText) throws Exception {
        URI uri = new URI(urlText);
        URL url = uri.toURL();
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try (InputStream inputStream = url.openStream()) {
            int n = 0;
            byte [] buffer = new byte[2048];
            while (-1 != (n = inputStream.read(buffer))) {
                output.write(buffer, 0, n);
            }
        }

        return output.toByteArray();
    }


    public static String byteaToBase64(byte[] byteaData) {
        return Base64.getEncoder().encodeToString(byteaData);
    }





}

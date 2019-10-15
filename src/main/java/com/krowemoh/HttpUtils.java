package com.krowemoh;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.lang.StringBuilder;
import java.io.IOException;
import java.util.Objects;

public class HttpUtils {
    public static Document getByUri(String uri) {
        URL url;
        HttpURLConnection connection;
        InputStream is = null;
        BufferedReader br = null;
        String inputLine;
        StringBuilder sb = null;

        try {
            url = new URL(uri);
            connection = (HttpURLConnection) url.openConnection();
            // Add header info for simulate browser
            connection.setRequestProperty("Accept",
                    "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            connection.setRequestProperty("Accept-Language", "en,zh-CN;q=0.9,zh;q=0.8");
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36" +
                            " (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");

            connection.connect();
            is = connection.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            sb = new StringBuilder();

            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine).append("\n");
            }
        } catch (IOException mue) {
            mue.printStackTrace();
            System.out.println("Failed!");
        } finally {
            try {
                if (is != null)
                    is.close();
                if (br != null)
                    br.close();
            } catch (IOException ioe) {
                // nothing to see here
            }
        }
        return new Document(Objects.requireNonNull(sb).toString());
    }
}
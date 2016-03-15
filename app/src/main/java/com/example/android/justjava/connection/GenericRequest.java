package com.example.android.justjava.connection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;


public class GenericRequest {
    static String response = null;
    public final static int GETR = 1;
    public final static int POSTR = 2;

    //Constructor
    public GenericRequest() {
    }

    public String makeWebCall(String url, int method) {
        return this.makeWebCall(url, method, null);
    }

    public String makeWebCall(String host, int method, HashMap<String, String> params){
        URL url;
        String response = "";
        try {
            url = new URL(host);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15001);
            conn.setConnectTimeout(15001);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            if (method == POSTR) {
                conn.setRequestMethod("POST");
            } else if (method == GETR) {
                conn.setRequestMethod("GET");

            }

            if (params != null) {
                OutputStream ostream = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ostream, "UTF-8"));
                StringBuilder requestresult = new StringBuilder();
                boolean first = true;
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    if (first)
                        first = false;
                    else
                        requestresult.append("&");
                    requestresult.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                    requestresult.append("=");
                    requestresult.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                }
                writer.write(requestresult.toString());
                writer.flush();
                writer.close();
                ostream.close();
            }
            int reqresponseCode = conn.getResponseCode();

            if (reqresponseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

}




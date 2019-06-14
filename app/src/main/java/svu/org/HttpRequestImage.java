package svu.org;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestImage extends AsyncTask<HttpCallImage, String, String> {

    public static final int READ_TIMEOUT = 15000;
    public static final int CONNECTION_TIMEOUT = 15000;

    @Override
    protected void onPreExecute() {
        // Create Show ProgressBar
    }

    private String getDataString(HashMap<String, FileOutputStream> params, int methodType) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean isFirst = true;
        if(params != null) {
            for (Map.Entry<String, FileOutputStream> entry : params.entrySet()) {
                if (isFirst) {
                    isFirst = false;
                    if (methodType == HttpCallImage.GET) {
                        result.append("?");
                    }
                } else {
                    result.append("&");
                }
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(String.valueOf(entry.getValue()), "UTF-8"));
            }
        }

        return result.toString();
    }

    @Override
    protected String doInBackground(HttpCallImage... params) {
        HttpCallImage httpcall = params[0];
        HttpURLConnection urlConnection = null;
        StringBuilder result = new StringBuilder();

        try {

            String dataParams = getDataString(httpcall.getParams(), httpcall.getMethodtype());
            URL url = new URL(httpcall.getMethodtype() == HttpCallImage.GET ? httpcall.getUrl() +  dataParams: httpcall.getUrl());
            urlConnection =(HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(httpcall.getMethodtype() == HttpCallImage.GET ? "GET" : "POST");
            if(httpcall.getParams() != null && httpcall.getMethodtype() == HttpCallImage.POST) {
                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                writer.append(dataParams);
                writer.flush();
                writer.close();
                os.close();
            }
            int responseCode = urlConnection.getResponseCode();

            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
            urlConnection.setReadTimeout(READ_TIMEOUT);
            urlConnection.connect();


            if(!(
                    responseCode != HttpURLConnection.HTTP_OK &&
                            responseCode != HttpURLConnection.HTTP_CREATED &&
                            responseCode != HttpURLConnection.HTTP_BAD_REQUEST &&
                            responseCode != HttpURLConnection.HTTP_NOT_FOUND &&
                            responseCode != HttpURLConnection.HTTP_INTERNAL_ERROR)
            ) {
                String line;
                BufferedReader br;

                if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                    br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(("404").getBytes("UTF-8"))));
                } else if (!(responseCode == HttpURLConnection.HTTP_BAD_REQUEST || responseCode == HttpURLConnection.HTTP_INTERNAL_ERROR)) {
                    br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                } else{
                    br = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream()));
                }
                while((line = br.readLine()) != null){
                    result.append(line);
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch(Exception e){
            e.printStackTrace();
        }

        return result.toString();
    }

    protected void onResponse(String response) throws JSONException {

    }

    protected void onPostExecute(String result){
        super.onPostExecute(result);
        try {
            onResponse(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}


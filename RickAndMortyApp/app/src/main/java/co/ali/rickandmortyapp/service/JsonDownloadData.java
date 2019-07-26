package co.ali.rickandmortyapp.service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class JsonDownloadData {
    private HttpURLConnection urlConnection = null;
    private final ExceptionHandler exceptionHandler;

    public JsonDownloadData(final ExceptionHandler exceptionHandler) {
     this.exceptionHandler = exceptionHandler;
    }

    public JSONObject getJSONFromUrl(String strUrl) {
        InputStream inStream;
        String json;

        inStream = getInputStream(strUrl);

        json = getJsonString(inStream);

        return getJsonObj(json);
    }

    public Bitmap downloadImageFromUrl(String strUrl) {
        InputStream inStream;

        inStream = getInputStream(strUrl);

        return getBitmap(inStream);
    }

    public InputStream getInputStream(String strUrl){
        InputStream inStream = null;

        //Get InputStream from URL
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            inStream = urlConnection.getInputStream();
        } catch (Exception e){
            exceptionHandler.displayExceptionMessage(e.getMessage());
        }

        return inStream;
    }

    public String getJsonString(InputStream inStream) {
        String json = "";

        //Read data from InputStream
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            inStream.close();
            urlConnection.disconnect();
            json = sb.toString();
        } catch (Exception e) {
            exceptionHandler.displayExceptionMessage(e.getMessage());
        }

        return json;
    }

    public JSONObject getJsonObj(String json){
        JSONObject jsonObj;

        try {
            jsonObj = new JSONObject(json);
        } catch (Exception e) {
            exceptionHandler.displayExceptionMessage(e.getMessage());
            return null;
        }

        return jsonObj;
    }

    public Bitmap getBitmap(InputStream inStream) {
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inStream);

            return BitmapFactory.decodeStream(bufferedInputStream);
        } catch (Exception e) {
            exceptionHandler.displayExceptionMessage(e.getMessage());
            return null;
        }
    }
}

package com.example.android.quakereport;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {



    /** Sample JSON response for a USGS query */
    private static final String LOG_TAG= QueryUtils.class.getSimpleName();
    private static final String SAMPLE_JSON_RESPONSE = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Return a list of {@link Earthquake} objects that has been built up from
     * parsing a JSON response.
     */


public static List<Earthquake> fetchEarthquakeData(String url)
{
    List <Earthquake> earthquake;
    URL url1 =createUrl(url);

    String jsonResponse = null;

    try {
        jsonResponse = makeHttpRequest(url1);
    } catch (IOException e) {
        e.printStackTrace();
    }
    earthquake = extractFeatureFromJSON(jsonResponse);

    return earthquake;

}

    private static String makeHttpRequest(URL url1) throws IOException {

        String jsonRespose = "";

        if (url1 == null)
            return jsonRespose;

        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        try {

            httpURLConnection = (HttpURLConnection) url1.openConnection();
            httpURLConnection.setReadTimeout(10000 /* milliseconds */);
            httpURLConnection.setConnectTimeout(15000 /* milliseconds */);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            if(httpURLConnection.getResponseCode() == 200){
                inputStream = httpURLConnection.getInputStream();

                jsonRespose = readFromStream(inputStream);
            }
            else {
                Log.e(LOG_TAG,"Error response code "+httpURLConnection.getResponseCode());
            }

        } catch (IOException e) {
            Log.e(LOG_TAG, "problem to retrieve the earthquake JSON ", e);
        } finally {
            if (httpURLConnection != null)
                httpURLConnection.disconnect();
            if (inputStream != null)
                inputStream.close();

        }
        return jsonRespose;
    }

    public static List<Earthquake> extractFeatureFromJSON(String jsonResponse) {

    if(TextUtils.isEmpty(jsonResponse))
    {
        return null;
    }
        List<Earthquake> earthquakes = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.
            JSONObject rootJSONObject = new JSONObject(jsonResponse);
            JSONArray features = rootJSONObject.optJSONArray("features");
            for(int i = 0; i<features.length(); i++)
            {
                JSONObject jsonObject = features.getJSONObject(i);
                JSONObject properties = jsonObject.getJSONObject("properties");
                double mag = properties.getDouble("mag");
                String place = properties.getString("place");
                long time = properties.getLong("time");
                String url = properties.getString("url");
                earthquakes.add(new Earthquake(mag,place,time,url));
            }


        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;

    }

    private static String readFromStream(InputStream inputStream) throws IOException {

        StringBuilder output = new StringBuilder();

        if(inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line!=null)
            {
                output.append(line);
                line  = bufferedReader.readLine();

            }
        }
        return output.toString();

    }

    private static URL createUrl(String url) {
    URL url2 = null;

    try{
        url2 = new URL(url);

    } catch (MalformedURLException e)
    {
        Log.e(LOG_TAG,"Error with creating url ",e);

    }
    return url2;
    }
}

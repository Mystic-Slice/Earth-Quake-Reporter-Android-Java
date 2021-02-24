package com.example.android.earthquakereport;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();



    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Return a list of {@link Event} objects that has been built up from
     * parsing a JSON response.
     */
    public static List<Event> extractFeaturesFromJson(String earthquakeJSON) {


        if(TextUtils.isEmpty(earthquakeJSON)){
            return null;
        }


        // Create an empty ArrayList that we can start adding earthquakes to
        List<Event> earthquakes = new ArrayList<>();


        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // Create a JSONObject from the JSON response string
            JSONObject earthQuakeQueryResponse = new JSONObject(earthquakeJSON);

            // Extract the JSONArray associated with the key called "features",
            // which represents a list of features (or earthquakes).
            JSONArray quakesArray = earthQuakeQueryResponse.getJSONArray("features");


            for(int i = 0;i<quakesArray.length();i++){
                JSONObject quake = new JSONObject();
                quake = quakesArray.getJSONObject(i);
                quake = quake.getJSONObject("properties");

                //Getting and formatting the location
                String fullLocation = quake.getString("place");
                String splitter = "of";
                String[] arr;
                String offset = "Near the";
                arr = fullLocation.split(splitter);
                String location = arr[arr.length - 1];
                if(arr.length == 2){
                    offset = arr[0];
                    offset += "of";
                }

                // Getting and formatting the magnitude
                double magnitude = quake.getDouble("mag");

                // Getting and formatting the date and time of the quake
                long timeInMs =quake.getLong("time");
                Date dateOfQuake = new Date(timeInMs);
                SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM DD , yyyy");
                String date = dateFormatter.format(dateOfQuake);
                SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm a");
                String time = timeFormatter.format(dateOfQuake);

                //Getting the url
                String url = quake.getString("url");


                earthquakes.add(new Event(location,offset,magnitude,date,time,url));
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

    /**
     * Returns new URL object from the given string URL
     */
    private static URL createUrl(String stringUrl){
        URL url = null;
        try{
            url = new URL(stringUrl);
        }catch (MalformedURLException e){
            Log.e(LOG_TAG,"Problem building the URL ",e);
        }
        return url;
    }
    /**
     * Make an HTTP request to the given URL and return a String as the response
     */
    private static String makeHttpRequest(URL url) throws IOException{
        String jsonResponse = "";

        if(url == null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if(urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else{
                Log.e(LOG_TAG,"Error response code: "+urlConnection.getResponseCode());
            }
        }catch (IOException e){
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


    /**
     * Query the USGS dataset and return a list of {@link Event} objects.
     */
    public static List<Event> fetchEarthquakeData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        Log.i("JSON",jsonResponse);

        // Extract relevant fields from the JSON response and create a list of {@link Earthquake}s
        List<Event> earthquakes = extractFeaturesFromJson(jsonResponse);

        // Return the list of {@link Earthquake}s
        return earthquakes;
    }

}

package android.example.imagefetchingtest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.util.ArrayList;
import java.util.List;

public class Utils {

    private static final String LOG_TAG = Utils.class.getSimpleName();

    private Utils() {
    }


    static List<News> fetchBooks(String requestUrl) {
        // Create valid url object from the requestURL
        URL url = createUrl(requestUrl);

        // Initialize empty String object to hold the parsed JSON response
        String jsonResponse = "";

        // Perform HTTP request to the above created valid URL
        try {
            jsonResponse = makeHttpRequest(url);

        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request for the search criteria");
        }

        // Extract information from the JSON response for each book
        // Return list of books
        return Utils.extractFeatures(jsonResponse);
    }

    /** Returns new URL object from the given string URL. */
    private static URL createUrl(String stringUrl) {
        // Initialize an empty {@link URL} object to hold the parsed URL from the stringUrl
        URL url = null;

        // Parse valid URL from param stringUrl
        // Handle Malformed urls
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the url! Check your URL.");
        }

        // Return valid url
        return url;
    }

    /** Make an HTTP request to the given URL and return a String as the response. */
    private static String makeHttpRequest(URL url) throws IOException {
        // Initialize variable to hold the parsed json response
        String jsonResponse = "";

        // Return early if url is null
        if (url == null) {
            return jsonResponse;
        }

        // Initialize HTTP connection object
        HttpURLConnection urlConnection = null;

        // Initialize {@link InputStream} to hold response from request
        InputStream inputStream = null;

        try {
            // Establish connection to the url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Set request type
            urlConnection.setRequestMethod("GET");

            // Set read and connection timeout in milliseconds
            // Basically, setting how long to wait on the request
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);

            // Establish connection to the url
            urlConnection.connect();

            // Check for successful connection
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Connection successfully established
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error while connecting. Error Code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            e.getMessage();
            Log.e(LOG_TAG, "Problem encountered while retrieving book results");
        } finally {
            if (urlConnection != null) {
                // Disconnect the connection after successfully making the HTTP request
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Close the stream after successfully parsing the request
                // This may throw an IOException which is why it is explicitly mentioned in the
                // method signature
                inputStream.close();
            }
        }

        // Return JSON as a {@link String}
        return jsonResponse;
    }


    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            // Decode the bits
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));

            // Buffer the decoded characters
            BufferedReader reader = new BufferedReader(inputStreamReader);

            // Store a line of characters from the {@link BufferedReader}
            String line = reader.readLine();

            // If not end of buffered input stream, read next line and add to output
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }

        // Convert the mutable characters sequence from the builder into a string and return
        return output.toString();
    }


    private static List<News> extractFeatures(String newsJSON) {
        // Exit early if no data was returned from the HTTP request
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }


        // Initialize list of strings to hold the extracted books
        List<News> allBooks = new ArrayList<>();

        // Traverse the raw JSON response parameter and mine for relevant information
        try {
            // Create JSON object from response
            JSONObject rawJSONResponse = new JSONObject(newsJSON);
            JSONObject response=rawJSONResponse.getJSONObject("response");
            // Extract the array that holds the books
            JSONArray results = response.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                // Get the current book
                JSONObject SingleNews = results.getJSONObject(i);
                // Get the current book's volume information
                JSONObject fields = SingleNews.getJSONObject("fields");


                // Get the book's thumbnail from the volume information

                Bitmap image = null;

                String imageUrl = "";

                imageUrl = fields.getString("thumbnail");

                image = getImage(imageUrl);


                News item = new News( image);

                // Add book to the list
                allBooks.add(item);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing the google books JSON results", e);
        }

        // Return the successfully parsed book titles as a {@link List} object
        return allBooks;
    }

    private static Bitmap getImage(String imageUrl) {


        if (imageUrl == null) {
            return null;
        }
        URL Imagelink = createUrl(imageUrl);
        try {
            assert Imagelink != null;
            return BitmapFactory.decodeStream(Imagelink.openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }


}

package com.gretagrigute.moodformovies.network;


import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.gretagrigute.moodformovies.BuildConfig;
import com.gretagrigute.moodformovies.constants.TMDbApiConstants;
import com.gretagrigute.moodformovies.model.MovieVideo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

    /**
     * Created by Greta Grigutė on 2019-01-05.
     */
    public class NetworkUtilsMovieTrailer {

        private static final String TAG = NetworkUtilsMovieTrailer.class.getSimpleName();
        //please add the API key in the gradle.properties like this:
        //MoodForMovies_TMDbApiKey="your-key"
        private static final String apiKey = BuildConfig.ApiKey;
        private static final String language = "en-US";
        private static Uri.Builder uriBuilder;


        public static URL buildUrl(String id) {
            Uri completeUri;
            Uri baseUri = Uri.parse(TMDbApiConstants.TMDB_API_URL_BASE);

            // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
            uriBuilder = baseUri.buildUpon();

            // Append query parameter and its value. For example, the `sort_by = popularity.desc`
            uriBuilder.appendPath(id);
            uriBuilder.appendPath(TMDbApiConstants.VIDEOS);
            uriBuilder.appendQueryParameter(TMDbApiConstants.QUERRY_PARAMETER_LANGUAGE, language);
            uriBuilder.appendQueryParameter(TMDbApiConstants.QUERRY_PARAMETER_API_KEY, apiKey);

            completeUri = uriBuilder.build();


            URL url = null;
            try {
                url = new URL(completeUri.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            Log.d(TAG, "Built URI " + url);

            return url;
        }

        /**
         * This method returns the entire result from the HTTP response.
         *
         * @param url The URL to fetch the HTTP response from.
         * @return The contents of the HTTP response.
         * @throws IOException Related to network and stream reading
         */
        public static String getResponseFromHttpUrl(URL url) throws IOException {
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                InputStream in = urlConnection.getInputStream();

                Scanner scanner = new Scanner(in);
                scanner.useDelimiter("\\A");

                boolean hasInput = scanner.hasNext();
                if (hasInput) {
                    return scanner.next();
                } else {
                    return null;
                }
            } finally {
                urlConnection.disconnect();
            }
        }

        /**
         * Return a list of movies objects that has been built up from
         * parsing the given JSON response.
         */
        public static List<MovieVideo> extractFeatureFromJson(String moviesJSON) {
            // If the JSON string is empty or null, then return early.
            if (TextUtils.isEmpty(moviesJSON)) {
                return null;
            }

            // Create an empty ArrayList that we can start adding movies to
            List<MovieVideo> movieVideos = new ArrayList<>();

            // Try to parse the JSON response string. If there's a problem with the way the JSON
            // is formatted, a JSONException exception object will be thrown.
            // Catch the exception so the app doesn't crash, and print the error message to the logs.
            try {

                // Create a JSONObject from the JSON response string
                JSONObject baseJsonResponse = new JSONObject(moviesJSON);

                // Extract the JSONArray associated with the key called "results",
                // which represents a list of movie trailers.
                JSONArray results = baseJsonResponse.getJSONArray(TMDbApiConstants.RESULTS_ARRAY);

                // For each movie trailer in the movieVideos Array, create an {@link MovieVideo} object
                for (int i = 0; i < results.length(); i++) {

                    // Get a single movie video item at position i within the list of
                    JSONObject currentMovie = results.getJSONObject(i);

                    //Retrieve the field that you need from this json object:

                    // Extract the value for the key called "name"
                    String name = currentMovie.optString(TMDbApiConstants.VIDEO_NAME);

                    // Extract the value for the key called "key"
                    String key = currentMovie.optString(TMDbApiConstants.VIDEO_KEY);

                    // Extract the value for the "type"
                    String type = currentMovie.optString(TMDbApiConstants.VIDEO_TYPE);

                    // Create a new {@link MovieVideo} object with
                    MovieVideo movieVideo = new MovieVideo(name, key, type);

                    // Add the new {@link movieVideo} to the list of videos.
                    movieVideos.add(movieVideo);
                }

            } catch (JSONException e) {
                // If an error is thrown when executing any of the above statements in the "try" block,
                // catch the exception here, so the app doesn't crash. Print a log message
                // with the message from the exception.
                Log.e(TAG, "Problem parsing the TMDb JSON video trailers results", e);
            }

            // Return the list of movie videos
            return movieVideos;
        }
    }
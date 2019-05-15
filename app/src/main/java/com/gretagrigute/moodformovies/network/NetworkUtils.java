package com.gretagrigute.moodformovies.network;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.gretagrigute.moodformovies.BuildConfig;
import com.gretagrigute.moodformovies.constants.TMDbApiConstants;
import com.gretagrigute.moodformovies.model.MovieData;

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
public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();
    //please add the API key in the gradle.properties like this:
    //MoodForMovies_TMDbApiKey="your-key"
    private static final String apiKey = BuildConfig.ApiKey;
    private static final String language = "en-US";
    private static final String sort_by = "popularity.desc";
    private static final String page = "1";
    private static Uri.Builder uriBuilder;


    public static URL buildUrl(String choice) {
        Uri completeUri;
        switch (choice) {
            case TMDbApiConstants.POPULAR:
                Uri baseUriPopular = Uri.parse(TMDbApiConstants.TMDB_API_URL_POPULAR);

                // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
                uriBuilder = baseUriPopular.buildUpon();

                // Append query parameter and its value. For example, the `sort_by = popularity.desc`
                uriBuilder.appendQueryParameter(TMDbApiConstants.QUERRY_PARAMETER_API_KEY, apiKey);

                completeUri = uriBuilder.build();
                break;
            case TMDbApiConstants.TOP_RATED:
                Uri baseUriTopRated = Uri.parse(TMDbApiConstants.TMDB_API_URL_TOP_RATED);

                // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
                uriBuilder = baseUriTopRated.buildUpon();

                // Append query parameter and its value. For example, the `sort_by = popularity.desc`
                uriBuilder.appendQueryParameter(TMDbApiConstants.QUERRY_PARAMETER_API_KEY, apiKey);
                completeUri = uriBuilder.build();
                break;
            default:
                Uri baseUri = Uri.parse(TMDbApiConstants.TMDB_API_URL);

                // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
                uriBuilder = baseUri.buildUpon();

                // Append query parameter and its value. For example, the `sort_by = popularity.desc`
                uriBuilder.appendQueryParameter(TMDbApiConstants.QUERRY_PARAMETER_LANGUAGE, language);
                uriBuilder.appendQueryParameter(TMDbApiConstants.QUERRY_PARAMETER_SORT_BY, sort_by);
                uriBuilder.appendQueryParameter(TMDbApiConstants.QUERRY_PARAMETER_PAGE, page);
                uriBuilder.appendQueryParameter(TMDbApiConstants.QUERRY_PARAMETER_API_KEY, apiKey);

                completeUri = uriBuilder.build();
                break;
        }

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
    public static List<MovieData> extractFeatureFromJson(String moviesJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(moviesJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding movies to
        List<MovieData> movies = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(moviesJSON);

            // Extract the JSONArray associated with the key called "results",
            // which represents a list of articles.
            JSONArray results = baseJsonResponse.getJSONArray(TMDbApiConstants.RESULTS_ARRAY);

            // For each article in the articlesArray, create an {@link Article} object
            for (int i = 0; i < results.length(); i++) {

                // Get a single article at position i within the list of
                JSONObject currentMovie = results.getJSONObject(i);

                //Retrieve the field that you need from this json object:

                // Extract the value for the key called "id"
                int movieId = currentMovie.optInt(TMDbApiConstants.ID);

                // Extract the value for the key called "release_date"
                String releaseDate = currentMovie.optString(TMDbApiConstants.RELEASE_DATE);

                // Extract the value for the key called "title"
                String title = currentMovie.optString(TMDbApiConstants.TITLE);

                // Extract the value for the "vote_average"
                String voteAverage = currentMovie.optString(TMDbApiConstants.VOTE_AVERAGE);

                //Extract the value for the key "overview"
                String plotSynopsis = currentMovie.optString(TMDbApiConstants.PLOT_SYNOPSIS);

                // Extract the value for the key called "poster_path"
                String moviePosterPath = currentMovie.optString(TMDbApiConstants.MOVIE_POSTER);

                String moviePoster = "";

                if (!TextUtils.isEmpty(moviePosterPath)) {
                    moviePoster = TMDbApiConstants.IMAGE_URL_BASE_w92 + moviePosterPath;
                    Log.d(TAG, "Movie poster path is" + moviePoster);
                }

                // Create a new {@link MovieData} object with
                MovieData movieData = new MovieData(movieId, releaseDate, title, voteAverage, plotSynopsis, moviePoster);

                // Add the new {@link movieData} to the list of movies.
                movies.add(movieData);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(TAG, "Problem parsing the TMDb JSON results", e);
        }

        // Return the list of movies
        return movies;
    }
}

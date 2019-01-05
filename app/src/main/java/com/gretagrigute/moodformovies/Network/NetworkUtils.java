package com.gretagrigute.moodformovies.Network;

import android.net.Uri;
import android.util.Log;

import com.gretagrigute.moodformovies.BuildConfig;
import com.gretagrigute.moodformovies.Constants.TMDbApiConstants;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Greta Grigutė on 2019-01-05.
 */
public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static Uri.Builder uriBuilder;

    //please add the API key in the gradle.properties like this:
    //MoodForMovies_TMDbApiKey="your-key"
    private static final String apiKey = BuildConfig.ApiKey;
    private static final String language = "en-US";
    private static final String sort_by = "popularity.desc";
    private static final String page = "1";

    public static URL buildUrl() {

        Uri baseUri = Uri.parse(TMDbApiConstants.TMDB_API_URL);

        // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
        uriBuilder = baseUri.buildUpon();

        // Append query parameter and its value. For example, the `sort_by = popularity.desc`
        uriBuilder.appendQueryParameter(TMDbApiConstants.QUERRY_PARAMETER_LANGUAGE, language);
        uriBuilder.appendQueryParameter(TMDbApiConstants.QUERRY_PARAMETER_SORT_BY, sort_by);
        uriBuilder.appendQueryParameter(TMDbApiConstants.QUERRY_PARAMETER_PAGE, page);
        uriBuilder.appendQueryParameter(TMDbApiConstants.QUERRY_PARAMETER_API_KEY, apiKey);

       Uri completeUri = uriBuilder.build();

        URL url = null;
        try {
            url = new URL(completeUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

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
}

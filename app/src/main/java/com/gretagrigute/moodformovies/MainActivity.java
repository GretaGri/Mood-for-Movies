package com.gretagrigute.moodformovies;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gretagrigute.moodformovies.Constants.TMDbApiConstants;

public class MainActivity extends AppCompatActivity {

    private Uri.Builder uriBuilder;

    //please add the API key in the gradle.properties like this:
    //MoodForMovies_TMDbApiKey="your-key"
    String apiKey = BuildConfig.ApiKey;
    String language = "en-US";
    String sort_by = "popularity.desc";
    String page = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Uri baseUri = Uri.parse(TMDbApiConstants.TMDB_API_URL);

        // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
        uriBuilder = baseUri.buildUpon();

        // Append query parameter and its value. For example, the `sort_by = popularity.desc`
        uriBuilder.appendQueryParameter(TMDbApiConstants.QUERRY_PARAMETER_LANGUAGE, language);
        uriBuilder.appendQueryParameter(TMDbApiConstants.QUERRY_PARAMETER_SORT_BY, sort_by);
        uriBuilder.appendQueryParameter(TMDbApiConstants.QUERRY_PARAMETER_PAGE, page);
        uriBuilder.appendQueryParameter(TMDbApiConstants.QUERRY_PARAMETER_API_KEY, apiKey);
    }
}

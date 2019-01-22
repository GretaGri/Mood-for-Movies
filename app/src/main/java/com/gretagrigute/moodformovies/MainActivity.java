package com.gretagrigute.moodformovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gretagrigute.moodformovies.Constants.Constants;
import com.gretagrigute.moodformovies.Constants.TMDbApiConstants;
import com.gretagrigute.moodformovies.Model.MovieData;
import com.gretagrigute.moodformovies.Network.NetworkUtils;
import com.gretagrigute.moodformovies.UI.ListFragment;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String choice;
    private TextView noConnection;
    private ProgressBar loadingSpinner;
    private FrameLayout fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noConnection = findViewById(R.id.tv_no_connection);
        loadingSpinner = findViewById(R.id.loading_spinner);
        fragment = findViewById(R.id.fragment);

        choice = "first_page";
        new DownloadMoviesTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.main_activity_settings_menu, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    // Override onOptionsItemSelected to handle clicks on the refresh button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_show_popular) {
            choice = TMDbApiConstants.POPULAR;
            new DownloadMoviesTask().execute();
            return true;
        }

        if (id == R.id.action_show_top_rated) {
            choice = TMDbApiConstants.TOP_RATED;
            new DownloadMoviesTask().execute();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class DownloadMoviesTask extends AsyncTask<URL, Integer, String> {
        protected String doInBackground(URL... urls) {
            URL movieUrl = NetworkUtils.buildUrl(choice);
            String result = "";

            if (movieUrl != null) {
                try {
                    result = NetworkUtils.getResponseFromHttpUrl(movieUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return result;
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(String result) {
            ArrayList<MovieData> moviesList = (ArrayList<MovieData>) NetworkUtils.extractFeatureFromJson(result);

            if (moviesList != null) {
                noConnection.setVisibility(View.GONE);
                loadingSpinner.setVisibility(View.VISIBLE);
                Fragment listFragment = new ListFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(Constants.PARCELABLE, moviesList);
                listFragment.setArguments(bundle);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.fragment, listFragment);
                fragmentTransaction.commit();
                loadingSpinner.setVisibility(View.GONE);
                fragment.setVisibility(View.VISIBLE);

            } else {
                Log.d("MainActivity", "no connection");
                noConnection.setVisibility(View.VISIBLE);
                loadingSpinner.setVisibility(View.GONE);
                fragment.setVisibility(View.GONE);

            }
        }
    }
}


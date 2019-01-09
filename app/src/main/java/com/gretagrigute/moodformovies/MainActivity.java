package com.gretagrigute.moodformovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gretagrigute.moodformovies.Constants.TMDbApiConstants;
import com.gretagrigute.moodformovies.Model.MovieData;
import com.gretagrigute.moodformovies.Network.NetworkUtils;
import com.gretagrigute.moodformovies.UI.RecyclerViewAdapter;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    String choice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);

        choice = "first_page";

        new DownloadMoviesTask().execute();
    }

    private class DownloadMoviesTask extends AsyncTask<URL, Integer, String> {
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
            List<MovieData> moviesList = NetworkUtils.extractFeatureFromJson(result);
            if (moviesList != null) {
                RecyclerViewAdapter adapter = new RecyclerViewAdapter(moviesList);
                recyclerView.setAdapter(adapter);
            }
        }
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
}


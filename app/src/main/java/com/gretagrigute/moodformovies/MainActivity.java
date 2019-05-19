package com.gretagrigute.moodformovies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.gretagrigute.moodformovies.constants.Constants;
import com.gretagrigute.moodformovies.constants.TMDbApiConstants;
import com.gretagrigute.moodformovies.data.AppDataBase;
import com.gretagrigute.moodformovies.data.MovieDao;
import com.gretagrigute.moodformovies.data.MovieEntity;
import com.gretagrigute.moodformovies.model.MovieData;
import com.gretagrigute.moodformovies.network.NetworkUtils;
import com.gretagrigute.moodformovies.ui.ListFragment;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String choice;
    private TextView noConnection;
    private ProgressBar loadingSpinner;
    private FrameLayout fragment;
    ArrayList <MovieData> moviesList;

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

        if (id == R.id.action_show_favorite) {
            //Download favorite movies from Movie database
            downloadMoviesFromDatabase();
            // Update the cached copy of the words in the adapter.
            getFragmentWithNewList(moviesList);
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
            getFragmentWithNewList(moviesList);
        }
    }

    private void getFragmentWithNewList(ArrayList <MovieData> moviesList) {
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

    private void downloadMoviesFromDatabase(){
        AppDataBase db = AppDataBase.getInstance(this);
        final MovieDao movieDao = db.movieDao();
        Log.d("TAG", "Actively retrieving the tasks from the DataBase");
        final LiveData<List<MovieData>> movies = movieDao.loadAllMovies();
        movies.observe(this, new Observer<List<MovieData>>() {
                   @Override
                   public void onChanged(@Nullable final List<MovieData> movies) {
                       getMovieList((ArrayList) movies);
                    }
               });
            }

    private void getMovieList(ArrayList movies) {
      moviesList = movies;
    }
}


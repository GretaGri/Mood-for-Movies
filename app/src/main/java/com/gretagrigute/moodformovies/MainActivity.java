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
import android.widget.Toast;

import com.gretagrigute.moodformovies.constants.Constants;
import com.gretagrigute.moodformovies.constants.TMDbApiConstants;
import com.gretagrigute.moodformovies.data.AppDataBase;
import com.gretagrigute.moodformovies.data.MovieDao;
import com.gretagrigute.moodformovies.model.MovieData;
import com.gretagrigute.moodformovies.network.NetworkUtils;
import com.gretagrigute.moodformovies.ui.DetailsFragment;
import com.gretagrigute.moodformovies.ui.ListFragment;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ListFragment.OnMovieClickListener {

    private String choice;
    private TextView noConnection;
    private ProgressBar loadingSpinner;
    private FrameLayout fragment;
    private ArrayList<MovieData> moviesList;
    private MovieData movie;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noConnection = findViewById(R.id.tv_no_connection);
        loadingSpinner = findViewById(R.id.loading_spinner);
        fragment = findViewById(R.id.fragment);

        choice = TMDbApiConstants.CHOICE;
        if (savedInstanceState == null) {
            new DownloadMoviesTask().execute();
            downloadMoviesFromDatabase();
        } else {
            choice = savedInstanceState.getString(TMDbApiConstants.CHOICE);
            moviesList = savedInstanceState.getParcelableArrayList(TMDbApiConstants.MOVIE_LIST);
            movie = savedInstanceState.getParcelable(Constants.MOVIE);
            id = savedInstanceState.getInt(Constants.MOVIE_ID);

            if (choice.equals(TMDbApiConstants.POPULAR)) {
                new DownloadMoviesTask().execute();
                setTitle(getResources().getString(R.string.popular_label));
            } else if (choice.equals(TMDbApiConstants.TOP_RATED)){
                new DownloadMoviesTask().execute();
                setTitle(getResources().getString(R.string.top_rated_label));
            } else if (choice.equals(TMDbApiConstants.FAVORITE)) {
                setTitle(getResources().getString(R.string.favorite_label));
                //Download favorite movies from Movie database
                downloadMoviesFromDatabase();
                // Update the cached copy of the words in the adapter.
                getFragmentWithNewList(moviesList);
            }
        else if (choice.equals(TMDbApiConstants.DETAIL_PAGE)){
                Fragment detailsFragment = new DetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constants.MOVIE, movie);
                bundle.putInt(Constants.MOVIE_ID, id);
                detailsFragment.setArguments(bundle);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, detailsFragment).addToBackStack("tag");
                fragmentTransaction.commit();
            }}}

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
        switch (item.getItemId()) {
            case R.id.action_show_popular:
                setTitle(getResources().getString(R.string.popular_label));
                choice = TMDbApiConstants.POPULAR;
                new DownloadMoviesTask().execute();
                return true;

            case R.id.action_show_top_rated:
                setTitle(getResources().getString(R.string.top_rated_label));
                choice = TMDbApiConstants.TOP_RATED;
                new DownloadMoviesTask().execute();
                return true;

            case R.id.action_show_favorite:
                setTitle(getResources().getString(R.string.favorite_label));
                choice = TMDbApiConstants.FAVORITE;
                //Download favorite movies from Movie database
                downloadMoviesFromDatabase();
                if (moviesList.size() == 0) {
                    Toast.makeText(getApplicationContext(),
                            getApplicationContext().getString(R.string.no_movies_in_list),
                            Toast.LENGTH_LONG).show();
                }

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMovieSelected(int position, MovieData movie) {
        choice = TMDbApiConstants.DETAIL_PAGE;
        this.movie = movie;
        id = movie.getId();
        Log.d ("TAG_MainActivity", "id is set in main activity and is: " + id);
        Log.d ("TAG_MainActivity", "moviesList is set in main activity and is: " + movie.toString());
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
                    Toast.makeText(getApplicationContext(),
                            getApplicationContext().getString(R.string.no_movies_in_list),
                            Toast.LENGTH_LONG).show();
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

    private void getFragmentWithNewList(ArrayList<MovieData> moviesList) {
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

    private void downloadMoviesFromDatabase() {
        AppDataBase db = AppDataBase.getInstance(this);
        if(db != null) {
            final MovieDao movieDao = db.movieDao();
            Log.d("TAG", "Actively retrieving the tasks from the DataBase");
            final LiveData<List<MovieData>> movies = movieDao.loadAllMovies();
            movies.observe(this, new Observer<List<MovieData>>() {
                @Override
                public void onChanged(@Nullable final List<MovieData> movies) {
                    getMovieList((ArrayList) movies);
                }
            });
            // Update the cached copy of the words in the adapter.
            getFragmentWithNewList(moviesList);
        }
   }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TMDbApiConstants.CHOICE, choice);
        outState.putParcelableArrayList(TMDbApiConstants.MOVIE_LIST, moviesList);
        outState.putParcelable(Constants.MOVIE, movie);
        outState.putInt(Constants.MOVIE_ID, id);
    }

    private void getMovieList(ArrayList movies) {
        moviesList = movies;
    }
}


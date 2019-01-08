package com.gretagrigute.moodformovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gretagrigute.moodformovies.Model.MovieData;
import com.gretagrigute.moodformovies.Network.NetworkUtils;
import com.gretagrigute.moodformovies.UI.RecyclerViewAdapter;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);

        new DownloadMoviesTask().execute();
    }

    private class DownloadMoviesTask extends AsyncTask<URL, Integer, String> {
        protected String doInBackground(URL... urls) {
            URL movieUrl = NetworkUtils.buildUrl();
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
}


package com.gretagrigute.moodformovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.gretagrigute.moodformovies.Network.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       textView = (TextView) findViewById(R.id.text);

        new DownloadMoviesTask().execute();
    }

    private class DownloadMoviesTask extends AsyncTask<URL, Integer, String> {
        protected String doInBackground(URL... urls) {
            URL movieUrl = NetworkUtils.buildUrl();
            String result = "";

            if (movieUrl!=null){
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
        textView.setText(result);
        }
    }
}

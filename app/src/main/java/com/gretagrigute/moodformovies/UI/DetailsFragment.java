package com.gretagrigute.moodformovies.UI;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gretagrigute.moodformovies.Constants.Constants;
import com.gretagrigute.moodformovies.MainActivity;
import com.gretagrigute.moodformovies.Model.MovieData;
import com.gretagrigute.moodformovies.R;

import java.util.ArrayList;

/**
 * Created by Greta Grigutė on 2019-01-05.
 */
public class DetailsFragment extends Fragment {

        ArrayList<MovieData> moviesList;
        Integer id;
    ImageView posterImageView;
    TextView titleTextView;
    TextView dateTextView;
    TextView averageVoteTextView;
    TextView plotTextView;

    public DetailsFragment() {
            // Required empty public constructor
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            moviesList = getArguments().getParcelableArrayList(Constants.PARCELABLE);
            id = getArguments().getInt(Constants.MOVIE_ID);
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View itemView = inflater.inflate(R.layout.fragment_movies_details, container, false);

            posterImageView = (ImageView) itemView.findViewById(R.id.iv_image);
            titleTextView = (TextView) itemView.findViewById(R.id.tv_title);
            dateTextView = (TextView) itemView.findViewById(R.id.tv_date);
            averageVoteTextView = (TextView) itemView.findViewById(R.id.tv_average_vote);
            plotTextView = (TextView) itemView.findViewById(R.id.tv_plot);

            if (moviesList != null&&id!=-1) {
                String moviePoster = moviesList.get(id).getMoviePoster();
                Glide.with(getActivity()).load(moviePoster).into(posterImageView);
                titleTextView.setText(moviesList.get(id).getVoteAverage());
                titleTextView.setSelected(true);
                titleTextView.setText(moviesList.get(id).getTitle());
                dateTextView.setText(moviesList.get(id).getReleaseDate());
                plotTextView.setText(moviesList.get(id).getPlotSynopsis());
            }

            return itemView;
        }
    }


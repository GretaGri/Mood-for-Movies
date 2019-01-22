package com.gretagrigute.moodformovies.UI;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gretagrigute.moodformovies.Constants.Constants;
import com.gretagrigute.moodformovies.Model.MovieData;
import com.gretagrigute.moodformovies.R;

import java.util.ArrayList;

/**
 * Created by Greta Grigutė on 2019-01-05.
 */
public class DetailsFragment extends Fragment {

    private ArrayList<MovieData> moviesList;
    private Integer id;
    private ImageView posterImageView;
    private TextView titleTextView;
    private TextView dateTextView;
    private TextView averageVoteTextView;
    private TextView plotTextView;

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

        posterImageView = itemView.findViewById(R.id.iv_image);
        titleTextView = itemView.findViewById(R.id.tv_title);
        dateTextView = itemView.findViewById(R.id.tv_date);
        averageVoteTextView = itemView.findViewById(R.id.tv_average_vote);
        plotTextView = itemView.findViewById(R.id.tv_plot);

        if (moviesList != null && id != -1) {
            String moviePoster = moviesList.get(id).getMoviePoster();
            GlideApp.with(getActivity())
                    .load(moviePoster)
                    .placeholder(R.drawable.placeholder)
                    .into(posterImageView);
            averageVoteTextView.setText(moviesList.get(id).getVoteAverage());
            titleTextView.setSelected(true);
            titleTextView.setText(moviesList.get(id).getTitle());
            dateTextView.setText(moviesList.get(id).getReleaseDate());
            plotTextView.setText(moviesList.get(id).getPlotSynopsis());
        }

        return itemView;
    }
}


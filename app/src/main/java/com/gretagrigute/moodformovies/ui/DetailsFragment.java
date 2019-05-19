package com.gretagrigute.moodformovies.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.gretagrigute.moodformovies.AppExecutors;
import com.gretagrigute.moodformovies.constants.Constants;
import com.gretagrigute.moodformovies.data.AppDataBase;
import com.gretagrigute.moodformovies.data.MovieDao;
import com.gretagrigute.moodformovies.model.MovieData;
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
        final ImageButton button = itemView.findViewById(R.id.imageButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        AppDataBase db = AppDataBase.getInstance(getContext());
                        MovieDao movieDao = db.movieDao();

                        //to set is Favorite to true
                        if (moviesList.get(id).getIsFavorite() == Constants.IS_FAVORITE_FALSE) {
                            MovieData favoriteMovie = moviesList.get(id);
                            favoriteMovie.setIsFavorite(Constants.IS_FAVORITE_TRUE);
                            movieDao.insertMovie(favoriteMovie);
                            AppExecutors.getInstance().mainThread().execute(new Runnable() {
                                @Override
                                public void run() {
                                    button.setImageDrawable(getResources().getDrawable(R.drawable.ic_star));
                                }
                            });
                        } else {
                            movieDao.deleteMovie(moviesList.get(id));
                            AppExecutors.getInstance().mainThread().execute(new Runnable() {
                                @Override
                                public void run() {
                                    button.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_empty));
                                }
                            });
                        }
                    }
                });
            }
        });

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
            if (moviesList.get(id).getIsFavorite() == Constants.IS_FAVORITE_FALSE){
                button.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_empty));
            } else {button.setImageDrawable(getResources().getDrawable(R.drawable.ic_star));}
        }

        return itemView;
    }
}


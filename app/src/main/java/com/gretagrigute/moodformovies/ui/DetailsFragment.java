package com.gretagrigute.moodformovies.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.gretagrigute.moodformovies.AppExecutors;
import com.gretagrigute.moodformovies.R;
import com.gretagrigute.moodformovies.constants.Constants;
import com.gretagrigute.moodformovies.data.AppDataBase;
import com.gretagrigute.moodformovies.data.MovieDao;
import com.gretagrigute.moodformovies.model.MovieData;
import com.gretagrigute.moodformovies.model.MovieReview;
import com.gretagrigute.moodformovies.model.MovieVideo;
import com.gretagrigute.moodformovies.network.NetworkUtilsMovieReview;
import com.gretagrigute.moodformovies.network.NetworkUtilsMovieTrailer;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Greta Grigutė on 2019-01-05.
 */
public class DetailsFragment extends Fragment {

    private ArrayList<MovieData> moviesList;
    private MovieData movie;
    private Integer position;
    private Integer id;
    private ImageView posterImageView;
    private TextView titleTextView;
    private TextView dateTextView;
    private TextView averageVoteTextView;
    private TextView plotTextView;
    private RecyclerView rwMoviesVideos;
    private RecyclerView rwMoviesReview;
    private ArrayList<MovieVideo> moviesVideoList;

    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        moviesList = getArguments().getParcelableArrayList(Constants.PARCELABLE);
        position = getArguments().getInt(Constants.MOVIE_ID);
        movie = moviesList.get(position);

        id = moviesList.get(position).getId();
        Log.d("TAG", "Id is:" + id);
//        AppExecutors.getInstance().networkIO().execute(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        View itemView = inflater.inflate(R.layout.fragment_movies_details, container, false);

        posterImageView = itemView.findViewById(R.id.iv_image);
        titleTextView = itemView.findViewById(R.id.tv_title);
        dateTextView = itemView.findViewById(R.id.tv_date);
        averageVoteTextView = itemView.findViewById(R.id.tv_average_vote);
        plotTextView = itemView.findViewById(R.id.tv_plot);
        rwMoviesReview = itemView.findViewById(R.id.reviews_list);
        rwMoviesVideos = itemView.findViewById(R.id.trailer_list);

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
                        if (movie.getIsFavorite() == Constants.IS_FAVORITE_FALSE) {
                            movie.setIsFavorite(Constants.IS_FAVORITE_TRUE);
                            movieDao.insertMovie(movie);
                            AppExecutors.getInstance().mainThread().execute(new Runnable() {
                                @Override
                                public void run() {
                                    button.setImageDrawable(getResources().getDrawable(R.drawable.ic_star));
                                }
                            });
                        } else {
                            movie.setIsFavorite(Constants.IS_FAVORITE_FALSE);
                            movieDao.deleteMovie(movie);
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

        if (moviesList != null && position != -1) {
            String moviePoster = movie.getMoviePoster();
            GlideApp.with(getActivity())
                    .load(moviePoster)
                    .placeholder(R.drawable.placeholder)
                    .into(posterImageView);
            averageVoteTextView.setText(movie.getVoteAverage());
            titleTextView.setSelected(true);
            titleTextView.setText(movie.getTitle());
            dateTextView.setText(movie.getReleaseDate());
            plotTextView.setText(movie.getPlotSynopsis());
            if (movie.getIsFavorite() == Constants.IS_FAVORITE_FALSE) {
                button.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_empty));
            } else {
                button.setImageDrawable(getResources().getDrawable(R.drawable.ic_star));
            }
        }


        AppExecutors.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                URL movieUrl = NetworkUtilsMovieTrailer.buildUrl(id.toString());
                String result = "";

                if (movieUrl != null) {
                    try {
                        result = NetworkUtilsMovieTrailer.getResponseFromHttpUrl(movieUrl);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                final ArrayList<MovieVideo> moviesVideoList = (ArrayList<MovieVideo>) NetworkUtilsMovieTrailer.extractFeatureFromJson(result);
                if (moviesVideoList != null) {
                    AppExecutors.getInstance().mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                            rwMoviesVideos.setLayoutManager(layoutManager);
                            MovieTrailerAdapter adapter = new MovieTrailerAdapter(moviesVideoList);
                            rwMoviesVideos.setAdapter(adapter);
                        }
                    });
                }
            }
        });

        AppExecutors.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                URL movieUrl = NetworkUtilsMovieReview.buildUrl(id.toString());
                String result = "";

                if (movieUrl != null) {
                    try {
                        result = NetworkUtilsMovieReview.getResponseFromHttpUrl(movieUrl);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                final ArrayList<MovieReview> moviesReviewList = (ArrayList<MovieReview>) NetworkUtilsMovieReview.extractFeatureFromJson(result);
                if (moviesReviewList != null) {
                    AppExecutors.getInstance().mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                            rwMoviesReview.setLayoutManager(layoutManager);
                            MovieReviewAdapter adapter = new MovieReviewAdapter(moviesReviewList);
                            rwMoviesReview.setAdapter(adapter);
                        }
                    });
                }
            }
        });

        return itemView;
    }
}


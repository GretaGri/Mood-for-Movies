package com.gretagrigute.moodformovies.UI;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gretagrigute.moodformovies.Model.MovieData;
import com.gretagrigute.moodformovies.R;

import java.util.List;

/**
 * Created by Greta Grigutė on 2019-01-05.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewAdapterViewHolder> {
    private List<MovieData> movieList;
    private Context context;

    //Empty constructor
    public RecyclerViewAdapter(List<MovieData> movieList) {
        this.movieList = movieList;
    }


    @NonNull
    @Override
    public RecyclerViewAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movies_details_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new RecyclerViewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterViewHolder recyclerViewAdapterViewHolder, int i) {
        String moviePoster = movieList.get(i).getMoviePoster();
        Glide.with(context).load(moviePoster).into(recyclerViewAdapterViewHolder.posterImageView);
        recyclerViewAdapterViewHolder.averageVoteTextView.setText(movieList.get(i).getVoteAverage());
        recyclerViewAdapterViewHolder.titleTextView.setSelected(true);
        recyclerViewAdapterViewHolder.titleTextView.setText(movieList.get(i).getTitle());
        recyclerViewAdapterViewHolder.dateTextView.setText(movieList.get(i).getReleaseDate());
        recyclerViewAdapterViewHolder.plotTextView.setText(movieList.get(i).getPlotSynopsis());
    }

    @Override
    public int getItemCount() {
        return movieList == null ? 0 : movieList.size();
    }

    public class RecyclerViewAdapterViewHolder extends RecyclerView.ViewHolder {

        public final ImageView posterImageView;
        public final TextView titleTextView;
        public final TextView dateTextView;
        public final TextView averageVoteTextView;
        public final TextView plotTextView;

        public RecyclerViewAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            posterImageView = (ImageView) itemView.findViewById(R.id.iv_image);
            titleTextView = (TextView) itemView.findViewById(R.id.tv_title);
            dateTextView = (TextView) itemView.findViewById(R.id.tv_date);
            averageVoteTextView = (TextView) itemView.findViewById(R.id.tv_average_vote);
            plotTextView = (TextView) itemView.findViewById(R.id.tv_plot);
        }
    }
}

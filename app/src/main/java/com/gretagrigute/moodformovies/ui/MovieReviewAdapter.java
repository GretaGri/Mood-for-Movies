package com.gretagrigute.moodformovies.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gretagrigute.moodformovies.R;
import com.gretagrigute.moodformovies.model.MovieReview;

import java.util.ArrayList;
import java.util.List;

public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.RecyclerViewAdapterViewHolder> {
    private List<MovieReview> movieReviewList;
    private Context context;

    //Empty constructor
    public MovieReviewAdapter(ArrayList<MovieReview> movieReviewList) {
        this.movieReviewList = movieReviewList;
    }


    @NonNull
    @Override
    public RecyclerViewAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_reviews_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new RecyclerViewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterViewHolder recyclerViewAdapterViewHolder, int i) {
        recyclerViewAdapterViewHolder.author.setText(movieReviewList.get(i).getAuthor());
        Log.d ("LOG_MovieTrailerAdapter","review author is: " + movieReviewList.get(i).getAuthor());
        recyclerViewAdapterViewHolder.content.setText(movieReviewList.get(i).getContent());
        Log.d ("LOG_MovieTrailerAdapter","review content is: " + movieReviewList.get(i).getContent());
    }

    @Override
    public int getItemCount() {
        return movieReviewList == null ? 0 : movieReviewList.size();
    }

    public class RecyclerViewAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView author;
        public final TextView content;

        public RecyclerViewAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.tv_author);
            content = itemView.findViewById(R.id.tv_content);
        }

        @Override
        public void onClick(View v) {
        }
    }
}
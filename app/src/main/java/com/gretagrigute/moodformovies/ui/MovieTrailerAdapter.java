package com.gretagrigute.moodformovies.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gretagrigute.moodformovies.R;
import com.gretagrigute.moodformovies.model.MovieVideo;

import java.util.ArrayList;
import java.util.List;

public class MovieTrailerAdapter extends RecyclerView.Adapter<MovieTrailerAdapter.RecyclerViewAdapterViewHolder> {
    private List<MovieVideo> movieVideoList;
    private Context context;

    //Empty constructor
    public MovieTrailerAdapter(ArrayList<MovieVideo> movieVideoList) {
        this.movieVideoList = movieVideoList;
    }


    @NonNull
    @Override
    public RecyclerViewAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_trailer_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new RecyclerViewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterViewHolder recyclerViewAdapterViewHolder, int i) {
        recyclerViewAdapterViewHolder.title.setText(movieVideoList.get(i).getName());
        Log.d("LOG_MovieTrailerAdapter", "movie name is: " + movieVideoList.get(i).getName());
        recyclerViewAdapterViewHolder.type.setText(movieVideoList.get(i).getType());
        Log.d("LOG_MovieTrailerAdapter", "movie type is: " + movieVideoList.get(i).getType());
    }

    @Override
    public int getItemCount() {
        return movieVideoList == null ? 0 : movieVideoList.size();
    }

    public class RecyclerViewAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView title;
        public final TextView type;
        public final Button button_open;

        public RecyclerViewAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_video_title);
            type = itemView.findViewById(R.id.tv_video_type);
            button_open = itemView.findViewById(R.id.button_open_trailer);
            button_open.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String key = movieVideoList.get(getLayoutPosition()).getKey();
            String url = "https://youtu.be/" + key;
            Log.d("LOG_MovieTrailerAdapter", "Youtube url is: " + url);
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            v.getContext().startActivity(i);
        }
    }
}
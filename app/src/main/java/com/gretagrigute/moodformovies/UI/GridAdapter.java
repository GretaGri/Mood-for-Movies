package com.gretagrigute.moodformovies.UI;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gretagrigute.moodformovies.Constants.Constants;
import com.gretagrigute.moodformovies.Model.MovieData;
import com.gretagrigute.moodformovies.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Greta Grigutė on 2019-01-13.
 */
public class GridAdapter extends RecyclerView.Adapter<GridAdapter.RecyclerViewAdapterViewHolder> {
    private List<MovieData> movieList;
    private Context context;

    //Empty constructor
    public GridAdapter(ArrayList<MovieData> movieList) {
        this.movieList = movieList;
    }


    @NonNull
    @Override
    public RecyclerViewAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_poster_grid_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new RecyclerViewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterViewHolder recyclerViewAdapterViewHolder, int i) {
        String moviePoster = movieList.get(i).getMoviePoster();
        Glide.with(context).load(moviePoster).into(recyclerViewAdapterViewHolder.posterImageView);
    }

    @Override
    public int getItemCount() {
        return movieList == null ? 0 : movieList.size();
    }

    public class RecyclerViewAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView posterImageView;
        public final FrameLayout frameLayout;

        public RecyclerViewAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            posterImageView = (ImageView) itemView.findViewById(R.id.iv_image);
            posterImageView.setOnClickListener(this);
            frameLayout = (FrameLayout) itemView.findViewById(R.id.fragment_list);
        }

        @Override
        public void onClick(View v) {
            Integer id = getLayoutPosition();
            ArrayList<MovieData> moviesList = (ArrayList<MovieData>) movieList;
            Fragment detailsFragment = new DetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(Constants.PARCELABLE, moviesList);
            bundle.putInt(Constants.MOVIE_ID,id);
            detailsFragment.setArguments(bundle);
            Log.d("GridAdapter","id is: "+ id);
            FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment, detailsFragment);
            fragmentTransaction.commit();
        }
    }
}





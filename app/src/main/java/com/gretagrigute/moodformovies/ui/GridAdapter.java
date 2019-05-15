package com.gretagrigute.moodformovies.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.gretagrigute.moodformovies.constants.Constants;
import com.gretagrigute.moodformovies.model.MovieData;
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
        GlideApp.with(context)
                .load(moviePoster)
                .placeholder(R.drawable.placeholder)
                .into(recyclerViewAdapterViewHolder.posterImageView);
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
            posterImageView = itemView.findViewById(R.id.iv_image);
            posterImageView.setOnClickListener(this);
            frameLayout = itemView.findViewById(R.id.fragment_list);
        }

        @Override
        public void onClick(View v) {
            int id = getLayoutPosition();
            ArrayList<MovieData> moviesList = (ArrayList<MovieData>) movieList;
            Fragment detailsFragment = new DetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(Constants.PARCELABLE, moviesList);
            bundle.putInt(Constants.MOVIE_ID, id);
            detailsFragment.setArguments(bundle);
            FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment, detailsFragment).addToBackStack( "tag" );
            fragmentTransaction.commit();
        }
    }
}





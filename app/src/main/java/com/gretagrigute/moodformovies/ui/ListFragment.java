package com.gretagrigute.moodformovies.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.gretagrigute.moodformovies.constants.Constants;
import com.gretagrigute.moodformovies.model.MovieData;
import com.gretagrigute.moodformovies.R;

import java.util.ArrayList;

/**
 * Created by Greta Grigutė on 2019-01-05.
 */
public class ListFragment extends Fragment implements GridAdapter.OnItemClickListener{

    private RecyclerView recyclerView;
    private ArrayList<MovieData> moviesList;
    // Define a new interface OnMovieClickListener that triggers a callback in the host activity
    OnMovieClickListener callback;

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        moviesList = getArguments().getParcelableArrayList(Constants.PARCELABLE);

    }

    @Override
    public void onItemClick(int position) {
        // Trigger the callback method and pass in the position that was clicked
        callback.onMovieSelected(position, moviesList);
        Log.d("ListFragment", "movie selected id is:" + position);
    }

    // OnMovieClickListener interface, calls a method in the host activity named onMovieSelected
    public interface OnMovieClickListener {
        void onMovieSelected(int position, ArrayList <MovieData> moviesList);
    }

    // Override onAttach to make sure that the container activity has implemented the callback
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            callback = (OnMovieClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnMovieClickListener");
        }
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main_list, container, false);
        recyclerView = rootView.findViewById(R.id.recycler_view);

        int numberOfColumns = 2;
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), numberOfColumns);
        recyclerView.setLayoutManager(layoutManager);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getActivity(), R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);

        if (moviesList != null) {
            GridAdapter adapter = new GridAdapter(moviesList,this);
            recyclerView.setAdapter(adapter);
        }
        return rootView;
    }
}

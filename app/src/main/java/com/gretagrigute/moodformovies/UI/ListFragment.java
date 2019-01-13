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

import com.gretagrigute.moodformovies.Constants.Constants;
import com.gretagrigute.moodformovies.MainActivity;
import com.gretagrigute.moodformovies.Model.MovieData;
import com.gretagrigute.moodformovies.R;

import java.util.ArrayList;

/**
 * Created by Greta Grigutė on 2019-01-05.
 */
public class ListFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<MovieData> moviesList;

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        moviesList = getArguments().getParcelableArrayList(Constants.PARCELABLE);

        }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main_list, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        int numberOfColumns = 3;
        GridLayoutManager layoutManager = new GridLayoutManager (getActivity(), numberOfColumns);
        recyclerView.setLayoutManager(layoutManager);

        if (moviesList != null) {
            GridAdapter adapter = new GridAdapter(moviesList);
            recyclerView.setAdapter(adapter);
        }

        return rootView;
    }

}

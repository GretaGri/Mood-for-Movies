package com.gretagrigute.moodformovies.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.gretagrigute.moodformovies.model.MovieData;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT*FROM movie_table")
    LiveData<List<MovieData>> loadAllMovies();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovie(MovieData movie);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(MovieData movie);

    @Delete
    void deleteMovie(MovieData movie);

    @Query("SELECT * FROM movie_table WHERE id = :id")
    LiveData<MovieData> loadMovieById(int id);

}

package com.gretagrigute.moodformovies.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT*FROM movie_table")
    LiveData <List<MovieEntity>> loadAllMovies();

    @Insert
    void insertMovie (MovieEntity movie);

    @Update (onConflict = OnConflictStrategy.REPLACE)
    void updateMovie (MovieEntity movie);

    @Delete
    void deleteMovie (MovieEntity movie);

    @Query("SELECT * FROM movie_table WHERE id = :id")
    LiveData<MovieEntity> loadMovieById (int id);

}

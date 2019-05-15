package com.gretagrigute.moodformovies.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "movie_table")
public class MovieEntity {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private final int movieId;
    private final String movieTitle;
    private final String synopsis;
    private final String userRating;
    private final String moviePoster;
    private final int isFavorite;

    public MovieEntity(int movieId, String movieTitle, String synopsis,
                       String userRating, String moviePoster, int isFavorite) {
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.synopsis = synopsis;
        this.userRating = userRating;
        this.moviePoster = moviePoster;
        this.isFavorite = isFavorite;
    }

    public int getMovieId() {
        return movieId;
    }

    public String getMovieTitle(){
        return this.movieTitle;}

    public String getUserRating() {
        return userRating;
    }

    public String getPlotSynopsis() {
        return synopsis;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public int getIsFavorite() {
        return isFavorite;
    }

}

package com.gretagrigute.moodformovies.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.gretagrigute.moodformovies.constants.Constants;

/**
 * Created by Greta Grigutė on 2019-01-05.
 */
@Entity(tableName = "movie_table")
public class MovieData implements Parcelable {
    public static final Creator<MovieData> CREATOR = new Creator<MovieData>() {
        @Override
        public MovieData createFromParcel(Parcel in) {
            return new MovieData(in);
        }

        @Override
        public MovieData[] newArray(int size) {
            return new MovieData[size];
        }
    };
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private int id;
    private String releaseDate;
    private String title;
    private String voteAverage;
    private String plotSynopsis;
    private String moviePoster;
    @ColumnInfo(name = "favorite")
    private int isFavorite = Constants.IS_FAVORITE_FALSE;

    public MovieData(int id, String releaseDate, String title, String voteAverage, String plotSynopsis, String moviePoster, int isFavorite) {
        this.id = id;
        this.releaseDate = releaseDate;
        this.title = title;
        this.voteAverage = voteAverage;
        this.plotSynopsis = plotSynopsis;
        this.moviePoster = moviePoster;
        this.isFavorite = isFavorite;
    }

    @Ignore
    public MovieData(int id, String releaseDate, String title, String voteAverage, String plotSynopsis, String moviePoster) {
        this.id = id;
        this.releaseDate = releaseDate;
        this.title = title;
        this.voteAverage = voteAverage;
        this.plotSynopsis = plotSynopsis;
        this.moviePoster = moviePoster;
    }

    protected MovieData(Parcel in) {
        id = in.readInt();
        releaseDate = in.readString();
        title = in.readString();
        voteAverage = in.readString();
        plotSynopsis = in.readString();
        moviePoster = in.readString();
        isFavorite = in.readInt();
    }

    public int getId() {
        return id;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public int getIsFavorite() {
        return isFavorite;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }

    public void setMoviePoster(String moviePoster) {
        this.moviePoster = moviePoster;
    }

    public void setIsFavorite(int isFavorite) {
        this.isFavorite = isFavorite;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id" + id +
                "release_date='" + releaseDate + '\'' +
                ", title='" + title + '\'' +
                ", vote_average='" + voteAverage + '\'' +
                ", overview='" + plotSynopsis + '\'' +
                ", poster_path='" + moviePoster + '\'' +
                ", is_favorite ='" + isFavorite + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(releaseDate);
        dest.writeString(title);
        dest.writeString(voteAverage);
        dest.writeString(plotSynopsis);
        dest.writeString(moviePoster);
        dest.writeInt(isFavorite);
    }
}


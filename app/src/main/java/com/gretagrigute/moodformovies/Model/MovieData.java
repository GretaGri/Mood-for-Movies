package com.gretagrigute.moodformovies.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Greta Grigutė on 2019-01-05.
 */
public class MovieData implements Parcelable {
    private final String releaseDate;
    private final String title;
    private final String voteAverage;
    private final String plotSynopsis;
    private final String moviePoster;

    public MovieData(String releaseDate, String title, String voteAverage, String plotSynopsis, String moviePoster) {
        this.releaseDate = releaseDate;
        this.title = title;
        this.voteAverage = voteAverage;
        this.plotSynopsis = plotSynopsis;
        this.moviePoster = moviePoster;
    }

    protected MovieData(Parcel in) {
        releaseDate = in.readString();
        title = in.readString();
        voteAverage = in.readString();
        plotSynopsis = in.readString();
        moviePoster = in.readString();
    }

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

    @Override
    public String toString() {
        return "Movie{" +
                "release_date='" + releaseDate + '\'' +
                ", title='" + title + '\'' +
                ", vote_average='" + voteAverage + '\'' +
                ", overview='" + plotSynopsis + '\'' +
                ", poster_path='" + moviePoster + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(releaseDate);
        dest.writeString(title);
        dest.writeString(voteAverage);
        dest.writeString(plotSynopsis);
        dest.writeString(moviePoster);
    }
}


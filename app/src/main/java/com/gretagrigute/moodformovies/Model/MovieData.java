package com.gretagrigute.moodformovies.Model;

/**
 * Created by Greta Grigutė on 2019-01-05.
 */
public class MovieData {
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
}


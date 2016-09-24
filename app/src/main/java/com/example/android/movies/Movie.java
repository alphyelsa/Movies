package com.example.android.movies;

/**
 * Created by aesebast on 9/15/2016.
 *
 * This class stores the details of each movie.
 *
 */
public class Movie {

    private String id;
    private String movieTitle;
    private String moviePoster;
    private String plotSynopsis;
    private String userRating;
    private String releaseYear;
    private String length;

    public Movie(String id, String movieTitle, String moviePoster, String plotSynopsis, String userRating, String releaseDate) {
        this.id = id;
        this.movieTitle = movieTitle;
        this.moviePoster = moviePoster;
        this.plotSynopsis = plotSynopsis;
        this.userRating = userRating;
        this.releaseYear = releaseDate.split("-")[0];
        this.length = "0";
    }

    public Movie(String id, String movieTitle, String moviePoster, String plotSynopsis, String userRating, String releaseDate, String length) {
        this.id = id;
        this.movieTitle = movieTitle;
        this.moviePoster = moviePoster;
        this.plotSynopsis = plotSynopsis;
        this.userRating = userRating;
        this.length = length;
        this.releaseYear = releaseDate.split("-")[0];
    }

    public Movie(String movieDescription) {
        String[] movieDetails = movieDescription.split(":##:");
        this.id = movieDetails[0];
        this.movieTitle = movieDetails[1];
        this.moviePoster = movieDetails[2];
        this.plotSynopsis = movieDetails[3];
        this.userRating = movieDetails[4];
        this.releaseYear = movieDetails[5];
        this.length = movieDetails[6];
    }

    public String getId() {
        return id;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public String getUserRating() {
        return userRating;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public String getLength() {
        return length;
    }

    @Override
    public String toString() {
        return getId() + ":##:" + getMovieTitle() + ":##:" + getMoviePoster() + ":##:" + getPlotSynopsis() + ":##:" + getUserRating() + ":##:" + getReleaseYear() + ":##:" + getLength();
    }

}

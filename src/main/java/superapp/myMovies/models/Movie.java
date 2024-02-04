package superapp.myMovies.models;

import java.util.List;

public class Movie {
    private long id ;
    private String overview ;
    private String release_date ;
    private String poster_path ;
    private String backdrop_path ;
    private String title ;
    private double vote_average ;
    private List<String> genre_ids;

    private boolean video;

    private String trailerUrl;


    public Movie() {}

    public Movie(long id, String overview, String release_date, String poster_path, String backdrop_path, String title,
                 double vote_average, List<String> genre_ids, boolean video) {
        this.id = id;
        this.overview = overview;
        this.release_date = release_date;
        this.poster_path = poster_path;
        this.backdrop_path = backdrop_path;
        this.title = title;
        this.vote_average = vote_average;
        this.genre_ids = genre_ids;
        this.video = video;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "poster_path='" + poster_path + '\'' +
                ", title='" + title + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public List<String> getGenre_ids() {
        return genre_ids;
    }

    public void setGenre_ids(List<String> genre_ids) {
        this.genre_ids = genre_ids;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public String getTrailerUrl() {
        return trailerUrl;
    }

    public void setTrailerUrl(String trailerUrl) {
        this.trailerUrl = trailerUrl;
    }
}


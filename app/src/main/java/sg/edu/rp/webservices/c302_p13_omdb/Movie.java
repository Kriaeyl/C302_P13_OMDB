package sg.edu.rp.webservices.c302_p13_omdb;
import com.google.firebase.firestore.Exclude;

public class Movie {

    private String movieId;
    private String title, rating, released, runtime, genre, actors, plot, language, poster;

    public Movie(String movieId, String title, String rating, String released, String runtime, String genre, String actors, String plot, String language, String poster) {
        this.movieId = movieId;
        this.title = title;
        this.rating = rating;
        this.released = released;
        this.runtime = runtime;
        this.genre = genre;
        this.actors = actors;
        this.plot = plot;
        this.language = language;
        this.poster = poster;
    }

    public Movie(String title, String rating, String released, String runtime, String genre, String actors, String plot, String language, String poster) {
        this.title = title;
        this.rating = rating;
        this.released = released;
        this.runtime = runtime;
        this.genre = genre;
        this.actors = actors;
        this.plot = plot;
        this.language = language;
        this.poster = poster;
    }

    @Exclude
    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }
}
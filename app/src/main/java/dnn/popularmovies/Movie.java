package dnn.popularmovies;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by deivid on 14/12/2016.
 */
public class Movie implements Serializable {

    private int id;
    private String title;
    private String sinopse;
    private Date releaseDate;
    private double voteAverage;
    private String poster;
    private String thumb;
    private List<Trailler> traillers;
    private List<Reviews> reviews;

    public Movie() {
    }

    public List<Trailler> getTraillers() {
        return traillers;
    }

    public void setTraillers(List<Trailler> traillers) {
        this.traillers = traillers;
    }

    public List<Reviews> getReviews() {
        return reviews;
    }

    public void setReviews(List<Reviews> reviews) {
        this.reviews = reviews;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSinopse() {
        return sinopse;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }


}


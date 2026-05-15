package com.example.tayang;

public class FavoriteMovie extends Movie {

    private int id;
    private String title;
    private String posterPath;
    private String backdropPath;
    private String overview;
    private double voteAverage;
    private String mediaType;

    public void setId(int id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setPosterPath(String posterPath) { this.posterPath = posterPath; }
    public void setBackdropPath(String backdropPath) { this.backdropPath = backdropPath; }
    public void setOverview(String overview) { this.overview = overview; }
    public void setVoteAverage(double voteAverage) { this.voteAverage = voteAverage; }
    public void setMediaType(String mediaType) { this.mediaType = mediaType; }

    @Override public int getId() { return id; }
    @Override public String getTitle() { return title; }
    @Override public String getPosterPath() { return posterPath; }
    @Override public String getBackdropPath() { return backdropPath; }
    @Override public String getOverview() { return overview; }
    @Override public double getVoteAverage() { return voteAverage; }
    @Override public String getMediaType() { return mediaType; }
    @Override public String getFullPosterUrl() { return Constants.IMAGE_BASE_URL + posterPath; }
    @Override public String getFullBackdropUrl() { return Constants.IMAGE_BASE_URL + backdropPath; }
}
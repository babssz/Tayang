package com.example.tayang;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    // Film trending minggu ini (untuk banner)
    @GET("trending/all/week")
    Call<MovieResponse> getTrending(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("include_adult") boolean includeAdult,
            @Query("with_genres") String genres
    );

    // Film populer
    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("include_adult") boolean includeAdult,
            @Query("with_genres") String genres
    );

    // Serial populer
    @GET("tv/popular")
    Call<MovieResponse> getPopularTv(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("include_adult") boolean includeAdult,
            @Query("with_genres") String genres
    );

    // Detail film
    @GET("movie/{id}")
    Call<Movie> getMovieDetail(
            @Path("id") int id,
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("with_genres") String genres
    );

    // Trailer film
    @GET("movie/{id}/videos")
    Call<VideoResponse> getMovieVideos(
            @Path("id") int id,
            @Query("api_key") String apiKey
    );

    // Pencarian
    @GET("search/multi")
    Call<MovieResponse> searchMovies(
            @Query("api_key") String apiKey,
            @Query("query") String query,
            @Query("language") String language,
            @Query("include_adult") boolean includeAdult
    );

    // Ambil daftar genre film
    @GET("genre/movie/list")
    Call<GenreResponse> getGenres(
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    // Film berdasarkan genre
    @GET("discover/movie")
    Call<MovieResponse> getMoviesByGenre(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("include_adult") boolean includeAdult,
            @Query("with_genres") int genreId
    );
}
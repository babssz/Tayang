package com.example.tayang;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExploreFragment extends Fragment {

    private EditText etSearch;
    private RecyclerView rvGenres, rvResults;
    private Button btnRefresh;
    private MovieAdapter resultAdapter;
    private GenreAdapter genreAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_explore, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etSearch = view.findViewById(R.id.et_search);
        rvGenres = view.findViewById(R.id.rv_genres);
        rvResults = view.findViewById(R.id.rv_results);
        btnRefresh = view.findViewById(R.id.btn_refresh);

        // Setup RecyclerView genre horizontal
        rvGenres.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // Setup RecyclerView hasil grid 3 kolom
        rvResults.setLayoutManager(new GridLayoutManager(getContext(), 3));

        // Load genre
        loadGenres();

        // Load film populer sebagai default
        loadDefault();

        // Search listener
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 2) {
                    searchMovies(s.toString().trim());
                } else if (s.toString().trim().isEmpty()) {
                    loadDefault();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Tombol refresh
        btnRefresh.setOnClickListener(v -> {
            btnRefresh.setVisibility(View.GONE);
            loadDefault();
            loadGenres();
        });
    }

    private void loadGenres() {
        ApiClient.getService().getGenres(Constants.API_KEY, "id")
                .enqueue(new Callback<GenreResponse>() {
                    @Override
                    public void onResponse(Call<GenreResponse> call, Response<GenreResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<Genre> genres = response.body().getGenres();
                            genreAdapter = new GenreAdapter(getContext(), genres, genre ->
                                    loadByGenre(genre.getId()));
                            rvGenres.setAdapter(genreAdapter);
                        }
                    }

                    @Override
                    public void onFailure(Call<GenreResponse> call, Throwable t) {
                        if (getContext() != null)
                            btnRefresh.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void loadDefault() {
        ApiClient.getService().getPopularMovies(Constants.API_KEY, "id", false,
                        "28,12,16,35,18,10751,14,878")
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            updateResults(response.body().getResults());
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {
                        if (getContext() != null)
                            btnRefresh.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void loadByGenre(int genreId) {
        ApiClient.getService().getMoviesByGenre(Constants.API_KEY, "id", false, genreId)
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            updateResults(response.body().getResults());
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {
                        if (getContext() != null)
                            btnRefresh.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void searchMovies(String query) {
        ApiClient.getService().searchMovies(Constants.API_KEY, query, "id", false)
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            updateResults(response.body().getResults());
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {
                        if (getContext() != null)
                            btnRefresh.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void updateResults(List<Movie> movies) {
        if (resultAdapter == null) {
            resultAdapter = new MovieAdapter(getContext(), movies, movie -> navigateToDetail(movie));
            rvResults.setAdapter(resultAdapter);
        } else {
            resultAdapter.updateData(movies);
        }
    }

    private void navigateToDetail(Movie movie) {
        Bundle args = new Bundle();
        args.putInt("movieId", movie.getId());
        args.putString("movieTitle", movie.getTitle());
        args.putString("posterPath", movie.getPosterPath());
        args.putString("backdropPath", movie.getBackdropPath());
        args.putString("overview", movie.getOverview());
        args.putFloat("rating", (float) movie.getVoteAverage());
        args.putString("mediaType", movie.getMediaType() != null ? movie.getMediaType() : "movie");

        Navigation.findNavController(requireView())
                .navigate(R.id.action_explore_to_detail, args);
    }
}
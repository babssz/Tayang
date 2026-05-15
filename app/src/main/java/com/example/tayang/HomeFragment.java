package com.example.tayang;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import androidx.navigation.Navigation;

public class HomeFragment extends Fragment {

    private ViewPager2 vpBanner;
    private TabLayout tabIndicator;
    private RecyclerView rvMovies, rvTv;
    private Button btnRefresh;

    private BannerAdapter bannerAdapter;
    private MovieAdapter movieAdapter, tvAdapter;

    private Handler autoScrollHandler = new Handler(Looper.getMainLooper());
    private int currentBannerPage = 0;

    private Runnable autoScrollRunnable = new Runnable() {
        @Override
        public void run() {
            if (bannerAdapter != null && bannerAdapter.getItemCount() > 0) {
                currentBannerPage = (currentBannerPage + 1) % bannerAdapter.getItemCount();
                vpBanner.setCurrentItem(currentBannerPage, true);
            }
            autoScrollHandler.postDelayed(this, 3000);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        vpBanner = view.findViewById(R.id.vp_banner);
        tabIndicator = view.findViewById(R.id.tab_indicator);
        rvMovies = view.findViewById(R.id.rv_movies);
        rvTv = view.findViewById(R.id.rv_tv);
        btnRefresh = view.findViewById(R.id.btn_refresh);

        // Setup RecyclerView horizontal
        rvMovies.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvTv.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // Load data dari API
        loadTrending();
        loadPopularMovies();
        loadPopularTv();

        // Tombol refresh
        btnRefresh.setOnClickListener(v -> {
            btnRefresh.setVisibility(View.GONE);
            loadTrending();
            loadPopularMovies();
            loadPopularTv();
        });
    }

    private void loadTrending() {
        ApiClient.getService().getTrending(Constants.API_KEY, "id", false, "28,12,16,35,18,10751,14,878")
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<Movie> trending = response.body().getResults();

                            // Setup banner
                            bannerAdapter = new BannerAdapter(getContext(), trending, movie ->
                                navigateToDetail(movie));
                            vpBanner.setAdapter(bannerAdapter);

                            // Hubungkan dot indicator ke ViewPager2
                            new TabLayoutMediator(tabIndicator, vpBanner,
                                    (tab, position) -> {}).attach();

                            // Mulai auto-scroll
                            autoScrollHandler.postDelayed(autoScrollRunnable, 3000);
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {
                        if (getContext() != null)
                            btnRefresh.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void loadPopularMovies() {
        ApiClient.getService().getPopularMovies(Constants.API_KEY, "id", false, "28,12,16,35,18,10751,14,878")
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<Movie> movies = response.body().getResults();
                            movieAdapter = new MovieAdapter(getContext(), movies, movie ->
                                navigateToDetail(movie));
                            rvMovies.setAdapter(movieAdapter);
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {
                        if (getContext() != null)
                            btnRefresh.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void loadPopularTv() {
        ApiClient.getService().getPopularTv(Constants.API_KEY, "id", false, "28,12,16,35,18,10751,14,878\"")
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<Movie> tvList = response.body().getResults();
                            tvAdapter = new MovieAdapter(getContext(), tvList, movie ->
                                    navigateToDetail(movie));
                            rvTv.setAdapter(tvAdapter);
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {
                        if (getContext() != null)
                            btnRefresh.setVisibility(View.VISIBLE);
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Stop auto-scroll saat fragment di destroy biar ga memory leak
        autoScrollHandler.removeCallbacks(autoScrollRunnable);
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
                .navigate(R.id.action_home_to_detail, args);
    }
}
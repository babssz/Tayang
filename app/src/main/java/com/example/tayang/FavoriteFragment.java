package com.example.tayang;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavoriteFragment extends Fragment {

    private RecyclerView rvFavorites;
    private TextView tvEmpty;
    private DatabaseHelper dbHelper;
    private MovieGridAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvFavorites = view.findViewById(R.id.rv_favorites);
        tvEmpty = view.findViewById(R.id.tv_empty);
        dbHelper = new DatabaseHelper(getContext());

        rvFavorites.setLayoutManager(new GridLayoutManager(getContext(), 3));

        loadFavorites();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh setiap kali halaman dibuka
        // biar langsung update kalau ada film baru disimpan/dihapus
        loadFavorites();
    }

    private void loadFavorites() {
        List<Movie> favorites = dbHelper.getAllFavorites();

        if (favorites.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            rvFavorites.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            rvFavorites.setVisibility(View.VISIBLE);

            if (adapter == null) {
                adapter = new MovieGridAdapter(getContext(), favorites,
                        movie -> navigateToDetail(movie));
                rvFavorites.setAdapter(adapter);
            } else {
                adapter.updateData(favorites);
            }
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
                .navigate(R.id.action_favorite_to_detail, args);
    }
}
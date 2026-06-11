package com.example.tayang;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailFragment extends Fragment {

    private DatabaseHelper dbHelper;
    private Movie currentMovie;
    private Button btnFavorite;
    private boolean isFavorite = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbHelper = new DatabaseHelper(getContext());

        // Ambil data yang dikirim dari HomeFragment
        Bundle args = getArguments();
        if (args == null) return;

        int movieId = args.getInt("movieId");
        String title = args.getString("movieTitle");
        String posterPath = args.getString("posterPath");
        String backdropPath = args.getString("backdropPath");
        String overview = args.getString("overview");
        float rating = args.getFloat("rating");
        String mediaType = args.getString("mediaType", "movie");

        // Buat objek movie dari data yang diterima
        currentMovie = new FavoriteMovie();
        ((FavoriteMovie) currentMovie).setId(movieId);
        ((FavoriteMovie) currentMovie).setTitle(title);
        ((FavoriteMovie) currentMovie).setPosterPath(posterPath);
        ((FavoriteMovie) currentMovie).setBackdropPath(backdropPath);
        ((FavoriteMovie) currentMovie).setOverview(overview);
        ((FavoriteMovie) currentMovie).setVoteAverage(rating);
        ((FavoriteMovie) currentMovie).setMediaType(mediaType);

        // Setup views
        ImageView ivBackdrop = view.findViewById(R.id.iv_backdrop);
        ImageView ivPoster = view.findViewById(R.id.iv_poster);
        TextView tvTitle = view.findViewById(R.id.tv_title);
        TextView tvRating = view.findViewById(R.id.tv_rating);
        TextView tvOverview = view.findViewById(R.id.tv_overview);
        Button btnPlay = view.findViewById(R.id.btn_play);
        btnFavorite = view.findViewById(R.id.btn_favorite);
        ImageView btnBack = view.findViewById(R.id.btn_back);

        // Isi data ke views
        tvTitle.setText(title);
        tvRating.setText("★ " + String.format("%.1f", rating));
        tvOverview.setText(overview != null && !overview.isEmpty()
                ? overview : "Sinopsis tidak tersedia.");

        // Load gambar
        Glide.with(this).load(Constants.IMAGE_BASE_URL + backdropPath)
                .placeholder(R.color.surface).into(ivBackdrop);
        Glide.with(this).load(Constants.IMAGE_BASE_URL + posterPath)
                .placeholder(R.color.surface).into(ivPoster);

        // Cek status favorit
        isFavorite = dbHelper.isFavorite(movieId);
        updateFavoriteButton();

        // Tombol back
        btnBack.setOnClickListener(v ->
                Navigation.findNavController(view).popBackStack());

        // Tombol favorit
        btnFavorite.setOnClickListener(v -> {
            // Animasi bounce
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(btnFavorite, "scaleX", 1f, 1.1f, 1f);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(btnFavorite, "scaleY", 1f, 1.1f, 1f);
            scaleX.setDuration(300);
            scaleY.setDuration(300);
            scaleX.start();
            scaleY.start();

            if (isFavorite) {
                dbHelper.removeFavorite(movieId);
                isFavorite = false;
                Toast.makeText(getContext(), "Dihapus dari favorit", Toast.LENGTH_SHORT).show();
            } else {
                dbHelper.addFavorite(currentMovie);
                isFavorite = true;
                Toast.makeText(getContext(), "Ditambahkan ke favorit", Toast.LENGTH_SHORT).show();
            }
            updateFavoriteButton();
        });

        // Tombol play trailer
        btnPlay.setOnClickListener(v -> {
            // Ambil trailer dari API lalu buka PlayerActivity
            loadTrailer(movieId, mediaType);
        });
    }

    private void updateFavoriteButton() {
        if (isFavorite) {
            btnFavorite.setText("♥  Tersimpan di Favorit");
            btnFavorite.setTextColor(getResources().getColor(R.color.highlight, null));
        } else {
            btnFavorite.setText("♡  Simpan ke Favorit");
            btnFavorite.setTextColor(getResources().getColor(R.color.white, null));
        }
    }

    private void loadTrailer(int movieId, String mediaType) {
        Call<VideoResponse> call = ApiClient.getService()
                .getMovieVideos(movieId, Constants.API_KEY);

        call.enqueue(new Callback<VideoResponse>() {
            @Override
            public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (Trailer trailer : response.body().getResults()) {
                        if (trailer.getType().equals("Trailer")) {
                            String youtubeKey = trailer.getKey();

                            // Coba buka di app YouTube dulu
                            android.net.Uri uri = android.net.Uri
                                    .parse("vnd.youtube:" + youtubeKey);
                            android.content.Intent intent =
                                    new android.content.Intent(
                                            android.content.Intent.ACTION_VIEW, uri);

                            if (intent.resolveActivity(
                                    requireActivity().getPackageManager()) != null) {
                                startActivity(intent);
                            } else {
                                // Fallback ke browser
                                android.net.Uri webUri = android.net.Uri
                                        .parse("https://www.youtube.com/watch?v=" + youtubeKey);
                                startActivity(new android.content.Intent(
                                        android.content.Intent.ACTION_VIEW, webUri));
                            }
                            return;
                        }
                    }
                    Toast.makeText(getContext(), "Trailer tidak tersedia",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<VideoResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Gagal memuat trailer",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
package com.example.tayang;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MovieGridAdapter extends RecyclerView.Adapter<MovieGridAdapter.GridViewHolder> {

    private Context context;
    private List<Movie> movies;
    private MovieAdapter.OnMovieClickListener listener;

    public MovieGridAdapter(Context context, List<Movie> movies,
                            MovieAdapter.OnMovieClickListener listener) {
        this.context = context;
        this.movies = movies;
        this.listener = listener;
    }

    @NonNull
    @Override
    public GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie_grid, parent, false);
        return new GridViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GridViewHolder holder, int position) {
        Movie movie = movies.get(position);

        holder.itemView.startAnimation(
                AnimationUtils.loadAnimation(context, R.anim.fade_slide_up));

        holder.tvTitle.setText(movie.getTitle());
        holder.tvRating.setText("★ " + String.format("%.1f", movie.getVoteAverage()));

        Glide.with(context)
                .load(movie.getFullPosterUrl())
                .placeholder(R.color.surface)
                .into(holder.ivPoster);

        holder.itemView.setOnClickListener(v -> listener.onMovieClick(movie));
    }

    @Override
    public int getItemCount() { return movies != null ? movies.size() : 0; }

    public void updateData(List<Movie> newMovies) {
        this.movies = newMovies;
        notifyDataSetChanged();
    }

    static class GridViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPoster;
        TextView tvTitle, tvRating;

        public GridViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPoster = itemView.findViewById(R.id.iv_poster);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvRating = itemView.findViewById(R.id.tv_rating);
        }
    }
}
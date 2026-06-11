package com.example.tayang;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerViewHolder> {

    private Context context;
    private List<Movie> movies;
    private MovieAdapter.OnMovieClickListener listener;

    public BannerAdapter(Context context, List<Movie> movies,
                         MovieAdapter.OnMovieClickListener listener) {
        this.context = context;
        this.movies = movies;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_banner, parent, false);
        return new BannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        Movie movie = movies.get(position);

        holder.tvTitle.setText(movie.getTitle());

        Glide.with(context)
                .load(movie.getFullBackdropUrl())
                .placeholder(R.color.surface)
                .into(holder.ivBackdrop);

        holder.itemView.setOnClickListener(v -> listener.onMovieClick(movie));
    }

    @Override
    public int getItemCount() { return movies != null ? movies.size() : 0; }

    static class BannerViewHolder extends RecyclerView.ViewHolder {
        ImageView ivBackdrop;
        TextView tvTitle, tvRating;

        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            ivBackdrop = itemView.findViewById(R.id.iv_backdrop);
            tvTitle = itemView.findViewById(R.id.tv_banner_title);
        }
    }
}
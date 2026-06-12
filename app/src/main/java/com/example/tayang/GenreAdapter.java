package com.example.tayang;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.GenreViewHolder> {

    private Context context;
    private List<Genre> genres;
    private OnGenreClickListener listener;
    private int selectedPosition = -1;

    public interface OnGenreClickListener {
        void onGenreClick(Genre genre);
    }

    public GenreAdapter(Context context, List<Genre> genres, OnGenreClickListener listener) {
        this.context = context;
        this.genres = genres;
        this.listener = listener;
    }

    @NonNull
    @Override
    public GenreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_genre, parent, false);
        return new GenreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreViewHolder holder, int position) {
        Genre genre = genres.get(position);
        holder.tvGenre.setText(genre.getName());

        // Highlight genre yang dipilih
        if (selectedPosition == position) {
            holder.tvGenre.setBackgroundResource(R.drawable.bg_genre_active);
            holder.tvGenre.setTextColor(context.getResources().getColor(R.color.white, null));
        } else {
            holder.tvGenre.setBackgroundResource(R.drawable.bg_genre_inactive);
            holder.tvGenre.setTextColor(context.getResources().getColor(R.color.text_muted, null));
        }

        holder.itemView.setOnClickListener(v -> {
            selectedPosition = position;
            notifyDataSetChanged();
            listener.onGenreClick(genre);
        });
    }

    @Override
    public int getItemCount() { return genres != null ? genres.size() : 0; }

    static class GenreViewHolder extends RecyclerView.ViewHolder {
        TextView tvGenre;

        public GenreViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGenre = itemView.findViewById(R.id.tv_genre);
        }
    }
}
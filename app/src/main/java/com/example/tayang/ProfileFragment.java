package com.example.tayang;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class ProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences prefs = requireActivity()
                .getSharedPreferences("tayang_prefs", getActivity().MODE_PRIVATE);

        // Ambil data user
        String name = prefs.getString("name", "Pengguna");
        String email = prefs.getString("email", "");
        boolean isDark = prefs.getBoolean("is_dark_theme", true);

        // Setup views
        TextView tvName = view.findViewById(R.id.tv_name);
        TextView tvEmail = view.findViewById(R.id.tv_email);
        TextView tvFavoriteCount = view.findViewById(R.id.tv_favorite_count);
        TextView tvWatchedCount = view.findViewById(R.id.tv_watched_count);
        SwitchMaterial switchTheme = view.findViewById(R.id.switch_theme);
        LinearLayout itemLogout = view.findViewById(R.id.item_logout);

        // Isi data user
        tvName.setText(name);
        tvEmail.setText(email);

        // Hitung jumlah favorit dari SQLite
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        tvFavoriteCount.setText(String.valueOf(dbHelper.getAllFavorites().size()));
        tvWatchedCount.setText(String.valueOf(dbHelper.getAllFavorites().size()));

        // Set status toggle theme
        switchTheme.setChecked(isDark);

        // Toggle dark/light theme
        switchTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean("is_dark_theme", isChecked).apply();
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        // Logout
        itemLogout.setOnClickListener(v -> {
            prefs.edit()
                    .putBoolean("is_logged_in", false)
                    .apply();
            Intent intent = new Intent(getActivity(), AuthActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}
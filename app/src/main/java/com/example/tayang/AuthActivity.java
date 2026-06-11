package com.example.tayang;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.content.SharedPreferences;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Cek tema tersimpan, default dark
        SharedPreferences prefs = getSharedPreferences("tayang_prefs", MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("is_dark_theme", true); // default true = dark
        if (isDark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
    }
}
package com.example.tayang;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Ambil view nama app
        TextView tvAppName = findViewById(R.id.tv_app_name);

        // Load animasi dari file anim yang akan kita buat
        Animation fadeSlideUp = AnimationUtils.loadAnimation(this, R.anim.fade_slide_up);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        // Jalankan animasi
        tvAppName.startAnimation(fadeSlideUp);

        // Setelah 2.5 detik, cek apakah user sudah login
        new Handler().postDelayed(() -> {
            SharedPreferences prefs = getSharedPreferences("tayang_prefs", MODE_PRIVATE);
            boolean isLoggedIn = prefs.getBoolean("is_logged_in", false);

            Intent intent;
            if (isLoggedIn) {
                // Langsung ke beranda
                intent = new Intent(SplashActivity.this, MainActivity.class);
            } else {
                // Ke halaman login
                intent = new Intent(SplashActivity.this, AuthActivity.class);
            }
            startActivity(intent);
            finish(); // tutup SplashActivity biar ga bisa back ke sini
        }, 2500);
    }
}
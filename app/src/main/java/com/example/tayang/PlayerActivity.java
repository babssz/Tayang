package com.example.tayang;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class PlayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        String youtubeKey = getIntent().getStringExtra("youtube_key");

        WebView webView = findViewById(R.id.web_view);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());

        // Load trailer YouTube via embed
        String html = "<html><body style='margin:0;padding:0;background:#000;'>" +
                "<iframe width='100%' height='100%' " +
                "src='https://www.youtube.com/embed/" + youtubeKey + "?autoplay=1' " +
                "frameborder='0' allowfullscreen></iframe>" +
                "</body></html>";

        webView.loadData(html, "text/html", "utf-8");
    }
}
package com.example.tayang;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class PlayerActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        String youtubeKey = getIntent().getStringExtra("youtube_key");

        webView = findViewById(R.id.web_view);

        // Setup WebView
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setMediaPlaybackRequiresUserGesture(false);

        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());

        // Load trailer YouTube
        String html = "<!DOCTYPE html><html><head>" +
                "<style>body{margin:0;padding:0;background:#000;}" +
                "iframe{position:fixed;top:0;left:0;width:100%;height:100%;}</style>" +
                "</head><body>" +
                "<iframe src='https://www.youtube.com/embed/" + youtubeKey +
                "?autoplay=1&rel=0&showinfo=0' " +
                "frameborder='0' allow='autoplay;encrypted-media' allowfullscreen>" +
                "</iframe></body></html>";

        webView.loadDataWithBaseURL(
                "https://www.youtube.com",
                html,
                "text/html",
                "utf-8",
                null
        );
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
package com.project.buspad_25;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {

    private WebView gameWebView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);

        gameWebView = findViewById(R.id.webView);

        WebSettings webSettings = gameWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        gameWebView.setWebViewClient(new WebViewClient());

        String gamePath = getIntent().getStringExtra("gamePath");

        if (gamePath != null) {
            gameWebView.loadUrl("file:///android_asset/" + gamePath);
        } else {
            gameWebView.loadUrl("file:///android_asset/www/Stick-Hero/index.html"); // fallback
        }
    }

    @Override
    public void onBackPressed() {
        if (gameWebView.canGoBack()) {
            gameWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}

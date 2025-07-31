package com.project.buspad_25;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class InternetActivity extends AppCompatActivity {
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_internet);

        // Set up the back button
        ImageView backButton = findViewById(R.id.back_main);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(InternetActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        // Find WebView by ID
        WebView webBrowser = findViewById(R.id.web_browser);

        // Load YouTube as the default webpage
        webBrowser.loadUrl("https://www.google.com/");
        webBrowser.getSettings().setJavaScriptEnabled(true); // Enable JavaScript
        webBrowser.setWebViewClient(new WebClient());

    }

    // Custom WebViewClient class to handle URL loading
    private static class WebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return true;
        }
    }
}
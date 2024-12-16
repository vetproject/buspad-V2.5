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

public class YoutubeActivity extends AppCompatActivity {

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_youtube);

        // Set up the back button
        ImageView backButton = findViewById(R.id.back_main);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(YoutubeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        WebView webBrowser = findViewById(R.id.web_browser);

        webBrowser.loadUrl("https://www.youtube.com/");
        webBrowser.getSettings().setJavaScriptEnabled(true);
        webBrowser.setWebViewClient(new WebClient());

    }

    private static class WebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return true;
        }
    }

}
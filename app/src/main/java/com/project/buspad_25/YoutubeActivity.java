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

    private WebView webBrowser;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_youtube);

        // Initialize WebView
        webBrowser = findViewById(R.id.web_browser);
        webBrowser.getSettings().setJavaScriptEnabled(true);
        webBrowser.setWebViewClient(new WebClient());
        webBrowser.loadUrl("https://www.youtube.com/");

        // Back button in toolbar
        ImageView backButton = findViewById(R.id.back_main);
        backButton.setOnClickListener(v -> {
            if (webBrowser.canGoBack()) {
                webBrowser.goBack(); // Go to previous page in WebView history
            } else {
                // If no history, go back to main activity
                Intent intent = new Intent(YoutubeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (webBrowser.canGoBack()) {
            webBrowser.goBack(); // Go back in WebView history
        } else {
            super.onBackPressed(); // Default back behavior (finish activity)
        }
    }

    private static class WebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return true;
        }
    }
}

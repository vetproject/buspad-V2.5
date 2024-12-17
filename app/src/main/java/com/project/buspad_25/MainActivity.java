package com.project.buspad_25;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Locale;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Link other pages ----------------------------------

        // Ticket View
        CardView ticketCardView = findViewById(R.id.ticket_view);
        ticketCardView.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TicketActivity.class);
            startActivity(intent);
        });

        // YouTube View
        CardView youtubeCardView = findViewById(R.id.youtube_view);
        youtubeCardView.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, YoutubeActivity.class);
            startActivity(intent);
        });

        // Music View
        CardView musicCardView = findViewById(R.id.music_view);
        musicCardView.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MusicActivity.class);
            startActivity(intent);
        });

        // Video View
        CardView videoCardView = findViewById(R.id.video_view);
        videoCardView.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, VideoActivity.class);
            startActivity(intent);
        });

        // Internet View
        CardView interCardView = findViewById(R.id.internet_view);
        interCardView.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, InternetActivity.class);
            startActivity(intent);
        });

        // Game View
        CardView gameCardView = findViewById(R.id.game_view);
        gameCardView.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            startActivity(intent);
        });

        /* Language change functionality ----------------------------------*/

        // English Language Button
        CardView englishButton = findViewById(R.id.english_view);
        englishButton.setOnClickListener(v -> setLocale("en"));

        // Khmer Language Button
        CardView khmerButton = findViewById(R.id.khmer_view);
        khmerButton.setOnClickListener(v -> setLocale("km"));

        // Chinese Language Button
        CardView chineseButton = findViewById(R.id.chines_view);
        chineseButton.setOnClickListener(v -> setLocale("zh"));
    }

    // Function to change the language dynamically
    public void setLocale(String langCode) {
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);

        Configuration config = getResources().getConfiguration();
        config.setLocale(locale); // Update locale
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        // Manually update visible text in the UI
        updateUI();
    }

    private void updateUI() {
        // Update the text for all visible UI components manually
        TextView ticketText = findViewById(R.id.ticket_text);
        ticketText.setText(getString(R.string.booking_ticket));

        TextView youtubeText = findViewById(R.id.youtube_text);
        youtubeText.setText(getString(R.string.youtube));

        TextView musicText = findViewById(R.id.music_text);
        musicText.setText(getString(R.string.music));

        TextView videoText = findViewById(R.id.video_text);
        videoText.setText(getString(R.string.movie));

        TextView internetText = findViewById(R.id.internet_text);
        internetText.setText(getString(R.string.internet));

        TextView gameText = findViewById(R.id.game_text);
        gameText.setText(getString(R.string.game));
    }
}

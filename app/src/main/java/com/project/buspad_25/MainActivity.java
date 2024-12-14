package com.project.buspad_25;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        /* Link other page ----------------------------------*/

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

        /* Change languages in buspad ----------------------------------*/


    }
}
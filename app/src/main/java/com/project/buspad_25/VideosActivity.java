package com.project.buspad_25;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


public class VideosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_video);

        // Set up the back button
        ImageView backButton = findViewById(R.id.back_main);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(VideosActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        // Set up the movie categories
        // Set up the cartoon movies
        LinearLayout cartoonMovies = findViewById(R.id.cartoon_movies);
        cartoonMovies.setOnClickListener(v -> {
            Intent intent = new Intent(VideosActivity.this, CartoonAcitvity.class);
            startActivity(intent);
            finish();
        });
        // Set up the hollywood movies
        LinearLayout hollywoodMovies = findViewById(R.id.hollywood_movies);
        hollywoodMovies.setOnClickListener(v -> {
            Intent intent = new Intent(VideosActivity.this, HollywoodActivity.class);
            startActivity(intent);
            finish();

        });

        // Set up the khmer dubbed movies
        LinearLayout khmerDubbedMovies = findViewById(R.id.movie_dubbed_khmer);
        khmerDubbedMovies.setOnClickListener(v -> {
            Intent intent = new Intent(VideosActivity.this, DubbedkhmerActivity.class);
            startActivity(intent);
            finish();
        });

        // Set up the song video
        LinearLayout songVideo = findViewById(R.id.song);
        songVideo.setOnClickListener(v -> {
            Intent intent = new Intent(VideosActivity.this, SongActivity.class);
            startActivity(intent);
            finish();
        });

        // Set up the khmer comedy
        LinearLayout khmerComedy = findViewById(R.id.khmer_comedy);
        khmerComedy.setOnClickListener(v -> {
            Intent intent = new Intent(VideosActivity.this, KhmercomedyActivity.class);
            startActivity(intent);
            finish();
        });


    }
}
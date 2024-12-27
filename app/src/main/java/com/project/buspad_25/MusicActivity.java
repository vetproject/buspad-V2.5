package com.project.buspad_25;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


public class MusicActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_music);

        // Set up the back button
        ImageView backButton = findViewById(R.id.back_main);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(MusicActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        //set up the music remix
        LinearLayout remixSong = findViewById(R.id.remix_song);
        remixSong.setOnClickListener(v ->{
            Intent intent = new Intent(MusicActivity.this, RemixSongActivity.class);
            startActivity(intent);
            finish();
        });
        //set up th music khmer
        LinearLayout khmerSong = findViewById(R.id.khmer_song);
        khmerSong.setOnClickListener(v -> {
            Intent intent = new Intent(MusicActivity.this,KhmerSongActivity.class);
            startActivity(intent);
            finish();
        });
        // set up the english song
        LinearLayout englishSong = findViewById(R.id.english_song);
        englishSong.setOnClickListener(v -> {
            Intent intent = new Intent(MusicActivity.this, EnglishSongActivity.class);
            startActivity(intent);
            finish();
        });
    }



}
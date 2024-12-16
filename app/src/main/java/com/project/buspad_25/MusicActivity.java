package com.project.buspad_25;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

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


    }
}
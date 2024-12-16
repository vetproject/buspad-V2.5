package com.project.buspad_25;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


public class GameActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);

        // Set up the back button
        ImageView backButton = findViewById(R.id.back_main);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(GameActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });


    }
}
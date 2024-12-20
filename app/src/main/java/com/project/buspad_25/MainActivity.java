package com.project.buspad_25;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputType;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {

    private static final String PASSWORD = "12345"; // Set your desired password here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Link other pages
        setupCardViews();

        // Add click listener for Version Text
        TextView versionText = findViewById(R.id.version_text);
        versionText.setOnClickListener(v -> showPasswordDialog());
    }

    private void setupCardViews() {
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

        // Language change functionality
        setupLanguageButtons();
    }

    private void setupLanguageButtons() {
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

    public void setLocale(String langCode) {
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);

        Configuration config = getResources().getConfiguration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        // Manually update visible text in the UI
        updateUI();
    }

    private void updateUI() {
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

    private void showPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Password");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String enteredPassword = input.getText().toString();
            if (enteredPassword.equals(PASSWORD)) {
                openSettings();
            } else {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        startActivity(intent);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        // Show a message indicating that back navigation is disabled
        Toast.makeText(this, "Back navigation is disabled on this screen", Toast.LENGTH_SHORT).show();
    }
}
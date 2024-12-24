package com.project.buspad_25;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputType;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {

    private static final String PASSWORD = "12345"; // Set your desired password here

    // declare internet and bluetooth
    private ImageView internetIcon;
    private ImageView bluetoothIcon;

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

        // find id of internet and bluetooth

        internetIcon = findViewById(R.id.internet_icon);
        bluetoothIcon = findViewById(R.id.bluetooth_icon);

        // Register Receivers
        registerReceiver(bluetoothReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        // Initial Status Check
        checkInternetConnection();
        checkBluetoothStatus();

        // Set onClickListeners
        internetIcon.setOnClickListener(v -> openInternetSettings());
        bluetoothIcon.setOnClickListener(v -> openBluetoothSettings());
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


    @SuppressLint("SetTextI18n")
    private void checkInternetConnection() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            internetIcon.setImageResource(R.drawable.internet_connect); // Replace with connected icon
        } else {
            internetIcon.setImageResource(R.drawable.internet_dis); // Replace with disconnected icon
        }
    }

    @SuppressLint("SetTextI18n")
    private void checkBluetoothStatus() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            bluetoothIcon.setImageResource(R.drawable.bluetooth_connect); // Replace with enabled icon
        } else {
            bluetoothIcon.setImageResource(R.drawable.bluetooth); // Replace with disabled icon
        }
    }

    // BroadcastReceiver for Bluetooth State
    private final BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
            switch (state) {
                case BluetoothAdapter.STATE_ON:
                    bluetoothIcon.setImageResource(R.drawable.bluetooth_connect);
                    break;
                case BluetoothAdapter.STATE_OFF:
                    bluetoothIcon.setImageResource(R.drawable.bluetooth);
                    break;
            }
        }
    };

    // BroadcastReceiver for Network Connectivity
    private final BroadcastReceiver networkReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkInternetConnection();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister Receivers
        unregisterReceiver(bluetoothReceiver);
        unregisterReceiver(networkReceiver);
    }

    // Open Internet Settings when clicked
    private void openInternetSettings() {
        Intent intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
        startActivity(intent);
    }

    // Open Bluetooth Settings when clicked
    private void openBluetoothSettings() {
        Intent intent = new Intent(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
        startActivity(intent);
    }

}
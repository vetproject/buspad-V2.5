package com.project.buspad_25;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MotionEvent;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private InactivityHandler inactivityHandler;
    private SystemStatusManager statusManager;
    private AdVideoDialog adVideoDialog;
    private static final String PASSWORD = "vet666888";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Init ads and inactivity handler
        adVideoDialog = new AdVideoDialog(this);
        inactivityHandler = new InactivityHandler(this, adVideoDialog);
        inactivityHandler.start();

        // Setup System status (Bluetooth/WiFi)
        statusManager = new SystemStatusManager(this);
        statusManager.init();

        // Setup menu card views
        new CardMenuHandler(this).init();

        // Version password click
        findViewById(R.id.version_text).setOnClickListener(v -> showPasswordDialog());

        // Language buttons
        new LanguageManager(this).setupLanguageButtons();
    }

    private void showPasswordDialog() {
        new PasswordDialog(this, PASSWORD, this::openSettings).show();
    }

    private void openSettings() {
        startActivity(new Intent(Settings.ACTION_SETTINGS));
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        inactivityHandler.reset();
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        inactivityHandler.stop();
        statusManager.unregister();
        adVideoDialog.destroy();
    }

    @Override
    public void onBackPressed() {
        // By not calling super.onBackPressed(), we disable the default back button behavior.
        // This is a common way to prevent users from navigating back from certain activities.
        // super.onBackPressed();
        super.onBackPressed();
        Toast.makeText(this, "Back navigation is disabled on this screen", Toast.LENGTH_SHORT).show();
    }
}

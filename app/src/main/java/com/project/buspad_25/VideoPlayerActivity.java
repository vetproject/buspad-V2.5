package com.project.buspad_25;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;

import androidx.appcompat.app.AppCompatActivity;

public class VideoPlayerActivity extends AppCompatActivity {

    private PlayerView playerView;
    private ExoPlayer exoPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        playerView = findViewById(R.id.player_view);

        String videoPath = getIntent().getStringExtra("videoPath");

        if (videoPath != null && !videoPath.isEmpty()) {
            initializePlayer(videoPath);
        } else {
            Toast.makeText(this, "Failed to load video", Toast.LENGTH_SHORT).show();
        }
    }

    private void initializePlayer(String videoPath) {
        try {
            // Initialize ExoPlayer
            exoPlayer = new ExoPlayer.Builder(this).build();
            playerView.setPlayer(exoPlayer);

            // Prepare the media source for HTTP URL
            Uri uri = Uri.parse(videoPath);
            MediaSource mediaSource = buildMediaSource(uri);

            // Prepare and play the video
            try {
                exoPlayer.setMediaSource(mediaSource);
                exoPlayer.prepare();
                exoPlayer.play();
            } catch (Exception e) {
                Log.e("VideoPlayerActivity", "Error playing video: " + e.getMessage(), e);
                Toast.makeText(this, "Error playing video", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("VideoPlayerActivity", "Error initializing player: " + e.getMessage(), e);
            Toast.makeText(this, "Error initializing player", Toast.LENGTH_SHORT).show();
        }
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ProgressiveMediaSource.Factory(new DefaultHttpDataSource.Factory())
                .createMediaSource(MediaItem.fromUri(uri));
    }

    @Override
    protected void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    protected void onStop() {
        super.onStop();
        releasePlayer();
    }

    private void releasePlayer() {
        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;
        }
    }

}
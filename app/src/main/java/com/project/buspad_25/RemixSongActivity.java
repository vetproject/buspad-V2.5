package com.project.buspad_25;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class RemixSongActivity extends AppCompatActivity {
    // Initialize variables
    TextView playerPosition, playerDuration, currentSongTitle;
    SeekBar seekBar;
    ImageView btRew, btPlay, btPause, btFf;
    MediaPlayer mediaPlayer;
    Handler handler = new Handler();
    Runnable runnable;
    ArrayList<Song> songList = new ArrayList<>();
    SongAdapter songAdapter;
    ListView songListView;

    int currentSongIndex = 0; // Track the currently playing song index

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_remix);

        //set back to music page
        ImageView backBtn = findViewById(R.id.back_main);
        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(RemixSongActivity.this, MusicActivity.class);
            startActivity(intent);
            finish();
        });

        // Assign variables
        playerPosition = findViewById(R.id.player_position);
        playerDuration = findViewById(R.id.player_duration);
        currentSongTitle = findViewById(R.id.current_song_title); // Add this TextView in your layout
        seekBar = findViewById(R.id.seek_id);
        btRew = findViewById(R.id.bt_rew);
        btPlay = findViewById(R.id.bt_play);
        btPause = findViewById(R.id.bt_pause);
        btFf = findViewById(R.id.bt_ff);
        songListView = findViewById(R.id.song_list);

        // Initialize MediaPlayer
        mediaPlayer = new MediaPlayer();

        // Initialize the song list
        initializeSongList();

        // Initialize Runnable for updating SeekBar
        runnable = new Runnable() {
            @Override
            public void run() {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(this, 500);
            }
        };

        btPlay.setOnClickListener(v -> {
            btPlay.setVisibility(View.GONE);
            btPause.setVisibility(View.VISIBLE);
            mediaPlayer.start();
            seekBar.setMax(mediaPlayer.getDuration());
            handler.postDelayed(runnable, 0);
        });

        btPause.setOnClickListener(v -> {
            btPause.setVisibility(View.GONE);
            btPlay.setVisibility(View.VISIBLE);
            mediaPlayer.pause();
            handler.removeCallbacks(runnable);
        });

        btFf.setOnClickListener(v -> {
            // Advance to the next song
            if (currentSongIndex < songList.size() - 1) {
                currentSongIndex++;
            } else {
                currentSongIndex = 0; // Loop back to the first song
            }
            songAdapter.setSelectedPosition(currentSongIndex); // Highlight the next song
            playSong(songList.get(currentSongIndex));
        });

        btRew.setOnClickListener(v -> {
            // Go back to the previous song
            if (currentSongIndex > 0) {
                currentSongIndex--;
            } else {
                currentSongIndex = songList.size() - 1; // Loop back to the last song
            }
            songAdapter.setSelectedPosition(currentSongIndex); // Highlight the previous song
            playSong(songList.get(currentSongIndex));
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
                playerPosition.setText(convertFormat(mediaPlayer.getCurrentPosition()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                    handler.postDelayed(runnable, 0);
                }
            }
        });

        mediaPlayer.setOnCompletionListener(mp -> {
            btPause.setVisibility(View.GONE);
            btPlay.setVisibility(View.VISIBLE);
            mediaPlayer.seekTo(0);
            seekBar.setProgress(0);
            playerPosition.setText(convertFormat(0));
        });
    }

    @SuppressLint("DefaultLocale")
    private String convertFormat(int duration) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) -
                        TimeUnit.MILLISECONDS.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
    }

    private void initializeSongList() {
        // Add songs to the list
        songList.add(new Song("VANNDA_ម្តាយ_MAMA_&_ក្មេងខ្មែរ_with_LYRIC", String.valueOf(R.raw.v1)));
        songList.add(new Song("ហេតុអ្វីយើងបែកគ្នា_ថុល_សុភីទិ", String.valueOf(R.raw.t1)));
        songList.add(new Song("គ្មានអ្វីត្រូវស្តាយ_ព្រាប_សុវត្ថិ", String.valueOf(R.raw.p1)));

        // Set up the adapter and ListView
        songAdapter = new SongAdapter(this, songList);
        songListView.setAdapter(songAdapter);

        // Highlight the first song as selected by default
        songAdapter.setSelectedPosition(currentSongIndex);

        // Handle item click to play the selected song
        songListView.setOnItemClickListener((parent, view, position, id) -> {
            currentSongIndex = position; // Update the current song index
            songAdapter.setSelectedPosition(position); // Highlight the selected song
            playSong(songList.get(currentSongIndex)); // Automatically play the selected song

            // Update the play/pause button visibility
            btPlay.setVisibility(View.GONE); // Hide Play button
            btPause.setVisibility(View.VISIBLE); // Show Pause button
        });
    }

    private void playSong(Song song) {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }

        try {
            int resId = Integer.parseInt(song.getSongFilePath());
            mediaPlayer = MediaPlayer.create(this, resId);
            mediaPlayer.start();
            seekBar.setMax(mediaPlayer.getDuration());
            playerDuration.setText(convertFormat(mediaPlayer.getDuration()));
            currentSongTitle.setText(song.getSongName()); // Display the song name in the view
            handler.postDelayed(runnable, 0);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading song", Toast.LENGTH_SHORT).show();
        }
    }
}

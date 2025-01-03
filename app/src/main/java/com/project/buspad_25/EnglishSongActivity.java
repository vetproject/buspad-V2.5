package com.project.buspad_25;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;

public class EnglishSongActivity extends AppCompatActivity {

    // Initialize UI elements
    TextView playerPosition, playerDuration, currentSongTitle;
    SeekBar seekBar;
    ImageView btRew, btPlay, btPause, btFf;
    MediaPlayer mediaPlayer;
    Handler handler = new Handler();
    Runnable runnable;
    ListView songListView;

    ArrayList<Song> songList = new ArrayList<>();
    SongAdapter songAdapter;

    int currentSongIndex = 0; // Track the currently playing song index

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_english_song);

        // Initialize UI components
        playerPosition = findViewById(R.id.player_position);
        playerDuration = findViewById(R.id.player_duration);
        currentSongTitle = findViewById(R.id.current_song_title);
        seekBar = findViewById(R.id.seek_id);
        btRew = findViewById(R.id.bt_rew);
        btPlay = findViewById(R.id.bt_play);
        btPause = findViewById(R.id.bt_pause);
        btFf = findViewById(R.id.bt_ff);
        songListView = findViewById(R.id.song_list);

        // Initialize MediaPlayer
        mediaPlayer = new MediaPlayer();

        // Initialize the song list adapter and set it to the ListView
        songAdapter = new SongAdapter(this, songList);
        songListView.setAdapter(songAdapter);

        // Fetch songs from the server
        new FetchSongsTask().execute();

        // Handle back button click
        findViewById(R.id.back_main).setOnClickListener(v -> {
            Intent intent = new Intent(EnglishSongActivity.this, MusicActivity.class);
            startActivity(intent);
            finish();
        });

        // Handle song item click
        songListView.setOnItemClickListener((parent, view, position, id) -> {
            currentSongIndex = position;
            songAdapter.setSelectedPosition(position);
            playSong(songList.get(currentSongIndex));
        });

        // Handle Play button click
        btPlay.setOnClickListener(v -> {
            btPlay.setVisibility(View.GONE);
            btPause.setVisibility(View.VISIBLE);
            mediaPlayer.start();
            seekBar.setMax(mediaPlayer.getDuration());
            handler.postDelayed(runnable, 0);
        });

        // Handle Pause button click
        btPause.setOnClickListener(v -> {
            btPause.setVisibility(View.GONE);
            btPlay.setVisibility(View.VISIBLE);
            mediaPlayer.pause();
            handler.removeCallbacks(runnable);
        });

        // Handle Fast-Forward button click
        btFf.setOnClickListener(v -> {
            if (currentSongIndex < songList.size() - 1) {
                currentSongIndex++;
            } else {
                currentSongIndex = 0; // Loop back to the first song
            }
            songAdapter.setSelectedPosition(currentSongIndex);
            playSong(songList.get(currentSongIndex));
        });

        // Handle Rewind button click
        btRew.setOnClickListener(v -> {
            if (currentSongIndex > 0) {
                currentSongIndex--;
            } else {
                currentSongIndex = songList.size() - 1; // Loop back to the last song
            }
            songAdapter.setSelectedPosition(currentSongIndex);
            playSong(songList.get(currentSongIndex));
        });

        // Update the current position of the song as SeekBar progresses
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

        // When song finishes, reset the player to the beginning
        mediaPlayer.setOnCompletionListener(mp -> {
            btPause.setVisibility(View.GONE);
            btPlay.setVisibility(View.VISIBLE);
            mediaPlayer.seekTo(0);
            seekBar.setProgress(0);
            playerPosition.setText(convertFormat(0));
        });

        // Runnable to update the SeekBar in real-time
        runnable = new Runnable() {
            @Override
            public void run() {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(this, 500);
            }
        };
    }

    private class FetchSongsTask extends AsyncTask<Void, Void, List<Song>> {

        @Override
        protected List<Song> doInBackground(Void... voids) {
            List<Song> songs = new ArrayList<>();
            try {
                JSch jsch = new JSch();
                Session session = jsch.getSession("root", "192.168.8.131", 22);
                session.setPassword("P@ssw0rd()");
                session.setConfig("StrictHostKeyChecking", "no");
                session.connect();

                Channel channel = session.openChannel("sftp");
                channel.connect();
                ChannelSftp sftp = (ChannelSftp) channel;

                Vector<ChannelSftp.LsEntry> files = sftp.ls("/var/www/html/busPad/musics/English song");
                for (ChannelSftp.LsEntry file : files) {
                    if (!file.getAttrs().isDir()) {
                        String songTitle = file.getFilename();
                        String songUri = "http://192.168.8.131/busPad/musics/English song/" + songTitle;
                        songs.add(new Song(songTitle, songUri)); // Add song with title and URI
                    }
                }

                sftp.disconnect();
                session.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return songs;
        }

        @Override
        protected void onPostExecute(List<Song> result) {
            if (result.isEmpty()) {
                Toast.makeText(EnglishSongActivity.this, "Failed to fetch songs", Toast.LENGTH_SHORT).show();
            } else {
                songList = new ArrayList<>(result);
                songAdapter = new SongAdapter(EnglishSongActivity.this, songList);
                songListView.setAdapter(songAdapter);
            }
        }
    }

    private void playSong(Song song) {
        // Stop and reset the MediaPlayer if it's already playing
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }

        try {
            // Create MediaPlayer instance from the song URI (which is the file path)
            mediaPlayer.setDataSource(song.getSongFilePath());  // Use song URI for the file path
            mediaPlayer.prepare();  // Prepare the MediaPlayer
            mediaPlayer.start();  // Start playing the song

            // Update the UI with the song details
            currentSongTitle.setText(song.getSongName());  // Display song name
            playerDuration.setText(convertFormat(mediaPlayer.getDuration()));  // Show song duration
            playerPosition.setText(convertFormat(0));  // Set the current position to 0 initially
            seekBar.setMax(mediaPlayer.getDuration());  // Set SeekBar max to song duration

            // Start updating the SeekBar
            handler.postDelayed(runnable, 0);

            // Update play/pause buttons visibility
            btPlay.setVisibility(View.GONE);  // Hide play button
            btPause.setVisibility(View.VISIBLE);  // Show pause button

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading song", Toast.LENGTH_SHORT).show();
        }

        // Listen for song completion to reset the player and buttons
        mediaPlayer.setOnCompletionListener(mp -> {
            btPause.setVisibility(View.GONE);  // Hide pause button
            btPlay.setVisibility(View.VISIBLE);  // Show play button
            seekBar.setProgress(0);  // Reset the SeekBar
            playerPosition.setText(convertFormat(0));  // Reset the current position text
        });
    }

    @SuppressLint("DefaultLocale")
    private String convertFormat(int duration) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) -
                        TimeUnit.MILLISECONDS.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
    }
}

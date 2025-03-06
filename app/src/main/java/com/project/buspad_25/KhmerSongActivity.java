package com.project.buspad_25;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.media.AudioManager;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class KhmerSongActivity extends AppCompatActivity {

    // Initialize UI elements
    TextView playerPosition, playerDuration, currentSongTitle;
    SeekBar seekBar;
    ImageView btRew, btPlay, btPause, btFf;
    MediaPlayer mediaPlayer;
    Handler handler = new Handler();
    Runnable runnable;
    ListView songListView;

    ArrayList<Song> songList = new ArrayList<>();
    HashMap<String, String> songMap = new HashMap<>();
    SongAdapter songAdapter;

    int currentSongIndex = 0; // Track the currently playing song index

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_khmer_song);

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

         // Set up audio stream type
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        // Initialize MediaPlayer
        mediaPlayer = new MediaPlayer();

        // Initialize the song list adapter and set it to the ListView
        songAdapter = new SongAdapter(this, songList);
        songListView.setAdapter(songAdapter);

        // Fetch songs from the server
        new FetchSongsTask().execute();

        // Handle back button click
        findViewById(R.id.back_main).setOnClickListener(v -> {
            Intent intent = new Intent(KhmerSongActivity.this, MusicActivity.class);
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
            if (isMediaPlayerReady()) {
                mediaPlayer.start();
                handler.postDelayed(runnable, 0);
                btPlay.setVisibility(View.GONE);
                btPause.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(this, "MediaPlayer is not ready", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle Pause button click
        btPause.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                handler.removeCallbacks(runnable);
                btPause.setVisibility(View.GONE);
                btPlay.setVisibility(View.VISIBLE);
            }
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
                if (fromUser && mediaPlayer.isPlaying()) {
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
            if (mediaPlayer != null) {
                try {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                } catch (Exception e) {
                    Log.e("KhmerSongActivity", "Error on song completion: " + e.getMessage());
                }
            }
            currentSongIndex = (currentSongIndex + 1) % songList.size(); // Loop through songs
            playSong(songList.get(currentSongIndex));
        });

        // Runnable to update the SeekBar in real-time
        runnable = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    if (mediaPlayer.isPlaying()) {
                        seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    }
                }
                handler.postDelayed(this, 500);
            }
        };
    }
    @Override
    protected void onPause() {
        super.onPause();
        if(mediaPlayer != null && mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null; // Avoid memory leaks
        }
    }

    private class FetchSongsTask extends AsyncTask<Void, Void, List<Song>> {
        @Override
        protected List<Song> doInBackground(Void... voids) {
            List<Song> songs = new ArrayList<>();
            try {
                JSch jsch = new JSch();
                Session session = jsch.getSession("root", "192.168.1.222", 22);
                session.setPassword(" "); // Make sure to set the correct password
                session.setConfig("StrictHostKeyChecking", "no");
                session.connect();

                Channel channel = session.openChannel("sftp");
                channel.connect();
                ChannelSftp sftp = (ChannelSftp) channel;

                Vector<ChannelSftp.LsEntry> files = sftp.ls("/var/www/html/busPad/musics/Khmersong");
                for (ChannelSftp.LsEntry file : files) {
                    if (!file.getAttrs().isDir()) {
                        String songTitle = file.getFilename();
                        String songUri = "http://192.168.1.222/busPad/musics/Khmersong/" + songTitle;
                        Log.d("KhmerSongActivity", "Found song: " + songTitle + " at URI: " + songUri);
                        songMap.put(songTitle, songUri);
                        songs.add(new Song(songTitle, songUri)); // Add song with title and URI
                    }
                }

                sftp.disconnect();
                session.disconnect();
            } catch (Exception e) {
                Log.e("KhmerSongActivity", "Error fetching songs: " + e.getMessage());
                e.printStackTrace();
            }
            return songs;
        }

        @Override
        protected void onPostExecute(List<Song> result) {
            if (result.isEmpty()) {
                Toast.makeText(KhmerSongActivity.this, "Failed to fetch songs", Toast.LENGTH_SHORT).show();
            } else {
                songList = new ArrayList<>(result);
                songAdapter = new SongAdapter(KhmerSongActivity.this, songList);
                songListView.setAdapter(songAdapter);
            }
        }
    }

    private void playSong(Song song) {
        if (song == null || song.getSongFilePath() == null) {
            Toast.makeText(this, "Invalid song data", Toast.LENGTH_SHORT).show();
            return;
        }

        // Stop and reset the MediaPlayer if it's already playing
        if (mediaPlayer != null) {
            try {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
            } catch (Exception e) {
                Log.e("KhmerSongActivity", "Error stopping MediaPlayer: " + e.getMessage());
            }
            btPause.setVisibility(View.GONE);
            btPlay.setVisibility(View.VISIBLE);
            mediaPlayer.reset(); // Reset before setting a new data source
        } else {
            mediaPlayer = new MediaPlayer(); // Initialize MediaPlayer if it's null
        }

        mediaPlayer.setOnErrorListener((mp, what, extra) -> {
            Log.e("KhmerSongActivity", "Error occurred: What=" + what + " Extra=" + extra);
            return true; // Handle error
        });
        try {
            Log.d("KhmerSongActivity", "Playing song: " + song.getSongFilePath());
            mediaPlayer.setDataSource(song.getSongFilePath());

            mediaPlayer.setOnPreparedListener(mp -> {
                mp.start();  // Start playing the song when prepared
                currentSongTitle.setText(song.getSongName());
                if (!mp.isPlaying()) {
                    mp.start();
                }

                playerDuration.setText(convertFormat(mp.getDuration()));
                playerPosition.setText(convertFormat(0));
                seekBar.setMax(mp.getDuration());

                // Start updating the SeekBar
                handler.postDelayed(runnable, 0);
                btPlay.setVisibility(View.GONE);
                btPause.setVisibility(View.VISIBLE);
            });

            mediaPlayer.prepareAsync();  // Prepare asynchronously
         } catch (Exception e) {
            Log.e("KhmerSongActivity", "Error loading song: " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(this, "Error loading song", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isMediaPlayerReady() {
        return mediaPlayer != null &&
                (mediaPlayer.getCurrentPosition() >= 0 || mediaPlayer.getDuration() > 0);
    }

    @SuppressLint("DefaultLocale")
    private String convertFormat(int duration) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) -
                        TimeUnit.MILLISECONDS.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
    }
}
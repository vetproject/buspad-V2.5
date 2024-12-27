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

public class EnglishSongActivity extends AppCompatActivity {
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
            Intent intent = new Intent(EnglishSongActivity.this, MusicActivity.class);
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
        songList.add(new Song("Alan Walker - Alone", String.valueOf(R.raw.e1)));
        songList.add(new Song("Alan Walker - Darkside (feat. Au_Ra and Tomine Harket)", String.valueOf(R.raw.e2)));
        songList.add(new Song("Alan Walker - Faded", String.valueOf(R.raw.e3)));
        songList.add(new Song("Alan Walker - Sing Me To Sleep", String.valueOf(R.raw.e4)));
        songList.add(new Song("Alan Walker - The Spectre", String.valueOf(R.raw.e5)));
        songList.add(new Song("Alan Walker, K-391 & Emelie Hollow - Lily (Lyrics)", String.valueOf(R.raw.e6)));
        songList.add(new Song("Alan Walker, Sabrina Carpenter & Farruko - On My Way", String.valueOf(R.raw.e8)));
        songList.add(new Song("Britney Spears - From The Bottom Of My Broken Heart (Official Video)", String.valueOf(R.raw.e8)));
        songList.add(new Song("Bruno Mars - Just The Way You Are (Official Music Video)", String.valueOf(R.raw.e9)));
        songList.add(new Song("Ellie Goulding - Love Me Like You Do (Lyrics)", String.valueOf(R.raw.e10)));
        songList.add(new Song("Enrique Iglesias - Why Not Me (Lyrics Video)", String.valueOf(R.raw.e11)));
        songList.add(new Song("Fifth Harmony - Work from Home (Official Video) ft. Ty Dolla $ign", String.valueOf(R.raw.e12)));
        songList.add(new Song("Jason Mraz - I'm Yours (Lyrics)", String.valueOf(R.raw.e13)));
        songList.add(new Song("Fifth Harmony - Worth It (Lyrics) ft. Kid Ink", String.valueOf(R.raw.e14)));
        songList.add(new Song("Justin Timberlake - Mirrors (Lyrics)", String.valueOf(R.raw.e15)));
        songList.add(new Song("Keep on loving you", String.valueOf(R.raw.e16)));
        songList.add(new Song("Khalid - Young Dumb & Broke (Lyrics)", String.valueOf(R.raw.e17)));
        songList.add(new Song("Khalid - Young Dumb & Broke (Official Video)", String.valueOf(R.raw.e18)));
        songList.add(new Song("Lewis Capaldi - Someone You Loved (Lyrics)", String.valueOf(R.raw.e19)));
        songList.add(new Song("Luis Fonsi - Despacito ft. Daddy Yankee", String.valueOf(R.raw.e20)));
        songList.add(new Song("Luis Fonsi - Despacito ft. Daddy Yankee_2", String.valueOf(R.raw.e21)));
        songList.add(new Song("MOMOLAND(모모랜드) - -BAAM- Moving Dance Practice", String.valueOf(R.raw.e22)));
        songList.add(new Song("Pitbull - Rain Over Me ft. Marc Anthony", String.valueOf(R.raw.e23)));
        songList.add(new Song("SHAUN feat. Conor Maynard - Way Back Home (Lyrics) Sam Feldt Edit", String.valueOf(R.raw.e24)));
        songList.add(new Song("Sean Kingston - Beautiful Girls (Alt. Version)", String.valueOf(R.raw.e25)));
        songList.add(new Song("Selena Gomez - Same Old Love (Official Music Video)", String.valueOf(R.raw.e26)));
        songList.add(new Song("When I Was Your Man - Bruno Mars (Lyrics)", String.valueOf(R.raw.e27)));
        songList.add(new Song("Wiz Khalifa - See You Again ft. Charlie Puth [Official Video] Furious 7 Soundtrack", String.valueOf(R.raw.e28)));
        songList.add(new Song("You Are The Reason - Calum Scott (Lyrics)", String.valueOf(R.raw.e29)));
        songList.add(new Song("mouse love rice english version", String.valueOf(R.raw.e30)));
        songList.add(new Song("สายแนนหัวใจ - ก้อง ห้วยไร่ (เพลงประกอบภาพยนตร์ นาคี2)", String.valueOf(R.raw.e31)));

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

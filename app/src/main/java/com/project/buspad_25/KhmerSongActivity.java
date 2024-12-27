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

public class KhmerSongActivity extends AppCompatActivity {
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
            Intent intent = new Intent(KhmerSongActivity.this, MusicActivity.class);
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
        songList.add(new Song("(ស្រលាញ់អូន ៣៦៥ ថ្ងៃ )HM VCD 132 3 Srolanh Oun 366 Tnay", String.valueOf(R.raw.k1)));
        songList.add(new Song("ហBong Chea Bonla Knong Trung Oun   Moun Mara ", String.valueOf(R.raw.k2)));
        songList.add(new Song("Chhorn sovanareach", String.valueOf(R.raw.k3)));
        songList.add(new Song("Chhorn sovanareach2", String.valueOf(R.raw.k4)));
        songList.add(new Song("Chhorn sovanareach3", String.valueOf(R.raw.k5)));
        songList.add(new Song("Ellie Goulding - Love Me Like You Do (Lyrics)", String.valueOf(R.raw.k6)));
        songList.add(new Song("Keep on loving you", String.valueOf(R.raw.k7)));
        songList.add(new Song("MOMOLAND(모모랜드) - -BAAM- Moving Dance Practice", String.valueOf(R.raw.k8)));
        songList.add(new Song("Selena Gomez - Same Old Love (Official Music Video) ", String.valueOf(R.raw.k9)));
        songList.add(new Song("Suly Pheng - មិនច្បាស់ជាមួយអូន ft. Olica (Lyrics Video)", String.valueOf(R.raw.k10)));
        songList.add(new Song("VANNDA   ម្តាយ MAMA &  ក្មេងខ្មែរ  with  LYRIC", String.valueOf(R.raw.k11)));
        songList.add(new Song("VannDa - Time To Rise feat. Master Kong Nay (Official Music Video)", String.valueOf(R.raw.k12)));
        songList.add(new Song("mouse love rice english version", String.valueOf(R.raw.k13)));
        songList.add(new Song("สายแนนหัวใจ - ก้อง ห้วยไร่ (เพลงประกอบภาพยนตร์ นาคี2) ", String.valueOf(R.raw.k14)));
        songList.add(new Song("កាត់ចិត្តបែបណា", String.valueOf(R.raw.k15)));
        songList.add(new Song("ការដូគ្មានន័យ - មូន ម៉ារ៉ា Moon Mara - Moon Mara Song", String.valueOf(R.raw.k16)));
        songList.add(new Song("ការដូគ្មានន័យ - មូន ម៉ារ៉ា Moon Mara - Moon Mara Song", String.valueOf(R.raw.k17)));
        songList.add(new Song("កុំដើម្បីអូន", String.valueOf(R.raw.k18)));
        songList.add(new Song("កូនក្រមុំបងគ្រាន់តែជាមនុស្សស្រីធម្មតា-នី រតនា", String.valueOf(R.raw.k19)));
        songList.add(new Song("ក្តីសុខបងពេលគ្មានអូន-រ៉េត ស៊ូហ្សាណា", String.valueOf(R.raw.k20)));
        songList.add(new Song("ខ្ញុំក្លាយជាអ្នកជំងឺស្នេហា", String.valueOf(R.raw.k21)));
        songList.add(new Song("គេនោះជាស្រីកាប់ដាវ - តន់ ចន្ទសីម៉ា _ OFFICAL AUDIO _", String.valueOf(R.raw.k22)));
        songList.add(new Song("គេមិនស្រលាញ់យើងទេ - សួស ážœáž¸áž áŸ’ážŸá", String.valueOf(R.raw.k23)));
        songList.add(new Song("ជាប់បាក់ឌុបចប់ស្នេហ៍- សាពូន មីដាដា_ Chhoub Bakdub Chob Snae By Sapoun Midada (Audio Lyrics)", String.valueOf(R.raw.k24)));
        songList.add(new Song("ឈឺចាប់ម្នាក់ឯងគ្មានអ្នកដឹង", String.valueOf(R.raw.k25)));
        songList.add(new Song("ឈឺយ៉ាងនេះរស់យ៉ាងណា áž…áŸ’ážšáŸ€áž„ážŠáŸ„áž™á", String.valueOf(R.raw.k26)));
        songList.add(new Song("ដងស្ទឹងសង្កែ ច្រៀងដោយ ដួងវីរៈសិទ្ធ -- Dong Steung Song Kae by Doung Viraksith", String.valueOf(R.raw.k27)));
        songList.add(new Song("ដឹងខ្ញុំយំដែរទេ  GII GII", String.valueOf(R.raw.k28)));
        songList.add(new Song("ធ្លាប់នឹកម្នាក់នេះទេ", String.valueOf(R.raw.k29)));
        songList.add(new Song("និយមន័យស្នេហ៍ _ សុពូនមុីដាដា 【LYRIC】", String.valueOf(R.raw.k30)));
        songList.add(new Song("បាត់បង - លីនកា_ Lin Ka sonG oriGinal sonG khme", String.valueOf(R.raw.k31)));
        songList.add(new Song("បេះដូងមិនងាយស្រលាញ់  ប៉ាច់ áž‚áž", String.valueOf(R.raw.k32)));
        songList.add(new Song("ប្រងើយ-នូ ឧសភា__YouTube__Music Channel__BY SOPHA", String.valueOf(R.raw.k33)));
        songList.add(new Song("ប្រពន្ធអូនលំបាកហើយ - នី រតនា ", String.valueOf(R.raw.k34)));
        songList.add(new Song("ផ្លូវណាទៅផ្ទះម៉ែក្មេក ច្រៀងដោយ  - កញ្ញា ថាន់ នឿថង [ LYRIC MUSIC OFFICIAL ]", String.valueOf(R.raw.k35)));
        songList.add(new Song("មនុស្សខ្លះ MonusKlas - ដា សុម៉ាវត្តី _ ពិភពនៃអារម្មណ៍ _", String.valueOf(R.raw.k36)));
        songList.add(new Song("មហាឈឺ ►នី រតនា _►Moha Cher _ By Ny Rothana", String.valueOf(R.raw.k37)));
        songList.add(new Song("មិនចង់បានអ្នកថ្មី", String.valueOf(R.raw.k38)));
        songList.add(new Song("មិនអាចទៅរួច(min ach tov rouc - Mustache Band", String.valueOf(R.raw.k39)));
        songList.add(new Song("យប់នេះអូនយំ - Yob nis Oun yom By Rady Phealing Full HD", String.valueOf(R.raw.k40)));
        songList.add(new Song("យល់ច្រលំថាគេស្រលាញ់យើង__ Thol Lina __ khmer song 2019", String.valueOf(R.raw.k4)));
        songList.add(new Song("រាស្ត្រសាមញ្ញ - Por Xeang Ft. Ela La (Official lyric) - YouTube", String.valueOf(R.raw.k5)));
        songList.add(new Song("លឺសំលេងបេះដូងទេ", String.valueOf(R.raw.k42)));
        songList.add(new Song("សង្សារមួយខែពីរខែ", String.valueOf(R.raw.k43)));
        songList.add(new Song("សុខទុកអូនយ៉ាងណា បងមានចង់ដឹងទេ", String.valueOf(R.raw.k45)));
        songList.add(new Song("សុំមិនស្គាល់បងល្អជាង áž…áŸ’ážšáŸ€áž„ážŠá", String.valueOf(R.raw.k46)));
        songList.add(new Song("សោភា  Som p", String.valueOf(R.raw.k47)));
        songList.add(new Song("អរគុណអ្នកទីបី - Manith Jupiter [Official Audio]", String.valueOf(R.raw.k48)));
        songList.add(new Song("អារម្មណ៏ពេលខូចចិត្ត", String.valueOf(R.raw.k49)));
        songList.add(new Song("អូន", String.valueOf(R.raw.k50)));

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

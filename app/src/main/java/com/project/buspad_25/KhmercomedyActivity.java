package com.project.buspad_25;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class KhmercomedyActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private VideoAdapter videoAdapter;
    private List<Video> videoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cartoon_acitvity);

        ImageView backBtn = findViewById(R.id.back_main);
        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(KhmercomedyActivity.this, VideosActivity.class);
            startActivity(intent);
            finish();
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch video list from the server
        new FetchVideosTask().execute();
    }

    private class FetchVideosTask extends AsyncTask<Void, Void, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            ArrayList<String> videos = new ArrayList<>();
            try {
                JSch jsch = new JSch();
                Session session = jsch.getSession("root", "192.168.1.222", 22);
                session.setPassword(" ");
                session.setConfig("StrictHostKeyChecking", "no");
                session.connect();

                Channel channel = session.openChannel("sftp");
                channel.connect();
                ChannelSftp sftp = (ChannelSftp) channel;

                Vector<ChannelSftp.LsEntry> files = sftp.ls("/var/www/html/busPad/movies/Khmercomedy");
                for (ChannelSftp.LsEntry file : files) {
                    if (!file.getAttrs().isDir()) {
                        videos.add(file.getFilename());
                    }
                }

                sftp.disconnect();
                session.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return videos;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            if (result.isEmpty()) {
                Toast.makeText(KhmercomedyActivity.this, "Failed to fetch videos", Toast.LENGTH_SHORT).show();
            } else {
                for (String videoName : result) {
                    String videoPath = "http://192.168.1.222/busPad/movies/Khmercomedy/" + videoName;
                    videoList.add(new Video(videoName, videoPath));
                }
                videoAdapter = new VideoAdapter(KhmercomedyActivity.this, videoList);
                recyclerView.setAdapter(videoAdapter);
            }
        }
    }
}
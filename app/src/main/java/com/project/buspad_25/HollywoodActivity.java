package com.project.buspad_25;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.util.ArrayList;
import java.util.Vector;

import androidx.appcompat.app.AppCompatActivity;

public class HollywoodActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> videoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartoon_acitvity);

        listView = findViewById(R.id.listView);

        // Fetch video list from the server
        new FetchVideosTask().execute();

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String videoName = videoList.get(position);
            // Construct the HTTP URL to access the video file
            String videoPath = "http://192.168.8.131/busPad/movies/Hollywood/" + videoName;

            // Pass the HTTP URL to the VideoPlayerActivity
            Intent intent = new Intent(HollywoodActivity.this, VideoPlayerActivity.class);
            intent.putExtra("videoPath", videoPath);
            startActivity(intent);
        });
    }

    private class FetchVideosTask extends AsyncTask<Void, Void, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            ArrayList<String> videos = new ArrayList<>();
            try {
                JSch jsch = new JSch();
                Session session = jsch.getSession("root", "192.168.8.131", 22);
                session.setPassword("P@ssw0rd()");
                session.setConfig("StrictHostKeyChecking", "no");
                session.connect();

                Channel channel = session.openChannel("sftp");
                channel.connect();
                ChannelSftp sftp = (ChannelSftp) channel;

                Vector<ChannelSftp.LsEntry> files = sftp.ls("/var/www/html/busPad/movies/Hollywood");
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
                Toast.makeText(HollywoodActivity.this, "Failed to fetch videos", Toast.LENGTH_SHORT).show();
            } else {
                videoList = result;
                adapter = new ArrayAdapter<>(HollywoodActivity.this, android.R.layout.simple_list_item_1, videoList);
                listView.setAdapter(adapter);
            }
        }
    }
}
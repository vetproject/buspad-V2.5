package com.project.buspad_25;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.util.Vector;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MusicActivity extends AppCompatActivity {

    private LinearLayout folderContainer;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_music);

        folderContainer = findViewById(R.id.folder_container);
        ImageView backButton = findViewById(R.id.back_main);
        backButton.setOnClickListener(v -> {
            startActivity(new Intent(MusicActivity.this, MainActivity.class));
            finish();
        });

        new FetchFoldersTask().execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class FetchFoldersTask extends AsyncTask<Void, Void, Vector<ChannelSftp.LsEntry>> {
        @Override
        protected Vector<ChannelSftp.LsEntry> doInBackground(Void... voids) {
            Vector<ChannelSftp.LsEntry> folders = new Vector<>();
            try {
                JSch jsch = new JSch();
                Session session = jsch.getSession("root", "192.168.8.222", 22);
                session.setPassword("vet666888");
                session.setConfig("StrictHostKeyChecking", "no");
                session.connect();

                Channel channel = session.openChannel("sftp");
                channel.connect();
                ChannelSftp sftp = (ChannelSftp) channel;

                Vector<ChannelSftp.LsEntry> entries = sftp.ls("/var/www/html/musics");
                for (ChannelSftp.LsEntry entry : entries) {
                    if (entry.getAttrs().isDir() && !entry.getFilename().equals(".") && !entry.getFilename().equals("..")) {
                        folders.add(entry);
                    }
                }

                sftp.disconnect();
                session.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return folders;
        }

        @Override
        protected void onPostExecute(Vector<ChannelSftp.LsEntry> folders) {
            if (folders.isEmpty()) {
                Toast.makeText(MusicActivity.this, "No folders found or connection failed", Toast.LENGTH_SHORT).show();
            } else {
                LayoutInflater inflater = LayoutInflater.from(MusicActivity.this);
                for (ChannelSftp.LsEntry folder : folders) {
                    @SuppressLint("InflateParams")
                    LinearLayout view = (LinearLayout) inflater.inflate(R.layout.folder_item, null);

                    TextView name = view.findViewById(R.id.folder_name);
                    name.setText(folder.getFilename());

                    view.setOnClickListener(v -> {
                        Intent intent = new Intent(MusicActivity.this, SongFolderActivity.class);
                        intent.putExtra("folderName", folder.getFilename());
                        startActivity(intent);
                    });

                    folderContainer.addView(view);
                }
            }
        }
    }
}

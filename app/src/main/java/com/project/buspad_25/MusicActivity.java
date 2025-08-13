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

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
    private class FetchFoldersTask extends AsyncTask<Void, Void, List<String>> {
        @Override
        protected List<String> doInBackground(Void... voids) {
            List<String> folders = new ArrayList<>();
            try {
                URL url = new URL("http://192.168.8.222/managerfile/music_folders.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                InputStream inputStream = new BufferedInputStream(conn.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                JSONArray jsonArray = new JSONArray(result.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    folders.add(jsonArray.getString(i));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return folders;
        }

        @Override
        protected void onPostExecute(List<String> folders) {
            if (folders.isEmpty()) {
                Toast.makeText(MusicActivity.this, "No folders found or connection failed", Toast.LENGTH_SHORT).show();
            } else {
                LayoutInflater inflater = LayoutInflater.from(MusicActivity.this);
                for (String folderName : folders) {
                    @SuppressLint("InflateParams")
                    LinearLayout view = (LinearLayout) inflater.inflate(R.layout.folder_item, null);

                    TextView name = view.findViewById(R.id.folder_name);
                    name.setText(folderName);

                    view.setOnClickListener(v -> {
                        Intent intent = new Intent(MusicActivity.this, SongFolderActivity.class);
                        intent.putExtra("folderName", folderName);
                        startActivity(intent);
                    });

                    folderContainer.addView(view);
                }
            }
        }
    }

}

package com.project.buspad_25;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class VideosActivity extends AppCompatActivity {

    private LinearLayout folderContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_video);

        folderContainer = findViewById(R.id.folder_container);
        ImageView backButton = findViewById(R.id.back_main);
        backButton.setOnClickListener(v -> {
            startActivity(new Intent(VideosActivity.this, MainActivity.class));
            finish();
        });

        new FetchFoldersHttpTask().execute();
    }

    private class FetchFoldersHttpTask extends AsyncTask<Void, Void, List<String>> {
        @Override
        protected List<String> doInBackground(Void... voids) {
            List<String> folders = new ArrayList<>();
            try {
                URL url = new URL("http://192.168.8.222/managerfile/movies.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder builder = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }

                JSONArray array = new JSONArray(builder.toString());
                for (int i = 0; i < array.length(); i++) {
                    folders.add(array.getString(i));
                }

                reader.close();
                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return folders;
        }

        @Override
        protected void onPostExecute(List<String> folders) {
            if (folders.isEmpty()) {
                Toast.makeText(VideosActivity.this, "No folders found", Toast.LENGTH_SHORT).show();
            } else {
                LayoutInflater inflater = LayoutInflater.from(VideosActivity.this);
                for (String folder : folders) {
                    LinearLayout view = (LinearLayout) inflater.inflate(R.layout.folder_item, folderContainer, false);
                    TextView name = view.findViewById(R.id.folder_name);
                    name.setText(folder);

                    view.setOnClickListener(v -> {
                        Intent intent = new Intent(VideosActivity.this, FolderVideosActivity.class);
                        intent.putExtra("folderName", folder);
                        startActivity(intent);
                    });

                    folderContainer.addView(view);
                }
            }
        }
    }

}

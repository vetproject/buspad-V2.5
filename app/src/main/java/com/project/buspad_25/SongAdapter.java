package com.project.buspad_25;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SongAdapter extends ArrayAdapter<Song> {

    private int selectedPosition = -1; // Track the selected song position

    public SongAdapter(@NonNull Context context, ArrayList<Song> songList) {
        super(context, 0, songList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_music, parent, false);
        }

        Song song = getItem(position);

        // Bind views
        TextView songTitle = convertView.findViewById(R.id.item_title);
        TextView songArtist = convertView.findViewById(R.id.item_subtitle);
        ImageView albumArt = convertView.findViewById(R.id.item_image);

        // Populate views with data
        songTitle.setText(song.getSongName());
        songArtist.setText("Unknown Artist"); // Can be updated if your data contains artist info
        albumArt.setImageResource(R.drawable.music_ic); // Placeholder album art

        // Highlight the selected item
        if (position == selectedPosition) {
            convertView.setBackgroundColor(getContext().getResources().getColor(android.R.color.black)); // Highlight color
        } else {
            convertView.setBackgroundColor(getContext().getResources().getColor(androidx.cardview.R.color.cardview_dark_background)); // Default color
        }

        return convertView;
    }

    // Method to update the selected song
    public void setSelectedPosition(int position) {
        selectedPosition = position; // Update the selected position
        notifyDataSetChanged(); // Refresh the list to apply the highlighting
    }
}

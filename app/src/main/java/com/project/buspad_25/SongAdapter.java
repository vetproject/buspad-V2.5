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
import androidx.core.content.ContextCompat;

public class SongAdapter extends ArrayAdapter<Song> {

    private int selectedPosition = -1; // Track the selected song position

    public SongAdapter(@NonNull Context context, ArrayList<Song> songList) {
        super(context, 0, songList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_music, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.songTitle = convertView.findViewById(R.id.item_title);
            viewHolder.songArtist = convertView.findViewById(R.id.item_subtitle);
            viewHolder.albumArt = convertView.findViewById(R.id.item_image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Song song = getItem(position);

        // Populate views with data
        if (song != null) {
            viewHolder.songTitle.setText(song.getSongName());
            viewHolder.songArtist.setText("Unknown Artist"); // Update this if you have artist info in Song class
            viewHolder.albumArt.setImageResource(R.drawable.music_ic); // Placeholder album art
        }

        // Highlight the selected item
        if (position == selectedPosition) {
            convertView.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.black)); // Highlight color
        } else {
            convertView.setBackgroundColor(ContextCompat.getColor(getContext(), androidx.cardview.R.color.cardview_dark_background)); // Default color
        }

        return convertView;
    }

    // Method to update the selected song
    public void setSelectedPosition(int position) {
        selectedPosition = position; // Update the selected position
        notifyDataSetChanged(); // Refresh the list to apply the highlighting
    }

    // ViewHolder pattern for performance
    private static class ViewHolder {
        TextView songTitle;
        TextView songArtist;
        ImageView albumArt;
    }
}
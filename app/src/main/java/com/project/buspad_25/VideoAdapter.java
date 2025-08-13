package com.project.buspad_25;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.WindowDecorActionBar;
import androidx.recyclerview.widget.RecyclerView;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    private final Context context;
    private final List<Video> videoList;

    public VideoAdapter(Context context, List<Video> videoList) {
        this.context = context;
        this.videoList = videoList;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_video, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        Video video = videoList.get(position);
        holder.title.setText(video.getTitle());
        holder.duration.setText(video.getDuration());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, VideoPlayerActivity.class);
            intent.putExtra("videoPath", video.getVideoUri());
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return videoList.size();
    }

    static class VideoViewHolder extends RecyclerView.ViewHolder {
        @SuppressLint("RestrictedApi")
        public WindowDecorActionBar.TabImpl video_duration;
        TextView title;
        TextView duration;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.video_title);
            duration = itemView.findViewById(R.id.video_duration);
        }
    }
}


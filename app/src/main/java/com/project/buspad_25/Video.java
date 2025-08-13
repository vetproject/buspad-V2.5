package com.project.buspad_25;

public class Video {
    private final String title;
    private final String videoUri;
    private final String duration;

    public Video(String title, String videoUri, String duration) {
        this.title = title;
        this.videoUri = videoUri;
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public String getVideoUri() {
        return videoUri;
    }

    public String getDuration() {
        return duration;
    }
}

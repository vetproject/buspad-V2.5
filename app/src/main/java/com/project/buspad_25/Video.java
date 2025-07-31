package com.project.buspad_25;

public class Video {
    private final String title;
    private final String videoUri;

    public Video(String title, String videoUri) {
        this.title = title;
        this.videoUri = videoUri;
    }

    public String getTitle() {
        return title;
    }

    public String getVideoUri() {
        return videoUri;
    }
}

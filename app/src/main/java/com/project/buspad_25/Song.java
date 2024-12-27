package com.project.buspad_25;

public class Song {
    private String songName;
    private String songFilePath;

    public Song(String songName, String songFilePath) {
        this.songName = songName;
        this.songFilePath = songFilePath;
    }

    public String getSongName() {
        return songName;
    }

    public String getSongFilePath() {
        return songFilePath;
    }
}
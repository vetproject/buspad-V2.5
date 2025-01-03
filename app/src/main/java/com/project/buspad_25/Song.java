package com.project.buspad_25;

public class Song {
    private String songName;
    private String songFilePath; // URI or path of the song

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

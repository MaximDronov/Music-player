package com.example.audioplayer;

public class Song {
    private String title;
    private int imageResource;
    private int audioResource;

    public Song(String title, int imageResource, int audioResource) {
        this.title = title;
        this.imageResource = imageResource;
        this.audioResource = audioResource;
    }

    public String getTitle() {
        return title;
    }

    public int getImageResource() {
        return imageResource;
    }

    public int getAudioResource() {
        return audioResource;
    }
}

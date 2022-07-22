package com.example.wifip2p.Media;

import android.net.Uri;

public class Audio {

//    songId, songTitle, songArtist, path, genre, duration
    private final Uri uri;
    private final String songName;
    private final String songArtist;
    private final long duration;
    boolean isSelected;

    public Audio(Uri uri, String songName, String songArtist, long duration, boolean isSelected) {
        this.uri = uri;
        this.songName = songName;
        this.songArtist = songArtist;
        this.duration = duration;
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public Uri getUri() {
        return uri;
    }

    public String getSongName() {
        return songName;
    }

    public String getSongArtist() {
        return songArtist;
    }

    public long getDuration() {
        return duration;
    }
}

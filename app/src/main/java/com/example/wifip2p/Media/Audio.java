package com.example.wifip2p.Media;

import android.net.Uri;

import java.io.Serializable;

public class Audio implements Serializable {

//    songId, songTitle, songArtist, path, genre, duration
    private final String uri;
    private final String mimeType;
    private final String songName;
    private final String songArtist;
    private final long duration;
    boolean isSelected;

    public Audio(String uri, String mimeType, String songName, String songArtist, long duration, boolean isSelected) {
        this.uri = uri;
        this.mimeType = mimeType;
        this.songName = songName;
        this.songArtist = songArtist;
        this.duration = duration;
        this.isSelected = isSelected;
    }

    public String getUri() {
        return uri;
    }

    public String getMimeType() {
        return mimeType;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
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

package com.example.wifip2p.Media;

import android.net.Uri;

import java.io.Serializable;

public class Video implements Serializable {

    String className;
    private final String uri;
    private final String name;
    private final int duration;
    private final int size;
    private boolean isSelected;

    public Video(String uri, String name, int duration, int size, boolean isSelected) {
        this.uri = uri;
        this.name = name;
        this.duration = duration;
        this.size = size;
        this.isSelected = isSelected;
    }

    public String getClassName() {
        className = "video";
        return className;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public String getUri() {
        return uri;
    }

    public String getName() {
        return name;
    }

    public int getDuration() {
        return duration;
    }

    public int getSize() {
        return size;
    }
}

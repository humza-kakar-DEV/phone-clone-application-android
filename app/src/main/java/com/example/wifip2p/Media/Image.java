package com.example.wifip2p.Media;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class Image {

    String className;
    private final Uri uri;
    private final String name;
    private final int height;
    private final int width;
    private boolean isSelected;

    public Image(Uri uri, String name, int height, int width, boolean isSelected) {
        this.uri = uri;
        this.name = name;
        this.height = height;
        this.width = width;
        this.isSelected = isSelected;
    }

    public String getClassName() {
        className = "image";
        return className;
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

    public String getName() {
        return name;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

}

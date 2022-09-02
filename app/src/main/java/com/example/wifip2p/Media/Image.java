package com.example.wifip2p.Media;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Image implements Serializable {

    String className;
    private final String uri;
    private final String name;
    private final int height;
    private final int width;
    private boolean isSelected;

    public Image(String uri, String name, int height, int width, boolean isSelected) {
        this.uri = uri;
        this.name = name;
        this.height = height;
        this.width = width;
        this.isSelected = isSelected;
    }

    public String getUri() {
        return uri;
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

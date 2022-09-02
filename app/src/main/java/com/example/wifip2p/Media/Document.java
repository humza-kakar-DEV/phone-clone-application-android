package com.example.wifip2p.Media;

import android.net.Uri;

import java.io.Serializable;

public class Document implements Serializable {

    String className;
    long id;
    String contentUri;
    String name;
    String mimeType;
    String mediaType;
    int size;
    boolean isSelected;

    public Document(long id, String contentUri, String name, String mimeType, String mediaType, int size, boolean isSelected) {
        this.id = id;
        this.contentUri = contentUri;
        this.name = name;
        this.mimeType = mimeType;
        this.mediaType = mediaType;
        this.size = size;
        this.isSelected = isSelected;
    }

    public String getClassName() {
        className = "document";
        return className;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContentUri() {
        return contentUri;
    }

    public void setContentUri(String contentUri) {
        this.contentUri = contentUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

}

package com.example.wifip2p.Media;

import android.net.Uri;

public class Document {

    long id;
    Uri contentUri;
    String name;
    String mimeType;
    String mediaType;
    int size;
    boolean isSelected;

    public Document(long id, Uri contentUri, String name, String mimeType, String mediaType, int size, boolean isSelected) {
        this.id = id;
        this.contentUri = contentUri;
        this.name = name;
        this.mimeType = mimeType;
        this.mediaType = mediaType;
        this.size = size;
        this.isSelected = isSelected;
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

    public Uri getContentUri() {
        return contentUri;
    }

    public void setContentUri(Uri contentUri) {
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

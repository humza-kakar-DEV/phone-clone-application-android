package com.example.wifip2p.Media;

import android.net.Uri;

public class Document {

    long id;
    Uri contentUri;
    String name;
    String type;
    int size;

    public Document(long id, Uri contentUri, String name, String type, int size) {
        this.id = id;
        this.contentUri = contentUri;
        this.name = name;
        this.type = type;
        this.size = size;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

}

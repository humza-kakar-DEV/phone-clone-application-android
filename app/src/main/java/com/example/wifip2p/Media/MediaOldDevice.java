package com.example.wifip2p.Media;

import android.content.Context;
import android.util.Log;

import com.example.wifip2p.Utils.Constant;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MediaOldDevice {

    List<Image> imageFileList = new ArrayList<>();
    List<Video> videoFileList = new ArrayList<>();
    List<Audio> audioFileList = new ArrayList<>();
    List<Document> documentFileList = new ArrayList<>();

    public MediaOldDevice() {

    }

    public List<Image> getFileImages(File root) {

        File[] list = root.listFiles();

        for (File f : list) {
            if (f.isDirectory()) {
                getFileImages(f);
            } else {
                if (f.getName().endsWith(".png") || f.getName().endsWith(".jpg")) {
                    imageFileList.add(new Image(f.getAbsolutePath(), f.getName(), 0, 0, false));
                }
            }
        }

        return imageFileList;
    }

    public List<Video> getFileVideos(File root) {

        File[] list = root.listFiles();

        for (File f : list) {
            if (f.isDirectory()) {
                getFileVideos(f);
            } else {
                if (f.getName().endsWith(".mp4")) {
//                    Log.d(Constant.THREAD_TAG, "video: " + f.getName());
                    videoFileList.add(new Video(f.getAbsolutePath(), f.getName(), 0, 0, false));
                }
            }
        }

        return videoFileList;
    }

    public List<Audio> getFileAudios(File root) {

        File[] list = root.listFiles();

        for (File f : list) {
            if (f.isDirectory()) {
                getFileAudios(f);
            } else {
                if (f.getName().endsWith(".mp3")) {
                    audioFileList.add(new Audio(f.getAbsolutePath(), "audio/mpeg", f.getName(), null, 0, false));
//                    Log.d(Constant.THREAD_TAG, "audios: " + f.getName());
                }
            }
        }

        return audioFileList;
    }

    public List<Document> getFileDocuments(File directoryDocuments) {

        File[] list = directoryDocuments.listFiles();

        for (File f : list) {
            if (f.isDirectory()) {
                getFileAudios(f);
            } else {
                documentFileList.add(new Document(0, f.getAbsolutePath(), f.getName(), null, null, 0, false));
//                Log.d(Constant.THREAD_TAG, "documents: " + f.getName());
            }
        }

        return documentFileList;
    }

}

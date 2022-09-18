package com.example.wifip2p;

import android.app.Activity;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.wifip2p.Fragment.FileTypeFragment;
import com.example.wifip2p.Media.Apk;
import com.example.wifip2p.Media.ApkMedia;
import com.example.wifip2p.Media.Audio;
import com.example.wifip2p.Media.AudioMedia;
import com.example.wifip2p.Media.Contact;
import com.example.wifip2p.Media.ContactMedia;
import com.example.wifip2p.Media.Document;
import com.example.wifip2p.Media.DocumentMedia;
import com.example.wifip2p.Media.Image;
import com.example.wifip2p.Media.ImageMedia;
import com.example.wifip2p.Media.MediaOldDevice;
import com.example.wifip2p.Media.Video;
import com.example.wifip2p.Media.VideoMedia;
import com.example.wifip2p.Utils.Constant;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LoadFileThread extends Thread {

    public static final String TAG = "humFileLoad";
    Activity activity;
//    public LoadFileHandler loadFileHandler;

    private List<Image> imageList;
    private List<Video> videoList;
    private List<Audio> audioList;
    private List<Contact> contactList;
    private List<Document> documentList;
    private List<Apk> apkList;

    int imageSize;
    int videoSize;
    int audioSize;
    int contactSize;
    int documentSize;
    int apkSize;

    public LoadFileThread(Activity activity) {
        this.activity = activity;
    }

    public void setImageList(List<Image> imageList) {
        this.imageList = imageList;
    }

    public void setVideoList(List<Video> videoList) {
        this.videoList = videoList;
    }

    public void setAudioList(List<Audio> audioList) {
        this.audioList = audioList;
    }

    public void setContactList(List<Contact> contactList) {
        this.contactList = contactList;
    }

    public void setDocumentList(List<Document> documentList) {
        this.documentList = documentList;
    }

    public void setApkList(List<Apk> apkList) {
        this.apkList = apkList;
    }

    @Override
    public void run() {
        super.run();

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MainActivity2 mainActivity2 = (MainActivity2) activity;
                mainActivity2.startLoadingDialog();
            }
        });

        ContactMedia contactMedia = new ContactMedia(activity.getApplicationContext());
        ApkMedia apkMedia = new ApkMedia(activity.getApplicationContext());

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {

            ImageMedia imageMedia = new ImageMedia(activity.getApplicationContext());
            VideoMedia videoMedia = new VideoMedia(activity.getApplicationContext());
            AudioMedia audioMedia = new AudioMedia(activity.getApplicationContext());
            DocumentMedia documentMedia = new DocumentMedia(activity.getApplicationContext());

            imageList.addAll(imageMedia.generateImages());
            videoList.addAll(videoMedia.generateVideos());
            audioList.addAll(audioMedia.generateAudios());
            documentList.addAll(documentMedia.generateDocuments());
            contactList.addAll(contactMedia.getContactList());
            apkList.addAll(apkMedia.getInstalledPackages());

            imageSize = imageList.size();
            audioSize = audioList.size();
            videoSize = videoList.size();
            documentSize = documentList.size();
            contactSize = contactList.size();
            apkSize = apkList.size();

        } else {

            MediaOldDevice mediaOldDevice = new MediaOldDevice();
            imageList.addAll(mediaOldDevice.getFileImages(Environment.getExternalStorageDirectory()));
            audioList.addAll(mediaOldDevice.getFileAudios(Environment.getExternalStorageDirectory()));
            videoList.addAll(mediaOldDevice.getFileVideos(Environment.getExternalStorageDirectory()));
            documentList.addAll(mediaOldDevice.getFileDocuments(Environment.getExternalStorageDirectory()));
            contactList.addAll(contactMedia.getContactList());
            apkList.addAll(apkMedia.getInstalledPackages());

            imageSize = imageList.size();
            audioSize = audioList.size();
            videoSize = videoList.size();
            documentSize = documentList.size();
            contactSize = contactList.size();
            apkSize = apkList.size();

        }



        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MainActivity2 mainActivity2 = (MainActivity2) activity;
                mainActivity2.loadFileThreadResults(imageSize, audioSize, videoSize, documentSize, contactSize, apkSize);
                mainActivity2.stopLoadingDialog();
            }
        });

    }
}

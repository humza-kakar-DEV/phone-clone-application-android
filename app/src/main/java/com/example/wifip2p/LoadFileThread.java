package com.example.wifip2p;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.wifip2p.Media.Apk;
import com.example.wifip2p.Media.ApkMedia;
import com.example.wifip2p.Media.Audio;
import com.example.wifip2p.Media.AudioMedia;
import com.example.wifip2p.Media.ContactMedia;
import com.example.wifip2p.Media.DocumentMedia;
import com.example.wifip2p.Media.ImageMedia;
import com.example.wifip2p.Media.VideoMedia;

import java.security.Permission;

public class LoadFileThread extends Thread {

    PermissionActivity mainActivity2;
    public static final String TAG = "humLoad";

    public LoadFileThread (PermissionActivity mainActivity2) {
        this.mainActivity2 = mainActivity2;
    }

    @Override
    public void run() {
        super.run();

//            use thread handler logic when u need to send data from activity to thread otherwise
//            writing code in run method is enough

        Log.d(TAG, "file loading");

        ImageMedia imageMedia = new ImageMedia(mainActivity2.getApplicationContext());
        AudioMedia audioMedia = new AudioMedia(mainActivity2.getApplicationContext());
        VideoMedia videoMedia = new VideoMedia(mainActivity2.getApplicationContext());
        ContactMedia contactMedia = new ContactMedia(mainActivity2.getApplicationContext());
        DocumentMedia documentMedia = new DocumentMedia(mainActivity2.getApplicationContext());
        ApkMedia apkMedia = new ApkMedia(mainActivity2.getApplicationContext());

        int imageSize = imageMedia.getImageSize();
        int audioSize = audioMedia.getAudioSize();
        int videoSize = videoMedia.getVideoSize();
        int contactSize = contactMedia.getContactList().size();
        int documentSize = documentMedia.getDocumentSize();
        int apkSize = apkMedia.getApkSize();

        mainActivity2.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mainActivity2.fileLoadedFromThread(imageSize, audioSize, videoSize, contactSize, documentSize, apkSize);
            }
        });
    }

    class LoadFileHandler extends Handler {

//            use thread handler logic when u need to send data from activity to thread otherwise
//            writing code in run method is enough

        PermissionActivity mainActivity2;

        public LoadFileHandler (PermissionActivity mainActivity2) {
            this.mainActivity2 = mainActivity2;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            Log.d(TAG, "file loading");


            ImageMedia imageMedia = new ImageMedia(mainActivity2.getApplicationContext());
            AudioMedia audioMedia = new AudioMedia(mainActivity2.getApplicationContext());
            VideoMedia videoMedia = new VideoMedia(mainActivity2.getApplicationContext());
            ContactMedia contactMedia = new ContactMedia(mainActivity2.getApplicationContext());
            DocumentMedia documentMedia = new DocumentMedia(mainActivity2.getApplicationContext());

            int imageSize = imageMedia.getImageSize();
            int audioSize = audioMedia.getAudioSize();
            int videoSize = videoMedia.getVideoSize();
            int contactSize = contactMedia.getContactList().size();
            int documentSize = documentMedia.getDocumentSize();

            mainActivity2.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mainActivity2.fileLoadedFromThread(imageSize, audioSize, videoSize, contactSize, documentSize, 0);
                }
            });
        }
    }
}

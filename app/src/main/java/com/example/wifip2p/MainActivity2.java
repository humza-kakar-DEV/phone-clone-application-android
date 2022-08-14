package com.example.wifip2p;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.wifip2p.Fragment.FileShareFragment;
import com.example.wifip2p.Fragment.FileShowFragment;
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
import com.example.wifip2p.Media.Video;
import com.example.wifip2p.Media.VideoMedia;
import com.example.wifip2p.Utils.CommunicationInterface;
import com.example.wifip2p.Utils.CommunicationInterfaceReference;

import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity implements CommunicationInterface {

    private static final String TAG = "hmMainActivity2";
    public static final String TAG_FILE_THREAD = "humLoad";
    private List<Image> imageMegaList = new ArrayList<Image>();
    private List<Video> videoMegaList = new ArrayList<>();
    private List<Audio> audioMegaList = new ArrayList<>();
    private List<Contact> contactMegaList = new ArrayList<>();
    private List<Document> documentMegaList = new ArrayList<>();
    private List<Apk> apkMegaList = new ArrayList<>();

    int imageSize;
    int audioSize;
    int videoSize;
    int documentSize;
    int contactSize;
    int apkSize;

    private FrameLayout frameLayout;
    private FileShowFragment fileShowFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        frameLayout = findViewById(R.id.frameLayout);

        if (getIntent() != null) {
            imageSize = getIntent().getIntExtra("per1", 0);
            audioSize = getIntent().getIntExtra("per2", 0);
            videoSize = getIntent().getIntExtra("per3", 0);
            documentSize = getIntent().getIntExtra("per4", 0);
            contactSize = getIntent().getIntExtra("per5", 0);
            apkSize = getIntent().getIntExtra("per6", 0);
        }

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.anim.fragment_slide_in,  // enter
                        R.anim.fragment_fade_out,  // exit
                        R.anim.fragment_fade_in,   // popEnter
                        R.anim.fragment_slide_out  // popExit
                )
                .replace(frameLayout.getId(), new FileTypeFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void changeFragment(String fileType, List<?> dynamicList) {

                fileShowFragment = FileShowFragment.newInstance(fileType, dynamicList);
                getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                        R.anim.fragment_slide_in,  // enter
                        R.anim.fragment_fade_out,  // exit
                        R.anim.fragment_fade_in,   // popEnter
                        R.anim.fragment_slide_out  // popExit
                    )
                    .replace(frameLayout.getId(), fileShowFragment)
                    .addToBackStack(null)
                    .commit();

    }

    @Override
    public void clearList(String name) {
        switch (name) {
            case "image":
                imageMegaList.clear();
                break;
            case "video":
                videoMegaList.clear();
                break;
            case "audio":
                audioMegaList.clear();
                break;
            case "contact":
                contactMegaList.clear();
                break;
            case "document":
                documentMegaList.clear();
                break;
            case "apk":
                apkMegaList.clear();
                break;
        }
    }

    @Override
    public void singleSelection(Object object, String objectType, boolean state) {
        switch (objectType) {
            case "image":
                Image image = (Image) object;
                if (state && !imageMegaList.contains(image)) {
                    imageMegaList.add(image);
                } else {
                    imageMegaList.remove(image);
                }
                break;
            case "video":
                Video video = (Video) object;
                if (state && !videoMegaList.contains(video)) {
                    videoMegaList.add(video);
                } else {
                    videoMegaList.remove(video);
                }
                break;
            case "audio":
                Audio audio = (Audio) object;
                if (state && !audioMegaList.contains(audio)) {
                    audioMegaList.add(audio);
                } else {
                    audioMegaList.remove(audio);
                }
                break;
            case "contact":
                Contact contact = (Contact) object;
                if (state && !contactMegaList.contains(contact)) {
                    contactMegaList.add(contact);
                } else {
                    contactMegaList.remove(contact);
                }
                break;
            case "document":
                Document document = (Document) object;
                if (state && !documentMegaList.contains(document)) {
                    documentMegaList.add(document);
                } else {
                    documentMegaList.remove(document);
                }
                break;
            case "apk":
                Apk apk = (Apk) object;
                if (state && !apkMegaList.contains(apk)) {
                    apkMegaList.add(apk);
                } else {
                    apkMegaList.remove(apk);
                }
                break;
        }
    }

    @Override
    public void checkSelection(CheckBox checkBox, String type) {
        if (imageMegaList.size() == imageSize && type.equals("image")) {
            checkBox.setChecked(true);
        }
        else if (videoMegaList.size() == videoSize && type.equals("video")) {
            checkBox.setChecked(true);
        } else if (audioMegaList.size() == audioSize && type.equals("audio")) {
            checkBox.setChecked(true);
        }
        else if (contactMegaList.size() == contactSize && type.equals("contact")) {
            checkBox.setChecked(true);
        }
        else if (documentMegaList.size() == documentSize && type.equals("document")) {
            checkBox.setChecked(true);
        }
//        else if (apkMegaList.size() == apkMedia.getApkSize() && type.equals("apk")) {
//            checkBox.setChecked(true);
//        }
        else {
            checkBox.setChecked(false);
        }
    }

    @Override
    public void sendData() {
        Log.d(TAG, "image size: " + imageMegaList.size());
        Log.d(TAG, "video size: " + videoMegaList.size());
        Log.d(TAG, "audio size: " + audioMegaList.size());
        Log.d(TAG, "contact size: " + contactMegaList.size());
        Log.d(TAG, "document size: " + documentMegaList.size());
        Log.d(TAG, "apk size: " + apkMegaList.size());

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.anim.fragment_slide_in,  // enter
                        R.anim.fragment_fade_out,  // exit
                        R.anim.fragment_fade_in,   // popEnter
                        R.anim.fragment_slide_out  // popExit
                )
                .replace(frameLayout.getId(), FileShareFragment.newInstance(null, audioMegaList))
                .addToBackStack(null)
                .commit();
    }
}
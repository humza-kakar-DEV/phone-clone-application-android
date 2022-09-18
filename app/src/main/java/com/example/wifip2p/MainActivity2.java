package com.example.wifip2p;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.FrameLayout;

import com.example.wifip2p.Fragment.FileShareFragment;
import com.example.wifip2p.Fragment.FileShowFragment;
import com.example.wifip2p.Fragment.FileTypeFragment;
import com.example.wifip2p.Media.Apk;
import com.example.wifip2p.Media.Audio;
import com.example.wifip2p.Media.Contact;
import com.example.wifip2p.Media.Document;
import com.example.wifip2p.Media.Image;
import com.example.wifip2p.Media.Video;
import com.example.wifip2p.Utils.CommunicationInterface;
import com.example.wifip2p.Utils.Constant;
import com.example.wifip2p.Utils.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity implements CommunicationInterface {

    private static final String TAG = "hmMainActivity2";
    public static final String TAG_FILE_THREAD = "humLoad";
    private final List<Image> imageMegaList = new ArrayList<Image>();
    private final List<Video> videoMegaList = new ArrayList<>();
    private final List<Audio> audioMegaList = new ArrayList<>();
    private final List<Contact> contactMegaList = new ArrayList<>();
    private final List<Document> documentMegaList = new ArrayList<>();
    private final List<Apk> apkMegaList = new ArrayList<>();
    int imageSize;
    int audioSize;
    int videoSize;
    int documentSize;
    int contactSize;
    int apkSize;
    String groupOwnerAddress;
    ClientThread clientThread;

    private FrameLayout frameLayout;
    private FileTypeFragment fileTypeFragment;
    private FileShowFragment fileShowFragment;
    private FileShareFragment fileShareFragment;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        frameLayout = findViewById(R.id.frameLayout);

        loadingDialog = new LoadingDialog(this);

//! LOAD DATA HERE
//! ------------------------------------------------------------------

        if (getIntent() != null) {
            groupOwnerAddress = getIntent().getStringExtra(Constant.GROUP_OWNER_TAG);
        }

        clientThread = new ClientThread(MainActivity2.this);
        clientThread.setName("client thread");
        clientThread.start();

        fileTypeFragment = new FileTypeFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.anim.fragment_slide_in,  // enter
                        R.anim.fragment_fade_out,  // exit
                        R.anim.fragment_fade_in,   // popEnter
                        R.anim.fragment_slide_out  // popExit
                )
                .replace(frameLayout.getId(), fileTypeFragment)
                .addToBackStack(null)
                .commit();
    }

    public void loadFileThreadResults (int imageSize, int audioSize, int videoSize, int documentSize, int contactSize, int apkSize) {
        this.imageSize = imageSize;
        this.audioSize = audioSize;
        this.videoSize = videoSize;
        this.documentSize = documentSize;
        this.contactSize = contactSize;
        this.apkSize = apkSize;
    }

    public void startLoadingDialog () {
        loadingDialog.startLoadingDialog();
    }

    public void stopLoadingDialog () {
        loadingDialog.dismissDialog();
//!        this method will trigger after
//!        files are loaded in LoadFileThread
        fileTypeFragment.loadFileThreadResults();
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
            default:
                imageMegaList.clear();
                videoMegaList.clear();
                audioMegaList.clear();
                audioMegaList.clear();
                contactMegaList.clear();
                documentMegaList.clear();
                apkMegaList.clear();
                break;
        }
    }

    @Override
    public void allSelectionInterfaceMethod(List<Image> imageList, List<Audio> audioList, List<Video> videoList, List<Contact> contactList, List<Document> documentList, List<Apk> apkList) {
        imageMegaList.addAll(imageList);
        audioMegaList.addAll(audioList);
        videoMegaList.addAll(videoList);
        contactMegaList.addAll(contactList);
        documentMegaList.addAll(documentList);
        apkMegaList.addAll(apkList);
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
        } else if (videoMegaList.size() == videoSize && type.equals("video")) {
            checkBox.setChecked(true);
        } else if (audioMegaList.size() == audioSize && type.equals("audio")) {
            checkBox.setChecked(true);
        } else if (contactMegaList.size() == contactSize && type.equals("contact")) {
            checkBox.setChecked(true);
        } else if (documentMegaList.size() == documentSize && type.equals("document")) {
            checkBox.setChecked(true);
        } else checkBox.setChecked(apkMegaList.size() == apkSize && type.equals("apk"));
    }

    @Override
    public void sendData() {
        Log.d(Constant.MAIN_ACTIVITY_TAG, "image size: " + imageMegaList.size());
        Log.d(Constant.MAIN_ACTIVITY_TAG, "video size: " + videoMegaList.size());
        Log.d(Constant.MAIN_ACTIVITY_TAG, "audio size: " + audioMegaList.size());
        Log.d(Constant.MAIN_ACTIVITY_TAG, "contact size: " + contactMegaList.size());
        Log.d(Constant.MAIN_ACTIVITY_TAG, "document size: " + documentMegaList.size());
        Log.d(Constant.MAIN_ACTIVITY_TAG, "apk size: " + apkMegaList.size());

        if (imageMegaList.size() == 0 && videoMegaList.size() == 0 && audioMegaList.size() == 0 && contactMegaList.size() == 0 && documentMegaList.size() == 0 && apkMegaList.size() == 0) {
            return;
        }

        fileShareFragment = FileShareFragment.newInstance(null, audioMegaList);

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.anim.fragment_slide_in,  // enter
                        R.anim.fragment_fade_out,  // exit
                        R.anim.fragment_fade_in,   // popEnter
                        R.anim.fragment_slide_out  // popExit
                )
                .replace(frameLayout.getId(), fileShareFragment)
                .addToBackStack(null)
                .commit();

        fileShareFragment.setClientThread(clientThread);
        fileShareFragment.setGroupOwnerAddress(groupOwnerAddress);

        fileShareFragment.setTotalImageSize(imageSize);
        fileShareFragment.setTotalAudioSize(audioSize);
        fileShareFragment.setTotalVideoSize(videoSize);
        fileShareFragment.setTotalDocumentSize(documentSize);
        fileShareFragment.setTotalContactSize(contactSize);
        fileShareFragment.setTotalApkSize(apkSize);

        fileShareFragment.setImageList(imageMegaList);
        fileShareFragment.setAudioList(audioMegaList);
        fileShareFragment.setVideoList(videoMegaList);
        fileShareFragment.setDocumentList(documentMegaList);
        fileShareFragment.setContactList(contactMegaList);
        fileShareFragment.setApkList(apkMegaList);
    }

    public void clientThreadResult(int totalFileSize, int currentFileSize, String fileName, String fileType) {
        if (fileShareFragment != null) {
            fileShareFragment.clientThreadResult(totalFileSize, currentFileSize, fileName, fileType);
        }
    }

    public void clientThreadResultFileSize (String fileType) {
        if (fileShareFragment != null) {
            fileShareFragment.clientThreadResultFileSize(fileType);
        }
    }

}
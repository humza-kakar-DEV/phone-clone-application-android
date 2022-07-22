package com.example.wifip2p;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.wifip2p.Fragment.FileShowFragment;
import com.example.wifip2p.Fragment.FileTypeFragment;
import com.example.wifip2p.Media.Audio;
import com.example.wifip2p.Media.AudioMedia;
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
    private List<Image> imageMegaList = new ArrayList<Image>();
    private List<Video> videoMegaList = new ArrayList<>();
    private List<Audio> audioMegaList = new ArrayList<>();
    private List<Image> imageList = new ArrayList<>();
    private List<Video> videoList = new ArrayList<>();
    private List<Audio> audioList = new ArrayList<>();

    private FrameLayout frameLayout;
    private FileTypeFragment fileTypeFragment;
    private FileShowFragment fileShowFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        frameLayout = findViewById(R.id.frameLayout);

        ImageMedia imageMedia = new ImageMedia(this);
        VideoMedia videoMedia = new VideoMedia(this);
        AudioMedia audioMedia = new AudioMedia(this);

//        imageList.addAll(imageMedia.generateImages());
//        videoList.addAll(videoMedia.generateVideos());
//        audioList.addAll(audioMedia.generateAudios());

        fileTypeFragment = FileTypeFragment.newInstance(imageMedia.generateImages(), videoMedia.generateVideos(), audioMedia.generateAudios());
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
        }
    }

    @Override
    public void checkSelection(CheckBox checkBox, String type) {
        if (imageMegaList.size() == 51 && type.equals("image")) {
            checkBox.setChecked(true);
        } else if (videoMegaList.size() == 3 && type.equals("video")) {
            checkBox.setChecked(true);
        } else if (audioMegaList.size() == 51 && type.equals("audio")) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }
    }

    @Override
    public void sendData() {
        Toast.makeText(MainActivity2.this, "image size: " + imageMegaList.size(), Toast.LENGTH_SHORT).show();
        Toast.makeText(MainActivity2.this, "video size: " + videoMegaList.size(), Toast.LENGTH_SHORT).show();
        Toast.makeText(MainActivity2.this, "audio size: " + audioMegaList.size(), Toast.LENGTH_SHORT).show();
    }
}
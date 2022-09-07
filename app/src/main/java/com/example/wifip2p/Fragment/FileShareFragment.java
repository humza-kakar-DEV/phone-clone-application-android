package com.example.wifip2p.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.wifip2p.ClientThread;
import com.example.wifip2p.Media.Apk;
import com.example.wifip2p.Media.Audio;
import com.example.wifip2p.Media.AudioMedia;
import com.example.wifip2p.Media.Contact;
import com.example.wifip2p.Media.Document;
import com.example.wifip2p.Media.Image;
import com.example.wifip2p.Media.ImageMedia;
import com.example.wifip2p.Media.Video;
import com.example.wifip2p.Utils.CommunicationInterface;
import com.example.wifip2p.Utils.CommunicationInterfaceReference;
import com.example.wifip2p.Utils.Constant;
import com.example.wifip2p.databinding.FragmentFileShareBinding;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class FileShareFragment extends Fragment {

    public static final String TAG = "hmShare";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String ARG_PARAM3 = "param3";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Context context;
    FragmentFileShareBinding binding;
    List<Image> imageList = new ArrayList<>();
    List<Audio> audioList = new ArrayList<>();
    List<Video> videoList = new ArrayList<>();
    List<Document> documentList = new ArrayList<>();
    List<Contact> contactList = new ArrayList<>();
    List<Apk> apkList = new ArrayList<>();
    private ClientThread clientThread;
    private String groupOwnerAddress;
    private boolean loadingState;

    private int totalImageSize;
    private int totalAudioSize;
    private int totalVideoSize;
    private int totalDocumentSize;
    private int totalContactSize;
    private int totalApkSize;
    private String currentSharedFile;

    private CommunicationInterfaceReference communicationInterfaceReference;

    public FileShareFragment() {
        // Required empty public constructor
    }

    public static FileShareFragment newInstance(String param1, List<Audio> audioList) {
        FileShareFragment fragment = new FileShareFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putSerializable(ARG_PARAM2, (Serializable) audioList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            audioList = (List<Audio>) getArguments().getSerializable(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFileShareBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        context = view.getContext();

//?        display loading screen when data
//?        is transferred to file share fragment
//?        from mainActivity2.java

        dataListLogic();

        setupTextViews();

        clientThreadSendMessage();

        return view;
    }

    public void setupTextViews() {
        binding.imageTextView.setText(totalImageSize + "/ " + imageList.size() + " items ");
        binding.audioTextView.setText(totalAudioSize + " / " + audioList.size() + " items ");
        binding.videoTextView.setText(totalVideoSize + " / " + videoList.size() + " items ");
        binding.documentTextView.setText(totalDocumentSize + " / " + documentList.size() + " items ");
        binding.contactTextView.setText(totalContactSize + " / " + contactList.size() + " items ");
        binding.apkTextView.setText(totalApkSize + " / " + apkList.size() + " items ");
    }

    public void setupMaterialCardLogic() {
    }

    public void dataListLogic() {
        totalImageSize = imageList.size();
        totalAudioSize = audioList.size();
        totalVideoSize = videoList.size();
        totalDocumentSize = documentList.size();
        totalContactSize = contactList.size();
        totalApkSize = apkList.size();
    }

    public void displayLoadingScreen(boolean state) {
        if (state) {
            loadingState = state;
//?            loading screen view visible
//            Toast.makeText(context, "data preparing", Toast.LENGTH_SHORT).show();
        } else {
            loadingState = state;
//?           loading screen view gone
//            Toast.makeText(context, "loading finished!", Toast.LENGTH_SHORT).show();
        }
    }

    public void clientThreadResult(int totalFileSize, int currentFileSize, String fileName, String fileType) {
        binding.fileTypeHeadingTextView.setText(fileType);
        binding.fileNameTextView.setText(fileName);
        binding.progressBar.setMax(totalFileSize);
        binding.progressBar.setProgress(currentFileSize);
    }

    public void clientThreadResultFileSize(String fileType) {
        switch (fileType) {
            case "Image":
                totalImageSize--;
                break;
            case "Audio":
                totalAudioSize--;
                break;
            case "Video":
                totalVideoSize--;
                break;
            case "Document":
                totalDocumentSize--;
                break;
            case "Contact":
                totalContactSize--;
                break;
            case "Apk":
                totalApkSize--;
                break;
        }
        setupTextViews();
    }

    public void clientThreadSendMessage() {

        if (groupOwnerAddress == null) {
            return;
        }

        sendImageData();
        sendAudioData();
        sendVideoData();
        sendDocumentData();
        sendContactData();
        sendApkData();
    }

    public void sendImageData() {
        if (imageList.size() == 0) {
            return;
        }

//        ImageMedia imageMedia = new ImageMedia(context);

        Toast.makeText(context, "image called", Toast.LENGTH_SHORT).show();

        for (Image image : imageList) {

            Bundle bundle = new Bundle();
            bundle.putString(Constant.GROUP_OWNER_TAG, groupOwnerAddress);
            bundle.putSerializable(Constant.IMAGE_TAG, image);

            Message message = Message.obtain();
            message.setData(bundle);

            clientThread.clientThreadHandler.sendMessage(message);
        }
    }

    public void sendAudioData() {
        if (audioList.size() == 0) {
            return;
        }

        Toast.makeText(context, "audio called", Toast.LENGTH_SHORT).show();

//        AudioMedia audioMedia = new AudioMedia(context);

        for (Audio audio : audioList) {

            Bundle bundle = new Bundle();
            bundle.putString(Constant.GROUP_OWNER_TAG, groupOwnerAddress);
            bundle.putSerializable(Constant.AUDIO_TAG, audio);
            bundle.putInt(Constant.DYNAMIC_OBJ_TAG, 1);

            Message message = Message.obtain();
            message.setData(bundle);

            clientThread.clientThreadHandler.sendMessage(message);
        }
    }

    public void sendVideoData() {
        if (videoList.size() == 0) {
            return;
        }

        Toast.makeText(context, "video called", Toast.LENGTH_SHORT).show();

        for (Video video : videoList) {
            Bundle bundle = new Bundle();
            bundle.putString(Constant.GROUP_OWNER_TAG, groupOwnerAddress);
            bundle.putSerializable(Constant.VIDEO_TAG, video);

            Message message = Message.obtain();
            message.setData(bundle);

            clientThread.clientThreadHandler.sendMessage(message);
        }
    }

    public void sendDocumentData() {
        if (documentList.size() == 0) {
            return;
        }

        Toast.makeText(context, "document called", Toast.LENGTH_SHORT).show();


        for (Document document : documentList) {
            Bundle bundle = new Bundle();
            bundle.putString(Constant.GROUP_OWNER_TAG, groupOwnerAddress);
            bundle.putSerializable(Constant.DOCUMENT_TAG, document);

            Message message = Message.obtain();
            message.setData(bundle);

            clientThread.clientThreadHandler.sendMessage(message);
        }
    }

    public void sendContactData() {
        if (contactList.size() == 0) {
            return;
        }

        Toast.makeText(context, "contact called", Toast.LENGTH_SHORT).show();

        for (Contact contact : contactList) {
            Bundle bundle = new Bundle();
            bundle.putString(Constant.GROUP_OWNER_TAG, groupOwnerAddress);
            bundle.putSerializable(Constant.CONTACT_TAG, contact);

            Message message = Message.obtain();
            message.setData(bundle);

            clientThread.clientThreadHandler.sendMessage(message);
        }
    }

    public void sendApkData() {
        if (apkList.size() == 0) {
            return;
        }

        Toast.makeText(context, "apk called", Toast.LENGTH_SHORT).show();


        for (Apk apk : apkList) {
            Bundle bundle = new Bundle();
            bundle.putString(Constant.GROUP_OWNER_TAG, groupOwnerAddress);
            bundle.putSerializable(Constant.APK_TAG, apk);

            Message message = Message.obtain();
            message.setData(bundle);

            clientThread.clientThreadHandler.sendMessage(message);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof CommunicationInterface) {
            communicationInterfaceReference = new CommunicationInterfaceReference(context);
        }
    }

    public ClientThread getClientThread() {
        return clientThread;
    }

    public void setClientThread(ClientThread clientThread) {
        this.clientThread = clientThread;
    }

    public String getGroupOwnerAddress() {
        return groupOwnerAddress;
    }

    public void setGroupOwnerAddress(String groupOwnerAddress) {
        this.groupOwnerAddress = groupOwnerAddress;
    }

    public void setTotalImageSize(int totalImageSize) {
        this.totalImageSize = totalImageSize;
    }

    public void setTotalAudioSize(int totalAudioSize) {
        this.totalAudioSize = totalAudioSize;
    }

    public void setTotalVideoSize(int totalVideoSize) {
        this.totalVideoSize = totalVideoSize;
    }

    public void setTotalDocumentSize(int totalDocumentSize) {
        this.totalDocumentSize = totalDocumentSize;
    }

    public void setTotalContactSize(int totalContactSize) {
        this.totalContactSize = totalContactSize;
    }

    public void setTotalApkSize(int totalApkSize) {
        this.totalApkSize = totalApkSize;
    }

    public void setCurrentSharedFile(String currentSharedFile) {
        this.currentSharedFile = currentSharedFile;
    }

    public void setImageList(List<Image> imageList) {
        this.imageList.addAll(imageList);
    }

    public void setAudioList(List<Audio> audioList) {
        this.audioList.addAll(audioList);
    }

    public void setVideoList(List<Video> videoList) {
        this.videoList.addAll(videoList);
    }

    public void setDocumentList(List<Document> documentList) {
        this.documentList.addAll(documentList);
    }

    public void setContactList(List<Contact> contactList) {
        this.contactList.addAll(contactList);
    }

    public void setApkList(List<Apk> apkList) {
        this.apkList.addAll(apkList);
    }
}
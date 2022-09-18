package com.example.wifip2p.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.wifip2p.Adapter.FileRecyclerViewAdapter;
import com.example.wifip2p.MainActivity2;
import com.example.wifip2p.Media.Apk;
import com.example.wifip2p.Media.Audio;
import com.example.wifip2p.Media.Contact;
import com.example.wifip2p.Media.Document;
import com.example.wifip2p.Media.Image;
import com.example.wifip2p.Media.Video;
import com.example.wifip2p.Utils.CommunicationInterface;
import com.example.wifip2p.Utils.CommunicationInterfaceReference;
import com.example.wifip2p.databinding.FragmentFileShowBinding;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleConsumer;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FileShowFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FileShowFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = "hmFileShow";

    private String mParam1;
    private List<?> mParam2;
    private List<Image> imageList = new ArrayList<Image>();
    private List<Video> videoList = new ArrayList<Video>();
    private List<Audio> audioList = new ArrayList<Audio>();
    private List<Contact> contactList = new ArrayList<Contact>();
    private List<Document> documentList = new ArrayList<Document>();
    private List<Apk> apkList = new ArrayList<Apk>();
    private String fileType;

    private Context context;
    private FileRecyclerViewAdapter fileRecyclerViewAdapter;

    FragmentFileShowBinding binding;
    CommunicationInterfaceReference communicationInterfaceReference;

    public FileShowFragment() {
        // Required empty public constructor
    }

    public static FileShowFragment newInstance(String param1, List<?> param2) {
        FileShowFragment fragment = new FileShowFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putSerializable(ARG_PARAM2, (Serializable) param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = (List<?>) getArguments().getSerializable(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFileShowBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        context = view.getContext();

        switch (mParam1) {
            case "image":
                binding.textViewHeading.setText("IMAGES");
                imageList.addAll((List<Image>) mParam2);
                communicationInterfaceReference.invokeCheckSelection(binding.allSelectCheckBox, mParam1);
                fileType = "image";
                fileRecyclerViewAdapter = new FileRecyclerViewAdapter (imageList, fileType, binding.allSelectCheckBox, context, getActivity());
                break;
            case "video":
                binding.textViewHeading.setText("VIDEOS");
                videoList.addAll((List<Video>) mParam2);
                communicationInterfaceReference.invokeCheckSelection(binding.allSelectCheckBox, mParam1);
                fileType = "video";
                fileRecyclerViewAdapter = new FileRecyclerViewAdapter (videoList, fileType, binding.allSelectCheckBox, context, getActivity());
                break;
            case "audio":
                binding.textViewHeading.setText("AUDIOS");
                audioList.addAll((List<Audio>) mParam2);
                communicationInterfaceReference.invokeCheckSelection(binding.allSelectCheckBox, mParam1);
                fileType = "audio";
                fileRecyclerViewAdapter = new FileRecyclerViewAdapter (audioList, fileType, binding.allSelectCheckBox, context, getActivity());
                break;
            case "contact":
                binding.textViewHeading.setText("CONTACTS");
                contactList.addAll((List<Contact>) mParam2);
                communicationInterfaceReference.invokeCheckSelection(binding.allSelectCheckBox, mParam1);
                fileType = "contact";
                fileRecyclerViewAdapter = new FileRecyclerViewAdapter (contactList, fileType, binding.allSelectCheckBox, context, getActivity());
                break;
            case "document":
                binding.textViewHeading.setText("DOCUMENTS");
                documentList.addAll((List<Document>) mParam2);
                Log.d(TAG, "onCreateView: " + documentList.size());
                communicationInterfaceReference.invokeCheckSelection(binding.allSelectCheckBox, mParam1);
                fileType = "document";
                fileRecyclerViewAdapter = new FileRecyclerViewAdapter (documentList, fileType, binding.allSelectCheckBox, context, getActivity());
                break;
            case "apk":
                binding.textViewHeading.setText("APPLICATIONS");
                apkList.addAll((List<Apk>) mParam2);
                communicationInterfaceReference.invokeCheckSelection(binding.allSelectCheckBox, mParam1);
                fileType = "apk";
                fileRecyclerViewAdapter = new FileRecyclerViewAdapter (apkList, fileType, binding.allSelectCheckBox, context, getActivity());
                break;
        }

//        recyclerView.setHasFixedSize(true);
//        binding.recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        binding.recyclerView.setAdapter(fileRecyclerViewAdapter);

        binding.doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        binding.allSelectCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.allSelectCheckBox.isChecked()) {
                        switch (fileType) {
                            case "image":
                                for (Image image : imageList) {
                                    communicationInterfaceReference.invokeSingleSelection(image, "image", true);
                                    image.setSelected(true);
                                }
                                fileRecyclerViewAdapter.notifyDataSetChanged();
                                break;
                            case "video":
                                for (Video video : videoList) {
                                    communicationInterfaceReference.invokeSingleSelection(video, "video", true);
                                    video.setSelected(true);
                                }
                                fileRecyclerViewAdapter.notifyDataSetChanged();
                                break;
                            case "audio":
                                for (Audio audio : audioList) {
                                    communicationInterfaceReference.invokeSingleSelection(audio, "audio", true);
                                    audio.setSelected(true);
                                }
                                fileRecyclerViewAdapter.notifyDataSetChanged();
                                break;
                            case "contact":
                                for (Contact contact : contactList) {
                                    communicationInterfaceReference.invokeSingleSelection(contact, "contact", true);
                                    contact.setSelected(true);
                                }
                                fileRecyclerViewAdapter.notifyDataSetChanged();
                                break;
                            case "document":
                                for (Document document : documentList) {
                                    communicationInterfaceReference.invokeSingleSelection(document, "document", true);
                                    document.setSelected(true);
                                }
                                fileRecyclerViewAdapter.notifyDataSetChanged();
                                break;
                            case "apk":
                                for (Apk apk : apkList) {
                                    communicationInterfaceReference.invokeSingleSelection(apk, "apk", true);
                                    apk.setSelected(true);
                                }
                                fileRecyclerViewAdapter.notifyDataSetChanged();
                                break;
                        }
                    } else {
                        switch (fileType) {
                            case "image":
                                for (Image image : imageList) {
                                    communicationInterfaceReference.invokeSingleSelection(image, "image", false);
                                    image.setSelected(false);
                                }
                                fileRecyclerViewAdapter.notifyDataSetChanged();
                                break;
                            case "video":
                                for (Video video : videoList) {
                                    communicationInterfaceReference.invokeSingleSelection(video, "video", false);
                                    video.setSelected(false);
                                }
                                fileRecyclerViewAdapter.notifyDataSetChanged();
                                break;
                            case "audio":
                                for (Audio audio : audioList) {
                                    communicationInterfaceReference.invokeSingleSelection(audio, "audio", false);
                                    audio.setSelected(false);
                                }
                                fileRecyclerViewAdapter.notifyDataSetChanged();
                                break;
                            case "contact":
                                for (Contact contact : contactList) {
                                    communicationInterfaceReference.invokeSingleSelection(contact, "contact", false);
                                    contact.setSelected(false);
                                }
                                fileRecyclerViewAdapter.notifyDataSetChanged();
                                break;
                            case "document":
                                for (Document document : documentList) {
                                    communicationInterfaceReference.invokeSingleSelection(document, "document", false);
                                    document.setSelected(false);
                                }
                                fileRecyclerViewAdapter.notifyDataSetChanged();
                                break;
                            case "apk":
                                for (Apk apk : apkList) {
                                    communicationInterfaceReference.invokeSingleSelection(apk, "apk", false);
                                    apk.setSelected(false);
                                }
                                fileRecyclerViewAdapter.notifyDataSetChanged();
                                break;
                        }
                    }
                }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof CommunicationInterface) {
            communicationInterfaceReference = new CommunicationInterfaceReference (context);
        }
    }
}
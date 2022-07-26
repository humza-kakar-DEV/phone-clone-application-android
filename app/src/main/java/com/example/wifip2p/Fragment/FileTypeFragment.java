package com.example.wifip2p.Fragment;

import android.content.Context;
import android.hardware.lights.LightState;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.wifip2p.Media.Apk;
import com.example.wifip2p.Media.ApkMedia;
import com.example.wifip2p.Media.Audio;
import com.example.wifip2p.Media.Contact;
import com.example.wifip2p.Media.Document;
import com.example.wifip2p.Media.Image;
import com.example.wifip2p.Media.Video;
import com.example.wifip2p.R;
import com.example.wifip2p.Utils.CommunicationInterface;
import com.example.wifip2p.Utils.CommunicationInterfaceReference;
import com.example.wifip2p.databinding.FragmentFileTypeBinding;

import java.io.Serializable;
import java.util.List;

public class FileTypeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private static final String ARG_PARAM5 = "param5";
    private static final String ARG_PARAM6 = "param6";
    private static final String TAG = "hmFileType";

    private List<Image> imageList;
    private List<Video> videoList;
    private List<Audio> audioList;
    private List<Contact> contactList;
    private List<Document> documentList;
    private List<Apk> apkList;

    private Context context;
    private CommunicationInterfaceReference communicationInterfaceReference;

    final int[] drawableId = new int[] {
            R.drawable.ic_baseline_person_24,
            R.drawable.ic_baseline_image_24,
            R.drawable.ic_baseline_video_library_24,
            R.drawable.ic_baseline_audiotrack_24,
            R.drawable.ic_baseline_article_24,
            R.drawable.ic_baseline_apps_24,
    };
    final String[] fileType = new String[] {
            "Contacts",
            "Images",
            "Videos",
            "Audios",
            "Documents",
            "Apps",
    };

    FragmentFileTypeBinding binding;

    public FileTypeFragment() {

    }

    public static FileTypeFragment newInstance(List<Image> imageList, List<Video> videoList, List<Audio> audioList, List<Contact> contactList, List<Document> documentList, List<Apk> apkList) {
        FileTypeFragment fragment = new FileTypeFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, (Serializable) imageList);
        args.putSerializable(ARG_PARAM2, (Serializable) videoList);
        args.putSerializable(ARG_PARAM3, (Serializable) audioList);
        args.putSerializable(ARG_PARAM4, (Serializable) contactList);
        args.putSerializable(ARG_PARAM5, (Serializable) documentList);
        args.putSerializable(ARG_PARAM6, (Serializable) apkList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            imageList = (List<Image>) getArguments().getSerializable(ARG_PARAM1);
            videoList = (List<Video>) getArguments().getSerializable(ARG_PARAM2);
            audioList = (List<Audio>) getArguments().getSerializable(ARG_PARAM3);
            contactList = (List<Contact>) getArguments().getSerializable(ARG_PARAM4);
            documentList = (List<Document>) getArguments().getSerializable(ARG_PARAM5);
            apkList = (List<Apk>) getArguments().getSerializable(ARG_PARAM6);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = FragmentFileTypeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        context = view.getContext();

        //        text views
        binding.imageTextView.setText(imageList.size() + " items");
        binding.videoTextView.setText(videoList.size() + " items");
        binding.audioTextView.setText(audioList.size() + " items");
        binding.contactTextView.setText(contactList.size() + " items");
        binding.documentTextView.setText(documentList.size() + " items");
        binding.apkTextView.setText(apkList.size() + " items");

        binding.allSelectCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.allSelectCheckBox.isChecked()) {
                    binding.imageCheckBox.setChecked(true);
                    binding.videoCheckBox.setChecked(true);
                    binding.audioCheckBox.setChecked(true);
                    binding.contactCheckBox.setChecked(true);
                    binding.documentCheckBox.setChecked(true);
                    binding.apkCheckBox.setChecked(true);
                } else {
                    binding.imageCheckBox.setChecked(false);
                    binding.videoCheckBox.setChecked(false);
                    binding.audioCheckBox.setChecked(false);
                    binding.contactCheckBox.setChecked(false);
                    binding.documentCheckBox.setChecked(false);
                    binding.apkCheckBox.setChecked(false);
                }
            }
        });

        if (binding.imageCheckBox.isSelected() && binding.videoCheckBox.isChecked() && binding.audioCheckBox.isChecked() && binding.documentCheckBox.isChecked() && binding.apkCheckBox.isChecked() && binding.contactCheckBox.isChecked()) {
            Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
            binding.allSelectCheckBox.setChecked(true);
        }

        binding.imageContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                communicationInterfaceReference.invokeChangeFragment("image", imageList);
            }
        });

        binding.videoContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                communicationInterfaceReference.invokeChangeFragment("video", videoList);
            }
        });

        binding.audioContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                communicationInterfaceReference.invokeChangeFragment("audio", audioList);
            }
        });

        binding.contactContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                communicationInterfaceReference.invokeChangeFragment("contact", contactList);
            }
        });

        binding.documentContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                communicationInterfaceReference.invokeChangeFragment("document", documentList);
            }
        });

        binding.apkContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                communicationInterfaceReference.invokeChangeFragment("apk", apkList);
            }
        });

        binding.imageCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.imageCheckBox.isChecked()) {
                    communicationInterfaceReference.invokeClearList("image");
                    for (Image image : imageList) {
                        communicationInterfaceReference.invokeSingleSelection(image, "image", true);
                        image.setSelected(true);
                    }
                } else {
                    binding.allSelectCheckBox.setChecked(false);
                    for (Image image : imageList) {
                        communicationInterfaceReference.invokeSingleSelection(image, "image", false);
                        image.setSelected(false);
                    }
                }
            }
        });

        binding.videoCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.videoCheckBox.isChecked()) {
                    communicationInterfaceReference.invokeClearList("video");
                    for (Video video : videoList) {
                        communicationInterfaceReference.invokeSingleSelection(video, "video", true);
                        video.setSelected(true);
                    }
                } else {
                    binding.allSelectCheckBox.setChecked(false);
                    for (Video video : videoList) {
                        communicationInterfaceReference.invokeSingleSelection(video, "video", false);
                        video.setSelected(false);
                    }
                }
            }
        });

        binding.audioCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.audioCheckBox.isChecked()) {
                    communicationInterfaceReference.invokeClearList("audio");
                    for (Audio audio : audioList) {
                        communicationInterfaceReference.invokeSingleSelection(audio, "audio", true);
                        audio.setSelected(true);
                    }
                } else {
                    binding.allSelectCheckBox.setChecked(false);
                    for (Audio audio : audioList) {
                        communicationInterfaceReference.invokeSingleSelection(audio, "audio", false);
                        audio.setSelected(false);
                    }
                }
            }
        });

        binding.contactCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.contactCheckBox.isChecked()) {
                    communicationInterfaceReference.invokeClearList("contact");
                    for (Contact contact : contactList) {
                        communicationInterfaceReference.invokeSingleSelection(contact, "contact", true);
                        contact.setSelected(true);
                    }
                } else {
                    binding.allSelectCheckBox.setChecked(false);
                    for (Contact contact : contactList) {
                        communicationInterfaceReference.invokeSingleSelection(contact, "contact", false);
                        contact.setSelected(false);
                    }
                }
            }
        });

        binding.documentCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.documentCheckBox.isChecked()) {
                    communicationInterfaceReference.invokeClearList("document");
                    for (Document document : documentList) {
                        communicationInterfaceReference.invokeSingleSelection(document, "document", true);
                        document.setSelected(true);
                    }
                } else {
                    binding.allSelectCheckBox.setChecked(false);
                    for (Document document : documentList) {
                        communicationInterfaceReference.invokeSingleSelection(document, "document", false);
                        document.setSelected(false);
                    }
                }
            }
        });

        binding.apkCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.apkCheckBox.isChecked()) {
                    communicationInterfaceReference.invokeClearList("apk");
                    for (Apk apk : apkList) {
                        communicationInterfaceReference.invokeSingleSelection(apk, "apk", true);
                        apk.setSelected(true);
                    }
                } else {
                    binding.allSelectCheckBox.setChecked(false);
                    for (Apk apk : apkList) {
                        communicationInterfaceReference.invokeSingleSelection(apk, "apk", false);
                        apk.setSelected(false);
                    }
                }
            }
        });

        binding.sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                communicationInterfaceReference.invokeSendData();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        communicationInterfaceReference.invokeCheckSelection(binding.imageCheckBox, "image");
        communicationInterfaceReference.invokeCheckSelection(binding.videoCheckBox, "video");
        communicationInterfaceReference.invokeCheckSelection(binding.audioCheckBox, "audio");
        communicationInterfaceReference.invokeCheckSelection(binding.contactCheckBox, "contact");
        communicationInterfaceReference.invokeCheckSelection(binding.documentCheckBox, "document");
        communicationInterfaceReference.invokeCheckSelection(binding.apkCheckBox, "apk");
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof CommunicationInterface) {
            communicationInterfaceReference = new CommunicationInterfaceReference((CommunicationInterface) context);
        }
    }
}
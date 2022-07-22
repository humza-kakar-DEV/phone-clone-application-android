package com.example.wifip2p.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.example.wifip2p.Media.Audio;
import com.example.wifip2p.Media.Image;
import com.example.wifip2p.Media.Video;
import com.example.wifip2p.R;
import com.example.wifip2p.Utils.CommunicationInterface;
import com.example.wifip2p.Utils.CommunicationInterfaceReference;
import com.example.wifip2p.databinding.FragmentFileTypeBinding;

import java.io.Serializable;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FileTypeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FileTypeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String TAG = "hmFileType";

    private List<Image> imageList;
    private List<Video> videoList;
    private List<Audio> audioList;

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

    public static FileTypeFragment newInstance(List<Image> imageList, List<Video> videoList, List<Audio> audioList) {
        FileTypeFragment fragment = new FileTypeFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, (Serializable) imageList);
        args.putSerializable(ARG_PARAM2, (Serializable) videoList);
        args.putSerializable(ARG_PARAM3, (Serializable) audioList);
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
                    for (Audio audio : audioList) {
                        communicationInterfaceReference.invokeSingleSelection(audio, "audio", false);
                        audio.setSelected(false);
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
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof CommunicationInterface) {
            communicationInterfaceReference = new CommunicationInterfaceReference((CommunicationInterface) context);
        }
    }
}
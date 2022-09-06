package com.example.wifip2p.Fragment;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.wifip2p.Media.Audio;
import com.example.wifip2p.R;
import com.example.wifip2p.databinding.FragmentFileShareBinding;
import com.example.wifip2p.databinding.FragmentFileTypeBinding;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class FileShareFragment extends Fragment {

    public static final String TAG = "hmShare";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Context context;
    FragmentFileShareBinding binding;
    List<Audio> audioList = new ArrayList<>();

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


        return view;
    }

    public void displayLoadingScreen (boolean state) {
        if (state) {
//?            loading screen view visible
        } else {
//?           loading screen view gone
        }
    }

    public void setupProgressBar () {

    }
}
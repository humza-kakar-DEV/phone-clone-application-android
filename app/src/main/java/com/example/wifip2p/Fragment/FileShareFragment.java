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

//        Audio audio = audioList.get(0);
//        ContentValues values = new ContentValues();
//
//        InputStream inputStream = null;
//        OutputStream outputStream = null;
//
//        try {
//
//            ContentResolver resolver = context.getApplicationContext().getContentResolver();
//
////            reading file with file input stream bytes
//            inputStream = context.getContentResolver().openInputStream(audio.getUri());
//            int size = inputStream.available();
//            byte[] bytes = new byte[size];
//            int c;
//            while ((c = inputStream.read(bytes)) != -1) {
//                Log.d(TAG, "onCreateView: " + c);
//            }
//
//            // URI of the image to remove.
//            Uri audioUri = audio.getUri();
//
//// WHERE clause.
////            String selection = "...";
////            String[] selectionArgs = "...";
//
//// Perform the actual removal.
//            int numAudioRemoved = resolver.delete(
//                    audioUri,
//                    null,
//                    null);
//
//            Log.d(TAG, "audio file deleted: " + numAudioRemoved);
//
////            writing file on external audio directory with output stream bytes
//            Uri collection;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                collection = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
//            } else {
//                collection = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//            }
//
//            outputStream = context.getContentResolver().openOutputStream(collection);
//
//        } catch (IOException e) {
//            Log.d(TAG, "onCreateView: " + e.getMessage());
////            Toast.makeText(context, "couldn't read file with file input stream", Toast.LENGTH_SHORT).show();
//        } finally {
////            if (inputStream != null) {
////                inputStream.close();
////            }
////            if (outputStream != null) {
////                outputStream.close();
////            }
//        }
//


        return view;
    }

    public void storeAudio () {
        // Add a specific media item.
        ContentResolver resolver = context.getApplicationContext()
                .getContentResolver();

// Find all audio files on the primary external storage device.
        Uri audioCollection;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            audioCollection = MediaStore.Audio.Media
                    .getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        } else {
            audioCollection = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        }

// Publish a new song.
        ContentValues
                newSongDetails = new ContentValues();
        newSongDetails.put(MediaStore.Audio.Media.DISPLAY_NAME,
                "My Song.mp3");

// Keeps a handle to the new song's URI in case we need to modify it
// later.
        Uri myFavoriteSongUri = resolver
                .insert(audioCollection, newSongDetails);
    }
}
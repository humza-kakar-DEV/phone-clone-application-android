package com.example.wifip2p.Media;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AudioMedia {

    private static final String TAG = "hmAudio";
    private Context context;

    public AudioMedia (Context context) {
        this.context = context;
    }

    public List<Audio> generateAudios () {

        List<Audio> audioList = new ArrayList<Audio>();

        Uri collection;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            collection = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        } else {
            collection = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        }
        //    songId, songTitle, songArtist, path, genre, duration
        String[] projection = new String[] {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION,
        };

        String selection = MediaStore.Images.Media.DURATION +
                " >= ?";

        String[] selectionArgs = new String[] {
                String.valueOf(TimeUnit.MILLISECONDS.convert(5, TimeUnit.MINUTES)),
        };

        String sortOrder = MediaStore.Audio.Media.DISPLAY_NAME + " ASC";

        try (Cursor cursor = context.getApplicationContext().getContentResolver().query(
                collection,
                projection,
                null,
                null,
                sortOrder
        )) {
            // Cache column indices.
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
            int songNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
            int songArtistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
            int songDuration = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);

            while (cursor.moveToNext()) {
                // Get values of columns for a given video.
                long id = cursor.getLong(idColumn);
                String songName = cursor.getString(songNameColumn);
                String songArtist = cursor.getString(songArtistColumn);
                long duration = cursor.getInt(songDuration);

                Uri contentUri = ContentUris.withAppendedId(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);

                // Stores column values and the contentUri in a local object
                // that represents the media file.
                audioList.add(new Audio(contentUri, songName, songArtist, duration, false));
                Log.d(TAG, "getAudios: " + contentUri);
            }

            return audioList;
        }
    }

}

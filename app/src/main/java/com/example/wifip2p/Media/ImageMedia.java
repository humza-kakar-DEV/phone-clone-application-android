package com.example.wifip2p.Media;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.example.wifip2p.Utils.CommunicationInterface;
import com.example.wifip2p.Utils.CommunicationInterfaceReference;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ImageMedia {

    private static final String TAG = "hmImageClass";
    private Context context;
    public ImageMedia (Context context) {
        this.context = context;
    }

    public int getImageSize() {
        Uri collection;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        } else {
            collection = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        }

        String[] projection = new String[] {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.HEIGHT,
                MediaStore.Images.Media.WIDTH,
        };

        try (Cursor cursor = context.getApplicationContext().getContentResolver().query(
                collection,
                projection,
                null,
                null,
                null
        )) {
            // Cache column indices
            return cursor.getCount();
        }
    }

    public List<Image> generateImages () {

        List<Image> imageList = new ArrayList<Image>();

        Uri collection;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        } else {
            collection = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        }

        String[] projection = new String[] {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.HEIGHT,
                MediaStore.Images.Media.WIDTH,
        };

        String selection = MediaStore.Images.Media.DURATION +
                " >= ?";

        String[] selectionArgs = new String[] {
                String.valueOf(TimeUnit.MILLISECONDS.convert(5, TimeUnit.MINUTES)),
        };

        String sortOrder = MediaStore.Images.Media.DISPLAY_NAME + " ASC";

        try (Cursor cursor = context.getApplicationContext().getContentResolver().query(
                collection,
                projection,
                null,
                null,
                sortOrder
        )) {
            // Cache column indices.
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            int nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
            int heightColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.HEIGHT);
            int widthColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.WIDTH);

            while (cursor.moveToNext()) {
                // Get values of columns for a given video.
                long id = cursor.getLong(idColumn);
                String name = cursor.getString(nameColumn);
                int height = cursor.getInt(heightColumn);
                int width = cursor.getInt(heightColumn);

                Uri contentUri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

                // Stores column values and the contentUri in a local object
                // that represents the media file.
                imageList.add(new Image(contentUri.toString(), name, height, width, false));
                Log.d(TAG, "getImages: " + contentUri);
            }

            return imageList;
        }
    }

}

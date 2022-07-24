package com.example.wifip2p.Media;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.DocumentsProvider;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.util.ArrayList;
import java.util.List;

public class DocumentMedia {

    private static final String TAG = "hmDoc";
    Context context;
    List<Document> documentList = new ArrayList<>();

//    String[] contentUriPrefixesToTry = new String[]{
//            "content://downloads/public_downloads",
//            "content://downloads/my_downloads"
//    };

    public DocumentMedia(Context context) {
        this.context = context;
    }

    public List<Document> generateDocuments() {

        ContentResolver cr = context.getContentResolver();

        Uri collection;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            collection = MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL );
        } else {
            collection = MediaStore.Downloads.EXTERNAL_CONTENT_URI;
        }

        Log.d(TAG, "generateDocuments: " + collection);

// every column, although that is huge waste, you probably need
// BaseColumns.DATA (the path) only.
        String[] projection = new String[] {
                MediaStore.MediaColumns._ID,
                MediaStore.MediaColumns.MIME_TYPE,
                MediaStore.MediaColumns.DISPLAY_NAME,
        };

// exclude media files, they would be here also.

//        String selection = MediaStore.Downloads. + " = 'application/pdf'";

        String sortOrder = null; // unordered


        try (Cursor cursorAllDocumentFiles = cr.query(collection, null, null, null, sortOrder)) {

            int idDocumentColumn = cursorAllDocumentFiles.getColumnIndexOrThrow(MediaStore.MediaColumns._ID);
            int documentTypeMimeColumn = cursorAllDocumentFiles.getColumnIndexOrThrow(MediaStore.MediaColumns.MIME_TYPE);
            int documentNameColumn = cursorAllDocumentFiles.getColumnIndexOrThrow(MediaStore.Downloads.DISPLAY_NAME);
//            int documentTypeColumn = cursorAllDocumentFiles.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE);
//            int documentSizeColumn = cursorAllDocumentFiles.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE);

            while (cursorAllDocumentFiles.moveToNext()) {

//                Log.d(TAG, "generateDocuments: ");

                long id = cursorAllDocumentFiles.getLong(idDocumentColumn);
                String name = cursorAllDocumentFiles.getString(documentNameColumn);
//                String type = cursorAllDocumentFiles.getString(documentTypeColumn);
                String mimeType = cursorAllDocumentFiles.getString(documentTypeMimeColumn);
//                int size = cursorAllDocumentFiles.getInt(documentSizeColumn);
//                Uri contentUri = ContentUris.withAppendedId(uri, (id));

//                Log.d(TAG, "mimeType: " + mimeType + " mediaType: " + type);
                Log.d(TAG, "generateDocuments: " + mimeType);

//                documentList.add(new Document(id, contentUri, name, type, size));

//                for (String contentUriPrefix : contentUriPrefixesToTry) {
//                    try {
//                         /*   final Uri contentUri = ContentUris.withAppendedId(
//                                    Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));*/
//
//                    } catch (NumberFormatException e) {
//                    }
//                }
            }
        }
        return documentList;
    }
}

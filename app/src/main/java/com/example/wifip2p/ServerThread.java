package com.example.wifip2p;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;

import com.example.wifip2p.Utils.Constant;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerThread extends Thread {

    MainActivity mainActivity;
    Context context;

    ServerSocket welcomeSocket = null;
    Socket socket = null;
    InputStream inputStream = null;
    FileOutputStream fileOutputStream = null;
    BufferedOutputStream bufferedOutputStream = null;
    DataInputStream dataInputStream = null;

    int fileCount;
    String imageMimeType = "image/jpeg";
    String audioMimeType = "audio/mpeg";
    String videoMimeType = "video/mp4";
    String documentMimeType = "";
    List<String[]> data = new ArrayList<String[]>();
    ContentValues values = new ContentValues();
    Uri uri;
    String fileName;
    int fileType;

    public ServerThread(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        context = mainActivity.getApplicationContext();
    }

    public void dynamicCasting(String name, int fileType) throws IOException, ClassNotFoundException {
        switch (fileType) {
//?         storing image case
            case 0:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    values.put(MediaStore.MediaColumns.DISPLAY_NAME, "wifi-direct_" + name);       //file name
                    values.put(MediaStore.MediaColumns.MIME_TYPE, MediaStore.Images.Media.MIME_TYPE);        //file extension, will automatically add to file
                    values.put(MediaStore.MediaColumns.RELATIVE_PATH, "hamzaPhoneCloneImage");     //end "/" is not mandatory
                    uri = context.getContentResolver().insert(MediaStore.Files.getContentUri("external"), values);
                    fileOutputStream = (FileOutputStream) context.getContentResolver().openOutputStream(uri);
                }
                else {
                    File hamzaPhoneCloneFolder = new File(String.valueOf(Environment.getExternalStorageDirectory() + File.separator + "hamzaPhoneClone"));
                    if (!hamzaPhoneCloneFolder.exists()) {
                        hamzaPhoneCloneFolder.mkdir();
                    }
                    fileOutputStream = new FileOutputStream(hamzaPhoneCloneFolder.getAbsolutePath() + File.separator + name);
                }
                break;
//?         storing audio case
            case 1:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    values.put(MediaStore.MediaColumns.DISPLAY_NAME, "wifi-direct_" + name);       //file name
                    values.put(MediaStore.MediaColumns.MIME_TYPE, MediaStore.Audio.Media.MIME_TYPE);        //file extension, will automatically add to file
                    values.put(MediaStore.MediaColumns.RELATIVE_PATH, "hamzaPhoneCloneAudio");     //end "/" is not mandatory
                    uri = context.getContentResolver().insert(MediaStore.Files.getContentUri("external"), values);
                    fileOutputStream = (FileOutputStream) context.getContentResolver().openOutputStream(uri);
                }
                else {
                    File hamzaPhoneCloneFolder = new File(String.valueOf(Environment.getExternalStorageDirectory() + File.separator + "hamzaPhoneClone"));
                    if (!hamzaPhoneCloneFolder.exists()) {
                        hamzaPhoneCloneFolder.mkdir();
                    }
                    fileOutputStream = new FileOutputStream(hamzaPhoneCloneFolder.getAbsolutePath() + File.separator + name);
                }
                break;
//?         storing video case
            case 2:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    values.put(MediaStore.MediaColumns.DISPLAY_NAME, "wifi-direct_" + name);       //file name
                    values.put(MediaStore.MediaColumns.MIME_TYPE, MediaStore.Video.Media.MIME_TYPE);        //file extension, will automatically add to file
                    values.put(MediaStore.MediaColumns.RELATIVE_PATH, "hamzaPhoneCloneVideo");     //end "/" is not mandatory
                    uri = context.getContentResolver().insert(MediaStore.Files.getContentUri("external"), values);
                    fileOutputStream = (FileOutputStream) context.getContentResolver().openOutputStream(uri);
                }
                else {
                    File hamzaPhoneCloneFolder = new File(String.valueOf(Environment.getExternalStorageDirectory() + File.separator + "hamzaPhoneClone"));
                    if (!hamzaPhoneCloneFolder.exists()) {
                        hamzaPhoneCloneFolder.mkdir();
                    }
                    fileOutputStream = new FileOutputStream(hamzaPhoneCloneFolder.getAbsolutePath() + File.separator + name);
                }
//?         storing document case
            case 3:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    values.put(MediaStore.MediaColumns.DISPLAY_NAME, "wifi-direct_" + name);       //file name
                    values.put(MediaStore.MediaColumns.MIME_TYPE, MediaStore.Files.FileColumns.MIME_TYPE);        //file extension, will automatically add to file
                    values.put(MediaStore.MediaColumns.RELATIVE_PATH, MediaStore.Files.FileColumns.MEDIA_TYPE_DOCUMENT);     //end "/" is not mandatory
                    uri = context.getContentResolver().insert(MediaStore.Files.getContentUri("external"), values);
                    fileOutputStream = (FileOutputStream) context.getContentResolver().openOutputStream(uri);
                }
                else {
                    File hamzaPhoneCloneFolder = new File(String.valueOf(Environment.getExternalStorageDirectory() + File.separator + "hamzaPhoneClone"));
                    if (!hamzaPhoneCloneFolder.exists()) {
                        hamzaPhoneCloneFolder.mkdir();
                    }
                    fileOutputStream = new FileOutputStream(hamzaPhoneCloneFolder.getAbsolutePath() + File.separator + name);
                }
                break;
//?         storing apk case
            case 4:
                File contactsDirectory = new File(Environment.getExternalStorageDirectory() + File.separator + "hamzaPhoneClone");
                if (!contactsDirectory.exists()) {
                    contactsDirectory.mkdir();
                }
//                File contactsFolder = new File(contactsDirectory.getAbsolutePath() + File.separator + "Contacts");
//                if (!contactsFolder.exists()) {
//                    contactsFolder.mkdirs();
//                }
                fileOutputStream = new FileOutputStream(contactsDirectory.getAbsolutePath() + File.separator + name + ".csv");
                break;
            case 5:
                File apkDirectory = new File(Environment.getExternalStorageDirectory() + File.separator + "hamzaPhoneClone");
                if (!apkDirectory.exists()) {
                    apkDirectory.mkdir();
                }
//                File apkFolder = new File(apkDirectory.getAbsolutePath() + File.separator + "Apk");
//                if (!apkFolder.exists()) {
//                    apkFolder.mkdirs();
//                }
                fileOutputStream = new FileOutputStream(apkDirectory.getAbsolutePath() + File.separator + name + ".apk");
                break;
        }
    }

    @Override
    public void run() {
        super.run();

        try {

            welcomeSocket = new ServerSocket(8888);

            while (true) {

                socket = welcomeSocket.accept();
                inputStream = socket.getInputStream();

                dataInputStream = new DataInputStream(inputStream);
                fileName = dataInputStream.readUTF();
                fileType = dataInputStream.readInt();

                dynamicCasting(fileName, fileType);

                Log.d(Constant.THREAD_TAG, "server thread file name: " + fileName);

                byte[] buffer = new byte[4096];

                int bytesRead;

                bufferedOutputStream = new BufferedOutputStream(fileOutputStream);

                while (true) {
                    bytesRead = inputStream.read(buffer, 0, buffer.length);
                    if (bytesRead == -1) {
                        break;
                    }
                    bufferedOutputStream.write(buffer, 0, bytesRead);
                    bufferedOutputStream.flush();
                }

                fileCount++;

                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mainActivity.serverResult(fileCount, fileName);
                    }
                });

                fileOutputStream.close();
                bufferedOutputStream.close();
                socket.close();

            }


        } catch (IOException e) {

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


//        ServerSocket welcomeSocket = null;
//        Socket socket = null;

//        try {
//
//            welcomeSocket = new ServerSocket(8888);
//
//            while (true) {
//
//                //Listen for incoming connections on specified port
//                //Block thread until someone connects
//                socket = welcomeSocket.accept();
//
//                is = socket.getInputStream();
//
//                DataInputStream dataInputStream = new DataInputStream(is);
//
//                String name = dataInputStream.readUTF();
//                int fileType = dataInputStream.readInt();
//
//                Log.d(Constant.THREAD_TAG, "server thread: " + name + " | " + fileType);
//
//                mainActivity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(mainActivity, "file name: " + name, Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//                dynamicCasting(name, fileType);
//
//                byte[] buffer = new byte[4096];
//
//                int bytesRead;
//
//                BufferedOutputStream bos = new BufferedOutputStream(fos);
//
//                while (true) {
//                    bytesRead = is.read(buffer, 0, buffer.length);
//                    if (bytesRead == -1) {
//                        break;
//                    }
//                    bos.write(buffer, 0, bytesRead);
//                    bos.flush();
//                }
//
////!             Whole while loop socket code finishes here :
//
//                fos.close();
//                bos.close();
//                socket.close();
//
//                fileCount++;
//
//                mainActivity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        mainActivity.serverResult(fileCount, name);
//                    }
//                });
//
//            }
//
//
//        } catch (Exception e) {
//            Log.d(Constant.THREAD_TAG, "server thread: " + e.getMessage());
//        }

    }

    public void createContact (String displayName, String mobileNumber) {
//        String displayName  = "ABC";
//        String mobileNumber = "88888888";
        String emailID      = "abc@mail.com";
        String homeNumber   = "1111";
        String workNumber   = "2222";
        String company      = "xyz";
        String jobTitle     = "boss";

        ArrayList<ContentProviderOperation> contentProviderOperationArrayList = new ArrayList <ContentProviderOperation> ();

        contentProviderOperationArrayList.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        //! Name
        if (displayName != null) {
            contentProviderOperationArrayList.add(ContentProviderOperation.newInsert(
                            ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(
                            ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                            displayName).build());
        }

        //! Mobile Number
        if (mobileNumber != null) {
            contentProviderOperationArrayList.add(ContentProviderOperation.
                    newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, mobileNumber)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                    .build());
        }

        //Home
//        if (homeNumber != null) {
//            contentProviderOperationArrayList.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
//                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
//                    .withValue(ContactsContract.Data.MIMETYPE,
//                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
//                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, homeNumber)
//                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
//                            ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
//                    .build());
//        }

        //Work
//        if (workNumber != null) {
//            contentProviderOperationArrayList.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
//                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
//                    .withValue(ContactsContract.Data.MIMETYPE,
//                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
//                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, workNumber)
//                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
//                            ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
//                    .build());
//        }

        //Email
//        if (emailID != null) {
//            contentProviderOperationArrayList.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
//                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
//                    .withValue(ContactsContract.Data.MIMETYPE,
//                            ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
//                    .withValue(ContactsContract.CommonDataKinds.Email.DATA, emailID)
//                    .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
//                    .build());
//        }

        //Organization
//        if (!company.equals("") && !jobTitle.equals("")) {
//            contentProviderOperationArrayList.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
//                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
//                    .withValue(ContactsContract.Data.MIMETYPE,
//                            ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
//                    .withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, company)
//                    .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
//                    .withValue(ContactsContract.CommonDataKinds.Organization.TITLE, jobTitle)
//                    .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
//                    .build());
//        }

        // Creating new contact
        try {
            context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, contentProviderOperationArrayList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

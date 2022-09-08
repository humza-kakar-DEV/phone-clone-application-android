package com.example.wifip2p;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.ResultReceiver;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.wifip2p.Media.Apk;
import com.example.wifip2p.Media.Audio;
import com.example.wifip2p.Media.Contact;
import com.example.wifip2p.Media.Document;
import com.example.wifip2p.Media.DynamicObject;
import com.example.wifip2p.Media.Image;
import com.example.wifip2p.Media.Video;
import com.example.wifip2p.Utils.Constant;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerThread extends Thread {

    MainActivity mainActivity;
    Context context;
    FileOutputStream fos;
    InputStream is;
    int fileCount;
    String imageMimeType = "image/jpeg";
    String audioMimeType = "audio/mpeg";
    String videoMimeType = "video/mp4";
    String documentMimeType = "";
    ContentValues values = new ContentValues();
    Uri uri;

    public ServerThread(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        context = mainActivity.getApplicationContext();
    }

    public void dynamicCasting (String name, int fileType) {
        switch (fileType) {
            case 0:
                values.put(MediaStore.MediaColumns.DISPLAY_NAME, "wifi-direct_" + name);       //file name
                values.put(MediaStore.MediaColumns.MIME_TYPE, MediaStore.Images.Media.MIME_TYPE);        //file extension, will automatically add to file
                values.put(MediaStore.MediaColumns.RELATIVE_PATH, MediaStore.Images.ImageColumns.RELATIVE_PATH);     //end "/" is not mandatory
                uri = context.getContentResolver().insert(MediaStore.Files.getContentUri("external"), values);
                break;
            case 1:
                values.put(MediaStore.MediaColumns.DISPLAY_NAME, "wifi-direct_" + name);       //file name
                values.put(MediaStore.MediaColumns.MIME_TYPE, MediaStore.Audio.Media.MIME_TYPE);        //file extension, will automatically add to file
                values.put(MediaStore.MediaColumns.RELATIVE_PATH, MediaStore.Audio.AudioColumns.RELATIVE_PATH);     //end "/" is not mandatory
                uri = context.getContentResolver().insert(MediaStore.Files.getContentUri("external"), values);
                break;
            case 2:
                values.put(MediaStore.MediaColumns.DISPLAY_NAME, "wifi-direct_" + name);       //file name
                values.put(MediaStore.MediaColumns.MIME_TYPE, MediaStore.Video.Media.MIME_TYPE);        //file extension, will automatically add to file
                values.put(MediaStore.MediaColumns.RELATIVE_PATH, MediaStore.Video.VideoColumns.RELATIVE_PATH);     //end "/" is not mandatory
                uri = context.getContentResolver().insert(MediaStore.Files.getContentUri("external"), values);
                break;
            case 3:
                values.put(MediaStore.MediaColumns.DISPLAY_NAME, "wifi-direct_" + name);       //file name
                values.put(MediaStore.MediaColumns.MIME_TYPE, MediaStore.Files.FileColumns.MIME_TYPE);        //file extension, will automatically add to file
                values.put(MediaStore.MediaColumns.RELATIVE_PATH, MediaStore.Files.FileColumns.MEDIA_TYPE_DOCUMENT);     //end "/" is not mandatory
                uri = context.getContentResolver().insert(MediaStore.Files.getContentUri("external"), values);
                break;
        }
    }

    @Override
    public void run() {
        super.run();

        ServerSocket welcomeSocket = null;
        Socket socket = null;

        try {

            welcomeSocket = new ServerSocket(8888);

            while(true)
            {

                //Listen for incoming connections on specified port
                //Block thread until someone connects
                socket = welcomeSocket.accept();

                is = socket.getInputStream();

                DataInputStream dataInputStream = new DataInputStream(is);
                String name = dataInputStream.readUTF();
                int fileType = dataInputStream.readInt();

                Log.d(Constant.THREAD_TAG, "client thread: file name: " + name);

                byte[] buffer = new byte[4096];

                int bytesRead;

                dynamicCasting(name, fileType);

                fos = (FileOutputStream) context.getContentResolver().openOutputStream(uri);

                BufferedOutputStream bos = new BufferedOutputStream(fos);

                while(true)
                {
                    bytesRead = is.read(buffer, 0, buffer.length);
                    if(bytesRead == -1)
                    {
                        break;
                    }
                    bos.write(buffer, 0, bytesRead);
                    bos.flush();

                }

//!             Whole while loop socket code finishes here :

                bos.close();
                socket.close();

                fileCount++;

                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mainActivity.serverResult(fileCount, name);
                    }
                });

            }


        } catch (IOException e) {
            Log.d(Constant.THREAD_TAG, "server thread: " + e.getMessage());
        }
        catch(Exception e)
        {
            Log.d(Constant.THREAD_TAG, "server thread: " + e.getMessage());
        }

    }
}

class ServerThreadHandler extends Handler {

    MainActivity mainActivity;
    Context context;
    ServerSocket serverSocket;
    Socket client;
    byte buf[] = new byte[1024];
    int len;
    String name;

    List<Audio> audioList = new ArrayList<>();

    Image image;
    Video video;
    Audio audio;
    Document document;
    Apk apk;
    Contact contact;
    Object object;


    public ServerThreadHandler(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        context = mainActivity.getApplicationContext();
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);

        try {

//              ************** RECEIVING OBJECT THROUGH CLIENT SOCKET **********

            Log.d(Constant.THREAD_TAG, "server thread: socket status - " + client.isConnected());

            serverSocket = new ServerSocket(8888);
            client = serverSocket.accept();

            Log.d(Constant.THREAD_TAG, "server thread: socket status - " + client.isConnected());

//              ************* RECEIVING AUDIO FILE THROUGH CLIENT SOCKET **********

                ContentValues values = new ContentValues();

                values.put(MediaStore.MediaColumns.DISPLAY_NAME, audioList.get(0).getSongName());       //file name
                values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mpeg");        //file extension, will automatically add to file
                values.put(MediaStore.MediaColumns.RELATIVE_PATH, MediaStore.Audio.Media.RELATIVE_PATH);     //end "/" is not mandatory

                Uri uri = context.getContentResolver().insert(MediaStore.Files.getContentUri("external"), values);

                InputStream is = client.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);

                OutputStream os = client.getOutputStream();
                PrintWriter pw = new PrintWriter(os);

                byte[] buffer = new byte[is.available()];
                int bytesRead;

                FileOutputStream fos = (FileOutputStream) context.getContentResolver().openOutputStream(uri);
                BufferedOutputStream bos = new BufferedOutputStream(fos);

                while (true) {

                    bytesRead = is.read(buffer, 0, buffer.length);

                    if (bytesRead == -1) {
                        break;
                    }

//                    Log.d(Constant.THREAD_TAG, "server thread - file size: " + bytesRead);

                    bos.write(buffer, 0, bytesRead);
                    bos.flush();

                }

                serverSocket.close();
                client.close();

//                --------------------------------------------------------

//                Uri collection;
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                    collection = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
//                } else {
//                    collection = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//                }
//
//                FileOutputStream fileOutputStream = (FileOutputStream) context.getContentResolver().openOutputStream(collection);
//
//                while ((len = inputStream.read()) != -1) {
//                    Log.d(TAG_FILE, "server thread - file size: " + len);
//                    fileOutputStream.write(len);
//                }

            //                final File f = new File(Environment.getExternalStorageDirectory() + "/"
//                        + context.getPackageName() + "/wifip2pshared-" + System.currentTimeMillis()
//                        + ".jpg");
//
//                File dirs = new File(f.getParent());
//                if (!dirs.exists())
//                    Log.d(TAG_FILE, "server thread - folder created: " + dirs.mkdirs());
//                Log.d(TAG_FILE, "server thread - file created: " + f.createNewFile());

//                Log.d(TAG_FILE, "server thread - file size: " + inputStream.available());

//                while ((len = inputStream.read()) != -1) {
//                    fileOutputStream.write(len);
//                }
//                serverSocket.close();
//                Log.d(TAG_FILE, "server thread: - file path: " + f.getAbsolutePath());
//
//                mainActivity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        mainActivity.showFile(f.getAbsolutePath());
//                    }
//                });

        } catch (Exception e) {

            System.err.println("Client Error: " + e.getMessage());
            System.err.println("Localized: " + e.getLocalizedMessage());
            System.err.println("Stack Trace: " + e.getStackTrace());

            Log.d(Constant.THREAD_TAG, "server error: " + e.getMessage());
            Log.d(Constant.THREAD_TAG, "server localized: " + e.getLocalizedMessage());
            Log.d(Constant.THREAD_TAG, "server stack trace: " + e.getStackTrace());

        } finally {

        }
    }
}

package com.example.wifip2p;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.wifip2p.Media.Audio;
import com.example.wifip2p.Utils.HmMessage;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerThread extends Thread implements Serializable {

    public ServerThreadHandler serverThreadHandler;
    MainActivity mainActivity;
    public static final String TAG = "hmThread";
    public static final String TAG_FILE = "hmFile";

    public ServerThread (MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void run() {
        super.run();
        Looper.prepare();
        serverThreadHandler = new ServerThreadHandler(mainActivity);
        Looper.loop();
    }

    public class ServerThreadHandler extends Handler implements Serializable {

        MainActivity mainActivity;
        Context context;
        ServerSocket serverSocket;
        Socket client;
        byte buf[]  = new byte[1024];
        int len;
        List<Audio> audioList = new ArrayList<>();

        public ServerThreadHandler (MainActivity mainActivity) {
            this.mainActivity = mainActivity;
            context = mainActivity.getApplicationContext();
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            try {

//                ************** RECEIVING OBJECT THROUGH CLIENT SOCKET **********


                Log.d(TAG, "server thread: started before connection");

                serverSocket = new ServerSocket(8888);
                client = serverSocket.accept();

                Log.d(TAG, "server thread: connected to client thread WELCOME!");

                InputStream inputStream = client.getInputStream();
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

                audioList = (List<Audio>) objectInputStream.readObject();

                for (Audio audio : audioList) {
                    Log.d(TAG_FILE, "server thread - audio name: " + audio.getSongName());
                }

//                ----------------------------------------

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

                while(true)
                {
                    bytesRead = is.read(buffer, 0, buffer.length);
                    if(bytesRead == -1)
                    {
                        break;
                    }

                    Log.d(TAG_FILE, "server thread - file size: " + bytesRead);

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

//                System.err.println("Client Error: " + e.getMessage());
//                System.err.println("Localized: " + e.getLocalizedMessage());
//                System.err.println("Stack Trace: " + e.getStackTrace());

                Log.d(TAG, "server error: " + e.getMessage());
                Log.d(TAG, "server localized: " + e.getLocalizedMessage());
                Log.d(TAG, "server stack trace: " + e.getStackTrace());

            } finally {
            }
        }
    }
}

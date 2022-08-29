package com.example.wifip2p;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.wifip2p.Media.Audio;
import com.example.wifip2p.Media.AudioMedia;
import com.example.wifip2p.Media.Contact;
import com.example.wifip2p.Media.DynamicObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientThread extends Thread {

    ClientThreadHandler clientThreadHandler;
    MainActivity mainActivity;
    public static final String TAG = "hmThread";
    public static final String TAG_FILE = "hmFile";

    public ClientThread (MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void run() {
        super.run();
        Looper.prepare();
        clientThreadHandler = new ClientThreadHandler(mainActivity);
        Looper.loop();
    }

    public ClientThreadHandler getClientThreadHandler() {
        return clientThreadHandler;
    }

    public class ClientThreadHandler extends Handler {

        private static final String AUDIO_TAG = "hmAudioKey";

        MainActivity mainActivity;
        Context context;
        Socket socket = new Socket();
        byte buf[]  = new byte[1024];
        int len;
        String hostAddress;
        Audio audio;
        Contact contact;
        String fileType;

        public ClientThreadHandler (MainActivity mainActivity) {
            this.mainActivity = mainActivity;
            context = mainActivity.getApplicationContext();
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            if (msg.getData() != null) {
                fileType = msg.getData().getString("fileTypeKey");
                contact = (Contact) msg.getData().getSerializable(AUDIO_TAG);
                hostAddress = msg.getData().getString("hmHostAddress");
                Log.d(TAG_FILE, "audio from CLIENT thread HANDLER: " + audio.getSongName());
            }

            try {

//     *******************   SENDING OBJECT & INPUT STREAMS TO SERVER SOCKET   *******************


                socket.bind(null);
                socket.connect(new InetSocketAddress(hostAddress, 8888), 5000);

                Log.d(TAG, "client thread: connected to server thread WELCOME!");

                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
//                ObjectOutputStream objectOutputStreamFileName = new ObjectOutputStream(socket.getOutputStream());

                DynamicObject dynamicObject = new DynamicObject(contact, fileType);

                objectOutputStream.writeObject (dynamicObject);

                OutputStream os = socket.getOutputStream();
                PrintWriter pw = new PrintWriter(os);

                InputStream is = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);

                ContentResolver contentResolver = context.getApplicationContext().getContentResolver();
                FileInputStream fis = (FileInputStream) contentResolver.openInputStream(Uri.parse(audio.getUri()));

                byte[] buffer = new byte[fis.available()];

                BufferedInputStream bis = new BufferedInputStream(fis);

                while(true)
                {

                    int bytesRead = bis.read(buffer, 0, buffer.length);

                    if(bytesRead == -1)
                    {
                        break;
                    }

                    //BytesToSend = BytesToSend - bytesRead;
                    os.write(buffer,0, bytesRead);
                    os.flush();
                }

////                --------------------------------------------------------
//
////                OutputStream outputStream = socket.getOutputStream();
////                ContentResolver cr = context.getContentResolver();
////                File file = new File("/sdcard/DCIM/Camera/download.jpg");
////                FileInputStream fileInputStream = new FileInputStream(file);
////
////                Log.d(TAG_FILE, "client thread - file size: " + fileInputStream.available());
////
////                while ((len = fileInputStream.read(buf)) != -1) {
////                    outputStream.write(buf, 0, len);
////                }
////
////                outputStream.close();
////                fileInputStream.close();

            } catch (Exception e) {

                Log.d(TAG, "client error: " + e.getMessage());
                Log.d(TAG, "client localized: " + e.getLocalizedMessage());
                Log.d(TAG, "client stack trace: " + e.getStackTrace());

            } finally {
                if (socket != null) {
                    if (socket.isConnected()) {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            //catch logic
                        }
                    }
                }
            }
        }
    }
}

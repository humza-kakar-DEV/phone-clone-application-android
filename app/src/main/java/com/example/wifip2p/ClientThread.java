package com.example.wifip2p;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.autofill.AutofillId;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.wifip2p.Media.Apk;
import com.example.wifip2p.Media.Audio;
import com.example.wifip2p.Media.AudioMedia;
import com.example.wifip2p.Media.Contact;
import com.example.wifip2p.Media.Document;
import com.example.wifip2p.Media.DynamicObject;
import com.example.wifip2p.Media.Image;
import com.example.wifip2p.Media.ImageMedia;
import com.example.wifip2p.Media.Video;
import com.example.wifip2p.Utils.Constant;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

public class ClientThread extends Thread {

    MainActivity mainActivity;
    Context context;

    int len;
    String hostAddress;
    String classType;
    Socket socket = new Socket();
    FileInputStream fileInputStream;

    Object object;
    Image image;
    Audio audio;
    Video video;
    Document document;
    Apk apk;

    ImageMedia imageMedia;
    AudioMedia audioMedia;

    public ClientThread(MainActivity mainActivity, String hostAddress) {
        this.mainActivity = mainActivity;
        this.context = mainActivity.getApplicationContext();
        this.hostAddress = hostAddress;
    }

    @Override
    public void run() {
        super.run();

        imageMedia = new ImageMedia(mainActivity);
        audioMedia = new AudioMedia(mainActivity);

        image = imageMedia.generateImages().get(2);
        audio = audioMedia.generateAudios().get(8);

        Log.d(Constant.THREAD_TAG, "file name: " + audio.getSongName());

        while (true) {
            if (audio == null) {
                Log.d(Constant.THREAD_TAG, "client thread: cannot run connection image is  null! ");
            } else {
                break;
            }
        }

        try {

//     *******************   SENDING OBJECT & INPUT STREAMS TO SERVER SOCKET   *******************

            Log.d(Constant.THREAD_TAG, "client thread: socket status - false");

            socket.bind(null);
            socket.connect(new InetSocketAddress(hostAddress, 8888), 5000);

            Log.d(Constant.THREAD_TAG, "client thread: socket status - true");

            OutputStream os = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os);

            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);

            BufferedReader br = new BufferedReader(isr);

            DataOutputStream dataOutputStream = new DataOutputStream(os);
            dataOutputStream.writeUTF(audio.getSongName());

            ContentResolver contentResolver = context.getApplicationContext().getContentResolver();
            fileInputStream = (FileInputStream) contentResolver.openInputStream(Uri.parse(audio.getUri()));

            byte[] buffer = new byte[fileInputStream.available()];

             BufferedInputStream bis = new BufferedInputStream(fileInputStream);

            while (true) {

                int bytesRead = bis.read(buffer, 0, buffer.length);

                if (bytesRead == -1) {
                    break;
                }

                Log.d(Constant.THREAD_TAG, "client thread: file bytes - " + bytesRead);;

                //BytesToSend = BytesToSend - bytesRead;
                os.write(buffer, 0, bytesRead);
                os.flush();
                
            }

////!                --------------------------------------------------------

        } catch (Exception e) {

            Log.d(Constant.THREAD_TAG, "client stack trace: " + e.getStackTrace());
            Log.d(Constant.THREAD_TAG, "client error: " + e.getMessage());
            Log.d(Constant.THREAD_TAG, "client localized: " + e.getLocalizedMessage());

        } finally {
            if (socket != null) {
                if (socket.isConnected()) {
//                        try {
////                            socket.close();
//                        }
//                        catch (IOException e) {
//                            //catch logic
//                        }
                }
            }
        }
    }
}

    class ClientThreadHandler extends Handler {

    private static final String AUDIO_TAG = "hmAudioKey";

    MainActivity mainActivity;
    Context context;
    Socket socket = new Socket();
    int len;
    String hostAddress;
    String classType;
    FileInputStream fileInputStream;

    Object object;
    Image image;
    Audio audio;
    Video video;
    Document document;
    Apk apk;

    public ClientThreadHandler(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        context = mainActivity.getApplicationContext();
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);

        try {

//     *******************   SENDING OBJECT & INPUT STREAMS TO SERVER SOCKET   *******************

            Log.d(Constant.THREAD_TAG, "client thread: before connection");

            socket.bind(null);
            socket.connect(new InetSocketAddress(hostAddress, 8888), 5000);

            Log.d(Constant.THREAD_TAG, "client thread: connected to server thread WELCOME!");

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            DynamicObject dynamicObject = new DynamicObject();

            if (msg.getData() != null) {
                hostAddress = msg.getData().getString(Constant.GROUP_OWNER_TAG);
                classType = msg.getData().getString(Constant.CLASS_TAG);
                object = (Object) msg.getData().getSerializable(Constant.FILE_OBJECT_TAG);
            }

            if (msg.getData().getSerializable(Constant.FILE_OBJECT_TAG).equals("contact")) {
                dynamicObject.setObject(object);
                dynamicObject.setObjectType(classType);
                objectOutputStream.writeObject(dynamicObject);
                return;
            }

            OutputStream os = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os);

            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);

            BufferedReader br = new BufferedReader(isr);

            ContentResolver contentResolver = context.getApplicationContext().getContentResolver();
            image = (Image) object;
            dynamicObject.setObjectType(classType);
            objectOutputStream.writeObject(dynamicObject);
            fileInputStream = (FileInputStream) contentResolver.openInputStream(Uri.parse(image.getUri()));

            byte[] buffer = new byte[fileInputStream.available()];

            BufferedInputStream bis = new BufferedInputStream(fileInputStream);

            while (true) {

                int bytesRead = bis.read(buffer, 0, buffer.length);

                if (bytesRead == -1) {
                    break;
                }

                //BytesToSend = BytesToSend - bytesRead;
                os.write(buffer, 0, bytesRead);
                os.flush();
            }

////!                --------------------------------------------------------
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
//
////!                --------------------------------------------------------

        } catch (Exception e) {

            Log.d(Constant.THREAD_TAG, "client stack trace: " + e.getStackTrace());
            Log.d(Constant.THREAD_TAG, "client error: " + e.getMessage());
            Log.d(Constant.THREAD_TAG, "client localized: " + e.getLocalizedMessage());

        } finally {
            if (socket != null) {
                if (socket.isConnected()) {
//                        try {
////                            socket.close();
//                        }
//                        catch (IOException e) {
//                            //catch logic
//                        }
                }
            }
        }
    }
}

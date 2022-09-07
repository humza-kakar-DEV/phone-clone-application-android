package com.example.wifip2p;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.wifip2p.Media.Apk;
import com.example.wifip2p.Media.Audio;
import com.example.wifip2p.Media.AudioMedia;
import com.example.wifip2p.Media.Contact;
import com.example.wifip2p.Media.Document;
import com.example.wifip2p.Media.Image;
import com.example.wifip2p.Media.ImageMedia;
import com.example.wifip2p.Media.Video;
import com.example.wifip2p.Media.VideoMedia;
import com.example.wifip2p.Utils.Constant;
import com.example.wifip2p.Utils.FileSizeCalculator;
import com.example.wifip2p.Utils.FileSizes;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread {

    MainActivity2 mainActivity2;
    public ClientThreadHandler clientThreadHandler;

    public ClientThread(MainActivity2 mainActivity2) {
        this.mainActivity2 = mainActivity2;
    }

    @Override
    public void run() {
        super.run();

        Looper.prepare();
        clientThreadHandler = new ClientThreadHandler(mainActivity2);
        Looper.loop();

    }

    public class ClientThreadHandler extends Handler {

        MainActivity2 mainActivity2;
        Context context;
        FileInputStream fis;
        ContentResolver contentResolver;
        Socket clientSocket = null;
        OutputStream os = null;

        String hostAddress;
        int currentFileSize = 0;
        long totalFileSize = 0;

        Image image;
        Audio audio;
        Video video;
        Document document;
        Contact contact;
        Apk apk;
        Object object;
        int objectType;
        String fileType;

        public ClientThreadHandler(MainActivity2 mainActivity2) {
           this.mainActivity2 = mainActivity2;
           this.context = mainActivity2.getApplicationContext();
           contentResolver = context.getApplicationContext().getContentResolver();
        }

        public void dynamicCasting (Object object, int objectType) {
            switch (objectType) {
                case 0:
                    image = (Image) object;
                    try {
                        fis = (FileInputStream) contentResolver.openInputStream(Uri.parse(image.getUri()));
                        DataOutputStream dataOutputStream = new DataOutputStream(os);
                        dataOutputStream.writeUTF(image.getName());
                        fileType = "Image";
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    audio = (Audio) object;
                    try {
                        fis = (FileInputStream) contentResolver.openInputStream(Uri.parse(audio.getUri()));
                        DataOutputStream dataOutputStream = new DataOutputStream(os);
                        dataOutputStream.writeUTF(audio.getSongName());
                        fileType = "Audio";
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    video = (Video) object;
                    try {
                        fis = (FileInputStream) contentResolver.openInputStream(Uri.parse(video.getUri()));
                        DataOutputStream dataOutputStream = new DataOutputStream(os);
                        dataOutputStream.writeUTF(video.getName());
                        fileType = "Video";
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    break;
            }
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            if (msg.getData().getString(Constant.GROUP_OWNER_TAG) != null) {
                hostAddress = msg.getData().getString(Constant.GROUP_OWNER_TAG);
                object = (Object) msg.getData().getSerializable(Constant.AUDIO_TAG);
                objectType = msg.getData().getInt(Constant.DYNAMIC_OBJ_TAG);
            } else {
                return;
            }

            try {

                clientSocket = new Socket(hostAddress, 8888);
                os = clientSocket.getOutputStream();
                PrintWriter pw = new PrintWriter(os);

                InputStream is = clientSocket.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);

                dynamicCasting(object, objectType);

                byte[] buffer = new byte[4096];

                BufferedInputStream bis = new BufferedInputStream(fis);

                long bytesToSend = fis.available();

                totalFileSize += bytesToSend;

                Log.d(Constant.THREAD_TAG, "song name: " + audio.getSongName());

                Log.d(Constant.THREAD_TAG, "file size: " + FileSizeCalculator.getSize(bytesToSend));

                mainActivity2.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        mainActivity2.clientThreadResult(100, 100, audio.getSongName(), fileType);
                        mainActivity2.clientThreadResultFileSize(fileType);
                    }
                });

                while (true) {

                    int bytesRead = bis.read(buffer, 0, buffer.length);

                    if (bytesRead == -1) {
                        break;
                    }

                    currentFileSize += bytesRead;

//                    Log.d(Constant.THREAD_TAG, "bytes: " + currentFileSize);

                    mainActivity2.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                mainActivity2.clientThreadResult(fis.available(), currentFileSize, audio.getSongName(), fileType);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    os.write(buffer, 0, bytesRead);
                    os.flush();

                }

//!             Whole for loop socket code finishes here :

                currentFileSize = 0;
                totalFileSize = 0;

                fis.close();
                bis.close();

                br.close();
                isr.close();
                is.close();

                pw.close();
                os.close();
                clientSocket.close();

                Log.d(Constant.THREAD_TAG, "total file size sent: " + FileSizeCalculator.getSize(totalFileSize));

            } catch (IOException e) {
                Log.d(Constant.THREAD_TAG, "client thread: " + e.getMessage());
            } catch (Exception e) {
                Log.d(Constant.THREAD_TAG, "client thread: " + e.getMessage());
            }

////!                --------------------------------------------------------

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

////!                --------------------------------------------------------

        }
    }
}

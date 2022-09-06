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
import com.example.wifip2p.Media.Document;
import com.example.wifip2p.Media.DynamicObject;
import com.example.wifip2p.Media.Image;
import com.example.wifip2p.Media.ImageMedia;
import com.example.wifip2p.Media.Video;
import com.example.wifip2p.Media.VideoMedia;
import com.example.wifip2p.Utils.Constant;
import com.example.wifip2p.Utils.FileSizeCalculator;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInput;
import java.io.DataInputStream;
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

        String hostAddress;
        int currentFileSize = 0;
        long totalFileSize = 0;
        int fileCount = 0;

        ImageMedia imageMedia;
        AudioMedia audioMedia;
        VideoMedia videoMedia;

        Audio audio;

        public ClientThreadHandler(MainActivity2 mainActivity2) {
           this.mainActivity2 = mainActivity2;
           this.context = mainActivity2.getApplicationContext();
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);


            if (msg.getData().getString(Constant.GROUP_OWNER_TAG) != null) {
                hostAddress = msg.getData().getString(Constant.GROUP_OWNER_TAG);
                audio = (Audio) msg.getData().getSerializable(Constant.AUDIO_TAG);
            } else {
                return;
            }

            Socket clientSocket = null;
            OutputStream os = null;

            try {

                clientSocket = new Socket(hostAddress, 8888);
                os = clientSocket.getOutputStream();
                PrintWriter pw = new PrintWriter(os);

                InputStream is = clientSocket.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);

                byte[] buffer = new byte[4096];

                ContentResolver contentResolver = context.getApplicationContext().getContentResolver();
                FileInputStream fis = (FileInputStream) contentResolver.openInputStream(Uri.parse(audio.getUri()));

                BufferedInputStream bis = new BufferedInputStream(fis);

                DataOutputStream dataOutputStream = new DataOutputStream(os);
                dataOutputStream.writeUTF(audio.getSongName());

                long bytesToSend = fis.available();

                fileCount++;

                totalFileSize += bytesToSend;

                Log.d(Constant.THREAD_TAG, "song name: " + audio.getSongName());

                Log.d(Constant.THREAD_TAG, "file size: " + FileSizeCalculator.getSize(bytesToSend));

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
//                            try {
////                                mainActivity2.clientResult(fis.available(), currentFileSize, audio.getSongName(), fileCount);
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
                        }
                    });

                    os.write(buffer, 0, bytesRead);
                    os.flush();

                }

//!             Whole for loop socket code finishes here :

                currentFileSize = 0;
                totalFileSize = 0;

                mainActivity2.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        mainActivity.clientResult(100, 100, audio.getSongName(), fileCount);
                    }
                });

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

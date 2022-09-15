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

import com.example.wifip2p.Fragment.FileShareFragment;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

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

    public class ClientThreadHandler extends Handler implements FileShareFragment.sendFileToThread {

        MainActivity2 mainActivity2;
        Context context;
        FileInputStream fis = null;
        ContentResolver contentResolver;
        Socket clientSocket = null;
        OutputStream os = null;

        File contactsFile;
        boolean receivedContact = false;

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
        String fileName;

        public ClientThreadHandler(MainActivity2 mainActivity2) {
            this.mainActivity2 = mainActivity2;
            this.context = mainActivity2.getApplicationContext();
            contentResolver = context.getApplicationContext().getContentResolver();
        }

        public void dynamicCasting(Object object, int objectType) {
            switch (objectType) {
                case 0:
                    image = (Image) object;
                    try {
                        fis = (FileInputStream) contentResolver.openInputStream(Uri.parse(image.getUri()));
                        DataOutputStream dataOutputStream = new DataOutputStream(os);
                        dataOutputStream.writeUTF(image.getName());
                        dataOutputStream.writeInt(0);
                        fileType = "Image";
                        fileName = image.getName();
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
                        dataOutputStream.writeInt(1);
                        fileType = "Audio";
                        fileName = audio.getSongName();
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
                        dataOutputStream.writeInt(2);
                        fileType = "Video";
                        fileName = video.getName();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    document = (Document) object;
                    try {
                        fis = (FileInputStream) contentResolver.openInputStream(Uri.parse(document.getContentUri()));
                        DataOutputStream dataOutputStream = new DataOutputStream(os);
                        dataOutputStream.writeUTF(document.getName());
                        dataOutputStream.writeInt(3);
                        fileType = "Document";
                        fileName = document.getName();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    try {
                        DataOutputStream dataOutputStream = new DataOutputStream(os);
                        dataOutputStream.writeUTF("Contacts");
                        dataOutputStream.writeInt(4);
                        fileType = "Contacts";
                        fileName = "Contacts.csv";
                        receivedContact = true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 5:
                    apk = (Apk) object;
                    try {
                        fis = new FileInputStream(apk.getAppPath());
                        Log.d(Constant.THREAD_DATA_SEND, "apk file size: " + fis.available());
                        DataOutputStream dataOutputStream = new DataOutputStream(os);
                        dataOutputStream.writeUTF(apk.getAppName());
                        dataOutputStream.writeInt(5);
                        fileType = "Apk";
                        fileName = apk.getAppName();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            if (msg.getData().getString(Constant.GROUP_OWNER_TAG) != null) {
                hostAddress = msg.getData().getString(Constant.GROUP_OWNER_TAG);
                object = (Object) msg.getData().getSerializable(Constant.DYNAMIC_OBJ_TAG);
                objectType = msg.getData().getInt(Constant.DYNAMIC_INT_TAG);
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

                BufferedInputStream bis = new BufferedInputStream(fis);

                long bytesToSend = fis.available();

                Log.d(Constant.THREAD_TAG, "client thread: bytes = " + bytesToSend);

                byte[] buffer = new byte[4096];

                totalFileSize += bytesToSend;

                mainActivity2.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mainActivity2.clientThreadResultFileSize(fileType);
                    }
                });

                while (true) {

                    int bytesRead = bis.read(buffer, 0, buffer.length);

                    if (bytesRead == -1) {
                        break;
                    }

                    currentFileSize += bytesRead;

                    mainActivity2.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                mainActivity2.clientThreadResult(fis.available(), currentFileSize, fileName, fileType);
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

                if (receivedContact) {
                    contactsFile.delete();
                    receivedContact = !receivedContact;
                }

                fis.close();
                bis.close();

                br.close();
                isr.close();
                is.close();

                pw.close();
                os.close();
                clientSocket.close();

            } catch (IOException e) {
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


        @Override
        public void getFile(FileInputStream fileInputStream, File contactsCsvFile) {
            this.fis = fileInputStream;
            this.contactsFile = contactsCsvFile;
            try {
                Log.d(Constant.THREAD_TAG, "getFile interface: " + fis.available());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

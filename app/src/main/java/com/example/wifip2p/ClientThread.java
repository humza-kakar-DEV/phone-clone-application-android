package com.example.wifip2p;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.wifip2p.Fragment.FileShareFragment;
import com.example.wifip2p.Media.Apk;
import com.example.wifip2p.Media.Audio;
import com.example.wifip2p.Media.Contact;
import com.example.wifip2p.Media.Document;
import com.example.wifip2p.Media.Image;
import com.example.wifip2p.Media.Video;
import com.example.wifip2p.Utils.Constant;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
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

    public class ClientThreadHandler extends Handler implements FileShareFragment.sendFileToThread {

        MainActivity2 mainActivity2;
        Context context;
        ContentResolver contentResolver;

        FileInputStream fis = null;
        BufferedInputStream bufferedInputStream = null;
        Socket clientSocket = null;
        OutputStream outputStream = null;
        DataOutputStream dataOutputStream = null;

        File contactsFile;
        boolean receivedContact = false;

        String hostAddress;
        int currentFileSize = 0;
        long totalFileSize = 0;
        long bytesToSend;

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

        public void dynamicCasting(Object object, int objectType) throws IOException {
            switch (objectType) {
                case 0:
                    image = (Image) object;
                    dataOutputStream.writeUTF(image.getName());
                    dataOutputStream.writeInt(0);
                    fileType = "Image";
                    fileName = image.getName();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        fis = (FileInputStream) contentResolver.openInputStream(Uri.parse(image.getUri()));
                    } else {
                        File file = new File(image.getUri());
                        fis = new FileInputStream(file);
                    }
                    break;
                case 1:
                    audio = (Audio) object;
                    dataOutputStream.writeUTF(audio.getSongName());
                    dataOutputStream.writeInt(1);
                    fileType = "Audio";
                    fileName = audio.getSongName();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        fis = (FileInputStream) contentResolver.openInputStream(Uri.parse(audio.getUri()));
                    } else {
                        File file = new File(audio.getUri());
                        fis = new FileInputStream(file);
                    }
                    break;
                case 2:
                    video = (Video) object;
                    dataOutputStream.writeUTF(video.getName());
                    dataOutputStream.writeInt(2);
                    fileType = "Video";
                    fileName = video.getName();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        fis = (FileInputStream) contentResolver.openInputStream(Uri.parse(video.getUri()));
                    } else {
                        File file = new File(video.getUri());
                        fis = new FileInputStream(file);
                    }
                    break;
                case 3:
                    document = (Document) object;
                    dataOutputStream.writeUTF(document.getName());
                    dataOutputStream.writeInt(3);
                    fileType = "Document";
                    fileName = document.getName();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        fis = (FileInputStream) contentResolver.openInputStream(Uri.parse(document.getContentUri()));
                    } else {
                        File file = new File(document.getContentUri());
                        fis = new FileInputStream(file);
                    }
                    break;
                case 4:
                    try {
                        dataOutputStream = new DataOutputStream(outputStream);
                        dataOutputStream.writeUTF("Contacts");
                        dataOutputStream.writeInt(4);
                        fileType = "Contacts";
                        fileName = "Contacts.csv";
                        receivedContact = true;
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 5:
                    apk = (Apk) object;
                    try {
                        fis = new FileInputStream(apk.getAppPath());
                        dataOutputStream.writeUTF(apk.getAppName());
                        dataOutputStream.writeInt(5);
                        fileType = "Apk";
                        fileName = apk.getAppName();
                    }
                    catch (FileNotFoundException e) {
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
            }

            try {

                clientSocket = new Socket(hostAddress, 8888);
                outputStream = clientSocket.getOutputStream();
                dataOutputStream = new DataOutputStream(outputStream);

                dynamicCasting(object, objectType);

//                Log.d(Constant.THREAD_TAG, "client thread - file size: " + fis.available());

                byte[] buffer = new byte[4096];

                bytesToSend = fis.available();

                totalFileSize += bytesToSend;

                Log.d(Constant.THREAD_TAG, "file total size: " + totalFileSize);

                bufferedInputStream = new BufferedInputStream(fis);

                mainActivity2.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mainActivity2.clientThreadResultFileCount(fileType);
                    }
                });

                while (true) {

                    int bytesRead = bufferedInputStream.read(buffer, 0, buffer.length);

                    if (bytesRead == -1) {
                        break;
                    }

                    currentFileSize += bytesRead;

                    mainActivity2.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mainActivity2.clientThreadResult((int) totalFileSize, currentFileSize, fileName, fileType);
                        }
                    });

                    outputStream.write(buffer, 0, bytesRead);
                    outputStream.flush();

                }

                currentFileSize = 0;
                totalFileSize = 0;

                if (receivedContact) {
                    contactsFile.delete();
                    receivedContact = !receivedContact;
                }

                dataOutputStream.close();
                outputStream.close();
                fis.close();
                bufferedInputStream.close();
                clientSocket.close();

            } catch (IOException e) {

                Log.d(Constant.THREAD_TAG, "client thread connection status: " + e.getMessage());

            }

//            try {
//
//                clientSocket = new Socket(hostAddress, 8888);
//                os = clientSocket.getOutputStream();
//                InputStream is = clientSocket.getInputStream();
//                dataOutputStream = new DataOutputStream(os);
//                bufferedInputStream = new BufferedInputStream(fis);
//
//                dynamicCasting(object, objectType);
//
//                bytesToSend = fis.available();
//
//               Log.d(Constant.THREAD_TAG, "client thread: bytes = " + bytesToSend);
//
//                byte[] buffer = new byte[4096];
//
//                totalFileSize += bytesToSend;
//
//                mainActivity2.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.d(Constant.CONNECTION_TAG, "client thread file type: " + fileType);
//                        mainActivity2.clientThreadResultFileSize(fileType);
//                    }
//                });
//
//                while (true) {
//
//                    int bytesRead = bufferedInputStream.read(buffer, 0, buffer.length);
//
//                    if (bytesRead == -1) {
//                        break;
//                    }
//
//                    currentFileSize += bytesRead;
//
//                    mainActivity2.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            mainActivity2.clientThreadResult(0, currentFileSize, fileName, fileType);
//                        }
//                    });
//
//                    os.write(buffer, 0, bytesRead);
//                    os.flush();
//
//                }
//
////!             Whole for loop socket code finishes here :
//
//                currentFileSize = 0;
//                totalFileSize = 0;
//
//                if (receivedContact) {
//                    contactsFile.delete();
//                    receivedContact = !receivedContact;
//                }
//
//                dataOutputStream.close();
//                fis.close();
//                bufferedInputStream.close();
//                is.close();
//                os.close();
//                clientSocket.close();
//
//            }
//
//            catch (IOException e) {
//                Log.d(Constant.THREAD_TAG, "client thread connection: " + e.getMessage());
//            }

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

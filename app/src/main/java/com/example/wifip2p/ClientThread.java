package com.example.wifip2p;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

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

    public class ClientThreadHandler extends Handler {

        MainActivity mainActivity;
        Context context;
        Socket socket = new Socket();
        byte buf[]  = new byte[1024];
        int len;

        public ClientThreadHandler (MainActivity mainActivity) {
            this.mainActivity = mainActivity;
            context = mainActivity.getApplicationContext();
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            if (msg.getData() == null) {
                return;
            }

            try {

                Log.d(TAG, "client thread: started before connection");

                String host = msg.getData().getString("hmHostAddress");
                socket.bind(null);
                socket.connect(new InetSocketAddress(host, 8888), 5000);

                Log.d(TAG, "client thread: connected to server thread WELCOME!");

                OutputStream outputStream = socket.getOutputStream();
                ContentResolver cr = context.getContentResolver();
                File file = new File("/sdcard/DCIM/Camera/download.jpg");
                FileInputStream fileInputStream = new FileInputStream(file);

                Log.d(TAG_FILE, "client thread - file size: " + fileInputStream.available());

                while ((len = fileInputStream.read(buf)) != -1) {
                    outputStream.write(buf, 0, len);
                }

                outputStream.close();
                fileInputStream.close();
            } catch (FileNotFoundException e) {
                //catch logic
            } catch (IOException e) {
                //catch logic
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

package com.example.wifip2p;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends Thread {

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

    public class ServerThreadHandler extends Handler {

        MainActivity mainActivity;
        Context context;
        ServerSocket serverSocket;
        Socket client;
        byte buf[]  = new byte[1024];
        int len;

        public ServerThreadHandler (MainActivity mainActivity) {
            this.mainActivity = mainActivity;
            context = mainActivity.getApplicationContext();
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            try {

                Log.d(TAG, "server thread: started before connection");

                serverSocket = new ServerSocket(8888);
                client = serverSocket.accept();

                Log.d(TAG, "server thread: connected to client thread WELCOME!");

                final File f = new File(Environment.getExternalStorageDirectory() + "/"
                        + context.getPackageName() + "/wifip2pshared-" + System.currentTimeMillis()
                        + ".jpg");

                File dirs = new File(f.getParent());
                if (!dirs.exists())
                    Log.d(TAG_FILE, "server thread - folder created: " + dirs.mkdirs());
                Log.d(TAG_FILE, "server thread - file created: " + f.createNewFile());

                InputStream inputStream = client.getInputStream();
                FileOutputStream fileOutputStream = new FileOutputStream(f);

                Log.d(TAG_FILE, "server thread - file size: " + inputStream.available());

                while ((len = inputStream.read()) != -1) {
                    fileOutputStream.write(len);
                }
                serverSocket.close();
                Log.d(TAG_FILE, "server thread: - file path: " + f.getAbsolutePath());

                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mainActivity.showFile(f.getAbsolutePath());
                    }
                });

            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }
}

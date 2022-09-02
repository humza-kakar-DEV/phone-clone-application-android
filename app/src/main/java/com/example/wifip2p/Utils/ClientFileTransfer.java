package com.example.wifip2p.Utils;

import android.util.Log;

import com.example.wifip2p.Media.Audio;
import com.example.wifip2p.Media.AudioMedia;
import com.example.wifip2p.Media.DynamicObject;

import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientFileTransfer extends Thread {

    String groupOwnerAddress;

    public ClientFileTransfer (String groupOwnerAddress) {
        this.groupOwnerAddress = groupOwnerAddress;
    }

    @Override
    public void run() {
        super.run();

        try {
            Socket socket = new Socket();
            socket.bind(null);
            socket.connect(new InetSocketAddress(groupOwnerAddress, 5000), 5000);
            InputStream in = socket.getInputStream();
            DataInputStream dataInputStream = new DataInputStream(in);

            while(true) {
                Log.d(Constant.THREAD_TAG, "run: " + dataInputStream.readUTF());
//                FileOutputStream fos =
//                        new FileOutputStream("I:\\MY-LEARNINGS\\JAVA\\Workspace\\client\\"+System.currentTimeMillis()+"-data.txt");
//                int read = 0;
//                byte[] mybytearray = new byte[4096];
//                while ((read = dis.read(mybytearray)) != -1 && read != 3) {
//                    fos.write(mybytearray, 0, read);
//                    fos.flush();
//                }
//                fos.close();
                //System.out.println("The value of read : "+read);
                // System.out.println("File has been received successfully.");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

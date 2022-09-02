package com.example.wifip2p.Utils;

import android.util.Log;

import com.example.wifip2p.MainActivity;
import com.example.wifip2p.Media.Audio;
import com.example.wifip2p.Media.AudioMedia;
import com.example.wifip2p.Media.DynamicObject;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class ServerFileTransfer extends Thread {

    MainActivity mainActivity;
    int count;

    public ServerFileTransfer(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void run() {
        super.run();

        try {

            byte[] done = new byte[3];
            String str = "done";  //randomly anything
            done = str.getBytes();
            ServerSocket ss = new ServerSocket(5000);
            Socket socket = ss.accept();
            byte[] mybytearray = new byte[4096];
            OutputStream os = socket.getOutputStream();

            AudioMedia audioMedia = new AudioMedia(mainActivity);

            while (true) {

                count++;

                if (count == 10) {
                    count = 0;
                }

                Audio audio = audioMedia.generateAudios().get(count);

              DataOutputStream dataOutputStream = new DataOutputStream(os);

                dataOutputStream.writeUTF(audio.getSongName());

//                FileInputStream fis = new FileInputStream(myFile);
//                BufferedInputStream bis = new BufferedInputStream(fis);
//                DataInputStream dis = new DataInputStream(bis);
//                File myFile= new File("I:\\MY-LEARNINGS\\JAVA\\Workspace\\server\\src\\com\\dd\\server\\gistfile1.txt");
//                dos.writeUTF(Integer.toString(count));
//                dos.writeLong(myFile.length());
//                int read;
//                System.out.println("---------File Writing started----------");
//                int count = 0;
//                while((read = dis.read(mybytearray)) != -1){
//                    dos.write(mybytearray, 0, read);
//                    dos.flush();
//                    ++count;
//                    System.out.println("Writing sub component of file. Step : "+count);
//                }
//                System.out.println("---------File Writing ended----------");
//                System.out.println("Flushing data DONE command sent.");
//                dis.close();
//                bis.close();
//                fis.close();
//                TimeUnit.SECONDS.sleep(1);
//                System.out.println("File transfer has been completed.");
//                dos.write(done, 0, 3);
//                objectOutputStream.flush();
                dataOutputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

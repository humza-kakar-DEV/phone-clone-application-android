package com.example.wifip2p;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModel;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wifip2p.Media.Apk;
import com.example.wifip2p.Media.ApkMedia;
import com.example.wifip2p.Media.Audio;
import com.example.wifip2p.Media.AudioMedia;
import com.example.wifip2p.Media.Contact;
import com.example.wifip2p.Media.ContactMedia;
import com.example.wifip2p.Media.Document;
import com.example.wifip2p.Media.DocumentMedia;
import com.example.wifip2p.Media.Image;
import com.example.wifip2p.Media.ImageMedia;
import com.example.wifip2p.Media.Video;
import com.example.wifip2p.Media.VideoMedia;
import com.example.wifip2p.Utils.ClientFileTransfer;
import com.example.wifip2p.Utils.Constant;
import com.example.wifip2p.Utils.ServerFileTransfer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.lang.invoke.ConstantCallSite;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<WifiP2pDevice> refreshedPeers = new ArrayList<WifiP2pDevice>();
    String[] deviceNameArray;
    WifiP2pDevice[] deviceArray;
    ServerThread serverThread;
    ClientThread clientThread;

    private Button button;
    private FloatingActionButton floatingActionButton;
    private ListView listView;
    private TextView textView, serverTextView, clientTextView;
    private ProgressBar progressBar;

    private final IntentFilter intentFilter = new IntentFilter();
    private WifiP2pManager manager;
    private WifiDirectBroadcastReceiver wifiDirectBroadcast;
    private List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
    private WifiP2pManager.Channel channel;

    ImageMedia imageMedia;
    VideoMedia videoMedia;
    AudioMedia audioMedia;
    DocumentMedia documentMedia;
    ApkMedia apkMedia;
    ContactMedia contactMedia;

    Audio audio;
    Image image;
    Document document;
    Apk apk;
    Contact contact;

    int imageSize;
    int audioSize;
    int videoSize;
    int documentSize;
    int contactSize;
    int apkSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        textView = (TextView) findViewById(R.id.textView);
        serverTextView = (TextView) findViewById(R.id.serverTextView);
        clientTextView = (TextView) findViewById(R.id.clientTextView);
        progressBar = (ProgressBar) findViewById(R.id.clientProgressBar);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                intent.putExtra("per1", imageSize);
                intent.putExtra("per2", audioSize);
                intent.putExtra("per3", videoSize);
                intent.putExtra("per4", documentSize);
                intent.putExtra("per5", contactSize);
                intent.putExtra("per6", apkSize);
                startActivity(intent);
            }
        });

        serverTextView.setVisibility(View.GONE);
        clientTextView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        imageMedia = new ImageMedia(this);
        audioMedia = new AudioMedia(this);
        contactMedia = new ContactMedia(this);
        videoMedia = new VideoMedia(this);
        documentMedia = new DocumentMedia(this);
        apkMedia = new ApkMedia(this);

        imageSize = getIntent().getIntExtra("per1", 0);
        audioSize = getIntent().getIntExtra("per2", 0);
        videoSize = getIntent().getIntExtra("per3", 0);
        documentSize = getIntent().getIntExtra("per4", 0);
        contactSize = getIntent().getIntExtra("per5", 0);
        apkSize = getIntent().getIntExtra("per6", 0);

// ------------------**************---------------------}

        // Indicates a change in the Wi-Fi P2P status.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);

        // Indicates a change in the list of available peers.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);

        // Indicates the state of Wi-Fi P2P connectivity has changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);

        // Indicates this device's details have changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(this, getMainLooper(), null);

        manager.removeGroup(channel, actionListener);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                    Toast.makeText(MainActivity.this, "discovery started", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(int i) {
                    Toast.makeText(MainActivity.this, "discovery failed", Toast.LENGTH_SHORT).show();
                }
            });
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                WifiP2pDevice wifiP2pDevice = peers.get(i);
                WifiP2pConfig wifiP2pConfig = new WifiP2pConfig();
                wifiP2pConfig.deviceAddress = wifiP2pDevice.deviceAddress;
                wifiP2pConfig.wps.setup = WpsInfo.PBC;

                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    manager.connect(channel, wifiP2pConfig, new WifiP2pManager.ActionListener() {
                        @Override
                        public void onSuccess() {
//                            connecting to the wifi direct group
                        }

                        @Override
                        public void onFailure(int i) {
//                            connection failed
                        }
                    });
                }
            }
        });
    }

    private final WifiP2pManager.ActionListener actionListener = new WifiP2pManager.ActionListener() {
        @Override
        public void onSuccess() {
            Toast.makeText(MainActivity.this, "wifi direct connection disabled", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailure(int i) {

        }
    };

    private WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList) {
            if (!wifiP2pDeviceList.getDeviceList().equals(peers)) {
                peers.clear();
                peers.addAll(wifiP2pDeviceList.getDeviceList());

                deviceNameArray = new String[wifiP2pDeviceList.getDeviceList().size()];
                deviceArray = new WifiP2pDevice[wifiP2pDeviceList.getDeviceList().size()];

                int index = 0;
                for (WifiP2pDevice wifiP2pDevice : wifiP2pDeviceList.getDeviceList()) {
                    deviceNameArray[index] = wifiP2pDevice.deviceName;
                    deviceArray[index] = wifiP2pDevice;
                    index++;
                }

                ArrayAdapter<String> adapter =
                        new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, deviceNameArray);
                listView.setAdapter(adapter);

                if (peers.size() == 0) {
//                    device not found
                    return;
                }
            }
        }
    };

    public WifiP2pManager.ConnectionInfoListener connectionInfoListener = new WifiP2pManager.ConnectionInfoListener() {
        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {
            String groupOwnerAddress = wifiP2pInfo.groupOwnerAddress.getHostAddress();
            if (wifiP2pInfo.groupFormed && wifiP2pInfo.isGroupOwner) {

//                server block which will receive files from client

//              UI coding
                Toast.makeText(MainActivity.this, "connection server", Toast.LENGTH_SHORT).show();
                textView.setText("CONNECTION SERVER");
                serverTextView.setVisibility(View.VISIBLE);

//              thread coding
                serverThread = new ServerThread(MainActivity.this);
                serverThread.setName("server thread");
                serverThread.start();


            } else if (wifiP2pInfo.groupFormed) {

//                client block which will send files to the server

//                UI coding
                Toast.makeText(MainActivity.this, "connection client", Toast.LENGTH_SHORT).show();
                textView.setText("CONNECTION CLIENT");

//                thread coding

                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                intent.putExtra(Constant.GROUP_OWNER_TAG, groupOwnerAddress);
                intent.putExtra("per1", imageSize);
                intent.putExtra("per2", audioSize);
                intent.putExtra("per3", videoSize);
                intent.putExtra("per4", documentSize);
                intent.putExtra("per5", contactSize);
                intent.putExtra("per6", apkSize);
                startActivity(intent);

            }
        }
    };

    public void serverResult(int fileCount, String fileName) {
        serverTextView.setVisibility(View.VISIBLE);
        serverTextView.setText(fileName + ": " + fileCount);
    }

    public void clientResult(int totalFileSize, int currentFileSize, String fileName, int fileCount) {
        clientTextView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        clientTextView.setText(fileName + " --- " + fileCount);
        progressBar.setMax(totalFileSize);
        progressBar.setProgress(currentFileSize);
    }

    //  this method is called from server thread
    public void showFile(String filePath) {
        if (filePath != null) {
            Intent intent = new Intent();
            intent.setAction(android.content.Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse("file://" + filePath), "image/*");
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        wifiDirectBroadcast = new WifiDirectBroadcastReceiver(this, peerListListener, connectionInfoListener, manager, channel);
        registerReceiver(wifiDirectBroadcast, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(wifiDirectBroadcast);
    }
}
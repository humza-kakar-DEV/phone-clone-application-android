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
import android.widget.TextView;
import android.widget.Toast;

import com.example.wifip2p.Media.Audio;
import com.example.wifip2p.Media.AudioMedia;
import com.example.wifip2p.Media.Contact;
import com.example.wifip2p.Media.ContactMedia;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ViewModel viewModel;

    private static final String TAG = "hmConnection";
    private static final String AUDIO_TAG = "hmAudioKey";

    private ListView listView;
    private TextView textView;
    private List<WifiP2pDevice> refreshedPeers = new ArrayList<WifiP2pDevice>();
    String[] deviceNameArray;
    WifiP2pDevice[] deviceArray;
    ServerThread serverThread;
    ClientThread clientThread;

    List<Audio> audioList = new ArrayList<>();
    List<Contact> contactList = new ArrayList<>();

    private final IntentFilter intentFilter = new IntentFilter();
    private WifiP2pManager manager;
    private WifiDirectBroadcastReceiver wifiDirectBroadcast;
    private List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
    private WifiP2pManager.Channel channel;

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        textView = (TextView) findViewById(R.id.textView);

        AudioMedia audioMedia = new AudioMedia(this);
        ContactMedia contactMedia = new ContactMedia(this);

        contactList.addAll(contactMedia.getContactList());

//        for (Contact contact : contactList) {
//            Log.d(TAG, "user name: " + contact.getName());
//            Log.d(TAG, "phone number: " + contact.getPhoneNo());
//        }

//        ContactMedia contactMedia = new ContactMedia(this);

        for (int i = 0; i <= 3; i++) {
            audioList.add(audioMedia.generateAudios().get(i));
        }

        for (int i = 1; i <= 3; i++) {
//            contactList.add(contactMedia.getContactList().get(i));
        }

//      starting thread, always call start method which will
//      run thread in background, run method executes thread
//      on ui/main thread

        serverThread = new ServerThread(MainActivity.this);
        serverThread.setName("server thread");
        serverThread.start();

        clientThread = new ClientThread(MainActivity.this);
        clientThread.setName("client thread");
        clientThread.start();

// ------------------**************---------------------

//        if (clientThread.getClientThreadHandler() != null) {
//            for (Audio audio : audioList) {
//                Bundle bundle = new Bundle();
//                Message message = Message.obtain();
//                bundle.putSerializable(AUDIO_TAG, audio);
//                message.setData(bundle);
//                clientThread.getClientThreadHandler().sendMessage(message);
//            }
//        }

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

        manager.removeGroup(channel , actionListener);

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
                        new ArrayAdapter<String>(MainActivity.this , android.R.layout.simple_list_item_1 , deviceNameArray);
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

//                UI coding
                Toast.makeText(MainActivity.this, "connection owner", Toast.LENGTH_SHORT).show();
                textView.setText("CONNECTION SERVER");

//              thread coding
                Message message = Message.obtain();
                message.arg1 = 1;
                serverThread.serverThreadHandler.sendMessage(message);

            } else if (wifiP2pInfo.groupFormed) {

//                client block which will send files to the server

//                UI coding
                Toast.makeText(MainActivity.this, "connection client", Toast.LENGTH_SHORT).show();
                textView.setText("CONNECTION CLIENT");

//                thread coding
//                Audio audio = audioList.get(1);
                Contact contact = contactList.get(0);

                Message message = Message.obtain();

                Bundle bundle = new Bundle();
                bundle.putString("hmHostAddress", groupOwnerAddress);
                bundle.putString("fileTypeKey", contact.getClassName());
                bundle.putSerializable(AUDIO_TAG, contact);

                message.setData(bundle);

                clientThread.getClientThreadHandler().sendMessage(message);

            }
        }
    };

    public void setIsWifiP2pEnabled (boolean value) {
        if (value) {
            Log.d(TAG, "setIsWifiP2pEnabled: enabled");
//            Toast.makeText(this, "wifi enabled", Toast.LENGTH_SHORT).show();
        } else {
            Log.d(TAG, "setIsWifiP2pEnabled: disabled");
//            Toast.makeText(this, "wifi disabled", Toast.LENGTH_SHORT).show();
        }
    }

//  this method is called from server thread
    public void showFile (String filePath) {
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
        registerReceiver(wifiDirectBroadcast , intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(wifiDirectBroadcast);
    }
}
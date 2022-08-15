package com.example.wifip2p.Utils;

import android.net.Uri;
import android.widget.CheckBox;

import com.example.wifip2p.Media.Apk;
import com.example.wifip2p.Media.Audio;
import com.example.wifip2p.Media.Contact;
import com.example.wifip2p.Media.Document;
import com.example.wifip2p.Media.Image;
import com.example.wifip2p.Media.Video;

import java.util.List;

public interface CommunicationInterface {
    void changeFragment (String fileType, List<?> dynamicList);
    void singleSelection (Object object, String objectType, boolean state);
    void checkSelection (CheckBox checkBox, String type);
    void clearList (String name);
    void allSelectionInterfaceMethod (List<Image> imageList, List<Audio> audioList, List<Video> videoList, List<Contact> contactList, List<Document> documentList, List<Apk> apkList);
    void sendData ();
}

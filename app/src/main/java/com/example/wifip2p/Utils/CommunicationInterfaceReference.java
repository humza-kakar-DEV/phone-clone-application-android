package com.example.wifip2p.Utils;

import android.content.Context;
import android.net.Uri;
import android.widget.CheckBox;

import com.example.wifip2p.Media.Apk;
import com.example.wifip2p.Media.Audio;
import com.example.wifip2p.Media.Contact;
import com.example.wifip2p.Media.Document;
import com.example.wifip2p.Media.Image;
import com.example.wifip2p.Media.Video;

import java.util.List;

public class CommunicationInterfaceReference {

    private CommunicationInterface communicationInterface;

    public CommunicationInterfaceReference (Context context) {
        this.communicationInterface = (CommunicationInterface) context;
    }

    public void invokeChangeFragment (String fileType, List<?> dynamicList) {
            communicationInterface.changeFragment(fileType, dynamicList);
    }

    public void invokeSingleSelection (Object object, String objectType, boolean state) {
        communicationInterface.singleSelection (object, objectType, state);
    }

    public void invokeCheckSelection (CheckBox checkBox, String type) {
        communicationInterface.checkSelection(checkBox, type);
    }

    public void invokeSendData () {
        communicationInterface.sendData();
    }

    public void invokeClearList (String name) {
        communicationInterface.clearList(name);
    }

    public void invokeAllSelectionInterfaceMethod (List<Image> imageList, List<Audio> audioList, List<Video> videoList, List<Contact> contactList, List<Document> documentList, List<Apk> apkList) {
        communicationInterface.allSelectionInterfaceMethod(imageList, audioList, videoList, contactList, documentList, apkList);
    }

    public void invokeFileShareFragmentState (boolean state) {
        communicationInterface.fileShareFragmentState(state);
    }
}

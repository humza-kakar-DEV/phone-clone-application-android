package com.example.wifip2p.Utils;

import android.net.Uri;
import android.widget.CheckBox;

import com.example.wifip2p.Media.Image;

import java.util.List;

public class CommunicationInterfaceReference {

    private CommunicationInterface communicationInterface;

    public CommunicationInterfaceReference (CommunicationInterface communicationInterface) {
        this.communicationInterface = communicationInterface;
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
}

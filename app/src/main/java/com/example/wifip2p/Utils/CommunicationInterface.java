package com.example.wifip2p.Utils;

import android.net.Uri;
import android.widget.CheckBox;

import com.example.wifip2p.Media.Image;

import java.util.List;

public interface CommunicationInterface {
    void changeFragment (String fileType, List<?> dynamicList);
    void singleSelection (Object object, String objectType, boolean state);
    void checkSelection (CheckBox checkBox, String type);
    void clearList (String name);
    void sendData ();
}

package com.example.wifip2p.Media;

import java.io.Serializable;

public class Contact implements Serializable {

    String className;
    String name;
    String phoneNo;
    boolean isSelected;

    public Contact(String name, String phoneNo, boolean isSelected) {
        this.name = name;
        this.phoneNo = phoneNo;
        this.isSelected = isSelected;
    }

    public String getClassName() {
        className = "contact";
        return className;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

}

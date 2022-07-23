package com.example.wifip2p.Media;

public class Contact {

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

    String name;
    String phoneNo;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    boolean isSelected;

    public Contact(String name, String phoneNo, boolean isSelected) {
        this.name = name;
        this.phoneNo = phoneNo;
        this.isSelected = isSelected;
    }

}

package com.example.wifip2p.Media;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.io.Serializable;

public class Apk implements Serializable {

    String className;
    String appName;
    String appPackageName;
    String appPath;
    Drawable appIcon;
    boolean isSelected;

    public Apk(String appName, String appPackageName, String appPath, Drawable appIcon, boolean isSelected) {
        this.appName = appName;
        this.appPackageName = appPackageName;
        this.appPath = appPath;
        this.appIcon = appIcon;
        this.isSelected = isSelected;
    }

    public String getClassName() {
        className = "apk";
        return className;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppPackageName() {
        return appPackageName;
    }

    public void setAppPackageName(String appPackageName) {
        this.appPackageName = appPackageName;
    }

    public String getAppPath() {
        return appPath;
    }

    public void setAppPath(String appPath) {
        this.appPath = appPath;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }
}

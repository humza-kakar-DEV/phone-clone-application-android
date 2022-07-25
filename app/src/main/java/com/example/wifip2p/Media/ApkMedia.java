package com.example.wifip2p.Media;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ApkMedia {

    public static final String TAG = "hmApk";
    Context context;

    public ApkMedia (Context context) {
        this.context = context;
    }

    public List<Apk> getInstalledapk () {

        List<Apk> apkList = new ArrayList<Apk>();

        final PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> packages =  pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo packageInfo : packages) {
            apkList.add(new Apk(packageInfo.packageName, packageInfo.sourceDir, false));
        }

        return apkList;
    }

}

package com.example.wifip2p.Media;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ApkMedia {

    public static final String TAG = "hmApk";
    Context context;

    public ApkMedia (Context context) {
        this.context = context;
    }

    public int getApkSize () {

        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> totalSize = context.getPackageManager().queryIntentActivities(intent, 0);

        return totalSize.size();
    }

    public List<Apk> getInstalledPackages () {

        List<Apk> apkList = new ArrayList<>();
        List<ResolveInfo> resolveInfos = new ArrayList<>();

        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        resolveInfos = context.getPackageManager().queryIntentActivities(intent, 0);

        for (ResolveInfo resolveInfo : resolveInfos) {
            String appName = resolveInfo.activityInfo.applicationInfo.name;
            String appPackageName = resolveInfo.activityInfo.packageName;
            String appPath = resolveInfo.activityInfo.applicationInfo.sourceDir;
            Drawable appIcon = resolveInfo.activityInfo.loadIcon(context.getPackageManager());

            apkList.add(new Apk(appName, appPackageName, appPath, appIcon, false));

            Log.d(TAG, "apk media: " + appPath);
        }

        return apkList;

    }

}

package com.example.wifip2p;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import java.security.Permission;

public class PermissionActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1122;
    private static final int ALL_FILE_ACCESS = 2296;
    private static final String TAG = "hmPermission";
    String[] permissions = new String[] {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.QUERY_ALL_PACKAGES,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);

        checkPermission();
    }

    private void checkPermission() {
        Intent intent = new Intent(this, MainActivity2.class);
        int result1 = checkSelfPermission(permissions[0]);
        int result2 = checkSelfPermission(permissions[1]);
        int result3 = checkSelfPermission(permissions[2]);
        int result4 = checkSelfPermission(permissions[3]);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager() && (result1 == PackageManager.PERMISSION_GRANTED) && (result2 == PackageManager.PERMISSION_GRANTED) && (result3 == PackageManager.PERMISSION_GRANTED)) {
                startActivity(intent);
            } else {
                Toast.makeText(this, "All file access is required", Toast.LENGTH_SHORT).show();
                requestPermissions(permissions, REQUEST_CODE);
                try {
                    Intent settingsIntent1 = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    settingsIntent1.addCategory("android.intent.category.DEFAULT");
                    settingsIntent1.setData(Uri.parse(String.format("package:%s",getApplicationContext().getPackageName())));
                    startActivityForResult(settingsIntent1, ALL_FILE_ACCESS);
                } catch (Exception e) {
                    Intent settingsIntent2 = new Intent();
                    settingsIntent2.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    startActivityForResult(settingsIntent2, ALL_FILE_ACCESS);
                }
            }
        }
            if (((result1 == PackageManager.PERMISSION_GRANTED) && (result2 == PackageManager.PERMISSION_GRANTED) && (result3 == PackageManager.PERMISSION_GRANTED)) ) {
                startActivity(intent);
            } else {
                requestPermissions(permissions, REQUEST_CODE);
            }
        }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if ((grantResults.length > 0) && (requestCode == REQUEST_CODE)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (requestCode == ALL_FILE_ACCESS) {
                    checkPermission();
                }
            }
            checkPermission();
        }

    }
}
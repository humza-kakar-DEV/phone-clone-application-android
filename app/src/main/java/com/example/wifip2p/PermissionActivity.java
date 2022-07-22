package com.example.wifip2p;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.security.Permission;

public class PermissionActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1122;
    private static final String TAG = "hmPermission";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);

        checkPermission();
    }

    private void checkPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            String[] permissions = new String[] {
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.READ_CONTACTS,
            };
            int result1 = checkSelfPermission(permissions[0]);
            int result2 = checkSelfPermission(permissions[1]);
            if ((result1 == PackageManager.PERMISSION_GRANTED) && (result2 == PackageManager.PERMISSION_GRANTED)) {
                Intent intent = new Intent(PermissionActivity.this, MainActivity2.class);
                startActivity(intent);
                finish();
            } else {
                requestPermissions(permissions, REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if ((grantResults.length > 0) && (requestCode == REQUEST_CODE)) {
            checkPermission();
        }
    }
}
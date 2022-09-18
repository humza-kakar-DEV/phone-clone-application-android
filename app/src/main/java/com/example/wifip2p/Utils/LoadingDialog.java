package com.example.wifip2p.Utils;

import android.app.Activity;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.wifip2p.R;

public class LoadingDialog {

    private FragmentActivity fragmentActivity;
    private AlertDialog alertDialog;

    public LoadingDialog(FragmentActivity fragmentActivity)
    {
        this.fragmentActivity = fragmentActivity;
    }

    public void startLoadingDialog()
    {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(fragmentActivity);
        LayoutInflater inflater = fragmentActivity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_dialog_loading,null));
        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.show();
    }


    public void dismissDialog()
    {
        alertDialog.dismiss();
    }

}

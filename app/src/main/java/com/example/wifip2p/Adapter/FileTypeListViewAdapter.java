package com.example.wifip2p.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.wifip2p.Media.Image;
import com.example.wifip2p.R;

import org.w3c.dom.Text;

public class FileTypeListViewAdapter extends BaseAdapter {

    Context context;
    int[] drawableId;
    String[] fileType;
    LayoutInflater layoutInflater;
    private boolean checkBoxState;
    private CheckBox checkBox;

    public FileTypeListViewAdapter (Context context , int[] drawableId, String[] fileType) {
        this.context = context;
        this.drawableId = drawableId;
        this.fileType = fileType;
        layoutInflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return fileType.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View listView = layoutInflater.inflate(R.layout.list_view_file_type, null);
        TextView textViewFileType = (TextView) listView.findViewById(R.id.textViewFileType);
        ImageView imageView = (ImageView) listView.findViewById(R.id.imageView);
        checkBox = (CheckBox) listView.findViewById(R.id.checkBox);

        textViewFileType.setText(fileType[i]);
        imageView.setImageResource(drawableId[i]);

        return listView;
    }

    public void allSelect (boolean state) {
        if (checkBox != null) {
            checkBox.setChecked(state);
        }
    }
}

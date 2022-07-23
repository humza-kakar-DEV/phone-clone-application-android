package com.example.wifip2p.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wifip2p.Media.Audio;
import com.example.wifip2p.Media.Contact;
import com.example.wifip2p.Media.Image;
import com.example.wifip2p.Media.Video;
import com.example.wifip2p.R;
import com.example.wifip2p.Utils.CommunicationInterface;
import com.example.wifip2p.Utils.CommunicationInterfaceReference;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

public class FileRecyclerViewAdapter extends RecyclerView.Adapter<FileRecyclerViewAdapter.MyRecyclerViewHolder> {

    public static final String TAG = "myR";
    List<Image> imageList = new ArrayList<>();
    List<Video> videoList = new ArrayList<>();
    List<Audio> audioList = new ArrayList<>();
    List<Contact> contactList = new ArrayList<>();

    private List<Bitmap> thumbnails;
    private List<?> dynamicList;
    private String fileType;
    private FragmentActivity fragmentActivity;
    private Context context;
    CheckBox typeCheckBox;
    CommunicationInterfaceReference communicationInterfaceReference;

    public FileRecyclerViewAdapter(List<?> list, String fileType, List<Bitmap> thumbnails, CheckBox typeCheckBox, Context context, FragmentActivity fragmentActivity) {
        this.context = context;
        this.thumbnails = thumbnails;
        this.dynamicList = list;
        this.fileType = fileType;
        this.fragmentActivity = fragmentActivity;
        this.typeCheckBox = typeCheckBox;
    }

    @NonNull
    @Override
    public FileRecyclerViewAdapter.MyRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.recycler_view_file_layout, parent, false);
        if (fragmentActivity instanceof CommunicationInterface) {
            communicationInterfaceReference = new CommunicationInterfaceReference ((CommunicationInterface) fragmentActivity);
        }
        switch (fileType) {
            case "image":
                imageList.addAll((List<Image>) dynamicList);
                break;
            case "video":
                videoList.addAll((List<Video>) dynamicList);
                break;
            case "audio":
                audioList.addAll((List<Audio>) dynamicList);
                break;
            case "contact":
                contactList.addAll((List<Contact>) dynamicList);
                break;
        }
        return new MyRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FileRecyclerViewAdapter.MyRecyclerViewHolder holder, int position) {
        switch (fileType) {
            case "image":
                Image image = imageList.get(position);
                holder.bind(image);
                if (image.isSelected()) {
                    holder.checkBox.setChecked(true);
                } else {
                    holder.checkBox.setChecked(false);
                }
                break;
            case "video":
                Video video = videoList.get(position);
                holder.bind(video);
                if (video.isSelected()) {
                    holder.checkBox.setChecked(true);
                } else {
                    holder.checkBox.setChecked(false);
                }
                break;
            case "audio":
                Audio audio = audioList.get(position);
                holder.bind(audio);
                if (audio.isSelected()) {
                    holder.checkBox.setChecked(true);
                } else {
                    holder.checkBox.setChecked(false);
                }
                break;
            case "contact":
                Contact contact = contactList.get(position);
                holder.bind(contact);
                if (contact.isSelected()) {
                    holder.checkBox.setChecked(true);
                } else {
                    holder.checkBox.setChecked(false);
                }
                break;
        }

////        setting drawable image for audio files
////        because they don't have bitmap thumbnails
        if (fileType.equals("audio")) {
            holder.roundedImageView.setImageResource(R.drawable.ic_baseline_audiotrack_24);
        } else if (fileType.equals("contact")) {
            holder.roundedImageView.setImageResource(R.drawable.ic_baseline_person_24);
        } else {
            Bitmap thumbnail = thumbnails.get(position);
            holder.roundedImageView.setImageBitmap(thumbnail);
        }

////        android intent to open image on click
//        holder.roundedImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_VIEW);
//                if ("audio".equals(fileType)) {
//                    intent.setDataAndType(image.getUri(), "audio/*");
//                } else {
//                    intent.setDataAndType(image.getUri(), "image/*");
//                }
//                context.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount()  {
        return dynamicList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class MyRecyclerViewHolder extends RecyclerView.ViewHolder {

        RoundedImageView roundedImageView;
        CheckBox checkBox;


        public MyRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            roundedImageView = itemView.findViewById(R.id.imageView);
            checkBox = itemView.findViewById(R.id.checkBox);

        }

        void bind (Object object) {
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checkBox.isChecked()) {
                        switch (fileType) {
                            case "image":
                                Image image = (Image) object;
                                image.setSelected(true);
                                communicationInterfaceReference.invokeSingleSelection(image, "image", true);
                                communicationInterfaceReference.invokeCheckSelection(typeCheckBox, fileType);
                                break;
                            case "video":
                                Video video = (Video) object;
                                video.setSelected(true);
                                communicationInterfaceReference.invokeSingleSelection(video, "video", true);
                                communicationInterfaceReference.invokeCheckSelection(typeCheckBox, fileType);
                                break;
                            case "audio":
                                Audio audio = (Audio) object;
                                audio.setSelected(true);
                                communicationInterfaceReference.invokeSingleSelection(audio, "audio", true);
                                communicationInterfaceReference.invokeCheckSelection(typeCheckBox, fileType);
                                break;
                            case "contact":
                                Contact contact = (Contact) object;
                                contact.setSelected(true);
                                communicationInterfaceReference.invokeSingleSelection(contact, "contact", true);
                                communicationInterfaceReference.invokeCheckSelection(typeCheckBox, fileType);
                                break;
                        }
                    } else {
                        switch (fileType) {
                            case "image":
                                Image image = (Image) object;
                                image.setSelected(false);
                                communicationInterfaceReference.invokeSingleSelection(image, "image", false);
                                communicationInterfaceReference.invokeCheckSelection(typeCheckBox, fileType);
                                break;
                            case "video":
                                Video video = (Video) object;
                                video.setSelected(false);
                                communicationInterfaceReference.invokeSingleSelection(video, "video", false);
                                communicationInterfaceReference.invokeCheckSelection(typeCheckBox, fileType);
                                break;
                            case "audio":
                                Audio audio = (Audio) object;
                                audio.setSelected(false);
                                communicationInterfaceReference.invokeSingleSelection(audio, "audio", false);
                                communicationInterfaceReference.invokeCheckSelection(typeCheckBox, fileType);
                                break;
                            case "contact":
                                Contact contact = (Contact) object;
                                contact.setSelected(false);
                                communicationInterfaceReference.invokeSingleSelection(contact, "contact", false);
                                communicationInterfaceReference.invokeCheckSelection(typeCheckBox, fileType);
                                break;
                        }
                    }
                }
            });
        }
    }
}

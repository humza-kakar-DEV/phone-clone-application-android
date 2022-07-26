package com.example.wifip2p.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wifip2p.Media.Apk;
import com.example.wifip2p.Media.Audio;
import com.example.wifip2p.Media.Contact;
import com.example.wifip2p.Media.Document;
import com.example.wifip2p.Media.Image;
import com.example.wifip2p.Media.Video;
import com.example.wifip2p.R;
import com.example.wifip2p.Utils.CommunicationInterface;
import com.example.wifip2p.Utils.CommunicationInterfaceReference;
import com.makeramen.roundedimageview.RoundedImageView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class FileRecyclerViewAdapter extends RecyclerView.Adapter<FileRecyclerViewAdapter.MyRecyclerViewHolder> {

    public static final String TAG = "myR";
    List<Image> imageList = new ArrayList<>();
    List<Video> videoList = new ArrayList<>();
    List<Audio> audioList = new ArrayList<>();
    List<Contact> contactList = new ArrayList<>();
    List<Document> documentList = new ArrayList<>();
    List<Apk> apkList = new ArrayList<>();

    private List<?> dynamicList;
    private String fileType;
    private FragmentActivity fragmentActivity;
    private Context context;
    CheckBox typeCheckBox;
    CommunicationInterfaceReference communicationInterfaceReference;

    public FileRecyclerViewAdapter(List<?> list, String fileType, CheckBox typeCheckBox, Context context, FragmentActivity fragmentActivity) {
        this.context = context;
        this.dynamicList = list;
        this.fileType = fileType;
        this.fragmentActivity = fragmentActivity;
        this.typeCheckBox = typeCheckBox;
        if (fragmentActivity instanceof CommunicationInterface) {
            communicationInterfaceReference = new CommunicationInterfaceReference(fragmentActivity);
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
            case "document":
                documentList.addAll((List<Document>) dynamicList);
                break;
            case "apk":
                apkList.addAll((List<Apk>) dynamicList);
                break;
        }
    }

    @NonNull
    @Override
    public FileRecyclerViewAdapter.MyRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.recycler_view_file_layout_v2, parent, false);
        return new MyRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FileRecyclerViewAdapter.MyRecyclerViewHolder holder, int position) {

        switch (fileType) {
            case "image":
                Image image = imageList.get(position);
                Glide.with(context)
                        .load(image.getUri())
                        .centerCrop()
                        .into(holder.imageView);
                holder.textView.setText(image.getName());
                holder.bind(image);
                if (image.isSelected()) {
                    holder.checkBox.setChecked(true);
                } else {
                    holder.checkBox.setChecked(false);
                }
                break;
            case "video":
                Video video = videoList.get(position);
                Glide.with(context)
                        .load(video.getUri())
                        .centerCrop()
                        .into(holder.imageView);
                holder.textView.setText(video.getName());
                holder.bind(video);
                if (video.isSelected()) {
                    holder.checkBox.setChecked(true);
                } else {
                    holder.checkBox.setChecked(false);
                }
                break;
            case "audio":
                Glide.with(context)
                        .load(R.drawable.ic_baseline_audiotrack_24)
                        .centerCrop()
                        .into(holder.imageView);
                Audio audio = audioList.get(position);
                holder.textView.setText(audio.getSongName());
                holder.bind(audio);
                if (audio.isSelected()) {
                    holder.checkBox.setChecked(true);
                } else {
                    holder.checkBox.setChecked(false);
                }
                break;
            case "contact":
                Glide.with(context)
                        .load(R.drawable.ic_baseline_person_24)
                        .centerCrop()
                        .into(holder.imageView);
                Contact contact = contactList.get(position);
                holder.textView.setText(contact.getPhoneNo());
                holder.bind(contact);
                if (contact.isSelected()) {
                    holder.checkBox.setChecked(true);
                } else {
                    holder.checkBox.setChecked(false);
                }
                break;
            case "document":
                Glide.with(context)
                        .load(R.drawable.ic_baseline_article_24)
                        .centerCrop()
                        .into(holder.imageView);
                Document document = documentList.get(position);
                holder.textView.setText(document.getName());
                holder.bind(document);
                if (document.isSelected()) {
                    holder.checkBox.setChecked(true);
                } else {
                    holder.checkBox.setChecked(false);
                }
                break;
            case "apk":
                Apk apk = apkList.get(position);
                Glide.with(context)
                        .load(apk.getAppIcon())
                        .centerCrop()
                        .into(holder.imageView);
                holder.textView.setText(apk.getAppPackageName());
                holder.bind(apk);
                if (apk.isSelected()) {
                    holder.checkBox.setChecked(true);
                } else {
                    holder.checkBox.setChecked(false);
                }
                break;
        }

    }

    @Override
    public int getItemCount() {
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

        RoundedImageView imageView;
        CheckBox checkBox;
        TextView textView;

        public MyRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.roundedImageView);
            checkBox = itemView.findViewById(R.id.checkBox);
            textView = itemView.findViewById(R.id.textView);

        }

        void bind(Object object) {
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
                            case "document":
                                Document document = (Document) object;
                                document.setSelected(true);
                                communicationInterfaceReference.invokeSingleSelection(document, "document", true);
                                communicationInterfaceReference.invokeCheckSelection(typeCheckBox, fileType);
                                break;
                            case "apk":
                                Apk apk = (Apk) object;
                                apk.setSelected(true);
                                communicationInterfaceReference.invokeSingleSelection(apk, "apk", true);
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
                            case "document":
                                Document document = (Document) object;
                                document.setSelected(false);
                                communicationInterfaceReference.invokeSingleSelection(document, "document", false);
                                communicationInterfaceReference.invokeCheckSelection(typeCheckBox, fileType);
                                break;
                            case "apk":
                                Apk apk = (Apk) object;
                                apk.setSelected(false);
                                communicationInterfaceReference.invokeSingleSelection(apk, "apk", false);
                                communicationInterfaceReference.invokeCheckSelection(typeCheckBox, fileType);
                                break;
                        }
                    }
                }
            });
        }
    }
}

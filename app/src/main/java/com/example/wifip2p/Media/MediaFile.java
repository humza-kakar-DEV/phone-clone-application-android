package com.example.wifip2p.Media;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.util.Size;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MediaFile {

    Context context;

    public MediaFile (Context context) {
        this.context = context;
    }

    public List<Uri> getAudios() {
        List<Uri> uriList = new ArrayList<>();
        AudioMedia audioMedia = new AudioMedia(context);
        for (Audio audio : audioMedia.generateAudios()) {
            uriList.add(Uri.parse(audio.getUri()));
        }
        return uriList;
    }

    public List<Uri> getVideos () {
        List<Uri> uriList = new ArrayList<>();
        VideoMedia videoMedia = new VideoMedia(context);
        for (Video video : videoMedia.generateVideos()) {
//            uriList.add(video.getUri());
        }
        return uriList;
    }

    public List<Uri> getImages () {
        List<Uri> uriList = new ArrayList<>();
        ImageMedia imageMedia = new ImageMedia(context);
        for (Image image : imageMedia.generateImages()) {
//            uriList.add(image.getUri());
        }
        return uriList;
    }
}

package com.example.wifip2p.Utils;

public class FileSizeCalculator {

    public static String getSize (long size) {

        String s = "";
        double kb = size / 1024;
        double mb = kb / 1024;
        double gb = mb / 1024;
        double tb = gb / 1024;

        if(size < 1024L) {
            s = size + " Bytes";
        } else if(size >= 1024 && size < (1024L * 1024)) {
            s =  String.format("%.2f", kb) + " KB";
        } else if(size >= (1024L * 1024) && size < (1024L * 1024 * 1024)) {
            s = String.format("%.2f", mb) + " MB";
        } else if(size >= (1024L * 1024 * 1024) && size < (1024L * 1024 * 1024 * 1024)) {
            s = String.format("%.2f", gb) + " GB";
        } else if(size >= (1024L * 1024 * 1024 * 1024)) {
            s = String.format("%.2f", tb) + " TB";
        }

        return s;

    }


}

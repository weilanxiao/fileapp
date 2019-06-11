package com.app808.fileapp.utils;

public class SuffixUtils {

    public static boolean isMusic(String name){
        return name.endsWith(".mp3") || name.endsWith(".wav") ||name.endsWith(".aac");
    }

    public static boolean isVideo(String name){
        return name.endsWith(".avi") || name.endsWith(".mov") ||name.endsWith(".rmvb")
                || name.endsWith(".rm") || name.endsWith(".flv") || name.endsWith(".mp4") || name.endsWith(".3gp");
    }

    public static boolean isPhoto(String name){
        return name.endsWith(".bmp") || name.endsWith(".gif") ||name.endsWith(".jpeg") ||name.endsWith(".jpg") || name.endsWith(".svg")
                || name.endsWith(".tiff") || name.endsWith(".psd") || name.endsWith(".png") || name.endsWith(".swf");
    }

    public static boolean isTxt(String name){
        return name.endsWith(".txt") || name.endsWith(".ini") ||name.endsWith(".xml");
    }

    public static boolean isInsatll(String name){
        return name.endsWith(".apk");
    }

    public static boolean isArchive(String name){
        return name.endsWith(".rar") || name.endsWith(".zip") || name.endsWith(".7z")
                || name.endsWith(".tar") || name.endsWith(".tar.gz");
    }
}

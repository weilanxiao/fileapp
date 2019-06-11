package com.app808.fileapp.service;

import android.content.Context;
import android.widget.Toast;

import com.app808.fileapp.utils.OpenIntent;
import com.app808.fileapp.utils.SuffixUtils;

public class OpenFileService {

    public static void openFile(Context context, String filename){
        if (SuffixUtils.isMusic(filename)) {
            context.startActivity(OpenIntent.getAudioFileIntent(context, filename));
        } else if (SuffixUtils.isVideo(filename)) {
            context.startActivity(OpenIntent.getVideoFileIntent(context, filename));
        } else if (SuffixUtils.isPhoto(filename)) {
            context.startActivity(OpenIntent.getImageFileIntent(context, filename));
        } else if (SuffixUtils.isInsatll(filename)) {
            // OpenIntent.get(filename);
            Toast.makeText(context,"在不支持该类型文件",Toast.LENGTH_SHORT).show();
        } else if (SuffixUtils.isArchive(filename)) {
            // OpenIntent.get(filename);
            Toast.makeText(context,"在不支持该类型文件",Toast.LENGTH_SHORT).show();
        } else {
            context.startActivity(OpenIntent.getTextFileIntent(filename,true));
        }
    }
}

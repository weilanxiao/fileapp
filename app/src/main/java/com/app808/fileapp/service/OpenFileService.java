package com.app808.fileapp.service;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.app808.fileapp.entity.ConstVaule;
import com.app808.fileapp.utils.OpenIntent;
import com.app808.fileapp.utils.SuffixUtils;

public class OpenFileService {

    private static void open(Context context, String filename, Handler handler){
//
//        new Thread(){
//            @Override
//            public void run()
//            {
//                // 1、实例化一个Message对象
//                Message message = Message.obtain();
//                message.obj = open(context, filename);
//                // 定义标签
//                message.what = 0;
//                handler.sendMessage(message);
//            }
//        }.start();
        open(context, filename);
    }

    private static boolean open(Context context, String filename){
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
        return true;
    }

    public static void openFile(Context context, String filename){
//        Handler mHandler = new Handler()
//        {
//            @Override
//            public void handleMessage(Message msg)
//            {
//                // TODO Auto-generated method stub
//                super.handleMessage(msg);
//                // 4、接收消息并执行UI的更新操作
//                if (msg.obj != null)
//                {
//                    Log.i("打开外部应用", "...");
//                } else
//                {
//                    Log.i("未能打开外部应用", "...");
//
//                }
//            }
//
//        };
        open(context, filename);
    }
}

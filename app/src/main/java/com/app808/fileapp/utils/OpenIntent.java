package com.app808.fileapp.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.app808.fileapp.entity.ConstVaule;

import java.io.File;

public class OpenIntent {

    //android获取一个用于打开HTML文件的intent
    public static Intent getHtmlFileIntent(Context context, String param ) {
        Uri uri = Uri.parse(param ).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(param ).build();
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(uri, "text/html");
        Log.i("uri:",uri.toString());
        return intent;
    }
    //android获取一个用于打开图片文件的intent
    public static Intent getImageFileIntent(Context context, String param ) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Uri uri = FileProvider.getUriForFile(context,context.getApplicationContext().getPackageName() + ".provider", new File(param));
        intent.setDataAndType(uri, "image/*");
        Log.i("uri:",uri.toString());
        return intent;
    }
    //android获取一个用于打开PDF文件的intent
    public static Intent getPdfFileIntent( String param ) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Uri uri = Uri.fromFile(new File(param ));
        intent.setDataAndType(uri, "application/pdf");
        Log.i("uri:",uri.toString());
        return intent;
    }
    //android获取一个用于打开文本文件的intent
    public static Intent getTextFileIntent( String param, boolean paramBoolean) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (paramBoolean) {
            Uri uri1 = Uri.parse(param );
            intent.setDataAndType(uri1, "text/plain");
            Log.i("uri:",uri1.toString());
        } else {
            Uri uri2 = Uri.fromFile(new File(param ));
            intent.setDataAndType(uri2, "text/plain");
            Log.i("uri:",uri2.toString());
        }
        return intent;
    }
    //android获取一个用于打开音频文件的intent
    public static Intent getAudioFileIntent(Context context, String param ) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        // Uri uri = Uri.fromFile(new File(param ));
        Uri uri = FileProvider.getUriForFile(context,
                context.getApplicationContext().getPackageName() + ".provider", new File(param));
        intent.setDataAndType(uri, "audio/*");
        Log.i("uri:",uri.toString());
        return intent;
    }
    //android获取一个用于打开视频文件的intent
    public static Intent getVideoFileIntent(Context context, String param ) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        // Uri uri = Uri.fromFile(new File(param ));
        Uri uri = FileProvider.getUriForFile(context,
                context.getApplicationContext().getPackageName() + ".provider", new File(param));
        intent.setDataAndType(uri, "video/*");
        Log.i("uri:",uri.toString());
        return intent;
    }

    //android获取一个用于打开CHM文件的intent
    public static Intent getChmFileIntent(Context context, String param ) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        // Uri uri = Uri.fromFile(new File(param ));
        Uri uri = FileProvider.getUriForFile(context,
                context.getApplicationContext().getPackageName() + ".provider", new File(param));
        intent.setDataAndType(uri, "application/x-chm");
        return intent;
    }

    //android获取一个用于打开Word文件的intent
    public static Intent getWordFileIntent(Context context, String param ) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        // Uri uri = Uri.fromFile(new File(param ));
        Uri uri = FileProvider.getUriForFile(context,
                context.getApplicationContext().getPackageName() + ".provider", new File(param));
        intent.setDataAndType(uri, "application/msword");
        return intent;
    }
    //android获取一个用于打开Excel文件的intent
    public static Intent getExcelFileIntent(Context context, String param ) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        // Uri uri = Uri.fromFile(new File(param ));
        Uri uri = FileProvider.getUriForFile(context,
                context.getApplicationContext().getPackageName() + ".provider", new File(param));
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        return intent;
    }
    //android获取一个用于打开PPT文件的intent
    public static Intent getPptFileIntent(Context context, String param ) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        // Uri uri = Uri.fromFile(new File(param));
        Uri uri = FileProvider.getUriForFile(context,
                context.getApplicationContext().getPackageName() + ".provider", new File(param));
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        return intent;
    }
}

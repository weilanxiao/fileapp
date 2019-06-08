package com.app808.fileapp.utils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.app808.fileapp.entity.FileBean;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.List;

import static com.app808.fileapp.utils.FileUtils.convertToDateTime;

public class FileFilterUtils {

    private static final String TAG = "FileFilterUtils";

    public static List<FilenameFilter> musicFilter;
    public static List<FilenameFilter> videoFilter;
    public static List<FilenameFilter> photoFilter;
    public static List<FilenameFilter> txtFilter;
    public static List<FilenameFilter> installFilter;
    public static List<FilenameFilter> archiveFilter;

    static class MusicFilter implements FilenameFilter{
        @Override
        public boolean accept(File dir, String name) {
            return name.endsWith(".mp3") || name.endsWith(".wav") ||name.endsWith(".aac");
        }
    }

    static class VideoFilter implements FilenameFilter{
        @Override
        public boolean accept(File dir, String name) {
            return name.endsWith(".avi") || name.endsWith(".mov") ||name.endsWith(".rmvb")
                    || name.endsWith(".rm") || name.endsWith(".flv") || name.endsWith(".mp4") || name.endsWith(".3gp");
        }
    }

    static class PhotoFilter implements FilenameFilter{

        @Override
        public boolean accept(File dir, String name) {
            return name.endsWith(".bmp") || name.endsWith(".gif") ||name.endsWith(".jpeg") || name.endsWith(".svg")
                    || name.endsWith(".tiff") || name.endsWith(".psd") || name.endsWith(".png") || name.endsWith(".swf");
        }
    }

    static class TxtFilter implements FilenameFilter{
        @Override
        public boolean accept(File dir, String name) {
            return name.endsWith(".txt") || name.endsWith(".ini") ||name.endsWith(".xml");
        }
    }

    static class InstallFilter implements FilenameFilter{
        @Override
        public boolean accept(File dir, String name) {
            return name.endsWith(".apk");
        }
    }

    static class ArchiveFilter implements FilenameFilter{
        @Override
        public boolean accept(File dir, String name) {
            return name.endsWith(".rar") || name.endsWith(".zip") || name.endsWith(".7z")
                    || name.endsWith(".tar") || name.endsWith(".tar.gz");
        }
    }

    static {
        musicFilter = new ArrayList<>();
        videoFilter = new ArrayList<>();
        photoFilter = new ArrayList<>();
        txtFilter = new ArrayList<>();
        installFilter = new ArrayList<>();
        archiveFilter = new ArrayList<>();

        musicFilter.add(new MusicFilter());
        videoFilter.add(new VideoFilter());
        photoFilter.add(new PhotoFilter());
        txtFilter.add(new TxtFilter());
        installFilter.add(new InstallFilter());
        archiveFilter.add(new ArchiveFilter());
    }

    public static List<FileBean> getASYNC(String rootpath, List<FilenameFilter> filters){
        //取得需要遍历的文件目录
        List<FileBean> list = new ArrayList();
        File home = new File(rootpath);
        //遍历文件目录,添加
        for(FilenameFilter filter: filters){
            if (home.listFiles(filter).length > 0) {
                for (File file : home.listFiles(filter)) {
                    Log.i(TAG,file.getName());
                    list.add(new FileBean(file.getName(), file.getParent(), file.length(), convertToDateTime(file.lastModified()), false));
                }
            }
        }
        for(File file:home.listFiles()){
            if(file.isDirectory()){
                list.addAll(getASYNC(file.getPath(), filters));
            }
        }
        return list;
    }

    public static void getCategoryFile(String rootpath, List<FilenameFilter> filters, Handler handler){
        try {
            new Thread(){
                @Override
                public void run()
                {
                    Log.i(TAG,"start thread to get category file...");
                    // 1、实例化一个Message对象
                    Message message = Message.obtain();
                    // 将图片流赋值给Message对象
                    message.obj = getASYNC(rootpath, filters);
                    // 定义标签
                    message.what = 0;
                    handler.sendMessage(message);
                }
            }.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

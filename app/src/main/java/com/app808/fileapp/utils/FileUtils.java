package com.app808.fileapp.utils;

import android.util.Log;

import com.app808.fileapp.entity.FileBean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileUtils {

    // 读取文件
    public static List<FileBean> getFile(String path){
        File rootFiles = new File(path);
        File[] files = rootFiles.listFiles();
        List<FileBean> fileBeans = new ArrayList<>(files.length);
        for (File file: files) {
            if(file.isDirectory()){
                FileBean dir = new FileBean(file.getName(), file.getPath(), null, convertToDateTime(file.lastModified()), true);
                fileBeans.add(dir);
            }else {
                FileBean fileBean = new FileBean(file.getName(), file.getParent(), file.length(), convertToDateTime(file.lastModified()), false);
                fileBeans.add(fileBean);
            }
        }
        return fileBeans;
    }

    private static List<File> getFile(File file){
        List<File> list = new ArrayList<>();
        list.add(file);
        File[] fileArray = file.listFiles();
        if (fileArray == null) {
            return list;
        } else {
            for (File f : fileArray) {
                if (f.isFile()) {
                    list.add(0, f);
                } else {
                    getFile(f);
                }
            }
        }
        return list;
    }
    private static LocalDateTime convertToDateTime(Long longTime){
        Instant instant = Instant.ofEpochMilli(longTime);
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    // 创建文件
    public static boolean createFile(String filePath, String fileName){
        String fileStr = filePath + fileName;
        // 打开文件
        File file = new File(filePath);
        if (!file.exists()) {
            /**  注意这里是 mkdirs()方法  可以创建多个文件夹 */
            file.mkdirs();
        }
        File subfile = new File(fileStr);
        if (!subfile.exists()) {
            try {
                boolean b = subfile.createNewFile();
                return b;
            } catch (IOException e) {
                Log.i("创建文件","失败");
                e.printStackTrace();
            }
        } else {
            return true;
        }
        return false;
    }

    // 删除文件
    private static boolean deleteFiles(String filePath) {
        List<File> files = getFile(new File(filePath));
        if (files.size() != 0) {
            for (int i = 0; i < files.size(); i++) {
                File file = files.get(i);
                /**  如果是文件则删除  如果都删除可不必判断  */
                file.delete();
//                if (file.isFile()) {
//                    file.delete();
//                }
            }
        }
        return true;
    }

    // 重命名文件
    public static void renameFile(String oldPath, String newPath) {
        File oleFile = new File(oldPath);
        File newFile = new File(newPath);
        //执行重命名
        oleFile.renameTo(newFile);
    }

    // 文件拷贝
    // 要复制的目录下的所有非子目录(文件夹)文件拷贝
    private static boolean CopySdcardFile(File fromFile, File toFile) {
        try {
            InputStream fosfrom = new FileInputStream(fromFile);
            OutputStream fosto = new FileOutputStream(toFile);
            byte bt[] = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0) {
                fosto.write(bt, 0, c);
            }
            fosfrom.close();
            fosto.close();
            return true;
        } catch (Exception ex) {
            Log.i("复制文件出错",ex.toString());
            return false;
        }
    }

    // 复制文件夹
    private static void copyDirectiory(String sourceDir, String targetDir)
            throws IOException {
        // 新建目标目录
        (new File(targetDir)).mkdirs();
        // 获取源文件夹当前下的文件或目录
        File[] file = (new File(sourceDir)).listFiles();
        for (int i = 0; i < file.length; i++) {
            if (file[i].isFile()) {
                // 源文件
                File sourceFile=file[i];
                // 目标文件
                File targetFile=new
                        File(new File(targetDir).getAbsolutePath()
                        +File.separator+file[i].getName());
                CopySdcardFile(sourceFile,targetFile);
            }
            if (file[i].isDirectory()) {
                // 准备复制的源文件夹
                String dir1=sourceDir + "/" + file[i].getName();
                // 准备复制的目标文件夹
                String dir2=targetDir + "/"+ file[i].getName();
                copyDirectiory(dir1, dir2);
            }
        }
    }

    // 复制
    public static void copy(FileBean srcFile, String resFile) {
        try{
            if(srcFile.getDir()){
                Log.i("复制文件夹",srcFile.getPath());
                copyDirectiory(srcFile.getPath(),resFile+'/'+srcFile.getName());
            }else{
                Log.i("复制文件",srcFile.getPath()+'/'+srcFile.getName());
                CopySdcardFile(new File(srcFile.getPath()+'/'+srcFile.getName()), new File(resFile+'/'+srcFile.getName()));
            }
            Log.i("文件操作","复制成功");
        }catch (Exception e){
            throw new RuntimeException("复制文件操作失败...");
        }

    }

    public static void move(FileBean srcFile, String resFile){
        try{
            if(srcFile.getDir()){
                Log.i("移动文件夹",srcFile.getPath());
                copyDirectiory(srcFile.getPath(),resFile+'/'+srcFile.getName());
                deleteFiles(srcFile.getPath());
            }else{
                Log.i("移动文件",srcFile.getPath()+'/'+srcFile.getName());
                CopySdcardFile(new File(srcFile.getPath()+'/'+srcFile.getName()), new File(resFile+'/'+srcFile.getName()));
                deleteFiles(srcFile.getPath()+'/'+srcFile.getName());
            }
            Log.i("文件操作","移动成功");
        }catch (Exception e){
            throw new RuntimeException("移动文件操作失败...");
        }

    }

    public static void delete(FileBean srcFile){
        try{
            if(srcFile.getDir()){
                Log.i("删除文件夹",srcFile.getPath());
                deleteFiles(srcFile.getPath());
            }else{
                Log.i("删除文件",srcFile.getPath()+'/'+srcFile.getName());
                deleteFiles(srcFile.getPath()+'/'+srcFile.getName());
            }
            Log.i("文件操作","删除成功");
        }catch (Exception e){
            throw new RuntimeException("删除文件操作失败...");
        }
    }
}

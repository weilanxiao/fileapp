package com.app808.fileapp.service;

import android.nfc.cardemulation.HostNfcFService;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.app808.fileapp.entity.ConstVaule;
import com.app808.fileapp.entity.FileBean;
import com.app808.fileapp.utils.FileSyncUtils;
import com.app808.fileapp.utils.JsonToBean;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.List;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;

import static android.os.Build.HOST;
import static com.app808.fileapp.utils.FileUtils.convertToDateTime;
import static jcifs.smb.NtStatus.NT_STATUS_OBJECT_PATH_NOT_FOUND;

public class SyncService {

    private static final String SMB_ROOT = "smb://" + ConstVaule.SERVER_IP + "/" + "app/";

    private void dir(String path) throws FileNotFoundException {

    }
    private void file(String path) {

    }

    private static List<FileBean> load(SmbFile remoteSmbFile) throws SmbException {
        SmbFile[] files = remoteSmbFile.listFiles();
        List<FileBean> fileBeans = new ArrayList<>(files.length);
        for (SmbFile file: files) {
            FileBean fileBean;
            String name = file.getName();
            String path = file.getPath();
            Log.i("name ",name);
            Log.i( "path ",path);
            if(file.isDirectory()){
                fileBean = new FileBean(name.replace("/",""),
                        path.substring(0,path.length()-1).replace(SMB_ROOT,"/"), null, convertToDateTime(file.lastModified()), true);
                fileBeans.add(fileBean);
            }else {
                fileBean = new FileBean(name, path.replace(SMB_ROOT,"/"), file.length(), convertToDateTime(file.lastModified()), false);
                fileBeans.add(fileBean);
            }
            Log.i("sync name:", fileBean.getName());
            Log.i("sync path:", fileBean.getPath());
        }
        return fileBeans;
    }

    private static boolean upload(String filepath){
        InputStream in = null;
        OutputStream out = null;
        try {
            NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(ConstVaule.SERVER_IP, ConstVaule.SERVER_NAME, ConstVaule.SERVER_PASSWORD);
            String url =SMB_ROOT + filepath.replace(ConstVaule.ROOT_PATH,"");
            SmbFile remoteSmbFile = new SmbFile(url,auth);
            Log.i("url: ",remoteSmbFile.getPath());
            String parent = remoteSmbFile.getParent();
            SmbFile parentSmb = new SmbFile(parent,auth);
            Log.i("file path:",parent);
            if(!parentSmb.exists()){
                parentSmb.mkdirs();
            }
            Log.i("file path:",filepath);
            File localFile = new File(filepath);
            //打开一个文件输入流执行本地文件，要从它读取内容
            in = new BufferedInputStream(new FileInputStream(localFile));
            //打开一个远程Samba文件输出流，作为复制到的目的地
            out = new BufferedOutputStream(new SmbFileOutputStream(remoteSmbFile));
            //缓冲内存
            byte [] buffer =  new   byte [1024];
            while  (in.read(buffer) != - 1 ) {
                out.write(buffer);
                buffer = new byte[1024];
            }
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        } finally{
            if(null != in){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(null != out){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static boolean copyFile(SmbFile smbFile, String path){
        SmbFileInputStream in = null;
        FileOutputStream out = null;
        Log.i("copy path ",path);
        try {
            File file = new File(path);
            if(!file.exists()){
                // 不存在
            }
            if(file.isFile()){
                // 是文件，下载
            }else{
                //
                File[] localFiles = file.listFiles();
                if (null == localFiles) {
                    // 目录不存在的话,就创建目录
                    new File(file.getParent()).mkdirs();
                    Log.i("copy ", path+"目录创建成功！");
                }
            }
            in = new SmbFileInputStream(smbFile);
            out = new FileOutputStream(path);
            byte[] buffer = new byte[4096];
            int len = -1;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
                buffer = new byte[4096];
            }
            out.flush();
            Log.i("copy ", "拷贝 "+smbFile.getName()+" 成功");
        } catch (Exception e) {
            Log.i("copy ","拷贝远程文件到本地目录失败", e);
            return false;
        } finally {
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    private static boolean getRemoteFile(String path){
        NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(ConstVaule.SERVER_IP, ConstVaule.SERVER_NAME, ConstVaule.SERVER_PASSWORD);
        String url = SMB_ROOT + path.substring(1);
        try {
            SmbFile remoteSmbFile = new SmbFile(url,auth);
            if (remoteSmbFile != null) {
                if (remoteSmbFile.isDirectory()) {
                    SmbFile[] smbFiles = remoteSmbFile.listFiles();
                    for (SmbFile file : smbFiles) {
                        File file1 = new File(ConstVaule.ROOT_PATH+"/"+file.getName());
                        if (remoteSmbFile.isDirectory()) {
                            Log.i("remote path:",file.getPath()+file.getName());
                            getRemoteFile(file.getName());
                            Log.i("local path:", file.getPath()+file.getName());
                            if (!file1.exists()) {
                                // 本地不存在服务器文件，则下载
                                return copyFile(file, file1.getPath());
                            }else {
                                Log.i("local file ","已存在 "+file1);
                                String smbUrl =  url + file.getName();
                                Log.i("smburl: ",smbUrl);
//                                SmbFile smbFile1 = new SmbFile(smbUrl,auth);
//                                // smbFile1.delete();
//                                Log.i("local file ","删除："+smbUrl.replace("\\\\","")+" 成功");
                            }
                        }else {
                            return copyFile(file, file1.getPath());
                        }
                    }
                } else if (remoteSmbFile.isFile()) {
                    return copyFile(remoteSmbFile, ConstVaule.ROOT_PATH + path);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (SmbException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void getSync(String path, Handler handler){
        NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(ConstVaule.SERVER_IP,
                ConstVaule.SERVER_NAME, ConstVaule.SERVER_PASSWORD);
        String url ="smb://" + ConstVaule.SERVER_IP + "/" + "app"+path;
        new Thread(){
            @Override
            public void run()
            {
                SmbFile remoteSmbFile = null;
                try {
                    remoteSmbFile = new SmbFile(url,auth);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                // 1、实例化一个Message对象
                Message message = Message.obtain();
                // 将图片流赋值给Message对象
                try {
                    message.obj = load(remoteSmbFile);
                } catch (SmbException e) {
                    e.printStackTrace();
                }
                // 定义标签
                message.what = 0;
                handler.sendMessage(message);
            }
        }.start();
    }

    public static void upload(String filepath, Handler handler){
        try {
            new Thread(){
                @Override
                public void run()
                {
                    // 1、实例化一个Message对象
                    Message message = Message.obtain();
                    // 将图片流赋值给Message对象
                    message.obj = upload(filepath);
                    // 定义标签
                    message.what = 0;
                    handler.sendMessage(message);
                }
            }.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void download(String path, Handler handler){
        try {
            new Thread(){
                @Override
                public void run()
                {
                    // 1、实例化一个Message对象
                    Message message = Message.obtain();
                    // 将图片流赋值给Message对象
                    message.obj = getRemoteFile(path);
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

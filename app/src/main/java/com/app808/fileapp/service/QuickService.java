package com.app808.fileapp.service;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

import com.app808.fileapp.entity.ConstVaule;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;


public class QuickService {
    private static final String path = "config.properties";
    private Properties prop;
    private Activity mActivity;
    private List<String> datas;
    AssetManager assetManager;


    public QuickService(Activity activity){
        prop = new Properties();
        mActivity = activity;
        Log.i("...","...");
        loadQuick();
        datas = new ArrayList<>();
    }

    private void loadQuick(){
        try {
            File s = mActivity.getFilesDir();
            Log.i("...",Environment.getExternalStorageDirectory().getPath()+File.separator);
            File file = new File(s,path);
            if(!file.exists()){
                file.createNewFile();
                FileInputStream fis = new FileInputStream(file);  //打开assets目录下的config.properties文件
                prop.load(fis);
                setValue("quickpath", ConstVaule.ROOT_PATH+",");
            }else{
                FileInputStream fis = new FileInputStream(file);  //打开assets目录下的config.properties文件
                prop.load(fis);
            }
            Log.i("...","...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> getPaths(){
        String[] result = getValue("quickpath").split(",");
        List<String> paths = new ArrayList<String>(result.length);
        Collections.addAll(paths,result);
        datas.clear();
        datas.addAll(paths);
        return paths;
    }

    private String getValue(String key){
        String value  = prop.getProperty(key);
        return value;
    }

    public void setPath(List<String> paths){
        List<Integer> integers = new ArrayList<>(paths.size());
        for(int i=0;i<paths.size();i++){
            for(String s:datas){
                if(paths.get(i).equals(s)){
                    integers.add(i);
                }
            }
        }
        for(int i:integers){
            paths.remove(i);
        }
        datas.addAll(paths);
        StringBuffer sb =new StringBuffer();
        for(String s:datas){
            sb.append(s);
            sb.append(",");
        }
        String res = sb.substring(0,sb.length()-1);
        setValue("quickpath",res);
    }

    private String setValue(String key,String value) {
        try {
            prop.setProperty (key,value);
            File file = new File(mActivity.getFilesDir(),path);
            OutputStream fos = new FileOutputStream(file);
            prop.store(fos, "Update '" + key + "' value");
            fos.flush();
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

package com.app808.fileapp.utils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class FileSyncUtils {

    private static final String TAG = "FileSyncUtils";

    private static String sync(String baseUrl) throws IOException {
        Log.i(TAG,"start sync");
        StringBuilder tempParams = new StringBuilder();
        Map<String,String> paramsMap= new HashMap<>();
        paramsMap.put("key","0");
        paramsMap.put("func","0");
        int pos = 0;
        for (String key : paramsMap.keySet()) {
            if (pos > 0) {
                tempParams.append("&");
            }
            tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key),"utf-8")));
            pos++;
        }
        String requestUrl = baseUrl + tempParams.toString();
        // 新建一个URL对象
        URL url = new URL(requestUrl);
        // 打开一个HttpURLConnection连接
        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
        // 设置连接主机超时时间
        urlConn.setConnectTimeout(10 * 1000);
        //设置从主机读取数据超时
        urlConn.setReadTimeout(10 * 1000);
        // 设置是否使用缓存  默认是true
        urlConn.setUseCaches(true);
        // 设置为Post请求
        urlConn.setRequestMethod("GET");
        //urlConn设置请求头信息
        //设置请求中的媒体类型信息。
        urlConn.setRequestProperty("Content-Type", "application/json");
        //设置客户端与服务连接类型
        urlConn.addRequestProperty("Connection", "Keep-Alive");
        // 开始连接
        urlConn.connect();
        // 判断请求是否成功
        String result="";
        if (urlConn.getResponseCode() == 200) {
            // 获取返回的数据
            result = streamToString(urlConn.getInputStream());
            Log.e(TAG, "Get方式请求成功，result--->" + result);
        } else {
            Log.e(TAG, "Get方式请求失败, code--->" + urlConn.getResponseCode());
        }
        // 关闭连接
        urlConn.disconnect();
        return result;
    }

    public static void getSync(String url, Handler mHandler){
        try {
            new Thread(){
                @Override
                public void run()
                {
                    try{
                        // 1、实例化一个Message对象
                        Message message = Message.obtain();
                        // 将图片流赋值给Message对象
                        message.obj = sync(url);
                        // 定义标签
                        message.what = 0;
                        mHandler.sendMessage(message);
                    } catch (UnsupportedEncodingException e1) {
                        e1.printStackTrace();
                    } catch (ProtocolException e1) {
                        e1.printStackTrace();
                    } catch (MalformedURLException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String streamToString(InputStream is) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            baos.close();
            is.close();
            byte[] byteArray = baos.toByteArray();
            return new String(byteArray);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }
}

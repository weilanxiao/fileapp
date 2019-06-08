package com.app808.fileapp.utils;

import android.util.Log;

import com.app808.fileapp.entity.FileBean;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JsonToBean {

    public static List<FileBean> jsonToFileBean(String json){
        ObjectMapper mapper = new ObjectMapper();
        //解析器支持解析单引号
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        //解析器支持解析结束符
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        List<FileBean> result;
        try {
            //转换为HashMap对象
            Map<String, List<LinkedHashMap>> maps = mapper.readValue(json, Map.class);
            List<LinkedHashMap> list = maps.get("files");
            result=new ArrayList<>(list.size());
            for (LinkedHashMap bean:list){
                Iterator<Map.Entry> iterator= bean.entrySet().iterator();
                String name = (String) iterator.next().getValue();
                Long size = (Long.valueOf((Integer)iterator.next().getValue()));
                Long time = (Long.valueOf((Integer)iterator.next().getValue()));
                Log.i("name",name);
                Log.i("size", String.valueOf(size));
                Log.i("date", String.valueOf(time));
                result.add(
                        new FileBean(name,
                                "/onedrive/"+name,
                                size,
                                FileUtils.convertToDateTime(time),
                                false)
                );
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

package com.app808.fileapp.utils;

import com.app808.fileapp.entity.FileBean;

import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileUtils {

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

    private static LocalDateTime convertToDateTime(Long longTime){
        Instant instant = Instant.ofEpochMilli(longTime);
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }
}

package com.app808.fileapp.entity;

import java.time.LocalDateTime;
import java.util.Date;

public class FileBean {

    private String mName;
    private String mPath;
    private Long mSize;
    private LocalDateTime mLastData;
    private Boolean isDir;

    public FileBean() {
    }

    public FileBean(String name, String path, Long size, LocalDateTime lastData, Boolean isDir) {
        mName = name;
        mPath = path;
        mSize = size;
        mLastData = lastData;
        this.isDir = isDir;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getPath() {
        return mPath;
    }

    public void setPath(String path) {
        mPath = path;
    }

    public Long getSize() {
        return mSize;
    }

    public void setSize(Long size) {
        mSize = size;
    }

    public LocalDateTime getLastData() {
        return mLastData;
    }

    public void setLastData(LocalDateTime lastData) {
        mLastData = lastData;
    }

    public Boolean getDir() {
        return isDir;
    }

    public void setDir(Boolean dir) {
        isDir = dir;
    }
}

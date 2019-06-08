package com.app808.fileapp.dummy;

import com.app808.fileapp.entity.FileBean;
import com.app808.fileapp.utils.FileSyncUtils;
import com.app808.fileapp.utils.JsonToBean;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class CloudDummy {

    /**
     * An array of sample (dummy) items.
     */
    private FileBean data;
    private boolean isChecked;

    public CloudDummy(FileBean data, boolean isChecked){
        this.data = data;
        this.isChecked = isChecked;
    }

    public FileBean getData() {
        return data;
    }

    public void setData(FileBean data) {
        this.data = data;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }


    public static List<CloudDummy> update(List<FileBean> fileBeans) {
        List<CloudDummy> list = new ArrayList<>(fileBeans.size());
        for(FileBean fileBean:fileBeans){
            list.add(new CloudDummy(fileBean,false));
        }
        return list;
    }

}

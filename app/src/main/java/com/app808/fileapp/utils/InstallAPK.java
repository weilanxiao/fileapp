package com.app808.fileapp.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

public class InstallAPK {

    private Activity mActivity;

    public InstallAPK(Activity activity){
        mActivity = activity;
    }

    public boolean checkIsAndroidO() {
        boolean isGranted = true;
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            if (mActivity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //如果没有写sd卡权限
                isGranted = false;
            }
            if (mActivity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                isGranted = false;
            }
            Log.i("cbs","isGranted == "+isGranted);
            if (!isGranted) {
                try {
                    mActivity.requestPermissions(
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 102);
                    if (mActivity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED &&
                            mActivity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                                    != PackageManager.PERMISSION_GRANTED) {
                        //如果没有写sd卡权限
                        return false;
                    }
                } catch (Exception e) {
                    Log.i("权限设置","错误");
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * 申请权限回调
     */
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case INSTALL_PACKAGES_REQUEST_CODE:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    mMainPresenter.installApk();
//                } else {
//                    //  引导用户手动开启安装权限
//                    Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
//                    startActivityForResult(intent, GET_UNKNOWN_APP_SOURCES);
//                }
//                break;
//            default:
//                break;
//
//        }
//    }
}

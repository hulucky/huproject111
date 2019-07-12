package com.example.hu.huproject.Activity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.example.hu.huproject.R;
import com.xzkydz.function.splash.SplashActivity;

//闪屏页
public class MySplashActivity extends SplashActivity {
    @Override
    public Class<?> getNavigationActivity() {
        return MyNavigationActivity.class;
    }

    @Override
    public void setShowContent() {
        super.setShowContent();
        setAppName("单轨吊");
        setVersionName(getVersionName(MySplashActivity.this));
        setBgImag(R.mipmap.hoister);
    }

    //版本名
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }
    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pi;
    }
}

package com.example.hu.huproject.Utils;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.inputmethod.InputMethodManager;

/**
 * Date: 2018/3/28  10:05
 * Description: 常用方法类
 */

public class StartActivity {

    //跳转Activity
    public static void toActivity(@NonNull Activity activity , @NonNull Class<?> targetClass){
        Intent intent = new Intent();
        intent.setClass(activity, targetClass);
        //根据版本设置跳转方式
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
        } else {
            activity.startActivity(intent);
        }
    }


    /**
     * function ：关闭软键盘
     */
    public static void closeKey(Activity context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(context.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

}

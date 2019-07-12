package com.example.hu.huproject.Utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Vampire on 2017/6/9.
 */

public class ScreenShot {
    // 获取指定Activity的截屏，保存到png文件
    private static Bitmap takeScreenShot(Activity activity, int height_toolbar) {
        //View是你需要截图的View
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();
        //获取状态栏高度
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        System.out.println(statusBarHeight);
        //获取屏幕长和高
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay().getHeight();
        //去掉标题栏
        Bitmap bitMap = Bitmap.createBitmap(b1, 0, statusBarHeight + height_toolbar, width, (height - statusBarHeight - height_toolbar) / 2);
        view.destroyDrawingCache();
        return bitMap;
    }

    //保存到sdcard
    private static void savePic(Bitmap b, String strFileName) {
        FileOutputStream fos = null;
        File file = new File(strFileName);
        //判断文件是否存在，存在则删除
        if (file.exists()) {
            file.delete();
        }
        //判断文件是否存在，不存在则创建
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            fos = new FileOutputStream(strFileName);
            b.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //程序入口
    public static void shoot(Activity a, String name, int height) {
        File sdcardDir = Environment.getExternalStorageDirectory();
//        String path = sdcardDir.getPath() + "/提升机综合测试仪/测试报告/";
        String path0 = sdcardDir.getPath() + "/单轨吊综合测试仪/测试图片/";
        //判断文件夹是否存在，不存在则创建
        File fileDir = new File(path0);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        ScreenShot.savePic(ScreenShot.takeScreenShot(a, height), path0 + name + ".png");
    }


    /**
     * @Description ：截取布局
     */
    public static void getPic(final View view, final String nameStr) {

        File sdcardDir = Environment.getExternalStorageDirectory();
        final String path0 = sdcardDir.getPath() + "/单轨吊综合测试仪/测试图片/";
        File fileDir = new File(path0);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
            Log.i("kok", "创建目录了");
        }
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 要在运行在子线程中
                final Bitmap bmp = view.getDrawingCache(); // 获取图片
                File f = new File(path0 + nameStr + ".png");
                if (f.exists()) {
                    f.delete();
                }
                //判断文件是否存在，不存在则创建
                if (!f.exists()) {
                    try {
                        f.createNewFile();
                        Log.i("kok", "创建图片了");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    FileOutputStream out = new FileOutputStream(f);
                    bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
                    out.flush();
                    out.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                view.destroyDrawingCache(); // 保存过后释放资源
            }
        }, 500);
    }

    /**
     * @Description ：截取View缓存图片PNG，将View0、View1、View2 叠加
     * view： 控件   nameStr:图片名称
     */
    public static void getPictureToSD(View view0, View view1, View view2, String nameStr) {

        view0.setDrawingCacheEnabled(true);
        Bitmap tBitmap0 = view0.getDrawingCache();
        tBitmap0 = tBitmap0.createBitmap(tBitmap0);
        view0.setDrawingCacheEnabled(false);

        view1.setDrawingCacheEnabled(true);
        Bitmap tBitmap1 = view1.getDrawingCache();
        tBitmap1 = tBitmap1.createBitmap(tBitmap1);
        view1.setDrawingCacheEnabled(false);

        view2.setDrawingCacheEnabled(true);
        Bitmap tBitmap2 = view2.getDrawingCache();
        tBitmap2 = tBitmap1.createBitmap(tBitmap2);
        view1.setDrawingCacheEnabled(false);

        Bitmap bitmapMiddle1 = Bitmap.createBitmap(tBitmap0.getWidth(), tBitmap0.getHeight(), tBitmap0.getConfig());
        Canvas canvas = new Canvas(bitmapMiddle1);
        canvas.drawBitmap(tBitmap0, new Matrix(), null);
        canvas.drawBitmap(tBitmap1, 0, 0, null);  //120、350为bitmap2写入点的x、y坐标

        Bitmap bitmapMiddle2 = Bitmap.createBitmap(bitmapMiddle1.getWidth(), bitmapMiddle1.getHeight(), bitmapMiddle1.getConfig());
        Canvas canvas2 = new Canvas(bitmapMiddle2);
        canvas2.drawBitmap(bitmapMiddle1, new Matrix(), null);
        canvas2.drawBitmap(tBitmap2, 0, 0, null);  //120、350为bitmap2写入点的x、y坐标

//        Bitmap bitmap2;
//        Bitmap bitmap3 = Bitmap.createBitmap(bitmap1.getWidth(), bitmap1.getHeight(), bitmap1.getConfig());
//        Canvas canvas = new Canvas(bitmap3);
//        canvas.drawBitmap(bitmap1, new Matrix(), null);
//        canvas.drawBitmap(bitmap2, 120, 350, null);  //120、350为bitmap2写入点的x、y坐标
////将合并后的bitmap3保存为png图片到本地
//        FileOutputStream out = new FileOutputStream(path+File.separator+"image3.png");
//        bitmap3.compress(Bitmap.CompressFormat.PNG, 90, out);


        File f = new File(Environment.getExternalStorageDirectory() + "/" + nameStr + ".png");
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bitmapMiddle2.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description ：截取View缓存图片PNG
     * view： 控件   nameStr:图片名称
     */
    public static void getPictureToSD(View view, String nameStr) {

        view.setDrawingCacheEnabled(true);
        Bitmap tBitmap = view.getDrawingCache();
        tBitmap = tBitmap.createBitmap(tBitmap);
        view.setDrawingCacheEnabled(false);
        File sdcardDir = Environment.getExternalStorageDirectory();
        final String path0 = sdcardDir.getPath() + "/提升机综合测试仪/测试图片/";
        File f = new File(path0 + nameStr + ".png");
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            tBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
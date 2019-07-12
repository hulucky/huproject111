package com.example.hu.huproject.Bean;

import android.os.Environment;
import android.util.Log;

import com.example.hu.huproject.Interface.ISensorInf;
import com.example.hu.huproject.Utils.MyFunc;
import com.example.hu.huproject.Utils.SensorType;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DecimalFormat;

public class QjBean implements ISensorInf {
    public byte[] bRec;
    public Long time = System.currentTimeMillis();
    public final DecimalFormat df2 = new DecimalFormat("####0.00");
    public int powerVel = 0; //电量
    public int assiVel = 0; //信号
    public float xqj = 0F; //x倾角
    public float yqj = 0F; //y倾角
    public float zqj = 0F; //z倾角

    public QjBean(byte[] bRec) {
        this.bRec = bRec;
        caculate(bRec);
    }


    public float getQj() {
        return yqj;
    }

    public void set(float yqj) {
        this.yqj = yqj;
    }

    public void caculate(byte[] bRec) {
        this.bRec = bRec;
//        String filedata = readFileData(Environment.getExternalStorageDirectory() + "/test.txt");
        String filedata = readFileData1(Environment.getExternalStorageDirectory() + "/test.txt");
        String KHH = "";

        for (int i = 0; i < bRec.length; i++) {
            KHH = KHH + String.valueOf(bRec[i]) + " ";
        }
        KHH = filedata + "\n" + KHH + "\n";
        writeFileData(Environment.getExternalStorageDirectory() + "/test.txt", KHH);
        time = System.currentTimeMillis();
        xqj = (float) MyFunc.twobyteToint(bRec[14], bRec[15]) / 100;
        yqj = (float) MyFunc.twobyteToint(bRec[16], bRec[17]) / 100;
        zqj = (float) MyFunc.twobyteToint(bRec[18], bRec[19]) / 100;
        Log.i("ddf", "  xqj: " + xqj + "  yqj: " + yqj + "  zqj: " + zqj);
        powerVel = MyFunc.twoBytesToInt(bRec, 20);
        assiVel = MyFunc.HexToInt(MyFunc.Byte2Hex(bRec[22]));
    }


    public static void writeFileData(String filename, String message) {
        try {
            FileOutputStream fos = new FileOutputStream(filename);//获得FileOutputStream

            //将要写入的字符串转换为byte数组
            byte[] bytes = message.getBytes();
            fos.write(bytes);//将byte数组写入文件
            fos.close();//关闭文件输出流

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //打开指定文件，读取其数据，返回字符串对象(旧)
    public static String readFileData(String fileName) {

        String result = "";

        try {

            FileInputStream fis = new FileInputStream(fileName);

            //获取文件长度
            int lenght = fis.available();//文件过大时，有可能会导致内存溢出

            byte[] buffer = new byte[lenght];

            fis.read(buffer);
            fis.close();
            //将byte数组转换成指定格式的字符串
            result = new String(buffer, "UTF-8");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    //打开指定文件，读取其数据，返回字符串对象（）
    public static String readFileData1(String fileName) {

        StringBuffer stringBuffer = new StringBuffer();
        try {

            FileInputStream fis = new FileInputStream(fileName);
            byte[] bytes = new byte[1024];
            int len;
            while ((len = fis.read(bytes)) != -1) {
                stringBuffer.append(new String(bytes, 0, len, "UTF-8"));
            }
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return stringBuffer.toString();
    }


    @Override
    public int statue() {
        return 0;
    }

    @Override
    public float getPower() {
        return powerVel;
    }

    @Override
    public float getSignal() {
        return assiVel;
    }

    @Override
    public int getSensorType() {
        return SensorType.SENSOR_TYPE_QINGJIAO;
    }

    @Override
    public long getTime() {
        return time;
    }
}

package com.example.hu.huproject.Utils;


import android.util.Log;

import java.util.Arrays;

public class GetJiaoYanWei {

    public static byte getJiaoYan(String hex) {
        byte[] bytes = MyFunc.HexToByteArr(hex);
        Log.i("aaaa", "bytes==" + Arrays.toString(bytes));
        byte temp = bytes[2];
        for (int i = 3; i < bytes.length - 1; i++) {//异或到校验位的前一位，从而得到校验位
            temp ^= bytes[i];
        }
        return temp;
    }
}

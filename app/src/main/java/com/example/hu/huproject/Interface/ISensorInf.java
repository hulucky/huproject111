package com.example.hu.huproject.Interface;

public interface ISensorInf {
    //状态
    int statue();
    //传感器电量
    float getPower();
    //传感器信号强度
    float getSignal();
    //传感器类型
    int getSensorType();
    //获取传感器数据的时间点
    //用于判断传感器是否处于断开状态
    //（当前时间 - getTime（）） > 设定的判定时间 ? 断开：通信；
    long getTime();
}

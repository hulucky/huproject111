package com.example.hu.huproject.Bean;

import com.example.hu.huproject.Interface.ISensorInf;
import com.example.hu.huproject.Utils.MyFunc;
import com.example.hu.huproject.Utils.SensorType;

public class TimeBean1 implements ISensorInf {

    public float powerVel = 0;//电量
    public float assiVel = 0;//信号
    public Long time = 0L;//获取传感器数据的时间点

    public byte[] bRec;//串口数据包（时间传感器的心跳包）

    public TimeBean1(byte[] bRec) {
        this.bRec = bRec;
        caculate(bRec);
    }

    //calculate:计算
    //解析串口数据包，更新电量和信号（共18位）
    public void caculate(byte[] bRec) {
        this.bRec = bRec;
        time = System.currentTimeMillis();
        powerVel = MyFunc.twoBytesToInt(bRec, 14);
        assiVel = MyFunc.HexToInt(MyFunc.Byte2Hex(bRec[16]));
    }

    public float getPowerVel() {
        return powerVel;
    }

    public void setPowerVel(float powerVel) {
        this.powerVel = powerVel;
    }

    public float getAssiVel() {
        return assiVel;
    }

    public void setAssiVel(float assiVel) {
        this.assiVel = assiVel;
    }

    public void setTime(Long time) {
        this.time = time;
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
        return SensorType.SENSOR_TYPE_TIME1;
    }

    @Override
    public long getTime() {
        return time;
    }
}

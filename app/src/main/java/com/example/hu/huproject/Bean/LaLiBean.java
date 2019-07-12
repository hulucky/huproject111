package com.example.hu.huproject.Bean;

import com.example.hu.huproject.Interface.ISensorInf;
import com.example.hu.huproject.Utils.MyFunc;
import com.example.hu.huproject.Utils.SensorType;

//系统力的实体类
public class LaLiBean implements ISensorInf {
    public float powerVel = 0;//电量
    public float assiVel = 0;//信号
    public Long time = 0L;//获取传感器数据的时间点

    public byte[] bRec;//串口数据包

    public LaLiBean(byte[] bRec) {
        this.bRec = bRec;
        caculate(bRec);
    }

    //calculate:计算
    //解析串口数据包，并且更新Bean的属性（共38位）
    public void caculate(byte[] bRec) {
        this.bRec = bRec;
        time = System.currentTimeMillis();
        powerVel = MyFunc.twoBytesToInt(bRec, 18);
        assiVel = MyFunc.HexToInt(MyFunc.Byte2Hex(bRec[20]));
    }

    //状态
    @Override
    public int statue() {
        return 0;
    }

    //传感器电量
    @Override
    public float getPower() {
        return powerVel;
    }

    //传感器信号强度
    @Override
    public float getSignal() {
        return assiVel;
    }

    //传感器类型
    @Override
    public int getSensorType() {
        return SensorType.SENSOR_TYPE_LALI;
    }

    //获取传感器数据的时间点
    //用于判断传感器是否处于断开状态
    //（当前时间 - getTime（）） > 设定的判定时间 ? 断开：通信；
    @Override
    public long getTime() {
        return time;
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
}

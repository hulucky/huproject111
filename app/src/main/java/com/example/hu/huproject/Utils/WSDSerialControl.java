package com.example.hu.huproject.Utils;

import android.content.Context;
import android.util.Log;

import com.example.hu.huproject.Application.MyApp;
import com.example.hu.huproject.Bean.LaLiBean;
import com.example.hu.huproject.Bean.SuDuBean;
import com.example.hu.huproject.Bean.TimeBean;
import com.example.hu.huproject.Bean.WSDBean;
import com.example.hu.huproject.Interface.ISensorInf;
import com.example.hu.huproject.Listener.OnReceiveBreakTimeListener;
import com.xzkydz.bean.ComBean;
import com.xzkydz.helper.SerialHelper;

import java.util.Arrays;

import javax.annotation.Nullable;

import static com.example.hu.huproject.Application.MyApp.wsdBean;

//串口控制工具类
public class WSDSerialControl extends SerialHelper {

    public WSDSerialControl(Context context, int mDataType) {
        super(context, mDataType);
    }

    public WSDSerialControl(Context context, String sPort, String sBaudRate, int mDataType) {
        super(context, sPort, sBaudRate, mDataType);
    }

    @Override
    protected void onDataReceived(ComBean comBean) {
//        MyApp.comBean = comBean;
        switch (comBean.recDataType) {
            case 16://温湿度传感器
                MyApp.comBeanWSD = comBean;
                if (wsdBean == null) {
                    wsdBean = new WSDBean(comBean.recData);
                } else {
                    wsdBean.caculate(comBean.recData);
                }
                // 设置回调接口
                if (receivedSensorData != null && wsdBean != null) {
                    receivedSensorData.getData(wsdBean);
                }
                break;
        }

    }

    private OnReceivedSensorData receivedSensorData;

    public void setOnReceivedSensorListener(@Nullable OnReceivedSensorData l) {
        this.receivedSensorData = l;
    }

    public interface OnReceivedSensorData {
        void getData(ISensorInf sensorInf);
    }

}

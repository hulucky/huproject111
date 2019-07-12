package com.example.hu.huproject.Utils;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.hu.huproject.Application.MyApp;
import com.example.hu.huproject.Bean.LaLiBean;
import com.example.hu.huproject.Bean.QjBean;
import com.example.hu.huproject.Bean.SuDuBean;
import com.example.hu.huproject.Bean.TimeBean;
import com.example.hu.huproject.Fragment.KongDongTimeTestFragment;
import com.example.hu.huproject.Interface.ISensorInf;
import com.example.hu.huproject.Listener.OnReceiveBreakTimeListener;
import com.xzkydz.bean.ComBean;
import com.xzkydz.helper.SerialHelper;

import java.util.Arrays;

import javax.annotation.Nullable;

import static com.example.hu.huproject.Application.MyApp.MY_TAG;
import static com.example.hu.huproject.Application.MyApp.comBeanTime;
import static com.example.hu.huproject.Application.MyApp.getInstance;
import static com.example.hu.huproject.Application.MyApp.laLiBean;
import static com.example.hu.huproject.Application.MyApp.qjBean;
import static com.example.hu.huproject.Application.MyApp.sdBean;
import static com.example.hu.huproject.Application.MyApp.timeBean;

//串口控制工具类
public class SerialControl extends SerialHelper {
    private static final String TAG = "SerialControl";
    public int i = 0;

    public SerialControl(Context context, int mDataType) {
        super(context, mDataType);
    }

    public SerialControl(Context context, String sPort, String sBaudRate, int mDataType) {
        super(context, sPort, sBaudRate, mDataType);
    }

    private boolean isGetTimeAck = false;
    private boolean isGetSuduAck = false;
    private double breakTime;

    @Override
    protected void onDataReceived(ComBean comBean) {
        MyApp.comBean = comBean;
        Log.i(TAG, "type==" + comBean.recDataType);
        int ackNumber = MyApp.ackNumber;//获取到KongDongTimeTestFragment中存储的ackNumber
        switch (comBean.recDataType) {
            case 96://速度传感器
                try {
                    if (comBean.recData.length == 38) {//正常的速度响应
                        MyApp.comBeanSd = comBean;//给速度实体类赋值
                        // sdBean 是定义的速度传感器解析类，需要实现ISensorInf接口。
                        // 在这儿获取最新的速度压数据。sdBean。
                        if (sdBean == null) {
                            sdBean = new SuDuBean(comBean.recData);
                        } else {
                            //解析速度串口数据包，并且更新sdBean的属性。
                            sdBean.caculate(comBean.recData);
                            Log.i("bbb", "速度数据长度为：" + comBean.recData.length +
                                    "速度数据为：" + Arrays.toString(comBean.recData));
                        }
                        // 设置回调接口
                        if (receivedSensorData != null && sdBean != null) {
                            receivedSensorData.getData(sdBean);
                        }
                    }
                    //标记速度测试重新开始响应 E3  -29
                    if (comBean.recData[13] == -29 && comBean.recData.length == 16
                            && comBean.recData[12] == ackNumber) {
//                        Log.i("asa", "速度数据长度为：" + comBean.recData.length +
//                                "速度数据为：" + Arrays.toString(comBean.recData));
                        isGetSuduAck = true;
                        Log.i("asa", "速度收到ACK ");
                        Log.i("asa", "速度ackNumber==" + ackNumber);
                    } else if (comBean.recData[13] == -29 && comBean.recData.length == 16
                            && comBean.recData[12] != ackNumber) {
                        isGetSuduAck = false;
                    }
                } catch (Exception e) {
                    Log.e(TAG, "速度传感器解析错误：" + e.getMessage());
                }
                break;
            case 81://时间传感器
                try {
                    //只取开始（时间1）信号，不取结束（时间2）信号
                    if (comBean.recData[10] == 1) {
                        //正常的心跳包
                        if (comBean.recData.length == 18 && comBean.recData[13] != -29) {
                            MyApp.comBeanTime = comBean;//给时间实体类赋值
                            // timeBean 是定义的速度传感器解析类，需要实现ISensorInf接口。
                            // 在这儿获取最新的时间数据。timeBean。
                            if (timeBean == null) {
                                timeBean = new TimeBean(comBean.recData);
                            } else {
                                //解析速度串口数据包，并且更新sdBean的属性。
                                timeBean.caculate(comBean.recData);
                                Log.i("bbbbb", "时间数据长度为：" + comBean.recData.length +
                                        "时间数据为：" + Arrays.toString(comBean.recData));
                            }
                            // 设置回调接口
                            if (receivedSensorData != null && timeBean != null) {
                                receivedSensorData.getData(timeBean);
                            }
                        }
                        //E3  -29
                        //开始计时响应的包
                        if (comBean.recData.length == 18 && comBean.recData[13] == -29 && comBean.recData[12] == ackNumber) {
                            isGetTimeAck = true;
                            breakTime = 0;
                            Log.i("asa", "时间收到ACK ");
                            Log.i("asa", "时间ackNumber==" + ackNumber);
                        } else if (comBean.recData.length == 18 && comBean.recData[13] == -29 && comBean.recData[12] != ackNumber) {
                            Log.i("asa", "收到计时响应包，但ack有错，时间ackNumber==" + ackNumber);
                            isGetTimeAck = false;
                        }
                        //时间传感器电平变化就会测试时间数据
                        if (isGetTimeAck && comBean.recData.length == 22) {
                            float miao = (float) MyFunc.twobyteToint_(comBean.recData[14], comBean.recData[15]);
                            float haomaio = (float) MyFunc.twobyteToint_(comBean.recData[16], comBean.recData[17]) * 0.1f / 1000f;
                            breakTime = miao + haomaio;
                            //当开关信号变化时，接口中的方法被调用
                            listener.OnReceivedBreakData(breakTime);
                            //                        KongdongTimeFragmentVp1.setBreakTime(breakTime);
                            MyApp.breakTime = breakTime;//给断开时间赋值
                            Log.i("ppp", "----------------miao==" + miao + "    haomiao==" + haomaio + "     breaktime==" + breakTime);
                            //第13位适用于区分数据包的前后 分-->和  2 ， 和--->分  5
                            Log.i("asa", "时间数据长度为：" + comBean.recData.length +
                                    "时间数据为：" + Arrays.toString(comBean.recData));
                        }
                    }


                } catch (Exception e) {
                    Log.e(TAG, "时间传感器解析错误：" + e.getMessage());
                }
                break;
            case 64://拉力传感器
                try {
                    if (comBean.recData.length == 22) {
                        MyApp.comBeanLali = comBean;//给拉力实体类赋值
                        if (laLiBean == null) {
                            laLiBean = new LaLiBean(comBean.recData);
                        } else {
                            //解析拉力串口数据包，并且更新laLiBean的属性。
                            laLiBean.caculate(comBean.recData);
                            Log.i("bbb", "拉力数据长度为：" + comBean.recData.length +
                                    "拉力数据为：" + Arrays.toString(comBean.recData));
                        }
                        // 设置回调接口
                        if (receivedSensorData != null && laLiBean != null) {
                            receivedSensorData.getData(laLiBean);
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "拉力传感器解析错误：" + e.getMessage());
                }
                break;
            case -94://倾角传感器
//                MyApp.isAngleConnected = true;
                if (comBean.recData.length == 24) {
                    Log.i("dds", "倾角数据为: " + Arrays.toString(comBean.recData));
                    if (qjBean == null) {
                        qjBean = new QjBean(comBean.recData);
                    } else {
                        qjBean.caculate(comBean.recData);
                    }
                    // 设置回调接口
                    if (receivedSensorData != null && qjBean != null) {
                        receivedSensorData.getData(qjBean);
                    }
                }
                break;
            case 16://温湿度传感器
                break;
        }
        if (isGetSuduAck && isGetTimeAck) {
            MyApp.isAckSync = true;
            Log.i("aaa", "ACK同步了,isAckSync为true");
        } else {
            MyApp.isAckSync = false;
        }
    }


    private OnReceivedSensorData receivedSensorData;

    public void setOnReceivedSensorListener(@Nullable OnReceivedSensorData l) {
        this.receivedSensorData = l;
    }

    public interface OnReceivedSensorData {
        void getData(ISensorInf sensorInf);
    }

    public static OnReceiveBreakTimeListener listener;

    public static void setOnClickListerer(OnReceiveBreakTimeListener listerer) {
        listener = listerer;
    }

}

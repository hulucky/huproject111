package com.example.hu.huproject.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hu.huproject.Application.MyApp;
import com.example.hu.huproject.Bean.SuDuBean;
import com.example.hu.huproject.Bean.TimeBean1;
import com.example.hu.huproject.Entity.ManZaiDownEntity;
import com.example.hu.huproject.Entity.TaskEntity;
import com.example.hu.huproject.R;
import com.example.hu.huproject.Utils.MyFunc;
import com.example.hu.huproject.Utils.ScreenShot;
import com.example.hu.huproject.Utils.SerialControl;
import com.example.hu.huproject.Utils.TaskEntityUtils;
import com.example.hu.huproject.View.PopupView;
import com.mchsdk.paysdk.mylibrary.ListFragment;
import com.sensor.SensorData;
import com.sensor.SensorInf;
import com.sensor.view.SensorView;
import com.xzkydz.bean.ComBean;
import com.xzkydz.helper.ComControl;
import com.xzkydz.helper.SerialHelper;
import com.xzkydz.util.DataType;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.json.JSONArray;
import org.json.JSONException;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


//满载向下制动距离测试的测试界面
public class ManZaiXiaTestFragment extends ListFragment {

    Unbinder unbinder;
    @BindView(R.id.sv_time1)
    SensorView svTime1;
    @BindView(R.id.sv_sudu)
    SensorView svSudu;
    @BindView(R.id.llsensor)
    LinearLayout llsensor;
    @BindView(R.id.frameLayout1)
    FrameLayout frameLayout1;
    @BindView(R.id.frameLayout2)
    FrameLayout frameLayout2;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.linear_time)
    LinearLayout linearTime;
    @BindView(R.id.tv_zhidongjuli)
    TextView tvZhidongjuli;
    @BindView(R.id.tv_zhidongchusudu)
    TextView tvZhidongchusudu;
    @BindView(R.id.ll1)
    LinearLayout ll1;
    @BindView(R.id.tv_liumiaojuli)
    TextView tvLiumiaojuli;
    @BindView(R.id.tv_zhidongtime)
    TextView tvZhidongtime;
    @BindView(R.id.rl1)
    RelativeLayout rl1;
    @BindView(R.id.btn_save)
    TextView btnSave;
    @BindView(R.id.btn_start)
    TextView btnStart;

    private MySerialcontrol comA;//串口
    private Context context;
    private TimeBean1 timeBean1;
    private SuDuBean suDuBean;
    private boolean isTime1Connected;
    private boolean isSuduConnected;
    private boolean isGetTime1Ack;
    private boolean isGetSuduAck;
    private int ackNumber = 0;
    private int time1Xuhao = 0;//当序号为1时才划线，保证只划线一次
    private float breakTime1;//时间传感器 断开/闭合 的时间
    private boolean isRunning = false;
    private int numButonClick = 0;
    private boolean isAckSync;//ACK是否同步标志
    private Handler timeHandler;
    private ArrayList<Double> speedDataLists;
    private ArrayList<Double> y_speed;
    private double addX1, addY1, addY1_before, max_speed;
    private int cishu = 0;//划竖线次数，用此数来保证竖线只会划一次(break时)
    private double chaTime = 0;//当速度未开始划线时，用此数来记录走过的时间（计时器的时间减去此数来保证速度和时间同步）
    private int endTimeCishu = 0;//划竖线次数，用此数来保证竖线只会划一次（停止时）
    private int s1_left;//断点左侧时间
    private int s1_right;//断点右侧时间
    private double zhanBi;//占比
    private double breakTime_double;//制动时间（减掉了未划线的时间）
    //点集
    private XYMultipleSeriesDataset mDataset;
    private XYSeries series1;
    private XYSeries series2;
    private XYSeries series3;
    private GraphicalView chart;//图像视图
    private XYMultipleSeriesRenderer renderer;
    private DecimalFormat df1 = new DecimalFormat("0.0");
    private DecimalFormat df2 = new DecimalFormat("0.00");
    private float d = 0.1455f;//直径
    private boolean start_bl;//开始划线标志
    private String x_time = "0";//运行时间（是一个string类型数据，表示多少秒）
    private JSONArray jsonArray_set1;
    private boolean isGetBreakTime = false;//开关信号是否变化
    //任务参数id
    private Long id_param;
    private TaskEntity mtaskEntity;
    private ManZaiDownEntity manZaiDownEntity;
    private MyApp myApp;


    //实例化自己，可以从外部传参
    public static ManZaiXiaTestFragment newInstance(int position) {
        ManZaiXiaTestFragment testFragment = new ManZaiXiaTestFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", position);
        testFragment.setArguments(bundle);
        return testFragment;
    }

    @Override
    protected void lazyLoadData() {

    }

    @Override
    protected void initView() {
        context = getContext();
        comA = new MySerialcontrol(context, DataType.DATA_OK_PARSE);
        comA.setiDelay(50);
        ComControl.OpenComPort(comA);
        svTime1.setOnStatusChangeListener(new MyOnStatusChanger());
        svSudu.setOnStatusChangeListener(new MyOnStatusChanger());
        timeHandler = new Handler();
        timeHandler.post(updateTime);
        initChart();
        myApp = MyApp.getInstance();
        mtaskEntity = MyApp.getTaskEntity();
        id_param = mtaskEntity.getTaskId();//拿到本次测试的id
        Log.i("ggg", "id_param: " + id_param);
    }

    private void initChart() {
        speedDataLists = new ArrayList<>();
        y_speed = new ArrayList<Double>();
        addX1 = 0;
        addY1 = 0;
        max_speed = 0;
        addY1_before = 0;
        chaTime = 0;
        endTimeCishu = 0;
        s1_left = 0;
        s1_right = 0;
        zhanBi = 0.0;
        breakTime_double = 0.0;
        series1 = new XYSeries("速度");
        series2 = new XYSeries("制动时间", 1);
        series3 = new XYSeries("停止时间", 2);
        //创建一个数据集的实例，这个数据集将被用来创建图表
        mDataset = new XYMultipleSeriesDataset();
        //将点集添加进这个数据集中
        mDataset.addSeries(0, series1);
        mDataset.addSeries(1, series2);
        mDataset.addSeries(2, series3);
        // 以下都是曲线的样式和属性等等的设置，renderer相当于一个用来给图表做渲染的句柄
        renderer = buildRendener();
        //设置好图表的样式
        setChartSettings(renderer);

        chart = ChartFactory.getCubeLineChartView(getActivity(), mDataset, renderer, 0.1f);
        //生成图表(chart----GraphicalView图像视图)
        chart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        frameLayout2.removeAllViews();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        //获取当前点击的点的相关信息
                        SeriesSelection seriesSelection = chart.getCurrentSeriesAndPoint();
                        if (seriesSelection != null && seriesSelection.getSeriesIndex() == 0) {
                            PopupView pv = new PopupView(getActivity()); // 浮窗
                            pv.setStartX(event.getX()); // 设置浮窗的位置
                            pv.setStartY(event.getY());
                            pv.setValue(String.format("%.2f", seriesSelection.getValue())); // 设置浮窗上显示的数值
                            pv.setTextColor(getResources().getColor(R.color.red));
                            frameLayout2.addView(pv);// 将浮窗添加到Framelayout
                        }
                        break;
                }
                return false;
            }
        });
        //将图表添加到布局中去
        frameLayout1.addView(chart);
    }

    //计算制动距离
    private String calCulateZhiDongJuLi() {
        //(参见公式)
        if (isGetBreakTime) {
            //计算出第一段距离
            double s1 = (y_speed.get(s1_left - 1) + y_speed.get(s1_right - 1)) * 0.1 * zhanBi / 2;
            //计算出剩下的距离
            double sum0 = 0;
            for (int i = s1_right - 1; i <= y_speed.size(); i++) {
                if (i == (y_speed.size() - 1)) {
                    break;
                }
                double s = (y_speed.get(i) + y_speed.get(i + 1)) * 0.1 / 2;
                sum0 = sum0 + s;
            }
            //制动距离等于第一段距离加上剩下的距离
            double sum = s1 + sum0;
            Log.i("ggg", "s1=" + s1 + "    sum0=" + sum0);
            return df2.format(sum);
        } else {//未触发开关信号时，制动距离给个默认值0
            return "0";
        }
    }

    //计算制动初速度
    private String calculateChuSudu() {
        if (isGetBreakTime) {
            double zhiDongChuSuDu = (y_speed.get(s1_left - 1) + y_speed.get(s1_right - 1)) / 2;
            return df2.format(zhiDongChuSuDu);
        } else {
            return "0";
        }
    }

    //计算6s运行距离
    private String calculateSixJuli() {
        if (isGetBreakTime) {
            double zhiDongChuSuDu = (y_speed.get(s1_left - 1) + y_speed.get(s1_right - 1)) / 2;
            double d = (double) Math.round(zhiDongChuSuDu * 100) / 100;
            Log.i("ggg", "d: ===" + d);
            return df2.format(d * 6);
        } else {
            return "0";
        }
    }

    //计算制动时间
    private String calculateZhiDongTime() {
        if (isGetBreakTime) {
            return df2.format(addX1 - breakTime_double);
        } else {
            return "0";
        }
    }


    private void stop() {
        btnStart.setText("开始");
        isRunning = false;
        tvZhidongjuli.setText(calCulateZhiDongJuLi());//制动距离
        tvZhidongchusudu.setText(calculateChuSudu());//制动初速度
        tvLiumiaojuli.setText(calculateSixJuli());//6s距离
        tvZhidongtime.setText(calculateZhiDongTime());//制动时间
        x_time = df2.format(addX1);
        Log.i("ggg", "x_time: " + x_time);
        jsonArray_set1 = new JSONArray();
        for (double b : y_speed) {//y_speed是本次测试所有的速度集合
            try {
                jsonArray_set1.put(b);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick({R.id.btn_save, R.id.btn_start})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_save://保存数据
                if (isTime1Connected && isSuduConnected) {
                    if (isRunning) {
                        Toast.makeText(getContext(), "请停止测试后再保存", Toast.LENGTH_SHORT).show();
                    } else {
                        if (id_param == null) {
                            Toast.makeText(getActivity(), "id_param为空，保存失败！", Toast.LENGTH_SHORT).show();
                        } else {
                            manZaiDownEntity = new ManZaiDownEntity();
                            manZaiDownEntity.setKey(id_param);
                            mtaskEntity.setIs_ManZaiXiangXiaSave(true);
                            TaskEntityUtils.update(mtaskEntity);

                            manZaiDownEntity.setCreateTime(tvTime.getText().toString());//创建时间
                            manZaiDownEntity.setMaxSpeed(df2.format(max_speed));//最大速度(string类型)
                            manZaiDownEntity.setRunTimeTotal(x_time);//运行时间(string类型),就是停止时间
                            jsonArray_set1 = new JSONArray();
                            for (double b : y_speed) {//y_speed是本次测试所有的速度集合
                                try {
                                    jsonArray_set1.put(b);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            manZaiDownEntity.setPoint_speed(jsonArray_set1.toString()); //速度集合
                            manZaiDownEntity.setBreakTime(df2.format(breakTime_double));//断开时间
                            manZaiDownEntity.setZhiDongJuli(tvZhidongjuli.getText().toString());//制动距离
                            manZaiDownEntity.setZhiDongChuSuDu(tvZhidongchusudu.getText().toString());//制动初速度
                            manZaiDownEntity.setLiuMiaoJuLi(tvLiumiaojuli.getText().toString());//6s运行距离
                            manZaiDownEntity.setZhiDongShiJian(tvZhidongtime.getText().toString());//制动时间

                            //保存满载向下数据数据
                            myApp.getmDaoSession().getManZaiDownEntityDao().insert(manZaiDownEntity);
                            Toast toast = Toast.makeText(getActivity(),
                                    "保存成功！", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);// 居中显示
                            toast.show();

                            //截屏
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ScreenShot.getPic(rl1, "满载向下制动距离图_" + manZaiDownEntity.getId());
                                }
                            }, 500);
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "传感器未连接！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_start:
                if (isTime1Connected && isSuduConnected) {
                    if (isRunning) {//如果正在运行，那么点击之后会停止,停止后，按钮显示为开始
                        //停止
                        stop();
                    } else {//如果现在是停止状态，那么点击之后会开始
                        //开始
                        //4B59 0B    FFFF    0001      01      02        FF      FF     0000       E3           EA
                        //帧头 长度 目的地址 生产批号 客户编码 仪器编码  设备类型 设备编号  序号  (13，指令)E3    (14)校验
                        //                                                                         227           234
                        isGetBreakTime = false;
                        isGetTime1Ack = false;
                        isGetSuduAck = false;
                        cishu = 0;
                        time1Xuhao = 0;
                        breakTime1 = 0;
                        isAckSync = false;
                        numButonClick += 1;//每点击一次，序号加一
                        ackNumber = numButonClick % 4;
                        initChart();
                        switch (ackNumber) {
                            case 0:
                                comA.sendHex("4B590BFFFF00010102FFFF0000E3EA");
                                break;
                            case 1://第一次发这个
                                comA.sendHex("4B590BFFFF00010102FFFF0001E3EB");
                                break;
                            case 2:
                                comA.sendHex("4B590BFFFF00010102FFFF0002E3E8");
                                break;
                            case 3:
                                comA.sendHex("4B590BFFFF00010102FFFF0003E3E9");
                                break;
                        }
                    }
                } else {
                    Toast.makeText(context, "传感器未连接", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    private class MyOnStatusChanger implements SensorView.OnStatusChangeListener {
        @Override
        public void status(View view, int i, int i1) {
            int id = view.getId();
            if (i1 == SensorInf.SEARCHING) {
                switch (id) {
                    case R.id.sv_time1://断开链接时调用
                        isTime1Connected = false;
                        Toast.makeText(getContext(), "开关信号断开", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.sv_sudu://断开链接时调用
                        isSuduConnected = false;
                        Toast.makeText(getContext(), "速度传感器断开", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }
    }


    public class MySerialcontrol extends SerialHelper {

        public MySerialcontrol(Context context, int mDataType) {
            super(context, mDataType);
        }

        public MySerialcontrol(Context context, String sPort, String sBaudRate, int mDataType) {
            super(context, sPort, sBaudRate, mDataType);
        }

        @Override
        protected void onDataReceived(final ComBean comBean) {
            Log.i("ggg", "comBean: " + Arrays.toString(comBean.recData));
            switch (comBean.recDataType) {
                case 81://时间传感器
                    if (comBean.recData[10] == 1) {//表示只读开始时间信号
                        isTime1Connected = true;
                        if (comBean.recData.length == 18 && comBean.recData[13] != -29) {
                            if (timeBean1 == null) {
                                timeBean1 = new TimeBean1(comBean.recData);
                            } else {
                                timeBean1.caculate(comBean.recData);
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    SensorData sensorData = new SensorData(timeBean1.getPower(), timeBean1.getSignal(), SensorInf.NORMAL, System.currentTimeMillis());
                                    //时刻更新传感器状态
                                    svTime1.setData(sensorData);
                                }
                            });
                        }
                        //开始计时响应的包
                        if (comBean.recData.length == 18 && comBean.recData[13] == -29 && comBean.recData[12] == ackNumber) {
                            isGetTime1Ack = true;
                        } else if (comBean.recData.length == 18 && comBean.recData[13] == -29 && comBean.recData[12] != ackNumber) {
                            isGetTime1Ack = false;
                        }
                        //时间传感器电平变化就会测试时间数据
                        if (isGetTime1Ack && comBean.recData.length == 22) {
                            time1Xuhao = time1Xuhao + 1;
                            if (time1Xuhao == 1) {//只拿第一次的值，防止跳动
                                float miao = (float) MyFunc.twobyteToint_(comBean.recData[14], comBean.recData[15]);
                                float haomaio = (float) MyFunc.twobyteToint_(comBean.recData[16], comBean.recData[17]) * 0.1f / 1000f;
                                breakTime1 = miao + haomaio;
                                cishu = cishu + 1;
                                if (cishu == 1) {//只有第一次的时候才会划线
                                    isGetBreakTime = true;//
                                    mDataset.removeSeries(series2);
                                    if (breakTime1 > 0) {
                                        breakTime_double = breakTime1 - chaTime;//断开时间(8.523)
                                        int round = (int) Math.round(breakTime_double * 100);//=852
                                        s1_left = round / 10;//断点左侧时间 =85
                                        s1_right = round / 10 + 1; //断开右侧时间 =86
                                        zhanBi = (10 - round % 10) / (double) 10;//占比=0.8
                                        Log.i("ggg", "断开时间: " + breakTime_double);
                                        Log.i("ggg", "chaTime: " + chaTime);
                                        series2.add(breakTime_double, 0);
                                        series2.add(breakTime_double, max_speed * 3 / 2);
                                    }
                                    mDataset.addSeries(1, series2);
                                    chart.invalidate();
                                }
                            }

                        }
                    }
                    break;

                case 96://速度传感器
                    if (comBean.recData.length == 38) {//正常的速度响应(显示传感去状态)
                        Log.i("jjjjj", "suduByte==" + Arrays.toString(comBean.recData));
                        isSuduConnected = true;
                        if (suDuBean == null) {
                            suDuBean = new SuDuBean(comBean.recData);
                        } else {
                            suDuBean.caculate(comBean.recData);
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                SensorData sensorData = new SensorData(suDuBean.getPower(), suDuBean.getSignal(), SensorInf.NORMAL, System.currentTimeMillis());
                                svSudu.setData(sensorData);
                            }
                        });

                    }
                    //标记速度测试重新开始响应 E3  -29
                    if (comBean.recData[13] == -29 && comBean.recData.length == 16
                            && comBean.recData[12] == ackNumber) {
                        isGetSuduAck = true;
                    } else if (comBean.recData[13] == -29 && comBean.recData.length == 16
                            && comBean.recData[12] != ackNumber) {
                        isGetSuduAck = false;
                    }

                    break;
            }

            //这段代码表示点击开始后成功同步
            if (comBean.recData[13] == -29 && isGetTime1Ack && isGetSuduAck) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("jjjjj", "停止");
                        isAckSync = true;
                        btnStart.setText("停止");
                        isRunning = true;

                        tvZhidongjuli.setText("0");//制动距离
                        tvZhidongchusudu.setText("0");//制动初速度
                        tvLiumiaojuli.setText("0");//6s距离
                        tvZhidongtime.setText("0");//制动时间

                        y_speed.clear();
                        jsonArray_set1 = null;
                    }
                });
            }

            //开始画图
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (btnStart.getText().toString().trim().equals("停止")) {
                        if (isAckSync && isRunning && comBean.recData.length == 38 && comBean.recDataType == 96) {
                            updateChart1(comBean.recData);
                        }

                    }
                }
            });

        }


    }


    //画图
    private void updateChart1(byte[] buffer) {

        Log.i("jjjjj", "suduByte==" + Arrays.toString(buffer));
        speedDataLists.clear();
        speedDataLists.add((double) MyFunc.twoBytesToInt_speed(buffer,
                14));
        speedDataLists.add((double) MyFunc.twoBytesToInt_speed(buffer,
                16));
        speedDataLists.add((double) MyFunc.twoBytesToInt_speed(buffer,
                18));
        speedDataLists.add((double) MyFunc.twoBytesToInt_speed(buffer,
                20));
        speedDataLists.add((double) MyFunc.twoBytesToInt_speed(buffer,
                22));
        speedDataLists.add((double) MyFunc.twoBytesToInt_speed(buffer,
                24));
        speedDataLists.add((double) MyFunc.twoBytesToInt_speed(buffer,
                26));
        speedDataLists.add((double) MyFunc.twoBytesToInt_speed(buffer,
                28));
        speedDataLists.add((double) MyFunc.twoBytesToInt_speed(buffer,
                30));
        speedDataLists.add((double) MyFunc.twoBytesToInt_speed(buffer,
                32));
        //画点;
        for (int i = 0; i < 10; i++) {
            //得到速度
            addY1 = (speedDataLists.get(i) * 3.1415f * d * 10d) / 200d;
            if (addY1 > max_speed) {
                max_speed = addY1;
            }
            Log.i("jjjjj", "addY1==" + addY1 + "addX1==" + addX1);

            if (addY1_before == 0 && addY1 == 0) {//点击开始，但并未划线时，记录此时走过的时间
                chaTime = chaTime + 0.1;
                continue;
            }
            if (addY1 > 0.02285 && addY1_before == 0) {
                //速度开始划线条件  (运算之前是addY1 > 3)===0.02285
                start_bl = true;
            }
            if (start_bl) {
                addX1 = (addX1 + 0.1);
                renderer.setXAxisMin(0, 0);
                renderer.setXAxisMax(addX1 + 30, 0);
                renderer.setYAxisMin(0);
                renderer.setYAxisMax(max_speed * 3 / 2);
                renderer.setXAxisMin(0, 1);
                renderer.setXAxisMax(addX1 + 30, 1);
                renderer.setYAxisMin(0, 1);
                renderer.setYAxisMax(max_speed * 3 / 2, 1);
                renderer.setXAxisMin(0, 2);
                renderer.setXAxisMax(addX1 + 30, 2);
                renderer.setYAxisMin(0, 2);
                renderer.setYAxisMax(max_speed * 3 / 2, 2);
                y_speed.add(addY1);
                mDataset.removeSeries(series1);
                series1.add(addX1, addY1);
                mDataset.addSeries(0, series1);
                chart.invalidate();//重绘，更新视图
                addY1_before = addY1;
            }
            //最大速度大于0.2并且现速度为0时，认为已经停止,停止时划线
            if (max_speed > 0.2 && addY1 == 0) {
                stop();
                endTimeCishu = endTimeCishu + 1;
                if (endTimeCishu == 1) {
                    mDataset.removeSeries(series3);
                    series3.add(addX1, 0);
                    series3.add(addX1, max_speed * 3 / 2);
                    mDataset.addSeries(2, series3);
                    chart.invalidate();
                }
                return;
            }
        }
        //以上都是速度的数据

    }


    @Override
    protected int setLayoutId() {
        return R.layout.fragment_manzaixia_test;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isAckSync = false;
        ackNumber = 0;
        svSudu.destroy();
        svTime1.destroy();
        comA.close();
        if (timeHandler != null) {
            timeHandler.removeCallbacks(updateTime);
            timeHandler = null;
        }
    }

    //更新时间
    Runnable updateTime = new Runnable() {
        @Override
        public void run() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String format = sdf.format(new Date());
            tvTime.setText(format);
            if (timeHandler == null) {
                return;
            }
            timeHandler.postDelayed(updateTime, 1000);
        }
    };


    private XYMultipleSeriesRenderer buildRendener() {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer(3);
        // 设置图表中曲线本身的样式，包括颜色、点的大小以及线的粗细等
        XYSeriesRenderer r = new XYSeriesRenderer();//第一条
        r.setColor(Color.RED);
        r.setPointStyle(PointStyle.CIRCLE);
        r.setFillPoints(true);
        r.setChartValuesSpacing(3);
        renderer.addSeriesRenderer(r);

        r = new XYSeriesRenderer();//第二条
        r.setColor(Color.BLUE);
        r.setPointStyle(PointStyle.CIRCLE);
        r.setFillPoints(true);
        r.setChartValuesSpacing(3);
        renderer.addSeriesRenderer(r);

        r = new XYSeriesRenderer();//第三条
        r.setColor(Color.BLACK);
        r.setPointStyle(PointStyle.CIRCLE);
        r.setFillPoints(true);
        r.setChartValuesSpacing(3);
        renderer.addSeriesRenderer(r);

        return renderer;
    }

    //设置图表的样式
    private void setChartSettings(XYMultipleSeriesRenderer renderer) {
        // 有关对图表的渲染可参看api文档
        renderer.setApplyBackgroundColor(true);//设置是否显示背景色
        renderer.setBackgroundColor(Color.WHITE);//设置背景色
        renderer.setShowGrid(false);
        renderer.setXLabels(12);//设置X轴标签数量
        renderer.setYLabels(10);//设置X轴标签数量
        renderer.setXTitle("时间(s)");
        renderer.setMargins(new int[]{20, 40, 10, 30});// 图形4边距上左下右
        // 设置点的大小(图上显示的点的大小和图例中点的大小都会被设置)
        renderer.setPointSize(0f);
        renderer.setShowLegend(true);//底部说明
        renderer.setMarginsColor(Color.parseColor("#eeeeee"));
        renderer.setAxesColor(Color.parseColor("#000000"));
        renderer.setChartTitleTextSize(20);// 设置整个图表标题文字大小
        renderer.setAxisTitleTextSize(16);// 设置轴标题文字的大小
        renderer.setLabelsTextSize(15);//设置刻度显示文字的大小(XY轴都会被设置)
        renderer.setLegendTextSize(15);//图例文字大小
        renderer.setPanEnabled(true, true);// 可上下拖动，可水平拖动
        renderer.setPanLimits(new double[]{0, 10000, 0, 10000});//设置拉动的范围
        renderer.setZoomEnabled(true);//设置渲染器允许放大缩小
        renderer.setZoomLimits(new double[]{0, 10000, 0, 10000});//设置缩放的范围
        renderer.setZoomButtonsVisible(false);//设置显示放大缩小按钮
        renderer.setAntialiasing(true);//消除锯齿
        renderer.setFitLegend(true);//调节图例至适当位置
        // 设置点的缓冲半径值(在某点附近点击时,多大范围内算点击这个点)
        renderer.setSelectableBuffer(20);
        renderer.setXLabelsColor(Color.BLACK);
        renderer.setLabelsColor(Color.BLACK);
        renderer.setYLabelsColor(0, Color.RED);
        renderer.setXAxisMin(0, 0);
        renderer.setXAxisMax(30, 0);
        renderer.setYAxisMin(0, 0);
        renderer.setYAxisMax(50, 0);

        renderer.setYLabelsColor(1, Color.TRANSPARENT);
        renderer.setXAxisMin(0, 1);
        renderer.setXAxisMax(30, 1);
        renderer.setYAxisMin(0, 1);
        renderer.setYAxisMax(50, 1);

        renderer.setYLabelsColor(2, Color.TRANSPARENT);
        renderer.setXAxisMin(0, 2);
        renderer.setXAxisMax(30, 2);
        renderer.setYAxisMin(0, 2);
        renderer.setYAxisMax(50, 2);
        renderer.setYLabelsAlign(Paint.Align.RIGHT, 0);
        renderer.setYLabelsAlign(Paint.Align.RIGHT, 1);
        renderer.setYLabelsAlign(Paint.Align.RIGHT, 2);
    }


}

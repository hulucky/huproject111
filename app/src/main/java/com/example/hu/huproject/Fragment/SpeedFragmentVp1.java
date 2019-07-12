package com.example.hu.huproject.Fragment;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hu.huproject.Activity.DataDetailActivity;
import com.example.hu.huproject.Application.MyApp;
import com.example.hu.huproject.Entity.SpeedEntity;
import com.example.hu.huproject.Entity.TaskEntity;
import com.example.hu.huproject.R;
import com.example.hu.huproject.Utils.MyFunc;
import com.example.hu.huproject.Utils.ScreenShot;
import com.example.hu.huproject.Utils.StringUtil;
import com.example.hu.huproject.Utils.TaskEntityUtils;
import com.example.hu.huproject.View.PopupView;
import com.mchsdk.paysdk.mylibrary.ListFragment;
import com.xzkydz.bean.ComBean;

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
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

//速度==》》测试图
public class SpeedFragmentVp1 extends Fragment {
    RelativeLayout rlTitle;
    FrameLayout frameLayout1;
    FrameLayout frameLayout2;
    TextView speedYunxing;
    LinearLayout ll1;
    TextView speedMax;
    TextView tvTime;

    //任务参数id
    private Long id_param;
    //数据集合
    private List<Double> speedDataLists;
    private List<Double> currentDataList;
    //Y值集合
    private List<Double> y_speed;//速度点集集合
    private List<Double> y_current;
    private List<Double> y_speed_f;
    //运行时间（是一个string类型数据，表示多少秒）
    private String x_time;

    private JSONArray jsonArray_set1;
    private JSONArray jsonArray_get;//查看结果时用来存取数据
    //速度实体类
    private SpeedEntity speedEntity;
    private List<SpeedEntity> speedEntities;

    //点集
    private XYMultipleSeriesDataset mDataset;
    private XYSeries series1;
    private XYSeries series2;
    private GraphicalView chart;//图像视图
    private XYMultipleSeriesRenderer renderer;
    private double addX1, addX2, addY1, addY2, addY1_before;
    private int f_count;
    //划线条件
    private boolean draw_gtzs, draw_csdj;
    //是否点击开始
    private boolean startBtn_bl;
    //是否点击停止
    private boolean stopBtn_bl;
    //是否显示实时值:位移
    private boolean showValue_bl;
    //是否收到ACK,以便同时开始
    private boolean speed_bl, current_bl;
    //单位脉冲代表的距离
    private double unit_distance;
    //速度开始划线条件:速度、隔爆壳
    private boolean start_bl, start_bl2;
    //最大速度（计算）
    private double max_speed, max_speed_f;
    //最大电流（计算）
    //给定坐标存储
    private HashMap hm = new HashMap();
    //初始化小数格式
    DecimalFormat df2 = new DecimalFormat("0.00");
    private Handler mHandler_time;
    private Handler mHandler_drawChart;//画图
    private static final int MSG_UPDATE_CURRENT_TIME = 1;
    private static final int MSG_UPDATE_CHART = 2;
    private static byte[] buffer;

    private static final String TAG = "SpeedFragmentVp1";
    private float d = 0.1455f;//直径
    private TaskEntity mtaskEntity;
    private MyApp myApp;
    private String parent;//用于判断该fragment是从哪里进入的

    private View view;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.fragment_speedvp1, container, false);
        myApp = MyApp.getInstance();
        mHandler_time = new Handler();
        parent = getActivity().getClass().getName();
        Log.i(TAG, "parent== " + parent);

        mtaskEntity = MyApp.getTaskEntity();
        id_param = mtaskEntity.getTaskId();//拿到本次测试的id
        initView();
        return view;
    }

    public void initView() {
        if (view != null) {
            rlTitle = view.findViewById(R.id.rl_title);
            frameLayout1 = view.findViewById(R.id.frameLayout1);
            frameLayout2 = view.findViewById(R.id.frameLayout2);
            speedYunxing = view.findViewById(R.id.speed_yunxing);
            ll1 = view.findViewById(R.id.ll1);
            speedMax = view.findViewById(R.id.speed_max);
            tvTime = view.findViewById(R.id.tv_time);

            initData();//初始化数据
            initTestData();//折线
        }
    }

    public static SpeedFragmentVp1 newInstance(int position) {
        SpeedFragmentVp1 fragment = new SpeedFragmentVp1();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        fragment.setArguments(bundle);
        return fragment;
    }


    //实时更新时间
    Runnable update_time = new Runnable() {
        @Override
        public void run() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String format = sdf.format(new Date());
            try {
                tvTime.setText(format);
            } catch (Exception e) {

            }
            // 延时1s后又将线程加入到线程队列中
            Message message = new Message();
            message.what = MSG_UPDATE_CURRENT_TIME;
            if (mHandler_time != null) {
                mHandler_time.sendMessage(message);
            } else {
                return;
            }
            mHandler_time.postDelayed(update_time, 1000);
        }
    };

    public void start() {//开始画图（外部调用）,开始画图前初始化图表
        y_speed.clear();
        jsonArray_set1 = null;
        speedYunxing.setText("0");
        speedMax.setText("0");
        initData();
        initTestData();
        mHandler_drawChart = new Handler();
        mHandler_drawChart.post(drawChart);
    }

    public void stop() {//停止画图（外部调用）
        if (mHandler_drawChart != null) {
            x_time = df2.format(addX1);
            jsonArray_set1 = new JSONArray();
            for (double b : y_speed) {//y_speed是本次测试所有的速度集合
                try {
                    jsonArray_set1.put(b);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Log.i("dfd", "x_time== " + x_time + "\n" + "jsonArray长度为" + jsonArray_set1.length() + "\n" + "jsonArray为：" + jsonArray_set1.toString());
            //停止时保存数据
            MyApp.SpeedListX = x_time;//运行时间
            MyApp.jsonArray_SpeedListY = jsonArray_set1;//给速度集合赋值
            MyApp.max_speed = max_speed;//最大速度

            mHandler_drawChart.removeCallbacks(drawChart);
            mHandler_drawChart.removeMessages(MSG_UPDATE_CHART);
            mHandler_drawChart = null;
        }
    }

    public void save1() {//保存，外部调用
        if (id_param == null || StringUtil.isEmpty(tvTime.getText().toString())
                || (speedYunxing.getText().toString().equals("0") && speedMax.getText().toString().equals("0"))//当速度和最大速度都为0时，提示保存失败
                ) {
            Toast.makeText(getActivity(), "id_param为空或者数据不全，保存失败！", Toast.LENGTH_SHORT).show();
        } else {
            speedEntity = new SpeedEntity();
            speedEntity.setKey(id_param);
            mtaskEntity.setIs_SpeedSave(true);
            TaskEntityUtils.update(mtaskEntity);
            speedEntity.setTime(tvTime.getText().toString());//保存的是点击保存时的时间
            speedEntity.setMaxSpeed(speedMax.getText().toString());//最大速度
            speedEntity.setRunTimeTotal(x_time);//运行时间
            Log.i(TAG, "运行时间====" + x_time);
            jsonArray_set1 = new JSONArray();
            for (double b : y_speed) {//y_speed是本次测试所有的速度集合
                try {
                    jsonArray_set1.put(b);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            //保存速度集合
            speedEntity.setPoint_speed(jsonArray_set1.toString());
            //保存速度数据
            myApp.getmDaoSession().getSpeedEntityDao().insert(speedEntity);
            //保存任务的id，以便在匀速分析和减速分析界面拿到速度实体类，能够更新
            MyApp.entityID = speedEntity.getId();
            Log.i("vh", "id== " + speedEntity.getId());
            Log.i("vh", "key== " + id_param);
            //截屏
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ScreenShot.getPic(rlTitle, "速度图_" + speedEntity.getId());
                }
            }, 1000);
        }
    }

    //折线
    public void initTestData() {
        series1 = new XYSeries("速度");
        //创建一个数据集的实例，这个数据集将被用来创建图表
        mDataset = new XYMultipleSeriesDataset();
        //将点集添加进这个数据集中
        mDataset.addSeries(0, series1);
        // 以下都是曲线的样式和属性等等的设置，renderer相当于一个用来给图表做渲染的句柄
        renderer = buildRendener();
        //设置好图表的样式
        setChartSettings(renderer);

        Log.i(TAG, "测试=====");
        mHandler_time.post(update_time);
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
                        if (seriesSelection != null) {
                            PopupView pv = new PopupView(getActivity());//浮窗
                            //设置浮窗的位置
                            pv.setStartX(event.getX());
                            pv.setStartY(event.getY());
                            if (seriesSelection.getSeriesIndex() == 0) {//第一条线
                                if (hm.get(String.format("%.1f", seriesSelection.getXValue())) != null) {
                                    PopupView pv2 = new PopupView(getActivity());//浮窗
                                    pv2.setStartX(event.getX());//设置浮窗的位置
                                    pv2.setStartY(event.getY() + 50);
                                    pv2.setValue(String.format("%.2f", hm.get(String.format("%.1f", seriesSelection.getXValue()))));
                                    pv2.setTextColor(getResources().getColor(R.color.color_text_press));
                                    // 设置浮窗上显示的数值
                                    pv.setValue(String.format("%.2f", seriesSelection.getValue()));
                                    frameLayout2.addView(pv2);
                                } else {
                                    // 设置浮窗上显示的数值
                                    pv.setValue(String.format("%.2f", seriesSelection.getValue()));
                                }
                                pv.setTextColor(getResources().getColor(R.color.red));
                            }
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


    private void showData(SpeedEntity speedEntity) {
        Log.i(TAG, "showdata---maxSpeed===" + speedEntity.getMaxSpeed());
        if (speedEntity != null) {
            tvTime.setText(speedEntity.getTime());
            speedMax.setText(speedEntity.getMaxSpeed());
        }
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
        renderer.setMargins(new int[]{20, 40, 10, 30});// 图形 4 边距上左下右
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
        renderer.setAntialiasing(true);//消除锯齿
        renderer.setFitLegend(true);//调节图例至适当位置
        // 设置点的缓冲半径值(在某点附近点击时,多大范围内算点击这个点)
        renderer.setSelectableBuffer(20);
        renderer.setZoomButtonsVisible(false);//设置显示放大缩小按钮
        renderer.setXLabelsColor(Color.BLACK);
        renderer.setLabelsColor(Color.BLACK);
        renderer.setYLabelsColor(0, Color.RED);
        renderer.setXAxisMin(0, 0);
        renderer.setXAxisMax(30, 0);
        renderer.setYAxisMax(50, 0);
        renderer.setYAxisMin(0, 0);
        renderer.setYLabelsAlign(Paint.Align.RIGHT, 0);
    }

    //曲线样式和属性设置
    private XYMultipleSeriesRenderer buildRendener() {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer(1);
        // 设置图表中曲线本身的样式，包括颜色、点的大小以及线的粗细等
        XYSeriesRenderer r = new XYSeriesRenderer();//第一条
        r.setColor(Color.RED);
        r.setPointStyle(PointStyle.CIRCLE);
        r.setFillPoints(true);
        r.setChartValuesSpacing(3);
        renderer.addSeriesRenderer(0, r);

        return renderer;
    }

    //初始化数据
    private void initData() {
        speedDataLists = new ArrayList<>();
        currentDataList = new ArrayList<>();
        y_speed = new ArrayList<Double>();
        y_current = new ArrayList<Double>();
        y_speed_f = new ArrayList<Double>();
        addX1 = 0;
        addX2 = 0;
        addY1 = 0;
        addY2 = 0;
        addY1_before = 0;
        f_count = 0;
        //
        max_speed = 0;
        max_speed_f = 0;
    }

    Runnable drawChart = new Runnable() {
        @Override
        public void run() {
            try {
                updateChart1(MyApp.comBeanSd);
            } catch (Exception e) {
                Log.i(TAG, "更新图表异常exceprion");
            }
            //延迟一秒后又将该线程加入到线程队列中
            Message message = new Message();
            message.what = MSG_UPDATE_CHART;
            if (mHandler_drawChart != null) {
                mHandler_drawChart.sendMessage(message);
            } else {
                return;
            }
            mHandler_drawChart.postDelayed(drawChart, 1000);
        }
    };

    //测试更新点
    public void updateChart1(ComBean ComRecData) {
        Message m = new Message();
        m.what = MSG_UPDATE_CURRENT_TIME;
        if (ComRecData.recData.length > 0 && ComRecData.recData != null && handler_updata != null) {
            m.obj = ComRecData.recData;
            handler_updata.sendMessage(m);
        }
    }

    Handler handler_updata = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_UPDATE_CURRENT_TIME && msg.obj != null) {
                buffer = (byte[]) msg.obj;//拿到串口数据
//                if (addX1 > 900) {
//                    handler_updata.removeMessages(MSG_UPDATE_CURRENT_TIME);
//                } else {
                // 接收到的数据
                if (buffer != null && buffer.length > 0) {
                    final int type = buffer[9] & 0xff;
                    switch (type) {
                        case 96://速度
                            //添加速度数据(添加前先清除)
                            if (buffer.length == 38) {
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
                                //找最大速度
                                int f = 0;
                                for (int i = 0; i < 10; i++) {
                                    //得到速度
                                    addY1 = (speedDataLists.get(i) * 3.1415f * d * 10) / 200;
                                    if (addY1 > max_speed) {
                                        max_speed = addY1;
                                    }

                                    if (addY1 > 0.03 && addY1_before == 0) {
                                        //速度开始划线条件  (运算之前是addY1 > 3)===0.02285
                                        start_bl = true;
                                    }
                                    if (start_bl) {
                                        if (addY1_before == 0 && addY1 == 0) {
                                            continue;
                                        }
                                        speedYunxing.setText(String.format("%.2f", addY1));//运行速度
                                        speedMax.setText(String.format("%.2f", max_speed));//最大速度
                                        renderer.setXAxisMin(0, 0);
                                        renderer.setXAxisMax(addX1 + 30, 0);
                                        renderer.setYAxisMin(0);
                                        renderer.setYAxisMax(max_speed * 3 / 2);
                                        //干嘛用的？
                                        f_count += addY1;
                                        f += addY1;
                                        y_speed.add(addY1);
                                        mDataset.removeSeries(series1);
                                        series1.add(addX1, addY1);
                                        mDataset.addSeries(0, series1);
                                        chart.invalidate();//重绘，更新视图
                                        addY1_before = addY1;
                                        addX1 = (float) (addX1 + 0.1);
                                    }
                                    //最大速度大于0.2并且现速度为0时，认为已经停止
                                    if (max_speed > 0.2 && addY1 == 0) {
                                        stop();
                                    }
                                }
                            }
                            break;
                        case 16://温湿度
                            break;
                    }
//                    }
                }
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handler_updata != null) {
            handler_updata.removeMessages(MSG_UPDATE_CURRENT_TIME);
            handler_updata = null;
        }
        if (mHandler_drawChart != null) {
            mHandler_drawChart.removeCallbacks(drawChart);
            mHandler_drawChart.removeMessages(MSG_UPDATE_CHART);
            mHandler_drawChart = null;
        }
        if (mHandler_time != null) {
            mHandler_time.removeCallbacks(update_time);
            mHandler_time.removeMessages(MSG_UPDATE_CURRENT_TIME);
            mHandler_time = null;
        }
    }
}

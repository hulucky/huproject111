package com.example.hu.huproject.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import com.example.hu.huproject.Adapter.TestItemAdapter;
import com.example.hu.huproject.Adapter.TestListAdapter;
import com.example.hu.huproject.Application.MyApp;
import com.example.hu.huproject.Bean.QjBean;
import com.example.hu.huproject.Entity.AngleEntity;
import com.example.hu.huproject.Entity.TaskEntity;
import com.example.hu.huproject.Interface.ISensorInf;
import com.example.hu.huproject.Listener.OnRecyclerViewItemClickListener;
import com.example.hu.huproject.R;
import com.example.hu.huproject.Utils.ScreenShot;
import com.example.hu.huproject.Utils.SerialControl;
import com.example.hu.huproject.Utils.TaskEntityUtils;
import com.example.hu.huproject.View.PopupView;
import com.example.hu.huproject.View.RecycleViewDivider;
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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


//角度测试的测试界面
public class AngleTestFragment extends ListFragment {

    @BindView(R.id.sv_speed)
    SensorView svSpeed;
    @BindView(R.id.sv_angle)
    SensorView svAngle;
    @BindView(R.id.sv_wenshidu)
    SensorView svWenshidu;
    @BindView(R.id.frameLayout1)
    FrameLayout frameLayout1;
    @BindView(R.id.frameLayout2)
    FrameLayout frameLayout2;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.linear_time)
    LinearLayout linearTime;
    @BindView(R.id.tv_yunxing_jiaodu)
    TextView tvYunxingJiaodu;
    @BindView(R.id.tv_zuida_angle)
    TextView tvZuidaAngle;
    @BindView(R.id.ll1)
    LinearLayout ll1;
    @BindView(R.id.rl1)
    RelativeLayout rl1;
    @BindView(R.id.btn_save)
    TextView btnSave;
    @BindView(R.id.btn_start)
    TextView btnStart;

    Unbinder unbinder;

    private MySerialControl comA;//串口
    private boolean isRunning = false;
    private QjBean qjBean;
    private Handler timeHandler;
    private Handler updateChartHandler;
    private XYMultipleSeriesDataset mDataset;
    private XYSeries series1;
    private GraphicalView chart;//图像视图
    private XYMultipleSeriesRenderer renderer;
    private DecimalFormat df = new DecimalFormat("0");
    private DecimalFormat df1 = new DecimalFormat("0.0");
    private DecimalFormat df2 = new DecimalFormat("0.00");
    private MyApp myApp;
    private TaskEntity taskEntity;
    private Long id_param;
    private float addX1, addY1, maxY1;
    private ArrayList<Float> yList = new ArrayList<>();
    private AngleEntity angleEntity;
    private JSONArray jsonArray_angleList;
    private boolean isAngleConnected;//角度传感器是否连接

    //实例化自己，可以从外部传参
    public static AngleTestFragment newInstance(int position) {
        AngleTestFragment testFragment = new AngleTestFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        testFragment.setArguments(bundle);
        return testFragment;
    }

    @Override
    protected void lazyLoadData() {

    }

    @Override
    protected void initView() {
        //初始化串口
        initEvent();
    }

    //初始化串口
    private void initEvent() {
        /*串口操作*/
        comA = new MySerialControl(getContext(), DataType.DATA_OK_PARSE);
        // 设置读取串口的时间间隔毫秒（大于0的整数），建议不少于50
        comA.setiDelay(50);
        //打开串口
        ComControl.OpenComPort(comA);
        svSpeed.setOnStatusChangeListener(new MyOnStatusChanger());
        svAngle.setOnStatusChangeListener(new MyOnStatusChanger());
        svWenshidu.setOnStatusChangeListener(new MyOnStatusChanger());
        timeHandler = new Handler();
        timeHandler.post(updateTime);
        initChart();
        myApp = MyApp.getInstance();
        taskEntity = MyApp.getTaskEntity();
        id_param = taskEntity.getTaskId();
        Log.d("ddq", "id_param: " + id_param);
    }

    Runnable updateChartRunnable = new Runnable() {
        @Override
        public void run() {
            updateChart(qjBean);
            if (updateChartHandler == null) {
                return;
            }
            updateChartHandler.postDelayed(updateChartRunnable, 1000);
        }
    };

    @SuppressLint("ClickableViewAccessibility")
    private void initChart() {
        addX1 = 0;
        addY1 = 0;
        maxY1 = 0;
        tvYunxingJiaodu.setText("");
        tvZuidaAngle.setText("");
        yList.clear();
        series1 = new XYSeries("角度");
        //创建一个数据集的实例，这个数据集将被用来创建图表
        mDataset = new XYMultipleSeriesDataset();
        //将点集添加进这个数据集中
        mDataset.addSeries(0, series1);
        // 以下都是曲线的样式和属性等等的设置，renderer相当于一个用来给图表做渲染的句柄
        renderer = buildRendener();
        //设置好图表的样式
        setChartSettings(renderer);

        chart = ChartFactory.getCubeLineChartView(getActivity(), mDataset, renderer, 0.1f);
        //生成图表(chart----GraphicalView图像视图)
        chart.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("DefaultLocale")
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

    private XYMultipleSeriesRenderer buildRendener() {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer(1);
        // 设置图表中曲线本身的样式，包括颜色、点的大小以及线的粗细等
        XYSeriesRenderer r = new XYSeriesRenderer();//第一条
        r.setColor(Color.RED);
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
//        renderer.setPanLimits(new double[]{0, 10000, 0, 10000});//设置拉动的范围
        renderer.setZoomEnabled(true);//设置渲染器允许放大缩小
//        renderer.setZoomLimits(new double[]{0, 10000, 0, 10000});//设置缩放的范围
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

        renderer.setYLabelsAlign(Paint.Align.RIGHT, 0);
    }


    private Runnable updateTime = new Runnable() {
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


    public class MySerialControl extends SerialHelper {

        public MySerialControl(Context context, int mDataType) {
            super(context, mDataType);
        }

        public MySerialControl(Context context, String sPort, String sBaudRate, int mDataType) {
            super(context, sPort, sBaudRate, mDataType);
        }

        @Override
        protected void onDataReceived(final ComBean comBean) {
            switch (comBean.recDataType) {
                case -94://倾角传感器
                    isAngleConnected = true;
                    if (comBean.recData.length == 24) {
                        Log.i("dds", "倾角数据为: " + Arrays.toString(comBean.recData));
                        if (qjBean == null) {
                            qjBean = new QjBean(comBean.recData);
                        } else {
                            qjBean.caculate(comBean.recData);
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                SensorData sensorData = new SensorData(qjBean.getPower(), qjBean.getSignal()
                                        , SensorInf.NORMAL, System.currentTimeMillis());
                                svAngle.setData(sensorData);
                            }
                        });
                    }
                    break;
            }
        }
    }

    private void updateChart(QjBean qjBean) {
        addY1 = qjBean.yqj;//只拿y轴的部分
        if (Math.abs(addY1) > maxY1) {
            maxY1 = Math.abs(addY1);
        }
        tvYunxingJiaodu.setText(addY1 + "");
        tvZuidaAngle.setText(maxY1 + "");
        renderer.setXAxisMin(0, 0);
        renderer.setXAxisMax(addX1 + 30, 0);
        renderer.setYAxisMin(-maxY1 * 3 / 2);
        renderer.setYAxisMax(maxY1 * 3 / 2);
        yList.add(addY1);
        mDataset.removeSeries(series1);
        series1.add(addX1, addY1);
        mDataset.addSeries(series1);
        chart.invalidate();
        addX1 = addX1 + 1;
    }


    @Override
    protected int setLayoutId() {
        return R.layout.fragment_angle_test;
    }


    @OnClick({R.id.btn_start, R.id.btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_start:
                if (isAngleConnected) {
                    if (isRunning) {//如果正在运行
                        btnStart.setText("开始");
                        isRunning = false;
                        if (updateChartHandler != null) {
                            updateChartHandler.removeCallbacks(updateChartRunnable);
                            updateChartHandler = null;
                        }
                    } else {//开始
                        btnStart.setText("停止");
                        isRunning = true;
                        if (updateChartHandler == null) {
                            initChart();
                            updateChartHandler = new Handler();
                            updateChartHandler.post(updateChartRunnable);//开始一秒画一次图
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "角度传感器尚未连接！", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.btn_save://保存
                if (isAngleConnected) {
                    if (isRunning) {
                        Toast.makeText(getContext(), "请停止测试后再保存", Toast.LENGTH_SHORT).show();
                    } else {
                        if (id_param == null) {
                            Toast.makeText(getActivity(), "id_param为空，保存失败！", Toast.LENGTH_SHORT).show();
                        } else {
                            angleEntity = new AngleEntity();
                            angleEntity.setKey(id_param);
                            taskEntity.setIs_AngleSave(true);
                            TaskEntityUtils.update(taskEntity);

                            angleEntity.setCreateTime(tvTime.getText().toString());//创建时间
                            angleEntity.setMaxAngle(df2.format(maxY1));//最大角度
                            angleEntity.setRunTime(df.format(addX1));//运行时间
                            jsonArray_angleList = new JSONArray();
                            for (float b : yList) {
                                try {
                                    jsonArray_angleList.put(b);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            angleEntity.setPointAngle(jsonArray_angleList.toString());
                            Log.i("ddh", "addX1: " + addX1 + "\n" + "maxY1: " + maxY1 + "\n"
                                    + "yList: " + yList.toString() + "\n" + "jsonArray_angleList： " + jsonArray_angleList.toString());

                            myApp.getmDaoSession().getAngleEntityDao().insert(angleEntity);
                            Toast toast = Toast.makeText(getActivity(),
                                    "保存成功！", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);// 居中显示
                            toast.show();

                            //截屏
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ScreenShot.getPic(rl1, "角度测试图_" + angleEntity.getId());
                                }
                            }, 500);
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "角度传感器尚未连接！", Toast.LENGTH_SHORT).show();

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
                    case R.id.sv_speed:
                        Toast.makeText(getContext(), "速度传感器断开", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.sv_angle:
                        isAngleConnected = false;
                        Toast.makeText(getContext(), "倾角传感器断开", Toast.LENGTH_SHORT).show();
                        btnStart.setText("开始");
                        isRunning = false;
                        if (updateChartHandler != null) {
                            updateChartHandler.removeCallbacks(updateChartRunnable);
                            updateChartHandler = null;
                        }
                        break;
                    case R.id.sv_wenshidu:
                        Toast.makeText(getContext(), "温湿度传感器断开", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        svSpeed.destroy();
        svAngle.destroy();
        svWenshidu.destroy();
        comA.close();
        if (timeHandler != null) {
            timeHandler.removeCallbacks(updateTime);
            timeHandler = null;
        }
        if (updateChartHandler != null) {
            updateChartHandler.removeCallbacks(updateChartRunnable);
            updateChartHandler = null;
        }
    }
}

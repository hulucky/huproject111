package com.example.hu.huproject.Fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hu.huproject.Application.MyApp;
import com.example.hu.huproject.Dialog.YunSuDialogs;
import com.example.hu.huproject.Entity.SpeedEntity;
import com.example.hu.huproject.Listener.OnDialogDismissListener;
import com.example.hu.huproject.R;
import com.example.hu.huproject.Utils.DBHelper;
import com.example.hu.huproject.Utils.ScreenShot;
import com.example.hu.huproject.View.PopupView;
import com.mchsdk.paysdk.mylibrary.ListFragment;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

//速度==》》匀速分析
public class SpeedFragmentVp2 extends ListFragment {
    @BindView(R.id.linear_title)
    LinearLayout linearTitle;
    @BindView(R.id.lineartext)
    LinearLayout linearText;
    @BindView(R.id.frameLayout1)
    FrameLayout frameLayout1;
    @BindView(R.id.frameLayout2)
    FrameLayout frameLayout2;
    @BindView(R.id.view1)
    View view1;
    @BindView(R.id.view2)
    View view2;
    @BindView(R.id.speed_fl_chart)
    RelativeLayout speedFlChart;
    @BindView(R.id.tv_average_speed)
    TextView tvAverageSpeed;
    @BindView(R.id.tv_run_time)
    TextView tvRunTime;

    /**
     * 速度图
     */
    private GraphicalView speed_chart;
    private XYMultipleSeriesDataset speed_mDataset;
    private XYSeries speed_series1;
    private XYMultipleSeriesRenderer speed_renderer;
    private double speed_addY1;//单点速度
    private double totalSpeed = 0;//速度总和，除以运行时间就是平均速度
    private JSONArray speed_jsonArray_get1;
    private String runTime;//总运行时间
    private double max_speed;
    private String startTime;
    private String endTime;
    private double runTimeAverage;//用户选段的运行时间 double


    private DecimalFormat df2 = new DecimalFormat("#.##");//保留两位小数
    private DecimalFormat df1 = new DecimalFormat("#.#");//保留一位小数
    private DBHelper instance;
    private String runtime_str;//用户选段的运行时间 string
    private String averageSpeed;//平均速度

    public static SpeedFragmentVp2 newInstance(int position) {
        SpeedFragmentVp2 fragment = new SpeedFragmentVp2();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        fragment.setArguments(bundle);
        return fragment;
    }

    private OnDialogDismissListener listener = new OnDialogDismissListener() {
        @Override
        public void onDialogDismiss() {
            startTime = MyApp.startTime;
            endTime = MyApp.endTime;
            runTimeAverage = Double.valueOf(endTime) - Double.valueOf(startTime);//运行时间
            runtime_str = df1.format(runTimeAverage);

            tvRunTime.setText(runtime_str);
            //判断当起点和终点都有数据时，才展示图表
            if (startTime != null && !startTime.equals("") & endTime != null && !endTime.equals("")) {
                initspeedData();
            } else {
                Log.i("vp2", "起点和终点未选择数据");
            }
        }
    };

    @Override
    protected void lazyLoadData() {
        if (MyApp.SpeedListX == null || MyApp.SpeedListX.equals("")) {//判断运行时间是否为空
            Toast.makeText(getContext(), "暂无数据", Toast.LENGTH_SHORT).show();
            return;
        }
        //显示dialog
        YunSuDialogs yunSuDialogs = new YunSuDialogs();
        yunSuDialogs.setOnClickListener(listener);//dialog消失时调用的接口
        yunSuDialogs.show(getChildFragmentManager(), "yunsu");

        //数据集合
        runTime = MyApp.SpeedListX;
        max_speed = MyApp.max_speed;
    }


    @Override
    protected void initView() {
        instance = DBHelper.getInstance(getContext());
    }

    //初始化速度电流
    private void initspeedData() {
        totalSpeed = 0;
        speed_series1 = new XYSeries("速度");
        // 创建一个数据集的实例，这个数据集将被用来创建图表
        speed_mDataset = new XYMultipleSeriesDataset();
        // 以下都是曲线的样式和属性等等的设置，renderer相当于一个用来给图表做渲染的句柄
        speed_renderer = speedBuildRenderer();
        // 设置好图表的样式
        speedSetChartSettings(speed_renderer);
        speed_chart = ChartFactory.getCubeLineChartView(getContext(), speedUpdateChart(),
                speed_renderer, 0.1f);
        speed_chart.setOnHoverListener(new View.OnHoverListener() {
            @Override
            public boolean onHover(View v, MotionEvent event) {
                int what = event.getAction();
                switch (what) {
                    case MotionEvent.ACTION_HOVER_ENTER:  //鼠标进入view
                        view1.setVisibility(View.VISIBLE);
                        break;
                    case MotionEvent.ACTION_HOVER_MOVE:  //鼠标在view上
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view1.getLayoutParams();
                        params.setMargins((int) event.getX(), 0, 0, 0);// 通过自定义坐标来放置你的控件
                        view1.setLayoutParams(params);
                        break;
                    case MotionEvent.ACTION_HOVER_EXIT:  //鼠标离开view
                        view1.setVisibility(View.GONE);
                        break;
                }
                return false;
            }
        });
        // 生成图表
        speed_chart.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        view1.setVisibility(View.GONE);
                        frameLayout2.removeAllViews();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        SeriesSelection seriesSelection = speed_chart
                                .getCurrentSeriesAndPoint();// 获取当前点击的点的相关信息
                        if (seriesSelection != null
                                && seriesSelection.getSeriesIndex() != 2) {
                            PopupView pv = new PopupView(getActivity()); // 浮窗
                            pv.setStartX(event.getX()); // 设置浮窗的位置
                            pv.setStartY(event.getY());
                            Log.i("dialog", "event.getX():" + event.getX() + "     event.getY()" + event.getY()
                                    + "\n" + "seriesSelection.getXValue()" + seriesSelection.getXValue()
                                    + "            seriesSelection.getValue()" + seriesSelection.getValue()
                                    + "\n" + "seriesSelection.getSeriesIndex()" + seriesSelection.getSeriesIndex());
                            pv.setValue(String.format("%.2f", seriesSelection.getValue())); // 设置浮窗上显示的数值
                            frameLayout2.addView(pv); // 将浮窗添加到Framelayout
                            //设置计算加速度时的初速度、末速度
                            if (seriesSelection.getSeriesIndex() == 0) {
                                //画竖线
                                view2.setVisibility(View.VISIBLE);
                                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view2.getLayoutParams();
                                params.setMargins((int) event.getX(), 0, 0, 0);// 通过自定义坐标来放置你的控件
                                view2.setLayoutParams(params);

                            }
                        }
                        break;
                }
                return false;
            }
        });
        //平均速度
        averageSpeed = df2.format((totalSpeed / runTimeAverage) / 10);
        tvAverageSpeed.setText(averageSpeed);
        // 将图表添加到布局中去
        frameLayout1.addView(speed_chart);
        //更新数据库
        final SpeedEntity speedEntity = instance.querySpeedById(MyApp.entityID);
        Log.i("vh", "id== " + MyApp.entityID);
        if (speedEntity != null) {
            speedEntity.setYunsu_startTime(startTime);//匀速运动起点
            speedEntity.setYunsu_endTime(endTime);//匀速运动末点
            speedEntity.setYunsu_runTime(runtime_str);//匀速运动时间
            speedEntity.setYunsu_averageSpeed(averageSpeed);//匀速运动平均速度
            instance.updateSpeed(speedEntity);
            //截屏
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ScreenShot.getPic(linearTitle, "速度匀速分析图_" + speedEntity.getId());
                }
            }, 500);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        runTime = "";
        max_speed = 0;
        startTime = "";
        endTime = "";
        runtime_str = "";
        averageSpeed = "";
    }

    /**
     * 截屏逻辑：保存时保存三张图片（不管匀速或者减速有没有选点）
     * 匀速分析或者减速分析后都会自动保存图片（图片会覆盖）
     */
    public void saveYunsu() {
        //更新数据库
        final SpeedEntity speedEntity = instance.querySpeedById(MyApp.entityID);
        Log.i("vh", "id== " + MyApp.entityID);
        if (speedEntity != null) {
            speedEntity.setYunsu_startTime(startTime);//匀速运动起点
            speedEntity.setYunsu_endTime(endTime);//匀速运动末点
            speedEntity.setYunsu_runTime(runtime_str);//匀速运动时间
            speedEntity.setYunsu_averageSpeed(averageSpeed);//匀速运动平均速度
            instance.updateSpeed(speedEntity);
            //截屏
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ScreenShot.getPic(linearTitle, "速度匀速分析图_" + speedEntity.getId());
                }
            }, 500);
        }
    }


    protected XYMultipleSeriesRenderer speedBuildRenderer() {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer(1);
        // 设置图表中曲线本身的样式，包括颜色、点的大小以及线的粗细等
        XYSeriesRenderer r = new XYSeriesRenderer();// 第一条
        r.setColor(Color.RED);
        r.setPointStyle(PointStyle.CIRCLE);
        r.setFillPoints(true);
        r.setChartValuesSpacing(3);
        renderer.addSeriesRenderer(r);

        return renderer;
    }

    protected void speedSetChartSettings(XYMultipleSeriesRenderer renderer) {
        // 有关对图表的渲染可参看api文档
        renderer.setApplyBackgroundColor(true);// 设置是否显示背景色
        renderer.setBackgroundColor(Color.WHITE);// 设置背景色
        renderer.setAxesColor(Color.WHITE);
        renderer.setShowGrid(false);
        renderer.setXLabels(12);
        renderer.setYLabels(10);
        renderer.setXTitle("时间(s)");
        renderer.setMargins(new int[]{20, 40, 10, 20}); // 图形 4 边距上左下右
        renderer.setPointSize(0f);// 设置点的大小(图上显示的点的大小和图例中点的大小都会被设置)
        renderer.setShowLegend(true);
        renderer.setMarginsColor(Color.parseColor("#eeeeee"));
        renderer.setAxesColor(Color.parseColor("#000000"));
        renderer.setChartTitleTextSize(20);
        renderer.setAxisTitleTextSize(16); // 设置轴标题文字的大小
        renderer.setChartTitleTextSize(20);// 设置整个图表标题文字大小
        renderer.setLabelsTextSize(15);// 设置刻度显示文字的大小(XY轴都会被设置)
        renderer.setLegendTextSize(15);// 图例文字大小
        renderer.setAntialiasing(true);
        renderer.setFitLegend(true);// 调节图例至适当位置
        renderer.setSelectableBuffer(20);// 设置点的缓冲半径值(在某点附近点击时,多大范围内算点击这个点)
        renderer.setPanEnabled(true, true);// 可上下拖动，可水平拖动
        renderer.setPanLimits(new double[]{0, 10000, 0, 10000});//设置拉动的范围
        renderer.setZoomEnabled(true);//设置渲染器允许放大缩小
        renderer.setZoomLimits(new double[]{0, 10000, 0, 10000});//设置缩放的范围
        renderer.setZoomButtonsVisible(false);//设置显示放大缩小按钮
        renderer.setXLabelsColor(Color.BLACK);
        renderer.setLabelsColor(Color.BLACK);
        renderer.setYLabelsColor(0, Color.RED);
        renderer.setXAxisMin(Double.parseDouble(startTime), 0);
        renderer.setXAxisMax(Double.parseDouble(endTime), 0);
        renderer.setYAxisMin(0);
        renderer.setYAxisMax(max_speed * 3 / 2);
        renderer.setYLabelsAlign(Paint.Align.LEFT);
    }

    /**
     * 查看结果更新点
     */

    public XYMultipleSeriesDataset speedUpdateChart() {
        int startTimeDouble = (int) (Double.parseDouble(startTime) * 10);
        int endTimeDouble = (int) (Double.parseDouble(endTime) * 10);
        Log.i("vp2", "startTimeDouble: " + startTimeDouble + "\n" + "endTimeDouble:" + endTimeDouble);
        // 同样是需要数据dataset和视图渲染器renderer
        XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
        // 设置好下一个需要增加的节点
        try {
            if (MyApp.jsonArray_SpeedListY != null && MyApp.jsonArray_SpeedListY.toString().length() > 0) {
                //拿到保存的速度数组
                speed_jsonArray_get1 = new JSONArray(MyApp.jsonArray_SpeedListY.toString());
                Log.i("vp2", "speed_jsonArray_get1长度为: " + speed_jsonArray_get1.length());
                Log.i("vp2", "runtime===" + runTime);
            }

            if (speed_jsonArray_get1 != null && speed_jsonArray_get1.length() > 0) {
                for (int i = startTimeDouble + 1; i < speed_jsonArray_get1.length(); i++) {
                    if (i > endTimeDouble) {
                        break;
                    }
                    //得到单点速度
                    speed_addY1 = speed_jsonArray_get1.getDouble(i);
                    //求出选中段的速度总和
                    totalSpeed += speed_addY1;
                    //设置速度数据源
                    speed_series1.add((Double.parseDouble(runTime) / speed_jsonArray_get1.length()) * i, speed_addY1);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mDataset.addSeries(speed_series1);
        return mDataset;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_speedanglevp2;
    }


    @OnClick({R.id.tv_average_speed, R.id.tv_run_time})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_average_speed:
                break;
            case R.id.tv_run_time:
                break;
        }
    }
}

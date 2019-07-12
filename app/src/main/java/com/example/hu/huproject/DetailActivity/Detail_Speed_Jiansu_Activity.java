package com.example.hu.huproject.DetailActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hu.huproject.Dialog.CxFenxiJiansuDialogs;
import com.example.hu.huproject.Entity.SpeedEntity;
import com.example.hu.huproject.Listener.OnDialogDismissListener;
import com.example.hu.huproject.R;
import com.example.hu.huproject.Utils.DBHelper;
import com.example.hu.huproject.Utils.ScreenShot;
import com.example.hu.huproject.View.PopupView;

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

public class Detail_Speed_Jiansu_Activity extends AppCompatActivity {
    @BindView(R.id.frameLayout1)
    FrameLayout frameLayout1;
    @BindView(R.id.frameLayout2)
    FrameLayout frameLayout2;
    @BindView(R.id.view1)
    View view1;
    @BindView(R.id.view2)
    View view2;
    @BindView(R.id.fl_chart)
    RelativeLayout flChart;
    @BindView(R.id.tv_average_jian_speed)
    TextView tvAverageJianSpeed;
    @BindView(R.id.tv_jiansu_time)
    TextView tvJiansuTime;
    @BindView(R.id.linearLayout1)
    LinearLayout linearLayout1;
    @BindView(R.id.tv_chu_speed)
    TextView tvChuSpeed;
    @BindView(R.id.tv_mo_sudu)
    TextView tvMoSudu;
    @BindView(R.id.linearLayout2)
    LinearLayout linearLayout2;
    @BindView(R.id.tv_max_speed)
    TextView tvMaxSpeed;
    @BindView(R.id.tv_min_sudu)
    TextView tvMinSudu;
    @BindView(R.id.linearLayout3)
    LinearLayout linearLayout3;
    @BindView(R.id.tv_cxFenXi)
    TextView tvCxFenXi;
    @BindView(R.id.linear_jieTu)
    LinearLayout linearJieTu;

    private DBHelper dbHelper;

    private GraphicalView speed_chart;
    private XYMultipleSeriesDataset speed_mDataset;
    private XYSeries speed_series1;
    private XYMultipleSeriesRenderer speed_renderer;
    private double speed_addY1;//单点速度
    //    private double totalSpeed = 0;//速度总和，除以运行时间就是平均速度
    private JSONArray speed_jsonArray_get1;
    private Context instance;
    private SpeedEntity speedEntity;

    private DecimalFormat df2 = new DecimalFormat("#.##");//保留两位小数
    private DecimalFormat df1 = new DecimalFormat("#.#");//保留一位小数
    private double jiansu_starttime;
    private double jiansu_endtime;
    private String jiansu_average_jiansudu;
    private String jiansu_time;
    private String jiansu_chusudu;
    private String jiansu_mosudu;
    private String jiansu_maxspeed;
    private String jiansu_minspeed;
    private double maxSpeed;
    private double runTimeTotal;
    private long id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail__speed__jiansu_);
        instance = this;
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        dbHelper = DBHelper.getInstance(this);
        id = getIntent().getLongExtra("id", -1);
        speedEntity = dbHelper.querySpeedById(id);
        //用户在点击保存之前匀速分析或者减速分析都不会保存，
        // 须保存之后，再进行匀速或者减速分析，才会被保存，这里判断能否取值
        if (speedEntity.getJiansu_startTime() != null && speedEntity.getJiansu_endTime() != null
                && speedEntity.getJiansu_avegare_jiansudu() != null
                && speedEntity.getJiansu_time() != null
                && speedEntity.getJiansu_chusudu() != null && speedEntity.getJiansu_mosudu() != null
                && speedEntity.getJiansu_maxspeed() != null && speedEntity.getJiansu_minspeed() != null) {

            jiansu_starttime = Double.parseDouble(speedEntity.getJiansu_startTime());
            jiansu_endtime = Double.parseDouble(speedEntity.getJiansu_endTime());
            jiansu_average_jiansudu = speedEntity.getJiansu_avegare_jiansudu();
            jiansu_time = speedEntity.getJiansu_time();
            jiansu_chusudu = speedEntity.getJiansu_chusudu();
            jiansu_mosudu = speedEntity.getJiansu_mosudu();
            jiansu_maxspeed = speedEntity.getJiansu_maxspeed();
            jiansu_minspeed = speedEntity.getJiansu_minspeed();
            maxSpeed = Double.parseDouble(speedEntity.getMaxSpeed());
            runTimeTotal = Double.parseDouble(speedEntity.getRunTimeTotal());

            initspeedData();//展示数据
        } else {
            Toast.makeText(instance, "没有数据！", Toast.LENGTH_SHORT).show();
        }
    }

    //展示数据
    private void initspeedData() {
        speed_series1 = new XYSeries("速度");
        // 创建一个数据集的实例，这个数据集将被用来创建图表
        speed_mDataset = new XYMultipleSeriesDataset();
        // 以下都是曲线的样式和属性等等的设置，renderer相当于一个用来给图表做渲染的句柄
        speed_renderer = speedBuildRenderer();
        // 设置好图表的样式
        speedSetChartSettings(speed_renderer);
        speed_chart = ChartFactory.getCubeLineChartView(instance, speedUpdateChart(),
                speed_renderer, 0.1f);

        tvAverageJianSpeed.setText(jiansu_average_jiansudu);
        tvJiansuTime.setText(jiansu_time);
        tvChuSpeed.setText(jiansu_chusudu);
        tvMoSudu.setText(jiansu_mosudu);
        tvMaxSpeed.setText(jiansu_maxspeed);
        tvMinSudu.setText(jiansu_minspeed);

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
                            PopupView pv = new PopupView(instance); // 浮窗
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

        // 将图表添加到布局中去
        frameLayout1.addView(speed_chart);
    }


    //设置曲线样式
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

    //设置好图表的样式
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
        renderer.setPanEnabled(true, true);// 可上下拖动，可水平拖动
        renderer.setPanLimits(new double[]{0, 10000, 0, 10000});
        renderer.setAntialiasing(true);
        renderer.setFitLegend(true);// 调节图例至适当位置
        renderer.setSelectableBuffer(20);// 设置点的缓冲半径值(在某点附近点击时,多大范围内算点击这个点)
        renderer.setZoomEnabled(true);// 设置渲染器允许放大缩小
        renderer.setZoomLimits(new double[]{0, 10000, 0, 10000});
        renderer.setZoomButtonsVisible(false);//设置显示放大缩小按钮

        renderer.setYLabelsColor(0, Color.RED);
        renderer.setXLabelsColor(Color.BLACK);
        renderer.setLabelsColor(Color.BLACK);
        renderer.setXAxisMin(jiansu_starttime, 0);

        renderer.setXAxisMax(jiansu_endtime, 0);
        renderer.setYAxisMin(0);

        renderer.setYAxisMax(maxSpeed * 3 / 2);
        renderer.setYLabelsAlign(Paint.Align.LEFT);
    }

    //设置好图表的数据源
    public XYMultipleSeriesDataset speedUpdateChart() {
        int startTimeDouble = (int) (jiansu_starttime * 10);
        int endTimeDouble = (int) (jiansu_endtime * 10);
        Log.i("vp2", "startTimeDouble: " + startTimeDouble + "\n" + "endTimeDouble:" + endTimeDouble);
        // 同样是需要数据dataset和视图渲染器renderer
        XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
        // 设置好下一个需要增加的节点
        try {
            //拿到保存的速度数组
            speed_jsonArray_get1 = new JSONArray(speedEntity.getPoint_speed());
            if (speed_jsonArray_get1.length() > 0) {
                for (int i = startTimeDouble; i < speed_jsonArray_get1.length(); i++) {
                    if (i > endTimeDouble) {
                        break;
                    }
                    //得到单点速度
                    speed_addY1 = speed_jsonArray_get1.getDouble(i);
                    //设置速度数据源
                    speed_series1.add((runTimeTotal / speed_jsonArray_get1.length()) * i, speed_addY1);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mDataset.addSeries(speed_series1);
        return mDataset;
    }

    @OnClick(R.id.tv_cxFenXi)
    public void onViewClicked() {
        //显示dialog
        CxFenxiJiansuDialogs dialogs = new CxFenxiJiansuDialogs();
        dialogs.setOnClickListener(listener);//dialog消失时调用的接口
        Bundle bundle = new Bundle();//创建dialog时，向其传递speedEntity的id
        bundle.putLong("id", id);
        dialogs.setArguments(bundle);
        dialogs.show(getSupportFragmentManager(), "CxFenxiJianSu");
    }

    //dialog消失时调用
    private OnDialogDismissListener listener = new OnDialogDismissListener() {
        @Override
        public void onDialogDismiss() {
            jiansu_starttime = Double.parseDouble(speedEntity.getJiansu_startTime());
            jiansu_endtime = Double.parseDouble(speedEntity.getJiansu_endTime());
            jiansu_average_jiansudu = speedEntity.getJiansu_avegare_jiansudu();
            jiansu_time = speedEntity.getJiansu_time();
            jiansu_chusudu = speedEntity.getJiansu_chusudu();
            jiansu_mosudu = speedEntity.getJiansu_mosudu();
            jiansu_maxspeed = speedEntity.getJiansu_maxspeed();
            jiansu_minspeed = speedEntity.getJiansu_minspeed();
            maxSpeed = Double.parseDouble(speedEntity.getMaxSpeed());
            runTimeTotal = Double.parseDouble(speedEntity.getRunTimeTotal());

            initspeedData();//展示数据

            //截屏
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ScreenShot.getPic(linearJieTu, "速度减速分析图_" + speedEntity.getId());
                }
            }, 500);
        }
    };
}

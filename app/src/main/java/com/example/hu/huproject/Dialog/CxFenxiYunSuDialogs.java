package com.example.hu.huproject.Dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hu.huproject.Entity.SpeedEntity;
import com.example.hu.huproject.Listener.OnDialogDismissListener;
import com.example.hu.huproject.R;
import com.example.hu.huproject.Utils.DBHelper;
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

import java.lang.reflect.Method;
import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//重新分析匀速dialog
public class CxFenxiYunSuDialogs extends DialogFragment {

    @BindView(R.id.tv_average_speed)
    TextView tvAverageSpeed;
    @BindView(R.id.tv_run_time)
    TextView tvRunTime;
    @BindView(R.id.frameLayout1)
    FrameLayout frameLayout1;
    @BindView(R.id.frameLayout2)
    FrameLayout frameLayout2;
    @BindView(R.id.frameLayout3)
    FrameLayout frameLayout3;
    @BindView(R.id.view1)
    View view1;
    @BindView(R.id.view2)
    View view2;
    @BindView(R.id.view3)
    View view3;
    @BindView(R.id.et_begin)
    EditText etBegin;
    @BindView(R.id.linear_begin)
    LinearLayout linearBegin;
    @BindView(R.id.et_end)
    EditText etEnd;
    @BindView(R.id.linear_end)
    LinearLayout linearEnd;
    @BindView(R.id.btn_sure)
    Button btnSure;
    @BindView(R.id.speed_fl_chart)
    RelativeLayout speedFlChart;

    private Activity instance;
    private GraphicalView speed_Chart;
    private XYMultipleSeriesDataset speed_mDataSet;
    private XYSeries speed_series1;
    private XYSeries speed_series2;
    private XYSeries speed_series3;
    private XYMultipleSeriesRenderer speed_render;
    private double speed_addY1;//速度集合
    private double speed_linshi;//临时用于显示的速度集合
    private JSONArray speed_jsonArray_get1;
    private String runTime;
    private double max_speed;
    //计算
    private boolean speedBeginFocus;
    private boolean speedEndFocus;
    //点击选择的起点和终点
    private double start_x1;
    private double end_x1;
    private DecimalFormat df1 = new DecimalFormat("0.0");
    private DecimalFormat df2 = new DecimalFormat("0.00");
    private double totalSpeed = 0;
    private DBHelper dbHelper;
    private SpeedEntity speedEntity;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        instance = getActivity();
        //// 使用不带Theme的构造器, 获得的dialog边框距离屏幕仍有几毫米的缝隙。
        Dialog dialog = new Dialog(instance, R.style.BottomDialog);

        Bundle bundle = getArguments();
        long id = bundle.getLong("id", -1);
        dbHelper = DBHelper.getInstance(instance);
        speedEntity = dbHelper.querySpeedById(id);


        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_yunsu);
        dialog.setCanceledOnTouchOutside(false);//外部点击取消(点击确定使dialog消失的时候回调接口)
        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setSoftInputMode(lp.SOFT_INPUT_STATE_ALWAYS_HIDDEN);//始终隐藏软键盘
        lp.gravity = Gravity.CENTER;//居中显示
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;//宽度持平
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;//宽度持
        window.setAttributes(lp);

        ButterKnife.bind(this, dialog);
        //软键盘监听
        initListener();
        //开始画图
        initChartView();
        return dialog;
    }

    private void initListener() {
        //屏蔽软键盘
        if (Build.VERSION.SDK_INT <= 10) {//4.0以下
            etBegin.setInputType(InputType.TYPE_NULL);
            etEnd.setInputType(InputType.TYPE_NULL);
        } else {
            try {
                Class<EditText> cls = EditText.class;
                Method setShowSoftInputOnFocus;
                setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(etBegin, false);
                setShowSoftInputOnFocus.invoke(etEnd, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //起点
        etBegin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                    speedBeginFocus = true;
                } else {
                    // 此处为失去焦点时的处理内容
                    speedBeginFocus = false;
                }
            }
        });
        //终点
        etEnd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                    speedEndFocus = true;
                } else {
                    // 此处为失去焦点时的处理内容
                    speedEndFocus = false;
                }
            }
        });
    }

    private void initChartView() {
        //数据集合(运行时间)
        runTime = speedEntity.getRunTimeTotal();
//        speed_jsonArray_get1 = MyApp.jsonArray_SpeedListY;
        max_speed = Double.parseDouble(speedEntity.getMaxSpeed());
        Log.i("dialog", "runTime:" + runTime + "\n" + "max_speed:" + max_speed);

        speed_series1 = new XYSeries("速度");
        speed_series2 = new XYSeries("起点",1);
        speed_series3 = new XYSeries("末点",2);
        // 创建一个数据集的实例，这个数据集将被用来创建图表
        speed_mDataSet = new XYMultipleSeriesDataset();
        speedUpdateChart();
        speed_mDataSet.addSeries(1,speed_series2);
        speed_mDataSet.addSeries(2,speed_series3);
        // 以下都是曲线的样式和属性等等的设置，renderer相当于一个用来给图表做渲染的句柄
        speed_render = speedBuildRenderer();
        // 设置好图表的样式
        speedSetChartSettings(speed_render);
        speed_Chart = ChartFactory.getCubeLineChartView(instance, speed_mDataSet, speed_render, 0.1f);
        //悬停事件监听
        speed_Chart.setOnHoverListener(new View.OnHoverListener() {
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
        //点击事件
        speed_Chart.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        view1.setVisibility(View.GONE);
                        if (speedBeginFocus) {
                            frameLayout2.removeAllViews();
                        }
                        if (speedEndFocus) {
                            frameLayout3.removeAllViews();
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        SeriesSelection seriesSelection = speed_Chart.getCurrentSeriesAndPoint();// 获取当前点击的点的相关信息
                        if (seriesSelection != null) {
                            //画竖线
                            view2.setVisibility(View.VISIBLE);
                            view3.setVisibility(View.VISIBLE);
                            if (speedBeginFocus) {
                                PopupView pv = new PopupView(instance);//浮窗
                                pv.setStartX(event.getX());
                                pv.setStartY(event.getY());
                                // 设置浮窗上显示的数值
                                pv.setValue(String.format("%.2f", seriesSelection.getValue()));
                                frameLayout2.addView(pv);// 将浮窗添加到Framelayout

//                                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view2.getLayoutParams();
//                                params.setMargins((int) event.getX(), 0, 0, 0);// 通过自定义坐标来放置你的控件
//                                view2.setLayoutParams(params);
                                start_x1 = seriesSelection.getXValue();
                                etBegin.setText(String.format("%.1f", start_x1));
                                speed_mDataSet.removeSeries(speed_series2);
                                speed_series2.clear();
                                speed_series2.add(start_x1, max_speed * 3 / 2);
                                speed_series2.add(start_x1, seriesSelection.getValue());
                                speed_series2.add(start_x1, 0);
                                speed_mDataSet.addSeries(1, speed_series2);
                                speed_Chart.invalidate();
                            }
                            if (speedEndFocus) {
                                PopupView pv = new PopupView(instance);//浮窗
                                pv.setStartX(event.getX());
                                pv.setStartY(event.getY());
                                // 设置浮窗上显示的数值
                                pv.setValue(String.format("%.2f", seriesSelection.getValue()));
                                frameLayout3.addView(pv);// 将浮窗添加到Framelayout

//                                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view3.getLayoutParams();
//                                params.setMargins((int) event.getX(), 0, 0, 0);// 通过自定义坐标来放置你的控件
//                                view3.setLayoutParams(params);
                                end_x1 = seriesSelection.getXValue();
                                etEnd.setText(String.format("%.1f", end_x1));
                                speed_mDataSet.removeSeries(speed_series3);
                                speed_series3.clear();
                                speed_series3.add(end_x1, max_speed * 3 / 2);
                                speed_series3.add(end_x1, seriesSelection.getValue());
                                speed_series3.add(end_x1, 0);
                                speed_mDataSet.addSeries(2, speed_series3);
                                speed_Chart.invalidate();
                            }
                            if (!etBegin.getText().toString().equals("") && !etEnd.getText().toString().equals("")) {
                                float v1 = Float.parseFloat(etEnd.getText().toString());//结束
                                float v2 = Float.parseFloat(etBegin.getText().toString());//开始
                                tvRunTime.setText(df1.format(v1 - v2));
                                int v12 = (int) (v1 * 10);
                                int v22 = (int) (v2 * 10);
                                try {
                                    if (v1 > v2) {
                                        //拿到保存的速度数组
                                        speed_jsonArray_get1 = new JSONArray(speedEntity.getPoint_speed());
                                        if (speed_jsonArray_get1 != null && speed_jsonArray_get1.length() > 0) {
                                            totalSpeed = 0;
                                            for (int i = v22 + 1; i < speed_jsonArray_get1.length(); i++) {
                                                if (i > v12) {
                                                    break;
                                                }
                                                //得到单点速度
                                                speed_linshi = speed_jsonArray_get1.getDouble(i);
                                                Log.i("asx", "speed_linshi===" + speed_linshi);
                                                //求出选中段的速度总和
                                                totalSpeed += speed_linshi;
                                            }
                                            tvAverageSpeed.setText(df2.format(totalSpeed / (v12 - v22)));
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                        }
                        break;
                }
                return false;
            }
        });
        // 将图表添加到布局中去
        frameLayout1.addView(speed_Chart);
    }

    //查看结果更新点（数据集合）
    private void speedUpdateChart() {
        // 同样是需要数据dataset和视图渲染器renderer
//        XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
        // 设置好下一个需要增加的节点
        try {
            speed_jsonArray_get1 = new JSONArray(speedEntity.getPoint_speed());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("dialog", "speed_jsonArray_get1: " + speed_jsonArray_get1.toString());

        if (speed_jsonArray_get1 != null && speed_jsonArray_get1.length() > 0) {
            for (int i = 0; i < speed_jsonArray_get1.length(); i++) {
                try {
                    speed_addY1 = speed_jsonArray_get1.getDouble(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //横坐标就是运行时间除以数组长度再乘 i ,如10/100 * 1，不断增加
                speed_series1.add((Double.parseDouble(runTime) / speed_jsonArray_get1.length()) * i, speed_addY1);
            }
            Log.i("dfd", "runTime :  " + runTime);
            Log.i("dfd", "length :  " + speed_jsonArray_get1.length());
        }
        speed_mDataSet.addSeries(speed_series1);
//        return mDataset;
    }

    private void speedSetChartSettings(XYMultipleSeriesRenderer renderer) {
        // 有关对图表的渲染可参看api文档
        renderer.setApplyBackgroundColor(true);// 设置是否显示背景色
        renderer.setBackgroundColor(Color.WHITE);// 设置背景色
        renderer.setShowGrid(false);
        renderer.setXLabels(12);
        renderer.setYLabels(10);
        renderer.setXTitle("时间(s)");
        renderer.setMargins(new int[]{20, 40, 10, 20}); // 图形 4 边距上左下右
        renderer.setPointSize(0f);// 设置点的大小(图上显示的点的大小和图例中点的大小都会被设置)
        renderer.setShowLegend(true);
        renderer.setMarginsColor(Color.parseColor("#eeeeee"));
        renderer.setAxesColor(Color.parseColor("#000000"));
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

        renderer.setYLabelsColor(0, Color.RED);
        renderer.setXLabelsColor(Color.BLACK);
        renderer.setLabelsColor(Color.BLACK);
        renderer.setXAxisMin(0, 0);
        renderer.setXAxisMax(Double.parseDouble(runTime) * 5 / 4d, 0);
        renderer.setYAxisMin(0,0);
        renderer.setYAxisMax(max_speed * 3 / 2,0);
//        renderer.setYLabelsAlign(Paint.Align.RIGHT);

        renderer.setYLabelsColor(1, Color.TRANSPARENT);
        renderer.setXAxisMin(0, 1);
        renderer.setXAxisMax(Double.parseDouble(runTime) * 5 / 4d, 1);
        renderer.setYAxisMin(0, 1);
        renderer.setYAxisMax(max_speed * 3 / 2, 1);

        renderer.setYLabelsColor(2, Color.TRANSPARENT);
        renderer.setXAxisMin(0, 2);
        renderer.setXAxisMax(Double.parseDouble(runTime) * 5 / 4d, 2);
        renderer.setYAxisMin(0, 2);
        renderer.setYAxisMax(max_speed * 3 / 2, 2);
        renderer.setYLabelsAlign(Paint.Align.RIGHT, 0);
        renderer.setYLabelsAlign(Paint.Align.RIGHT, 1);
        renderer.setYLabelsAlign(Paint.Align.RIGHT, 2);

    }


    private XYMultipleSeriesRenderer speedBuildRenderer() {
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

    //设置接口，当点击确定按钮dialog消失时，调用接口，更新vp2的视图
    private OnDialogDismissListener listener;

    public void setOnClickListener(OnDialogDismissListener listener) {
        this.listener = listener;
    }

    @OnClick({R.id.et_begin, R.id.btn_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.et_begin:
                break;
            case R.id.btn_sure:
                //点击确定保存起点和末点
                String begin = etBegin.getText().toString();
                String end = etEnd.getText().toString();

                if (begin.equals("") || end.equals("")) {
                    Toast.makeText(instance, "起点和末点不能为空", Toast.LENGTH_SHORT).show();
                } else if (Double.valueOf(end) <= Double.valueOf(begin)) {
                    Toast.makeText(instance, "选点不符合条件,末点应在起点后", Toast.LENGTH_SHORT).show();
                } else {//点击确定时更新数据库
                    double yunSuRuntime = Double.valueOf(end) - Double.valueOf(begin);
                    String yunSuRunTime = df1.format(yunSuRuntime);
                    String averageSpeed = df2.format((totalSpeed / yunSuRuntime) / 10);

                    speedEntity.setYunsu_startTime(begin);
                    speedEntity.setYunsu_endTime(end);
                    speedEntity.setYunsu_runTime(yunSuRunTime);
                    speedEntity.setYunsu_averageSpeed(averageSpeed);
                    dbHelper.updateSpeed(speedEntity);
                    listener.onDialogDismiss();
                    dismiss();
                }
                break;
        }
    }
}

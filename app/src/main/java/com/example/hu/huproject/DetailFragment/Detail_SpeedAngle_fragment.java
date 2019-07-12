package com.example.hu.huproject.DetailFragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.hu.huproject.Activity.DataDetailActivity;
import com.example.hu.huproject.DetailActivity.Detail_SpeedAngle_Jiansu_Activity;
import com.example.hu.huproject.DetailActivity.Detail_SpeedAngle_Yunsu_Activity;
import com.example.hu.huproject.DetailActivity.Detail_Speed_Jiansu_Activity;
import com.example.hu.huproject.DetailActivity.Detail_Speed_Yunsu_Activity;
import com.example.hu.huproject.Entity.SpeedAngleEntity;
import com.example.hu.huproject.Entity.SpeedEntity;
import com.example.hu.huproject.R;
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

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

//数据管理中速度与角度测试----->测试图界面（匀速分析和减速分析通过按钮点击弹出）
public class Detail_SpeedAngle_fragment extends Fragment {

    Button btnYunsu;
    Button btnJiansu;
    FrameLayout frameLayout1;
    FrameLayout frameLayout2;
    View view1;
    View view2;
    RelativeLayout flChart;
    TextView tvRunSpeed;
    TextView tvMaxSpeed;
    LinearLayout linearLayout1;
    TextView tvRunAngle;
    TextView tvMaxAngle;
    LinearLayout linearLayout2;
    Unbinder unbinder;

    private SpeedAngleEntity speedAngleEntity1;
    private View view;

    private XYMultipleSeriesDataset mDataset;
    private XYSeries series1;
    private XYSeries series2;
    private GraphicalView chart;//图像视图
    private XYMultipleSeriesRenderer renderer;
    private JSONArray jsonArray_get;//查看结果时用来存取数据
    private JSONArray jsonArray_get2;//查看结果时用来存取数据
    private double addX1, addY1;
    private double addX2, addY2;

    public static Detail_SpeedAngle_fragment newInstance(Long id) {
        Detail_SpeedAngle_fragment fragment = new Detail_SpeedAngle_fragment();
        Bundle bundle = new Bundle();
        bundle.putLong("id", id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_detail_speedangle, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }


    public void initView() {
        if (view != null) {
            btnYunsu = view.findViewById(R.id.btn_yunsu);
            btnJiansu = view.findViewById(R.id.btn_jiansu);
            frameLayout1 = view.findViewById(R.id.frameLayout1);
            frameLayout2 = view.findViewById(R.id.frameLayout2);
            view1 = view.findViewById(R.id.view1);
            view2 = view.findViewById(R.id.view2);
            flChart = view.findViewById(R.id.fl_chart);
            tvRunSpeed = view.findViewById(R.id.tv_run_speed);
            tvMaxSpeed = view.findViewById(R.id.tv_max_speed);
            linearLayout1 = view.findViewById(R.id.linearLayout1);
            tvRunAngle = view.findViewById(R.id.tv_run_angle);
            tvMaxAngle = view.findViewById(R.id.tv_max_angle);
            linearLayout2 = view.findViewById(R.id.linearLayout2);


            frameLayout2.removeAllViews();//当spinner切换时，移除上一个视图中的popView
            initChart();
        }
    }

    private void initChart() {
        addX1 = 0;
        addY1 = 0;
        addX2 = 0;
        addY2 = 0;
        jsonArray_get = new JSONArray();
        jsonArray_get2 = new JSONArray();

        series1 = new XYSeries("速度");
        series2 = new XYSeries("角度", 1);
        //创建一个数据集的实例，这个数据集将被用来创建图表
        mDataset = new XYMultipleSeriesDataset();
//        mDataset.addSeries(0, series1);
//        mDataset.addSeries(1, series2);
        //将点集添加进这个数据集中
        // 以下都是曲线的样式和属性等等的设置，renderer相当于一个用来给图表做渲染的句柄
        renderer = buildRendener();
        //设置好图表的样式
        setChartSettings(renderer);

        DataDetailActivity activity = (DataDetailActivity) getActivity();
        speedAngleEntity1 = activity.speedAngleEntity;
//        tvRunSpeed.setText(speedAngleEntity1.getRunSpeed());
        tvMaxSpeed.setText(speedAngleEntity1.getMaxSpeed());
        tvMaxAngle.setText(speedAngleEntity1.getMaxAngle());
        //不显示出来的数据不用作截取小数点处理：maxspeed
        renderer.setXAxisMin(0);
        double runtime = Double.parseDouble(speedAngleEntity1.getRunTimeTotal());
        renderer.setXAxisMax(runtime + 3);
        renderer.setYAxisMin(0);
        double maxspeed = Double.parseDouble(speedAngleEntity1.getMaxSpeed());
        renderer.setYAxisMax(maxspeed * 3 / 2);
        //角度数据
        renderer.setXAxisMin(0, 1);
        renderer.setXAxisMax(runtime + 3, 1);
        double maxAngle = Double.parseDouble(speedAngleEntity1.getMaxAngle());
        renderer.setYAxisMin(-maxAngle * 3 / 2, 1);
        renderer.setYAxisMax(maxAngle * 3 / 2, 1);
        //拿到速度数据和角度数据
        updateChart2(speedAngleEntity1);

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
                        SeriesSelection seriesSelection = chart.getCurrentSeriesAndPoint();// 获取当前点击的点的相关信息
                        if (seriesSelection != null
                                && seriesSelection.getSeriesIndex() != 2) {
                            PopupView pv = new PopupView(getActivity()); // 浮窗
                            pv.setStartX(event.getX()); // 设置浮窗的位置
                            pv.setStartY(event.getY());
                            pv.setValue(String.format("%.2f", seriesSelection.getValue())); // 设置浮窗上显示的数值
                            frameLayout2.addView(pv); // 将浮窗添加到Framelayout
                        }
                        break;
                }
                return false;
            }
        });
        //将图表添加到布局中去
        frameLayout1.addView(chart);
    }

    //曲线样式和属性设置
    private XYMultipleSeriesRenderer buildRendener() {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer(2);
        // 设置图表中曲线本身的样式，包括颜色、点的大小以及线的粗细等
        XYSeriesRenderer r = new XYSeriesRenderer();//第一条
        r.setColor(Color.RED);
        r.setPointStyle(PointStyle.CIRCLE);
        r.setFillPoints(true);
        r.setChartValuesSpacing(3);
        renderer.addSeriesRenderer(0, r);

        r = new XYSeriesRenderer();//第二条
        r.setColor(Color.BLACK);
        r.setPointStyle(PointStyle.CIRCLE);
        r.setFillPoints(true);
        r.setChartValuesSpacing(3);
        renderer.addSeriesRenderer(1, r);

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
        renderer.setAntialiasing(true);//消除锯齿
        renderer.setFitLegend(true);//调节图例至适当位置
        // 设置点的缓冲半径值(在某点附近点击时,多大范围内算点击这个点)
        renderer.setSelectableBuffer(20);
        renderer.setPanEnabled(true, true);// 可上下拖动，可水平拖动
        renderer.setPanLimits(new double[]{0, 10000, 0, 10000});//设置拉动的范围
        renderer.setZoomEnabled(true);//设置渲染器允许放大缩小
        renderer.setZoomLimits(new double[]{0, 10000, 0, 10000});//设置缩放的范围
        renderer.setZoomButtonsVisible(false);//设置显示放大缩小按钮
        renderer.setXLabelsColor(Color.BLACK);
        renderer.setLabelsColor(Color.BLACK);
        renderer.setYLabelsColor(0, Color.RED);
        renderer.setXAxisMin(0, 0);
        renderer.setXAxisMax(30, 0);
        renderer.setYAxisMin(0, 0);
        renderer.setYAxisMax(50, 0);
        renderer.setYLabelsAlign(Paint.Align.RIGHT, 0);
        /* 设置第二条Y轴-偏角 */
        renderer.setYLabelsColor(1, Color.BLACK);
        renderer.setXAxisMin(0, 1);
        renderer.setXAxisMax(30, 1);
        renderer.setYAxisMin(0, 1);
        renderer.setYAxisMax(50, 1);
        renderer.setYAxisAlign(Paint.Align.RIGHT, 1);
        renderer.setYLabelsAlign(Paint.Align.LEFT, 1);
    }

    private void updateChart2(SpeedAngleEntity speedEntity) {
        // 同样是需要数据dataset和视图渲染器renderer
//        XYMultipleSeriesDataset mDataset1 = new XYMultipleSeriesDataset();
        // 设置好下一个需要增加的节点
        try {
            if (speedEntity != null) {
                jsonArray_get = new JSONArray(speedEntity.getPoint_speed());
                jsonArray_get2 = new JSONArray(speedEntity.getPoint_angle());
            }
            if (jsonArray_get != null && jsonArray_get.length() > 0) {
                for (int i = 0; i < jsonArray_get.length(); i++) {
                    try {
                        addX1 = addX1 + 0.1;
                        addY1 = jsonArray_get.getDouble(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    series1.add(addX1, addY1);
                }
            }
            if (jsonArray_get2 != null && jsonArray_get2.length() > 0) {
                for (int i = 0; i < jsonArray_get2.length(); i++) {
                    try {
                        addX2 = addX2 + 0.1;
                        addY2 = jsonArray_get2.getDouble(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    series2.add(addX2, addY2);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mDataset.addSeries(0, series1);
        mDataset.addSeries(1, series2);
//        return mDataset1;
    }


    @OnClick({R.id.btn_yunsu, R.id.btn_jiansu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_yunsu://匀速分析
                Intent intent1 = new Intent(getContext(), Detail_SpeedAngle_Yunsu_Activity.class);
                intent1.putExtra("id", speedAngleEntity1.getId());
                startActivity(intent1);
                break;
            case R.id.btn_jiansu://减速分析
                Intent intent2 = new Intent(getContext(), Detail_SpeedAngle_Jiansu_Activity.class);
                intent2.putExtra("id", speedAngleEntity1.getId());
                startActivity(intent2);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

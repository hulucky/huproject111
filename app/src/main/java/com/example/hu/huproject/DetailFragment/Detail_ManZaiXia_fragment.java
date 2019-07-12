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
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.hu.huproject.Activity.DataDetailActivity;
import com.example.hu.huproject.DetailActivity.Detail_Speed_Jiansu_Activity;
import com.example.hu.huproject.DetailActivity.Detail_Speed_Yunsu_Activity;
import com.example.hu.huproject.Entity.ManZaiDownEntity;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

//数据管理中满载向下制动距离测试界面
public class Detail_ManZaiXia_fragment extends Fragment {

    FrameLayout frameLayout1;
    FrameLayout frameLayout2;
    View view1;
    View view2;
    RelativeLayout flChart;
    TextView tvZhidongjuli;
    TextView tvZhidongchusudu;
    LinearLayout linearLayout1;
    TextView tvLiumiaojuli;
    TextView tvZhidongtime;
    LinearLayout linearLayout2;
    Unbinder unbinder;

    private ManZaiDownEntity manZaiDownEntity1;
    private View view;

    private XYMultipleSeriesDataset mDataset;
    private XYSeries series1;
    private XYSeries series2;//断开竖线
    private XYSeries series3;//结束时竖线
    private GraphicalView chart;//图像视图
    private XYMultipleSeriesRenderer renderer;
    private JSONArray jsonArray_get;//查看结果时用来存取数据
    private double addX1, addY1;

    public static Detail_ManZaiXia_fragment newInstance(Long id) {
        Detail_ManZaiXia_fragment fragment = new Detail_ManZaiXia_fragment();
        Bundle bundle = new Bundle();
        bundle.putLong("id", id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_detail_manzaixia, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }


    /*  Bundle bundle = getArguments();
        if (bundle != null) {
            long id = bundle.getLong("id");
            DBHelper dbHelper = DBHelper.getInstance(getContext());
            SpeedEntity speedEntity = dbHelper.querySpeedById(id);
            Log.i("datadetailsddd", "speedEntity== " + speedEntity.toString());
        }*/
    public void initView() {
        if (view != null) {
            tvZhidongjuli = view.findViewById(R.id.tv_zhidongjuli);
            tvZhidongchusudu = view.findViewById(R.id.tv_zhidongchusudu);
            frameLayout1 = view.findViewById(R.id.frameLayout1);
            frameLayout2 = view.findViewById(R.id.frameLayout2);
            view1 = view.findViewById(R.id.view1);
            view2 = view.findViewById(R.id.view2);
            flChart = view.findViewById(R.id.fl_chart);
            tvLiumiaojuli = view.findViewById(R.id.tv_liumiaojuli);
            tvZhidongtime = view.findViewById(R.id.tv_zhidongtime);
            linearLayout1 = view.findViewById(R.id.linearLayout1);

            frameLayout2.removeAllViews();//当spinner切换时，移除上一个视图中的popView
            initChart();
        }
    }

    private void initChart() {
        addX1 = 0;
        addY1 = 0;
        jsonArray_get = new JSONArray();

        series1 = new XYSeries("速度");
        series2 = new XYSeries("制动时间");
        series3 = new XYSeries("结束时间");
        //创建一个数据集的实例，这个数据集将被用来创建图表
        mDataset = new XYMultipleSeriesDataset();
        mDataset.addSeries(0, series1);
        mDataset.addSeries(1, series2);
        mDataset.addSeries(2, series3);
        //将点集添加进这个数据集中
        // 以下都是曲线的样式和属性等等的设置，renderer相当于一个用来给图表做渲染的句柄
        renderer = buildRendener();
        //设置好图表的样式
        setChartSettings(renderer);

        DataDetailActivity activity = (DataDetailActivity) getActivity();
        manZaiDownEntity1 = activity.manZaiDownEntity;

        tvZhidongjuli.setText(manZaiDownEntity1.getZhiDongJuli());
        tvZhidongchusudu.setText(manZaiDownEntity1.getZhiDongChuSuDu());
        tvLiumiaojuli.setText(manZaiDownEntity1.getLiuMiaoJuLi());
        tvZhidongtime.setText(manZaiDownEntity1.getZhiDongShiJian());

        //不显示出来的数据不用作截取小数点处理：maxspeed
        renderer.setXAxisMin(0);
        double runtime = Double.parseDouble(manZaiDownEntity1.getRunTimeTotal());
        renderer.setXAxisMax(runtime + 3);
        renderer.setYAxisMin(0);
        double maxspeed = Double.parseDouble(manZaiDownEntity1.getMaxSpeed());
        renderer.setYAxisMax(maxspeed * 3 / 2);
        chart = ChartFactory.getCubeLineChartView(getActivity(), updateChart2(manZaiDownEntity1), renderer, 0.1f);

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
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer(3);
        // 设置图表中曲线本身的样式，包括颜色、点的大小以及线的粗细等
        XYSeriesRenderer r = new XYSeriesRenderer();//第一条
        r.setColor(Color.RED);
        r.setPointStyle(PointStyle.CIRCLE);
        r.setFillPoints(true);
        r.setChartValuesSpacing(3);
        renderer.addSeriesRenderer(0, r);

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

        renderer.setXAxisMin(0, 1);
        renderer.setXAxisMax(30, 1);
        renderer.setYAxisMin(0, 1);
        renderer.setYAxisMax(50, 1);

        renderer.setXAxisMin(0, 2);
        renderer.setXAxisMax(30, 2);
        renderer.setYAxisMin(0, 2);
        renderer.setYAxisMax(50, 2);
        renderer.setYLabelsAlign(Paint.Align.LEFT, 0);
        renderer.setYLabelsAlign(Paint.Align.RIGHT, 1);
        renderer.setYLabelsAlign(Paint.Align.RIGHT, 2);
    }

    private XYMultipleSeriesDataset updateChart2(ManZaiDownEntity manZaiDownEntity) {
        // 同样是需要数据dataset和视图渲染器renderer
        XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
        // 设置好下一个需要增加的节点
        try {
            if (manZaiDownEntity != null) {
                jsonArray_get = new JSONArray(manZaiDownEntity.getPoint_speed());
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String breakTime = manZaiDownEntity.getBreakTime();
        String maxSpeed = manZaiDownEntity.getMaxSpeed();
        String runTimeTotal = manZaiDownEntity.getRunTimeTotal();
        //断点数据
        series2.add(Float.parseFloat(breakTime), 0);
        series2.add(Float.parseFloat(breakTime), Float.parseFloat(maxSpeed) * 3 / 2);
        //结束点数据
        series3.add(Float.parseFloat(runTimeTotal), 0);
        series3.add(Float.parseFloat(runTimeTotal), Float.parseFloat(maxSpeed) * 3 / 2);
        mDataset.addSeries(0, series1);
        mDataset.addSeries(1, series2);
        mDataset.addSeries(2, series3);
        return mDataset;

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

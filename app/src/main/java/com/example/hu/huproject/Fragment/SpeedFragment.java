package com.example.hu.huproject.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hu.huproject.R;
import com.example.hu.huproject.View.MyMarkerView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.mchsdk.paysdk.mylibrary.ListFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SpeedFragment extends ListFragment {

    @BindView(R.id.lineChart)
    LineChart mLineChart;
    private List<String> mList;

    public static SpeedFragment newInstance(int position) {
        SpeedFragment fragment = new SpeedFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void lazyLoadData() {

    }

    @Override
    protected void initView() {
        initData();
        //显示边界
        mLineChart.setDrawBorders(true);
        //设置数据
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            entries.add(new Entry(i, (float) (Math.random()) * 80));
        }
        //一个LineDataSet就是一条线
        LineDataSet lineDataSet = new LineDataSet(entries, "温度");
        //线模式为圆滑曲线（默认折线）
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        //取消曲线显示的值为整数
//        lineDataSet.setValueFormatter(new IValueFormatter() {
//            @Override
//            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
//                int IValue = (int) value;
//                return String.valueOf(IValue);
//            }
//        });
        LineData data = new LineData(lineDataSet);
        mLineChart.setData(data);
        //得到X轴
        XAxis xAxis = mLineChart.getXAxis();
        //设置X轴的位置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //设置X轴坐标之间的最小间隔（因为此图有缩放功能，X轴,Y轴可设置可缩放）
        xAxis.setGranularity(1f);
        /**
         * 设置X轴的刻度数量
         * 第二个参数表示是否平均分配 如果为true则按比例分为12个点、
         * 如果为false则适配X刻度的值来分配点，可能没有12个点。
         */
        xAxis.setLabelCount(12, true);
        //设置X轴的值（最小值、最大值、然后会根据设置的刻度数量自动分配刻度显示）
        xAxis.setAxisMinimum(0f);
        xAxis.setAxisMaximum(11f);//
//        //设置X轴值为字符串
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mList.get((int) value);//mList为存有月份的集合
            }
        });
        /**
         * 得到并设置Y轴后，Y轴就会从0开始
         */
        //得到Y轴
        YAxis leftYAxis = mLineChart.getAxisLeft();
        YAxis rightYAxis = mLineChart.getAxisRight();
        //设置Y轴值
        leftYAxis.setAxisMinimum(0f);
        leftYAxis.setAxisMaximum(100f);

        rightYAxis.setAxisMinimum(0f);
        rightYAxis.setAxisMaximum(100f);
        leftYAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return (int)value+"%";
            }
        });
        rightYAxis.setEnabled(true);//右侧Y轴不显示
        rightYAxis.setGranularity(1f);
        rightYAxis.setLabelCount(11,false);
        rightYAxis.setTextColor(Color.BLUE);//文字颜色
        rightYAxis.setGridColor(Color.RED);//网格线颜色
        rightYAxis.setAxisLineColor(Color.GREEN);//Y轴颜色
        //限制线
        LimitLine limitLine = new LimitLine(95, "高限制性");//得到限制线
        limitLine.setLineWidth(4f);
        limitLine.setTextSize(10f);
        limitLine.setTextColor(Color.RED);  //颜色
        limitLine.setLineColor(Color.BLUE);
        rightYAxis.addLimitLine(limitLine); //Y轴添加限制线
        //图例
        Legend legend = mLineChart.getLegend();//得到图例
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setTextColor(Color.CYAN);//设置Legend 文本颜色
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //设置标签是否换行
        legend.setWordWrapEnabled(true);//可换行
        //隐藏Lengend
        legend.setEnabled(false);
        Description description = new Description();
//        description.setEnabled(false);//隐藏描述
        description.setText("X轴描述");
        description.setTextColor(Color.RED);
        mLineChart.setDescription(description);
        /**
         * MarkerView可自定义
         * 用于点击图标值时显示想要的内容
         */
        //设置显示MarkerView
        MyMarkerView mv = new MyMarkerView(getContext(), R.layout.markerview);
//        mv.setChartView(mLineChart);
        mLineChart.setMarkerView(mv);
    }

    //添加月份
    private void initData() {
        mList = new ArrayList<>();
        mList.add("一月");
        mList.add("二月");
        mList.add("三月");
        mList.add("四月");
        mList.add("五月");
        mList.add("六月");
        mList.add("七月");
        mList.add("八月");
        mList.add("九月");
        mList.add("十月");
        mList.add("十一月");
        mList.add("十二月");
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_speed;
    }
}

package com.example.hu.huproject.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hu.huproject.Adapter.TestItemAdapter;
import com.example.hu.huproject.Adapter.TestListAdapter;
import com.example.hu.huproject.Application.MyApp;
import com.example.hu.huproject.Interface.ISensorInf;
import com.example.hu.huproject.Listener.OnRecyclerViewItemClickListener;
import com.example.hu.huproject.R;
import com.example.hu.huproject.Utils.SerialControl;
import com.example.hu.huproject.View.RecycleViewDivider;
import com.mchsdk.paysdk.mylibrary.ListFragment;
import com.sensor.SensorData;
import com.sensor.SensorInf;
import com.sensor.view.SensorView;
import com.xzkydz.function.view.CustomViewPager;
import com.xzkydz.helper.ComControl;
import com.xzkydz.util.DataType;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

//制动力测试的测试界面
public class ZhiDongliTestFragment extends ListFragment {

    @BindView(R.id.sv_lali)
    SensorView svLali;
    @BindView(R.id.custom_ViewPager)
    CustomViewPager customViewPager;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.btn_start)
    TextView btnStart;
    @BindView(R.id.btn_save)
    TextView btnSave;
    private boolean isRunning = false;
    private ZhiDongLiFragmentVp1 vp1;
    private SerialControl comA;//串口
    private boolean isConnected = false;//传感器是否连接

    //实例化自己，可以从外部传参
    public static ZhiDongliTestFragment newInstance(int position) {
        ZhiDongliTestFragment testFragment = new ZhiDongliTestFragment();
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

        //初始化recycleView
        recyclerView.setHasFixedSize(true);//避免重新计算大小
        recyclerView.setNestedScrollingEnabled(false);//禁止嵌套滑动
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayoutManager.HORIZONTAL, R.drawable.cs_line_02));
        //按钮标签
        List<String> titleList = new ArrayList<>();
        titleList.add("制动力");
        TestListAdapter adapter = new TestListAdapter(getContext(), clickListener, titleList);
        recyclerView.setAdapter(adapter);

        //初始化viewPager
        customViewPager.setScanScroll(false);
        List<Fragment> fragmentList = new ArrayList<>();
        vp1 = ZhiDongLiFragmentVp1.newInstance(0);
        fragmentList.add(vp1);

        TestItemAdapter testItemAdapter = new TestItemAdapter(getChildFragmentManager(), fragmentList);
        customViewPager.setAdapter(testItemAdapter);
        //初始化串口
        initEvent();
    }


    //初始化串口
    private void initEvent() {
        /*串口操作*/
        comA = new SerialControl(getContext(), DataType.DATA_OK_PARSE);
        Log.i("sss", "new SerialControl");
        // 设置读取串口的时间间隔毫秒（大于0的整数），建议不少于50
        comA.setiDelay(20);
        //打开串口
        ComControl.OpenComPort(comA);
        Log.i("sss", "OpenComPort");
        svLali.setOnStatusChangeListener(new MyOnStatusChanger());
        //刷新传感器控件的状态
        comA.setOnReceivedSensorListener(new SerialControl.OnReceivedSensorData() {
            @Override
            public void getData(final ISensorInf sensorInf) {
                switch (sensorInf.getSensorType()) {
                    case 4://拉力
                        Log.i("sss", "4");
                        isConnected = true;
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //sensorData 用于设置传感器信息（电量、信号、状态、图片）
                                SensorData sensorData = new SensorData(sensorInf.getPower(), sensorInf.getSignal(),
                                        SensorInf.NORMAL, System.currentTimeMillis());
                                svLali.setData(sensorData);
                                Log.i("asa", "电量: " + sensorInf.getPower() + "\n" + "信号强度：" + sensorInf.getSignal());
                            }
                        });
                        break;
                }
            }
        });
    }


    private class MyOnStatusChanger implements SensorView.OnStatusChangeListener {

        @Override
        public void status(View view, int i, int i1) {
            int id = view.getId();
            if (i1 == SensorInf.SEARCHING) {
                switch (id) {
                    case R.id.sv_lali:
                        Toast.makeText(getContext(), "拉力传感器断开！", Toast.LENGTH_SHORT).show();
                        isConnected = false;
                        break;

                }
            }
        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_zhidongli_test;
    }


    @OnClick({R.id.btn_start, R.id.btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_start://开始
                if (isConnected) {//传感器连接之后才可以开始
                    if (isRunning) {//如果正在运行，那么此时的按钮显示为“记录”，那么点击之后变为开始
                        vp1.record();
                        btnStart.setText("开始");
                        isRunning = false;
                        MyApp.isRunning = false;
                    } else {
                        vp1.start();
                        btnStart.setText("记录");
                        isRunning = true;
                        MyApp.isRunning = true;
                    }
                } else {
                    Toast.makeText(getContext(), "传感器尚未连接，无法开始！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_save://保存
                if (isConnected) {//传感器连接之后才可以开始
                    if (!isRunning) {//如果现在没在测试，那么可以保存
                        vp1.save();
                    } else {
                        Toast.makeText(getContext(), "请停止测试后再保存！", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "传感器尚未连接，无法保存！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    OnRecyclerViewItemClickListener clickListener = new OnRecyclerViewItemClickListener() {

        @Override
        public void onRecyclerViewItemClicked(int position, RecyclerView.ViewHolder viewHolder) {
            customViewPager.setCurrentItem(position);
        }

        @Override
        public void onRecyclerViewItemLongClicked(int position, RecyclerView.ViewHolder viewHolder) {

        }

        @Override
        public void onRecyclerViewItemClicked() {

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        MyApp.isRunning = false;//清零时需要判断此时是否处于测试状态,故将此置位false
        svLali.destroy();
        comA.close();
    }
}

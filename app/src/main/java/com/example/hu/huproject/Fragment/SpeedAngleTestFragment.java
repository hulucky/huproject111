package com.example.hu.huproject.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.db.manager.DaoSession;
import com.db.manager.SpeedEntityDao;
import com.db.manager.TaskEntityDao;
import com.example.hu.huproject.Adapter.TestItemAdapter;
import com.example.hu.huproject.Adapter.TestListAdapter;
import com.example.hu.huproject.Application.MyApp;
import com.example.hu.huproject.Bean.SuDuBean;
import com.example.hu.huproject.Entity.SpeedEntity;
import com.example.hu.huproject.Entity.TaskEntity;
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

import static com.example.hu.huproject.Application.MyApp.MY_TAG;
import static com.xzkydz.function.app.KyApp.numberStr;
import static org.greenrobot.eventbus.EventBus.TAG;

//速度与角度测试的测试界面
public class SpeedAngleTestFragment extends ListFragment {

    @BindView(R.id.custom_ViewPager)
    CustomViewPager customViewPager;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.btn_start)
    TextView btnStart;
    @BindView(R.id.btn_save)
    TextView btnSave;
    @BindView(R.id.sv_speed)
    SensorView svSpeed;
    @BindView(R.id.sv_angle)
    SensorView svAngle;
    //    @BindView(R.id.sv_wenshidu)
//    SensorView svWenshidu;
    Unbinder unbinder;
    private SerialControl comA;//串口
    private SpeedAngleFragmentVp1 vp1;//测试图
    private boolean isRunning = false;
    private boolean isConnected = false;//判断传感器是否连接
    private boolean isQjConnected = false;//判断倾角是否连接
    private SpeedAngleFragmentVp2 vp2;
    private SpeedAngleFragmentVp3 vp3;
    private int startNum = 0;//开始按钮点击的次数
    private int saveNum = 0;//结束按钮点击的次数


    //实例化自己，可以从外部传参
    public static SpeedAngleTestFragment newInstance(int position) {
        SpeedAngleTestFragment testFragment = new SpeedAngleTestFragment();
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
        //初始化recycleView
        recyclerView.setHasFixedSize(true);//避免重新计算大小
        recyclerView.setNestedScrollingEnabled(false);//禁止嵌套滑动
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayoutManager.HORIZONTAL, R.drawable.cs_line_02));
        //按钮标签
        List<String> titleList = new ArrayList<>();
        titleList.add("测试图");
        titleList.add("匀速分析");
        titleList.add("减速分析");
        TestListAdapter adapter = new TestListAdapter(getContext(), clickListener, titleList);
        recyclerView.setAdapter(adapter);

        //初始化viewPager
        customViewPager.setScanScroll(false);
        List<Fragment> fragmentList = new ArrayList<>();
        vp1 = SpeedAngleFragmentVp1.newInstance(0);//测试图
        vp2 = SpeedAngleFragmentVp2.newInstance(1);
        vp3 = SpeedAngleFragmentVp3.newInstance(2);
        fragmentList.add(vp1);
        fragmentList.add(vp2);//匀速分析
        fragmentList.add(vp3);//减速分析
        TestItemAdapter testItemAdapter = new TestItemAdapter(getChildFragmentManager(), fragmentList);
        customViewPager.setOffscreenPageLimit(3);
        customViewPager.setAdapter(testItemAdapter);

        //初始化串口
        initEvent();
    }

    //初始化串口
    private void initEvent() {
        /*串口操作*/
        comA = new SerialControl(getContext(), DataType.DATA_OK_PARSE);
        // 设置读取串口的时间间隔毫秒（大于0的整数），建议不少于50
        comA.setiDelay(20);
        //打开串口
        ComControl.OpenComPort(comA);
        svSpeed.setOnStatusChangeListener(new MyOnStatusChanger());
        svAngle.setOnStatusChangeListener(new MyOnStatusChanger());
//        svWenshidu.setOnStatusChangeListener(new MyOnStatusChanger());
        //刷新传感器控件的状态
        comA.setOnReceivedSensorListener(new SerialControl.OnReceivedSensorData() {
            @Override
            public void getData(final ISensorInf sensorInf) {
                switch (sensorInf.getSensorType()) {
                    case 0://速度
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                isConnected = true;
                                //sensorData 用于设置传感器信息（电量、信号、状态、图片）
                                SensorData sensorData = new SensorData(sensorInf.getPower(), sensorInf.getSignal(),
                                        SensorInf.NORMAL, System.currentTimeMillis());
                                svSpeed.setData(sensorData);
                                Log.i("asa", "电量: " + sensorInf.getPower() + "\n" + "信号强度：" + sensorInf.getSignal());

                            }
                        });
                        break;
                    case 1://倾角
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                isQjConnected = true;
                                SensorData sensorData = new SensorData(sensorInf.getPower(), sensorInf.getSignal()
                                        , SensorInf.NORMAL, System.currentTimeMillis());
                                svAngle.setData(sensorData);
                            }
                        });
                        break;
                    case 2://温湿度
                        break;
                }
            }
        });
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_speedangle_test;
    }


    @OnClick({R.id.btn_start, R.id.btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_start:
                if (isConnected && isQjConnected) {
                    if (customViewPager.getCurrentItem() == 0) {
                        if (isRunning) {//如果正在运行
                            vp1.stop();//停止画图
                            btnStart.setText("开始");
                            isRunning = false;
                            MyApp.isRunning = false;
                        } else {
                            startNum = 0;
                            saveNum = 0;
                            startNum = startNum + 1;
                            vp1.start();//开始画图
                            btnStart.setText("停止");
                            isRunning = true;
                            MyApp.isRunning = isRunning;
                        }
                    } else {
                        Toast.makeText(getContext(), "请切换到测试图界面进行测试", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "传感器尚未连接！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_save://保存
                if (isConnected && isQjConnected) {
                    if (isRunning) {
                        Toast.makeText(getContext(), "请停止测试后再保存", Toast.LENGTH_SHORT).show();
                    } else {
                        saveNum = saveNum + 1;
                        if (saveNum > startNum) {//避免重复保存数据
                            break;
                        }
                        vp1.save1();
                        vp2.saveYunSu();
                        vp3.saveJianSu();
                        Toast toast = Toast.makeText(getActivity(),
                                "保存成功！", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);// 居中显示
                        toast.show();
                    }
                } else {
                    Toast.makeText(getContext(), "传感器尚未连接！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    //右侧选项按钮和viewpager联动
    OnRecyclerViewItemClickListener clickListener = new OnRecyclerViewItemClickListener() {

        @Override
        public void onRecyclerViewItemClicked(int position, RecyclerView.ViewHolder viewHolder) {
            if (position != 0) {
                if (isRunning) {
                    Toast.makeText(getContext(), "请停止该项测试后重试！", Toast.LENGTH_SHORT).show();
                    customViewPager.setCurrentItem(0);
                    return;
                }
            }
            customViewPager.setCurrentItem(position);
        }

        @Override
        public void onRecyclerViewItemLongClicked(int position, RecyclerView.ViewHolder viewHolder) {

        }

        @Override
        public void onRecyclerViewItemClicked() {

        }
    };

    private class MyOnStatusChanger implements SensorView.OnStatusChangeListener {

        @Override
        public void status(View view, int i, int i1) {
            int id = view.getId();
            if (i1 == SensorInf.SEARCHING) {
                switch (id) {
                    case R.id.sv_speed:
                        Toast.makeText(getContext(), "速度传感器断开！", Toast.LENGTH_SHORT).show();
                        isConnected = false;
                        break;
                    case R.id.sv_angle:
                        isQjConnected = false;
                        Toast.makeText(getContext(), "倾角传感器断开！", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.sv_wenshidu:
                        Toast.makeText(getContext(), "温湿度传感器断开！", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //把数据清零
        MyApp.isRunning = false;
        MyApp.jsonArray_SpeedListY = null;
        MyApp.SpeedListX = "";
        MyApp.max_speed = 0.0;
        MyApp.startTime = "";
        MyApp.endTime = "";
        MyApp.startTime_JianSu = "";
        MyApp.endTime_JianSu = "";
        MyApp.entityID = null;//当退出速度与角度测试界面时，把entityID置位null,保证下一次进来时，不会拿到上一次的任务

        startNum = 0;
        saveNum = 0;

        svSpeed.destroy();
        svAngle.destroy();
//        svWenshidu.destroy();
        comA.close();
    }
}

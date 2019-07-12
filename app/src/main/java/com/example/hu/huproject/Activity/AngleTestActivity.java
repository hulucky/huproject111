package com.example.hu.huproject.Activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hu.huproject.Adapter.TestAdapter;
import com.example.hu.huproject.Application.MyApp;
import com.example.hu.huproject.Fragment.AngleTestFragment;
import com.example.hu.huproject.Fragment.SpeedAngleTestFragment;
import com.example.hu.huproject.Fragment.SpeedAngleTestParaFragment;
import com.example.hu.huproject.Interface.ISensorInf;
import com.example.hu.huproject.R;
import com.example.hu.huproject.Utils.EyesUtils;
import com.example.hu.huproject.Utils.MyFunc;
import com.example.hu.huproject.Utils.WSDSerialControl;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.sensor.SensorData;
import com.sensor.SensorInf;
import com.sensor.view.SensorView;
import com.xzkydz.function.view.CustomViewPager;
import com.xzkydz.helper.ComControl;
import com.xzkydz.util.DataType;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//角度测试界面
public class AngleTestActivity extends AppCompatActivity {

    @BindView(R.id.iv_left)
    ImageView ivLeft;
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_tilte)
    TextView tvTilte;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.rl_right)
    RelativeLayout rlRight;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.titleBar)
    LinearLayout titleBar;
    @BindView(R.id.tabLayout)
    SlidingTabLayout tabLayout;
    @BindView(R.id.vp)
    CustomViewPager vp;
    @BindView(R.id.sv_wsd)
    SensorView svWsd;

    private TestAdapter testAdapter;
    private List<String> titleList;
    private AngleTestActivity instance;
    private List<Fragment> fragmentList;

    private Handler wsdHandler;

    private WSDSerialControl wsdSerialControl;
    private boolean isWsdConnected;

    private DecimalFormat df2 = new DecimalFormat("0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        if (Build.VERSION_CODES.M > Build.VERSION.SDK_INT) { //小于6.0
            EyesUtils.setStatusBarColorTw(this, ContextCompat.getColor(this, R.color.orange_my));    //设置标题栏透明白，字体为黑色
        } else {
            EyesUtils.setStatusBarLightMode(this, Color.WHITE);  //设置状态栏颜色和字体颜色
        }
        instance = this;
        initView();
        vp.setCurrentItem(1);
    }

    private void initView() {

        tvLeft.setText("角度测试");
        rlBack.setVisibility(View.VISIBLE);
        tvLeft.setVisibility(View.VISIBLE);
        tvTilte.setVisibility(View.VISIBLE);

        //tablayout标题
        titleList = new ArrayList<>();
        titleList.add(getResources().getString(R.string.test_para));
        titleList.add(getResources().getString(R.string.test_test));
        //fragment列表
        fragmentList = new ArrayList<>();
        fragmentList.add(SpeedAngleTestParaFragment.newInstance(0));
        fragmentList.add(AngleTestFragment.newInstance(1));
        testAdapter = new TestAdapter(getSupportFragmentManager(), 2, titleList, fragmentList, instance);
        //viewPager设置适配器
        vp.setAdapter(testAdapter);
        vp.setScanScroll(false);//禁止滑动
        //tablayout和viewpager关联
        tabLayout.setViewPager(vp);
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {

            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        //初始化串口
        initEvent();
    }


    private class MyOnStatusChange implements SensorView.OnStatusChangeListener {
        @Override
        public void status(View view, int i, int i1) {
            int id = view.getId();
            if (i1 == SensorInf.SEARCHING) {
                switch (id) {
                    case R.id.sv_wsd:
                        Toast.makeText(getApplicationContext(), "温湿度传感器断开！", Toast.LENGTH_SHORT).show();
                        isWsdConnected = false;
                        MyApp.comBeanWSD = null;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvTilte.setText("-- ℃ " + "-- %RH " + "-- hPa");
                            }
                        });
                        break;

                }
            }
        }
    }

    private void initEvent() {
        wsdSerialControl = new WSDSerialControl(instance, DataType.DATA_OK_PARSE);
        wsdSerialControl.setiDelay(20);
        ComControl.OpenComPort(wsdSerialControl);
        svWsd.setOnStatusChangeListener(new MyOnStatusChange());

        wsdSerialControl.setOnReceivedSensorListener(new WSDSerialControl.OnReceivedSensorData() {
            @Override
            public void getData(final ISensorInf sensorInf) {
                switch (sensorInf.getSensorType()) {
                    case 2://温湿度
                        isWsdConnected = true;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //sensorData 用于设置传感器信息（电量、信号、状态、图片）
                                SensorData sensorData = new SensorData(sensorInf.getPower(), sensorInf.getSignal(),
                                        SensorInf.NORMAL, System.currentTimeMillis());
                                Log.i("lol", "setData: ");
                                svWsd.setData(sensorData);
                            }
                        });
                        break;
                }
            }
        });

        wsdHandler = new Handler();
        wsdHandler.post(wsdRunnable);
    }

    Runnable wsdRunnable = new Runnable() {
        @Override
        public void run() {
            if (MyApp.comBeanWSD != null) {
                byte[] data = MyApp.comBeanWSD.recData;
                float wenduVer = MyFunc.fourbyte2float(data, 14);//温度
                float shiduVel = MyFunc.fourbyte2float(data, 18);//湿度
                float daqiyaVel = (float) MyFunc.fourByte2int(data, 22) / 100f;//大气压
                tvTilte.setText(df2.format(wenduVer) + "℃ " + df2.format(shiduVel) + "%RH " + df2.format(daqiyaVel) + "hPa");
            }
            wsdHandler.postDelayed(wsdRunnable, 2000);//两秒钟设置一次数据
        }
    };

    @OnClick({R.id.iv_left, R.id.tv_tilte})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_left://返回按钮
                finish();
                break;
            case R.id.tv_tilte:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (wsdHandler != null) {
            wsdHandler.removeCallbacks(wsdRunnable);
            wsdHandler = null;
        }
        svWsd.destroy();
        wsdSerialControl.close();
        MyApp.comBeanWSD = null;
    }
}

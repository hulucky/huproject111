package com.example.hu.huproject.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hu.huproject.Adapter.NvKongTimeAdapter;
import com.example.hu.huproject.Application.MyApp;
import com.example.hu.huproject.Bean.TimeBean1;
import com.example.hu.huproject.Bean.TimeBean2;
import com.example.hu.huproject.Entity.KongDongTimeEntity;
import com.example.hu.huproject.Entity.TaskEntity;
import com.example.hu.huproject.R;
import com.example.hu.huproject.Utils.MyFunc;
import com.example.hu.huproject.Utils.SerialControl;
import com.example.hu.huproject.Utils.TaskEntityUtils;
import com.mchsdk.paysdk.mylibrary.ListFragment;
import com.sensor.SensorData;
import com.sensor.SensorInf;
import com.sensor.view.SensorView;
import com.xzkydz.bean.ComBean;
import com.xzkydz.helper.ComControl;
import com.xzkydz.helper.SerialHelper;
import com.xzkydz.util.DataType;

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
import es.dmoral.toasty.Toasty;

import static com.example.hu.huproject.Application.MyApp.ackNumber;

//空动时间测试的测试界面
public class KongDongTimeTestFragment extends ListFragment {
    @BindView(R.id.tv_time1)
    TextView tvTime1;
    @BindView(R.id.tv_time2)
    TextView tvTime2;
    @BindView(R.id.tv_kdtime)
    TextView tvKdTime;
    @BindView(R.id.sv_time1)
    SensorView svTime1;
    @BindView(R.id.sv_time2)
    SensorView svTime2;
    @BindView(R.id.item0)
    TextView item0;
    @BindView(R.id.item1)
    TextView item1;
    @BindView(R.id.item2)
    TextView item2;
    @BindView(R.id.item3)
    TextView item3;
    @BindView(R.id.item4)
    TextView item4;
    @BindView(R.id.ll_tittle_name)
    LinearLayout llTittleName;
    @BindView(R.id.line)
    View line;
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.fl_data)
    RelativeLayout flData;
    @BindView(R.id.tv_kongtime)
    TextView tvKongtime;
    @BindView(R.id.tv_save)
    TextView tvSave;
    @BindView(R.id.tv_start)
    TextView tvStart;
    Unbinder unbinder;

    private TimeBean1 timeBean1;
    private TimeBean2 timeBean2;
    private KongSerialControl comA;
    private boolean isTime1Connected;
    private boolean isTime2Connected;
    private boolean isRunning;
    private int numButonClick = 0;//开始按钮点击的次数，用来判断两个时间包的序号是否一致
    private boolean isGetTime1Ack;
    private boolean isGetTime2Ack;
    private float breakTime1;//信号1断开时间
    private float breakTime2;//信号2断开时间
    private int ackNumber = 0;
    private boolean isAckSync;//ACK是否同步标志
    private Long id_param;
    private DecimalFormat df1 = new DecimalFormat("0.0");
    private DecimalFormat df2 = new DecimalFormat("0.00");
    private DecimalFormat df3 = new DecimalFormat("0.000");
    private List<KongDongTimeEntity> kongDongTimeEntities;
    private NvKongTimeAdapter kongTimeAdapter;
    public int testingNum = 0; //本次测试的序号
    private TaskEntity taskEntity;
    private MyApp myApp;
    private int time1Xuhao = 0;
    private int time2Xuhao = 0;
//    private long timeMillions1 = 0;
//    private long timeMillions2 = 0;

    @Override
    protected void lazyLoadData() {

    }

    @Override
    protected void initView() {
        myApp = MyApp.getInstance();
        taskEntity = MyApp.getTaskEntity();
        id_param = taskEntity.getTaskId();
        kongDongTimeEntities = new ArrayList<>();
        kongTimeAdapter = new NvKongTimeAdapter(getContext(), kongDongTimeEntities, KongDongTimeTestFragment.this);
        listview.setAdapter(kongTimeAdapter);
        //打开串口
        comA = new KongSerialControl(getContext(), DataType.DATA_OK_PARSE);
        comA.setiDelay(20);
        ComControl.OpenComPort(comA);
        svTime1.setOnStatusChangeListener(new MyOnStatusChanger());
        svTime2.setOnStatusChangeListener(new MyOnStatusChanger());
    }

    @OnClick({R.id.tv_save, R.id.tv_start})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_start://开始
                if (isTime1Connected && isTime2Connected) {
                    if (isRunning) {//如果正在运行，那么此时的按钮显示为“记录”，那么点击之后变为开始
                        //记录
                        tvStart.setText("开始");
                        isRunning = false;
                        KongDongTimeEntity kongEntity = new KongDongTimeEntity();
                        kongEntity.setKey(id_param);//id
                        kongEntity.setBreakTime1(df3.format(breakTime1));//时间1
                        kongEntity.setBreakTime2(df3.format(breakTime2));//时间2
                        float b1 = (float) Math.round(breakTime1 * 1000) / 1000f;
                        float b2 = (float) Math.round(breakTime2 * 1000) / 1000f;
                        kongEntity.setBreakTime(df3.format(Math.abs(b2 - b1)));//空动时间
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String str = sdf.format(new Date());
                        kongEntity.setCreateTime(str);//创建时间
                        kongDongTimeEntities.add(kongEntity);
                        kongTimeAdapter.updateListView(kongDongTimeEntities);
                        Toasty.success(getContext(), "成功记录一条测试数据", 1).show();

                    } else {
                        //开始
                        //4B59 0B    FFFF    0001      01      02        FF      FF     0000       E3           EA
                        //帧头 长度 目的地址 生产批号 客户编码 仪器编码  设备类型 设备编号  序号  (13，指令)E3    (14)校验
                        //                                                                         227           234
                        isGetTime1Ack = false;
                        isGetTime2Ack = false;
                        breakTime1 = 0;
                        breakTime2 = 0;
                        time1Xuhao = 0;
                        time2Xuhao = 0;
                        tvTime1.setText("");
                        tvTime2.setText("");
                        tvKdTime.setText("");
                        isAckSync = false;
                        numButonClick += 1;//每点击一次，序号加一
                        ackNumber = numButonClick % 4;
                        switch (ackNumber) {
                            case 0:
                                comA.sendHex("4B590BFFFF00010102FFFF0000E3EA");
                                break;
                            case 1://第一次发这个
                                comA.sendHex("4B590BFFFF00010102FFFF0001E3EB");
                                break;
                            case 2:
                                comA.sendHex("4B590BFFFF00010102FFFF0002E3E8");
                                break;
                            case 3:
                                comA.sendHex("4B590BFFFF00010102FFFF0003E3E9");
                                break;
                        }
//                        try {
//                            //延迟0.2s后再拿值
//                            Thread.currentThread().sleep(200);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        Log.i("ack", "isAckSync=== " + isAckSync);
//                        if (isAckSync) {
//                            tvStart.setText("记录");
//                            isRunning = true;
//                        } else {
//                            Toast.makeText(getContext(), "传感器未同步!!!", Toast.LENGTH_SHORT).show();
//                        }
                    }
                } else {
                    Toast.makeText(getContext(), "传感器未连接！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_save://保存
                if (isTime1Connected && isTime2Connected) {
                    if (!isRunning) {
                        //保存
                        //第一次testingNum全部保存为0，第二次全部为1，以此类推，用以区分是第几次测试
                        //当没有增加新数据时，testingNum也会自动增长，此时那一组测试的数据就为空
                        for (int i = 0; i < kongDongTimeEntities.size(); i++) {
                            KongDongTimeEntity kongDongTimeEntity = kongDongTimeEntities.get(i);
                            //过滤掉已经保存过的，从而给qianYinLiEntity设置
                            // 不同的testingNunm，用来区分序号
                            if (kongDongTimeEntity.getIsSave()) {
                                continue;
                            }
                            kongDongTimeEntity.setTestingNum(testingNum);
                            kongDongTimeEntity.setIsSave(true);
                            myApp.getmDaoSession().getKongDongTimeEntityDao().insert(kongDongTimeEntity);
                        }
                        testingNum = testingNum + 1;
                        taskEntity.setIs_KongTimeSave(true);
                        TaskEntityUtils.update(taskEntity);
                        Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                                "保存成功！", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);// 居中显示
                        toast.show();
                    } else {
                        Toast.makeText(getContext(), "请停止测试后再保存！", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "传感器尚未连接，无法保存！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    //点击删除按钮时，执行的事件
    public void showInitRes(KongDongTimeEntity kongDongTimeEntity, List<KongDongTimeEntity> list) {
        this.kongDongTimeEntities = list;
        if (kongDongTimeEntity.getIsSave()) {
            myApp.getmDaoSession().getKongDongTimeEntityDao().delete(kongDongTimeEntity);
        }
    }

    //串口控制类
    public class KongSerialControl extends SerialHelper {

        public KongSerialControl(Context context, int mDataType) {
            super(context, mDataType);
        }

        public KongSerialControl(Context context, String sPort, String sBaudRate, int mDataType) {
            super(context, sPort, sBaudRate, mDataType);
        }

        @Override
        protected void onDataReceived(ComBean comBean) {
            if (comBean.recDataType == 81) {//时间传感器
                if (comBean.recData[10] == 1) {//开始信号
                    isTime1Connected = true;
                    Log.i("asdasd", "信号1：" + Arrays.toString(comBean.recData));
                    if (comBean.recData.length == 18 && comBean.recData[13] != -29) {
                        if (timeBean1 == null) {
                            timeBean1 = new TimeBean1(comBean.recData);
                        } else {
                            timeBean1.caculate(comBean.recData);
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                SensorData sensorData = new SensorData(timeBean1.getPower(), timeBean1.getSignal(), SensorInf.NORMAL, System.currentTimeMillis());
                                //时刻更新传感器状态
                                svTime1.setData(sensorData);
                            }
                        });
                    }
                    //开始计时响应的包
                    if (comBean.recData.length == 18 && comBean.recData[13] == -29 && comBean.recData[12] == ackNumber) {
                        isGetTime1Ack = true;
                    } else if (comBean.recData.length == 18 && comBean.recData[13] == -29 && comBean.recData[12] != ackNumber) {
                        isGetTime1Ack = false;
                    }
                    //时间传感器电平变化就会测试时间数据
                    if (isGetTime1Ack && comBean.recData.length == 22) {
                        time1Xuhao = time1Xuhao + 1;
                        if (time1Xuhao == 1) {//只拿第一次的值，防止跳动
                            float miao = (float) MyFunc.twobyteToint_(comBean.recData[14], comBean.recData[15]);
                            float haomaio = (float) MyFunc.twobyteToint_(comBean.recData[16], comBean.recData[17]) * 0.1f / 1000f;
                            breakTime1 = miao + haomaio;

                            tvTime1.setText(df3.format(breakTime1));
                        }
                        if (!tvTime2.getText().toString().equals("")) {
                            float v = Math.round(breakTime2 * 1000) / 1000f - Math.round(breakTime1 * 1000) / 1000f;
                            tvKdTime.setText(df3.format(Math.abs(v)));
                        }
                    }

                }


                if (comBean.recData[10] == 2) {//结束信号
                    isTime2Connected = true;
                    Log.i("asdasd", "信号2：" + Arrays.toString(comBean.recData));
                    if (comBean.recData.length == 18 && comBean.recData[13] != -29) {
                        if (timeBean2 == null) {
                            timeBean2 = new TimeBean2(comBean.recData);
                        } else {
                            timeBean2.caculate(comBean.recData);
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                SensorData sensorData = new SensorData(timeBean2.getPower(), timeBean2.getSignal(), SensorInf.NORMAL, System.currentTimeMillis());
                                //时刻更新传感器状态
                                svTime2.setData(sensorData);
                            }
                        });
                    }
                    //开始计时响应的包
                    if (comBean.recData.length == 18 && comBean.recData[13] == -29 && comBean.recData[12] == ackNumber) {
                        isGetTime2Ack = true;
                    } else if (comBean.recData.length == 18 && comBean.recData[13] == -29 && comBean.recData[12] != ackNumber) {
                        isGetTime2Ack = false;
                    }
                    //时间传感器电平变化就会测试时间数据
                    if (isGetTime2Ack && comBean.recData.length == 22) {
                        time2Xuhao = time2Xuhao + 1;
                        if (time2Xuhao == 1) {//只拿第一次的值，防止跳动
                            float miao = (float) MyFunc.twobyteToint_(comBean.recData[14], comBean.recData[15]);
                            float haomaio = (float) MyFunc.twobyteToint_(comBean.recData[16], comBean.recData[17]) * 0.1f / 1000f;
                            breakTime2 = miao + haomaio;
                            //实时显示断开时间
                            tvTime2.setText(df3.format(breakTime2));
                        }
                        if (!tvTime1.getText().toString().equals("")) {
                            float v = Math.round(breakTime2 * 1000) / 1000f - Math.round(breakTime1 * 1000) / 1000f;
                            tvKdTime.setText(df3.format(Math.abs(v)));
                        }
                    }
                }
            }
            //当ACK同步时，按钮变为记录
            if (comBean.recData[13] == -29 & isGetTime1Ack && isGetTime2Ack) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("jjjjj", "记录");
                        Toast.makeText(getContext(), "开始成功", Toast.LENGTH_SHORT).show();
                        isAckSync = true;
                        tvStart.setText("记录");
                        isRunning = true;
                    }
                });
            }
        }


    }


    private class MyOnStatusChanger implements SensorView.OnStatusChangeListener {
        @Override
        public void status(View view, int i, int i1) {
            int id = view.getId();
            if (i1 == SensorInf.SEARCHING) {
                switch (id) {
                    case R.id.sv_time1://开始信号
                        Toast.makeText(getContext(), "时间传感器1断开", Toast.LENGTH_SHORT).show();
                        isTime1Connected = false;
                        break;
                    case R.id.sv_time2://结束信号
                        isTime2Connected = false;
                        Toast.makeText(getContext(), "时间传感器2断开", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }
    }


    @Override
    protected int setLayoutId() {
        return R.layout.fragment_kongdongtime_test;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        ackNumber = 0;
        isAckSync = false;
        svTime1.destroy();
        svTime2.destroy();
        comA.close();

        testingNum = 0;//退出时，把测试序号清零
    }
}

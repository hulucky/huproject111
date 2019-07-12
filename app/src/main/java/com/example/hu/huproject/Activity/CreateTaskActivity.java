package com.example.hu.huproject.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hu.huproject.Application.MyApp;
import com.example.hu.huproject.Entity.TaskEntity;
import com.example.hu.huproject.Interface.IGreate;
import com.example.hu.huproject.Interface.ISensorInf;
import com.example.hu.huproject.R;
import com.example.hu.huproject.TaskInfoHu.TaskInfo;
import com.example.hu.huproject.Utils.ExpandableLayout;
import com.example.hu.huproject.Utils.EyesUtils;
import com.example.hu.huproject.Utils.StartActivity;
import com.example.hu.huproject.Utils.TaskEntityUtils;
import com.example.hu.huproject.Utils.WSDSerialControl;
import com.xzkydz.function.motor.module.ConstantData;
import com.xzkydz.helper.ComControl;
import com.xzkydz.util.DataType;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateTaskActivity extends AppCompatActivity implements IGreate {
    @BindView(R.id.et_1)
    public EditText et1;
    @BindView(R.id.iv_delete_1)
    ImageView ivDelete1;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.relativeLayout1)
    RelativeLayout relativeLayout1;
    @BindView(R.id.et_2)
    public EditText et2;
    @BindView(R.id.iv_delete_2)
    ImageView ivDelete2;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.relativeLayout2)
    RelativeLayout relativeLayout2;
    @BindView(R.id.et_3)
    public EditText et3;
    @BindView(R.id.iv_delete_3)
    ImageView ivDelete3;
    @BindView(R.id.tv3)
    TextView tv3;
    @BindView(R.id.relativeLayout3)
    RelativeLayout relativeLayout3;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbarlayout)
    AppBarLayout appbarlayout;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.iv_speed_and)
    ImageView ivSpeedAnd;
    @BindView(R.id.linear_speed_and_angle)
    LinearLayout linearSpeedAndAngle;
    @BindView(R.id.tv_speedandangle)
    TextView tvSpeedandangle;
    @BindView(R.id.expand_speed_and_angle)
    ExpandableLayout expandSpeedAndAngle;
    @BindView(R.id.view1)
    View view1;
    @BindView(R.id.tv_speed)
    TextView tvSpeed;
    @BindView(R.id.view2)
    View view2;
    @BindView(R.id.tv_angle)
    TextView tvAngle;
    @BindView(R.id.iv_zhidong)
    ImageView ivZhidong;
    @BindView(R.id.linear_zhidong)
    LinearLayout linearZhidong;
    @BindView(R.id.tv_kongdong_time)
    TextView tvKongdongTime;
    @BindView(R.id.view3)
    View view3;
    @BindView(R.id.tv_manzai_xiangxia)
    TextView tvManzaiXiangxia;
    @BindView(R.id.view4)
    View view4;
    @BindView(R.id.tv_kongzai_xiangshang)
    TextView tvKongzaiXiangshang;
    @BindView(R.id.expand_zhidong)
    ExpandableLayout expandZhidong;
    @BindView(R.id.iv_system_power)
    ImageView ivSystemPower;
    @BindView(R.id.linear_system_power)
    LinearLayout linearSystemPower;
    @BindView(R.id.tv_qianyinli)
    TextView tvQianyinli;
    @BindView(R.id.view5)
    View view5;
    @BindView(R.id.tv_zhidongli)
    TextView tvZhidongli;
    @BindView(R.id.view6)
    View view6;
    @BindView(R.id.tv_huishenglun)
    TextView tvHuishenglun;
    @BindView(R.id.expand_system_power)
    ExpandableLayout expandSystemPower;
    //Edittext的输入内容
    private String et_input1;
    private String et_input2;
    private String et_input3;
    private boolean isExpand1 = true;//速度与角度是否张开
    private boolean isExpand2 = true;//制动性能是否张开
    private boolean isExpand3 = true;//系统力是否张开
    private TaskInfo taskInfo;
    private Context context;
    private String className = "";
    private TaskEntity taskEntity;//总任务
    private WSDSerialControl wsdSerialControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        ButterKnife.bind(this);
        context = this;

        if (Build.VERSION_CODES.M > Build.VERSION.SDK_INT) { //小于6.0
            EyesUtils.setStatusBarColorTw(this, ContextCompat.getColor(this, R.color.orange_my));    //设置标题栏透明白，字体为黑色
        } else {
            EyesUtils.setStatusBarLightMode(this, Color.WHITE);  //设置状态栏颜色和字体颜色
        }
        expandSpeedAndAngle.setOrientation(ExpandableLayout.VERTICAL);
//        wsdSerialControl = new WSDSerialControl(context, DataType.DATA_OK_PARSE);
//        wsdSerialControl.setiDelay(50);
//        ComControl.OpenComPort(wsdSerialControl);
        appbarlayoutStatus();
        taskInfo = new TaskInfo(this);
        //Edittext获取焦点监听以及Edittext输入内容变化监听
        initView();
    }

    //appBarLayout的打开与关闭状态
    private void appbarlayoutStatus() {
        collapsingToolbarLayout.setTitle("测试项目");
        // collapsingToolbarLayout.setExpandedTitleColor(Color.parseColor("#fe8107"));
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(toolbar);
        //添加返回箭头
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.mipmap.icon_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //Edittext获取焦点监听以及Edittext输入内容变化监听
    private void initView() {
        //EditText获取焦点事件监听
        et1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    relativeLayout2.setVisibility(View.GONE);
                    relativeLayout3.setVisibility(View.GONE);
                } else {
                    relativeLayout2.setVisibility(View.VISIBLE);
                    relativeLayout3.setVisibility(View.VISIBLE);
                }
            }
        });
        et2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    relativeLayout1.setVisibility(View.GONE);
                    relativeLayout3.setVisibility(View.GONE);
                } else {
                    relativeLayout1.setVisibility(View.VISIBLE);
                    relativeLayout3.setVisibility(View.VISIBLE);
                }
            }
        });
        et3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    relativeLayout1.setVisibility(View.GONE);
                    relativeLayout2.setVisibility(View.GONE);
                } else {
                    relativeLayout1.setVisibility(View.VISIBLE);
                    relativeLayout2.setVisibility(View.VISIBLE);
                }
            }
        });

        //Edittext输入内容变化监听
        et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                et_input1 = et1.getText().toString().trim();
                int length = et_input1.length();//输入的长度
                tv1.setText(length + "/20");
                if (length == 20) {
                    Toast.makeText(CreateTaskActivity.this, "最多输入20个字符", Toast.LENGTH_SHORT).show();
                }
                if (!et_input1.equals("")) {
                    ivDelete1.setVisibility(View.VISIBLE);
                } else {
                    ivDelete1.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                et_input2 = et2.getText().toString().trim();
                int length = et_input2.length();//输入的长度
                tv2.setText(length + "/20");
                if (length == 20) {
                    Toast.makeText(CreateTaskActivity.this, "最多输入20个字符", Toast.LENGTH_SHORT).show();
                }
                if (!et_input2.equals("")) {
                    ivDelete2.setVisibility(View.VISIBLE);
                } else {
                    ivDelete2.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                et_input3 = et3.getText().toString().trim();
                int length = et_input3.length();//输入的长度
                tv3.setText(length + "/20");
                if (length == 20) {
                    Toast.makeText(CreateTaskActivity.this, "最多输入20个字符", Toast.LENGTH_SHORT).show();
                }
                if (!et_input3.equals("")) {
                    ivDelete3.setVisibility(View.VISIBLE);
                } else {
                    ivDelete3.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
//        tvSpeedandangle.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()){
//                    case MotionEvent.ACTION_DOWN:
//                        tvSpeedandangle.setBackgroundColor(getResources().getColor(R.color.orange_my));
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                    case MotionEvent.ACTION_UP:
//                        tvSpeedandangle.setBackgroundColor(getResources().getColor(R.color.white));
//                        break;
//
//                }
//                return false;
//            }
//        });
    }


    @OnClick({R.id.fab, R.id.iv_delete_1, R.id.iv_delete_2, R.id.iv_delete_3, R.id.linear_speed_and_angle, R.id.linear_zhidong,
            R.id.linear_system_power, R.id.tv_speedandangle, R.id.tv_speed, R.id.tv_angle,
            R.id.tv_kongdong_time, R.id.tv_manzai_xiangxia, R.id.tv_kongzai_xiangshang,
            R.id.tv_qianyinli, R.id.tv_zhidongli, R.id.tv_huishenglun})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fab://FloatActionButton的点击事件
                Intent intent = new Intent();
                intent.setClass(this, HistoryTaskSearchActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.iv_delete_1://Edittext后删除图标的点击事件
                et1.setText("");
                break;
            case R.id.iv_delete_2:
                et2.setText("");
                break;
            case R.id.iv_delete_3:
                et3.setText("");
                break;
            case R.id.linear_speed_and_angle://速度与角度是折叠还是张开
                if (isExpand1) {
                    expandSpeedAndAngle.collapse();
                    ivSpeedAnd.setImageResource(R.mipmap.rwxx_icon_close_bt);
                    isExpand1 = false;
                } else {
                    expandSpeedAndAngle.expand();
                    ivSpeedAnd.setImageResource(R.mipmap.rwxx_icon_open_bt);
                    isExpand1 = true;
                }
                break;
            case R.id.linear_zhidong://制动性能是折叠还是张开
                if (isExpand2) {
                    expandZhidong.collapse();
                    ivZhidong.setImageResource(R.mipmap.rwxx_icon_close_bt);
                    isExpand2 = false;
                } else {
                    expandZhidong.expand();
                    ivZhidong.setImageResource(R.mipmap.rwxx_icon_open_bt);
                    isExpand2 = true;
                }
                break;
            case R.id.linear_system_power://系统力是折叠还是张开
                if (isExpand3) {
                    expandSystemPower.collapse();
                    ivSystemPower.setImageResource(R.mipmap.rwxx_icon_close_bt);
                    isExpand3 = false;
                } else {
                    expandSystemPower.expand();
                    ivSystemPower.setImageResource(R.mipmap.rwxx_icon_open_bt);
                    isExpand3 = true;
                }
                break;
            case R.id.tv_speedandangle://速度与角度测试
                if (!taskInfo.checkParRight(context)) {//检查参数
                    return;
                }
                getTaskEntity();//拿到测试任务
                Intent intent1 = new Intent(this, Speed_Angle_TestActivity.class);
                startActivity(intent1);
                break;
            case R.id.tv_speed://速度测试
                if (!taskInfo.checkParRight(context)) {//检查参数
                    return;
                }
                getTaskEntity();
                Intent intent2 = new Intent(this, SpeedTestActivity.class);
                startActivity(intent2);
                break;
            case R.id.tv_angle://角度测试
                if (!taskInfo.checkParRight(context)) {//检查参数
                    return;
                }
                getTaskEntity();
                Intent intent3 = new Intent(this, AngleTestActivity.class);
                startActivity(intent3);
                break;
            case R.id.tv_kongdong_time://空动时间测试
                if (!taskInfo.checkParRight(context)) {//检查参数
                    return;
                }
                getTaskEntity();
                Intent intent4 = new Intent(this, KongDongTime_TestActivity.class);
                startActivity(intent4);
                break;
            case R.id.tv_manzai_xiangxia://满载向下制动距离测试
                if (!taskInfo.checkParRight(context)) {//检查参数
                    return;
                }
                getTaskEntity();
                Intent intent5 = new Intent(this, ManZaiXia_TestActivity.class);
                startActivity(intent5);
                break;
            case R.id.tv_kongzai_xiangshang://空载向上制动减速度测试
                if (!taskInfo.checkParRight(context)) {//检查参数
                    return;
                }
                getTaskEntity();
                Intent intent6 = new Intent(this, KongZaiUp_TestActivity.class);
                startActivity(intent6);
                break;
            case R.id.tv_qianyinli://牵引力测试
                if (!taskInfo.checkParRight(context)) {//检查参数
                    return;
                }
                getTaskEntity();
                Intent intent7 = new Intent(this, QianYinLi_TestActivity.class);
                startActivity(intent7);
                break;
            case R.id.tv_zhidongli://制动力测试
                if (!taskInfo.checkParRight(context)) {//检查参数
                    return;
                }
                getTaskEntity();
                Intent intent8 = new Intent(this, ZhiDongli_TestActivity.class);
                startActivity(intent8);
                break;
            case R.id.tv_huishenglun://回绳轮预张力测试
                if (!taskInfo.checkParRight(context)) {//检查参数
                    return;
                }
                getTaskEntity();
                Intent intent9 = new Intent(this, HuiShengLun_TestActivity.class);
                startActivity(intent9);
                break;
        }
    }

    private void getTaskEntity() {
        //taskEntity为空的话说明此时是新建任务，不为空说明此时是继续测试
        //继续测试时，在HistoryTaskSearchActivity类中已经提前设置了taskEntity
        taskEntity = MyApp.getTaskEntity();
        if (taskEntity == null) {
            taskEntity = taskInfo.newBuildTaskEntity();
            MyApp.setTaskEntity(taskEntity);
            TaskEntityUtils.insert(taskEntity);
        }
    }

    //从历史搜索界面返回时会调用此方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TaskEntity taskEntity1 = MyApp.getTaskEntity();
        if (taskEntity1 == null) {
            Log.i("qqqqq", "taskEntity1=null");
        } else {//如果此时有任务，说明点击了继续测试，此时继续测试即可
            Log.i("qqqqq", "taskEntity1.ID==" + taskEntity1.getTaskId());
            et1.setText(taskEntity1.getUnitName());//受检单位名称
            et2.setText(taskEntity1.getConvNumber());//单轨机编号
            et3.setText(taskEntity1.getPeopleName());//测试人员名字
        }
        if (resultCode == 55) {//复用参数
            Long taskId = data.getLongExtra(ConstantData.HistoryTask_ID_resultCode, 1L);
            Log.i("qqqqq", "taskId: " + taskId);
            TaskEntityUtils greateTaskUtils = new TaskEntityUtils();
            // 这个只是复用参数的历史任务，参数有可能在此基础上更改，不一定就是测试任务。
            TaskEntity taskEntity = greateTaskUtils.query(taskId);
            //根据历史任务初始化界面
            this.setParForHistoryTask(taskEntity);
        }
    }

    /*用选择的已完成历史任务，初始化当前界面*/
    @Override
    public void setParForHistoryTask(TaskEntity taskEntity) {
        et1.setText(taskEntity.getUnitName());//受检单位名称
        et2.setText(taskEntity.getConvNumber());//单轨机编号
        et3.setText(taskEntity.getPeopleName());//测试人员名字
        MyApp.setTaskEntity(null);//复用参数之后，将taskEntity清空，再次点击测试会新建任务
    }

    /**
     * 监听Back键按下事件,方法2:
     * 注意:
     * 返回值表示:是否能完全处理该事件
     * 在此处返回false,所以会继续传播该事件.
     * 在具体项目中此处的返回值视情况而定.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (MyApp.taskEntity == null) {
                Log.i("kkk", "onKeyDown: taskEntity==null");
                finish();
                return true;
            } else {
                Log.i("kkk", "onKeyDown: taskEntity!=null");
                Log.i("qqqqq", "MyApp.taskEntity.id=" + MyApp.taskEntity.getTaskId());
                onBackClick(MyApp.taskEntity);
                return true;
            }
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private void onBackClick(final TaskEntity taskEntity) {
        AlertDialog.Builder normalDialog = new AlertDialog.Builder(this);
        normalDialog.setTitle("选择此任务状态");
        normalDialog.setMessage("测试任务是否完成?");
        normalDialog.setPositiveButton("已完成",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        taskEntity.set_IsCompleteTask(true);
                        TaskEntityUtils.update(taskEntity);
                        MyApp.setTaskEntity(null);
                        finish();
                    }
                });
        normalDialog.setNegativeButton("未完成",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        taskEntity.set_IsCompleteTask(false);
                        TaskEntityUtils.update(taskEntity);
                        MyApp.setTaskEntity(null);
                        finish();
                    }
                });
        // 创建实例并显示
        normalDialog.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //此界面被销毁时，将测试任务置空
        Log.i("kkk", "onDestroy: ");
        MyApp.setTaskEntity(null);
//        wsdSerialControl.close();
    }
}

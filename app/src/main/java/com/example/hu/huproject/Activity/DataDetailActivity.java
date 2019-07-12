package com.example.hu.huproject.Activity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.db.manager.HuiShengLunEntityDao;
import com.db.manager.KongDongTimeEntityDao;
import com.db.manager.QianYinLiEntityDao;
import com.db.manager.ZhiDongLiEntityDao;
import com.example.hu.huproject.Application.MyApp;
import com.example.hu.huproject.Bean.GetReportBean;
import com.example.hu.huproject.DetailFragment.Detail_Angle_fragment;
import com.example.hu.huproject.DetailFragment.Detail_HuiShengLun_fragment;
import com.example.hu.huproject.DetailFragment.Detail_KongDongTime_fragment;
import com.example.hu.huproject.DetailFragment.Detail_KongZaiUp_fragment;
import com.example.hu.huproject.DetailFragment.Detail_ManZaiXia_fragment;
import com.example.hu.huproject.DetailFragment.Detail_QianYinLi_fragment;
import com.example.hu.huproject.DetailFragment.Detail_SpeedAngle_fragment;
import com.example.hu.huproject.DetailFragment.Detail_Speed_fragment;
import com.example.hu.huproject.DetailFragment.Detail_ZhiDongLi_fragment;
import com.example.hu.huproject.Entity.AngleEntity;
import com.example.hu.huproject.Entity.HuiShengLunEntity;
import com.example.hu.huproject.Entity.KongDongTimeEntity;
import com.example.hu.huproject.Entity.KongZaiUpEntity;
import com.example.hu.huproject.Entity.ManZaiDownEntity;
import com.example.hu.huproject.Entity.QianYinLiEntity;
import com.example.hu.huproject.Entity.SpeedAngleEntity;
import com.example.hu.huproject.Entity.SpeedEntity;
import com.example.hu.huproject.Entity.TaskEntity;
import com.example.hu.huproject.Entity.ZhiDongLiEntity;
import com.example.hu.huproject.Fragment.NullDataFragment;
import com.example.hu.huproject.Fragment.SpeedAngleFragmentVp1;
import com.example.hu.huproject.Fragment.WeightFragment;
import com.example.hu.huproject.R;
import com.example.hu.huproject.Utils.DBHelper;
import com.example.hu.huproject.Utils.TaskEntityUtils;
import com.jaeger.library.StatusBarUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;

import jxl.Workbook;
import jxl.write.WritableWorkbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableImage;
import jxl.write.WritableSheet;
import jxl.write.WriteException;


public class DataDetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.rb_0)
    RadioButton rb0;
    @BindView(R.id.rb_1)
    RadioButton rb1;
    @BindView(R.id.rb_2)
    RadioButton rb2;
    @BindView(R.id.rb_3)
    RadioButton rb3;
    @BindView(R.id.rb_4)
    RadioButton rb4;
    @BindView(R.id.rb_5)
    RadioButton rb5;
    @BindView(R.id.rb_6)
    RadioButton rb6;
    @BindView(R.id.rb_7)
    RadioButton rb7;
    @BindView(R.id.rb_8)
    RadioButton rb8;
    @BindView(R.id.rg_one)
    RadioGroup rgOne;
    @BindView(R.id.rg_two)
    RadioGroup rgTwo;
    @BindView(R.id.rg_three)
    RadioGroup rgThree;
    @BindView(R.id.ll_choose)
    LinearLayout llChoose;
    @BindView(R.id.fragment)
    FrameLayout fragment;
    @BindView(R.id.sp_choose_data)
    Spinner spChooseData;
    @BindView(R.id.cb_join_sport)
    CheckBox cbJoinSport;
    @BindView(R.id.ll_tittle)
    LinearLayout llTittle;
    @BindView(R.id.content_data_detail)
    RelativeLayout contentDataDetail;


    private NullDataFragment nullDataFragment; //空数据填充
    private WeightFragment weightFragment;
    public static int height;//appLayout的高度
    private Intent intent;
    public Long taskID;
    public MyApp mApp;
    public TaskEntity taskEntity;

    //加入报告的数据库表单
    public KongDongTimeEntity kongDongTimeEntity = null;
    public SpeedEntity speedEntity = null;
    public AngleEntity angleEntity = null;
    public SpeedAngleEntity speedAngleEntity = null;
    public ManZaiDownEntity manZaiDownEntity = null;
    public KongZaiUpEntity kongZaiUpEntity = null;
    public QianYinLiEntity qianYinLiEntity = null;

    private List<SpeedAngleEntity> speedAngleEntities;//速度与角度
    private List<SpeedEntity> speedEntities;//速度
    private List<AngleEntity> angleEntities;//角度
    public List<KongDongTimeEntity> kongDongTimeEntities;//空动时间
    private List<ManZaiDownEntity> manZaiDownEntities;//满载向下制动距离
    private List<KongZaiUpEntity> kongZaiUpEntities;//空载向上制动减速度
    public List<QianYinLiEntity> qianYinLiEntities;//牵引力测试
    public List<ZhiDongLiEntity> zhiDongLiEntities;//制动力测试
    public List<HuiShengLunEntity> huiShengLunEntities;//回绳轮预张力测试

    private DBHelper dBManager;
    //存储当前选中状态下的RadioButton的ID：清除选中状态的按钮用于判断清除哪个项目下的数据
    public int radioButtonID = R.id.rb_0;//rb_0指的是第一个按钮
    public int dataNum = 0;//记录Spinner选中位置
    //用于初始化Chart 中的Checkbox 的状态、同时用于判断哪些表单加入测试报告
    private Boolean[] dataInsertReportArry = {false, false, false, false, false, false, false, false, false};
    private GetReportBean getReportBean;//生成测试报告的数据Bean
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    ///用来存放spinner数据
    private ArrayMap<String, Integer> map = new ArrayMap<>();
    private Drawable drawable;//radioButton未被选中时的图标
    private Drawable drawableChecked;//radioButton选中后的图标
    private Detail_SpeedAngle_fragment detail_speedangle_fragment;//速度与角度测试界面
    private Detail_Speed_fragment detail_speed_fragment;//速度测试界面
    private Detail_Angle_fragment detail_angle_fragment;//角度测试界面
    private Detail_KongDongTime_fragment detail_kongDongTime_fragment;//空动时间
    private Detail_ManZaiXia_fragment manZaiXia_fragment;//满载向下
    private Detail_KongZaiUp_fragment detail_kongZaiUp_fragment;//空载向上
    private Detail_QianYinLi_fragment detail_qianYinLi_fragment;//牵引力
    private Detail_ZhiDongLi_fragment detail_zhiDongLi_fragment;//制动力
    private Detail_HuiShengLun_fragment detail_huiShengLun_fragment;//回绳轮预张力
    private FragmentTransaction transaction;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_detail);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  //决定左上角的图标是否可以点击
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        StatusBarUtil.setColor(this, getResources().getColor(R.color.tittleBar), 0);
        this.setTitle("详细测试数据");
        drawable = getResources().getDrawable(R.drawable.icon_checkbox);
        drawableChecked = getResources().getDrawable(R.drawable.icon_checkbox_checked);
        // 得到DBHelper对象
        dBManager = DBHelper.getInstance(this);
        sp = this.getSharedPreferences("data", Context.MODE_PRIVATE);
        editor = sp.edit();
        ViewTreeObserver vto = appBarLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //移除上一次监听，避免重复监听
                appBarLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                //在这里调用getHeight（）获得控件的高度
                height = appBarLayout.getHeight();
            }
        });
        mApp = MyApp.getInstance();
        intent = getIntent();
        //详细数据界面获取传过来的“任务ID” ，注意TAG用的是“TASKID”
        taskID = intent.getLongExtra("TASKID", 1L);
        taskEntity = TaskEntityUtils.query(taskID);
        MyApp.setTaskEntity(taskEntity);
        initData(); //初始化测试数据
        initEvent(); //事件
    }

    private void initEvent() {
        MyRadioButtonOnCheckedChangeListener checkedChangeListener = new MyRadioButtonOnCheckedChangeListener();
        rgOne.setOnCheckedChangeListener(checkedChangeListener);//radiogroup设置监听
        rgTwo.setOnCheckedChangeListener(checkedChangeListener);
        rgThree.setOnCheckedChangeListener(checkedChangeListener);
        cbJoinSport.setOnCheckedChangeListener(new MyJoinDataOnCheckBoxChangeListener());//checkbox设置监听
    }

    //各个测试项目的测试数据
    private void initData() {
        //根据taskID查询到所有的测试数据（taskEntity的id就是speedEntity的key）
        speedAngleEntities = dBManager.querySpeedAngleAllById(taskID);
        speedEntities = dBManager.querySpeedAllById(taskID);
        angleEntities = dBManager.queryAngleAllById(taskID);
        kongDongTimeEntities = dBManager.queryKongDongTimeAllById(taskID);
        manZaiDownEntities = dBManager.queryManZaiDownAllById(taskID);
        kongZaiUpEntities = dBManager.queryKongZaiUpAllById(taskID);
        qianYinLiEntities = dBManager.queryQianYinLiAllById(taskID);
        zhiDongLiEntities = dBManager.queryZhiDongLiAllById(taskID);
        huiShengLunEntities = dBManager.queryHuiShengLunAllById(taskID);
        Log.i("datadetailsddd", "speedEntities.size=== " + speedEntities.size() + "    taskID===" + taskID);
        getReportBean = new GetReportBean();//生成测试报告的数据Bean
        if (nullDataFragment == null)
            nullDataFragment = new NullDataFragment();
        if (speedAngleEntities.size() > 0) {
            speedAngleEntity = speedAngleEntities.get(0);
        }
        if (speedEntities.size() > 0) {
            speedEntity = speedEntities.get(0);
        }
        if (angleEntities.size() > 0) {
            angleEntity = angleEntities.get(0);
        }
        if (manZaiDownEntities.size() > 0) {
            manZaiDownEntity = manZaiDownEntities.get(0);
        }
        if (kongZaiUpEntities.size() > 0) {
            kongZaiUpEntity = kongZaiUpEntities.get(0);
        }
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        rb0.setChecked(true);
        if (rb0.isChecked()) {
            rgTwo.clearCheck();//清除radiobutton的选中状态
            rgThree.clearCheck();//清除radiobutton的选中状态
            //存储当前选中状态下的RadioButton的ID：清除选中状态的按钮用于判断清除哪个项目下的数据
            radioButtonID = R.id.rb_0;
            //checkBox状态（false）
            cbJoinSport.setChecked(dataInsertReportArry[0]);
            //判断有没有保存
            if (rb0.isChecked()) {
                rgTwo.clearCheck();//清除第二列的选中状态
                rgThree.clearCheck();//清除第三列的选中状态
                radioButtonID = R.id.rb_0;//记录当前选中的id
                //dataInsertReportArry用于初始化Chart中的Checkbox的状态、
                // 同时用于判断哪些表单加入测试报告
                cbJoinSport.setChecked(dataInsertReportArry[0]);
                if (taskEntity.getIs_SpeedAngleSave() && speedAngleEntities.size() > 0) {
                    //spinner和checkbox所在的那一行
                    llTittle.setVisibility(View.VISIBLE);
                    detail_speedangle_fragment = new Detail_SpeedAngle_fragment();
                    transaction.replace(R.id.fragment, detail_speedangle_fragment).commit();
                    getEntityForSpinner();
                } else {
                    llTittle.setVisibility(View.INVISIBLE);
                    transaction.replace(R.id.fragment, nullDataFragment).commit();
                }
            }
        }
    }

    //获取同一测试项目下的所有数据的测试时间数组
    private void getEntityForSpinner() {
        int testingNum = 0;//每一项测试的总次数
        //radioButtonID:已选中的radiobutton
        switch (radioButtonID) {
            case R.id.rb_0://速度与角度
                testingNum = speedAngleEntities.size();
                break;
            case R.id.rb_1://速度
                //速度测试的总次数
                testingNum = speedEntities.size();
                break;
            case R.id.rb_2:
                testingNum = angleEntities.size();
                break;
            case R.id.rb_3://空动时间
                int kongtimeZu = 0;
                for (int i = 0; i < kongDongTimeEntities.size(); i++) {
                    //testingNum是从0开始计算的
                    //testingNum = 0  一组数据
                    //testingNum = 1  二组数据
                    KongDongTimeEntity kongDongTimeEntity = kongDongTimeEntities.get(i);
                    int xuhao = kongDongTimeEntity.getTestingNum();
                    Log.i("werwqewq", "TestingNum===: " + xuhao);
                    if (xuhao > kongtimeZu) {
                        kongtimeZu = xuhao;//找一共有几组
                    }
                }
                testingNum = kongtimeZu + 1;
                break;
            case R.id.rb_4://满载向下
                testingNum = manZaiDownEntities.size();
                break;
            case R.id.rb_5://空载向上
                testingNum = kongZaiUpEntities.size();
                break;
            case R.id.rb_6://牵引力
                int zu = 0;
                for (int i = 0; i < qianYinLiEntities.size(); i++) {
                    //testingNum是从0开始计算的
                    //testingNum = 0  一组数据
                    //testingNum = 1  二组数据
                    QianYinLiEntity qianYinLiEntity = qianYinLiEntities.get(i);
                    int xuhao = qianYinLiEntity.getTestingNum();
                    Log.i("werwqewq", "TestingNum===: " + xuhao);
                    if (xuhao > zu) {
                        zu = xuhao;//找一共有几组
                    }
                }
                testingNum = zu + 1;
                Log.i("werwqewq", "testingNum= " + testingNum);
                break;
            case R.id.rb_7://制动力
                int zdlZu = 0;
                for (int i = 0; i < zhiDongLiEntities.size(); i++) {
                    //testingNum是从0开始计算的
                    //testingNum = 0  一组数据
                    //testingNum = 1  二组数据
                    ZhiDongLiEntity zhiDongLiEntity = zhiDongLiEntities.get(i);
                    int xuhao = zhiDongLiEntity.getTestingNum();
                    Log.i("werwqewq", "TestingNum===: " + xuhao);
                    if (xuhao > zdlZu) {
                        zdlZu = xuhao;//找一共有几组
                    }
                }
                testingNum = zdlZu + 1;
                break;
            case R.id.rb_8://回绳轮预张力
                int hslZu = 0;
                for (int i = 0; i < huiShengLunEntities.size(); i++) {
                    //testingNum是从0开始计算的
                    //testingNum = 0  一组数据
                    //testingNum = 1  二组数据
                    HuiShengLunEntity huiShengLunEntity = huiShengLunEntities.get(i);
                    int xuhao = huiShengLunEntity.getTestingNum();
                    Log.i("werwqewq", "TestingNum===: " + xuhao);
                    if (xuhao > hslZu) {
                        hslZu = xuhao;//找一共有几组
                    }
                }
                testingNum = hslZu + 1;
                break;
        }
        List<String> numStrList = new ArrayList<>();
        for (int i = 1; i <= testingNum; i++) {
            numStrList.add("第" + i + "次测试数据");
        }
        //给spinner设置数据
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.item_spinner, R.id.tv_item_spinner, numStrList);
        spChooseData.setAdapter(adapter);
//        //速度与角度
//        if (radioButtonID == R.id.rb_0 && cbJoinSport.isChecked()) {
//            spChooseData.setSelection(map.get("speed"));
//        }
//        //速度
//        if (radioButtonID == R.id.rb_1 && cbJoinSport.isChecked()) {
//            Log.i("DD", "rb1");
//            Log.i("DD", "last pos=" + sp.getInt("speed", 0));
//            spChooseData.setSelection(map.get("speed"));
//        }

        //获取每一项表单
        spChooseData.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                transaction = fragmentManager.beginTransaction();
                //i是position,l是id
//                //速度与角度
//                if (dataInsertReportArry[0] && radioButtonID == R.id.rb_0) {
//                    map.put("axia", position);
//                }
//                //速度
//                if (dataInsertReportArry[1] && radioButtonID == R.id.rb_1) {
//                    map.put("speed", position);
//                }
                switch (radioButtonID) {
                    case R.id.rb_0://速度与角度
                        dataNum = position;//记录Spinner选中位置
                        speedAngleEntity = speedAngleEntities.get(position);
                        detail_speedangle_fragment.initView();
                        break;
                    case R.id.rb_1://速度
                        dataNum = position;//记录Spinner选中位置
                        speedEntity = speedEntities.get(position);
                        detail_speed_fragment.initView();
                        break;
                    case R.id.rb_2://角度
                        dataNum = position;//记录Spinner选中位置
                        angleEntity = angleEntities.get(position);
                        detail_angle_fragment.initView();
                        break;
                    case R.id.rb_3://空动时间
                        dataNum = position;//记录Spinner选中位置
                        kongDongTimeEntities = mApp.getmDaoSession().getKongDongTimeEntityDao()
                                .queryBuilder().where(KongDongTimeEntityDao.Properties.Key.eq(taskID),
                                        KongDongTimeEntityDao.Properties.TestingNum.eq(position)).list();
                        detail_kongDongTime_fragment.initView();
                        break;
                    case R.id.rb_4://满载下
                        dataNum = position;//记录Spinner选中位置
                        manZaiDownEntity = manZaiDownEntities.get(position);
                        manZaiXia_fragment.initView();
                        break;
                    case R.id.rb_5://空载上
                        dataNum = position;//记录Spinner选中位置
                        kongZaiUpEntity = kongZaiUpEntities.get(position);
                        detail_kongZaiUp_fragment.initView();
                        break;
                    case R.id.rb_6://牵引力
                        dataNum = position;//记录Spinner选中位置
                        //根据QianYinLiEntity的key和testingNum来确定此实体属于第几组数据
                        qianYinLiEntities = mApp.getmDaoSession().getQianYinLiEntityDao()
                                .queryBuilder().where(QianYinLiEntityDao.Properties.Key.eq(taskID),
                                        QianYinLiEntityDao.Properties.TestingNum.eq(position)).list();
                        Log.i("werwqewq", "qianYinLiEntities.size()=== " + qianYinLiEntities.size());
                        detail_qianYinLi_fragment.initView();
                        break;
                    case R.id.rb_7://制动力
                        dataNum = position;//记录Spinner选中位置
                        //根据QianYinLiEntity的key和testingNum来确定此实体属于第几组数据
                        zhiDongLiEntities = mApp.getmDaoSession().getZhiDongLiEntityDao()
                                .queryBuilder().where(ZhiDongLiEntityDao.Properties.Key.eq(taskID),
                                        ZhiDongLiEntityDao.Properties.TestingNum.eq(position)).list();
                        Log.i("werwqewq", "zhiDongLiEntities.size()=== " + zhiDongLiEntities.size());
                        detail_zhiDongLi_fragment.initView();
                        break;
                    case R.id.rb_8://回绳轮预张力
                        dataNum = position;//记录Spinner选中位置
                        //根据QianYinLiEntity的key和testingNum来确定此实体属于第几组数据
                        huiShengLunEntities = mApp.getmDaoSession().getHuiShengLunEntityDao()
                                .queryBuilder().where(HuiShengLunEntityDao.Properties.Key.eq(taskID),
                                        HuiShengLunEntityDao.Properties.TestingNum.eq(position)).list();
                        Log.i("werwqewq", "huiShengLunEntities.size()=== " + huiShengLunEntities.size());
                        detail_huiShengLun_fragment.initView();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //RadioGroup 的选中状态变化事件
    class MyRadioButtonOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            switch (radioGroup.getId()) {
                case R.id.rg_one:
                    switch (radioGroup.getCheckedRadioButtonId()) {
                        case R.id.rb_0://速度与角度
                            if (rb0.isChecked()) {
                                rgTwo.clearCheck();//清除第二列的选中状态
                                rgThree.clearCheck();//清除第三列的选中状态
                                radioButtonID = R.id.rb_0;//记录当前选中的id
                                //dataInsertReportArry用于初始化Chart中的Checkbox的状态、
                                // 同时用于判断哪些表单加入测试报告
                                cbJoinSport.setChecked(dataInsertReportArry[0]);
                                if (taskEntity.getIs_SpeedAngleSave() && speedAngleEntities.size() > 0) {
                                    //spinner和checkbox所在的那一行
                                    llTittle.setVisibility(View.VISIBLE);
                                    detail_speedangle_fragment = new Detail_SpeedAngle_fragment();
                                    transaction.replace(R.id.fragment, detail_speedangle_fragment).commit();
                                    getEntityForSpinner();
                                } else {
                                    llTittle.setVisibility(View.INVISIBLE);
                                    transaction.replace(R.id.fragment, nullDataFragment).commit();
                                }
                            }
                            break;
                        case R.id.rb_1://速度测试
                            if (rb1.isChecked()) {
                                rgTwo.clearCheck();
                                rgThree.clearCheck();
                                radioButtonID = R.id.rb_1;
                                cbJoinSport.setChecked(dataInsertReportArry[1]);
                                if (taskEntity.getIs_SpeedSave() && speedEntities.size() > 0) {
                                    //spinner和checkbox所在的那一行
                                    llTittle.setVisibility(View.VISIBLE);
                                    detail_speed_fragment = new Detail_Speed_fragment();
                                    transaction.replace(R.id.fragment, detail_speed_fragment).commit();
                                    getEntityForSpinner();
                                } else {
                                    llTittle.setVisibility(View.INVISIBLE);
                                    transaction.replace(R.id.fragment, nullDataFragment).commit();
                                }
                            }
                            break;
                        case R.id.rb_2://角度
                            if (rb2.isChecked()) {
                                rgTwo.clearCheck();
                                rgThree.clearCheck();
                                radioButtonID = R.id.rb_2;
                                cbJoinSport.setChecked(dataInsertReportArry[2]);
                                if (taskEntity.getIs_AngleSave() && angleEntities.size() > 0) {
                                    //spinner和checkbox所在的那一行
                                    llTittle.setVisibility(View.VISIBLE);
                                    detail_angle_fragment = new Detail_Angle_fragment();
                                    transaction.replace(R.id.fragment, detail_angle_fragment).commit();
                                    getEntityForSpinner();
                                } else {
                                    llTittle.setVisibility(View.INVISIBLE);
                                    transaction.replace(R.id.fragment, nullDataFragment).commit();
                                }
                            }
                            break;
                    }
                    break;
                case R.id.rg_two:
                    switch (radioGroup.getCheckedRadioButtonId()) {
                        case R.id.rb_3://空动时间
                            if (rb3.isChecked()) {
                                rgOne.clearCheck();
                                rgThree.clearCheck();
                                radioButtonID = R.id.rb_3;
                                cbJoinSport.setChecked(dataInsertReportArry[3]);
                                if (taskEntity.getIs_KongTimeSave() && kongDongTimeEntities.size() > 0) {
                                    //spinner和checkbox所在的那一行
                                    llTittle.setVisibility(View.VISIBLE);
                                    detail_kongDongTime_fragment = new Detail_KongDongTime_fragment();
                                    transaction.replace(R.id.fragment, detail_kongDongTime_fragment).commit();
                                    getEntityForSpinner();
                                } else {
                                    llTittle.setVisibility(View.INVISIBLE);
                                    transaction.replace(R.id.fragment, nullDataFragment).commit();
                                }
                            }
                            break;
                        case R.id.rb_4://满载向下制动距离
                            if (rb4.isChecked()) {
                                rgOne.clearCheck();
                                rgThree.clearCheck();
                                radioButtonID = R.id.rb_4;
                                cbJoinSport.setChecked(dataInsertReportArry[4]);
                                if (taskEntity.getIs_ManZaiXiangXiaSave() && manZaiDownEntities.size() > 0) {
                                    //spinner和checkbox所在的那一行
                                    llTittle.setVisibility(View.VISIBLE);
                                    manZaiXia_fragment = new Detail_ManZaiXia_fragment();
                                    transaction.replace(R.id.fragment, manZaiXia_fragment).commit();
                                    getEntityForSpinner();
                                } else {
                                    llTittle.setVisibility(View.INVISIBLE);
                                    transaction.replace(R.id.fragment, nullDataFragment).commit();
                                }
                            }
                            break;
                        case R.id.rb_5://空载向上制动减速度
                            if (rb5.isChecked()) {
                                rgOne.clearCheck();
                                rgThree.clearCheck();
                                radioButtonID = R.id.rb_5;
                                cbJoinSport.setChecked(dataInsertReportArry[5]);
                                if (taskEntity.getIs_KongZaiUpSave() && kongZaiUpEntities.size() > 0) {
                                    //spinner和checkbox所在的那一行
                                    llTittle.setVisibility(View.VISIBLE);
                                    detail_kongZaiUp_fragment = new Detail_KongZaiUp_fragment();
                                    transaction.replace(R.id.fragment, detail_kongZaiUp_fragment).commit();
                                    getEntityForSpinner();
                                } else {
                                    llTittle.setVisibility(View.INVISIBLE);
                                    transaction.replace(R.id.fragment, nullDataFragment).commit();
                                }
                            }
                            break;
                    }
                    break;
                case R.id.rg_three:
                    switch (radioGroup.getCheckedRadioButtonId()) {
                        case R.id.rb_6://牵引力测试
                            if (rb6.isChecked()) {
                                rgOne.clearCheck();
                                rgTwo.clearCheck();
                                radioButtonID = R.id.rb_6;
                                cbJoinSport.setChecked(dataInsertReportArry[6]);
                                qianYinLiEntities = dBManager.queryQianYinLiAllById(taskID);
                                if (taskEntity.getIs_QianYinLiSave() && qianYinLiEntities.size() > 0) {
                                    Log.i("werwqewq", "qianYinLiEntities.size()===: " + qianYinLiEntities.size());
                                    //spinner和checkbox所在的那一行
                                    llTittle.setVisibility(View.VISIBLE);
                                    detail_qianYinLi_fragment = new Detail_QianYinLi_fragment();
                                    transaction.replace(R.id.fragment, detail_qianYinLi_fragment).commit();
                                    getEntityForSpinner();
                                } else {
                                    llTittle.setVisibility(View.INVISIBLE);
                                    transaction.replace(R.id.fragment, nullDataFragment).commit();
                                }
                            }
                            break;
                        case R.id.rb_7://制动力测试
                            if (rb7.isChecked()) {
                                rgOne.clearCheck();
                                rgTwo.clearCheck();
                                radioButtonID = R.id.rb_7;
                                cbJoinSport.setChecked(dataInsertReportArry[7]);
                                zhiDongLiEntities = dBManager.queryZhiDongLiAllById(taskID);
                                if (taskEntity.getIs_ZhiDongLiSave() && zhiDongLiEntities.size() > 0) {
                                    Log.i("werwqewq", "qianYinLiEntities.size()===: " + zhiDongLiEntities.size());
                                    //spinner和checkbox所在的那一行
                                    llTittle.setVisibility(View.VISIBLE);
                                    detail_zhiDongLi_fragment = new Detail_ZhiDongLi_fragment();
                                    transaction.replace(R.id.fragment, detail_zhiDongLi_fragment).commit();
                                    getEntityForSpinner();
                                } else {
                                    llTittle.setVisibility(View.INVISIBLE);
                                    transaction.replace(R.id.fragment, nullDataFragment).commit();
                                }
                            }
                            break;
                        case R.id.rb_8://回绳轮预张力测试
                            if (rb8.isChecked()) {
                                rgOne.clearCheck();
                                rgTwo.clearCheck();
                                radioButtonID = R.id.rb_8;
                                cbJoinSport.setChecked(dataInsertReportArry[8]);
                                huiShengLunEntities = dBManager.queryHuiShengLunAllById(taskID);
                                if (taskEntity.getIs_HuiShengLunSave() && huiShengLunEntities.size() > 0) {
                                    Log.i("werwqewq", "qianYinLiEntities.size()===: " + huiShengLunEntities.size());
                                    //spinner和checkbox所在的那一行
                                    llTittle.setVisibility(View.VISIBLE);
                                    detail_huiShengLun_fragment = new Detail_HuiShengLun_fragment();
                                    transaction.replace(R.id.fragment, detail_huiShengLun_fragment).commit();
                                    getEntityForSpinner();
                                } else {
                                    llTittle.setVisibility(View.INVISIBLE);
                                    transaction.replace(R.id.fragment, nullDataFragment).commit();
                                }
                            }
                            break;
                    }
                    break;
            }

        }
    }


    //checkBox 的状态改变事件
    class MyJoinDataOnCheckBoxChangeListener implements CheckBox.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (radioButtonID) {
                case R.id.rb_0:
                    if (isChecked) {
                        rb0.setCompoundDrawablesWithIntrinsicBounds(drawableChecked, null, null, null);
                    } else {
                        rb0.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                    }
                    dataInsertReportArry[0] = isChecked;
                    break;
                case R.id.rb_1:
                    if (isChecked) {
                        rb1.setCompoundDrawablesWithIntrinsicBounds(drawableChecked, null, null, null);
                    } else {
                        rb1.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                    }
                    dataInsertReportArry[1] = isChecked;
                    break;
                case R.id.rb_2: //速度测试
                    if (isChecked) {
                        rb2.setCompoundDrawablesWithIntrinsicBounds(drawableChecked, null, null, null);
//                        getReportBean.setSpeedEntity(speedEntity);
//                        if (map.get("speed") != null)
//                            dataNum = map.get("speed");
//                        Log.i("DD", "first pos==" + dataNum);
//                        map.put("speed", dataNum);
                    } else {
                        rb2.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                    }
                    dataInsertReportArry[2] = isChecked;
                    break;
                case R.id.rb_3:
                    if (isChecked) {
                        rb3.setCompoundDrawablesWithIntrinsicBounds(drawableChecked, null, null, null);
                    } else {
                        rb3.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                    }
                    dataInsertReportArry[3] = isChecked;
                    break;
                case R.id.rb_4:
                    if (isChecked) {
                        rb4.setCompoundDrawablesWithIntrinsicBounds(drawableChecked, null, null, null);
                    } else {
                        rb4.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                    }
                    dataInsertReportArry[4] = isChecked;
                    break;
                case R.id.rb_5:
                    if (isChecked) {
                        rb5.setCompoundDrawablesWithIntrinsicBounds(drawableChecked, null, null, null);
                    } else {
                        rb5.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                    }
                    dataInsertReportArry[5] = isChecked;
                    break;
                case R.id.rb_6:
                    if (isChecked) {
                        rb6.setCompoundDrawablesWithIntrinsicBounds(drawableChecked, null, null, null);
                    } else {
                        rb6.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                    }
                    dataInsertReportArry[6] = isChecked;
                    break;
                case R.id.rb_7:
                    if (isChecked) {
                        rb7.setCompoundDrawablesWithIntrinsicBounds(drawableChecked, null, null, null);
                    } else {
                        rb7.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                    }
                    dataInsertReportArry[7] = isChecked;
                    break;
                case R.id.rb_8:
                    if (isChecked) {
                        rb8.setCompoundDrawablesWithIntrinsicBounds(drawableChecked, null, null, null);
                    } else {
                        rb8.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                    }
                    dataInsertReportArry[8] = isChecked;
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApp.setTaskEntity(null);//返回时置空，以防在创建任务时查询到此任务
    }


    //创建选项菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_data_detail, menu);
        //返回true则显示该menu,false 则不显示
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //任务信息
        if (id == R.id.action_task_inf) {
            String mesString =
                    "· 单位名称：" + taskEntity.getUnitName() +
                            "\n· 单轨吊编号：" + taskEntity.getNumber() +
                            "\n· 测试人员：" + taskEntity.getPeopleName() +
                            "\n· [速度与角度测试 ]数据：" + speedAngleEntities.size() +
                            "\n· [速度测试 ]数据：" + speedEntities.size() +
                            "\n· [角度测试 ]数据：" + angleEntities.size() +
                            "\n· [空动时间 ]数据：" + kongDongTimeEntities.size() +
                            "\n· [满载向下 ]数据：" + manZaiDownEntities.size() +
                            "\n· [空载向上 ]数据：" + kongZaiUpEntities.size() +
                            "\n· [牵引力 ]数据：" + qianYinLiEntities.size() +
                            "\n· [制动力 ]数据：" + zhiDongLiEntities.size() +
                            "\n· [回绳轮 ]数据：" + huiShengLunEntities.size();


            final AlertDialog dialog = new AlertDialog.Builder
                    (DataDetailActivity.this).setMessage(mesString).create();
            dialog.show();
            return true;
        }
        //生成测试报告
        if (id == R.id.action_gen_sport) {
            updateExcel();
            return true;
        }
        //删除
//        if (id == R.id.action_delete) {
//            delete();
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * jxl暂时不提供修改已经存在的数据表,这里通过覆盖原文件来更新的,不适合大型数据更新!
     */
    private void updateExcel() {
        try {//先创建文件夹及文件
            //xls存储目录
            File fileDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/单轨吊综合测试仪/测试报告/");
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            //xls名称
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/单轨吊综合测试仪/测试报告/"
                    + taskEntity.getUnitName() + "_" + taskEntity.getPeopleName() + ".xls");

//            if (file.exists()) {
//                file.delete();
//            }
            if (!file.exists()) {
                file.createNewFile();
            }
            //图片存放目录
            File pictureDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/单轨吊综合测试仪/测试图片/");

            //创建一个指向dan.xls的输入流（用来读取模板文件）
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("assets/danguidiao.xls");
            Workbook wb = Workbook.getWorkbook(inputStream);//模板文件
            //用模板覆盖file（模板里的数据是不会变的，相当于workBook是一个中间变量，用来拿到模板数据，
            // 我们操作的只是这个中间变量，不会影响到模板，操作完通过write（）方法写入到file中）
            WritableWorkbook workbook = Workbook.createWorkbook(file, wb);

            //速度与角度
            if (dataInsertReportArry[0] && speedAngleEntities.size() != 0) {
                Log.i("excel", "speedAngleEntity== " + speedAngleEntity.toString());
                WritableSheet sheet5 = workbook.getSheet(5);//速度与角度
                WritableCell cell3_3 = sheet5.getWritableCell(2, 3);//最大速度
                WritableCell cell3_4 = sheet5.getWritableCell(2, 4);//最大倾角
                if (cell3_3.getType() == CellType.LABEL && cell3_4.getType() == CellType.LABEL) {
                    Label label3_3 = (Label) cell3_3;
                    Label label3_4 = (Label) cell3_4;
                    label3_3.setString(speedAngleEntity.getMaxSpeed());
                    label3_4.setString(speedAngleEntity.getMaxAngle());
                }
                //拿到图片
                File filePicture = new File(pictureDir, "速度与角度图_" + speedAngleEntity.getId() + ".png");
                sheet5.addImage(new WritableImage(1, 6, 6, 15, filePicture));
            }
            //速度和角度匀速分析
            if (dataInsertReportArry[0] && speedAngleEntities.size() != 0) {
                WritableSheet sheet6 = workbook.getSheet(6);
                WritableCell cell2_3 = sheet6.getWritableCell(2, 3);//平均速度
                WritableCell cell2_4 = sheet6.getWritableCell(2, 4);//运行时间
                Label label = (Label) cell2_3;
                Label labe2 = (Label) cell2_4;
                label.setString(speedAngleEntity.getYunsu_averageSpeed());
                labe2.setString(speedAngleEntity.getYunsu_runTime());
                File filePicture = new File(pictureDir, "速度与角度匀速分析图_" + speedAngleEntity.getId() + ".png");
                if (filePicture.exists()) {
                    sheet6.addImage(new WritableImage(1, 6, 6, 15, filePicture));
                }
            }
            //速度和角度减速分析
            if (dataInsertReportArry[0] && speedAngleEntities.size() != 0) {
                WritableSheet sheet7 = workbook.getSheet(7);
                WritableCell cell2_3 = sheet7.getWritableCell(2, 3);//平均减速度
                WritableCell cell2_4 = sheet7.getWritableCell(2, 4);//减速时间
                WritableCell cell2_5 = sheet7.getWritableCell(2, 5);//初速度
                WritableCell cell2_6 = sheet7.getWritableCell(2, 6);//末速度
                WritableCell cell2_7 = sheet7.getWritableCell(2, 7);//最大速度
                WritableCell cell2_8 = sheet7.getWritableCell(2, 8);//最小速度
                Label label = (Label) cell2_3;
                Label labe2 = (Label) cell2_4;
                Label labe3 = (Label) cell2_5;
                Label labe4 = (Label) cell2_6;
                Label labe5 = (Label) cell2_7;
                Label labe6 = (Label) cell2_8;
                label.setString(speedAngleEntity.getJiansu_avegare_jiansudu());
                labe2.setString(speedAngleEntity.getJiansu_time());
                labe3.setString(speedAngleEntity.getJiansu_chusudu());
                labe4.setString(speedAngleEntity.getJiansu_mosudu());
                labe5.setString(speedAngleEntity.getJiansu_maxspeed());
                labe6.setString(speedAngleEntity.getJiansu_minspeed());
                File filePicture = new File(pictureDir, "速度与角度减速分析图_" + speedAngleEntity.getId() + ".png");
                if (filePicture.exists()) {
                    sheet7.addImage(new WritableImage(1, 10, 6, 15, filePicture));
                }
            }

            //速度
            if (dataInsertReportArry[1] && speedEntities.size() != 0) {
                WritableSheet sheet8 = workbook.getSheet(8);
                WritableCell cell2_3 = sheet8.getWritableCell(2, 3);
                Label label = (Label) cell2_3;
                label.setString(speedEntity.getMaxSpeed());
                File filePicture = new File(pictureDir, "速度图_" + speedEntity.getId() + ".png");
                sheet8.addImage(new WritableImage(1, 5, 6, 15, filePicture));
            }
            //速度匀速分析
            if (dataInsertReportArry[1] && speedEntities.size() != 0) {
                WritableSheet sheet9 = workbook.getSheet(9);
                WritableCell cell2_3 = sheet9.getWritableCell(2, 3);//平均速度
                WritableCell cell2_4 = sheet9.getWritableCell(2, 4);//运行时间
                Label label = (Label) cell2_3;
                Label labe2 = (Label) cell2_4;
                label.setString(speedEntity.getYunsu_averageSpeed());
                labe2.setString(speedEntity.getYunsu_runTime());
                File filePicture = new File(pictureDir, "速度匀速分析图_" + speedEntity.getId() + ".png");
                sheet9.addImage(new WritableImage(1, 6, 6, 15, filePicture));
            }
            //速度减速分析  sheet10
            if (dataInsertReportArry[1] && speedEntities.size() != 0) {
                WritableSheet sheet10 = workbook.getSheet(10);
                WritableCell cell2_3 = sheet10.getWritableCell(2, 3);//平均减速度
                WritableCell cell2_4 = sheet10.getWritableCell(2, 4);//减速时间
                WritableCell cell2_5 = sheet10.getWritableCell(2, 5);//初速度
                WritableCell cell2_6 = sheet10.getWritableCell(2, 6);//末速度
                WritableCell cell2_7 = sheet10.getWritableCell(2, 7);//最大速度
                WritableCell cell2_8 = sheet10.getWritableCell(2, 8);//最小速度
                Label label = (Label) cell2_3;
                Label labe2 = (Label) cell2_4;
                Label labe3 = (Label) cell2_5;
                Label labe4 = (Label) cell2_6;
                Label labe5 = (Label) cell2_7;
                Label labe6 = (Label) cell2_8;
                label.setString(speedEntity.getJiansu_avegare_jiansudu());
                labe2.setString(speedEntity.getJiansu_time());
                labe2.setString(speedEntity.getJiansu_chusudu());
                labe2.setString(speedEntity.getJiansu_mosudu());
                labe2.setString(speedEntity.getJiansu_maxspeed());
                labe2.setString(speedEntity.getJiansu_minspeed());
                File filePicture = new File(pictureDir, "速度减速分析图_" + speedEntity.getId() + ".png");
                sheet10.addImage(new WritableImage(1, 10, 6, 15, filePicture));
            }

            //运行角度  sheet11
            if (dataInsertReportArry[2] && angleEntities.size() != 0) {
                WritableSheet sheet11 = workbook.getSheet(11);
                WritableCell cell2_3 = sheet11.getWritableCell(2, 3);//最大角度

                Label label = (Label) cell2_3;

                label.setString(angleEntity.getMaxAngle());

                File filePicture = new File(pictureDir, "角度测试图_" + angleEntity.getId() + ".png");
                sheet11.addImage(new WritableImage(1, 5, 6, 15, filePicture));
            }


            //空动时间   sheet12
            if (dataInsertReportArry[3] && kongDongTimeEntities.size() != 0) {
                WritableSheet sheet12 = workbook.getSheet(12);
                for (int i = 0; i < kongDongTimeEntities.size(); i++) {
                    if (i > 9) {//最多十组数据
                        break;
                    }
                    KongDongTimeEntity entity = kongDongTimeEntities.get(i);
                    WritableCell cell1 = sheet12.getWritableCell(2, 4 + i);//时间1
                    WritableCell cell2 = sheet12.getWritableCell(3, 4 + i);//时间2
                    WritableCell cell3 = sheet12.getWritableCell(4, 4 + i);//空动时间
                    Label label = (Label) cell1;
                    Label labe2 = (Label) cell2;
                    Label labe3 = (Label) cell3;
                    label.setString(entity.getBreakTime1());
                    labe2.setString(entity.getBreakTime2());
                    labe3.setString(entity.getBreakTime());
                }
            }


            //满载向下   sheet13
            if (dataInsertReportArry[4] && manZaiDownEntities.size() != 0) {
                WritableSheet sheet13 = workbook.getSheet(13);
                WritableCell cell2_3 = sheet13.getWritableCell(2, 3);//制动距离
                WritableCell cell4_3 = sheet13.getWritableCell(4, 3);//制动初速度
                WritableCell cell2_4 = sheet13.getWritableCell(2, 4);//6s运行距离
                WritableCell cell4_4 = sheet13.getWritableCell(4, 4);//制动时间
                Label label = (Label) cell2_3;
                Label labe2 = (Label) cell4_3;
                Label labe3 = (Label) cell2_4;
                Label labe4 = (Label) cell4_4;
                label.setString(manZaiDownEntity.getZhiDongJuli());
                labe2.setString(manZaiDownEntity.getZhiDongChuSuDu());
                labe3.setString(manZaiDownEntity.getLiuMiaoJuLi());
                labe4.setString(manZaiDownEntity.getZhiDongShiJian());
                File filePicture = new File(pictureDir, "满载向下制动距离图_" + manZaiDownEntity.getId() + ".png");
                sheet13.addImage(new WritableImage(1, 6, 6, 15, filePicture));
            }
            //空载向上制动减速度   sheet14
            if (dataInsertReportArry[5] && kongZaiUpEntities.size() != 0) {
                WritableSheet sheet14 = workbook.getSheet(14);
                WritableCell cell2_3 = sheet14.getWritableCell(2, 3);//制动初速度
                WritableCell cell4_3 = sheet14.getWritableCell(4, 3);//制动时间
                WritableCell cell2_4 = sheet14.getWritableCell(2, 4);//制动减速度
                Label label = (Label) cell2_3;
                Label labe2 = (Label) cell4_3;
                Label labe3 = (Label) cell2_4;
                label.setString(kongZaiUpEntity.getZhiDongChuSuDu());
                labe2.setString(kongZaiUpEntity.getZhiDongShiJian());
                labe3.setString(kongZaiUpEntity.getZhiDongJianSuDu());
                File filePicture = new File(pictureDir, "空载向上制动减速度图_" + kongZaiUpEntity.getId() + ".png");
                sheet14.addImage(new WritableImage(1, 6, 6, 15, filePicture));
            }


            //牵引力   sheet15
            if (dataInsertReportArry[6] && qianYinLiEntities.size() != 0) {
                WritableSheet sheet15 = workbook.getSheet(15);
                for (int i = 0; i < qianYinLiEntities.size(); i++) {
                    if (i > 9) {//最多十组数据
                        break;
                    }
                    QianYinLiEntity entity = qianYinLiEntities.get(i);
                    WritableCell cell1 = sheet15.getWritableCell(2, 3);//额定牵引力
                    WritableCell cell2 = sheet15.getWritableCell(2, 5 + i);//牵引力（最大）
                    WritableCell cell3 = sheet15.getWritableCell(3, 5 + i);//差值
                    WritableCell cell4 = sheet15.getWritableCell(4, 5 + i);//测试时间
                    Label label1 = (Label) cell1;
                    Label label2 = (Label) cell2;
                    Label labe3 = (Label) cell3;
                    Label labe4 = (Label) cell4;
                    label1.setString(entity.getEdingQianyinli());
                    label2.setString(entity.getMaxQianyinli());
                    labe3.setString(entity.getChazhi());
                    labe4.setString(entity.getTime());
                }
            }

            //制动力   sheet16
            if (dataInsertReportArry[7] && zhiDongLiEntities.size() != 0) {
                WritableSheet sheet16 = workbook.getSheet(16);
                for (int i = 0; i < zhiDongLiEntities.size(); i++) {
                    if (i > 9) {//最多十组数据
                        break;
                    }
                    ZhiDongLiEntity entity = zhiDongLiEntities.get(i);
                    WritableCell cell1 = sheet16.getWritableCell(2, 3);//额定牵引力
                    WritableCell cell2 = sheet16.getWritableCell(2, 5 + i);//牵引力（最大）
                    WritableCell cell3 = sheet16.getWritableCell(3, 5 + i);//比值
                    WritableCell cell4 = sheet16.getWritableCell(4, 5 + i);//测试时间
                    Label label1 = (Label) cell1;
                    Label label2 = (Label) cell2;
                    Label labe3 = (Label) cell3;
                    Label labe4 = (Label) cell4;
                    label1.setString(entity.getEdingZhiDongli());
                    label2.setString(entity.getMaxZhiDongli());
                    labe3.setString(entity.getBizhi());
                    labe4.setString(entity.getTime());
                }
            }


            //回绳轮预张力   sheet17
            if (dataInsertReportArry[8] && huiShengLunEntities.size() != 0) {
                WritableSheet sheet16 = workbook.getSheet(17);
                for (int i = 0; i < huiShengLunEntities.size(); i++) {
                    if (i > 9) {//最多十组数据
                        break;
                    }
                    HuiShengLunEntity entity = huiShengLunEntities.get(i);
                    WritableCell cell1 = sheet16.getWritableCell(2, 3);//钢丝绳破断力
                    WritableCell cell2 = sheet16.getWritableCell(2, 5 + i);//预张紧力（最大）
                    WritableCell cell3 = sheet16.getWritableCell(3, 5 + i);//比值
                    WritableCell cell4 = sheet16.getWritableCell(4, 5 + i);//测试时间
                    Label label1 = (Label) cell1;
                    Label label2 = (Label) cell2;
                    Label labe3 = (Label) cell3;
                    Label labe4 = (Label) cell4;
                    label1.setString(entity.getPoDuanLi());
                    label2.setString(entity.getMaxYuZhangJinLi());
                    labe3.setString(entity.getBizhi());
                    labe4.setString(entity.getTime());
                }
            }

//            WritableSheet sheet0 = workbook.getSheet(0);//拿到模板中的第一张工作表(有可能出现数组越界异常)
//            WritableCell writableCell = sheet0.getWritableCell(0, 0);//第一列第一行
//            Label label = (Label) writableCell;
//            label.setString("数据一");
//            //拿到要添加的图片
//            File filePic = new File(Environment.getExternalStorageDirectory() +
//                    "/单轨吊综合测试仪/.pic/" + "数据管理匀速界面.png");
//            /**
//             * x：起始列
//             * y：起始行
//             * width：宽几格
//             * height：高几格
//             * File：图片
//             */
//            sheet0.addImage(new WritableImage(0, 3, 6, 15, filePic));


            //关闭输入流
            if (inputStream != null) {
                inputStream.close();
            }
            //写入数据并关闭
            workbook.write();
            workbook.close();
            //关闭excel
            wb.close();

            try {
                Intent intent = getWordFileIntent(Environment.getExternalStorageDirectory() + "/单轨吊综合测试仪/测试报告/"
                        + taskEntity.getUnitName() + "_" + taskEntity.getPeopleName() + ".xls");
                startActivity(intent);
            } catch (Exception e) {
                Toasty.error(DataDetailActivity.this, "打开WPS失败").show();
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }

    }

    // 打开WPS
    public static Intent getWordFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/msword");
        return intent;
    }
}

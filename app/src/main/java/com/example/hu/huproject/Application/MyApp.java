package com.example.hu.huproject.Application;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.db.manager.DaoMaster;
import com.db.manager.DaoSession;
import com.example.hu.huproject.Bean.LaLiBean;
import com.example.hu.huproject.Bean.QjBean;
import com.example.hu.huproject.Bean.SuDuBean;
import com.example.hu.huproject.Bean.TimeBean;
import com.example.hu.huproject.Bean.TimeBean1;
import com.example.hu.huproject.Bean.TimeBean2;
import com.example.hu.huproject.Bean.WSDBean;
import com.example.hu.huproject.Entity.TaskEntity;
import com.example.hu.huproject.R;
import com.tencent.bugly.Bugly;
import com.xzkydz.bean.ComBean;
import com.xzkydz.function.app.KyApp;
import com.xzkydz.function.style.AppStyle;
import com.xzkydz.function.utils.SharedPreferencesUtils;

import org.json.JSONArray;

public class MyApp extends KyApp {
    public static final String MY_TAG = "MyApp";
    public static Long entityID;//单项测试任务的id（主键）

    private DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase db;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private static MyApp mInstance;

    public static int ackNumber;
    //是否正在运行
    public static boolean isRunning;
    //速度的数据
    public static JSONArray jsonArray_SpeedListY;//速度点集
//    public static JSONArray jsonArray_AngleListY;//角度点集
    public static String SpeedListX;//运行时间
    public static double max_speed;//最大速度
    /**
     * 匀速分析-选段之后保存数据
     */
    public static String startTime;//开始时间
    public static String endTime;//结束时间

    /**
     * 以下是减速分析时保存的数据
     */
    public static String startTime_JianSu;//减速选段开始时间
    public static String endTime_JianSu;//减速选段结束时间
    public static String jianSuTime_JianSu;//计算出的减速时间
    public static String chuSudu_JianSu;//用户选择的减速初速度
    public static String moSudu_JianSu;//用户选择的减速末速度
    public static String averageJianSudu;//计算出的平均减速度
    public static String maxSpeed_JianSudu;//计算出减速段的最大速度
    public static String minSpeed_JianSudu;//计算出的减速段的最小速度


    public static ComBean comBean;
    public static TaskEntity taskEntity;//测试任务
    //速度
    public static ComBean comBeanSd;
    public static SuDuBean sdBean;
    public static ComBean comBeanWSD;//温湿度实体类
    public static WSDBean wsdBean;//温湿度实体类
    //时间（计时器、开关信号）
    public static ComBean comBeanTime;
    public static ComBean comBeanTimeXiangYing;//时间计时器响应时，返回这个
    public static TimeBean timeBean;
    public static TimeBean1 timeBean1;//空动时间测试的开始传感器的数据
    public static TimeBean2 timeBean2;//空动时间测试的结束传感器的数据

    public static ComBean comBeanLali;//拉力（系统力）
    public static LaLiBean laLiBean;

    public static boolean isAckSync;//判断速度和时间传感器的ACK是否同步
    public static double breakTime = -1;
    public static double breakTime1 = -1;
    public static double breakTime2 = -1;

    public static QjBean qjBean;
//    public static boolean isAngleConnected;//倾角是否连接

    //倾角
    //温湿度
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = MyApp.this;
        setDataBase();
        Bugly.init(getApplicationContext(), "3eaf8778ad", false);
    }

    @Override
    public void setAppStyleColor() {
        super.setAppStyleColor();
        //设置APP的名称
        AppStyle.appNameId = R.string.app_name;
        //设置APP的主题色（Toolbar的颜色）
        AppStyle.appToolbarColor = R.color.orange_my;
        //实例化SharedPreferenceUtils工具类方便调用
        SharedPreferencesUtils.init(this);
    }

    //用于返回Application实例
    public static MyApp getInstance() {
        return mInstance;
    }

    private void setDataBase() {
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
        // 此处sport-db表示数据库名称 可以任意填写
        mHelper = new DaoMaster.DevOpenHelper(this, "huproject_one", null);
        db = mHelper.getWritableDatabase();
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }

    public DaoSession getmDaoSession() {
        return mDaoSession;
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    public static TaskEntity getTaskEntity() {
        return taskEntity;
    }

    public static void setTaskEntity(TaskEntity taskEntity) {
        MyApp.taskEntity = taskEntity;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}

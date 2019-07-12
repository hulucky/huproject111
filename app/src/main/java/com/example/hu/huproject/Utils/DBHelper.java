package com.example.hu.huproject.Utils;

import android.content.Context;

import com.db.manager.AngleEntityDao;
import com.db.manager.DaoSession;

import com.db.manager.HuiShengLunEntityDao;
import com.db.manager.KongDongTimeEntityDao;
import com.db.manager.KongZaiUpEntityDao;
import com.db.manager.ManZaiDownEntityDao;
import com.db.manager.QianYinLiEntityDao;
import com.db.manager.SpeedAngleEntityDao;
import com.db.manager.SpeedEntityDao;
import com.db.manager.TaskEntityDao;
import com.db.manager.ZhiDongLiEntityDao;
import com.example.hu.huproject.Application.MyApp;
import com.example.hu.huproject.Entity.AngleEntity;
import com.example.hu.huproject.Entity.HuiShengLunEntity;
import com.example.hu.huproject.Entity.KongDongTimeEntity;
import com.example.hu.huproject.Entity.KongZaiUpEntity;
import com.example.hu.huproject.Entity.ManZaiDownEntity;
import com.example.hu.huproject.Entity.QianYinLiEntity;
import com.example.hu.huproject.Entity.SpeedAngleEntity;
import com.example.hu.huproject.Entity.SpeedEntity;
import com.example.hu.huproject.Entity.ZhiDongLiEntity;

import java.util.List;

public class DBHelper {
    private static final String TAG = DBHelper.class.getSimpleName();
    private static DBHelper instance;
    private static Context appContext;
    private DaoSession mDaoSession;
    private TaskEntityDao taskEntityDao;
    private SpeedAngleEntityDao speedAngleEntityDao;
    private SpeedEntityDao speedEntityDao;
    private AngleEntityDao angleEntityDao;
    private KongDongTimeEntityDao kongDongTimeEntityDao;
    private ManZaiDownEntityDao manZaiDownEntityDao;
    private KongZaiUpEntityDao kongZaiUpEntityDao;
    private QianYinLiEntityDao qianYinLiEntityDao;
    private ZhiDongLiEntityDao zhiDongLiEntityDao;
    private HuiShengLunEntityDao huiShengLunEntityDao;

    // 单例模式，DBHelper只初始化一次
    public static DBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper();
            if (appContext == null) {
                appContext = context.getApplicationContext();
            }
            instance.mDaoSession = MyApp.getInstance().getmDaoSession();
            instance.taskEntityDao = instance.mDaoSession
                    .getTaskEntityDao();
            instance.speedAngleEntityDao = instance.mDaoSession.getSpeedAngleEntityDao();
            instance.speedEntityDao = instance.mDaoSession.getSpeedEntityDao();
            instance.angleEntityDao = instance.mDaoSession.getAngleEntityDao();
            instance.kongDongTimeEntityDao = instance.mDaoSession.getKongDongTimeEntityDao();
            instance.manZaiDownEntityDao = instance.mDaoSession.getManZaiDownEntityDao();
            instance.kongZaiUpEntityDao = instance.mDaoSession.getKongZaiUpEntityDao();
            instance.qianYinLiEntityDao = instance.mDaoSession.getQianYinLiEntityDao();
            instance.zhiDongLiEntityDao = instance.mDaoSession.getZhiDongLiEntityDao();
            instance.huiShengLunEntityDao = instance.mDaoSession.getHuiShengLunEntityDao();
        }
        return instance;
    }


    /**
     * 速度与角度
     */
    //增
    public void insertSpeedAngle(SpeedAngleEntity speedAngleEntity) {
        speedAngleEntityDao.insertOrReplace(speedAngleEntity);
    }

    //根据key查询所有
    public List<SpeedAngleEntity> querySpeedAngleAllById(Long id) {
        return speedAngleEntityDao.queryBuilder().where(SpeedAngleEntityDao.Properties.Key.eq(id)).list();
    }

    //查询所有
    public List<SpeedAngleEntity> querySpeedAngleAll() {
        return speedAngleEntityDao.loadAll();
    }

    //删
    public void deleteSpeedAngle(SpeedAngleEntity speedAngleEntity) {
        speedAngleEntityDao.deleteByKey(speedAngleEntity.getId());
    }

    //根据id查询单个
    public SpeedAngleEntity querySpeedAngleById(Long id) {
        return speedAngleEntityDao.load(id);
    }

    //改
    public void updateSpeedAngle(SpeedAngleEntity speedAngleEntity) {
        speedAngleEntityDao.update(speedAngleEntity);
    }


    /**
     * 速度
     */
    //增
    public void insertSpeed(SpeedEntity speedEntity) {
        speedEntityDao.insertOrReplace(speedEntity);
    }

    //根据key查询所有
    public List<SpeedEntity> querySpeedAllById(Long id) {
        return speedEntityDao.queryBuilder().where(SpeedEntityDao.Properties.Key.eq(id)).list();
    }

    //查询所有
    public List<SpeedEntity> querySpeedAll() {
        return speedEntityDao.loadAll();
    }

    //删
    public void deleteSpeed(SpeedEntity resultEntity) {
        speedEntityDao.deleteByKey(resultEntity.getId());
    }

    //根据id查询单个
    public SpeedEntity querySpeedById(Long id) {
        return speedEntityDao.load(id);
    }

    //改
    public void updateSpeed(SpeedEntity speedEntity) {
        speedEntityDao.update(speedEntity);
    }


    /**
     * 角度
     */
    //增
    public void insertAngle(AngleEntity angleEntity) {
        angleEntityDao.insertOrReplace(angleEntity);
    }

    //根据key查询所有
    public List<AngleEntity> queryAngleAllById(Long id) {
        return angleEntityDao.queryBuilder().where(AngleEntityDao.Properties.Key.eq(id)).list();
    }

    //查询所有
    public List<AngleEntity> queryAngleAll() {
        return angleEntityDao.loadAll();
    }

    //删
    public void deleteAngle(AngleEntity angleEntity) {
        angleEntityDao.deleteByKey(angleEntity.getId());
    }

    //根据id查询单个
    public AngleEntity queryAngleById(Long id) {
        return angleEntityDao.load(id);
    }

    //改
    public void updateAngle(AngleEntity speedEntity) {
        angleEntityDao.update(speedEntity);
    }


    /**
     * 空动时间
     */
    //增
    public void insertKongDongTime(KongDongTimeEntity kongDongTimeEntity) {
        kongDongTimeEntityDao.insertOrReplace(kongDongTimeEntity);
    }

    //根据key查询所有
    public List<KongDongTimeEntity> queryKongDongTimeAllById(Long id) {
        return kongDongTimeEntityDao.queryBuilder().where(KongDongTimeEntityDao.Properties.Key.eq(id)).list();
    }

    //查询所有
    public List<KongDongTimeEntity> queryKongDongTimeAll() {
        return kongDongTimeEntityDao.loadAll();
    }

    //删
    public void deleteKongDongTime(KongDongTimeEntity kongDongTimeEntity) {
        kongDongTimeEntityDao.deleteByKey(kongDongTimeEntity.getId());
    }

    //根据id查询单个
    public KongDongTimeEntity queryKongDongTimeById(Long id) {
        return kongDongTimeEntityDao.load(id);
    }

    //改
    public void updateKongDongTime(KongDongTimeEntity kongDongTimeEntity) {
        kongDongTimeEntityDao.update(kongDongTimeEntity);
    }


    /**
     * 满载向下制动距离
     */
    //增
    public void insertManZaiDown(ManZaiDownEntity manZaiDownEntity) {
        manZaiDownEntityDao.insertOrReplace(manZaiDownEntity);
    }

    //根据key查询所有
    public List<ManZaiDownEntity> queryManZaiDownAllById(Long id) {
        return manZaiDownEntityDao.queryBuilder().where(ManZaiDownEntityDao.Properties.Key.eq(id)).list();
    }

    //查询所有
    public List<ManZaiDownEntity> queryManZaiDownAll() {
        return manZaiDownEntityDao.loadAll();
    }

    //删
    public void deleteManZaiDown(ManZaiDownEntity manZaiDownEntity) {
        manZaiDownEntityDao.deleteByKey(manZaiDownEntity.getId());
    }

    //根据id查询单个
    public ManZaiDownEntity queryManZaiDownById(Long id) {
        return manZaiDownEntityDao.load(id);
    }

    //改
    public void updateManZaiDown(ManZaiDownEntity manZaiDownEntity) {
        manZaiDownEntityDao.update(manZaiDownEntity);
    }


    /**
     * 空载向上制动减速度
     */
    //增
    public void insertKongZaiUp(KongZaiUpEntity kongZaiUpEntity) {
        kongZaiUpEntityDao.insertOrReplace(kongZaiUpEntity);
    }

    //根据key查询所有
    public List<KongZaiUpEntity> queryKongZaiUpAllById(Long id) {
        return kongZaiUpEntityDao.queryBuilder().where(KongZaiUpEntityDao.Properties.Key.eq(id)).list();
    }

    //查询所有
    public List<KongZaiUpEntity> queryKongZaiUpAll() {
        return kongZaiUpEntityDao.loadAll();
    }

    //删
    public void deleteKongZaiUp(KongZaiUpEntity kongZaiUpEntity) {
        kongZaiUpEntityDao.deleteByKey(kongZaiUpEntity.getId());
    }

    //根据id查询单个
    public KongZaiUpEntity queryKongZaiUpById(Long id) {
        return kongZaiUpEntityDao.load(id);
    }

    //改
    public void updateKongZaiUp(KongZaiUpEntity kongZaiUpEntity) {
        kongZaiUpEntityDao.update(kongZaiUpEntity);
    }


    /**
     * 牵引力测试
     */
    //增
    public void insertQianYinLi(QianYinLiEntity qianYinLiEntity) {
        qianYinLiEntityDao.insertOrReplace(qianYinLiEntity);
    }

    //根据key查询所有
    public List<QianYinLiEntity> queryQianYinLiAllById(Long id) {
        return qianYinLiEntityDao.queryBuilder().where(QianYinLiEntityDao.Properties.Key.eq(id)).list();
    }

    //查询所有
    public List<QianYinLiEntity> queryQianYinLiAll() {
        return qianYinLiEntityDao.loadAll();
    }

    //删
    public void deleteQianYinLi(QianYinLiEntity qianYinLiEntity) {
        qianYinLiEntityDao.deleteByKey(qianYinLiEntity.getId());
    }

    //根据id查询单个
    public QianYinLiEntity queryQianYinLiById(Long id) {
        return qianYinLiEntityDao.load(id);
    }

    //改
    public void updateQianYinLi(QianYinLiEntity qianYinLiEntity) {
        qianYinLiEntityDao.update(qianYinLiEntity);
    }


    /**
     * 制动力测试
     */
    //增
    public void insertZhiDongLi(ZhiDongLiEntity zhiDongLiEntity) {
        zhiDongLiEntityDao.insertOrReplace(zhiDongLiEntity);
    }

    //根据key查询所有
    public List<ZhiDongLiEntity> queryZhiDongLiAllById(Long id) {
        return zhiDongLiEntityDao.queryBuilder().where(ZhiDongLiEntityDao.Properties.Key.eq(id)).list();
    }

    //查询所有
    public List<ZhiDongLiEntity> queryZhiDongLiAll() {
        return zhiDongLiEntityDao.loadAll();
    }

    //删
    public void deleteZhiDongLi(ZhiDongLiEntity zhiDongLiEntity) {
        zhiDongLiEntityDao.deleteByKey(zhiDongLiEntity.getId());
    }

    //根据id查询单个
    public ZhiDongLiEntity queryZhiDongLiById(Long id) {
        return zhiDongLiEntityDao.load(id);
    }

    //改
    public void updateZhiDongLi(ZhiDongLiEntity zhiDongLiEntity) {
        zhiDongLiEntityDao.update(zhiDongLiEntity);
    }


    /**
     * 回绳轮预张力测试
     */
    //增
    public void insertHuiShengLun(HuiShengLunEntity huiShengLunEntity) {
        huiShengLunEntityDao.insertOrReplace(huiShengLunEntity);
    }

    //根据key查询所有
    public List<HuiShengLunEntity> queryHuiShengLunAllById(Long id) {
        return huiShengLunEntityDao.queryBuilder().where(HuiShengLunEntityDao.Properties.Key.eq(id)).list();
    }

    //查询所有
    public List<HuiShengLunEntity> queryHuiShengLunAll() {
        return huiShengLunEntityDao.loadAll();
    }

    //删
    public void deleteHuiShengLun(HuiShengLunEntity huiShengLunEntity) {
        huiShengLunEntityDao.deleteByKey(huiShengLunEntity.getId());
    }

    //根据id查询单个
    public HuiShengLunEntity queryHuiShengLunById(Long id) {
        return huiShengLunEntityDao.load(id);
    }

    //改
    public void updateHuiShengLun(HuiShengLunEntity huiShengLunEntity) {
        huiShengLunEntityDao.update(huiShengLunEntity);
    }


}
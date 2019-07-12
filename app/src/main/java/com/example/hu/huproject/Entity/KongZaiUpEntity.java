package com.example.hu.huproject.Entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class KongZaiUpEntity {//空载向上制动减速度测试
    @Id(autoincrement = true)
    private Long id;
    private long key;
    //最大速度
    private String maxSpeed;
    //创建时间
    private String createTime;
    //速度集合
    private String point_speed;
    //单次测试的运行时间
    private String runTimeTotal;
    //断开时的时间
    private String breakTime;
    //制动距离
    private String zhiDongJuli;
    //制动初速度
    private String zhiDongChuSuDu;
    //6秒运行距离
    private String liuMiaoJuLi;
    //制动时间
    private String zhiDongShiJian;
    //制动减速度
    private String zhiDongJianSuDu;

    @Generated(hash = 651594343)
    public KongZaiUpEntity(Long id, long key, String maxSpeed, String createTime,
            String point_speed, String runTimeTotal, String breakTime,
            String zhiDongJuli, String zhiDongChuSuDu, String liuMiaoJuLi,
            String zhiDongShiJian, String zhiDongJianSuDu) {
        this.id = id;
        this.key = key;
        this.maxSpeed = maxSpeed;
        this.createTime = createTime;
        this.point_speed = point_speed;
        this.runTimeTotal = runTimeTotal;
        this.breakTime = breakTime;
        this.zhiDongJuli = zhiDongJuli;
        this.zhiDongChuSuDu = zhiDongChuSuDu;
        this.liuMiaoJuLi = liuMiaoJuLi;
        this.zhiDongShiJian = zhiDongShiJian;
        this.zhiDongJianSuDu = zhiDongJianSuDu;
    }

    @Generated(hash = 2020109171)
    public KongZaiUpEntity() {
    }

    @Override
    public String toString() {
        return "KongZaiUpEntity{" +
                "id=" + id +
                ", key=" + key +
                ", maxSpeed='" + maxSpeed + '\'' +
                ", createTime='" + createTime + '\'' +
                ", point_speed='" + point_speed + '\'' +
                ", runTimeTotal='" + runTimeTotal + '\'' +
                ", breakTime='" + breakTime + '\'' +
                ", zhiDongJuli='" + zhiDongJuli + '\'' +
                ", zhiDongChuSuDu='" + zhiDongChuSuDu + '\'' +
                ", liuMiaoJuLi='" + liuMiaoJuLi + '\'' +
                ", zhiDongShiJian='" + zhiDongShiJian + '\'' +
                ", zhiDongJianSuDu='" + zhiDongJianSuDu + '\'' +
                '}';
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getKey() {
        return this.key;
    }

    public void setKey(long key) {
        this.key = key;
    }

    public String getMaxSpeed() {
        return this.maxSpeed;
    }

    public void setMaxSpeed(String maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPoint_speed() {
        return this.point_speed;
    }

    public void setPoint_speed(String point_speed) {
        this.point_speed = point_speed;
    }

    public String getRunTimeTotal() {
        return this.runTimeTotal;
    }

    public void setRunTimeTotal(String runTimeTotal) {
        this.runTimeTotal = runTimeTotal;
    }

    public String getBreakTime() {
        return this.breakTime;
    }

    public void setBreakTime(String breakTime) {
        this.breakTime = breakTime;
    }

    public String getZhiDongJuli() {
        return this.zhiDongJuli;
    }

    public void setZhiDongJuli(String zhiDongJuli) {
        this.zhiDongJuli = zhiDongJuli;
    }

    public String getZhiDongChuSuDu() {
        return this.zhiDongChuSuDu;
    }

    public void setZhiDongChuSuDu(String zhiDongChuSuDu) {
        this.zhiDongChuSuDu = zhiDongChuSuDu;
    }

    public String getLiuMiaoJuLi() {
        return this.liuMiaoJuLi;
    }

    public void setLiuMiaoJuLi(String liuMiaoJuLi) {
        this.liuMiaoJuLi = liuMiaoJuLi;
    }

    public String getZhiDongShiJian() {
        return this.zhiDongShiJian;
    }

    public void setZhiDongShiJian(String zhiDongShiJian) {
        this.zhiDongShiJian = zhiDongShiJian;
    }

    public String getZhiDongJianSuDu() {
        return this.zhiDongJianSuDu;
    }

    public void setZhiDongJianSuDu(String zhiDongJianSuDu) {
        this.zhiDongJianSuDu = zhiDongJianSuDu;
    }
}

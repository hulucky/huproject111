package com.example.hu.huproject.Entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

//速度
@Entity
public class SpeedEntity {
    @Id(autoincrement = true)
    private Long id;
    private long key;
    //运行速度
    private String runSpeed;
    //最大速度
    private String maxSpeed;
    //创建时间
    private String time;
    //速度集合
    private String point_speed;
    //单次测试的运行时间
    private String runTimeTotal;
    /**
     * 匀速分析时保存的数据
     */
    private String yunsu_startTime;//匀速运动起点
    private String yunsu_endTime;//匀速运动末点
    private String yunsu_runTime;//匀速运动时间
    private String yunsu_averageSpeed;//匀速运动过程的平均速度
    /**
     * 减速分析时保存的数据
     */
    private String jiansu_startTime;//减速运动起点
    private String jiansu_endTime;//减速运动末点
    private String jiansu_avegare_jiansudu;//平均减速度
    private String jiansu_time;//减速时间
    private String jiansu_chusudu;//减速初速度
    private String jiansu_mosudu;//减速末速度
    private String jiansu_maxspeed;//减速过程中最大速度
    private String jiansu_minspeed;//减速过程中最小速度
    @Generated(hash = 1083488934)
    public SpeedEntity(Long id, long key, String runSpeed, String maxSpeed,
            String time, String point_speed, String runTimeTotal,
            String yunsu_startTime, String yunsu_endTime, String yunsu_runTime,
            String yunsu_averageSpeed, String jiansu_startTime,
            String jiansu_endTime, String jiansu_avegare_jiansudu,
            String jiansu_time, String jiansu_chusudu, String jiansu_mosudu,
            String jiansu_maxspeed, String jiansu_minspeed) {
        this.id = id;
        this.key = key;
        this.runSpeed = runSpeed;
        this.maxSpeed = maxSpeed;
        this.time = time;
        this.point_speed = point_speed;
        this.runTimeTotal = runTimeTotal;
        this.yunsu_startTime = yunsu_startTime;
        this.yunsu_endTime = yunsu_endTime;
        this.yunsu_runTime = yunsu_runTime;
        this.yunsu_averageSpeed = yunsu_averageSpeed;
        this.jiansu_startTime = jiansu_startTime;
        this.jiansu_endTime = jiansu_endTime;
        this.jiansu_avegare_jiansudu = jiansu_avegare_jiansudu;
        this.jiansu_time = jiansu_time;
        this.jiansu_chusudu = jiansu_chusudu;
        this.jiansu_mosudu = jiansu_mosudu;
        this.jiansu_maxspeed = jiansu_maxspeed;
        this.jiansu_minspeed = jiansu_minspeed;
    }
    @Generated(hash = 1545311017)
    public SpeedEntity() {
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
    public String getRunSpeed() {
        return this.runSpeed;
    }
    public void setRunSpeed(String runSpeed) {
        this.runSpeed = runSpeed;
    }
    public String getMaxSpeed() {
        return this.maxSpeed;
    }
    public void setMaxSpeed(String maxSpeed) {
        this.maxSpeed = maxSpeed;
    }
    public String getTime() {
        return this.time;
    }
    public void setTime(String time) {
        this.time = time;
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
    public String getYunsu_startTime() {
        return this.yunsu_startTime;
    }
    public void setYunsu_startTime(String yunsu_startTime) {
        this.yunsu_startTime = yunsu_startTime;
    }
    public String getYunsu_endTime() {
        return this.yunsu_endTime;
    }
    public void setYunsu_endTime(String yunsu_endTime) {
        this.yunsu_endTime = yunsu_endTime;
    }
    public String getYunsu_runTime() {
        return this.yunsu_runTime;
    }
    public void setYunsu_runTime(String yunsu_runTime) {
        this.yunsu_runTime = yunsu_runTime;
    }
    public String getYunsu_averageSpeed() {
        return this.yunsu_averageSpeed;
    }
    public void setYunsu_averageSpeed(String yunsu_averageSpeed) {
        this.yunsu_averageSpeed = yunsu_averageSpeed;
    }
    public String getJiansu_startTime() {
        return this.jiansu_startTime;
    }
    public void setJiansu_startTime(String jiansu_startTime) {
        this.jiansu_startTime = jiansu_startTime;
    }
    public String getJiansu_endTime() {
        return this.jiansu_endTime;
    }
    public void setJiansu_endTime(String jiansu_endTime) {
        this.jiansu_endTime = jiansu_endTime;
    }
    public String getJiansu_avegare_jiansudu() {
        return this.jiansu_avegare_jiansudu;
    }
    public void setJiansu_avegare_jiansudu(String jiansu_avegare_jiansudu) {
        this.jiansu_avegare_jiansudu = jiansu_avegare_jiansudu;
    }
    public String getJiansu_time() {
        return this.jiansu_time;
    }
    public void setJiansu_time(String jiansu_time) {
        this.jiansu_time = jiansu_time;
    }
    public String getJiansu_chusudu() {
        return this.jiansu_chusudu;
    }
    public void setJiansu_chusudu(String jiansu_chusudu) {
        this.jiansu_chusudu = jiansu_chusudu;
    }
    public String getJiansu_mosudu() {
        return this.jiansu_mosudu;
    }
    public void setJiansu_mosudu(String jiansu_mosudu) {
        this.jiansu_mosudu = jiansu_mosudu;
    }
    public String getJiansu_maxspeed() {
        return this.jiansu_maxspeed;
    }
    public void setJiansu_maxspeed(String jiansu_maxspeed) {
        this.jiansu_maxspeed = jiansu_maxspeed;
    }
    public String getJiansu_minspeed() {
        return this.jiansu_minspeed;
    }
    public void setJiansu_minspeed(String jiansu_minspeed) {
        this.jiansu_minspeed = jiansu_minspeed;
    }

}

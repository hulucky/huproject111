package com.example.hu.huproject.Entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class KongDongTimeEntity {
    //空动时间测试的实体类
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
    private String breakTime1;
    //断开时的时间
    private String breakTime2;
    //空动时间
    private String breakTime;
    private Integer testingNum;//序号
    private boolean isSave;//是否保存
    @Generated(hash = 840304903)
    public KongDongTimeEntity(Long id, long key, String maxSpeed, String createTime,
            String point_speed, String runTimeTotal, String breakTime1,
            String breakTime2, String breakTime, Integer testingNum,
            boolean isSave) {
        this.id = id;
        this.key = key;
        this.maxSpeed = maxSpeed;
        this.createTime = createTime;
        this.point_speed = point_speed;
        this.runTimeTotal = runTimeTotal;
        this.breakTime1 = breakTime1;
        this.breakTime2 = breakTime2;
        this.breakTime = breakTime;
        this.testingNum = testingNum;
        this.isSave = isSave;
    }
    @Generated(hash = 1710842181)
    public KongDongTimeEntity() {
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
    public String getBreakTime1() {
        return this.breakTime1;
    }
    public void setBreakTime1(String breakTime1) {
        this.breakTime1 = breakTime1;
    }
    public String getBreakTime2() {
        return this.breakTime2;
    }
    public void setBreakTime2(String breakTime2) {
        this.breakTime2 = breakTime2;
    }
    public String getBreakTime() {
        return this.breakTime;
    }
    public void setBreakTime(String breakTime) {
        this.breakTime = breakTime;
    }
    public Integer getTestingNum() {
        return this.testingNum;
    }
    public void setTestingNum(Integer testingNum) {
        this.testingNum = testingNum;
    }
    public boolean getIsSave() {
        return this.isSave;
    }
    public void setIsSave(boolean isSave) {
        this.isSave = isSave;
    }
}

package com.example.hu.huproject.Entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class HuiShengLunEntity {
    @Id(autoincrement = true)
    private Long id;
    private long key;
    private Integer testingNum;//序号
    private String maxYuZhangJinLi;//最大预张紧力
    private String bizhi;//比值
    private String poDuanLi;//钢丝绳破断力
    private String time;//测试时间
    private boolean isSave;//是否保存
    @Generated(hash = 1172615503)
    public HuiShengLunEntity(Long id, long key, Integer testingNum,
            String maxYuZhangJinLi, String bizhi, String poDuanLi, String time,
            boolean isSave) {
        this.id = id;
        this.key = key;
        this.testingNum = testingNum;
        this.maxYuZhangJinLi = maxYuZhangJinLi;
        this.bizhi = bizhi;
        this.poDuanLi = poDuanLi;
        this.time = time;
        this.isSave = isSave;
    }
    @Generated(hash = 1203677829)
    public HuiShengLunEntity() {
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
    public Integer getTestingNum() {
        return this.testingNum;
    }
    public void setTestingNum(Integer testingNum) {
        this.testingNum = testingNum;
    }
    public String getMaxYuZhangJinLi() {
        return this.maxYuZhangJinLi;
    }
    public void setMaxYuZhangJinLi(String maxYuZhangJinLi) {
        this.maxYuZhangJinLi = maxYuZhangJinLi;
    }
    public String getBizhi() {
        return this.bizhi;
    }
    public void setBizhi(String bizhi) {
        this.bizhi = bizhi;
    }
    public String getPoDuanLi() {
        return this.poDuanLi;
    }
    public void setPoDuanLi(String poDuanLi) {
        this.poDuanLi = poDuanLi;
    }
    public String getTime() {
        return this.time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public boolean getIsSave() {
        return this.isSave;
    }
    public void setIsSave(boolean isSave) {
        this.isSave = isSave;
    }


}

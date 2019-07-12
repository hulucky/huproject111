package com.example.hu.huproject.Entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class ZhiDongLiEntity {
    @Id(autoincrement = true)
    private Long id;
    private long key;
    private Integer testingNum;//序号
    private String maxZhiDongli;//最大牵引力
    private String bizhi;//比值
    private String zongZhiDongLi;//总制动力
    private String edingZhiDongli;//额定制动力
    private String time;//测试时间
    private boolean isSave;//是否保存
    @Generated(hash = 630346370)
    public ZhiDongLiEntity(Long id, long key, Integer testingNum,
            String maxZhiDongli, String bizhi, String zongZhiDongLi,
            String edingZhiDongli, String time, boolean isSave) {
        this.id = id;
        this.key = key;
        this.testingNum = testingNum;
        this.maxZhiDongli = maxZhiDongli;
        this.bizhi = bizhi;
        this.zongZhiDongLi = zongZhiDongLi;
        this.edingZhiDongli = edingZhiDongli;
        this.time = time;
        this.isSave = isSave;
    }
    @Generated(hash = 2111028953)
    public ZhiDongLiEntity() {
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
    public String getMaxZhiDongli() {
        return this.maxZhiDongli;
    }
    public void setMaxZhiDongli(String maxZhiDongli) {
        this.maxZhiDongli = maxZhiDongli;
    }
    public String getBizhi() {
        return this.bizhi;
    }
    public void setBizhi(String bizhi) {
        this.bizhi = bizhi;
    }
    public String getZongZhiDongLi() {
        return this.zongZhiDongLi;
    }
    public void setZongZhiDongLi(String zongZhiDongLi) {
        this.zongZhiDongLi = zongZhiDongLi;
    }
    public String getEdingZhiDongli() {
        return this.edingZhiDongli;
    }
    public void setEdingZhiDongli(String edingZhiDongli) {
        this.edingZhiDongli = edingZhiDongli;
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

package com.example.hu.huproject.Entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class AngleEntity {
    @Id(autoincrement = true)
    private Long id;
    private long key;
    private String maxAngle;//最大角度
    private String createTime;//创建时间
    private String pointAngle;//角度点集
    private String runTime;//本次测试的运行时间

    @Generated(hash = 350489689)
    public AngleEntity(Long id, long key, String maxAngle, String createTime,
                       String pointAngle, String runTime) {
        this.id = id;
        this.key = key;
        this.maxAngle = maxAngle;
        this.createTime = createTime;
        this.pointAngle = pointAngle;
        this.runTime = runTime;
    }

    @Generated(hash = 602833561)
    public AngleEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getKey() {
        return key;
    }

    public void setKey(long key) {
        this.key = key;
    }

    public String getMaxAngle() {
        return maxAngle;
    }

    public void setMaxAngle(String maxAngle) {
        this.maxAngle = maxAngle;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPointAngle() {
        return pointAngle;
    }

    public void setPointAngle(String pointAngle) {
        this.pointAngle = pointAngle;
    }

    public String getRunTime() {
        return runTime;
    }

    public void setRunTime(String runTime) {
        this.runTime = runTime;
    }
}

package com.example.hu.huproject.Entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class QianYinLiEntity {
    @Id(autoincrement = true)
    private Long id;
    private long key;
    private Integer testingNum;//序号
    private String maxQianyinli;//最大牵引力
    private String chazhi;//差值
    private String edingQianyinli;//额定牵引力
    private String time;//测试时间
    private boolean isSave;//是否保存
    @Generated(hash = 1746908734)
    public QianYinLiEntity(Long id, long key, Integer testingNum,
            String maxQianyinli, String chazhi, String edingQianyinli, String time,
            boolean isSave) {
        this.id = id;
        this.key = key;
        this.testingNum = testingNum;
        this.maxQianyinli = maxQianyinli;
        this.chazhi = chazhi;
        this.edingQianyinli = edingQianyinli;
        this.time = time;
        this.isSave = isSave;
    }
    @Generated(hash = 2051217239)
    public QianYinLiEntity() {
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
    public String getMaxQianyinli() {
        return this.maxQianyinli;
    }
    public void setMaxQianyinli(String maxQianyinli) {
        this.maxQianyinli = maxQianyinli;
    }
    public String getChazhi() {
        return this.chazhi;
    }
    public void setChazhi(String chazhi) {
        this.chazhi = chazhi;
    }
    public String getEdingQianyinli() {
        return this.edingQianyinli;
    }
    public void setEdingQianyinli(String edingQianyinli) {
        this.edingQianyinli = edingQianyinli;
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

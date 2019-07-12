package com.example.hu.huproject.Entity;

import com.xzkydz.function.search.module.ITaskRoot;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class TaskEntity implements ITaskRoot {
    //测试任务id---不能用int
    @Id(autoincrement = true)
    private Long id;
    //受检单位名称
    private String unitName;
    //单轨机编号
    private String convNumber;
    //测试员
    private String peopleName;
    //测试任务的状态
    private boolean _IsCompleteTask;
    //测试任务的创建时间
    private String greateTaskTime;
    //测试任务已经测得测试数据条数
    private int tashHaveGetData;
    //测试的类型(在历史搜索界面点击继续测试时，判断该返回哪个activity)
    private int taskType;
    //测试任务是否保存
    private boolean is_SpeedAngleSave;//速度与角度
    private boolean is_SpeedSave;//速度
    private boolean is_AngleSave;//角度
    private boolean is_KongTimeSave;//空动时间
    private boolean is_ManZaiXiangXiaSave;//满载向下
    private boolean is_KongZaiUpSave;//空载向上
    private boolean is_QianYinLiSave;//牵引力
    private boolean is_ZhiDongLiSave;//制动力
    private boolean is_HuiShengLunSave;//回绳轮预张力
    private Integer zdlTestingNum;//制动力
    private Integer qylTestingNum;//牵引力
    private Integer hslTestingNum;//回绳轮预张力
    //测试任务已经测得测试数据条数
    private int taskHaveGetData;
    //隐藏是否打开
    private boolean isAllowInput;
    private boolean is_WeightSave;

    @Override
    public String toString() {
        return "TaskEntity{" +
                "id=" + id +
                ", unitName='" + unitName + '\'' +
                ", convNumber='" + convNumber + '\'' +
                ", peopleName='" + peopleName + '\'' +
                ", _IsCompleteTask=" + _IsCompleteTask +
                ", greateTaskTime='" + greateTaskTime + '\'' +
                ", tashHaveGetData=" + tashHaveGetData +
                ", taskType=" + taskType +
                ", is_SpeedAngleSave=" + is_SpeedAngleSave +
                ", is_SpeedSave=" + is_SpeedSave +
                ", is_AngleSave=" + is_AngleSave +
                ", is_KongTimeSave=" + is_KongTimeSave +
                ", is_ManZaiXiangXiaSave=" + is_ManZaiXiangXiaSave +
                ", is_KongZaiUpSave=" + is_KongZaiUpSave +
                ", is_QianYinLiSave=" + is_QianYinLiSave +
                ", is_ZhiDongLiSave=" + is_ZhiDongLiSave +
                ", is_HuiShengLunSave=" + is_HuiShengLunSave +
                ", zdlTestingNum=" + zdlTestingNum +
                ", qylTestingNum=" + qylTestingNum +
                ", hslTestingNum=" + hslTestingNum +
                ", taskHaveGetData=" + taskHaveGetData +
                ", isAllowInput=" + isAllowInput +
                ", is_WeightSave=" + is_WeightSave +
                '}';
    }

    @Generated(hash = 250090569)
    public TaskEntity(Long id, String unitName, String convNumber,
            String peopleName, boolean _IsCompleteTask, String greateTaskTime,
            int tashHaveGetData, int taskType, boolean is_SpeedAngleSave,
            boolean is_SpeedSave, boolean is_AngleSave, boolean is_KongTimeSave,
            boolean is_ManZaiXiangXiaSave, boolean is_KongZaiUpSave,
            boolean is_QianYinLiSave, boolean is_ZhiDongLiSave,
            boolean is_HuiShengLunSave, Integer zdlTestingNum,
            Integer qylTestingNum, Integer hslTestingNum, int taskHaveGetData,
            boolean isAllowInput, boolean is_WeightSave) {
        this.id = id;
        this.unitName = unitName;
        this.convNumber = convNumber;
        this.peopleName = peopleName;
        this._IsCompleteTask = _IsCompleteTask;
        this.greateTaskTime = greateTaskTime;
        this.tashHaveGetData = tashHaveGetData;
        this.taskType = taskType;
        this.is_SpeedAngleSave = is_SpeedAngleSave;
        this.is_SpeedSave = is_SpeedSave;
        this.is_AngleSave = is_AngleSave;
        this.is_KongTimeSave = is_KongTimeSave;
        this.is_ManZaiXiangXiaSave = is_ManZaiXiangXiaSave;
        this.is_KongZaiUpSave = is_KongZaiUpSave;
        this.is_QianYinLiSave = is_QianYinLiSave;
        this.is_ZhiDongLiSave = is_ZhiDongLiSave;
        this.is_HuiShengLunSave = is_HuiShengLunSave;
        this.zdlTestingNum = zdlTestingNum;
        this.qylTestingNum = qylTestingNum;
        this.hslTestingNum = hslTestingNum;
        this.taskHaveGetData = taskHaveGetData;
        this.isAllowInput = isAllowInput;
        this.is_WeightSave = is_WeightSave;
    }

    @Generated(hash = 397975341)
    public TaskEntity() {
    }






    @Override
    public Long getTaskId() {
        return id;
    }

    //受检单位名称
    @Override
    public String getUnitName() {
        return unitName;
    }

    @Override
    public String getTestFunction() {
        return null;
    }

    //单轨机编号
    @Override
    public String getNumber() {
        return convNumber;
    }
    //测试员
    @Override
    public String getPeopleName() {
        return peopleName;
    }

    //测试任务已经测得测试数据条数
    @Override
    public int getTaskHaveGetData() {
        return tashHaveGetData;
    }

    //测试任务的状态
    @Override
    public boolean getTaskStatue() {
        return _IsCompleteTask;
    }

    //测试任务的创建时间
    @Override
    public String getGreateTaskTime() {
        return greateTaskTime;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getConvNumber() {
        return this.convNumber;
    }

    public void setConvNumber(String convNumber) {
        this.convNumber = convNumber;
    }

    public void setPeopleName(String peopleName) {
        this.peopleName = peopleName;
    }

    public boolean get_IsCompleteTask() {
        return this._IsCompleteTask;
    }

    public void set_IsCompleteTask(boolean _IsCompleteTask) {
        this._IsCompleteTask = _IsCompleteTask;
    }

    public void setGreateTaskTime(String greateTaskTime) {
        this.greateTaskTime = greateTaskTime;
    }

    public int getTashHaveGetData() {
        return this.tashHaveGetData;
    }

    public void setTashHaveGetData(int tashHaveGetData) {
        this.tashHaveGetData = tashHaveGetData;
    }

    public int getTaskType() {
        return this.taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }

    public boolean getIs_SpeedAngleSave() {
        return this.is_SpeedAngleSave;
    }

    public void setIs_SpeedAngleSave(boolean is_SpeedAngleSave) {
        this.is_SpeedAngleSave = is_SpeedAngleSave;
    }

    public boolean getIs_SpeedSave() {
        return this.is_SpeedSave;
    }

    public void setIs_SpeedSave(boolean is_SpeedSave) {
        this.is_SpeedSave = is_SpeedSave;
    }

    public boolean getIs_AngleSave() {
        return this.is_AngleSave;
    }

    public void setIs_AngleSave(boolean is_AngleSave) {
        this.is_AngleSave = is_AngleSave;
    }

    public boolean getIs_KongTimeSave() {
        return this.is_KongTimeSave;
    }

    public void setIs_KongTimeSave(boolean is_KongTimeSave) {
        this.is_KongTimeSave = is_KongTimeSave;
    }

    public boolean getIs_ManZaiXiangXiaSave() {
        return this.is_ManZaiXiangXiaSave;
    }

    public void setIs_ManZaiXiangXiaSave(boolean is_ManZaiXiangXiaSave) {
        this.is_ManZaiXiangXiaSave = is_ManZaiXiangXiaSave;
    }

    public boolean getIs_KongZaiUpSave() {
        return this.is_KongZaiUpSave;
    }

    public void setIs_KongZaiUpSave(boolean is_KongZaiUpSave) {
        this.is_KongZaiUpSave = is_KongZaiUpSave;
    }

    public boolean getIs_QianYinLiSave() {
        return this.is_QianYinLiSave;
    }

    public void setIs_QianYinLiSave(boolean is_QianYinLiSave) {
        this.is_QianYinLiSave = is_QianYinLiSave;
    }

    public boolean getIs_ZhiDongLiSave() {
        return this.is_ZhiDongLiSave;
    }

    public void setIs_ZhiDongLiSave(boolean is_ZhiDongLiSave) {
        this.is_ZhiDongLiSave = is_ZhiDongLiSave;
    }

    public boolean getIs_HuiShengLunSave() {
        return this.is_HuiShengLunSave;
    }

    public void setIs_HuiShengLunSave(boolean is_HuiShengLunSave) {
        this.is_HuiShengLunSave = is_HuiShengLunSave;
    }

    public Integer getZdlTestingNum() {
        return this.zdlTestingNum;
    }

    public void setZdlTestingNum(Integer zdlTestingNum) {
        this.zdlTestingNum = zdlTestingNum;
    }

    public Integer getQylTestingNum() {
        return this.qylTestingNum;
    }

    public void setQylTestingNum(Integer qylTestingNum) {
        this.qylTestingNum = qylTestingNum;
    }

    public Integer getHslTestingNum() {
        return this.hslTestingNum;
    }

    public void setHslTestingNum(Integer hslTestingNum) {
        this.hslTestingNum = hslTestingNum;
    }

    public void setTaskHaveGetData(int taskHaveGetData) {
        this.taskHaveGetData = taskHaveGetData;
    }

    public boolean getIsAllowInput() {
        return this.isAllowInput;
    }

    public void setIsAllowInput(boolean isAllowInput) {
        this.isAllowInput = isAllowInput;
    }

    public boolean getIs_WeightSave() {
        return this.is_WeightSave;
    }

    public void setIs_WeightSave(boolean is_WeightSave) {
        this.is_WeightSave = is_WeightSave;
    }


}

package com.example.hu.huproject.Interface;

import android.content.Context;

import com.example.hu.huproject.Entity.TaskEntity;

public interface ITaskInfo {
    //检查参数的合法性
    boolean checkParRight(Context context);

    /*新建测试任务插入数据库*/
    Long insertTaskTodb(TaskEntity taskEnity);

    /*将测试任务Enity抛为全局*/
    void setGlobalTaskEnity(TaskEntity taskEnity);

    /*创建新的测试任务*/
    TaskEntity newBuildTaskEntity( );

    void initViewForHistory(TaskEntity historyTask );
}

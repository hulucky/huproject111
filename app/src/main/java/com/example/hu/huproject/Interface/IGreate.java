package com.example.hu.huproject.Interface;

import com.example.hu.huproject.Entity.TaskEntity;

public interface IGreate {
    /*用选择的已完成历史任务，初始化当前界面*/
    void setParForHistoryTask(TaskEntity taskEntity);
}

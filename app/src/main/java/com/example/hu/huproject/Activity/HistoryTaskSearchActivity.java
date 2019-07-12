package com.example.hu.huproject.Activity;

import android.util.Log;

import com.example.hu.huproject.Application.MyApp;
import com.example.hu.huproject.Entity.TaskEntity;
import com.example.hu.huproject.Fragment.ZhiDongliTestFragment;
import com.xzkydz.function.search.module.ITaskRoot;
import com.xzkydz.function.search.view.SearchHistoryTaskActivity;

import java.util.ArrayList;
import java.util.List;

public class HistoryTaskSearchActivity extends SearchHistoryTaskActivity {

    private TaskEntity taskEntity;


    //返回所有的任务集合
    @Override
    protected List<ITaskRoot> getTaskDataList() {
        List<ITaskRoot> list = new ArrayList<>();
        List<TaskEntity> taskEntities = MyApp.getInstance().getmDaoSession().getTaskEntityDao().loadAll();
        list.addAll(taskEntities);
        return list;
    }

    //只要进入到继续测试和复用参数界面，就会调用此方法
    @Override
    protected Class getTestClass(ITaskRoot iTaskRoot) {
        if (iTaskRoot != null) {
            Log.d("qqqqq", "getTestClass: ");
            taskEntity = MyApp.getInstance().getmDaoSession().getTaskEntityDao().load(iTaskRoot.getTaskId());
        }
        return CreateTaskActivity.class;
    }

    //继续测试
    @Override
    public void continueTestOnclickListener() {
        super.continueTestOnclickListener();
        if(taskEntity!=null){
            Log.d("qqqqq", "继续测试: ");
            MyApp.setTaskEntity(taskEntity);
        }
    }

    //复用参数
    @Override
    public void reuseParesOnclickListener() {
        super.reuseParesOnclickListener();
        Log.d("qqqqq", "复用参数: ");
    }

}

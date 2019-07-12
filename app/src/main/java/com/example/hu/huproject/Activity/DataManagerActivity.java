package com.example.hu.huproject.Activity;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.example.hu.huproject.Application.MyApp;
import com.example.hu.huproject.Entity.TaskEntity;
import com.example.hu.huproject.Utils.TaskEntityUtils;
import com.xzkydz.function.data.view.DataMagerActivity;
import com.xzkydz.function.search.module.ITaskRoot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class DataManagerActivity extends DataMagerActivity {
    @Override
    public void getTaskList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<ITaskRoot> list = new ArrayList<>();
                List<TaskEntity> taskEntities = MyApp.getInstance().getmDaoSession().getTaskEntityDao().loadAll();
                Collections.sort(taskEntities, new Comparator<TaskEntity>() {
                    @Override
                    public int compare(TaskEntity o1, TaskEntity o2) {
                        String time1 = o1.getGreateTaskTime();
                        String time2 = o2.getGreateTaskTime();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date dt1 = null;
                        Date dt2 = null;
                        try {
                            dt1 = sdf.parse(time1);
                            dt2 = sdf.parse(time2);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if(dt1.before(dt2)){
                            return 1;
                        }
                        return -1;
                    }
                });
                list.addAll(taskEntities);
                setDataAndPushView(list);
            }
        }).start();
    }

    @NonNull
    @Override
    public Class<?> getDataDetailActivity() {
        return DataDetailActivity.class;
    }

    @Override
    public boolean setTaskComplete(ITaskRoot iTaskRoot) {
        TaskEntity taskEntity = (TaskEntity) iTaskRoot;
        taskEntity.set_IsCompleteTask(true);
        TaskEntityUtils.update(taskEntity);
        return true;
    }

    @Override
    public boolean deleteTaskAndData(ITaskRoot iTaskRoot) {
        TaskEntityUtils.delete(iTaskRoot.getTaskId());
        return true;
    }

    /*是否显示录入测试数据选项*/
    @Override
    public boolean isShowInputFragment() {
        return false;
    }

    /*设置输入Fragment*/
    @Override
    public Fragment setInputFragment() {
        return null;
    }
}

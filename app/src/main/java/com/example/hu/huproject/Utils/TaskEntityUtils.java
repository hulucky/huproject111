package com.example.hu.huproject.Utils;

import com.example.hu.huproject.Application.MyApp;
import com.example.hu.huproject.Entity.TaskEntity;

import java.util.List;

public class TaskEntityUtils {
    /**
     * 添加数据，如果有重复则覆盖
     * @param greateTask
     */
    public static Long insert(TaskEntity greateTask){
        return MyApp.getInstance().getmDaoSession().getTaskEntityDao().insert(greateTask);
    }
    /**
     * 删除数据
     * @param id
     */
    public static void delete(long id) {
        MyApp.getInstance().getmDaoSession().getTaskEntityDao().deleteByKey(id);
    }
    /**
     * 更新数据
     * @param greateTask
     */
    public static void update(TaskEntity greateTask) {
        MyApp.getInstance().getmDaoSession().getTaskEntityDao().update(greateTask);
    }
    /**
     * 查询Id为1的数据
     * @return
     */
    public static TaskEntity query(Long id) {
        return MyApp.getInstance().getmDaoSession().getTaskEntityDao().load(id);
    }
    /**
     * 查询全部数据
     */
    public static List<TaskEntity> queryAll() {
        return MyApp.getInstance().getmDaoSession().getTaskEntityDao().loadAll();
    }
}

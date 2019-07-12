package com.example.hu.huproject.TaskInfoHu;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import com.example.hu.huproject.Activity.CreateTaskActivity;
import com.example.hu.huproject.Entity.TaskEntity;
import com.example.hu.huproject.Interface.ITaskInfo;
import com.example.hu.huproject.R;
import com.xzkydz.function.utils.DateUtil;

import java.util.ArrayList;
import java.util.List;

public class TaskInfo implements ITaskInfo {
    private CreateTaskActivity createTaskActivity;


    public TaskInfo(CreateTaskActivity createTaskActivity) {
        this.createTaskActivity = createTaskActivity;
    }

    //检查参数的合法性
    @Override
    public boolean checkParRight(Context context) {
        Resources res = context.getResources();
        String infString = "";
        final List<View> errorViews = new ArrayList<>();
        if (isEmpty(createTaskActivity.et1)) {
            infString += res.getString(R.string.task_unit_notnull) + "\n";
            errorViews.add(createTaskActivity.et1);
        }
        if (isEmpty(createTaskActivity.et2)) {
            infString += res.getString(R.string.task_num_notnull) + "\n";
            errorViews.add(createTaskActivity.et2);
        }
        if (isEmpty(createTaskActivity.et3)) {
            infString += res.getString(R.string.task_man_notnull) + "\n";
            errorViews.add(createTaskActivity.et3);
        }
        //如果有错误参数，返回True
        if(errorViews.size()!=0){
            AlertDialog.Builder normalDialog = new AlertDialog.Builder(createTaskActivity);
            normalDialog.setTitle("错误信息提示");
            normalDialog.setMessage(infString);

            normalDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Animation mShakeAnim = AnimationUtils.loadAnimation(createTaskActivity, R.anim.shake_x);
                    for (int i = 0; i < errorViews.size(); i++) {
                        errorViews.get(i).startAnimation(mShakeAnim);
                    }
                }
            });
            normalDialog.show();
            return false;
        }
        return true;
    }

    /*新建测试任务插入数据库*/
    @Override
    public Long insertTaskTodb(TaskEntity taskEnity) {
        return null;
    }

    /*将测试任务Enity抛为全局*/
    @Override
    public void setGlobalTaskEnity(TaskEntity taskEnity) {

    }

    /*创建新的测试任务*/
    @Override
    public TaskEntity newBuildTaskEntity( ) {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setUnitName(createTaskActivity.et1.getText().toString());//单位名称
        taskEntity.setConvNumber(createTaskActivity.et2.getText().toString());//单轨机编号
        taskEntity.setPeopleName(createTaskActivity.et3.getText().toString());//测试人员
        taskEntity.setGreateTaskTime(DateUtil.getGreatedTaskTime());//任务创建时间
//        taskEntity.setTaskType(type);//测试任务的类型（共九种）
        return taskEntity;
    }

    @Override
    public void initViewForHistory(TaskEntity historyTask) {

    }

    //检测输入框不为空
    private boolean isEmpty(EditText editText) {
        return TextUtils.isEmpty(editText.getText().toString());
    }
}

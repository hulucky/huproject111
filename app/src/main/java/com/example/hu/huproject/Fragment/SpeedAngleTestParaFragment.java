package com.example.hu.huproject.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.db.manager.TaskEntityDao;
import com.example.hu.huproject.Application.MyApp;
import com.example.hu.huproject.Entity.TaskEntity;
import com.example.hu.huproject.R;
import com.mchsdk.paysdk.mylibrary.ListFragment;

import butterknife.BindView;
import butterknife.OnClick;

//速度与角度测试的参数界面
public class SpeedAngleTestParaFragment extends ListFragment {

    @BindView(R.id.tv_task)
    TextView tvTask;
    @BindView(R.id.tv_checked_param)
    TextView tvCheckedParam;
    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.ll_content1)
    LinearLayout llContent1;
    @BindView(R.id.tv_test_person)
    TextView tvTestPerson;
    @BindView(R.id.tv_item_result1)
    TextView tvItemResult1;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    private TaskEntity taskEntity;

    //实例化自己，可以从外部传参
    public static SpeedAngleTestParaFragment newInstance(int position) {
        SpeedAngleTestParaFragment testParaFragment = new SpeedAngleTestParaFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        testParaFragment.setArguments(bundle);
        return testParaFragment;
    }

    @Override
    protected void lazyLoadData() {
        taskEntity = MyApp.getTaskEntity();
        if (taskEntity != null) {
            tvCheckedParam.setText(taskEntity.getUnitName());//受检单位
            tvNumber.setText(taskEntity.getNumber());//单轨机编号
            tvTestPerson.setText(taskEntity.getPeopleName());//测试人员
            tvItemResult1.setText(taskEntity.getGreateTaskTime());//创建时间
        }else {
            Toast.makeText(getContext(), "taskEntity为空", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void initView() {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_test_param;
    }


    @OnClick({R.id.tv_task, R.id.tv_checked_param})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_task:
                Toast.makeText(getContext(), "哈撒给", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_checked_param:
                break;
        }
    }
}

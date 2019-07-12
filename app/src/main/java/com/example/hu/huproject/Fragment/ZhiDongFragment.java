package com.example.hu.huproject.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.hu.huproject.R;
import com.mchsdk.paysdk.mylibrary.ListFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

//制动测试fragment
public class ZhiDongFragment extends ListFragment {
    public Context instance;
    @BindView(R.id.radioBtn1)
    RadioButton radioBtn1;
    @BindView(R.id.radioBtn2)
    RadioButton radioBtn2;
    @BindView(R.id.radioBtn3)
    RadioButton radioBtn3;
    @BindView(R.id.radioBtn4)
    RadioButton radioBtn4;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.tv_sure)
    TextView tvSure;
    @BindView(R.id.linear_dialog)
    LinearLayout linearDialog;
    @BindView(R.id.tv_ceshi)
    TextView tvCeshi;

    private boolean isShowDialog = true;//用于判断是否该显示dialog布局
    public static ZhiDongFragment newInstance(int position) {
        ZhiDongFragment fragment = new ZhiDongFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void lazyLoadData() {
        linearDialog.setVisibility(View.VISIBLE);
//        if(isShowDialog){
//            linearDialog.setVisibility(View.VISIBLE);
//        }
    }

    @Override
    protected void initView() {
        instance = getActivity();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_zhidong;
    }


    @OnClick({R.id.tv_sure, R.id.tv_ceshi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_sure:
                linearDialog.setVisibility(View.GONE);
                isShowDialog = false;
                break;
            case R.id.tv_ceshi:
                break;
        }
    }
}

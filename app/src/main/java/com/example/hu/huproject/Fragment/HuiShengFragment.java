package com.example.hu.huproject.Fragment;

import android.os.Bundle;

import com.example.hu.huproject.R;
import com.mchsdk.paysdk.mylibrary.ListFragment;

//回绳轮放张力测试fragment
public class HuiShengFragment extends ListFragment {

    public static HuiShengFragment newInstance(int position){
        HuiShengFragment fragment = new HuiShengFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position",position);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    protected void lazyLoadData() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_huisheng;
    }
}

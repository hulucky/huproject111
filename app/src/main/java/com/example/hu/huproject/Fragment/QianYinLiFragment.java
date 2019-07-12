package com.example.hu.huproject.Fragment;

import android.os.Bundle;

import com.example.hu.huproject.R;
import com.mchsdk.paysdk.mylibrary.ListFragment;

//牵引力测试fragment
public class QianYinLiFragment extends ListFragment {

    public static QianYinLiFragment newInstance(int position){
        QianYinLiFragment fragment = new QianYinLiFragment();
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
        return R.layout.fragment_qianyinli;
    }
}

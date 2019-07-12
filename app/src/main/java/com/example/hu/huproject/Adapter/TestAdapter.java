package com.example.hu.huproject.Adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class TestAdapter extends FragmentPagerAdapter {
    private List<String> titleList;
    private Context context;
    private List<Fragment> fragmentList;
    private final int COUNT;

    public TestAdapter(FragmentManager fm, int count, List<String> titleList, List<Fragment> fragmentList, Context context) {
        super(fm);
        if (titleList == null || titleList.isEmpty()) {
            throw new ExceptionInInitializerError("list can't be null or empty");
        }
        if (count <= 0) {
            throw new ExceptionInInitializerError("count 小于0");
        }
        this.COUNT = count;
        this.titleList = titleList;
        this.context = context;
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return COUNT;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }
}

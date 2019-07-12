package com.example.hu.huproject.DetailFragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.hu.huproject.Activity.DataDetailActivity;
import com.example.hu.huproject.Adapter.DetailHuiShengLunAdapter;
import com.example.hu.huproject.Adapter.DetailQianYinLiAdapter;
import com.example.hu.huproject.Entity.HuiShengLunEntity;
import com.example.hu.huproject.Entity.QianYinLiEntity;
import com.example.hu.huproject.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

//数据管理中牵引力界面
public class Detail_HuiShengLun_fragment extends Fragment {
//    TextView tvYuZjl;
//    LinearLayout llHsl;
    TextView tvZuidaYuZjl;
    LinearLayout llZuidaQyl;
    TextView tvBizhi0;
    TextView tvBizhi;
    LinearLayout llBizhi;
    RelativeLayout llChart;
    TextView item0;
    TextView item1;
    TextView item2;
    TextView item3;
    TextView item4;
    LinearLayout llTittleName;
    View line;
    ListView listview;
    RelativeLayout flData;

    Unbinder unbinder;

    private View view;
    private DetailHuiShengLunAdapter hslAdapter;


    public static Detail_HuiShengLun_fragment newInstance(Long id) {
        Detail_HuiShengLun_fragment fragment = new Detail_HuiShengLun_fragment();
        Bundle bundle = new Bundle();
        bundle.putLong("id", id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_detail_huishenglun, container, false);
        initView();
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    public void initView() {
        if (view != null) {
            tvZuidaYuZjl = view.findViewById(R.id.tv_zuida_yuZjl);
            llZuidaQyl = view.findViewById(R.id.ll_zuida_yuZjl);
            tvBizhi0 = view.findViewById(R.id.tv_bizhi0);
            tvBizhi = view.findViewById(R.id.tv_bizhi);
            llBizhi = view.findViewById(R.id.ll_bizhi);
            llChart = view.findViewById(R.id.ll_chart);
            item0 = view.findViewById(R.id.item0);
            item1 = view.findViewById(R.id.item1);
            item2 = view.findViewById(R.id.item2);
            item3 = view.findViewById(R.id.item3);
            item4 = view.findViewById(R.id.item4);
            llTittleName = view.findViewById(R.id.ll_tittle_name);
            line = view.findViewById(R.id.line);
            listview = view.findViewById(R.id.listview);
            flData = view.findViewById(R.id.fl_data);
            initData();
        }
    }

    private void initData() {
        DataDetailActivity activity = (DataDetailActivity) getActivity();
        //拿到数据源
        List<HuiShengLunEntity> huiShengLunEntities = activity.huiShengLunEntities;
        hslAdapter = new DetailHuiShengLunAdapter(getActivity(),huiShengLunEntities,Detail_HuiShengLun_fragment.this);
        listview.setAdapter(hslAdapter);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    //删除按钮的点击事件
    public void showInitRes() {
        tvZuidaYuZjl.setText("");
        tvBizhi.setText("");
    }
    //每一项item点击事件
    public void onItemClick(HuiShengLunEntity entity) {
       tvZuidaYuZjl.setText(entity.getMaxYuZhangJinLi());
       tvBizhi.setText(entity.getBizhi());
    }


}
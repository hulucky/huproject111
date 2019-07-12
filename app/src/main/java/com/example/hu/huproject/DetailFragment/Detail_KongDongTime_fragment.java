package com.example.hu.huproject.DetailFragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.hu.huproject.Activity.DataDetailActivity;
import com.example.hu.huproject.Adapter.DetailKongTimeAdapter;
import com.example.hu.huproject.Adapter.DetailQianYinLiAdapter;
import com.example.hu.huproject.Entity.KongDongTimeEntity;
import com.example.hu.huproject.Entity.QianYinLiEntity;
import com.example.hu.huproject.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

//数据管理中空动时间测试界面
public class Detail_KongDongTime_fragment extends Fragment {
    Unbinder unbinder;
    TextView item0;
    TextView item1;
    TextView item2;
    TextView item3;
    TextView item4;
    LinearLayout llTittleName;
    View line;
    ListView listview;
    RelativeLayout flData;

    private View view;
    private DetailKongTimeAdapter kongtimeAdapter;


    public static Detail_KongDongTime_fragment newInstance(Long id) {
        Detail_KongDongTime_fragment fragment = new Detail_KongDongTime_fragment();
        Bundle bundle = new Bundle();
        bundle.putLong("id", id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_detail_kongdongtime, container, false);
        initView();
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    public void initView() {
        if (view != null) {
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
        List<KongDongTimeEntity> kongDongTimeEntities = activity.kongDongTimeEntities;
        kongtimeAdapter = new DetailKongTimeAdapter(getActivity(), kongDongTimeEntities, Detail_KongDongTime_fragment.this);
        listview.setAdapter(kongtimeAdapter);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    //删除按钮的点击事件
    public void showInitRes() {

    }
}

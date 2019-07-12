package com.example.hu.huproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.hu.huproject.Entity.KongDongTimeEntity;
import com.example.hu.huproject.Entity.QianYinLiEntity;
import com.example.hu.huproject.Fragment.KongDongTimeTestFragment;
import com.example.hu.huproject.Fragment.QianYinLiFragmentVp1;
import com.example.hu.huproject.R;

import java.util.List;


/**
 * Created by yk on 2016/12/26.
 * 空动时间采集结果ListView
 */

public class NvKongTimeAdapter extends BaseAdapter {
    private Context context;
    private List<KongDongTimeEntity> list;
    private KongDongTimeTestFragment kongDongTimeTestFragment;

    public NvKongTimeAdapter(Context context, List<KongDongTimeEntity> list, KongDongTimeTestFragment kongDongTimeTestFragment) {
        this.context = context;
        this.list = list;
        this.kongDongTimeTestFragment = kongDongTimeTestFragment;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateListView(List<KongDongTimeEntity> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item_kongtime, null);
            viewHolder.textView0 = (TextView) view.findViewById(R.id.item0);
            viewHolder.textView1 = (TextView) view.findViewById(R.id.item1);
            viewHolder.textView2 = (TextView) view.findViewById(R.id.item2);
            viewHolder.textView3 = (TextView) view.findViewById(R.id.item3);
            viewHolder.button = (ImageButton) view.findViewById(R.id.item4);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.textView0.setText(i + 1 + "");
        viewHolder.textView1.setText(list.get(i).getBreakTime1());
        viewHolder.textView2.setText(list.get(i).getBreakTime2());
        viewHolder.textView3.setText(list.get(i).getBreakTime());
        //删除点击事件
        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KongDongTimeEntity kongDongTimeEntity = list.get(i);
                list.remove(i);
                updateListView(list);//外面的list也会跟着更新
                kongDongTimeTestFragment.showInitRes(kongDongTimeEntity,list);
            }
        });

        return view;
    }

    class ViewHolder {

        TextView textView0;
        TextView textView1;
        TextView textView2;
        TextView textView3;
        ImageButton button;
    }
}

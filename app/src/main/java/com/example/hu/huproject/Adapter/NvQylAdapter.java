package com.example.hu.huproject.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.hu.huproject.Entity.ZhiDongLiEntity;
import com.example.hu.huproject.Fragment.ZhiDongLiFragmentVp1;
import com.example.hu.huproject.R;

import java.util.List;


/**
 * 制动力listview的adapter
 */

public class NvQylAdapter extends BaseAdapter {
    private Context context;
    private List<ZhiDongLiEntity> list;
    private ZhiDongLiFragmentVp1 zdlFragmentVp1;

    public NvQylAdapter(Context context, List<ZhiDongLiEntity> list, ZhiDongLiFragmentVp1 zdlFragmentVp1) {
        this.context = context;
        this.list = list;
        this.zdlFragmentVp1 = zdlFragmentVp1;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateListView(List<ZhiDongLiEntity> list) {
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
            view = LayoutInflater.from(context).inflate(R.layout.item_qyl, null);
            viewHolder.textView0 = (TextView) view.findViewById(R.id.item0);
            viewHolder.textView1 = (TextView) view.findViewById(R.id.item1);
            viewHolder.textView2 = (TextView) view.findViewById(R.id.item2);
            viewHolder.button = (ImageButton) view.findViewById(R.id.item3);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.textView0.setText(i + 1 + "");
        viewHolder.textView1.setText(list.get(i).getMaxZhiDongli());
        viewHolder.textView2.setText(list.get(i).getTime());
        //删除点击事件
        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ZhiDongLiEntity zhiDongLiEntity = list.get(i);
                list.remove(i);
                updateListView(list);//外面的list也会跟着更新
                zdlFragmentVp1.showInitRes(zhiDongLiEntity, list);
            }
        });

        return view;
    }

    class ViewHolder {

        TextView textView0;
        TextView textView1;
        TextView textView2;
        ImageButton button;
    }
}

package com.example.hu.huproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.hu.huproject.Entity.HuiShengLunEntity;
import com.example.hu.huproject.Entity.ZhiDongLiEntity;
import com.example.hu.huproject.Fragment.HuiShengLunFragmentVp1;
import com.example.hu.huproject.Fragment.ZhiDongLiFragmentVp1;
import com.example.hu.huproject.R;

import java.util.List;


/**
 * Created by yk on 2016/12/26.
 * 回绳轮采集结果ListView
 */

public class NvHslAdapter extends BaseAdapter {
    private Context context;
    private List<HuiShengLunEntity> list;
    private HuiShengLunFragmentVp1 huiShengLunFragmentVp1;

    public NvHslAdapter(Context context, List<HuiShengLunEntity> list, HuiShengLunFragmentVp1 huiShengLunFragmentVp1) {
        this.context = context;
        this.list = list;
        this.huiShengLunFragmentVp1 = huiShengLunFragmentVp1;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateListView(List<HuiShengLunEntity> list) {
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
            view = LayoutInflater.from(context).inflate(R.layout.item_qyl1, null);
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
        viewHolder.textView1.setText(list.get(i).getMaxYuZhangJinLi());
        viewHolder.textView2.setText(list.get(i).getBizhi());
        viewHolder.textView3.setText(list.get(i).getTime());
        //删除点击事件
        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HuiShengLunEntity huiShengLunEntity = list.get(i);
                list.remove(i);
                updateListView(list);//外面的list也会跟着更新
                huiShengLunFragmentVp1.showInitRes(huiShengLunEntity,list);
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

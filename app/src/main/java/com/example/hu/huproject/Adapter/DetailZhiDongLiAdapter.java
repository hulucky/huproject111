package com.example.hu.huproject.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hu.huproject.Application.MyApp;
import com.example.hu.huproject.DetailFragment.Detail_QianYinLi_fragment;
import com.example.hu.huproject.DetailFragment.Detail_ZhiDongLi_fragment;
import com.example.hu.huproject.Entity.QianYinLiEntity;
import com.example.hu.huproject.Entity.ZhiDongLiEntity;
import com.example.hu.huproject.R;

import java.util.ArrayList;
import java.util.List;


/**
 * 制动力adapter
 */

public class DetailZhiDongLiAdapter extends BaseAdapter {
    private Context context;
    private List<ZhiDongLiEntity> list;
    private Detail_ZhiDongLi_fragment fragment;
    private List<Boolean> isClick = new ArrayList<>();//控件是否被点击,默认为false，如果被点击，改变值，控件根据值改变自身颜色


    public DetailZhiDongLiAdapter(Context context, List<ZhiDongLiEntity> list, Detail_ZhiDongLi_fragment fragment) {
        this.context = context;
        this.list = list;
        this.fragment = fragment;
        if (list.size() != 0) {
            for (int i = 0; i < list.size(); i++) {
                isClick.add(false);
            }
        } else {
            for (int i = 0; i < 6; i++) {
                isClick.add(false);
            }
        }
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
            viewHolder.lltitleName = view.findViewById(R.id.ll_tittle_name);
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
        if (isClick.get(i)) {
            viewHolder.lltitleName.setBackgroundColor(Color.parseColor("#ADFF2F"));//黄绿色
        }else {
            viewHolder.lltitleName.setBackgroundColor(Color.parseColor("#c8d7ea"));
        }

        viewHolder.lltitleName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //除了被点击的item置为true外，其他的全部置为false
                for (int i = 0; i < isClick.size(); i++) {
                    isClick.set(i, false);
                }
                isClick.set(i, true);
                notifyDataSetChanged();//刷新界面
                fragment.onItemClick(list.get(i));
            }
        });

        //删除点击事件
        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //删除数据库中的数据
                MyApp.getInstance().getmDaoSession().getZhiDongLiEntityDao().delete(list.get(i));
                list.remove(i);
                isClick.set(i,false);//没有这一步的话，删除之后下一个item会被置为true
                updateListView(list);//外面的list也会跟着更新
                fragment.showInitRes();
            }
        });
        return view;
    }

    class ViewHolder {
        LinearLayout lltitleName;
        TextView textView0;
        TextView textView1;
        TextView textView2;
        ImageButton button;
    }
}

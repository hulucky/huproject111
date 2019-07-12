package com.example.hu.huproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.hu.huproject.Bean.WeightBean;
import com.example.hu.huproject.Fragment.WeightFragment;
import com.example.hu.huproject.R;

import java.util.List;

public class WeightAdapter extends BaseAdapter {
    private Context context;
    private List<WeightBean> list;
    private WeightFragment weightFragment;
    public WeightAdapter(Context context, List<WeightBean> list, WeightFragment weightFragment) {
        this.context = context;
        this.list = list;
        this.weightFragment = weightFragment;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     * @param list
     */
    public void updateListView(List<WeightBean> list){
        this.list = list;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (view == null){
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.weight_item, null);
            viewHolder.textView0 = view.findViewById(R.id.itemNum);
            viewHolder.textView1 = view.findViewById(R.id.itemValue);
            view.setTag(viewHolder);
        }else {
            viewHolder =(ViewHolder) view.getTag();
        }
        viewHolder.textView0.setText(position+1+"");
        viewHolder.textView1.setText(list.get(position).getWeight()+"");
     return view;
    }

    class  ViewHolder{
        TextView textView0;
        TextView textView1;
    }
}

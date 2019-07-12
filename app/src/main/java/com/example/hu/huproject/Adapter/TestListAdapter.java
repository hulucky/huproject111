package com.example.hu.huproject.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hu.huproject.Application.MyApp;
import com.example.hu.huproject.Listener.OnRecyclerViewItemClickListener;
import com.example.hu.huproject.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//RecyclerView的Adapter
public class TestListAdapter extends RecyclerView.Adapter<TestListAdapter.ViewHolder> {

    private Context context;
    private OnRecyclerViewItemClickListener clickListener;
    //控件是否被点击,默认为false，如果被点击，改变值，控件根据值改变自身颜色
    private List<Boolean> isClick;
    private List<String> titleList;//标签列表
    private boolean isRunning;//是否正在运行

    public TestListAdapter(Context context, OnRecyclerViewItemClickListener clickListener, List<String> titleList) {
        this.context = context;
        this.clickListener = clickListener;
        this.titleList = titleList;
        isClick = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            isClick.add(false);
        }
        isClick.set(0, true);//默认第一项为选中项
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_testlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        //设置按钮内容
        holder.tv.setText(titleList.get(position));
        isRunning = MyApp.isRunning;
        //点击事件
        if (!isRunning) {
            //根据isClick是true还是false来决定控件的颜色，没点击之前默认全是false
            if (isClick.get(position)) {
                holder.tv.setBackgroundResource(R.mipmap.rightbar_btn_bg);
            } else {
                holder.tv.setBackgroundResource(0);
            }
            holder.tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //除了被点击的item置为true外，其他的全部置为false
                    for (int i = 0; i < isClick.size(); i++) {
                        isClick.set(i, false);
                    }
                    isClick.set(position, true);
                    notifyDataSetChanged();//刷新界面
                    clickListener.onRecyclerViewItemClicked(position, holder);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return titleList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv)
        TextView tv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

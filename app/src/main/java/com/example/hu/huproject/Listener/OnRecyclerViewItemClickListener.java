package com.example.hu.huproject.Listener;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by zdd on 2017/6/16 0016.
 */

public interface OnRecyclerViewItemClickListener {
    void onRecyclerViewItemClicked(int position, RecyclerView.ViewHolder viewHolder);

    void onRecyclerViewItemLongClicked(int position, RecyclerView.ViewHolder viewHolder);

    void onRecyclerViewItemClicked();


}

package com.example.hu.huproject.View;

import android.content.Context;
import android.widget.TextView;

import com.example.hu.huproject.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.DecimalFormat;

import butterknife.BindView;

public class MyMarkerView extends MarkerView {
    private TextView textView;
    private DecimalFormat format = new DecimalFormat("##0");

    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    public MyMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        textView = findViewById(R.id.tv);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        textView.setText(format.format(e.getY()) );//用butterKnife会报空指针
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight() - 10);
    }
}

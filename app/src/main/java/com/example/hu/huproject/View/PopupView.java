package com.example.hu.huproject.View;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;

import com.example.hu.huproject.R;


/**
 * @ClassName: PopupView
 * @Description: 弹窗
 * @author Lamelias
 */

public class PopupView extends View {

    private static final String TAG = PopupView.class.getSimpleName();

    private float startX; // 浮窗起始绘制位置X坐标
    private float startY; // 浮窗起始绘制位置Y坐标
    private String value; // 浮窗显示的点的值

    private Paint textPaint = new Paint();// 字体画笔
    private Paint paint = new Paint();

    public PopupView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        renderPaint();
        Resources rec = getResources();

        BitmapDrawable bitmapDrawable = (BitmapDrawable) rec
                .getDrawable(R.drawable.popup);
        Bitmap bitmap = bitmapDrawable.getBitmap();
        canvas.drawBitmap(bitmap, startX - bitmap.getWidth() / 2, startY, null);
        canvas.drawText(value, startX - bitmap.getWidth() / 4, startY + bitmap.getHeight() * 2 / 3, textPaint);
        super.onDraw(canvas);
    }

    /**
     * @Title renderPaint
     * @Description 渲染画笔
     * @return void
     */
    public void renderPaint() {
        paint.setColor(Color.parseColor("#97aca5"));
        paint.setStyle(Style.FILL);

        //textPaint.setColor(Color.parseColor("#40a9b7"));
        textPaint.setTextSize(15);
        textPaint.setStyle(Style.FILL);
    }
    public void setTextColor(int color) {
        textPaint.setColor(color);
    }
    public float getStartX() {
        return startX;
    }

    public void setStartX(float startX) {
        this.startX = startX;
    }

    public float getStartY() {
        return startY;
    }

    public void setStartY(float startY) {
        this.startY = startY;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
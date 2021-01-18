package com.example.weny.schedulemanagecopy.activity.floatwindow;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.example.weny.schedulemanagecopy.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class FloatingView extends View {

    public int height = 150;
    public int width = 150;
    private Paint paint;





    /**
     * 记录手指按下时在屏幕上的横坐标的值
     */
    private float xDownInScreen;

    /**
     * 记录手指按下时在屏幕上的纵坐标的值
     */
    private float yDownInScreen;
    /**
     * 状态栏的高度
     */
    private int statusBarHeight;

    private WindowManager.LayoutParams params;

    private WindowManager windowManager;


    public FloatingView(Context context) {
        super(context);
        paint = new Paint();
    }


    public void setParams(WindowManager.LayoutParams params) {
        this.params = params;
    }


    public void setWindowManager(WindowManager windowManager) {
        this.windowManager = windowManager;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(height, width);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画大圆
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setColor(getResources().getColor(R.color.color_view_bg));
        canvas.drawCircle(width / 2, width / 2, width / 2, paint);
        //画小圆圈
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);
        canvas.drawCircle(width / 2, width / 2, (float) (width * 1.0 / 4), paint);
    }




}

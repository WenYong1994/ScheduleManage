package com.example.weny.schedulemanagecopy.activity.floatwindow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Toast;

import java.util.ArrayList;

public class ViewManager {
    FloatingView floatBall;
    WindowManager windowManager;
    public static ViewManager manager;
    Context context;
    private WindowManager.LayoutParams floatBallParams;


    private ViewManager(Context context) {
        this.context = context;
    }

    public static ViewManager getInstance(Context context) {
        if (manager == null) {
            manager = new ViewManager(context);
        }
        return manager;
    }





    private float lastX,lastY,thisX,thisY,offsetX,offsetY,historyX,historyY;

    @SuppressLint("ClickableViewAccessibility")
    public void showFloatBall() {
        floatBall = new FloatingView(context);
        windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        if (floatBallParams == null) {
            floatBallParams = new WindowManager.LayoutParams();
            floatBallParams.width = floatBall.width;
            floatBallParams.height = floatBall.height;
            floatBallParams.gravity = Gravity.TOP | Gravity.LEFT;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                floatBallParams.type= WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            }else {
                floatBallParams.type=WindowManager.LayoutParams.TYPE_PHONE;
            }
            floatBallParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
            floatBallParams.format = PixelFormat.RGBA_8888;
            floatBall.setParams(floatBallParams);
            floatBall.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            lastX = motionEvent.getRawX();
                            lastY = motionEvent.getRawY();
//                            lastDownTime = motionEvent.getDownTime();

                            offsetX =historyX -  lastX  ;

                            offsetY =historyY- lastY  ;

                            break;
                        case MotionEvent.ACTION_MOVE:
                            thisX = motionEvent.getRawX();
                            thisY = motionEvent.getRawY();
                            floatBallParams.x = (int) ( motionEvent.getRawX()  + offsetX);
                            floatBallParams.y = (int) ( motionEvent.getRawY() + offsetY) /*- 25*/;

                            //刷新
                            windowManager.updateViewLayout(floatBall, floatBallParams);
                            break;
                        case MotionEvent.ACTION_UP:
                            int x = (int) motionEvent.getRawX();
                            int screenWidth = getScreenWidth();

//                            MyAnimation myAnimation;
                            if (x <screenWidth/2){
//                                myAnimation = new MyAnimation(windowManager,floatBall,floatBallParams,x,0);
                                floatBallParams.x = 0;
                            }else {
//                                myAnimation =new MyAnimation(windowManager,floatBall,floatBallParams,x,screenWidth-floatBall.getMeasuredWidth());
                                floatBallParams.x = screenWidth-floatBall.getMeasuredWidth();
                            }
                            historyX = floatBallParams.x;
                            historyY = floatBallParams.y;

//                            myAnimation.setDuration(200);
//                            myAnimation.start();
//                            floatBall.startAnimation(myAnimation);
                            windowManager.updateViewLayout(floatBall, floatBallParams);

                            break;
                    }


                    return false;
                }
            });
        }

        floatBall.setWindowManager(windowManager);

        historyX = floatBallParams.x;
        historyY = floatBallParams.y;

        windowManager.addView(floatBall, floatBallParams);

        floatBall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                EventBus.getDefault().post(MyAccessibilityService.BACK);
                Toast.makeText(context, "点击了悬浮球 执行后退操作", Toast.LENGTH_SHORT).show();
            }
        });

        floatBall.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
//                EventBus.getDefault().post(MyAccessibilityService.HOME);
                Toast.makeText(context, "长按了悬浮球  执行返回桌面", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

    }

    class MyAnimation extends Animation{
        WindowManager windowManager;
        View view;
        WindowManager.LayoutParams layoutParams;
        int startX,endX;
        int distance;

        public MyAnimation(WindowManager windowManager, View view,WindowManager.LayoutParams layoutParams, int startX, int endX) {
            this.windowManager = windowManager;
            this.view = view;
            this.layoutParams = layoutParams;
            this.startX = startX;
            this.endX = endX;
            distance = endX - startX;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            layoutParams.x = (int) (startX+distance*interpolatedTime);
            windowManager.updateViewLayout(view,layoutParams);
        }
    }



    public int getScreenWidth() {
        return windowManager.getDefaultDisplay().getWidth();
    }

}
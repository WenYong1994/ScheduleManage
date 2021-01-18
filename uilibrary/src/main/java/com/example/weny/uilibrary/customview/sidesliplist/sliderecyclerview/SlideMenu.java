package com.example.weny.uilibrary.customview.sidesliplist.sliderecyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Scroller;
import android.view.MotionEvent;
import android.view.View;
import com.jimmy.common.util.DensityUtils;

import java.util.Timer;
import java.util.TimerTask;


public class SlideMenu extends ViewGroup {

    private static final String TAG = SlideMenu.class.getName();

    private int downX,downY, moveX, moved;
    private Scroller scroller;
    private boolean haveShowRight = false;
    private boolean haveShowLeft=false;
    public static SlideMenu slideMenu;

    private SlideLeftMenu leftMenu;
    private SlideRightMenu rightMenu;
    private SlideContentMenu contentMenu;
    private boolean isFindtMenu=false;
    private int slideThreshold;//单位dp

//    private long downTime;
    Timer timer;
    TimerTask timerTask;



    private static final int LONGPRESSTIME= 300;//长按超过0.3秒，触发长按事件

    OnSlideMenuStatusChangerListener onSlideMenuStatusChangerListener;

    OnSlideMenuDraggingListener onSlideMenuDraggingListener;

    OnSlideMenuDraggEndListener onSlideMenuDraggEndListener;

    public SlideMenu(Context context) {
        super(context);
        init();
    }


    public SlideMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SlideMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    public void setOnSlideMenuDraggingListener(OnSlideMenuDraggingListener onSlideMenuDraggingListener) {
        this.onSlideMenuDraggingListener = onSlideMenuDraggingListener;
    }

    public void setOnSlideMenuDraggEndListener(OnSlideMenuDraggEndListener onSlideMenuDraggEndListener) {
        this.onSlideMenuDraggEndListener = onSlideMenuDraggEndListener;
    }

    public void setOnSlideMenuStatusChangerListener(OnSlideMenuStatusChangerListener onSlideMenuStatusChangerListener) {
        this.onSlideMenuStatusChangerListener = onSlideMenuStatusChangerListener;
    }


    public void setSlideThreshold(int slideThreshold) {
        this.slideThreshold = slideThreshold;
    }

    private void init() {
        Interpolator interpolator =new DecelerateInterpolator(0.4f);
        scroller = new Scroller(getContext(),interpolator);
        slideThreshold= DensityUtils.dipToSp(getContext(),50);//默认值50
        onSlideMenuDraggEndListener =new OnSlideMenuDraggEndListener() {
            @Override
            public void onLeftDragEnd(boolean haveShowLeftMenu,int draggX) {
                if(haveShowLeftMenu){//leftMenu显示中,如果滑动距离大于阈值就要隐藏menu
                    if(draggX>slideThreshold){
                        hideLeftMenu();
                    }else {
                        showLeftMenu();
                    }

                }else {//leftMenu隐藏中,如果滑动距离大于阈值就要隐显示enu
                    if(draggX>slideThreshold){
                        showLeftMenu();
                    }else {
                        hideLeftMenu();
                    }
                }
            }

            @Override
            public void onRightDragEnd(boolean haveShowRightMenu,int draggX) {
                if(haveShowRightMenu){//leftMenu显示中,如果滑动距离大于阈值就要隐藏menu
                    if(draggX>slideThreshold){
                        hideRightMenu();
                    }else {
                        showRightMenu();
                    }

                }else {//leftMenu隐藏中,如果滑动距离大于阈值就要隐显示enu
                    if(draggX>slideThreshold){
                        showRightMenu();
                    }else {
                        hideRightMenu();
                    }
                }
            }

        };
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (slideMenu != null) {
            slideMenu.closeMenus();
        }
    }

    //缓慢滚动到指定位置
    private void smoothScrollTo(int destX, int destY) {
        int scrollX = getScrollX();
        int delta = destX - scrollX;
        //1000ms内滑动destX，效果就是慢慢滑动
        scroller.startScroll(scrollX, 0, delta, 0, 200);
        invalidate();
    }

    public void closeMenus() {
        smoothScrollTo(0, 0);
        haveShowRight = false;
        haveShowLeft=false;
        slideMenu=null;
    }


    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            postInvalidate();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {



        return super.dispatchTouchEvent(ev);
    }

    private boolean mScrolling;

    //拦截触摸事件
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                downY = (int) event.getY();
                mScrolling = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(downX - event.getX()) >= ViewConfiguration.get(getContext()).getScaledTouchSlop()||Math.abs(downY - event.getY()) >=ViewConfiguration.get(getContext()).getScaledTouchSlop()) {
                    mScrolling = true;
                } else {
                    mScrolling = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                mScrolling = false;
                break;
        }
        return mScrolling;
    }




    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if (!scroller.isFinished()) {//让动画还在执行的时候不能监听触摸事件
            return false;
        }
        super.onTouchEvent(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                downTime=System.currentTimeMillis();
                downX = (int) ev.getRawX();
                downY = (int) ev.getRawY();
                startTimer();
                return true;
//                break;
            case MotionEvent.ACTION_MOVE:
                moveX = (int) ev.getRawX();
                moved =  moveX - downX;

                float offsetX = Math.abs(ev.getX()-downX);
                float offsetY = Math.abs(ev.getY()-downY);
                if(offsetX<ViewConfiguration.get(getContext()).getScaledTouchSlop()){
                    return false;
                }
                if(offsetX> ViewConfiguration.get(getContext()).getScaledTouchSlop()||offsetY>ViewConfiguration.get(getContext()).getScaledTouchSlop()){
//                    downTime =0;//移动了。不能触发单击事件，将这个按下事件设置为0
                    cancelTimer();
                }
                move();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                cancelTimer();
                onMoveUp(getScrollX());
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void move() {
        if (slideMenu == this && (haveShowRight||haveShowLeft)) {
            if(haveShowRight){
                int x = rightMenu.getMeasuredWidth()-moved;
                scrollTo(x, 0);
                if (getScrollX() <= 0) {
                    scrollTo(0, 0);
                } else if (getScrollX() >= rightMenu.getMeasuredWidth()) {
                    scrollTo(rightMenu.getMeasuredWidth(), 0);
                }
            }
            if(haveShowLeft){
                int x = -leftMenu.getMeasuredWidth()-moved;
//                        if(x>0){
//                            return true;
//                        }
                scrollTo(x, 0);
                if (getScrollX() <= -leftMenu.getMeasuredWidth()) {
                    scrollTo(-leftMenu.getMeasuredWidth(), 0);
                } else if (getScrollX() >= 0) {
                    scrollTo(0, 0);
                }
            }
        }else if (slideMenu != null&& slideMenu != this){
            slideMenu.closeMenus();
        }
        if(slideMenu!=this){
            moved= (int) (moved*1.2);
            if(moved<0&&rightMenu!=null){
                if(Math.abs(moved)> ViewConfiguration.get(getContext()).getScaledTouchSlop()){
                    scrollTo(-moved, 0);
                    if (getScrollX() <= 0) {
                        scrollTo(0, 0);
                    } else if (getScrollX() >= rightMenu.getMeasuredWidth()) {
                        scrollTo(rightMenu.getMeasuredWidth(), 0);
                    }
                }
            }else if(moved>0&&leftMenu!=null){
                if(Math.abs(moved)> ViewConfiguration.get(getContext()).getScaledTouchSlop()){
                    scrollTo(-moved, 0);
                    if (getScrollX() <= -leftMenu.getMeasuredWidth()) {
                        scrollTo(-leftMenu.getMeasuredWidth(), 0);
                    } else if (getScrollX() >= 0) {
                        scrollTo(0, 0);
                    }
                }
            }
        }


        if(getScrollX()<0){
            if(onSlideMenuDraggingListener!=null){
                if(haveShowLeft) {
                    if (moved < 0) {
                        onSlideMenuDraggingListener.onLeftDragging(true,Math.abs(getScrollX()));
                    }
                }else {
                    onSlideMenuDraggingListener.onLeftDragging(false,Math.abs(getScrollX()));
                }
            }
        }else {
            if(onSlideMenuDraggingListener!=null){
                if(haveShowRight) {
                    if (moved > 0) {
                        onSlideMenuDraggingListener.onRightDragging(true,Math.abs(getScrollX()));
                    }
                }else {
                    onSlideMenuDraggingListener.onRightDragging(false,Math.abs(getScrollX()));
                }
            }
        }
    }

    private void cancelTimer() {
//        if(timerTask!=null){
//            timerTask.cancel();
//        }
//        if(timer!=null){
//            timer.cancel();
//        }
    }

    private void startTimer() {
//        timer=new Timer();
//        timerTask=new TimerTask() {
//            @Override
//            public void run() {
//                post(new Runnable() {
//                    @Override
//                    public void run() {
//                        performLongClick();
//                    }
//                });
//
//            }
//        };
//        timer.schedule(timerTask,LONGPRESSTIME,1000*60*60*24);
    }

    private void onMoveUp(int moveX){
        if(moveX>0){
            if(haveShowRight){
                int x = rightMenu.getMeasuredWidth() - moveX;
                if(onSlideMenuDraggEndListener!=null){
                    onSlideMenuDraggEndListener.onRightDragEnd(haveShowRight,Math.abs(x));
                }
            }else {
                if(onSlideMenuDraggEndListener!=null){
                    onSlideMenuDraggEndListener.onRightDragEnd(haveShowRight,Math.abs(moveX));
                }
            }
        }else if(moveX<0){
            if(haveShowLeft){
                int x = leftMenu.getMeasuredWidth() + moveX;
                if(onSlideMenuDraggEndListener!=null){
                    onSlideMenuDraggEndListener.onLeftDragEnd(haveShowLeft,Math.abs(x));
                }
            }else {
                if(onSlideMenuDraggEndListener!=null){
                    onSlideMenuDraggEndListener.onLeftDragEnd(haveShowLeft,Math.abs(moveX));
                }
            }
        }
    }

    public void hideRightMenu() {
        if(onSlideMenuStatusChangerListener!=null&&haveShowRight){
            onSlideMenuStatusChangerListener.onRightMenuHide();
        }
        haveShowRight = false;
        closeMenus();

    }

    public void showRightMenu() {
        if(onSlideMenuStatusChangerListener!=null&&!haveShowRight){
            onSlideMenuStatusChangerListener.onRightMenuShow();
        }
        haveShowRight = true;
        slideMenu = this;
        smoothScrollTo(rightMenu.getMeasuredWidth(), 0);
    }

    public void hideLeftMenu() {
        if(onSlideMenuStatusChangerListener!=null&&haveShowLeft){
            onSlideMenuStatusChangerListener.onLeftMenuHide();
        }
        haveShowLeft = false;
        closeMenus();
    }

    public void showLeftMenu() {
        if(onSlideMenuStatusChangerListener!=null&&!haveShowLeft){//保证执行一次回调
            onSlideMenuStatusChangerListener.onLeftMenuShow();
        }
        haveShowLeft = true;
        slideMenu = this;
        smoothScrollTo(-leftMenu.getMeasuredWidth(), 0);
    }


    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height= MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b){
        if((rightMenu==null||leftMenu==null||contentMenu==null)&&!isFindtMenu){
            isFindtMenu=true;//只遍历一次
            int childCount = getChildCount();
            for(int i =0;i<childCount;i++){
                View childAt = getChildAt(i);
                if(childAt instanceof SlideRightMenu){
                    rightMenu = (SlideRightMenu) childAt;
                }else if(childAt instanceof SlideLeftMenu){
                    leftMenu = (SlideLeftMenu) childAt;
                }else if(childAt instanceof SlideContentMenu){
                    contentMenu = (SlideContentMenu) childAt;
                }
                if(rightMenu!=null&&leftMenu!=null&&contentMenu!=null){
                    break;
                }
            }
        }
        if(contentMenu!=null){
            contentMenu.layout(l,t,r,b);
        }
        if(rightMenu!=null){
            rightMenu.layout(r, t, r + rightMenu.getMeasuredWidth(), b);
        }
        if(leftMenu!=null){
            leftMenu.layout(l-leftMenu.getMeasuredWidth(),t,l,b);
        }

    }


    public interface OnSlideMenuDraggingListener{

        void  onLeftDragging(boolean isHide,int draggX);// isHide表示当前滑动是目的是否是隐藏menu 相对于父控件的拖动距离

        void  onRightDragging(boolean isHide,int draggX);

    }

    public interface OnSlideMenuDraggEndListener{

        void onLeftDragEnd(boolean haveShowLeftMenu,int moveX);//相对于上一次停留的地方移动的距离

        void onRightDragEnd(boolean haveShowRightMenu,int moveX);

    }

    public interface OnSlideMenuStatusChangerListener{

        void onLeftMenuShow();

        void onLeftMenuHide();

        void onRightMenuShow();

        void onRightMenuHide();

    }

}

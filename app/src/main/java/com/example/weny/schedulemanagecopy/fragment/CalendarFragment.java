package com.example.weny.schedulemanagecopy.fragment;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weny.schedulemanagecopy.R;
import com.example.weny.schedulemanagecopy.adapter.SildeExpandAdapter;
import com.example.weny.schedulemanagecopy.adapter.drag.ItemDragCallback;
import com.example.weny.schedulemanagecopy.customview.ChangeDateScheduleVew;
import com.example.weny.schedulemanagecopy.customview.ScheduleListView;
import com.example.weny.uilibrary.customview.expandablerecyclerview.models.ExpandableGroup;
import com.example.weny.uilibrary.customview.sidesliplist.layoutmanager.ScrollSpeedLinearLayoutManger;
import com.example.weny.uilibrary.customview.sidesliplist.sliderecyclerview.SlideMenuView;
import com.example.weny.uilibrary.customview.sidesliplist.suspendsilderecyclerview.SuspendSlideRecyclerGroup;
import com.jeek.calendar.widget.calendar.month.MonthView;
import com.jeek.calendar.widget.calendar.schedule.ScheduleLayout;
import com.jeek.calendar.widget.calendar.schedule.ScheduleRecyclerView;
import com.jimmy.common.base.app.BaseFragment;
import com.jimmy.common.util.DensityUtils;
import com.jimmy.common.util.NotchScreenUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class CalendarFragment extends BaseFragment {

    @BindView(R.id.schedule_list_view)
    ScheduleListView scheduleListView;
    @BindView(R.id.data_title)
    TextView dataTitle;
    @BindView(R.id.slSchedule)
    ScheduleLayout scheduleLayout;
    @BindView(R.id.change_data_fra)
    ChangeDateScheduleVew changeDateScheduleVew;

    SlideMenuView draggingView;
    boolean isInitViewMeasure=false;
    int offSet;

    Handler handler;

    float rvY;
    //开始需要显示 helperview的临界值，当拖动view进入


    public CalendarFragment() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_calendar;
    }

    @Override
    protected void onSetViewAfter() {
        scheduleListView.setNeedSuspendBar(false);
        scheduleLayout.setOnDataChangeListener((year, month, days) -> {
            dataTitle.setText(year+"年"+(month+1)+"月"+days+"日");
        });
        dataTitle.setText(scheduleLayout.getCurrentSelectYear()+"年"+(scheduleLayout.getCurrentSelectMonth()+1)+"月"+scheduleLayout.getCurrentSelectDay()+"日");
        offSet = NotchScreenUtil.getOffSet(getActivity());
        handler=new Handler(getActivity().getMainLooper());
        downData();
    }


    public void downData() {
        List<ExpandableGroup> groupList =new ArrayList<>();
        final List<String> list0 = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            list0.add("0-" + i);
        }

        final ExpandableGroup group = new ExpandableGroup("今天", list0, "今天");

        final List<String> list1= new ArrayList<>();

        for (int i = 0; i < 6 ;i++) {
            list1.add("1-" + i);
        }

        final ExpandableGroup group1 = new ExpandableGroup("已完成", list1, "已完成");

        groupList.add(group);
        groupList.add(group1);

        scheduleListView.addGroups(groupList);
        scheduleListView.toggleGroups(group);
        scheduleListView.toggleGroups(group1);
        scheduleListView.setEnableExpand(false);



    }


    public void dispatchTouchEvent(MotionEvent ev){
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                int[] location = new int[2];
                scheduleListView.getLocationInWindow(location);
                rvY=location[1];
                break;
            case MotionEvent.ACTION_MOVE:
//                判断是否有拖拽的itemView
                if(scheduleListView.isDraging()){
                    if(draggingView==null){
                        draggingView =scheduleListView.getDraggingView();
                        Object currentSchedu = draggingView.getTag();
                        changeDateScheduleVew.setContent(currentSchedu.toString());
                        handler.postDelayed(() -> {
                            if(draggingView!=null){
                                draggingView.centerGroup.setVisibility(View.INVISIBLE);//不显示
                                changeDateScheduleVew.show(draggingView.centerGroup);
                            }
                        },200);//这里需要等动画执行完了再替换view

                        initViewMeasure();//修正view的宽高
                    }
                    moveChangeDateSchedueView(ev);
                }

                if(scheduleListView.isDraging()){
                    moveChangeDateSchedueView(ev);
                }
                moveChangeDateSchedueView(ev);

                break;
            case MotionEvent.ACTION_UP:

                if(draggingView!=null){
                    draggingView.centerGroup.setVisibility(View.VISIBLE);
                    changeDateScheduleVew.hide();
                }
                MonthView currentMonthView = scheduleLayout.getMonthCalendar().getCurrentMonthView();
                if(currentMonthView.isDirected()){
                    draggingView.centerGroup.setVisibility(View.GONE);//如果有拖拽直接消失在显示出来了
                    Object currentSchedu = draggingView.getTag();
                    scheduleListView.setDirectintg(currentSchedu);
                    Toast.makeText(getActivity(), currentMonthView.getmDirectYear()+"年"+(currentMonthView.getmDirectMonth()+1)+"月"+currentMonthView.getmDirectDay()+"日", Toast.LENGTH_SHORT).show();
                    currentMonthView.resetDirect();
                }
                draggingView=null;

                break;
        }
    }

    private void moveChangeDateSchedueView(MotionEvent ev) {
        if(draggingView!=null){
            int[] location = new  int[2];
            draggingView.centerGroup.getLocationInWindow (location); //获取在当前窗口内的绝对坐标
//            draggingView.centerGroup.getLocationOnScreen (location);//获取在整个屏幕内的绝对坐标

            //这里的y是整个屏幕的值，但是由于适配沉浸式布局的原因导致changeDataFra的窗口要比屏幕低一个状态栏高度。所以需要修正高度

            int[] parLoc = new int[2];
            ViewParent parent = changeDateScheduleVew.getParent();
            if(parent instanceof View){
                ((View)parent).getLocationInWindow(parLoc);
            }

            int parY = parLoc[1];
            int x = location[0];
            int y = location[1];
            //修正正确的位置
            y = y-parY;

            if(y<rvY){
                float ratio = (Math.abs(y-rvY)/ DensityUtils.dipToSp(getActivity(),10));
                if(ratio>1){
                    ratio=1;
                }
                float offSetY = DensityUtils.dipToSp(getActivity(),40)*ratio;
                float tragetX = ev.getX()- DensityUtils.dipToSp(getActivity(),40);
                changeDateScheduleVew.setX(tragetX);
                changeDateScheduleVew.setY(y-offSetY);
            }else {
                changeDateScheduleVew.setX(x);
                changeDateScheduleVew.setY(y);
            }

            if(y<rvY){
                float tragetX = ev.getX()- DensityUtils.dipToSp(getActivity(),40);
                changeDateScheduleVew.chioceDateStatus(tragetX,0);
            }else {
                float tragetX = ev.getX()- DensityUtils.dipToSp(getActivity(),40);
                changeDateScheduleVew.dragingStatus(tragetX,0);
            }
            scheduleLayout.getMonthCalendar().getCurrentMonthView().doDirectingAction(changeDateScheduleVew.getAnchorX(),changeDateScheduleVew.getAnchorY());

        }
    }

    private void initViewMeasure() {
        if(!isInitViewMeasure){
            isInitViewMeasure=true;
            //开始修正changeDateScheduleVew的宽高
            changeDateScheduleVew.amendmentView(changeDateScheduleVew.getMeasuredWidth()*SlideMenuView.HEIGHT_SCAL_FACTOR,changeDateScheduleVew.getMeasuredHeight()*SlideMenuView.HEIGHT_SCAL_FACTOR);
        }
    }



}

package com.example.weny.schedulemanagecopy.activity.sidesliplist;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.weny.schedulemanagecopy.R;
import com.example.weny.schedulemanagecopy.customview.ChangeDateScheduleVew;
import com.example.weny.schedulemanagecopy.customview.ScheduleListView;
import com.example.weny.uilibrary.customview.expandablerecyclerview.models.ExpandableGroup;
import com.example.weny.uilibrary.customview.sidesliplist.sliderecyclerview.SlideMenu;
import com.example.weny.uilibrary.customview.sidesliplist.sliderecyclerview.SlideMenuView;
import com.jimmy.common.base.app.BaseActivity;
import com.jimmy.common.util.DensityUtils;
import com.jimmy.common.util.LogUtils;
import com.jimmy.common.util.NotchScreenUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SideslipListActivity extends BaseActivity {
    @BindView(R.id.schedule_list_view)
    ScheduleListView scheduleListView;
    @BindView(R.id.m_root_view)
    RelativeLayout mRootView;




    //记录recyclerView的位置，用来判断是否拖动出了recyclerView的位置



    @Override
    protected void initPresenter() {

    }

    @Override
    protected int getRootViewId() {
        return R.id.m_root_view;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sideslip_list;
    }

    @Override
    protected void onSetContentViewAfter() {
        downData();
        initDragView();
//        scheduleListView.
    }




    private void initDragView() {


    }


    public void downData() {
        List<ExpandableGroup> groupList = new ArrayList<>();
        final List<String> list0 = new ArrayList<>();
        List<String> list1 = new ArrayList<>();
        List<String> list2 = new ArrayList<>();
        List<String> list3 = new ArrayList<>();
        List<String> list4 = new ArrayList<>();
        List<String> list5 = new ArrayList<>();
        List<String> list6 = new ArrayList<>();
        List<String> list7 = new ArrayList<>();
        List<String> list8 = new ArrayList<>();
        List<String> list9 = new ArrayList<>();
        List<String> list10 = new ArrayList<>();
        List<String> list11 = new ArrayList<>();
        List<String> list12 = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            list0.add("0-" + i);
        }
        for (int i = 0; i < 5; i++) {
            list1.add("1-" + i);
        }
        for (int i = 0; i < 10; i++) {
            list2.add("2-" + i);
        }
        for (int i = 0; i < 5; i++) {
            list3.add("3-" + i);
        }
        for (int i = 0; i < 5; i++) {
            list4.add("4-" + i);
        }
        for (int i = 0; i < 5; i++) {
            list5.add("5-" + i);
        }
        for (int i = 0; i < 5; i++) {
            list6.add("6-" + i);
        }
        for (int i = 0; i < 5; i++) {
            list7.add("7-" + i);
        }
        for (int i = 0; i < 5; i++) {
            list8.add("8-" + i);
        }
        for (int i = 0; i < 5; i++) {
            list9.add("9-" + i);
        }
        for (int i = 0; i < 5; i++) {
            list10.add("10-" + i);
        }
        for (int i = 0; i < 5; i++) {
            list11.add("11-" + i);
        }
        for (int i = 0; i < 7; i++) {
            list12.add("12-" + i);
        }

        final ExpandableGroup group = new ExpandableGroup("已过期", list0, "已过期");
        ExpandableGroup group1 = new ExpandableGroup("今天", list1, "今天");
        ExpandableGroup group2 = new ExpandableGroup("后2天", list2, "后2天");
        ExpandableGroup group3 = new ExpandableGroup("后3天", list3, "后3天");
        ExpandableGroup group4 = new ExpandableGroup("后4天", list4, "后4天");
        ExpandableGroup group5 = new ExpandableGroup("后5天", list5, "后5天");
        ExpandableGroup group6 = new ExpandableGroup("后6天", list6, "后6天");
        ExpandableGroup group7 = new ExpandableGroup("后7天", list7, "后7天");
        ExpandableGroup group8 = new ExpandableGroup("后8天", list8, "后8天");
        ExpandableGroup group9 = new ExpandableGroup("后9天", list9, "后9天");
        ExpandableGroup group10 = new ExpandableGroup("后10天", list10, "后10天");
        ExpandableGroup group11 = new ExpandableGroup("后11天", list11, "后11天");
        ExpandableGroup group12 = new ExpandableGroup("后12天", list12, "后12天");

        groupList.add(group);
        groupList.add(group1);
        groupList.add(group2);
        groupList.add(group3);
        groupList.add(group4);
        groupList.add(group5);
        groupList.add(group6);
        groupList.add(group7);
        groupList.add(group8);
        groupList.add(group9);
        groupList.add(group10);
        groupList.add(group11);
        groupList.add(group12);

        scheduleListView.addGroups(groupList);
        scheduleListView.setNeedSuspendBar(true);
    }


}

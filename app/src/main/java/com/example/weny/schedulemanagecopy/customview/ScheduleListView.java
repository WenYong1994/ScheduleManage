package com.example.weny.schedulemanagecopy.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weny.schedulemanagecopy.R;
import com.example.weny.schedulemanagecopy.adapter.SildeExpandAdapter;
import com.example.weny.schedulemanagecopy.adapter.drag.ItemDragCallback;
import com.example.weny.uilibrary.customview.expandablerecyclerview.models.ExpandableGroup;
import com.example.weny.uilibrary.customview.sidesliplist.layoutmanager.ScrollSpeedLinearLayoutManger;
import com.example.weny.uilibrary.customview.sidesliplist.sliderecyclerview.SlideMenuView;
import com.example.weny.uilibrary.customview.sidesliplist.sliderecyclerview.SlideTouchRecyclerView;
import com.example.weny.uilibrary.customview.sidesliplist.suspendsilderecyclerview.SuspendSlideRecyclerGroup;
import com.jimmy.common.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScheduleListView extends FrameLayout {

    View rootView;

    @BindView(R.id.recycler_view)
    SlideTouchRecyclerView recyclerView;
    @BindView(R.id.group_name)
    TextView groupName;
    @BindView(R.id.child_count)
    TextView childCount;
    @BindView(R.id.icon)
    ImageView icon;
    @BindView(R.id.group)
    FrameLayout mSuspensionBar;
    @BindView(R.id.suspend_rv_group)
    SuspendSlideRecyclerGroup suspendSlideRecyclerGroup;


    List<ExpandableGroup> groupList;
    SildeExpandAdapter sildeExpandAdapter;
    LinearLayoutManager scrollLinearLayoutManager;

    ItemDragCallback callback;

    public ScheduleListView(@NonNull Context context) {
        super(context);
        init();
    }

    public ScheduleListView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScheduleListView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //添加布局
        rootView = LayoutInflater.from(getContext()).inflate(R.layout.schedule_list_layout, this, false);
        addView(rootView);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        setRecyclerView();
    }

    private void setRecyclerView() {
        // 取消recyclerView itemView 小时的淡入淡出动画
        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        if (animator instanceof DefaultItemAnimator) {
            ((DefaultItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        groupList = new ArrayList<>();
        scrollLinearLayoutManager = new ScrollSpeedLinearLayoutManger(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(scrollLinearLayoutManager);
        sildeExpandAdapter = new SildeExpandAdapter(getContext(), groupList, recyclerView);
        recyclerView.setAdapter(sildeExpandAdapter);



        callback = new ItemDragCallback(sildeExpandAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);

        suspendSlideRecyclerGroup.setSuspendBarListener(new SuspendSlideRecyclerGroup.SuspendBarListener() {
            @Override
            public boolean isGroup(int position) {
                return sildeExpandAdapter.isGroup(position);
            }

            @Override
            public void updataView(View mSuspensionBar, int position) {
                ExpandableGroup expandableGroup = sildeExpandAdapter.getGroupByPosition(position);
                updateSuspensionBar(expandableGroup.getTitle(),expandableGroup.getItems().size()+"");
            }

            @Override
            public void onSuspendionBarOnClickListener(View mSuspendionBar, int currentPosition) {
                sildeExpandAdapter.toggleGroup(sildeExpandAdapter.getGroupByPosition(currentPosition));
            }
        });
        sildeExpandAdapter.setOnGroupRangChangeListener(() -> suspendSlideRecyclerGroup.onDataPositionChange());

        sildeExpandAdapter.setOnChildClickListener(view -> {
            Object o =  view.getTag();
            removeChild(o);
        });

    }


    public void addGroups(List<ExpandableGroup> data){
        groupList.addAll(data);
        sildeExpandAdapter.notifyDataSetChanged();
        //更新悬浮头
        suspendSlideRecyclerGroup.synSuspendBar();
    }

    public void removeChild(Object o){

        int position = sildeExpandAdapter.getPositionByChild(o);
        ExpandableGroup group = sildeExpandAdapter.getGroupByPosition(position);
        Object remove = group.removeItem(o);
        if(position>0&&remove!=null){
            sildeExpandAdapter.notifyItemRemoved(position);
            if(group.getItems().size()==0){
                sildeExpandAdapter.removeGroup(group);
            }
        }

    }


    public void setDirectintg(Object o){
        sildeExpandAdapter.setDirecting(o);
    }


    public void clearGroups(){
        groupList.clear();
        sildeExpandAdapter.notifyDataSetChanged();
    }

    public void toggleGroups(ExpandableGroup group){
        sildeExpandAdapter.toggleGroup(group);
    }

    public void setEnableExpand(boolean enable){
        sildeExpandAdapter.setExpandable(enable);
    }

    private void updateSuspensionBar(String title,String count) {
        groupName.setText(title);
        childCount.setText(count);
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public boolean isScrollTop() {
        return recyclerView.isScrollTop();
    }

    public void setNeedSuspendBar(boolean isNeed){
        suspendSlideRecyclerGroup.setNeedSuspendBar(isNeed);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        callback.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 返回当前是否正在拖拽
     * */
    public boolean isDraging(){
        return callback.isDraging();
    }

    /**
     * 获取正在拖拽的view
     * */
    public SlideMenuView getDraggingView(){
        return callback.getDragingView();
    }

}

package com.example.weny.schedulemanagecopy.adapter.drag;

import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.weny.schedulemanagecopy.adapter.SildeExpandAdapter;
import com.example.weny.schedulemanagecopy.app.App;
import com.example.weny.uilibrary.customview.expandablerecyclerview.viewholders.AllViewHolder;
import com.example.weny.uilibrary.customview.expandablerecyclerview.viewholders.ChildViewHolder;
import com.example.weny.uilibrary.customview.sidesliplist.sliderecyclerview.SlideMenuView;
import com.jimmy.common.util.DensityUtils;

import static android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_DRAG;
import static android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_IDLE;

public class ItemDragCallback extends ItemTouchHelper.Callback {

    private RecyclerView.ViewHolder mViewHolder;
    private int startPosition;
    private int endPosition=-1;
    private boolean isChangeStatus2Drag=false;

    private OnItemDragListener onItemDragListener;

    private boolean isDraging=false;
    private int itemStatus= SildeExpandAdapter.NOMAL_STATUS;


    private float fingerY;
    private float startY=-1;

    public ItemDragCallback(OnItemDragListener onItemDragListener) {
        this.onItemDragListener = onItemDragListener;
    }


    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int position = viewHolder.getLayoutPosition();
        //第一个item不用交换
        if (position == 0) {
            return 0;
        }

        if(viewHolder instanceof AllViewHolder){
            AllViewHolder allViewHolder = (AllViewHolder) viewHolder;
            if(allViewHolder.getGroupViewHolder().itemView.getVisibility()==View.VISIBLE){
                return 0;
            }
        }

        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = 0;
        return makeMovementFlags(dragFlags, swipeFlags);
    }


    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int fromPosition = viewHolder.getAdapterPosition();   //拖动的position
        int toPosition = target.getAdapterPosition();     //释放的position
        int position = viewHolder.getLayoutPosition();
        //第一个item不用交换
        if (position == 0) {
            return false;
        }
        if(toPosition== 0){
            return false;
        }
//
//        if(!isChangeStatus2Drag){
//            isChangeStatus2Drag=true;
//            changeStatus2Drat(viewHolder);
//        }

        endPosition = toPosition;
        if(onItemDragListener!=null){
            onItemDragListener.onItemMove(fromPosition,toPosition);
        }

        return true;
    }


    private void changeStatus2Drat(RecyclerView.ViewHolder viewHolder) {
        if (onItemDragListener!=null){
            onItemDragListener.onItemStatusDrag(viewHolder);
        }
    }
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }

    public void onTouchEvent(MotionEvent event){
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                fingerY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                fingerY = event.getY();
                float abs = Math.abs(fingerY - startY);
                if(startY!=-1&&abs> DensityUtils.dipToSp(App.getInstance(),10)){
                    if(!isChangeStatus2Drag){
                        itemStatus = SildeExpandAdapter.DRAG_STATUS;//进入拖动状态
                        isChangeStatus2Drag=true;
                        changeStatus2Drat(mViewHolder);
                    }
                }

                break;
        }
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        switch (actionState){
            case ACTION_STATE_DRAG:
                itemStatus = SildeExpandAdapter.MUTILPLE_STATUS;//进入复选状态
                startY = fingerY;
                //长按时调用
                mViewHolder = viewHolder;
                startPosition = viewHolder.getAdapterPosition();//记录开始的位置



                break;
            case ACTION_STATE_IDLE:
                if(mViewHolder != null&&itemStatus == SildeExpandAdapter.DRAG_STATUS){//让拖动状态回归初始状态
                    if(onItemDragListener!=null){
                        if(mViewHolder instanceof AllViewHolder){
                            AllViewHolder allViewHolder = (AllViewHolder) mViewHolder;
                            ChildViewHolder childViewHolder = allViewHolder.getChildViewHolder();
                            if(childViewHolder!=null&&childViewHolder.itemView.getVisibility()==View.VISIBLE){
                                itemStatus = SildeExpandAdapter.NOMAL_STATUS;
                                onItemDragListener.onItemMoved(childViewHolder,startPosition,endPosition);
                            }
                        }
                    }



                }
                mViewHolder=null;
                endPosition=-1;
                isChangeStatus2Drag=false;
                startY=-1;
                break;
        }
    }


    public interface OnItemDragListener{

        void onItemStatusDrag(RecyclerView.ViewHolder viewHolder);

        void onItemMove(int fromPosition, int toPosition);

        void onItemMoved(RecyclerView.ViewHolder viewHolder, int startPosition, int endPosition);


    }




    public boolean isDraging() {
        return itemStatus == SildeExpandAdapter.DRAG_STATUS;
    }

    public SlideMenuView getDragingView(){
        if(mViewHolder != null){
            if(mViewHolder instanceof AllViewHolder){
                AllViewHolder allViewHolder = (AllViewHolder) mViewHolder;
                ChildViewHolder childViewHolder = allViewHolder.getChildViewHolder();
                if(childViewHolder!=null&&childViewHolder.itemView.getVisibility()==View.VISIBLE&&childViewHolder instanceof SildeExpandAdapter.ItemChildViewHolder){
                    return ((SildeExpandAdapter.ItemChildViewHolder)childViewHolder).slideMenuView;
                }
            }
        }
        return null;
    }
}

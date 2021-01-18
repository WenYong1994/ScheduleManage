package com.example.weny.schedulemanagecopy.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.weny.schedulemanagecopy.R;
import com.example.weny.schedulemanagecopy.adapter.drag.ItemDragCallback;
import com.example.weny.uilibrary.customview.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.example.weny.uilibrary.customview.expandablerecyclerview.models.ExpandableGroup;
import com.example.weny.uilibrary.customview.expandablerecyclerview.models.ExpandableListPosition;
import com.example.weny.uilibrary.customview.expandablerecyclerview.viewholders.AllViewHolder;
import com.example.weny.uilibrary.customview.expandablerecyclerview.viewholders.ChildViewHolder;
import com.example.weny.uilibrary.customview.expandablerecyclerview.viewholders.GroupViewHolder;
import com.example.weny.uilibrary.customview.sidesliplist.sliderecyclerview.SlideMenuView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SildeExpandAdapter extends ExpandableRecyclerViewAdapter<SildeExpandAdapter.DataGroupViewHolder, SildeExpandAdapter.ItemChildViewHolder>
                                implements ItemDragCallback.OnItemDragListener{
    public static final int NOMAL_STATUS=0;
    public static final int MUTILPLE_STATUS=1;
    public static final int DRAG_STATUS=2;

    RecyclerView recyclerView;
    ArrayList<ItemChildViewHolder> holderList;
    Context context;
    SlideMenuView.OnSildeMenuStatusChangeListener onSildeMenuStatusChangeListener;
    View.OnClickListener onChildClickListener;


    private Handler handler;

    private int itemStstus=NOMAL_STATUS;
    private ItemChildViewHolder dragViewHolder;

    private Object directing;

    public SildeExpandAdapter(Context context,List<? extends ExpandableGroup> groups,RecyclerView recyclerView) {
        super(context,groups);
        this.context = context;
        holderList= new ArrayList<>();
        this.recyclerView = recyclerView;
        handler = new Handler(context.getMainLooper());
    }

    @Override
    public DataGroupViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.slide_item_group_layout,parent,false);
        DataGroupViewHolder dataGroupViewHolder = new DataGroupViewHolder(view);
        return dataGroupViewHolder;
    }

    @Override
    public ItemChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.silde_item_layou,parent,false);
        ItemChildViewHolder itemChildViewHolder = new ItemChildViewHolder(view);
        holderList.add(itemChildViewHolder);
        return itemChildViewHolder;
    }

    @Override
    public void onBindChildViewHolder(AllViewHolder allViewHolder, final ItemChildViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        Object o = group.getItems().get(childIndex);
        switch (itemStstus){
            case NOMAL_STATUS:
                holder.slideMenuView.itemImage.setImageResource(R.mipmap.schedule_comple_flase);
                holder.slideMenuView.centerGroup.setBackgroundResource(R.color.colorWhite);
                break;
            case MUTILPLE_STATUS:
                holder.slideMenuView.itemImage.setImageResource(R.mipmap.selector_flase_icon);
                holder.slideMenuView.centerGroup.setBackgroundResource(R.color.colorWhite);
                break;
            case DRAG_STATUS:
                if(holder == dragViewHolder){
                    holder.slideMenuView.centerGroup.setBackgroundResource(R.color.color_schedule_item_is_draging_bg);
                }else {
                    holder.slideMenuView.centerGroup.setBackgroundResource(R.color.color_schedule_item_drag_bg);
                }
                holder.slideMenuView.itemImage.setImageResource(R.mipmap.schedule_comple_flase);
                break;
        }
        holder.slideMenuView.recover();
        holder.itemView.setVisibility(View.VISIBLE);
        holder.slideMenuView.centerGroup.setVisibility(View.VISIBLE);
        holder.contentView.setText(o.toString());
        holder.slideMenuView.setTag(o);
        holder.itemView.setTag(R.id.content_tv,o);
        holder.slideMenuView.setOnSildeMenuStatusChangeListener(onSildeMenuStatusChangeListener);
        holder.slideMenuView.slideContentMenu.setOnLongClickListener(view -> {
            setItemStatusMuilple();
            return true;
        });
        holder.slideMenuView.imgClickView.setTag(o);
        holder.slideMenuView.imgClickView.setOnClickListener(onChildClickListener);
    }




    @Override
    public void onBindGroupViewHolder(final DataGroupViewHolder holder, int flatPosition, ExpandableGroup group) {
        Object data = group.getData();
        holder.dataView.setText(data.toString());
        holder.countView.setText(group.getItems().size()+"");
        holder.itemView.setOnLongClickListener(view -> true);
    }


    public void setOnSildeMenuStatusChangeListener(SlideMenuView.OnSildeMenuStatusChangeListener onSildeMenuStatusChangeListener) {
        this.onSildeMenuStatusChangeListener = onSildeMenuStatusChangeListener;
    }




    @Override
    public int getItemViewType(int position){
        return 0;//都返回0才能让TouchHelper可以拖动
    }



    public void setStatusDrag(){
        itemStstus=DRAG_STATUS;
        onItemViewStatusChange();
    }

    public void setStatusNomal(){
        itemStstus=NOMAL_STATUS;
        onItemViewStatusChange();
    }

    private void setItemStatusMuilple(){
        itemStstus=MUTILPLE_STATUS;
        onItemViewStatusChange();
    }

    private void onItemViewStatusChange(){
        for(ItemChildViewHolder holder:holderList){
            switch (itemStstus){
                case NOMAL_STATUS:
                    holder.slideMenuView.itemImage.setImageResource(R.mipmap.schedule_comple_flase);
                    holder.slideMenuView.centerGroup.setBackgroundResource(R.color.colorWhite);
                    break;
                case MUTILPLE_STATUS:
                    holder.slideMenuView.itemImage.setImageResource(R.mipmap.selector_flase_icon);
                    holder.slideMenuView.centerGroup.setBackgroundResource(R.color.colorWhite);
                    break;
                case DRAG_STATUS:
                    if(holder == dragViewHolder){
                        holder.slideMenuView.centerGroup.setBackgroundResource(R.color.color_schedule_item_is_draging_bg);
                    }else {
                        holder.slideMenuView.centerGroup.setBackgroundResource(R.color.color_schedule_item_drag_bg);
                        holder.slideMenuView.recover();
                    }
                    holder.slideMenuView.itemImage.setImageResource(R.mipmap.schedule_comple_flase);
                    break;

            }
        }
    }


    public void setOnChildClickListener(View.OnClickListener onChildClickListener) {
        this.onChildClickListener = onChildClickListener;
    }

    /**
     * 对拖拽的元素进行排序
     * @param fromPosition
     * @param toPosition
     */
    public void itemMove(int fromPosition, int toPosition) {
        if(toPosition==-1||toPosition==0){//等于-1 就是没有移动
            return;
        }
        notifyItemMoved(fromPosition, toPosition);
    }


    /**
     * 拖拽完成
     *
     * */
    public void itemMoveEnd(RecyclerView.ViewHolder viewHolder, int startPosition, int endPosition){

        if(startPosition==endPosition){
            return;
        }
        if(endPosition==-1){//等于-1 就是没有移动
            return;
        }
        if(endPosition==0){
            restoration(startPosition,0);
            return;
        }

//        if(getGroups().size()>1){
//            ExpandableGroup oneGroup = getGroupByPosition(0);
//            int size = oneGroup.getItems().size();
//            if(!isGroupExpanded(oneGroup)){
//                size=0;
//            }
//
//
//            if((startPosition>0&&startPosition<size+1)&&endPosition<=size+1){//起始位置在 已过期内,并且终止位置在 已过期内 不交换
//                restoration(startPosition, endPosition);
//                return;
//            }
//            if(startPosition>=size+1&&endPosition<=size+1){//终止位置在已过期内不交换
//                restoration(startPosition, endPosition);
//                return;
//            }
//        }


        ExpandableGroup startGroup = getGroupByPosition(startPosition);
        ExpandableGroup endGroup = getGroupByPosition(endPosition);
        handSwip(startPosition, endPosition, startGroup, endGroup);

    }


    private void handSwip(int startPosition, int endPosition, ExpandableGroup startGroup, ExpandableGroup endGroup) {
        int startType = getOwnItemViewType(startPosition);
        int endType = getOwnItemViewType(endPosition);

//        LogUtils.e("itemMove","fromType:"+startType+",fromPosition:"+startPosition+",toType:"+endType+",toPosition:"+endPosition);

        if(startGroup == endGroup&&endType== ExpandableListPosition.CHILD){//同一个group直接交换就ok了
            sameGroupSwap(getChildIndexInGroupByPosition(startPosition), getChildIndexInGroupByPosition(endPosition),startGroup);
            return;
        }

        if(startPosition<endPosition){//从上往下拖拽
            if(startGroup!=endGroup){//相同group在上面已经处理过了

                //todo 看情况放在前面展开 或者是放在后面
//                int toIndex = getChildIndexInGroupByPosition(endPosition)+1;//如果endPosition是group，就返回0，也是正确的
//                if(endGroup.getLastVisibleCount()>0&&endType==ExpandableListPosition.GROUP){//如果是和group交换并且group有lastVisible就放在lastVisible和被隐藏的item之间
//                    toIndex = endGroup.getItemCount();
//                }

                //todo 看情况放在前面展开 或者是放在后面
                int toIndex = getChildIndexInGroupByPosition(endPosition)+1;//如果endPosition是group，就返回0，也是正确的
                if((!endGroup.isExpand())&&endType==ExpandableListPosition.GROUP){
                    toIndex = endGroup.getItemCount();
                }

                pullDiffGroupSwap(getChildIndexInGroupByPosition(startPosition),toIndex,startPosition,endPosition,startGroup,endGroup);
                return;
            }
        }else {//从下往上拖拽
            ExpandableGroup targetGroup;
            if(endType==ExpandableListPosition.GROUP){//如果和group交换，那么目标group是当前group的上一个
                targetGroup =  getGroupByPosition(endPosition-1);
                pushDiffGroupSwap(getChildIndexInGroupByPosition(startPosition),targetGroup.getItems().size(),
                        startPosition,endPosition,startGroup,targetGroup);
                return;
            }else {//和child交换，目标group就是被交换child的group
                targetGroup = endGroup;
                pushDiffGroupSwap(getChildIndexInGroupByPosition(startPosition),getChildIndexInGroupByPosition(endPosition),
                        startPosition,endPosition,startGroup,targetGroup);
                return;
            }
        }
//        restoration(startPosition, endPosition);
    }

    //不同group从下往上拖拽，不同group的交换
    private void pushDiffGroupSwap(int fromIndex,int toIndex,int fromPosition,int toPosition,ExpandableGroup fromGroup,ExpandableGroup targetGroup){
        Object remove = fromGroup.removeItem(fromIndex);
        if(targetGroup.isExpand()){
            targetGroup.addItem(toIndex,remove);
        }else {
            targetGroup.addListVisibleItem(toIndex,remove);
        }
        updataTittle(fromIndex,toIndex,fromPosition, toPosition, fromGroup, targetGroup);
//        notifyItemChanged(fromPosition);
//        notifyItemChanged(toPosition);
//        notifyItemRangeChanged(Math.min(fromPosition,toPosition),Math.max(fromPosition,toPosition));


        //todo 是否需要移除 子项为0的
        if(fromGroup.getItems().size()==0){
            removeGroup(fromGroup);
        }
    }

    //更新group的title
    private void pullDiffGroupSwap(int fromIndex,int toIndex,int fromPosition,int toPosition,ExpandableGroup fromGroup,ExpandableGroup toGroup) {

        Object remove = fromGroup.removeItem(fromIndex);
        //todo 看情况放在前面展开 或者是放在后面
//        if(toGroup.getLastVisibleCount()==0){
//            toGroup.addItem(toIndex,remove);
//        }else {
//            toGroup.addListVisibleItem(toIndex,remove);
//        }

        //todo 放在最后面  不展开
        toGroup.addListVisibleItem(toIndex,remove);
        //更新title
        updataTittle(fromIndex,toIndex,fromPosition,toPosition,fromGroup,toGroup);
//        notifyItemChanged(fromPosition);
//        notifyItemChanged(toPosition);
//        notifyItemRangeChanged(Math.min(fromPosition,toPosition),Math.max(fromPosition,toPosition));


        if(fromGroup.getItems().size()==0){
            removeGroup(fromGroup);
        }



//        //todo 可以控制交换需要打开 被关闭分类
//        if(toGroup.getLastVisibleCount()==0){
//            if(!isGroupExpanded(toGroup)){
//                toggleGroup(toGroup);
//            }
//        }
    }

    private void updataTittle(int fromIndex,int toIndex,int fromPosition, int toPosition, ExpandableGroup fromGroup, ExpandableGroup targetGroup) {
        int groupPositionFrom = toPosition-(toIndex-targetGroup.getItemCount())-1;
        notifyItemChanged(groupPositionFrom);
        int groupPositionTo = fromPosition-(fromIndex-fromGroup.getItemCount())-1;
        notifyItemChanged(groupPositionTo);
        //group顺序变化了
        onGroupRangeChange();
    }

    //相同group下面交换
    private void sameGroupSwap(int startIndex, int endIndex, ExpandableGroup startGroup) {
        if(startIndex<endIndex){
            for (int i = startIndex; i < endIndex; i++) {
                Collections.swap(startGroup.getItems(), i, i + 1);
            }
//            notifyItemRangeChanged(startIndex, endIndex-startIndex+1);
        }else {
            for (int i = startIndex; i > endIndex; i--) {
                Collections.swap(startGroup.getItems(), i, i - 1);
            }
//            notifyItemRangeChanged(endIndex, startIndex-endIndex+1);
        }
//        notifyItemRemoved(startIndex);
//        notifyItemInserted(endIndex);
    }

    //复位
    private void restoration(int startPosition, int endPosition) {//复位
        if(endPosition==0){
            notifyItemMoved(endPosition,startPosition);
            notifyItemRangeChanged(0,startPosition+1);//交换回来
            return;
        }
        notifyItemMoved(startPosition,endPosition);
//        notifyItemChanged(startPosition);
//        notifyItemChanged(endPosition);
        if(startPosition<endPosition){
            notifyItemRangeChanged(startPosition,Math.abs(startPosition-endPosition)+1);//交换回来
        }else {
            notifyItemRangeChanged(endPosition,Math.abs(startPosition-endPosition)+1);//交换回来
        }
    }

    @Override
    public void onItemStatusDrag(RecyclerView.ViewHolder viewHolder) {
        if(viewHolder instanceof AllViewHolder){
            AllViewHolder allViewHolder = (AllViewHolder) viewHolder;
            if(allViewHolder.getChildViewHolder() instanceof SildeExpandAdapter.ItemChildViewHolder){
                SildeExpandAdapter.ItemChildViewHolder itemChildViewHolder = (SildeExpandAdapter.ItemChildViewHolder) allViewHolder.getChildViewHolder();
                dragViewHolder = itemChildViewHolder;
//                //这里执行收缩动画
                itemChildViewHolder.slideMenuView.scal();
                setStatusDrag();
            }
        }
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        itemMove(fromPosition,toPosition);
    }

    @Override
    public void onItemMoved(RecyclerView.ViewHolder viewHolder, int startPosition, int endPosition) {
        if(dragViewHolder!=null){
            dragViewHolder.slideMenuView.zoom();
        }
        setStatusNomal();
        itemMoveEnd(viewHolder,startPosition,endPosition);
        if(directing==dragViewHolder.slideMenuView.getTag()){
            dragViewHolder.itemView.setVisibility(View.GONE);
            int positionByChild = getPositionByChild(directing);
            ExpandableGroup groupByPosition = getGroupByPosition(positionByChild);
            groupByPosition.getItems().remove(directing);
            if(endPosition==-1){//没有移动
                notifyItemRemoved(startPosition);
            }else {
                notifyItemRemoved(endPosition);
            }
            directing=null;
            return;
        }
    }

    public int getItemStstus() {
        return itemStstus;
    }

    public void setDirecting(Object o) {
        directing = o;
    }

    public class DataGroupViewHolder extends GroupViewHolder{
        TextView dataView;
        TextView countView;
        public DataGroupViewHolder(View itemView){
            super(itemView);
            dataView = itemView.findViewById(R.id.group_name);
            countView=itemView.findViewById(R.id.child_count);
        }
    }

    public class ItemChildViewHolder extends ChildViewHolder{
        public TextView contentView;
        public SlideMenuView slideMenuView;


        public ItemChildViewHolder(View itemView){
            super(itemView);
            contentView = itemView.findViewById(R.id.content_tv);
            slideMenuView = itemView.findViewById(R.id.slide_menu_view);
        }
    }

}

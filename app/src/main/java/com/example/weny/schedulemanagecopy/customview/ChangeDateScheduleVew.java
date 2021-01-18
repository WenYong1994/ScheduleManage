package com.example.weny.schedulemanagecopy.customview;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weny.schedulemanagecopy.R;
import com.example.weny.uilibrary.customview.sidesliplist.sliderecyclerview.SlideMenuView;
import com.jimmy.common.util.DensityUtils;

public class ChangeDateScheduleVew extends FrameLayout {
    public static final int CHIOCE_DATE_STATUS=-1;//选日期状态
    public static final int DRAGING_STATUS=-2;//日程拖拽状态

    ImageView itemImg;
    TextView contentTv;
    TextView dateTv;
    View rootFra;

    private int status =DRAGING_STATUS;
    int[] location = new int[2];

    float dragingWidth,dragingHeight,chioceDateWidth,chioceDateHeight,dragingStartX;

    public ChangeDateScheduleVew(Context context) {
        super(context);
        init(context);
    }


    public ChangeDateScheduleVew(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ChangeDateScheduleVew(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        inflate(context, R.layout.change_date_schedule_layout,this);
        itemImg = findViewById(R.id.item_img);
        contentTv = findViewById(R.id.content_tv);
        dateTv = findViewById(R.id.data_tv);
        rootFra = findViewById(R.id.root_fra);
        chioceDateWidth= DensityUtils.dipToSp(getContext(),80);
        chioceDateHeight =  DensityUtils.dipToSp(getContext(),80);
    }




    public void chioceDateStatus(float x,float y){//更换日期状态
        if(status!=CHIOCE_DATE_STATUS){
            status = CHIOCE_DATE_STATUS;
            setPadding(0,DensityUtils.dipToSp(getContext(),10),0,0);

            ViewGroup.MarginLayoutParams marginLayoutParams = (MarginLayoutParams) itemImg.getLayoutParams();
            marginLayoutParams.leftMargin=DensityUtils.dipToSp(getContext(),10);
            itemImg.setLayoutParams(marginLayoutParams);

            contentTv.setPadding(0,0,0,0);

            //执行动画
            ShapeChangeAnimation shapeChangeAnimation =new ShapeChangeAnimation(this,dragingWidth,dragingHeight,chioceDateWidth,chioceDateHeight);
            startAnimation(shapeChangeAnimation);
            setBackgroundResource(R.drawable.schedule_change_data_icon);
        }
    }

    public void dragingStatus(float x,float y){//任务列表状态
        if(status!=DRAGING_STATUS){
            status = DRAGING_STATUS;
            setPadding(0,0,0,0);

            ViewGroup.MarginLayoutParams marginLayoutParams = (MarginLayoutParams) itemImg.getLayoutParams();
            marginLayoutParams.leftMargin=DensityUtils.dipToSp(getContext(),20);
            itemImg.setLayoutParams(marginLayoutParams);

            contentTv.setPadding(DensityUtils.dipToSp(getContext(),30),0,0,0);


            //执行动画
            ShapeChangeAnimation shapeChangeAnimation =new ShapeChangeAnimation(this,chioceDateWidth,chioceDateHeight,dragingWidth,dragingHeight);
            startAnimation(shapeChangeAnimation);

//            ViewGroup.MarginLayoutParams layoutParams = (MarginLayoutParams) getLayoutParams();
//            layoutParams.width = (int) startWidth;
//            layoutParams.height = (int) startHeight;
//            setLayoutParams(layoutParams);

            setBackgroundResource(R.color.color_schedule_item_is_draging_bg);
        }
    }


    public void amendmentView(float width,float height){
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.height = (int) height;
        layoutParams.width = (int) width;
        setLayoutParams(layoutParams);
        dragingWidth  = width;
        dragingHeight = height;
        dragingStartX = getX();
        setBackgroundResource(R.color.color_schedule_item_is_draging_bg);
    }

    public void setContent(String content){
        contentTv.setText(content);
    }

    public void setDate(String date){
        dateTv.setText(date);
    }

    public void setItemImgResources(int resId){
        itemImg.setImageResource(resId);
    }

    public float getAnchorX() {
        if(status==CHIOCE_DATE_STATUS){
            getLocationInWindow(location);
            if(location!=null){
                return location[0]+getWidth()/2f;
            }
        }
        return -1;
    }

    public float getAnchorY() {
        if(status==CHIOCE_DATE_STATUS){
            getLocationInWindow(location);
            if(location!=null){
                return location[1];
            }
        }
        return -1;
    }


    public void hide(){
        setVisibility(View.INVISIBLE);
        clearAnimation();
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.width = (int) dragingWidth;
        layoutParams.height = (int) (dragingHeight);
        setLayoutParams(layoutParams);

    }

    public void show(View view){
        int[] location = new  int[2];
        view.getLocationInWindow (location); //获取在当前窗口内的绝对坐标

        int[] parLoc = new int[2];
        ViewParent parent = getParent();
        if(parent instanceof View){
            ((View)parent).getLocationInWindow(parLoc);
        }

        int parY = parLoc[1];
        int x = location[0];
        int y = location[1];
        y = y-parY;



        setX(x);
        setY(y);
        setVisibility(View.VISIBLE);
    }

    //由拖拽状态变化到时间选择状态相互装换的动画
    class ShapeChangeAnimation extends Animation{
        View view;
        float startWidth,startHeight,distanceWidth,distanceHeight;
        boolean isChange;

        /**
         *
         * @param view 目标view
         * @param startWidth 开始的宽度
         * @param startHeight 开始的高度
         * @param trageWidth 目标宽度
         * @param trageHeight 目标高度
         *
         */
        public ShapeChangeAnimation(View view,float startWidth,float startHeight,float trageWidth,float trageHeight){
            this.view = view;
            this.distanceWidth =trageWidth - startWidth;
            this.distanceHeight=trageHeight - startHeight;
            this.startWidth =startWidth;
            this.startHeight =startHeight;
            isChange=false;
            setDuration(200);
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            if(view.getVisibility()==VISIBLE){
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.width = (int) (startWidth+ distanceWidth*interpolatedTime);
                layoutParams.height = (int) (startHeight+distanceHeight*interpolatedTime);
                view.setLayoutParams(layoutParams);
            }else {
                if(!isChange){
                    isChange=true;
                    ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                    layoutParams.width = (int) (startWidth+ distanceWidth);
                    layoutParams.height = (int) (startHeight+distanceHeight);
                    view.setLayoutParams(layoutParams);
                    view.setVisibility(INVISIBLE);
                    cancel();
                }
            }
        }

    }







}

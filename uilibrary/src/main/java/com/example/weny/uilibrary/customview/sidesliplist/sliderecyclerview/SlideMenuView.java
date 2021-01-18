package com.example.weny.uilibrary.customview.sidesliplist.sliderecyclerview;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.weny.uilibrary.R;
import com.jimmy.common.util.DensityUtils;

public class SlideMenuView extends FrameLayout {
    Context  context;


    public SlideMenu slideMenu;
    public TextView contentTv;
    public View leftView,rightView;
    public ImageView itemImage;
    public ViewGroup centerGroup;
    public ViewGroup slideContentMenu;
    public View imgClickView;

    public static final float WIDTH_SCAL_FACTOR = 0.7f;
    public static final float HEIGHT_SCAL_FACTOR = 0.7f;

    private OPENTYPE leftType,rightType;
    OnSildeMenuStatusChangeListener onSildeMenuStatusChangeListener;

    SildeScalAnimation sildeScalAnimation,sildeRecoverAnimation;

    float startWidth,startHeight,dargWidth,dargHeight;
    boolean isInitMesua=false;


    public SlideMenuView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public SlideMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }


    public SlideMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    public TextView getContentTv() {
        return contentTv;
    }

    public SlideMenu getSlideMenu() {
        return slideMenu;
    }

    private void init() {
        inflate(context, R.layout.slide_menu_viiew_layout,this);
        slideMenu = findViewById(R.id.slide_menu);
        leftView = findViewById(R.id.left_fra);
        rightView = findViewById(R.id.right_fra);
        contentTv = findViewById(R.id.content_tv);
        itemImage = findViewById(R.id.item_img);
        centerGroup = findViewById(R.id.center_group);
        slideContentMenu = findViewById(R.id.slide_content_menu);
        imgClickView = findViewById(R.id.img_click_view);

        slideMenu.setSlideThreshold(DensityUtils.dipToSp(context,60));
        slideMenu.setOnSlideMenuDraggingListener(new SlideMenu.OnSlideMenuDraggingListener() {
            @Override
            public void onLeftDragging(boolean isHide,int draggX) {
                if(!isHide){
                    int value1 = DensityUtils.dipToSp(context, 60);

                    int leftViewWidth = leftView.getWidth()/2;
                    if(draggX<value1){
                        leftType=OPENTYPE.TYPE_CLOSE;
                        leftView.setBackgroundColor(getContext().getResources().getColor(R.color.color_schedule_item_not_show));
                    }else if (draggX>value1&&draggX<leftViewWidth){
                        leftType=OPENTYPE.TYPE_FEW;
                        leftView.setBackgroundColor(getContext().getResources().getColor(R.color.color_schedule_item_left_few));
                    }else {
                        leftType=OPENTYPE.TYPE_MORE;
                        leftView.setBackgroundColor(getContext().getResources().getColor(R.color.color_schedule_item_left_more));
                    }
                }
            }

            @Override
            public void onRightDragging(boolean isHide,int draggX) {
                if(!isHide){
                    int value1 = DensityUtils.dipToSp(context, 60);
                    int leftViewWidth = rightView.getWidth()/2;
                    if(draggX<value1){
                        rightType=OPENTYPE.TYPE_CLOSE;
                        rightView.setBackgroundColor(getContext().getResources().getColor(R.color.color_schedule_item_not_show));
                    }else if (draggX>value1&&draggX<leftViewWidth){
                        rightType=OPENTYPE.TYPE_FEW;
                        rightView.setBackgroundColor(getContext().getResources().getColor(R.color.color_schedule_item_right_few));
                    }else {
                        rightType=OPENTYPE.TYPE_MORE;
                        rightView.setBackgroundColor(getContext().getResources().getColor(R.color.color_schedule_item_right_more));
                    }
                }
            }
        });

        slideMenu.setOnSlideMenuStatusChangerListener(new SlideMenu.OnSlideMenuStatusChangerListener() {
            @Override
            public void onLeftMenuShow() {
                if(onSildeMenuStatusChangeListener!=null){
                    onSildeMenuStatusChangeListener.onLeftMenuShow(SlideMenuView.this,leftType);
                }
            }

            @Override
            public void onLeftMenuHide() {
                if(onSildeMenuStatusChangeListener!=null){
                    onSildeMenuStatusChangeListener.onLeftMenuHide(SlideMenuView.this,leftType);
                }
                leftType=OPENTYPE.TYPE_CLOSE;
            }

            @Override
            public void onRightMenuShow() {
                if(onSildeMenuStatusChangeListener!=null){
                    onSildeMenuStatusChangeListener.onRightMenuShow(SlideMenuView.this,rightType);
                 }
            }

            @Override
            public void onRightMenuHide() {
                if(onSildeMenuStatusChangeListener!=null){
                    onSildeMenuStatusChangeListener.onRightMenuHide(SlideMenuView.this,rightType);
                }
                rightType=OPENTYPE.TYPE_CLOSE;
            }
        });




    }


    private void initMesue() {
        if(!isInitMesua){
            isInitMesua = true;
            startHeight =slideContentMenu.getMeasuredHeight();
            startWidth = slideContentMenu.getMeasuredWidth();

            dargHeight = startHeight*WIDTH_SCAL_FACTOR;
            dargWidth = startWidth*WIDTH_SCAL_FACTOR;
        }

    }

    public void setContentText(String contentText) {
        contentTv.setText(contentText);
    }

    public void setOnSildeMenuStatusChangeListener(OnSildeMenuStatusChangeListener onSildeMenuStatusChangeListener) {
        this.onSildeMenuStatusChangeListener = onSildeMenuStatusChangeListener;
    }


    public interface OnSildeMenuStatusChangeListener{

        void onRightMenuShow(SlideMenuView slideMenu,OPENTYPE type);

        void onRightMenuHide(SlideMenuView slideMenu,OPENTYPE type);

        void onLeftMenuShow(SlideMenuView slideMenu,OPENTYPE type);

        void onLeftMenuHide(SlideMenuView slideMenu,OPENTYPE type);

    }

    //执行缩小动画
    public void scal(){
        if(sildeScalAnimation == null){
            initMesue();
            sildeScalAnimation = new SildeScalAnimation(centerGroup,startWidth,startHeight,dargWidth,dargHeight);
        }
        if(getVisibility()==VISIBLE){
            centerGroup.startAnimation(sildeScalAnimation);
        }
    }


    //执行复原动画
    public void recover(){
        if(isInitMesua){
            ViewGroup.LayoutParams layoutParams = centerGroup.getLayoutParams();
            layoutParams.width = (int) startWidth;
            layoutParams.height = (int) startHeight;
            centerGroup.setLayoutParams(layoutParams);
        }
    }


    /**
     * 放大
     */
    public void zoom(){
        if(sildeRecoverAnimation == null){
            initMesue();
            sildeRecoverAnimation = new SildeScalAnimation(centerGroup,dargWidth,dargHeight,startWidth,startHeight);
        }
        centerGroup.startAnimation(sildeRecoverAnimation);
    }


    public enum OPENTYPE{
        TYPE_FEW,//展开少的
        TYPE_MORE,//展开多的
        TYPE_CLOSE//没有展开
    }

    class SildeScalAnimation extends Animation{
        View contentView;
        float contentViewWidth,contentViewHeight;
        float distanceWidth,distantHeight;


        public SildeScalAnimation(View contentView,float startWidt,float startHeight,float endWidth,float endHeight) {
            this.contentView = contentView;
            contentViewWidth = startWidt;
            contentViewHeight = startHeight;
            distanceWidth = startWidt - endWidth;
            distantHeight = startHeight - endHeight;
            setDuration(200);
        }


        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
            layoutParams.width = (int) (contentViewWidth-interpolatedTime*distanceWidth);
            layoutParams.height = (int) (contentViewHeight-interpolatedTime*distantHeight);
            contentView.setLayoutParams(layoutParams);
        }
    }



}

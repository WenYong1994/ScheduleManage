package com.example.weny.schedulemanagecopy.activity.shadow;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.weny.schedulemanagecopy.R;
import com.example.weny.uilibrary.widget.ShadowDrawable;
import com.jimmy.common.base.app.BaseActivity;
import com.jimmy.common.util.DensityUtils;

public class ShadowActivity extends BaseActivity implements IShadowView{
    IShadowPresenter presenter;


    @Override
    protected void initPresenter() {
        presenter = new ShadowPresenter(this,new ShadowMode());
    }

    @Override
    protected int getRootViewId() {
        return R.id.m_root_view;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shadow;
    }

    @Override
    protected void onSetContentViewAfter() {
        setBtnShadow();
    }


    private void setBtnShadow(){//设置阴影

        ShadowDrawable.setShadowDrawable(findViewById(R.id.view_3), ShadowDrawable.SHAPE_CIRCLE, Color.parseColor("#7C4DFF"),
                DensityUtils.dipToSp(this,8), Color.parseColor("#992979FF"), DensityUtils.dipToSp(this,6), DensityUtils.dipToSp(this,3), DensityUtils.dipToSp(this,3));

        ShadowDrawable.setShadowDrawable(findViewById(R.id.view_4), ShadowDrawable.SHAPE_ROUND,  getResources().getColor(R.color.colorBlue),
                DensityUtils.dipToSp(this,8), getResources().getColor(R.color.colorAccent50A), DensityUtils.dipToSp(this,6), DensityUtils.dipToSp(this,3), DensityUtils.dipToSp(this,3));

    }

}

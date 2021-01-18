package com.example.weny.schedulemanagecopy.activity.constraint_activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.weny.schedulemanagecopy.R;
import com.jimmy.common.base.app.BaseActivity;

public class ConstraintLayoutTestActivity extends BaseActivity {


    @Override
    protected void initPresenter() {

    }

    @Override
    protected int getRootViewId() {
        return R.id.m_root_view;
    }

    @Override
    protected int getLayoutId() {
//        return R.layout.activity_constraint_layout_test;
        return R.layout.activity_constraint_layout_test_1;
    }

    @Override
    protected void onSetContentViewAfter() {

    }
}

package com.example.weny.schedulemanagecopy.activity.InputDialog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.weny.schedulemanagecopy.R;
import com.example.weny.schedulemanagecopy.service.MyService;
import com.jimmy.common.base.app.BaseActivity;

public class InputWindowActivity extends BaseActivity {

    @Override
    protected void initPresenter() {
        Intent intent =new Intent(this, MyService.class);
        startService(intent);
    }


    @Override
    protected int getRootViewId() {
        return R.id.m_root_view;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_input_window;
    }



    @Override
    protected void onSetContentViewAfter() {
        findViewById(R.id.m_root_view).setOnClickListener(view -> {
            finish();
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}


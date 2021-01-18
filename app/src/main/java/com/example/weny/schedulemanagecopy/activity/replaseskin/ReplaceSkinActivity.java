package com.example.weny.schedulemanagecopy.activity.replaseskin;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.weny.schedulemanagecopy.R;
import com.jimmy.common.base.app.BaseActivity;

public class ReplaceSkinActivity extends BaseActivity {


    @Override
    protected void initPresenter() {

    }

    @Override
    protected int getRootViewId() {
        return R.id.m_root_view;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_replace_skin;
    }

    @Override
    protected void onSetContentViewAfter() {
        initView();

    }

    private void initView() {

        TextView textView = findViewById(R.id.tv);
        textView.setTextColor(getResources().getColor(R.color.color_text_view_color));

        View viewById = findViewById(R.id.bg_view);
        viewById.setBackgroundColor(getResources().getColor(R.color.color_view_bg));

        findViewById(R.id.btn).setOnClickListener(view -> {
            Intent intent =new Intent(getBaseActivity(),SkinSettingActivity.class);
            startActivity(intent);
        });

    }

    @Override
    protected void onSuperBefor(Bundle savedInstanceState) {
        super.onSuperBefor(savedInstanceState);
    }


}
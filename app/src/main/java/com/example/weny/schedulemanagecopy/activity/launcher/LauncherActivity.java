package com.example.weny.schedulemanagecopy.activity.launcher;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.weny.schedulemanagecopy.R;
import com.example.weny.schedulemanagecopy.activity.constraint_activity.ConstraintLayoutTestActivity;
import com.example.weny.schedulemanagecopy.activity.floatwindow.FloatWindowBtnActivity;
import com.example.weny.schedulemanagecopy.activity.kotlin_activity.Main2Activity;
import com.example.weny.schedulemanagecopy.activity.main.MainActivity;
import com.example.weny.schedulemanagecopy.activity.replaseskin.ReplaceSkinActivity;
import com.example.weny.schedulemanagecopy.activity.shadow.ShadowActivity;
import com.example.weny.schedulemanagecopy.activity.sidesliplist.SideslipListActivity;
import com.example.weny.schedulemanagecopy.activity.wake.SpeechWakeActivity;
import com.example.weny.schedulemanagecopy.service.MyService;
import com.example.weny.schedulemanagecopy.utils.NotificationUtils;
import com.example.weny.speech_recognition.SpeechHelper;
import com.example.weny.uilibrary.widget.ShadowDrawable;
import com.jimmy.common.base.app.BaseActivity;
import com.jimmy.common.util.DensityUtils;
import com.jimmy.common.util.LogUtils;

import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

public class LauncherActivity extends BaseActivity implements ILaunchView {
    ILauncherPresenter presenter;
    @BindView(R.id.calendar)
    TextView calendar;
    @BindView(R.id.btn)
    View btn;







    @Override
    protected void initPresenter() {
        presenter = new LauncherPresenter(this, new LauncherMode());
    }

    @Override
    protected int getRootViewId() {
        return R.id.m_root_view;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_launcher;
    }

    @Override
    protected void onSetContentViewAfter() {
//        presenter.showActivityCreatedMsg();
        presenter.addShortcuts(this);
        presenter.onIntent(this,getIntent());

        Intent intent =new Intent(this, MyService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        }else {
            startService(intent);
        }
//        NotificationUtils.notificationWidget(this);

//        NotificationUtils.test(this);


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        presenter.onIntent(this,intent);
    }

    @OnClick({R.id.text, R.id.calendar,R.id.btn,R.id.sidesliplist,R.id.hook_replace_skin,R.id.speech_wake,R.id.float_window_btn,R.id.contraint_layout_tv,R.id.kotlin_activity})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.float_window_btn:
                intent = new Intent(this, FloatWindowBtnActivity.class);
                startActivity(intent);
                break;
            case R.id.text:
                break;
            case R.id.calendar:
                intent =new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.btn:
                intent = new Intent(this, ShadowActivity.class);
                startActivity(intent);
                break;
            case R.id.sidesliplist:
                intent = new Intent(this, SideslipListActivity.class);
                startActivity(intent);
                break;
            case R.id.hook_replace_skin:
                intent = new Intent(this, ReplaceSkinActivity.class);
                startActivity(intent);
                break;
            case R.id.speech_wake:

                intent =new Intent(this, SpeechWakeActivity.class);
                startActivity(intent);
                break;
            case R.id.contraint_layout_tv:
                intent = new Intent(this, ConstraintLayoutTestActivity.class);
                startActivity(intent);
                break;
            case R.id.kotlin_activity:

                intent = new Intent(this, Main2Activity.class);
                startActivity(intent);
                break;
        }
    }






}

package com.example.weny.schedulemanagecopy.activity.floatwindow;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.weny.schedulemanagecopy.R;

public class FloatWindowBtnActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_float_window_btn);


        findViewById(R.id.start_float_window).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                checkMyPermission();
            }
        });
    }

    private void checkMyPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M&&!Settings.canDrawOverlays(this)) {
            //没有权限，需要申请权限，因为是打开一个授权页面，所以拿不到返回状态的，所以建议是在onResume方法中从新执行一次校验
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 100);
        }else{
            //已经有权限，可以直接显示悬浮窗
            ViewManager.getInstance(FloatWindowBtnActivity.this).showFloatBall();
        }
    }
}

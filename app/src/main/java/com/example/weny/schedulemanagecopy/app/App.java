package com.example.weny.schedulemanagecopy.app;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.weny.schedulemanagecopy.R;
import com.example.weny.schedulemanagecopy.listener.OnSkinChangeListener;
import com.example.weny.speech_recognition.SpeechHelper;
import com.fanjun.keeplive.KeepLive;
import com.fanjun.keeplive.config.ForegroundNotification;
import com.fanjun.keeplive.config.ForegroundNotificationClickListener;
import com.fanjun.keeplive.config.KeepLiveService;
import com.jimmy.common.skin.SkinEngine;
import com.jimmy.common.skin.SkinFactory;

public class App extends Application {

    private  static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //初始化 ap保活
        initKeepAlive();

        //初始化换肤引擎
        SkinEngine.getInstance().init(this,new OnSkinChangeListener());

        //初始化科大讯飞引擎
        SpeechHelper.init(this);




    }

    private void initKeepAlive() {
        //定义前台服务的默认样式。即标题、描述和图标
        ForegroundNotification foregroundNotification = new ForegroundNotification("测试","描述", R.mipmap.ic_launcher,
                //定义前台服务的通知点击事件
                new ForegroundNotificationClickListener() {

                    @Override
                    public void foregroundNotificationClick(Context context, Intent intent) {
                    }
                });
        //启动保活服务
        KeepLive.startWork(this, KeepLive.RunMode.ENERGY, foregroundNotification,
                //你需要保活的服务，如socket连接、定时任务等，建议不用匿名内部类的方式在这里写
                new KeepLiveService() {
                    /**
                     * 运行中
                     * 由于服务可能会多次自动启动，该方法可能重复调用
                     */
                    @Override
                    public void onWorking() {
                        Log.e("keepLive","onWorking+++++++++++++++++++++++++++++++++");
                    }
                    /**
                     * 服务终止
                     * 由于服务可能会被多次终止，该方法可能重复调用，需同onWorking配套使用，如注册和注销broadcast
                     */
                    @Override
                    public void onStop() {
                        Log.e("keepLive","onStop--------------------------------");
                    }
                });
    }

    public static App getInstance() {
        return instance;
    }
}

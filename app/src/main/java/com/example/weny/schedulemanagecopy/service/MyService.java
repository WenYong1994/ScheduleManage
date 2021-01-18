package com.example.weny.schedulemanagecopy.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import com.example.weny.schedulemanagecopy.R;
import com.example.weny.schedulemanagecopy.activity.InputDialog.InputWindowActivity;
import com.example.weny.schedulemanagecopy.utils.NotificationUtils;
import com.jimmy.common.util.LogUtils;

public class MyService extends Service {

    MyBroadCastReciver myBroadCastReciver;


    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(NotificationUtils.notifi_id,NotificationUtils.getNotificationWidget(this));
        myBroadCastReciver=new MyBroadCastReciver();
        IntentFilter intentFilter =new IntentFilter();

        intentFilter.addAction(NotificationUtils.NOTIFICATION_CLICK_ACTION);
        intentFilter.addAction("openInputWindowActivity");

        registerReceiver(myBroadCastReciver,intentFilter);

        if("openReplaceSkinAction".equalsIgnoreCase(intent.getAction())){
            Intent intent3 = new Intent(this,InputWindowActivity.class);
            intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent3);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myBroadCastReciver);
    }

    class MyBroadCastReciver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String str = intent.getStringExtra("btnTag");
            Log.e("MyBroadCastReciver",intent.getAction()+","+str);
            if(NotificationUtils.NOTIFICATION_CLICK_ACTION.equalsIgnoreCase(intent.getAction())){
                //notificationWidget

                if(str!=null){
                    switch (str){
                        case "btn_1":
                            NotificationUtils.remoteView.setTextViewText(R.id.content,"btn_1");
                            NotificationUtils.notificationWidget(context);
                            break;
                        case "btn_2":
                            NotificationUtils.remoteView.setTextViewText(R.id.content,"btn_2");

                            NotificationUtils.notificationWidget(context);
                            break;
                    }
                }
            }else if("openInputWindowActivity".equalsIgnoreCase(intent.getAction())){
                Intent intent1 =new Intent(context, InputWindowActivity.class);
                startActivity(intent1);
            }



        }
    }
}

package com.example.weny.schedulemanagecopy.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.example.weny.schedulemanagecopy.R;
import com.example.weny.schedulemanagecopy.activity.InputDialog.InputWindowActivity;
import com.example.weny.schedulemanagecopy.activity.launcher.LauncherActivity;

public class NotificationUtils {

    public static final int notifi_id = 10001;
    public static final String id = "schedule_channel_1";
    public static final String name = "schedule_channel_name_1";
    public static final String NOTIFICATION_CLICK_ACTION = "notificationClickAction";
    public static Notification notification;


    public static RemoteViews remoteView;

    public static void notificationWidget(Context context){
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(notifi_id,getNotificationWidget(context));
    }


    public static Notification getNotificationWidget(Context context){
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(notification==null){
            Notification.Builder builder = new Notification.Builder(context)
                    .setContentTitle(context.getString(R.string.app_name))
                    .setContentText("有待办消息未读")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setWhen(System.currentTimeMillis())
                    .setDefaults(NotificationCompat.FLAG_ONLY_ALERT_ONCE)
                    .setVibrate(new long[]{0})
                    .setSound(null);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //26及以上
                NotificationChannel channel = new NotificationChannel(id, "待办消息",
                        NotificationManager.IMPORTANCE_HIGH);
                channel.enableLights(false);
                channel.enableVibration(false);
                channel.setVibrationPattern(new long[]{0});
                channel.setSound(null, null);
                manager.createNotificationChannel(channel);
                builder.setChannelId(id);
            }

            if(remoteView == null){
                remoteView =new RemoteViews(context.getPackageName(), R.layout.notification_widget_layout);
                remoteView.setOnClickPendingIntent(R.id.btn_1,getPendingIntent(context,"btn_1",10));
                remoteView.setOnClickPendingIntent(R.id.btn_2,getPendingIntent(context,"btn_2",11));
                remoteView.setOnClickPendingIntent(R.id.btn_widget,getShowWidgetPendingIntent(context,"btn_widget",13));
            }

            remoteView.setTextViewText(R.id.content,"未选择");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                builder.setCustomContentView(remoteView);
            }else {
                builder.setContent(remoteView);
            }

            Intent intent=new Intent(context, LauncherActivity.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            PendingIntent pendingIntent=PendingIntent.getActivity(context,9,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);//设置一个点击意图


            notification = builder.build();

            notification.flags = Notification.FLAG_NO_CLEAR;
        }
        return notification;

    }

    public static Notification getNoSoundVibrateNofitcation(Context context){
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Notification.Builder builder = new Notification.Builder(context)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText("有待办消息未读")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .setDefaults(NotificationCompat.FLAG_ONLY_ALERT_ONCE)
                .setVibrate(new long[]{0})
                .setSound(null);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("to-do", "待办消息",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(false);
            channel.enableVibration(false);
            channel.setVibrationPattern(new long[]{0});
            channel.setSound(null, null);
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId("to-do");
        }

        Notification notification = builder.build();
//        notificationManager.notify(10, notification);
        return notification;
    }







    private static PendingIntent getPendingIntent(Context context,String str,int requestCode){
        Intent intent = new Intent(NOTIFICATION_CLICK_ACTION);
        intent.putExtra("btnTag",str);
        PendingIntent pi = PendingIntent.getBroadcast(context,requestCode,intent,0);
        return pi;
    }

    private static PendingIntent getShowWidgetPendingIntent(Context context,String str,int requestCode){
        Intent intent = new Intent(NOTIFICATION_CLICK_ACTION);
        intent.setClass(context, InputWindowActivity.class);
        intent.putExtra("btnTag",str);
        PendingIntent pi = PendingIntent.getActivity(context,requestCode,intent,0);
        return pi;
    }




    public static void release(){
        notification=null;
        remoteView = null;
    }


    public static void test(Context context){

    }


}

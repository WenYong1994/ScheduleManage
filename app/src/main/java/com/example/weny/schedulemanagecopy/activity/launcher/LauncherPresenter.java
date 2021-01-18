package com.example.weny.schedulemanagecopy.activity.launcher;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;

import com.example.weny.schedulemanagecopy.R;
import com.example.weny.schedulemanagecopy.activity.main.MainActivity;
import com.example.weny.schedulemanagecopy.activity.replaseskin.ReplaceSkinActivity;

import java.util.Arrays;

public class LauncherPresenter implements ILauncherPresenter {
    ILaunchView view;
    ILauncherMode mode;

    public LauncherPresenter(ILaunchView view, ILauncherMode mode) {
        this.view = view;
        this.mode = mode;
    }




    @Override
    public void showActivityCreatedMsg() {
        String msg = mode.getMsg("我显示好了");
        view.showToast(msg);
    }

    @Override
    public void addShortcuts(Context context) {

    }


    @Override
    public void onIntent(Context context,Intent intent) {

        if("openCalendarAction".equalsIgnoreCase(intent.getAction())){
            Intent openCalendarInten = new Intent(context,MainActivity.class);
            context.startActivity(openCalendarInten);
        }else if("openReplaceSkinAction".equalsIgnoreCase(intent.getAction())){
            Intent replaceSkinInetnt = new Intent(context, ReplaceSkinActivity.class);
            context.startActivity(replaceSkinInetnt);
        }

    }

}

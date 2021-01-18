package com.example.weny.schedulemanagecopy.activity.launcher;

import android.content.Context;
import android.content.Intent;

import com.jimmy.common.base.presenter.IBasePresenter;

public interface ILauncherPresenter extends IBasePresenter {

    void showActivityCreatedMsg();

    void addShortcuts(Context context);

    void onIntent(Context context,Intent intent);
}

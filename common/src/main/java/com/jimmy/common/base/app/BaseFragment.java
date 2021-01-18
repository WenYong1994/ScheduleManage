package com.jimmy.common.base.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 *
 * Created by weny on 2019/6/11.
 *
 *
 */
public abstract class BaseFragment extends Fragment {

    protected View rootView;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(getLayoutId(), container, false);
        if(onSetViewBefor()){
            return null;
        }
        unbinder = ButterKnife.bind(this, rootView);
        onSetViewAfter();
        return rootView;
    }

    protected boolean onSetViewBefor(){
        return false;
    };

    protected abstract int getLayoutId();

    protected abstract void onSetViewAfter();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(unbinder!=null){
            unbinder.unbind();
        }
    }
}

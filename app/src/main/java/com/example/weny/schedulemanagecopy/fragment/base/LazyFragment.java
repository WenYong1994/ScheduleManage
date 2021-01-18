package com.example.weny.schedulemanagecopy.fragment.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.weny.schedulemanagecopy.testlazyfragment.LogProxy;

public abstract class LazyFragment extends Fragment {

    LogProxy logProxy;

    View rootView;
    boolean isViewCreated = false;
//    boolean currentVisibleState =false;
    boolean isFristVisible=true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        logProxy = new LogProxy("LazyFragment");
        Log.e("LazyFragment","onCreateView");
        if(rootView==null){
            rootView = inflater.inflate(getLayoutRes(),container,false);
        }
        initView(rootView);
        isViewCreated=true;
        //补充分发，由于第一次会先调用setUserVisibleHint再调用 oncreateView所以在 setUserVisibleHint时不会分发事件
        if(getUserVisibleHint()&&!isHidden()){//如果这是默认的tab就需要分发事件
            dispatchUserVisibleHint(true);
        }
        return rootView;
    }


    public  <T extends View> T findViewById(int resId){
        if(rootView!=null){
            return rootView.findViewById(resId);
        }
        return null;
    }

    protected abstract void initView(View view);

    protected abstract int getLayoutRes();


    //viewPager会调用
    @Override
    public void setUserVisibleHint(boolean isVisible){
        super.setUserVisibleHint(isVisible);

//        if(currentVisibleState == isVisible){//如果当前状态和目标显示状态一致就不分发
//            return;
//        }

        if(isViewCreated){
            if(isVisible){//分发事件
                dispatchUserVisibleHint(true);
            }else{
                dispatchUserVisibleHint(false);
            }
        }


    }

    //兼容，FragmentTransaction
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        boolean isVisible = !hidden;//
    }


    protected void dispatchUserVisibleHint(boolean isVisible){
        if(isVisible){
            if(isFristVisible){
                isFristVisible=false;
                onFragmentFristVisibile();
            }
            onFragmentResume();
        }else {
            onFragmentPause();
        }
    }

    //当Fragment第一次启动
    protected abstract void onFragmentFristVisibile();


    public  void onFragmentPause(){
        logProxy.E("onFragmentPause++++++++++++");

    }

    public void onFragmentResume(){
        logProxy.E("onFragmentResume+++++++++++");

    }


    @Override
    public void onAttach(Context context){
        super.onAttach(context);
    }

    @Override
    public void onDetach(){
        super.onDetach();
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public void onResume(){
        super.onResume();
        if(getUserVisibleHint()&&!isHidden()){
            dispatchUserVisibleHint(true);
        }
    }


    @Override
    public void onPause(){
        super.onPause();
        if(getUserVisibleHint()&&!isHidden()){
            dispatchUserVisibleHint(false);
        }
    }

    @Override
    public void onStop(){
        super.onStop();
    }

    public void setLogProxy(LogProxy logProxy) {
        this.logProxy = logProxy;
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        isFristVisible=true;
        isViewCreated=false;
//        currentVisibleState=false;
    }
}

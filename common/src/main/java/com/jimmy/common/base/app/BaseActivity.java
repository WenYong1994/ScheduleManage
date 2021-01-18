package com.jimmy.common.base.app;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.gyf.barlibrary.ImmersionBar;
import com.jimmy.common.base.view.IBaseView;
import com.jimmy.common.constant.Constant;
import com.jimmy.common.skin.SkinEngine;
import com.jimmy.common.skin.SkinFactory;
import com.jimmy.common.util.NotchScreenUtil;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity implements IBaseView {
    Unbinder unbinder;
    private boolean isImmersion=true;

    private SkinFactory mSkinFactory;

    private String currentSkin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        onSuperBefor(savedInstanceState);
        super.onCreate(savedInstanceState);
        initPresenter();
        if(onSetContentViewBefor()){
            return;
        }
        if(isImmersion){
            ImmersionBar.with(this).init();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                getWindow().setStatusBarColor(Color.parseColor("#000000"));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//android6.0以后可以对状态栏文字颜色和图标进行修改
                    getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }
            }
        }
        setContentView(getLayoutId());
        unbinder = ButterKnife.bind(this);

        int rootViewId = getRootViewId();
        if(rootViewId!=0) NotchScreenUtil.setNotechScreen(this,rootViewId);

        onSetContentViewAfter();
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkSkin();
    }

    private void checkSkin() {
        if(!TextUtils.isEmpty(SkinEngine.getInstance().getCurrentSkin())&&!SkinEngine.getInstance().getCurrentSkin().equalsIgnoreCase(currentSkin)){
            changeSkin(SkinEngine.getInstance().getCurrentSkin());
        }
    }

    protected abstract void initPresenter();

    protected  boolean onSetContentViewBefor(){
        return false;
    };

    protected abstract int getRootViewId();

    protected abstract int getLayoutId();

    protected abstract void onSetContentViewAfter();

    //设置时候打开沉浸式布局，需要在onSetContentVIewBefore方法里面执行才有用
    protected void setImmersion(boolean isImmersion){
        this.isImmersion = isImmersion;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar immersionBar = ImmersionBar.with(this);
        if(immersionBar!=null){
            immersionBar.destroy();
        }
        if(unbinder!=null){
            unbinder.unbind();
        }
    }

    protected void onSuperBefor(Bundle savedInstanceState){
        //初始化换肤
        initChangeSkin();
    }

    private void initChangeSkin() {
        mSkinFactory = new SkinFactory();
        mSkinFactory.setDelegate(getDelegate());
        LayoutInflater layoutInflater = LayoutInflater.from(this);
//        Log.d("layoutInflaterTag", layoutInflater.toString());
        layoutInflater.setFactory2(mSkinFactory);
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLongToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    protected BaseActivity getBaseActivity(){
        return this;
    }

    /**
     * 换肤
     * */
    protected void changeSkin(String skinName){
        currentSkin = skinName;
        String path = SkinEngine.getSKinPath();
        SkinEngine.getInstance().load(path,skinName);
        mSkinFactory.changeSkin();
    }

    protected void closeOutSkin(){
        SkinEngine.getInstance().close();
        mSkinFactory.changeSkin();
    }

}

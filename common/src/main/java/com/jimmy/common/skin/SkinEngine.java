package com.jimmy.common.skin;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v4.content.ContextCompat;

import java.io.File;
import java.lang.reflect.Method;

public class SkinEngine {
    public static final String SKIN_RESOURCE = "skinResource";
    public static final String DEFAULT_SKIN = "DEFAULT_SKIN";

    //单例
    private final static SkinEngine instance = new SkinEngine();

    private  String currentSkin = DEFAULT_SKIN;

    public static SkinEngine getInstance() {
        return instance;
    }

    private SkinEngine() {
    }

    public  String getCurrentSkin() {
        return currentSkin;
    }

    public void init(Context context, SkinFactory.OnChangeSkinListener onChangeSkinListener) {
        mContext = context.getApplicationContext();
        //使用application的目的是，如果万一传进来的是Activity对象
        //那么它被静态对象instance所持有，这个Activity就无法释放了
        SkinFactory.setAllOnChangeSkinListener(onChangeSkinListener);
    }

    private Resources mOutResource;// TODO: 资源管理器
    private Context mContext;//上下文
    private String mOutPkgName;// TODO: 外部资源包的packageName

    public void close(){
        currentSkin = DEFAULT_SKIN;
        mOutPkgName = "";
        mOutResource = null;
    }

    public static String getSKinPath(){
        return Environment.getExternalStorageDirectory() +"/"+ SKIN_RESOURCE;
    }


    public void load(File file,String skinName){
        currentSkin = skinName;

        //取得PackageManager引用
        PackageManager mPm = mContext.getPackageManager();
        String path = file.getAbsolutePath();
        try {
            //“检索在包归档文件中定义的应用程序包的总体信息”，说人话，外界传入了一个apk的文件路径，这个方法，拿到这个apk的包信息,这个包信息包含什么？
            PackageInfo mInfo = mPm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
            mOutPkgName = mInfo.packageName;//先把包名存起来
            AssetManager assetManager;//资源管理器
            //TODO: 关键技术点3 通过反射获取AssetManager 用来加载外面的资源包
            assetManager = AssetManager.class.newInstance();//反射创建AssetManager对象，为何要反射？使用反射，是因为他这个类内部的addAssetPath方法是hide状态
            //addAssetPath方法可以加载外部的资源包
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);//为什么要反射执行这个方法？因为它是hide的,不直接对外开放，只能反射调用
            addAssetPath.invoke(assetManager, path);//反射执行方法
            mOutResource = new Resources(assetManager,//参数1，资源管理器
                    mContext.getResources().getDisplayMetrics(),//这个好像是屏幕参数
                    mContext.getResources().getConfiguration());//资源配置
            //最终创建出一个 "外部资源包"mOutResource ,它的存在，就是要让我们的app有能力加载外部的资源文件
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * TODO: 加载外部资源包
     */
    public void load(final String filePath,String skinName) {//path 是外部传入的apk文件名
        if(currentSkin!=null&&currentSkin.equalsIgnoreCase(skinName)){//不用重复加载
            return;
        }
        File file = new File(filePath,skinName);
        if (!file.exists()&&!file.isFile()) {
            return;
        }
        load(file,skinName);
    }

    /**
     * 提供外部资源包里面的颜色
     * @param resId
     * @return
     */
    public int getColor(Resources viewResources,int resId) {
        if (mOutResource == null) {
            return viewResources.getColor(resId);
        }

        String resName = viewResources.getResourceEntryName(resId);
        int outResId = mOutResource.getIdentifier(resName, "color", mOutPkgName);
        if (outResId == 0) {
            return resId;
        }
        int color = mOutResource.getColor(outResId);
        return color;
    }

    /**
     * 提供外部资源包里的图片资源
     * @param resId
     * @return
     */
    public Drawable getDrawable(Resources viewResource,int resId) {//获取图片
        if (mOutResource == null) {
            return viewResource.getDrawable(resId);
        }
        String resName = viewResource.getResourceEntryName(resId);
        int outResId = mOutResource.getIdentifier(resName, "drawable", mOutPkgName);
        if (outResId == 0) {
            return ContextCompat.getDrawable(mContext, resId);
        }
        return mOutResource.getDrawable(outResId);
    }

    //..... 这里还可以提供外部资源包里的String，font等等等，只不过要手动写代码来实现getXX方法
}

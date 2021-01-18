package com.example.weny.schedulemanagecopy.activity.replaseskin;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.weny.schedulemanagecopy.R;
import com.jimmy.common.base.app.BaseActivity;
import com.jimmy.common.constant.Constant;
import com.jimmy.common.skin.SkinEngine;
import com.jimmy.common.skin.SkinHelper;
import com.jimmy.common.util.ToastUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import butterknife.BindView;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class SkinSettingActivity extends BaseActivity {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};//内存读写的权限，现在要动态申请了？

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;


    List<String> list;
    SkinAdapter skinAdapter;
    @Override
    protected void initPresenter() {

    }

    @Override
    protected int getRootViewId() {
        return 0;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_skin_setting;
    }

    @Override
    protected void onSetContentViewAfter() {
        initRecycler();
        findSkin();
        verifyStoragePermissions(getBaseActivity());
    }

    private void initRecycler() {
        list =new ArrayList<>();
        skinAdapter = new SkinAdapter(this,list);
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setAdapter(skinAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        skinAdapter.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String skinName = view.getTag().toString();
                int permission = ActivityCompat.checkSelfPermission(getBaseActivity(),PERMISSIONS_STORAGE[0]);
                if (permission == PackageManager.PERMISSION_GRANTED) {
                    if(skinName == "使用默认"){
                        closeOutSkin();
                        return;
                    }
                    SkinHelper.INSTANCE.checkStartSkin(SkinSettingActivity.this, skinName, new Function1<String, Unit>() {
                        @Override
                        public Unit invoke(String s) {
                            changeSkin(s);
                            ToastUtils.showShortToast(SkinSettingActivity.this, "换肤为"+skinName);
                            return null;
                        }
                    });
                }else {
                    verifyStoragePermissions(getBaseActivity());
                }
            }
        });
    }

    private void checkStartSkin(String skinName) {
        //检查文件里面有没有这个
        String path = SkinEngine.getSKinPath()+"/"+skinName;
        File file = new File(path);
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    AssetManager assets = getAssets();
                    if(file.exists()){

                    }
                    if(writeFile(path,assets.open(skinName))){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                changeSkin(skinName);
                                ToastUtils.showShortToast(getBaseActivity(),"换肤为"+skinName);
                            }
                        });
                    }
                }catch (Exception e){}
            }
        }.start();
    }


    private static boolean writeFile(String fileName, InputStream in) throws IOException
    {
        boolean bRet = true;
        try {
            OutputStream os = new FileOutputStream(fileName);
            byte[] buffer = new byte[4112];
            int read;
            while((read = in.read(buffer)) != -1)
            {
                os.write(buffer, 0, read);
                Log.e("writeFile","复制中");
            }
            in.close();
            in = null;
            os.flush();
            os.close();
            os = null;
//			Log.v(TAG, "copyed file: " + fileName);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            bRet = false;
        }
        return bRet;
    }



    private void findSkin() {
        try {
            String[] files = getAssets().list("");
            for (String filePath:files) {
                if (filePath.contains("apk")) {
                    list.add(filePath);
                }
                if (filePath.contains("dc")) {
                    list.add(filePath);
                }
                if (filePath.contains("arr")) {
                    list.add(filePath);
                }
            }
            list.add("使用默认");
            skinAdapter.notifyDataSetChanged();

        } catch (IOException e) {
            e.printStackTrace();
        }

//        File parent = new File(Constant.skinPath);
//        if(parent.isDirectory()){
//            File[] listFiles = parent.listFiles();
//            for (File file:listFiles){
//                if(file.getName().contains("apk")){
//                    list.add(file.getName().replace(".apk",""));
//                }
//            }
//        }
//        list.add("使用默认");
//        skinAdapter.notifyDataSetChanged();

    }

    /**
     * 申请权限，为了要把外部文件写入到 手机内存中
     *
     * @param activity
     */
    public static void verifyStoragePermissions(AppCompatActivity activity) {

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,PERMISSIONS_STORAGE[0]);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


class SkinViewHolder extends RecyclerView.ViewHolder{
    TextView skinName;
    public SkinViewHolder(@NonNull View itemView) {
        super(itemView);
        skinName = itemView.findViewById(R.id.skin_name);
    }
}

class SkinAdapter extends RecyclerView.Adapter<SkinViewHolder>{
    Context context;
    List<String> list;

    View.OnClickListener onItemClickListener;

    public void setOnItemClickListener(View.OnClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public SkinAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public SkinViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.skin_item_layout,viewGroup,false);
        SkinViewHolder skinViewHolder =new SkinViewHolder(view);
        return skinViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SkinViewHolder skinViewHolder, int i) {
        String s = list.get(i);
        skinViewHolder.itemView.setTag(s);
        skinViewHolder.skinName.setText(s);
        skinViewHolder.itemView.setOnClickListener(onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }
}

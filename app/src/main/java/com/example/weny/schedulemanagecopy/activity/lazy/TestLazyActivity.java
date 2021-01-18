package com.example.weny.schedulemanagecopy.activity.lazy;

import android.annotation.TargetApi;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.weny.schedulemanagecopy.R;
import com.example.weny.schedulemanagecopy.activity.InputDialog.InputWindowActivity;
import com.example.weny.schedulemanagecopy.testlazyfragment.TestLazyFragment;
import com.example.weny.schedulemanagecopy.utils.NotificationUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class TestLazyActivity extends AppCompatActivity {

    ViewPager viewPager;
    ArrayList<Fragment> fragments;

    View.OnClickListener onTabBtnClickListener;

    Button btn1;
    Button btn2;
    Button btn3;
    Button btn4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_lazy);
        viewPager = findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(4);
        initView();
        onTabBtnClickListener = view -> ontabBtnClick(view);

        btn1 = findViewById(R.id.btn_1);
        btn1.setOnClickListener(onTabBtnClickListener);
        btn2=findViewById(R.id.btn_2);
        btn2.setOnClickListener(onTabBtnClickListener);
        btn3=findViewById(R.id.btn_3);
        btn3.setOnClickListener(onTabBtnClickListener);
        btn4=findViewById(R.id.btn_4);
        btn4.setOnClickListener(onTabBtnClickListener);

        addShortcuts(this);
    }

    private void ontabBtnClick(View view) {

        btn1.setBackgroundColor(getResources().getColor(R.color.colorBlue));
        btn2.setBackgroundColor(getResources().getColor(R.color.colorBlue));
        btn3.setBackgroundColor(getResources().getColor(R.color.colorBlue));
        btn4.setBackgroundColor(getResources().getColor(R.color.colorBlue));
        view.setBackgroundColor(getResources().getColor(R.color.colorAccent50A));
        switch (view.getId()){
            case R.id.btn_1:
                viewPager.setCurrentItem(0);
                break;
            case R.id.btn_2:
                viewPager.setCurrentItem(1);
                break;
            case R.id.btn_3:
                viewPager.setCurrentItem(2);
                break;
            case R.id.btn_4:
                viewPager.setCurrentItem(3);
                break;
        }




    }

    @TargetApi(Build.VERSION_CODES.N_MR1)
    public void addShortcuts(Context context) {
        ShortcutManager shortcutManager = context.getSystemService(ShortcutManager.class);
        Intent intent =new Intent();
        intent.setAction("openCalendarAction");
        intent.addCategory("testCategory");
//        intent.setData(Uri.parse("test1"));

        ShortcutInfo shortcut = new ShortcutInfo.Builder(context, "id1")
                .setShortLabel("查看日历")
                .setLongLabel("打开日历")
                .setIcon(Icon.createWithResource(context, R.drawable.triangular))
                .setIntent(intent)
                .build();


        Intent intent1 =new Intent();
        intent1.setAction("openReplaceSkinAction");
        intent1.addCategory("testCategory");
//        intent.setData(Uri.parse("test1"));
        ShortcutInfo shortcut1 = new ShortcutInfo.Builder(context, "id2")
                .setShortLabel("换肤")
                .setLongLabel("换肤")
                .setIcon(Icon.createWithResource(context, R.mipmap.schedule_comple_flase))
                .setIntent(intent1)
                .build();




        Intent intent3 =new Intent();
        intent3.setAction("openInputWindowActivity");
        intent3.setClass(context, InputWindowActivity.class);
        intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ShortcutInfo shortcut3 = new ShortcutInfo.Builder(context, "id3")
                .setShortLabel("添加任务")
                .setLongLabel("添加任务")
                .setIcon(Icon.createWithResource(context, R.mipmap.calendar_type_list))
                .setIntent(intent3)
                .build();


        shortcutManager.setDynamicShortcuts(Arrays.asList(shortcut,shortcut1,shortcut3));

    }

    private void initView() {
        fragments =new ArrayList<>();
        fragments.add(new TestLazyFragment().setName("1"));
        fragments.add(new TestLazyFragment().setName("2"));
        fragments.add(new TestLazyFragment().setName("3"));
        fragments.add(new TestLazyFragment().setName("4"));
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(0);
    }



    class PagerAdapter extends FragmentPagerAdapter {


        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public Fragment getItem(int i) {
            return fragments.get(i);
        }

    }



}

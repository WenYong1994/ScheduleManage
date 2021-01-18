package com.example.weny.schedulemanagecopy.testlazyfragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.weny.schedulemanagecopy.R;
import com.example.weny.schedulemanagecopy.activity.launcher.LauncherActivity;
import com.example.weny.schedulemanagecopy.fragment.base.LazyFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class TestLazyFragment extends LazyFragment {
    String name;

    public TestLazyFragment() {
        // Required empty public constructor
    }

    public TestLazyFragment setName(String name) {
        this.name = name;
        setLogProxy(new LogProxy(name));
        return this;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getActivity(), LauncherActivity.class);
                getActivity().startActivity(intent);
            }
        });
        ((TextView)findViewById(R.id.tv)).setText(name);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_test_lazy;
    }

    @Override
    protected void onFragmentFristVisibile() {
        Log.e("LazyFragment001",name+"：onFragmentFristVisibile");
    }

    @Override
    public void onFragmentResume() {
        super.onFragmentResume();
        Log.e("LazyFragment001",name+"：真正的Resume,开始耗时操作");
    }

    @Override
    public void onFragmentPause() {
        super.onFragmentPause();
        Log.e("LazyFragment001",name+": 真正的Pause,停止耗时操作");
    }
}

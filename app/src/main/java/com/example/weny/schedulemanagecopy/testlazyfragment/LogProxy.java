package com.example.weny.schedulemanagecopy.testlazyfragment;

import android.util.Log;

public class LogProxy {
    String name;

    public LogProxy(String name) {
        this.name = name;
    }

    public  void E(String s){
        if(this!=null){
            Log.e("LazyFragment",name+s);
        }
    }

}

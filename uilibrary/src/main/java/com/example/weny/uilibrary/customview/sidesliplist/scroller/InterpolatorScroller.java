package com.example.weny.uilibrary.customview.sidesliplist.scroller;

import android.content.Context;
import android.view.ViewConfiguration;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public class InterpolatorScroller extends Scroller {
    public InterpolatorScroller(Context context) {
        super(context);
    }

    public InterpolatorScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }



}

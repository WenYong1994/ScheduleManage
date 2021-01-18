package com.example.weny.schedulemanagecopy.listener;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.jimmy.common.skin.SkinEngine;
import com.jimmy.common.skin.SkinFactory;

import java.util.HashMap;

public class OnSkinChangeListener implements SkinFactory.OnChangeSkinListener {

    @Override
    public void onChangeSkin(View view, HashMap<String, String> attrsMap) {

        if (!TextUtils.isEmpty(attrsMap.get("background"))) {//属性名,例如，这个background，text，textColor....
            int bgId = Integer.parseInt(attrsMap.get("background").substring(1));//属性值，R.id.XXX ，int类型，
            // 这个值，在app的一次运行中，不会发生变化
            String attrType = view.getResources().getResourceTypeName(bgId); // 属性类别：比如 drawable ,color
            if (TextUtils.equals(attrType, "drawable")) {//区分drawable和color
                view.setBackground(SkinEngine.getInstance().getDrawable(view.getResources(),bgId));//加载外部资源管理器，拿到外部资源的drawable
            } else if (TextUtils.equals(attrType, "color")) {
                view.setBackgroundColor(SkinEngine.getInstance().getColor(view.getResources(),bgId));
            }
        }




        if (view instanceof TextView) {
            if (!TextUtils.isEmpty(attrsMap.get("textColor"))) {
                int textColorId = Integer.parseInt(attrsMap.get("textColor").substring(1));
                ((TextView) view).setTextColor(SkinEngine.getInstance().getColor(view.getResources(),textColorId));
            }
        }


        //自定义属性 测试
        if (!TextUtils.isEmpty(attrsMap.get("test_background"))) {//属性名,例如，这个background，text，textColor....
            int bgId = Integer.parseInt(attrsMap.get("test_background").substring(1));//属性值，R.id.XXX ，int类型，
            // 这个值，在app的一次运行中，不会发生变化
            String attrType = view.getResources().getResourceTypeName(bgId); // 属性类别：比如 drawable ,color
            if (TextUtils.equals(attrType, "drawable")) {//区分drawable和color
                view.setBackground(SkinEngine.getInstance().getDrawable(view.getResources(),bgId));//加载外部资源管理器，拿到外部资源的drawable
            } else if (TextUtils.equals(attrType, "color")) {
                view.setBackgroundColor(SkinEngine.getInstance().getColor(view.getResources(),bgId));
            }
        }


        //那么如果是自定义组件呢
//            if (view instanceof ZeroView) {
        //那么这样一个对象，要换肤，就要写针对性的方法了，每一个控件需要用什么样的方式去换，尤其是那种，自定义的属性，怎么去set，
        // 这就对开发人员要求比较高了，而且这个换肤接口还要暴露给 自定义View的开发人员,他们去定义
        // ....
//            }
    }
}

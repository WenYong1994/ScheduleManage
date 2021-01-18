package com.example.weny.schedulemanagecopy.activity.kotlin_activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.transition.Slide
import android.util.Log
import android.view.MotionEvent
import com.example.weny.schedulemanagecopy.R
import com.example.weny.uilibrary.customview.expandablerecyclerview.models.ExpandableGroup
import com.example.weny.uilibrary.customview.sidesliplist.sliderecyclerview.SlideMenuView
import com.jimmy.common.base.app.BaseActivity
import kotlinx.android.synthetic.main.activity_main2.*
import java.util.ArrayList

class Main2Activity : BaseActivity() {




    override fun initPresenter() {

    }

    override fun getRootViewId(): Int {
        return R.id.m_root_view
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main2
    }

    override fun onSetContentViewAfter() {
        var x= schedule_list_view.draggingView
        Log.e("mast",(x ?:"null" ).toString())


        schedule_list_view.setNeedSuspendBar(true)
        addData()
    }


    fun addData(){
        val groupList = ArrayList<ExpandableGroup<*, *>>()
        val list0 = ArrayList<String>()
        val list1 = ArrayList<String>()
        val list2 = ArrayList<String>()
        val list3 = ArrayList<String>()
        val list4 = ArrayList<String>()
        val list5 = ArrayList<String>()
        val list6 = ArrayList<String>()
        val list7 = ArrayList<String>()
        val list8 = ArrayList<String>()
        val list9 = ArrayList<String>()
        val list10 = ArrayList<String>()
        val list11 = ArrayList<String>()
        val list12 = ArrayList<String>()

        for (i in 0..4) {
            list0.add("0-$i")
        }
        for (i in 0..4) {
            list1.add("1-$i")
        }
        for (i in 0..9) {
            list2.add("2-$i")
        }
        for (i in 0..4) {
            list3.add("3-$i")
        }
        for (i in 0..4) {
            list4.add("4-$i")
        }
        for (i in 0..4) {
            list5.add("5-$i")
        }
        for (i in 0..4) {
            list6.add("6-$i")
        }
        for (i in 0..4) {
            list7.add("7-$i")
        }
        for (i in 0..4) {
            list8.add("8-$i")
        }
        for (i in 0..4) {
            list9.add("9-$i")
        }
        for (i in 0..4) {
            list10.add("10-$i")
        }
        for (i in 0..4) {
            list11.add("11-$i")
        }
        for (i in 0..6) {
            list12.add("12-$i")
        }

        val group = ExpandableGroup("已过期", list0, "已过期")
        val group1 = ExpandableGroup("今天", list1, "今天")
        val group2 = ExpandableGroup("后2天", list2, "后2天")
        val group3 = ExpandableGroup("后3天", list3, "后3天")
        val group4 = ExpandableGroup("后4天", list4, "后4天")
        val group5 = ExpandableGroup("后5天", list5, "后5天")
        val group6 = ExpandableGroup("后6天", list6, "后6天")
        val group7 = ExpandableGroup("后7天", list7, "后7天")
        val group8 = ExpandableGroup("后8天", list8, "后8天")
        val group9 = ExpandableGroup("后9天", list9, "后9天")
        val group10 = ExpandableGroup("后10天", list10, "后10天")
        val group11 = ExpandableGroup("后11天", list11, "后11天")
        val group12 = ExpandableGroup("后12天", list12, "后12天")

        groupList.add(group)
        groupList.add(group1)
        groupList.add(group2)
        groupList.add(group3)
        groupList.add(group4)
        groupList.add(group5)
        groupList.add(group6)
        groupList.add(group7)
        groupList.add(group8)
        groupList.add(group9)
        groupList.add(group10)
        groupList.add(group11)
        groupList.add(group12)

        schedule_list_view.addGroups(groupList)

    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {



        return super.onTouchEvent(event)
    }

}

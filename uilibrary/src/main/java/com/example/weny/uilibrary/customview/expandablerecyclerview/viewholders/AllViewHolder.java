package com.example.weny.uilibrary.customview.expandablerecyclerview.viewholders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class AllViewHolder extends RecyclerView.ViewHolder {

    ChildViewHolder childViewHolder;
    GroupViewHolder groupViewHolder;

    public AllViewHolder(@NonNull View allView,ChildViewHolder childViewHolder,GroupViewHolder groupViewHolder) {
        super(allView);
        this.childViewHolder = childViewHolder;
        this.groupViewHolder= groupViewHolder;
    }


    public ChildViewHolder getChildViewHolder() {
        return childViewHolder;
    }

    public GroupViewHolder getGroupViewHolder() {
        return groupViewHolder;
    }
}

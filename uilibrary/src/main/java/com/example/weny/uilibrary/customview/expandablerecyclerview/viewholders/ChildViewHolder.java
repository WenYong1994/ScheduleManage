package com.example.weny.uilibrary.customview.expandablerecyclerview.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.weny.uilibrary.customview.expandablerecyclerview.models.ExpandableGroup;


/**
 * ViewHolder for {@link ExpandableGroup#items}
 */
public class ChildViewHolder extends RecyclerView.ViewHolder {

  public ChildViewHolder(View itemView) {
    super(itemView);
  }

  public void setVisibility(int visibility){
    itemView.setVisibility(visibility);
  }

}

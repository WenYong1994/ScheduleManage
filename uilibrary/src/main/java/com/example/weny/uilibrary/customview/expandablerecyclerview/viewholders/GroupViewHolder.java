package com.example.weny.uilibrary.customview.expandablerecyclerview.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.weny.uilibrary.customview.expandablerecyclerview.listeners.OnGroupClickListener;
import com.example.weny.uilibrary.customview.expandablerecyclerview.models.ExpandableGroup;


/**
 *
 * The current implementation does now allow for sub {@link View} of the parent view to trigger
 * a collapse / expand. *Only* click events on the parent {@link View} will trigger a collapse or
 * expand
 */
public abstract class GroupViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

  private OnGroupClickListener listener;
  private AllViewHolder allViewHolder;

  public GroupViewHolder(View itemView) {
    super(itemView);
    itemView.setOnClickListener(this);
  }

  public void setAllViewHolder(AllViewHolder allViewHolder) {
    this.allViewHolder = allViewHolder;
  }

  @Override
  public void onClick(View v) {
    if (listener != null) {
      listener.onGroupClick(allViewHolder.getAdapterPosition());
    }
  }

  public void setOnGroupClickListener(OnGroupClickListener listener) {
    this.listener = listener;
  }

  public void expand() {}

  public void collapse() {}

  public void setVisibility(int visibility){
    itemView.setVisibility(visibility);
  }
}

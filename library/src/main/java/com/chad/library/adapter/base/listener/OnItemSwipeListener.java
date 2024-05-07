package com.chad.library.adapter.base.listener;

import android.graphics.Canvas;

import androidx.recyclerview.widget.RecyclerView;


public interface OnItemSwipeListener {
    
    void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos);

    
    void clearView(RecyclerView.ViewHolder viewHolder, int pos);

    
    void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos);

    
    void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive);
}

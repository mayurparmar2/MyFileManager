package com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter.Callback;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;


public class Listener_for_Recycler implements RecyclerView.OnItemTouchListener {
    private ClickListener clickListener;
    private GestureDetector gestureDetector;


    public interface ClickListener {
        void onClick(View view, int i);

        void onLongClick(View view, int i);
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean z) {
    }

    @Override
    public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
    }

    public Listener_for_Recycler(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
        this.clickListener = clickListener;
        this.gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent motionEvent) {
                ClickListener clickListener2;
                View findChildViewUnder = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
                if (findChildViewUnder == null || (clickListener2 = clickListener) == null) {
                    return;
                }
                clickListener2.onLongClick(findChildViewUnder, recyclerView.getChildPosition(findChildViewUnder));
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
        View findChildViewUnder = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
        if (findChildViewUnder == null || this.clickListener == null || !this.gestureDetector.onTouchEvent(motionEvent)) {
            return false;
        }
        this.clickListener.onClick(findChildViewUnder, recyclerView.getChildPosition(findChildViewUnder));
        return false;
    }
}

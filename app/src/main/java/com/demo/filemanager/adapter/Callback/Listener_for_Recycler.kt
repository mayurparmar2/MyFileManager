package com.demo.filemanager.adapter.Callback

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener

class Listener_for_Recycler(
    context: Context?,
    recyclerView: RecyclerView,
    clickListener: ClickListener
) : OnItemTouchListener {
    private val clickListener: ClickListener?
    private val gestureDetector: GestureDetector

    interface ClickListener {
        fun onClick(view: View?, i: Int)
        fun onLongClick(view: View?, i: Int)
    }

    override fun onRequestDisallowInterceptTouchEvent(z: Boolean) {}
    override fun onTouchEvent(recyclerView: RecyclerView, motionEvent: MotionEvent) {}

    init {
        this.clickListener = clickListener
        gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapUp(motionEvent: MotionEvent): Boolean {
                    return true
                }

                override fun onLongPress(motionEvent: MotionEvent) {
                    val findChildViewUnder = recyclerView.findChildViewUnder(motionEvent.x, motionEvent.y)
                    if (findChildViewUnder == null) {
                        return
                    }
                    clickListener.onLongClick(findChildViewUnder, recyclerView.getChildPosition(findChildViewUnder))
                }
            })
    }

    override fun onInterceptTouchEvent(
        recyclerView: RecyclerView,
        motionEvent: MotionEvent
    ): Boolean {
        val findChildViewUnder = recyclerView.findChildViewUnder(motionEvent.x, motionEvent.y)
        if (findChildViewUnder == null || clickListener == null || !gestureDetector.onTouchEvent(
                motionEvent
            )
        ) {
            return false
        }
        clickListener.onClick(findChildViewUnder, recyclerView.getChildPosition(findChildViewUnder))
        return false
    }
}

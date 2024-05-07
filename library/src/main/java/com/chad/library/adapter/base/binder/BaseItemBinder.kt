package com.chad.library.adapter.base.binder

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import com.chad.library.adapter.base.BaseBinderAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder


abstract class BaseItemBinder<T, VH : BaseViewHolder> {

    private val clickViewIds by lazy(LazyThreadSafetyMode.NONE) { ArrayList<Int>() }
    private val longClickViewIds by lazy(LazyThreadSafetyMode.NONE) { ArrayList<Int>() }

    internal var _adapter: BaseBinderAdapter? = null
    internal var _context: Context? = null

    val adapter: BaseBinderAdapter
        get() {
            checkNotNull(_adapter) {
                """This $this has not been attached to BaseBinderAdapter yet.
                    You should not call the method before addItemBinder()."""
            }
            return _adapter!!
        }

    val context: Context
        get() {
            checkNotNull(_context) {
                """This $this has not been attached to BaseBinderAdapter yet.
                    You should not call the method before onCreateViewHolder()."""
            }
            return _context!!
        }

    val data: MutableList<Any> get() = adapter.data

    abstract fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH

    
    abstract fun convert(holder: VH, data: T)

    
    open fun convert(holder: VH, data: T, payloads: List<Any>) {}

    open fun onFailedToRecycleView(holder: VH): Boolean {
        return false
    }

    
    open fun onViewAttachedToWindow(holder: VH) {}

    
    open fun onViewDetachedFromWindow(holder: VH) {}

    
    open fun onClick(holder: VH, view: View, data: T, position: Int) {}

    
    open fun onLongClick(holder: VH, view: View, data: T, position: Int): Boolean {
        return false
    }

    
    open fun onChildClick(holder: VH, view: View, data: T, position: Int) {}

    
    open fun onChildLongClick(holder: VH, view: View, data: T, position: Int): Boolean {
        return false
    }

    fun addChildClickViewIds(@IdRes vararg ids: Int) {
        ids.forEach {
            this.clickViewIds.add(it)
        }
    }

    fun getChildClickViewIds() = this.clickViewIds

    fun addChildLongClickViewIds(@IdRes vararg ids: Int) {
        ids.forEach {
            this.longClickViewIds.add(it)
        }
    }

    fun getChildLongClickViewIds() = this.longClickViewIds
}
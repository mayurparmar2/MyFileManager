package com.chad.library.adapter.base.provider

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import com.chad.library.adapter.base.BaseProviderMultiAdapter
import com.chad.library.adapter.base.util.getItemView
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import java.lang.ref.WeakReference


abstract class BaseItemProvider<T> {

    lateinit var context: Context

    private var weakAdapter: WeakReference<BaseProviderMultiAdapter<T>>? = null
    private val clickViewIds by lazy(LazyThreadSafetyMode.NONE) { ArrayList<Int>() }
    private val longClickViewIds by lazy(LazyThreadSafetyMode.NONE) { ArrayList<Int>() }

    internal fun setAdapter(adapter: BaseProviderMultiAdapter<T>) {
        weakAdapter = WeakReference(adapter)
    }

    open fun getAdapter(): BaseProviderMultiAdapter<T>? {
        return weakAdapter?.get()
    }

    abstract val itemViewType: Int

    abstract val layoutId: Int
        @LayoutRes
        get

    abstract fun convert(helper: BaseViewHolder, item: T)

    open fun convert(helper: BaseViewHolder, item: T, payloads: List<Any>) {}

    
    open fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder(parent.getItemView(layoutId))
    }

    
    open fun onViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {}

    
    open fun onViewAttachedToWindow(holder: BaseViewHolder) {}

    
    open fun onViewDetachedFromWindow(holder: BaseViewHolder) {}

    
    open fun onClick(helper: BaseViewHolder, view: View, data: T, position: Int) {}

    
    open fun onLongClick(helper: BaseViewHolder, view: View, data: T, position: Int): Boolean {
        return false
    }

    open fun onChildClick(helper: BaseViewHolder, view: View, data: T, position: Int) {}

    open fun onChildLongClick(helper: BaseViewHolder, view: View, data: T, position: Int): Boolean {
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
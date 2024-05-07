package com.chad.library.adapter.base

import android.util.SparseIntArray
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.chad.library.adapter.base.viewholder.BaseViewHolder


abstract class BaseMultiItemQuickAdapter<T : MultiItemEntity, VH : BaseViewHolder>(data: MutableList<T>? = null)
    : BaseQuickAdapter<T, VH>(0, data) {

    private val layouts: SparseIntArray by lazy(LazyThreadSafetyMode.NONE) { SparseIntArray() }

    override fun getDefItemViewType(position: Int): Int {
        return data[position].itemType
    }

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): VH {
        val layoutResId = layouts.get(viewType)
        require(layoutResId != 0) { "ViewType: $viewType found layoutResId，please use addItemType() first!" }
        return createBaseViewHolder(parent, layoutResId)
    }

    
    protected fun addItemType(type: Int, @LayoutRes layoutResId: Int) {
        layouts.put(type, layoutResId)
    }
}
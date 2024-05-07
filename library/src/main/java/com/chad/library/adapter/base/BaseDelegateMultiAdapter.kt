package com.chad.library.adapter.base

import android.view.ViewGroup
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.viewholder.BaseViewHolder


abstract class BaseDelegateMultiAdapter<T, VH : BaseViewHolder>(data: MutableList<T>? = null) :
        BaseQuickAdapter<T, VH>(0, data) {

    private var mMultiTypeDelegate: BaseMultiTypeDelegate<T>? = null

    
    fun setMultiTypeDelegate(multiTypeDelegate: BaseMultiTypeDelegate<T>) {
        this.mMultiTypeDelegate = multiTypeDelegate
    }

    fun getMultiTypeDelegate(): BaseMultiTypeDelegate<T>? = mMultiTypeDelegate

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): VH {
        val delegate = getMultiTypeDelegate()
        checkNotNull(delegate) { "Please use setMultiTypeDelegate first!" }
        val layoutId = delegate.getLayoutId(viewType)
        return createBaseViewHolder(parent, layoutId)
    }

    override fun getDefItemViewType(position: Int): Int {
        val delegate = getMultiTypeDelegate()
        checkNotNull(delegate) { "Please use setMultiTypeDelegate first!" }
        return delegate.getItemType(data, position)
    }
}
package com.chad.library.adapter.base.binder

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.chad.library.adapter.base.util.getItemView
import com.chad.library.adapter.base.viewholder.BaseViewHolder


abstract class QuickItemBinder<T> : BaseItemBinder<T, BaseViewHolder>() {

    @LayoutRes
    abstract fun getLayoutId(): Int

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder =
            BaseViewHolder(parent.getItemView(getLayoutId()))

}


package com.chad.library.adapter.base.binder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.chad.library.adapter.base.viewholder.BaseViewHolder


abstract class QuickDataBindingItemBinder<T, DB : ViewDataBinding> : BaseItemBinder<T, QuickDataBindingItemBinder.BinderDataBindingHolder<DB>>() {

    
    class BinderDataBindingHolder<DB : ViewDataBinding>(val dataBinding: DB) : BaseViewHolder(dataBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BinderDataBindingHolder<DB> {
        return BinderDataBindingHolder(onCreateDataBinding(LayoutInflater.from(parent.context), parent, viewType))
    }

    abstract fun onCreateDataBinding(layoutInflater: LayoutInflater, parent: ViewGroup, viewType: Int): DB
}
package com.chad.library.adapter.base.binder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.viewholder.BaseViewHolder


abstract class QuickViewBindingItemBinder<T, VB : ViewBinding> : BaseItemBinder<T, QuickViewBindingItemBinder.BinderVBHolder<VB>>() {

    
    class BinderVBHolder<VB : ViewBinding>(val viewBinding: VB) : BaseViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BinderVBHolder<VB> {
        return BinderVBHolder(onCreateViewBinding(LayoutInflater.from(parent.context), parent, viewType))
    }

    abstract fun onCreateViewBinding(layoutInflater: LayoutInflater, parent: ViewGroup, viewType: Int): VB
}
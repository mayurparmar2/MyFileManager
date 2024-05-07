package com.chad.library.adapter.base.viewholder

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding


open class BaseDataBindingHolder<BD : ViewDataBinding>(view: View) : BaseViewHolder(view) {

    val dataBinding = DataBindingUtil.bind<BD>(view)
}
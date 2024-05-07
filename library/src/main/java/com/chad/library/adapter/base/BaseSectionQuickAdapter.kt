package com.chad.library.adapter.base

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.chad.library.adapter.base.entity.SectionEntity
import com.chad.library.adapter.base.viewholder.BaseViewHolder


abstract class BaseSectionQuickAdapter<T : SectionEntity, VH : BaseViewHolder>
@JvmOverloads constructor(@LayoutRes private val sectionHeadResId: Int,
                          data: MutableList<T>? = null)
    : BaseMultiItemQuickAdapter<T, VH>(data) {

    constructor(@LayoutRes sectionHeadResId: Int,
                @LayoutRes layoutResId: Int,
                data: MutableList<T>? = null) : this(sectionHeadResId, data) {
        setNormalLayout(layoutResId)
    }

    init {
        addItemType(SectionEntity.HEADER_TYPE, sectionHeadResId)
    }

    
    protected abstract fun convertHeader(helper: VH, item: T)

    
    protected open fun convertHeader(helper: VH, item: T, payloads: MutableList<Any>) {}

    
    protected fun setNormalLayout(@LayoutRes layoutResId: Int) {
        addItemType(SectionEntity.NORMAL_TYPE, layoutResId)
    }

    override fun isFixedViewType(type: Int): Boolean {
        return super.isFixedViewType(type) || type == SectionEntity.HEADER_TYPE
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        if (holder.itemViewType == SectionEntity.HEADER_TYPE) {

            convertHeader(holder, getItem(position - headerLayoutCount))
        } else {
            super.onBindViewHolder(holder, position)
        }
    }

    override fun onBindViewHolder(holder: VH, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
            return
        }

        if (holder.itemViewType == SectionEntity.HEADER_TYPE) {
            convertHeader(holder, getItem(position - headerLayoutCount), payloads)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

}
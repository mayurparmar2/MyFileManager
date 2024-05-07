package com.chad.library.adapter.base.delegate

import android.util.SparseIntArray
import androidx.annotation.LayoutRes



abstract class BaseMultiTypeDelegate<T>(private var layouts: SparseIntArray = SparseIntArray()) {
    private var autoMode: Boolean = false
    private var selfMode: Boolean = false

    
    abstract fun getItemType(data: List<T>, position: Int): Int

    fun getLayoutId(viewType: Int): Int {
        val layoutResId = layouts.get(viewType)
        require(layoutResId != 0) { "ViewType: $viewType found layoutResId，please use registerItemType() first!" }
        return layoutResId
    }

    private fun registerItemType(type: Int, @LayoutRes layoutResId: Int) {
        this.layouts.put(type, layoutResId)
    }

    
    fun addItemTypeAutoIncrease(@LayoutRes vararg layoutResIds: Int): BaseMultiTypeDelegate<T> {
        autoMode = true
        checkMode(selfMode)
        for (i in layoutResIds.indices) {
            registerItemType(i, layoutResIds[i])
        }
        return this
    }

    
    fun addItemType(type: Int, @LayoutRes layoutResId: Int): BaseMultiTypeDelegate<T> {
        selfMode = true
        checkMode(autoMode)
        registerItemType(type, layoutResId)
        return this
    }

    private fun checkMode(mode: Boolean) {
        require(!mode) { "Don't mess two register mode" }
    }

}

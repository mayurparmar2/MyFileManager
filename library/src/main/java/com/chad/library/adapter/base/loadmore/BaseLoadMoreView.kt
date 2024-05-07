package com.chad.library.adapter.base.loadmore

import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.viewholder.BaseViewHolder



enum class LoadMoreStatus {
    Complete, Loading, Fail, End
}


abstract class BaseLoadMoreView {

    
    abstract fun getRootView(parent: ViewGroup): View

    
    abstract fun getLoadingView(holder: BaseViewHolder): View

    
    abstract fun getLoadComplete(holder: BaseViewHolder): View

    
    abstract fun getLoadEndView(holder: BaseViewHolder): View

    
    abstract fun getLoadFailView(holder: BaseViewHolder): View

    
    open fun convert(holder: BaseViewHolder, position: Int, loadMoreStatus: LoadMoreStatus) {
        when (loadMoreStatus) {
            LoadMoreStatus.Complete -> {
                getLoadingView(holder).isVisible(false)
                getLoadComplete(holder).isVisible(true)
                getLoadFailView(holder).isVisible(false)
                getLoadEndView(holder).isVisible(false)
            }
            LoadMoreStatus.Loading -> {
                getLoadingView(holder).isVisible(true)
                getLoadComplete(holder).isVisible(false)
                getLoadFailView(holder).isVisible(false)
                getLoadEndView(holder).isVisible(false)
            }
            LoadMoreStatus.Fail -> {
                getLoadingView(holder).isVisible(false)
                getLoadComplete(holder).isVisible(false)
                getLoadFailView(holder).isVisible(true)
                getLoadEndView(holder).isVisible(false)
            }
            LoadMoreStatus.End -> {
                getLoadingView(holder).isVisible(false)
                getLoadComplete(holder).isVisible(false)
                getLoadFailView(holder).isVisible(false)
                getLoadEndView(holder).isVisible(true)
            }
        }
    }

    private fun View.isVisible(visible: Boolean) {
        this.visibility = if (visible) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}


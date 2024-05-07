package com.chad.library.adapter.base.module

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.LoadMoreListenerImp
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.chad.library.adapter.base.loadmore.BaseLoadMoreView
import com.chad.library.adapter.base.loadmore.LoadMoreStatus
import com.chad.library.adapter.base.loadmore.SimpleLoadMoreView
import com.chad.library.adapter.base.viewholder.BaseViewHolder





interface LoadMoreModule

object LoadMoreModuleConfig {

    
    @JvmStatic
    var defLoadMoreView: BaseLoadMoreView = SimpleLoadMoreView()
}


open class BaseLoadMoreModule(private val baseQuickAdapter: BaseQuickAdapter<*, *>) : LoadMoreListenerImp {

    private var mLoadMoreListener: OnLoadMoreListener? = null
    
    private var mNextLoadEnable = true

    var loadMoreStatus = LoadMoreStatus.Complete
        private set

    var isLoadEndMoreGone: Boolean = false
        private set

    
    var loadMoreView = LoadMoreModuleConfig.defLoadMoreView
    
    var enableLoadMoreEndClick = false
    
    var isAutoLoadMore = true
    
    var isEnableLoadMoreIfNotFullPage = true
    
    var preLoadNumber = 1
        set(value) {
            if (value > 1) {
                field = value
            }
        }
    
    val isLoading: Boolean
        get() {
            return loadMoreStatus == LoadMoreStatus.Loading
        }

    
    val loadMoreViewPosition: Int
        get() {
            if (baseQuickAdapter.hasEmptyView()) {
                return -1
            }
            return baseQuickAdapter.let {
                it.headerLayoutCount + it.data.size + it.footerLayoutCount
            }
        }

    
    var isEnableLoadMore = false
        set(value) {
            val oldHasLoadMore = hasLoadMoreView()
            field = value
            val newHasLoadMore = hasLoadMoreView()

            if (oldHasLoadMore) {
                if (!newHasLoadMore) {
                    baseQuickAdapter.notifyItemRemoved(loadMoreViewPosition)
                }
            } else {
                if (newHasLoadMore) {
                    loadMoreStatus = LoadMoreStatus.Complete
                    baseQuickAdapter.notifyItemInserted(loadMoreViewPosition)
                }
            }
        }


    internal fun setupViewHolder(viewHolder: BaseViewHolder) {
        viewHolder.itemView.setOnClickListener {
            if (loadMoreStatus == LoadMoreStatus.Fail) {
                loadMoreToLoading()
            } else if (loadMoreStatus == LoadMoreStatus.Complete) {
                loadMoreToLoading()
            } else if (enableLoadMoreEndClick && loadMoreStatus == LoadMoreStatus.End) {
                loadMoreToLoading()
            }
        }
    }

    
    fun loadMoreToLoading() {
        if (loadMoreStatus == LoadMoreStatus.Loading) {
            return
        }
        loadMoreStatus = LoadMoreStatus.Loading
        baseQuickAdapter.notifyItemChanged(loadMoreViewPosition)
        invokeLoadMoreListener()
    }


    fun hasLoadMoreView(): Boolean {
        if (mLoadMoreListener == null || !isEnableLoadMore) {
            return false
        }
        if (loadMoreStatus == LoadMoreStatus.End && isLoadEndMoreGone) {
            return false
        }
        return baseQuickAdapter.data.isNotEmpty()
    }

    
    internal fun autoLoadMore(position: Int) {
        if (!isAutoLoadMore) {
            
            return
        }
        if (!hasLoadMoreView()) {
            return
        }
        if (position < baseQuickAdapter.itemCount - preLoadNumber) {
            return
        }
        if (loadMoreStatus != LoadMoreStatus.Complete) {
            return
        }
        if (loadMoreStatus == LoadMoreStatus.Loading) {
            return
        }
        if (!mNextLoadEnable) {
            return
        }

        invokeLoadMoreListener()
    }

    
    private fun invokeLoadMoreListener() {
        loadMoreStatus = LoadMoreStatus.Loading
        baseQuickAdapter.mRecyclerView?.let {
            it.post { mLoadMoreListener?.onLoadMore() }
        } ?: mLoadMoreListener?.onLoadMore()
    }

    
    fun checkDisableLoadMoreIfNotFullPage() {
        if (isEnableLoadMoreIfNotFullPage) {
            return
        }
        
        mNextLoadEnable = false
        val recyclerView = baseQuickAdapter.mRecyclerView ?: return
        val manager = recyclerView.layoutManager ?: return
        if (manager is LinearLayoutManager) {
            recyclerView.postDelayed({
                if (isFullScreen(manager)) {
                    mNextLoadEnable = true
                }
            }, 50)
        } else if (manager is StaggeredGridLayoutManager) {
            recyclerView.postDelayed({
                val positions = IntArray(manager.spanCount)
                manager.findLastCompletelyVisibleItemPositions(positions)
                val pos = getTheBiggestNumber(positions) + 1
                if (pos != baseQuickAdapter.itemCount) {
                    mNextLoadEnable = true
                }
            }, 50)
        }
    }

    private fun isFullScreen(llm: LinearLayoutManager): Boolean {
        return (llm.findLastCompletelyVisibleItemPosition() + 1) != baseQuickAdapter.itemCount ||
                llm.findFirstCompletelyVisibleItemPosition() != 0
    }

    private fun getTheBiggestNumber(numbers: IntArray?): Int {
        var tmp = -1
        if (numbers == null || numbers.isEmpty()) {
            return tmp
        }
        for (num in numbers) {
            if (num > tmp) {
                tmp = num
            }
        }
        return tmp
    }

    
    @JvmOverloads
    fun loadMoreEnd(gone: Boolean = false) {
        if (!hasLoadMoreView()) {
            return
        }

        isLoadEndMoreGone = gone

        loadMoreStatus = LoadMoreStatus.End

        if (gone) {
            baseQuickAdapter.notifyItemRemoved(loadMoreViewPosition)
        } else {
            baseQuickAdapter.notifyItemChanged(loadMoreViewPosition)
        }
    }

    
    fun loadMoreComplete() {
        if (!hasLoadMoreView()) {
            return
        }

        loadMoreStatus = LoadMoreStatus.Complete

        baseQuickAdapter.notifyItemChanged(loadMoreViewPosition)

        checkDisableLoadMoreIfNotFullPage()
    }

    
    fun loadMoreFail() {
        if (!hasLoadMoreView()) {
            return
        }
        loadMoreStatus = LoadMoreStatus.Fail
        baseQuickAdapter.notifyItemChanged(loadMoreViewPosition)
    }

    
    override fun setOnLoadMoreListener(listener: OnLoadMoreListener?) {
        this.mLoadMoreListener = listener
        isEnableLoadMore = true
    }

    
    internal fun reset() {
        if (mLoadMoreListener != null) {
            isEnableLoadMore = true
            loadMoreStatus = LoadMoreStatus.Complete
        }
    }
}
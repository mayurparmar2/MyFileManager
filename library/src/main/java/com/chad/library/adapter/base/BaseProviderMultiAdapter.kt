package com.chad.library.adapter.base

import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder


abstract class BaseProviderMultiAdapter<T>(data: MutableList<T>? = null) :
        BaseQuickAdapter<T, BaseViewHolder>(0, data) {

    private val mItemProviders by lazy(LazyThreadSafetyMode.NONE) { SparseArray<BaseItemProvider<T>>() }

    
    protected abstract fun getItemType(data: List<T>, position: Int): Int

    
    open fun addItemProvider(provider: BaseItemProvider<T>) {
        provider.setAdapter(this)
        mItemProviders.put(provider.itemViewType, provider)
    }

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val provider = getItemProvider(viewType)
        checkNotNull(provider) { "ViewType: $viewType no such provider found，please use addItemProvider() first!" }
        provider.context = parent.context
        return provider.onCreateViewHolder(parent, viewType).apply {
            provider.onViewHolderCreated(this, viewType)
        }
    }

    override fun getDefItemViewType(position: Int): Int {
        return getItemType(data, position)
    }

    override fun convert(holder: BaseViewHolder, item: T) {
        getItemProvider(holder.itemViewType)!!.convert(holder, item)
    }

    override fun convert(holder: BaseViewHolder, item: T, payloads: List<Any>) {
        getItemProvider(holder.itemViewType)!!.convert(holder, item, payloads)
    }

    override fun bindViewClickListener(viewHolder: BaseViewHolder, viewType: Int) {
        super.bindViewClickListener(viewHolder, viewType)
        bindClick(viewHolder)
        bindChildClick(viewHolder, viewType)
    }

    
    protected open fun getItemProvider(viewType: Int): BaseItemProvider<T>? {
        return mItemProviders.get(viewType)
    }

    override fun onViewAttachedToWindow(holder: BaseViewHolder) {
        super.onViewAttachedToWindow(holder)
        getItemProvider(holder.itemViewType)?.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: BaseViewHolder) {
        super.onViewDetachedFromWindow(holder)
        getItemProvider(holder.itemViewType)?.onViewDetachedFromWindow(holder)
    }

    protected open fun bindClick(viewHolder: BaseViewHolder) {
        if (getOnItemClickListener() == null) {
            
            
            viewHolder.itemView.setOnClickListener {
                var position = viewHolder.adapterPosition
                if (position == RecyclerView.NO_POSITION) {
                    return@setOnClickListener
                }
                position -= headerLayoutCount

                val itemViewType = viewHolder.itemViewType
                val provider = mItemProviders.get(itemViewType)

                provider.onClick(viewHolder, it, data[position], position)
            }
        }
        if (getOnItemLongClickListener() == null) {
            
            
            viewHolder.itemView.setOnLongClickListener {
                var position = viewHolder.adapterPosition
                if (position == RecyclerView.NO_POSITION) {
                    return@setOnLongClickListener false
                }
                position -= headerLayoutCount

                val itemViewType = viewHolder.itemViewType
                val provider = mItemProviders.get(itemViewType)
                provider.onLongClick(viewHolder, it, data[position], position)
            }
        }
    }

    protected open fun bindChildClick(viewHolder: BaseViewHolder, viewType: Int) {
        if (getOnItemChildClickListener() == null) {
            val provider = getItemProvider(viewType) ?: return
            val ids = provider.getChildClickViewIds()
            ids.forEach { id ->
                viewHolder.itemView.findViewById<View>(id)?.let {
                    if (!it.isClickable) {
                        it.isClickable = true
                    }
                    it.setOnClickListener { v ->
                        var position: Int = viewHolder.adapterPosition
                        if (position == RecyclerView.NO_POSITION) {
                            return@setOnClickListener
                        }
                        position -= headerLayoutCount
                        provider.onChildClick(viewHolder, v, data[position], position)
                    }
                }
            }
        }
        if (getOnItemChildLongClickListener() == null) {
            val provider = getItemProvider(viewType) ?: return
            val ids = provider.getChildLongClickViewIds()
            ids.forEach { id ->
                viewHolder.itemView.findViewById<View>(id)?.let {
                    if (!it.isLongClickable) {
                        it.isLongClickable = true
                    }
                    it.setOnLongClickListener { v ->
                        var position: Int = viewHolder.adapterPosition
                        if (position == RecyclerView.NO_POSITION) {
                            return@setOnLongClickListener false
                        }
                        position -= headerLayoutCount
                        provider.onChildLongClick(viewHolder, v, data[position], position)
                    }
                }
            }
        }
    }
}
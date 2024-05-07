package com.chad.library.adapter.base

import android.annotation.SuppressLint
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.binder.BaseItemBinder
import com.chad.library.adapter.base.viewholder.BaseViewHolder


open class BaseBinderAdapter(list: MutableList<Any>? = null) : BaseQuickAdapter<Any, BaseViewHolder>(0, list) {

    
    private val classDiffMap = HashMap<Class<*>, DiffUtil.ItemCallback<Any>?>()

    private val mTypeMap = HashMap<Class<*>, Int>()
    private val mBinderArray = SparseArray<BaseItemBinder<Any, *>>()

    init {
        setDiffCallback(ItemCallback())
    }

    
    @JvmOverloads
    fun <T : Any> addItemBinder(clazz: Class<out T>, baseItemBinder: BaseItemBinder<T, *>, callback: DiffUtil.ItemCallback<T>? = null): BaseBinderAdapter {
        val itemType = mTypeMap.size + 1
        mTypeMap[clazz] = itemType
        mBinderArray.append(itemType, baseItemBinder as BaseItemBinder<Any, *>)
        baseItemBinder._adapter = this
        callback?.let {
            classDiffMap[clazz] = it as DiffUtil.ItemCallback<Any>
        }
        return this
    }

    
    inline fun <reified T : Any> addItemBinder(baseItemBinder: BaseItemBinder<T, *>, callback: DiffUtil.ItemCallback<T>? = null): BaseBinderAdapter {
        addItemBinder(T::class.java, baseItemBinder, callback)
        return this
    }

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return getItemBinder(viewType).let {
            it._context = context
            it.onCreateViewHolder(parent, viewType)
        }
    }

    override fun convert(holder: BaseViewHolder, item: Any) {
        getItemBinder(holder.itemViewType).convert(holder, item)
    }

    override fun convert(holder: BaseViewHolder, item: Any, payloads: List<Any>) {
        getItemBinder(holder.itemViewType).convert(holder, item, payloads)
    }

    open fun getItemBinder(viewType: Int): BaseItemBinder<Any, BaseViewHolder> {
        val binder = mBinderArray[viewType]
        checkNotNull(binder) { "getItemBinder: viewType '$viewType' no such Binder found，please use addItemBinder() first!" }
        return binder as BaseItemBinder<Any, BaseViewHolder>
    }

    open fun getItemBinderOrNull(viewType: Int): BaseItemBinder<Any, BaseViewHolder>? {
        val binder = mBinderArray[viewType]
        return binder as? BaseItemBinder<Any, BaseViewHolder>
    }

    override fun getDefItemViewType(position: Int): Int {
        return findViewType(data[position].javaClass)
    }

    override fun bindViewClickListener(viewHolder: BaseViewHolder, viewType: Int) {
        super.bindViewClickListener(viewHolder, viewType)
        bindClick(viewHolder)
        bindChildClick(viewHolder, viewType)
    }

    override fun onViewAttachedToWindow(holder: BaseViewHolder) {
        super.onViewAttachedToWindow(holder)
        getItemBinderOrNull(holder.itemViewType)?.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: BaseViewHolder) {
        super.onViewDetachedFromWindow(holder)
        getItemBinderOrNull(holder.itemViewType)?.onViewDetachedFromWindow(holder)

    }

    override fun onFailedToRecycleView(holder: BaseViewHolder): Boolean {
        return getItemBinderOrNull(holder.itemViewType)?.onFailedToRecycleView(holder) ?: false
    }

    protected fun findViewType(clazz : Class<*>):Int {
        val type = mTypeMap[clazz]
        checkNotNull(type) { "findViewType: ViewType: $clazz Not Find!" }
        return type
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
                val binder = getItemBinder(itemViewType)

                binder.onClick(viewHolder, it, data[position], position)
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
                val binder = getItemBinder(itemViewType)
                binder.onLongClick(viewHolder, it, data[position], position)
            }
        }
    }

    protected open fun bindChildClick(viewHolder: BaseViewHolder, viewType: Int) {
        if (getOnItemChildClickListener() == null) {
            val provider = getItemBinder(viewType)
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
            val provider = getItemBinder(viewType)
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


    
    private inner class ItemCallback : DiffUtil.ItemCallback<Any>() {

        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            if (oldItem.javaClass == newItem.javaClass) {
                classDiffMap[oldItem.javaClass]?.let {
                    return it.areItemsTheSame(oldItem, newItem)
                }
            }

            return oldItem == newItem
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            if (oldItem.javaClass == newItem.javaClass) {
                classDiffMap[oldItem.javaClass]?.let {
                    return it.areContentsTheSame(oldItem, newItem)
                }
            }

            return true
        }

        override fun getChangePayload(oldItem: Any, newItem: Any): Any? {
            if (oldItem.javaClass == newItem.javaClass) {
                return classDiffMap[oldItem.javaClass]?.getChangePayload(oldItem, newItem)
            }
            return null
        }
    }
}
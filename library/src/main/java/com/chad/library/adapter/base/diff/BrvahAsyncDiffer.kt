package com.chad.library.adapter.base.diff

import android.os.Handler
import android.os.Looper
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DiffUtil.DiffResult
import androidx.recyclerview.widget.ListUpdateCallback
import com.chad.library.adapter.base.BaseQuickAdapter
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.Executor

class BrvahAsyncDiffer<T>(private val adapter: BaseQuickAdapter<T, *>,
                          private val config: BrvahAsyncDifferConfig<T>) : DifferImp<T> {
    private val mUpdateCallback: ListUpdateCallback = BrvahListUpdateCallback(adapter)
    private var mMainThreadExecutor: Executor

    private class MainThreadExecutor internal constructor() : Executor {
        val mHandler = Handler(Looper.getMainLooper())
        override fun execute(command: Runnable) {
            mHandler.post(command)
        }
    }

    private val sMainThreadExecutor: Executor = MainThreadExecutor()

    init {
        mMainThreadExecutor = config.mainThreadExecutor ?: sMainThreadExecutor
    }

    private val mListeners: MutableList<ListChangeListener<T>> = CopyOnWriteArrayList()

    private var mMaxScheduledGeneration = 0

    fun addData(index: Int, data: T) {
        val previousList: List<T> = adapter.data
        adapter.data.add(index, data)
        

        mUpdateCallback.onInserted(index, 1)
        onCurrentListChanged(previousList, null)
    }

    fun addData(data: T) {
        val previousList: List<T> = adapter.data
        adapter.data.add(data)

        mUpdateCallback.onInserted(previousList.size, 1)
        onCurrentListChanged(previousList, null)
    }

    fun addList(list: List<T>?) {
        if (list == null) return
        val previousList: List<T> = adapter.data
        adapter.data.addAll(list)
        

        mUpdateCallback.onInserted(previousList.size, list.size)
        onCurrentListChanged(previousList, null)
    }

    
    fun changeData(index: Int, newData: T, payload: T?) {
        val previousList: List<T> = adapter.data
        adapter.data[index] = newData
        

        mUpdateCallback.onChanged(index, 1, payload)
        onCurrentListChanged(previousList, null)
    }

    
    fun removeAt(index: Int) {
        val previousList: List<T> = adapter.data
        adapter.data.removeAt(index)



        mUpdateCallback.onRemoved(index, 1)
        onCurrentListChanged(previousList, null)
    }

    fun remove(t: T) {
        val previousList: List<T> = adapter.data
        val index = adapter.data.indexOf(t)
        if (index == -1) return
        adapter.data.removeAt(index)



        mUpdateCallback.onRemoved(index, 1)
        onCurrentListChanged(previousList, null)
    }


    @JvmOverloads
    fun submitList(newList: MutableList<T>?, commitCallback: Runnable? = null) {
        
        val runGeneration: Int = ++mMaxScheduledGeneration
        if (newList === adapter.data) {
            
            commitCallback?.run()
            return
        }
        val oldList: List<T> = adapter.data
        
        if (newList == null) {
            val countRemoved: Int = adapter.data.size
            adapter.data = arrayListOf()
            
            mUpdateCallback.onRemoved(0, countRemoved)
            onCurrentListChanged(oldList, commitCallback)
            return
        }
        
        if (adapter.data.isEmpty()) {
            adapter.data = newList
            
            mUpdateCallback.onInserted(0, newList.size)
            onCurrentListChanged(oldList, commitCallback)
            return
        }

        config.backgroundThreadExecutor.execute {
            val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize(): Int {
                    return oldList.size
                }

                override fun getNewListSize(): Int {
                    return newList.size
                }

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val oldItem: T? = oldList[oldItemPosition]
                    val newItem: T? = newList[newItemPosition]
                    return if (oldItem != null && newItem != null) {
                        config.diffCallback.areItemsTheSame(oldItem, newItem)
                    } else oldItem == null && newItem == null
                    
                }

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val oldItem: T? = oldList[oldItemPosition]
                    val newItem: T? = newList[newItemPosition]
                    if (oldItem != null && newItem != null) {
                        return config.diffCallback.areContentsTheSame(oldItem, newItem)
                    }
                    if (oldItem == null && newItem == null) {
                        return true
                    }
                    throw AssertionError()
                }

                override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
                    val oldItem: T? = oldList[oldItemPosition]
                    val newItem: T? = newList[newItemPosition]
                    if (oldItem != null && newItem != null) {
                        return config.diffCallback.getChangePayload(oldItem, newItem)
                    }
                    throw AssertionError()
                }
            })
            mMainThreadExecutor.execute {
                if (mMaxScheduledGeneration == runGeneration) {
                    latchList(newList, result, commitCallback)
                }
            }
        }
    }

    private fun latchList(
            newList: MutableList<T>,
            diffResult: DiffResult,
            commitCallback: Runnable?) {
        val previousList: List<T> = adapter.data
        adapter.data = newList

        diffResult.dispatchUpdatesTo(mUpdateCallback)
        onCurrentListChanged(previousList, commitCallback)
    }

    private fun onCurrentListChanged(previousList: List<T>,
                                     commitCallback: Runnable?) {
        for (listener in mListeners) {
            listener.onCurrentListChanged(previousList, adapter.data)
        }
        commitCallback?.run()
    }

    
    override fun addListListener(listener: ListChangeListener<T>) {
        mListeners.add(listener)
    }

    
    fun removeListListener(listener: ListChangeListener<T>) {
        mListeners.remove(listener)
    }

    fun clearAllListListener() {
        mListeners.clear()
    }
}
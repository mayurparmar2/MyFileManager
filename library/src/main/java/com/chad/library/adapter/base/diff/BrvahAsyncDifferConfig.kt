package com.chad.library.adapter.base.diff

import androidx.annotation.RestrictTo
import androidx.recyclerview.widget.DiffUtil
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class BrvahAsyncDifferConfig<T>(
        @RestrictTo(RestrictTo.Scope.LIBRARY)
        val mainThreadExecutor: Executor?,
        val backgroundThreadExecutor: Executor,
        val diffCallback: DiffUtil.ItemCallback<T>) {

    
    class Builder<T>(private val mDiffCallback: DiffUtil.ItemCallback<T>) {
        private var mMainThreadExecutor: Executor? = null
        private var mBackgroundThreadExecutor: Executor? = null
        
        fun setMainThreadExecutor(executor: Executor?): Builder<T> {
            mMainThreadExecutor = executor
            return this
        }

        
        fun setBackgroundThreadExecutor(executor: Executor?): Builder<T> {
            mBackgroundThreadExecutor = executor
            return this
        }

        
        fun build(): BrvahAsyncDifferConfig<T> {
            if (mBackgroundThreadExecutor == null) {
                synchronized(sExecutorLock) {
                    if (sDiffExecutor == null) {
                        sDiffExecutor = Executors.newFixedThreadPool(2)
                    }
                }
                mBackgroundThreadExecutor = sDiffExecutor
            }
            return BrvahAsyncDifferConfig(
                    mMainThreadExecutor,
                    mBackgroundThreadExecutor!!,
                    mDiffCallback)
        }

        companion object {
            
            private val sExecutorLock = Any()
            private var sDiffExecutor: Executor? = null
        }

    }
}
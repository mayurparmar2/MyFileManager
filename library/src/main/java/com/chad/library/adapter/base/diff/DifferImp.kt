package com.chad.library.adapter.base.diff

interface DifferImp<T> {
    fun addListListener(listChangeListener: ListChangeListener<T>)
}

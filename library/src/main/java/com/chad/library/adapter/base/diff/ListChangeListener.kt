package com.chad.library.adapter.base.diff

interface ListChangeListener<T> {
    fun onCurrentListChanged(previousList: List<T>, currentList: List<T>)
}

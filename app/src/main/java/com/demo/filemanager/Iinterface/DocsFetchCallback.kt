package com.demo.filemanager.Iinterface

interface DocsFetchCallback<T> {
    fun onSuccess(data: T)
    fun onFailure(exception: Exception)
}

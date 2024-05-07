package com.demo.filemanager.Iinterface

import com.demo.filemanager.DTO.File_DTO

class CallbackFav private constructor() {
    var state: ArrayList<File_DTO>? = null
        private set
    private var mListener: OnCustomStateListener? = null

    interface OnCustomStateListener {
        fun stateChanged()
    }

    fun setListener(onCustomStateListener: OnCustomStateListener?) {
        mListener = onCustomStateListener
    }

    fun changeState(arrayList: ArrayList<File_DTO>?) {
        if (mListener != null) {
            state = arrayList
            notifyStateChange()
        }
    }

    private fun notifyStateChange() {
        mListener!!.stateChanged()
    }

    companion object {
        private var mInstance: CallbackFav? = null
        val instance: CallbackFav?
            get() {
                if (mInstance == null) {
                    mInstance = CallbackFav()
                }
                return mInstance
            }
    }
}

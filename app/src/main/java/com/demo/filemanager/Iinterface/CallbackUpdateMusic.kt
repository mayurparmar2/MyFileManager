package com.demo.filemanager.Iinterface

class CallbackUpdateMusic private constructor() {
    private var stateListen: OncustomStateListen? = null

    interface OncustomStateListen {
        fun statechange()
    }

    fun setStateListen(oncustomStateListen: OncustomStateListen?) {
        stateListen = oncustomStateListen
    }

    fun change() {
        notidatachange()
    }

    protected fun notidatachange() {
        stateListen!!.statechange()
    }

    companion object {
        private var callbackupdate: CallbackUpdateMusic? = null
        val instance: CallbackUpdateMusic?
            get() {
                if (callbackupdate == null) {
                    callbackupdate = CallbackUpdateMusic()
                }
                return callbackupdate
            }
    }
}

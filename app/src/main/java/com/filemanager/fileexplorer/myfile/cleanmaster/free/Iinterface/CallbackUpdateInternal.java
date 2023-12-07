package com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface;


public class CallbackUpdateInternal {
    private static CallbackUpdateInternal callbackupdate;
    private OncustomStateListen stateListen;


    public interface OncustomStateListen {
        void statechange();
    }

    private CallbackUpdateInternal() {
    }

    public static CallbackUpdateInternal getInstance() {
        if (callbackupdate == null) {
            callbackupdate = new CallbackUpdateInternal();
        }
        return callbackupdate;
    }

    public void setStateListen(OncustomStateListen oncustomStateListen) {
        this.stateListen = oncustomStateListen;
    }

    public void change() {
        notidatachange();
    }

    protected void notidatachange() {
        this.stateListen.statechange();
    }
}

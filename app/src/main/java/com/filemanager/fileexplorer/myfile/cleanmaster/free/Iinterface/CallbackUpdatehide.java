package com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface;


public class CallbackUpdatehide {
    private static CallbackUpdatehide callbackupdate;
    private OncustomStateListen stateListen;


    public interface OncustomStateListen {
        void statechange();
    }

    private CallbackUpdatehide() {
    }

    public static CallbackUpdatehide getInstance() {
        if (callbackupdate == null) {
            callbackupdate = new CallbackUpdatehide();
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

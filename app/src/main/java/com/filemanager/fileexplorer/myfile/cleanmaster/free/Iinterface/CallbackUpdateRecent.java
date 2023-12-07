package com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface;


public class CallbackUpdateRecent {
    private static CallbackUpdateRecent callbackupdate;
    private OncustomStateListen stateListen;


    public interface OncustomStateListen {
        void statechange();
    }

    private CallbackUpdateRecent() {
    }

    public static CallbackUpdateRecent getInstance() {
        if (callbackupdate == null) {
            callbackupdate = new CallbackUpdateRecent();
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

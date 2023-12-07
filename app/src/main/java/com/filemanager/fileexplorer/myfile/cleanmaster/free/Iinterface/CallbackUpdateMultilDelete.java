package com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface;


public class CallbackUpdateMultilDelete {
    private static CallbackUpdateMultilDelete callbackupdate;
    private OncustomStateListen stateListen;


    public interface OncustomStateListen {
        void statechange();
    }

    private CallbackUpdateMultilDelete() {
    }

    public static CallbackUpdateMultilDelete getInstance() {
        if (callbackupdate == null) {
            callbackupdate = new CallbackUpdateMultilDelete();
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

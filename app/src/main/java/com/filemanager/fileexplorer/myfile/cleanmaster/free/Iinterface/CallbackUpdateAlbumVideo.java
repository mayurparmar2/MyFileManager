package com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface;


public class CallbackUpdateAlbumVideo {
    private static CallbackUpdateAlbumVideo callbackupdate;
    private OncustomStateListen stateListen;


    public interface OncustomStateListen {
        void statechange();
    }

    private CallbackUpdateAlbumVideo() {
    }

    public static CallbackUpdateAlbumVideo getInstance() {
        if (callbackupdate == null) {
            callbackupdate = new CallbackUpdateAlbumVideo();
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

package com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface;


public class CallbackDelete {
    private static CallbackDelete mInstance;
    private String filename;
    private OnCustomStateListener mListener;
    private String path;


    public interface OnCustomStateListener {
        void stateChanged();
    }

    private CallbackDelete() {
    }

    public static CallbackDelete getInstance() {
        if (mInstance == null) {
            mInstance = new CallbackDelete();
        }
        return mInstance;
    }

    public void setListener(OnCustomStateListener onCustomStateListener) {
        this.mListener = onCustomStateListener;
    }

    public void changeState(String str, String str2) {
        if (this.mListener != null) {
            this.path = str;
            this.filename = str2;
            notifyStateChange();
        }
    }

    private void notifyStateChange() {
        this.mListener.stateChanged();
    }

    public String getState() {
        return this.path;
    }

    public String getStringfilename() {
        return this.filename;
    }
}

package com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface;

import com.filemanager.fileexplorer.myfile.cleanmaster.free.DTO.ItemTest;

import java.util.ArrayList;


public class CallbackNewFile {
    private static CallbackNewFile mInstance;
    private ArrayList<ItemTest> file_dtos;
    private OnCustomStateListener mListener;


    public interface OnCustomStateListener {
        void stateChanged();
    }

    private CallbackNewFile() {
    }

    public static CallbackNewFile getInstance() {
        if (mInstance == null) {
            mInstance = new CallbackNewFile();
        }
        return mInstance;
    }

    public void setListener(OnCustomStateListener onCustomStateListener) {
        this.mListener = onCustomStateListener;
    }

    public void changeState(ArrayList<ItemTest> arrayList) {
        if (this.mListener != null) {
            this.file_dtos = arrayList;
            notifyStateChange();
        }
    }

    private void notifyStateChange() {
        this.mListener.stateChanged();
    }

    public ArrayList<ItemTest> getState() {
        return this.file_dtos;
    }
}

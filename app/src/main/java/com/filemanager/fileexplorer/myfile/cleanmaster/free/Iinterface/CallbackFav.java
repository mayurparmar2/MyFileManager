package com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface;

import com.filemanager.fileexplorer.myfile.cleanmaster.free.DTO.File_DTO;

import java.util.ArrayList;


public class CallbackFav {
    private static CallbackFav mInstance;
    private ArrayList<File_DTO> file_dtos;
    private OnCustomStateListener mListener;


    public interface OnCustomStateListener {
        void stateChanged();
    }

    private CallbackFav() {
    }

    public static CallbackFav getInstance() {
        if (mInstance == null) {
            mInstance = new CallbackFav();
        }
        return mInstance;
    }

    public void setListener(OnCustomStateListener onCustomStateListener) {
        this.mListener = onCustomStateListener;
    }

    public void changeState(ArrayList<File_DTO> arrayList) {
        if (this.mListener != null) {
            this.file_dtos = arrayList;
            notifyStateChange();
        }
    }

    private void notifyStateChange() {
        this.mListener.stateChanged();
    }

    public ArrayList<File_DTO> getState() {
        return this.file_dtos;
    }
}

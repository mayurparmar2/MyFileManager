package com.filemanager.fileexplorer.myfile.cleanmaster.free.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Activity.ResulfActivity;
import com.demo.example.R;

import java.io.File;


public class FilesFragment extends Fragment {
    static final int APP_STORAGE_ACCESS_REQUEST_CODE = 501;
    private LinearLayout linearLayout_internal;
    private LinearLayout linearLayout_sdcard;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getArguments();
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_files, viewGroup, false);
        this.linearLayout_sdcard = (LinearLayout) inflate.findViewById(R.id.l_sdcard);
        this.linearLayout_internal = (LinearLayout) inflate.findViewById(R.id.l_internal);
        if (!externalMemoryAvailable()) {
            this.linearLayout_sdcard.setVisibility(View.GONE);
        } else {
            this.linearLayout_sdcard.setVisibility(View.VISIBLE);
        }
        this.linearLayout_internal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FilesFragment.this.getContext(), ResulfActivity.class);
                intent.putExtra("nameitem", "Instorage");
                FilesFragment.this.getActivity().startActivity(intent);
                Animatoo.animateSwipeLeft(FilesFragment.this.getContext());
            }
        });
        this.linearLayout_sdcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FilesFragment.this.getContext(), ResulfActivity.class);
                intent.putExtra("nameitem", "SDcard");
                FilesFragment.this.getActivity().startActivity(intent);
                Animatoo.animateSwipeLeft(FilesFragment.this.getContext());
            }
        });
        return inflate;
    }

    public boolean externalMemoryAvailable() {
        File[] externalFilesDirs = ContextCompat.getExternalFilesDirs(getActivity(), null);
        if (externalFilesDirs.length <= 1 || externalFilesDirs[0] == null || externalFilesDirs[1] == null) {
            Log.d("TAG", "externalMemoryAvailable: fal");
            return false;
        }
        return true;
    }
}

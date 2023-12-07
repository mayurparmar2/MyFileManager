package com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;


public class MemoryBoosterAdapter extends FragmentStateAdapter {
    private static final String ARG_PARAM1 = "param1";
    private final List<Fragment> mFragmentList;
    private String[] title;

    public MemoryBoosterAdapter(FragmentManager fragmentManager, Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
        this.mFragmentList = new ArrayList();
    }

    @Override
    public Fragment createFragment(int i) {
        return this.mFragmentList.get(i);
    }

    @Override
    public int getItemCount() {
        return this.mFragmentList.size();
    }

    public void addfr(Fragment fragment) {
        this.mFragmentList.add(fragment);
    }
}

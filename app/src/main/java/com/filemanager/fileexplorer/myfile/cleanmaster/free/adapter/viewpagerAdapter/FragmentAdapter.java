package com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter.viewpagerAdapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;


public class FragmentAdapter extends FragmentStateAdapter {
    private final List<Fragment> mFragmentList;

    public FragmentAdapter(FragmentManager fragmentManager, Lifecycle lifecycle) {
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

    public void addFrag(Fragment fragment) {
        this.mFragmentList.add(fragment);
    }
}

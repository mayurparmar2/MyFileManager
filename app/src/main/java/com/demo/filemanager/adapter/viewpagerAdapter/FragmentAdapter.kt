package com.demo.filemanager.adapter.viewpagerAdapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter


class FragmentAdapter(fragmentManager: FragmentManager?, lifecycle: Lifecycle?) : FragmentStateAdapter(fragmentManager!!, lifecycle!!) {
    private val mFragmentList: MutableList<Fragment>

    init {
        mFragmentList = ArrayList<Fragment>()
    }

    override fun createFragment(i: Int): Fragment {
        return mFragmentList[i]
    }

    override fun getItemCount(): Int {
        return mFragmentList.size
    }

    fun addFrag(fragment: Fragment) {
        mFragmentList.add(fragment)
    }
}


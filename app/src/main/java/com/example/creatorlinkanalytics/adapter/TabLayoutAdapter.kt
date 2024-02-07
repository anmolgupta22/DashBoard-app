package com.example.creatorlinkanalytics.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter


class TabLayoutAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    private var fragmentList: List<Fragment> = ArrayList()
    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

    fun setData(fragmentList: List<Fragment>) {
        this.fragmentList = fragmentList
    }

    override fun getItemCount(): Int {
        return fragmentList.size
    }
}
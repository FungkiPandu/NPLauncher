package com.example.nplauncher.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.nplauncher.AppListFragment
import com.example.nplauncher.HomeFragment

class LauncherPagerAdapter(fm: FragmentManager) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                HomeFragment.newInstance()
            }
            else -> {
                AppListFragment.newInstance()
            }
        }
    }

    override fun getCount(): Int = 2
}
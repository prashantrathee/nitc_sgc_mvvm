package com.nitc.projectsgc.admin.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nitc.projectsgc.admin.AllMentorsFragment
import com.nitc.projectsgc.admin.AllStudentsFragment

class AdminDashboardPagerAdapter(fragmentManager: FragmentManager,lifecycle: Lifecycle):FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->AllStudentsFragment()
            1->AllMentorsFragment()
            else->throw IllegalArgumentException("Invalid position $position")
        }
    }
}
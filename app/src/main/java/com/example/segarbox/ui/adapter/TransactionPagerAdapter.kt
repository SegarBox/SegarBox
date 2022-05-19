package com.example.segarbox.ui.adapter

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.segarbox.ui.fragment.HistoryFragment
import com.example.segarbox.ui.fragment.InProgressFragment

class TransactionPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = InProgressFragment()
            1 -> fragment = HistoryFragment()
        }
        return fragment as Fragment
    }
}
package com.example.segarbox.ui.adapter

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.segarbox.data.local.static.Code
import com.example.segarbox.ui.fragment.HistoryFragment
import com.example.segarbox.ui.fragment.InProgressFragment

class TransactionPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {

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
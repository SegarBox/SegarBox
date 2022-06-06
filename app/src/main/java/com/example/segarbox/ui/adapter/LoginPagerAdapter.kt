package com.example.segarbox.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.segarbox.ui.fragment.LoginFragment
import com.example.segarbox.ui.fragment.RegisterFragment

class LoginPagerAdapter(activity: FragmentActivity): FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> LoginFragment()
            else -> RegisterFragment()
        }
    }

}
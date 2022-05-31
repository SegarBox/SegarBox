package com.example.segarbox.ui.activity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.segarbox.R
import com.example.segarbox.data.local.datastore.SettingPreferences
import com.example.segarbox.databinding.ActivityLoginBinding
import com.example.segarbox.ui.adapter.LoginPagerAdapter
import com.example.segarbox.ui.viewmodel.PrefViewModel
import com.example.segarbox.ui.viewmodel.PrefViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!
    private val prefViewModel by viewModels<PrefViewModel> {
        PrefViewModelFactory.getInstance(SettingPreferences.getInstance(dataStore))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        observeData()
        setTabWithViewPager()
        playAnimation()
    }

    private fun setTabWithViewPager() {
        val loginPagerAdapter = LoginPagerAdapter(this)
        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout

        viewPager.adapter = loginPagerAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = resources.getString(R.string.login)
                1 -> tab.text = getString(R.string.register)
            }
        }.attach()
    }

    private fun observeData() {
        prefViewModel.getTheme().observe(this) { isDarkMode ->
            when {
                isDarkMode -> {
                    binding.logo.setImageResource(R.drawable.logo_green)
                    binding.pat.setImageResource(R.drawable.pat_black)
                }
            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.logo, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
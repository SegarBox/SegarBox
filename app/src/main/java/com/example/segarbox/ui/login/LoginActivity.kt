package com.example.segarbox.ui.login

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.segarbox.R
import com.example.segarbox.databinding.ActivityLoginBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        observeData()
        setTabWithViewPager()
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
        viewModel.getTheme().observe(this) { event ->
            event.getContentIfNotHandled()?.let { isDarkMode ->
                when {
                    isDarkMode -> {
                        binding.logo.setImageResource(R.drawable.logo_green)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
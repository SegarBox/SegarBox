package com.example.segarbox.ui.activity

import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.annotation.StringRes
import com.example.segarbox.R
import com.example.segarbox.databinding.ActivityDetailBinding
import com.example.segarbox.databinding.ActivityHistoryBinding
import com.example.segarbox.helper.getColorFromAttr
import com.example.segarbox.helper.getHelperColor
import com.example.segarbox.ui.adapter.HistorySectionsPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class HistoryActivity : AppCompatActivity() {
    private var _binding: ActivityHistoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sectionsPagerAdapter = HistorySectionsPagerAdapter(this)
        binding.content.viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(binding.content.tabLayout, binding.content.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f

        init()
    }

    private fun init() {
        setToolbar()
        setBottomNav()
    }

    private fun setToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Transaction"
    }

    private fun setBottomNav() {
        val bottomNavColor = ColorStateList(
            arrayOf(
                intArrayOf(-android.R.attr.state_checked),
                intArrayOf(android.R.attr.state_checked),
            ),

            intArrayOf(
                this.getHelperColor(R.color.lightModeUnActiveButton),
                this.getColorFromAttr(com.google.android.material.R.attr.colorPrimary)
            )
        )

        binding.bottomNav.itemIconTintList = bottomNavColor
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_history_text_1,
            R.string.tab_history_text_2
        )
    }
}
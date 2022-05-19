package com.example.segarbox.ui.activity

import android.R.attr.state_checked
import android.R.attr.state_focused
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.widget.NestedScrollView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.segarbox.R
import com.example.segarbox.data.local.datastore.SettingPreferences
import com.example.segarbox.data.local.model.DummyModel
import com.example.segarbox.databinding.ActivityMainBinding
import com.example.segarbox.helper.getColorFromAttr
import com.example.segarbox.helper.getHelperColor
import com.example.segarbox.helper.getHelperDrawable
import com.example.segarbox.ui.adapter.DummyAdapter
import com.example.segarbox.ui.adapter.DummyAdapter2
import com.example.segarbox.ui.viewmodel.PrefViewModel
import com.example.segarbox.ui.viewmodel.PrefViewModelFactory
import com.google.android.material.R.attr.colorPrimary
import com.google.android.material.R.attr.colorSecondaryVariant
import kotlin.math.max
import kotlin.math.min

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private var ratio = 0F
    private var isThemeDarkMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()


    }

    private fun init() {
        setBottomNav()
    }

    private fun setBottomNav() {
        val bottomNavColor = ColorStateList(
            arrayOf(
                intArrayOf(-state_checked),
                intArrayOf(state_checked),
            ),

            intArrayOf(
                this.getHelperColor(R.color.lightModeUnActiveButton),
                this.getColorFromAttr(colorPrimary)
            )
        )

        binding.bottomNav.itemIconTintList = bottomNavColor

        val navView = binding.bottomNav
        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
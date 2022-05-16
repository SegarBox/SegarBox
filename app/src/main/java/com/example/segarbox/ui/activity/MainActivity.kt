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

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private var ratio = 0F
    private var isThemeDarkMode = false
    private val prefViewModel by viewModels<PrefViewModel> {
        PrefViewModelFactory.getInstance(SettingPreferences.getInstance(dataStore))
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        setToolbar()
        setBottomNav()
        observeData()
        setAdapter()
        binding.content.btnDetail.setOnClickListener(this)
        binding.content.btnDarkmode.setOnClickListener(this)
    }

    private fun setToolbar() {
        // Hooks
        val colorStrokeBelowRatio = ColorStateList(
            arrayOf(
                intArrayOf(-state_focused),
                intArrayOf(state_focused),
            ),

            intArrayOf(
                this.getColorFromAttr(colorSecondaryVariant),
                this.getColorFromAttr(colorSecondaryVariant)
            )
        )

        val colorStrokeAboveRatio = ColorStateList(
            arrayOf(
                intArrayOf(-state_focused),
                intArrayOf(state_focused),
            ),

            intArrayOf(
                this.getColorFromAttr(colorPrimary),
                this.getColorFromAttr(colorPrimary)
            )
        )

        // Initial Set Toolbar
        binding.toolbar.root.background.alpha = 0
        setSearchBar(colorStrokeBelowRatio, this.getColorFromAttr(colorSecondaryVariant))


        // On Scrolled
        binding.content.scrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, _ ->
            val headerHeight: Int = binding.content.header.height - binding.toolbar.root.height
            ratio = min(max(scrollY, 0), headerHeight).toFloat() / headerHeight
            val newAlpha = (ratio * 255).toInt()

            // Set Toolbar
            binding.toolbar.root.background.alpha = newAlpha

            // Set Toolbar Items
            if (ratio >= 0.65F) {
                setSearchBar(colorStrokeAboveRatio, this.getColorFromAttr(colorPrimary))
            } else {
                setSearchBar(colorStrokeBelowRatio, this.getColorFromAttr(colorSecondaryVariant))
            }

        })
    }

    private fun setSearchBar(colorStroke: ColorStateList, colorIcon: Int) {
        binding.toolbar.tiSearch.apply {
            setBoxStrokeColorStateList(colorStroke)
            defaultHintTextColor = colorStroke
            hintTextColor = colorStroke
            editText!!.setTextColor(colorIcon)

            var searchIcon = this@MainActivity.getHelperDrawable(R.drawable.ic_baseline_search_24)
            searchIcon = DrawableCompat.wrap(searchIcon)
            DrawableCompat.setTint(searchIcon, colorIcon)
            DrawableCompat.setTintMode(searchIcon, PorterDuff.Mode.SRC_IN)
            editText!!.setCompoundDrawablesWithIntrinsicBounds(searchIcon, null, null, null)

            binding.toolbar.ivCart.setColorFilter(colorIcon, PorterDuff.Mode.SRC_IN)

        }
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
    }

    private fun observeData() {
        prefViewModel.getTheme().observe(this) { isDarkMode ->
            when {
                isDarkMode -> {
                    isThemeDarkMode = true
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    binding.content.btnDarkmode.text = "Disable Dark Mode"
                }

                else -> {
                    isThemeDarkMode = false
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    binding.content.btnDarkmode.text = "Enable Dark Mode"
                }
            }
        }
    }

    private fun setAdapter() {
        val listItem = arrayListOf(
            DummyModel(),
            DummyModel(),
            DummyModel(),
            DummyModel(),
            DummyModel(),
            DummyModel(),
            DummyModel(),
            DummyModel()
        )
        val adapter1 = DummyAdapter()
        adapter1.submitList(listItem)

        val adapter2 = DummyAdapter2()
        adapter2.submitList(listItem)

        binding.content.rv1.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            binding.content.rv1.setHasFixedSize(true)
            binding.content.rv1.adapter = adapter1
        }

        binding.content.rv2.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            binding.content.rv2.setHasFixedSize(true)
            binding.content.rv2.adapter = adapter2
        }


    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_detail -> {
                startActivity(Intent(this, DetailActivity::class.java))
            }

            R.id.btn_darkmode -> {
                when {
                    isThemeDarkMode -> prefViewModel.saveTheme(false)
                    else -> prefViewModel.saveTheme(true)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val newAlpha = (ratio * 255).toInt()
        binding.toolbar.root.background.alpha = newAlpha
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
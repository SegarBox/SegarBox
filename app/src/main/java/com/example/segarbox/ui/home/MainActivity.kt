package com.example.segarbox.ui.home

import android.R.attr.state_checked
import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.core.data.RoomRepository
import com.example.core.data.source.local.datastore.SettingPreferences
import com.example.core.utils.getColorFromAttr
import com.example.core.utils.getHelperColor
import com.example.segarbox.R
import com.example.segarbox.databinding.ActivityMainBinding
import com.example.segarbox.ui.viewmodel.PrefViewModel
import com.example.segarbox.ui.viewmodel.PrefViewModelFactory
import com.example.segarbox.ui.viewmodel.RetrofitRoomViewModelFactory
import com.google.android.material.R.attr.colorPrimary

private val Context.dataStore by preferencesDataStore(name = "settings")
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel by viewModels<MainViewModel> {
        RetrofitRoomViewModelFactory.getInstance(RoomRepository(application),
            com.example.core.data.Repository())
    }
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
        setBottomNav()
        observeData()
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

    private fun observeData() {
        mainViewModel.getCityCount().observe(this) { dbCount ->
            if (dbCount == 0) {
                mainViewModel.getCityFromApi()
            }
        }
        mainViewModel.cityFromApi.observe(this) { cityResponse ->
            mainViewModel.insertCityToDB(cityResponse.rajaongkir.results)
        }

        prefViewModel.getIntro().observe(this) { isAlreadyIntro ->
            if (!isAlreadyIntro) {
                prefViewModel.saveIntro(true)
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
package com.example.segarbox.ui.activity

import android.R.attr.state_checked
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.segarbox.R
import com.example.segarbox.data.local.static.Code
import com.example.segarbox.data.repository.RetrofitRepository
import com.example.segarbox.data.repository.RoomRepository
import com.example.segarbox.databinding.ActivityMainBinding
import com.example.segarbox.helper.getColorFromAttr
import com.example.segarbox.helper.getHelperColor
import com.example.segarbox.ui.viewmodel.MainViewModel
import com.example.segarbox.ui.viewmodel.RetrofitRoomViewModelFactory
import com.google.android.material.R.attr.colorPrimary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel by viewModels<MainViewModel> {
        RetrofitRoomViewModelFactory.getInstance(RoomRepository(application), RetrofitRepository())
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

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
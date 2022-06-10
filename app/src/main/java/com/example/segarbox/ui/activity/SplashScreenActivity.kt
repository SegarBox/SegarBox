package com.example.segarbox.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.segarbox.R
import com.example.segarbox.data.local.datastore.SettingPreferences
import com.example.segarbox.databinding.ActivitySplashScreenBinding
import com.example.segarbox.ui.viewmodel.PrefViewModel
import com.example.segarbox.ui.viewmodel.PrefViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private var _binding: ActivitySplashScreenBinding? = null
    private val binding get() = _binding!!

    private var isThemeDarkMode = false
    private val prefViewModel by viewModels<PrefViewModel> {
        PrefViewModelFactory.getInstance(SettingPreferences.getInstance(dataStore))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init(){
        setNavigationBar()
        observeData()
    }

    private fun splashDelay(intent: Intent){
        val delay: Long = 4000
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(intent)
            finish()
        }, delay)
    }

    private fun setNavigationBar() {
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        window.navigationBarColor = Color.TRANSPARENT
    }

    private fun observeData() {
        prefViewModel.getTheme().observe(this) { isDarkMode ->
            when {
                isDarkMode -> {
                    isThemeDarkMode = true
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    binding.bg.setImageResource(R.color.brown_dark)
                    binding.splash.isVisible = false
                    binding.splashGreen.isVisible = true
                }

                else -> {
                    isThemeDarkMode = false
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        }

        prefViewModel.getIntro().observe(this) { isAlreadyIntro ->
            when {
                isAlreadyIntro -> splashDelay(Intent(this, MainActivity::class.java))
                else -> splashDelay(Intent(this, IntroActivity::class.java))
            }
        }
    }
}
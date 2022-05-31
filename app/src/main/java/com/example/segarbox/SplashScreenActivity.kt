package com.example.segarbox

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.segarbox.data.local.datastore.SettingPreferences
import com.example.segarbox.databinding.ActivitySplashScreenBinding
import com.example.segarbox.ui.activity.MainActivity
import com.example.segarbox.ui.viewmodel.PrefViewModel
import com.example.segarbox.ui.viewmodel.PrefViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
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
        observeData()

        supportActionBar?.hide()
        val delay: Long = 2000
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, delay)

    }

//        binding.bg.animate().translationY(-1600f).setDuration(1000).startDelay = 2000
//        binding.splash.animate().translationY(1400f).setDuration(1000).startDelay = 2000

//        binding.bg.animate().translationY(-2500F).setDuration (1000).setStartDelay(5000);
//        binding.pat.animate().translationY(-2500F).setDuration (1000).setStartDelay(5000);
//        binding.splash.animate().translationY(1400F).setDuration (3000).setStartDelay(5000);

    private fun observeData() {
        prefViewModel.getTheme().observe(this) { isDarkMode ->
            when {
                isDarkMode -> {
                    isThemeDarkMode = true
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    binding.pat.setImageResource(R.drawable.pat_black)
                }

                else -> {
                    isThemeDarkMode = false
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        }
    }
}
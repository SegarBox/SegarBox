package com.example.segarbox.ui.splash

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.core.data.source.local.datastore.SettingPreferences
import com.example.segarbox.R
import com.example.segarbox.databinding.ActivitySplashScreenBinding
import com.example.segarbox.ui.dev.DevActivity
import com.example.segarbox.ui.home.MainActivity
import com.example.segarbox.ui.intro.IntroActivity
import com.example.segarbox.ui.viewmodel.PrefViewModel
import com.example.segarbox.ui.viewmodel.PrefViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity(), View.OnClickListener {

    private var _binding: ActivitySplashScreenBinding? = null
    private val binding get() = _binding!!

    private val prefViewModel by viewModels<PrefViewModel> {
        PrefViewModelFactory.getInstance(SettingPreferences.getInstance(dataStore))
    }
    private val splashViewModel by viewModels<SplashViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        setNavigationBar()
        observeData()
        binding.root.setOnClickListener(this)
    }

    private fun splashDelay(intent: Intent) {
        val handler = Handler(Looper.getMainLooper())
        val delay: Long = 4000

        handler.postDelayed({
            splashViewModel.isDelay.observe(this) { isDelay ->
                if (isDelay) {
                    startActivity(intent)
                    finish()
                } else {
                    return@observe
                }
            }
        }, delay)

        splashViewModel.clickCount.observe(this) { clickCount ->
            if (clickCount == 3) {
                splashViewModel.setIsDelay(false)
                startActivity(Intent(this, DevActivity::class.java))
                finish()
            }
        }
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
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    binding.bg.setImageResource(R.color.brown_dark)
                    binding.splash.isVisible = false
                    binding.splashGreen.isVisible = true
                }

                else -> {
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.root -> splashViewModel.incrementClickCount()
        }
    }
}
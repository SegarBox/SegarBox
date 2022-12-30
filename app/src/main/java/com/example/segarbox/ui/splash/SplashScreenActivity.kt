package com.example.segarbox.ui.splash

import android.annotation.SuppressLint
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
import com.example.segarbox.R
import com.example.segarbox.databinding.ActivitySplashScreenBinding
import com.example.segarbox.ui.dev.DevActivity
import com.example.segarbox.ui.home.MainActivity
import com.example.segarbox.ui.intro.IntroActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity(), View.OnClickListener {

    private var _binding: ActivitySplashScreenBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SplashViewModel by viewModels()

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
            viewModel.isDelay.observe(this) { isDelay ->
                if (isDelay) {
                    startActivity(intent)
                    finish()
                } else {
                    return@observe
                }
            }
        }, delay)

        viewModel.clickCount.observe(this) { clickCount ->
            if (clickCount == 3) {
                viewModel.setIsDelay(false)
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
        viewModel.getTheme().observe(this) { event ->
            event.getContentIfNotHandled()?.let { isDarkMode ->
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
        }

        viewModel.getIntro().observe(this) { event ->
            event.getContentIfNotHandled()?.let { isAlreadyIntro ->
                when {
                    isAlreadyIntro -> splashDelay(Intent(this, MainActivity::class.java))
                    else -> splashDelay(Intent(this, IntroActivity::class.java))
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.root -> viewModel.incrementClickCount()
        }
    }
}
package com.example.bungkusyuk.ui.activity

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.WindowInsetsController
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import com.example.bungkusyuk.R
import com.example.bungkusyuk.databinding.ActivityDetailBinding
import kotlin.math.max
import kotlin.math.min

class DetailActivity : AppCompatActivity() {

    private var mInitialStatusBarColor = 0
    private var mFinalStatusBarColor = 0
    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding!!
    private var ratio = 0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init(){
        setToolbar()
    }

    private fun setToolbar(){
        // Initial Set Toolbar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.decorView.windowInsetsController?.setSystemBarsAppearance(0,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        }
        binding.toolbar.background.alpha = 0
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Cauliflower"

        mInitialStatusBarColor = Color.TRANSPARENT
        mFinalStatusBarColor = ContextCompat.getColor(this, R.color.white)

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        // On Scrolled
        binding.content.scrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, _ ->
            val headerHeight: Int = binding.toolbar.height
            ratio = min(max(scrollY, 0), headerHeight).toFloat() / headerHeight
            val newAlpha = (ratio * 255).toInt()

            // Set Toolbar
            binding.toolbar.background.alpha = newAlpha

            // Set Toolbar Items
            if (ratio >= 0.65F) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    window.decorView.windowInsetsController?.setSystemBarsAppearance(
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                    )
                }
//                binding.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.brown_med))

            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    window.decorView.windowInsetsController?.setSystemBarsAppearance(0,
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                    )
                }
//                binding.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
            }

        })

    }

    override fun onResume() {
        super.onResume()
        val newAlpha = (ratio * 255).toInt()
        binding.toolbar.background.alpha = newAlpha
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
package com.example.bungkusyuk.ui.activity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import com.example.bungkusyuk.R
import kotlin.math.max
import kotlin.math.min

class DetailActivity : AppCompatActivity() {

    private lateinit var mToolbar: androidx.appcompat.widget.Toolbar
    private lateinit var scrollView: NestedScrollView
    private var mInitialStatusBarColor = 0
    private var mFinalStatusBarColor = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        mToolbar = findViewById(R.id.toolbar)
        scrollView = findViewById(R.id.scrollView)

        mToolbar.background.alpha = 0
        setSupportActionBar(mToolbar)

        mInitialStatusBarColor = Color.TRANSPARENT
        mFinalStatusBarColor = ContextCompat.getColor(this, R.color.white)

        scrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, _ ->
            val headerHeight: Int = 100
            var ratio = 0f
            if (scrollY > 0 && headerHeight > 0) ratio =
                min(max(scrollY, 0), headerHeight).toFloat() / headerHeight
            val newAlpha = (ratio * 255).toInt()

            // Set Toolbar
            mToolbar.background.alpha = newAlpha

            // Set Toolbar Title
            if (ratio >= 0.65F) {
                mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.brown_med))
            } else {
                mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
            }

            // Set Status Bar
//            updateStatusBarColor(ratio)
        })
    }

    private fun updateStatusBarColor(scrollRatio: Float) {
        val r = interpolate(Color.red(mInitialStatusBarColor),
            Color.red(mFinalStatusBarColor),
            1 - scrollRatio)
        val g = interpolate(Color.green(mInitialStatusBarColor),
            Color.green(mFinalStatusBarColor),
            1 - scrollRatio)
        val b = interpolate(Color.blue(mInitialStatusBarColor),
            Color.blue(mFinalStatusBarColor),
            1 - scrollRatio)

        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        window.statusBarColor = Color.rgb(r, g, b)
    }

    private fun interpolate(from: Int, to: Int, param: Float): Int {
        return (from * param + to * (1 - param)).toInt()
    }
}
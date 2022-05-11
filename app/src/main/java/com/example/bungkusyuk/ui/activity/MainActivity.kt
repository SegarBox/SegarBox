package com.example.bungkusyuk.ui.activity

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import com.example.bungkusyuk.R
import com.example.bungkusyuk.databinding.ActivityMainBinding
import kotlin.math.max
import kotlin.math.min


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private var ratio = 0F


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        setToolbar()
        binding.btnDetail.setOnClickListener(this)
    }

    private fun setToolbar() {
        // Hooks
        val colorStrokeBelowRatio = ColorStateList(
            arrayOf(
                intArrayOf(-android.R.attr.state_focused),
                intArrayOf(android.R.attr.state_focused),
            ),

            intArrayOf(
                ContextCompat.getColor(this, R.color.white),
                ContextCompat.getColor(this, R.color.white)
            )
        )

        val colorStrokeAboveRatio = ColorStateList(
            arrayOf(
                intArrayOf(-android.R.attr.state_focused),
                intArrayOf(android.R.attr.state_focused),
            ),

            intArrayOf(
                ContextCompat.getColor(this, R.color.purple_700),
                ContextCompat.getColor(this, R.color.purple_700)
            )
        )

        // Initial Set Toolbar
        binding.toolbar.background.alpha = 0
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.tiSearch.apply {
            setBoxStrokeColorStateList(colorStrokeBelowRatio)
            defaultHintTextColor = colorStrokeBelowRatio
            hintTextColor = colorStrokeBelowRatio
            editText!!.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.white))
        }

        // On Scrolled
        binding.scrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, _ ->
            val headerHeight: Int = binding.header.height - binding.toolbar.height
            ratio = min(max(scrollY, 0), headerHeight).toFloat() / headerHeight
            val newAlpha = (ratio * 255).toInt()

            // Set Toolbar
            binding.tvRatio.text = newAlpha.toString()
            binding.toolbar.background.alpha = newAlpha

            // Set Toolbar Items
            if (ratio >= 0.65F) {
                binding.tiSearch.apply {
                    setBoxStrokeColorStateList(colorStrokeAboveRatio)
                    defaultHintTextColor = colorStrokeAboveRatio
                    hintTextColor = colorStrokeAboveRatio
                    editText!!.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.purple_500))
                }
            } else {
                binding.tiSearch.apply {
                    setBoxStrokeColorStateList(colorStrokeBelowRatio)
                    defaultHintTextColor = colorStrokeBelowRatio
                    hintTextColor = colorStrokeBelowRatio
                    editText!!.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.white))
                }
            }

        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_detail -> {
                startActivity(Intent(this, DetailActivity::class.java))
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val newAlpha = (ratio * 255).toInt()
        binding.toolbar.background.alpha = newAlpha

        if (ratio >= 0.65F) {
            binding.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.purple_700))
        } else {
            binding.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
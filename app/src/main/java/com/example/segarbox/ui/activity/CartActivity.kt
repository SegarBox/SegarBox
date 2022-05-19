package com.example.segarbox.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.core.view.isVisible
import com.example.segarbox.R
import com.example.segarbox.databinding.ActivityCartBinding
import com.example.segarbox.databinding.ActivityDetailBinding

class CartActivity : AppCompatActivity(), View.OnClickListener {

    private var _binding: ActivityCartBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        setToolbar()
        binding.toolbar.ivBack.setOnClickListener(this)
    }

    private fun setToolbar() {
        binding.toolbar.apply {
            ivBack.isVisible = true
            tvTitle.text = "Cart"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.iv_back -> {
                finish()
            }
        }
    }

}
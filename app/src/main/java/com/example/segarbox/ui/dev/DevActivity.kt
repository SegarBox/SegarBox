package com.example.segarbox.ui.dev

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.core.utils.DYNAMIC_BASE_URL
import com.example.segarbox.R
import com.example.segarbox.databinding.ActivityDevBinding
import com.example.segarbox.ui.home.MainActivity


class DevActivity : AppCompatActivity(), View.OnClickListener {

    private var _binding: ActivityDevBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDevBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        binding.btnSaveUrl.setOnClickListener(this)
        binding.btnMidtrans.setOnClickListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_save_url -> {
                DYNAMIC_BASE_URL = binding.etBaseUrl.text.toString()
                startActivity(Intent(this, MainActivity::class.java))
            }

            R.id.btn_midtrans -> {
                startActivity(Intent(this, MidtransActivity::class.java))
            }
        }
    }
}
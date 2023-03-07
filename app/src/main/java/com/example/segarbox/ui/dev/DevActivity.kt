package com.example.segarbox.ui.dev

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.core.utils.DYNAMIC_BASE_URL
import com.example.segarbox.R
import com.example.segarbox.databinding.ActivityDevBinding
import com.example.segarbox.ui.home.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DevActivity : AppCompatActivity(), View.OnClickListener {

    private var _binding: ActivityDevBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DevViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDevBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        binding.btnSaveUrl.setOnClickListener(this)
        binding.btnMidtrans.setOnClickListener(this)
        binding.btnGetMidtransStatus.setOnClickListener(this)
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

            R.id.btn_getMidtransStatus -> {
                viewModel.getMidtransStatus("SegarBox-167808768020").observe(this) { event ->
                    event.getContentIfNotHandled()?.let { resource ->
                        Log.e("HASIL", resource.toString())
                    }
                }
            }
        }
    }
}
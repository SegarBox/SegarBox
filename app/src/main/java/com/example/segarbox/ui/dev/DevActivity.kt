package com.example.segarbox.ui.dev

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.segarbox.R
import com.example.segarbox.databinding.ActivityDevBinding

class DevActivity : AppCompatActivity() {

    private var _binding: ActivityDevBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDevBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
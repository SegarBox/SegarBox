package com.example.segarbox.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.segarbox.R
import com.example.segarbox.databinding.ActivityDetailBinding
import com.example.segarbox.databinding.ActivityInvoiceBinding

class InvoiceActivity : AppCompatActivity() {

    private var _binding: ActivityInvoiceBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityInvoiceBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


}
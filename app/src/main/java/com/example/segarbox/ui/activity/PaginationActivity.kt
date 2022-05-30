package com.example.segarbox.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.segarbox.R
import com.example.segarbox.data.local.static.Code
import com.example.segarbox.databinding.ActivityPaginationBinding

class PaginationActivity : AppCompatActivity() {

    private var _binding: ActivityPaginationBinding? = null
    private val binding get() = _binding!!
    private var filter: String? = null
    private var filterValue: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPaginationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        getPaginationIntent()
        setToolbar()
        filterValue?.let {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setToolbar() {
        binding.toolbar.apply {
            tvTitle.text = "Pagination"
            ivBack.isVisible = true
        }
    }

    private fun getPaginationIntent() {
        filter = intent.getStringExtra(Code.KEY_FILTER)
        filterValue = intent.getStringExtra(Code.KEY_FILTER_VALUE)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
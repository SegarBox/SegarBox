package com.example.segarbox.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.segarbox.R
import com.example.segarbox.data.local.model.DummyModel
import com.example.segarbox.databinding.ActivityCartBinding
import com.example.segarbox.databinding.ActivityRatingBinding
import com.example.segarbox.ui.adapter.DummyAdapter
import com.example.segarbox.ui.adapter.DummyAdapter2
import com.example.segarbox.ui.adapter.DummyAdapterRating

class RatingActivity : AppCompatActivity(), View.OnClickListener {

    private var _binding: ActivityRatingBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRatingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        setToolbar()
        setAdapter()
        binding.toolbar.ivBack.setOnClickListener(this)
    }

    private fun setToolbar() {
        binding.toolbar.apply {
            ivBack.isVisible = true
            tvTitle.text = "Rating"
        }
    }

    private fun setAdapter() {
        val listItem = arrayListOf(
            DummyModel(),
            DummyModel(),
            DummyModel(),
            DummyModel(),
            DummyModel(),
            DummyModel(),
            DummyModel(),
            DummyModel()
        )
        val ratingAdapter = DummyAdapterRating()
        ratingAdapter.submitList(listItem)

        binding.content.rvCart.apply {
            layoutManager = LinearLayoutManager(this@RatingActivity, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = ratingAdapter
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
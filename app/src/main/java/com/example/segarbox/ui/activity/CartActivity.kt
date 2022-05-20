package com.example.segarbox.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.segarbox.R
import com.example.segarbox.data.local.model.DummyModelCart
import com.example.segarbox.databinding.ActivityCartBinding
import com.example.segarbox.ui.adapter.DummyAdapterCart

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
        setAdapter()
        setupView()
        binding.toolbar.ivBack.setOnClickListener(this)
    }

    private fun setupView(){
        binding.bottomPaymentInfo.tvButton.text = "Confirm"
    }

    private fun setAdapter() {
        val listItem = arrayListOf(
            DummyModelCart(),
            DummyModelCart(),
            DummyModelCart(),
            DummyModelCart(),
            DummyModelCart(),
            DummyModelCart()
        )

        val adapterCart = DummyAdapterCart()
        adapterCart.submitList(listItem)

        binding.content.rvCart.apply {
            layoutManager = LinearLayoutManager(this@CartActivity, LinearLayoutManager.VERTICAL, false)
            binding.content.rvCart.setHasFixedSize(true)
            binding.content.rvCart.adapter = adapterCart
        }
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
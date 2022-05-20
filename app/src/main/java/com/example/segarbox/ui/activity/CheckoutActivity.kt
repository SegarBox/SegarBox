package com.example.segarbox.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.segarbox.R
import com.example.segarbox.data.local.model.DummyModelCart
import com.example.segarbox.databinding.ActivityCheckoutBinding
import com.example.segarbox.ui.adapter.DummyAdapterCheckout

class CheckoutActivity : AppCompatActivity(), View.OnClickListener {

    private var _binding: ActivityCheckoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCheckoutBinding.inflate(layoutInflater)
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
            tvTitle.text = getString(R.string.checkout)
            ivBack.isVisible = true
        }
    }

    private fun setAdapter() {
        val listItem = arrayListOf(
            DummyModelCart(),
            DummyModelCart(),
            DummyModelCart(),
            DummyModelCart(),
            DummyModelCart(),
            DummyModelCart(),
            DummyModelCart()
        )

        val adapterCheckout = DummyAdapterCheckout()
        adapterCheckout.submitList(listItem)

        binding.content.rvCheckoutItem.apply {
            layoutManager = LinearLayoutManager(this@CheckoutActivity)
            adapter = adapterCheckout
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.iv_back -> finish()
        }
    }
}
package com.example.segarbox.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.datastore.preferences.preferencesDataStore
import com.example.segarbox.R
import com.example.segarbox.data.local.datastore.SettingPreferences
import com.example.segarbox.data.local.static.Code
import com.example.segarbox.data.remote.response.ProductItem
import com.example.segarbox.databinding.ActivityDetailBinding
import com.example.segarbox.helper.formatQty
import com.example.segarbox.helper.formatToRupiah
import com.example.segarbox.ui.viewmodel.PrefViewModel
import com.example.segarbox.ui.viewmodel.PrefViewModelFactory

class DetailActivity : AppCompatActivity(), View.OnClickListener {

    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding!!
    private var productItem: ProductItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        getIntentDetail()
        setToolbar()
        setDetail()
        binding.toolbar.ivBack.setOnClickListener(this)
        binding.toolbar.ivCart.setOnClickListener(this)
    }

    private fun getIntentDetail() {
        productItem = intent.getParcelableExtra(Code.KEY_DETAIL_VALUE)
    }

    private fun setToolbar() {
        binding.toolbar.apply {
            ivBack.isVisible = true
            ivCart.isVisible = true
            tvTitle.text = productItem?.label ?: ""
        }
    }

    private fun setDetail() {
        binding.content.apply {
            productItem?.let { item ->
                tvItemName.text = item.label
                tvQuantity.text = item.qty.formatQty(this@DetailActivity)
                tvPrice.text = item.price.formatToRupiah()
                tvItemDescription.text = item.detail
            }
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
            R.id.iv_cart -> {
                startActivity(Intent(this, CartActivity::class.java))
            }
        }
    }

}
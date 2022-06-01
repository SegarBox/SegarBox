package com.example.segarbox.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.datastore.preferences.preferencesDataStore
import com.bumptech.glide.Glide
import com.example.segarbox.R
import com.example.segarbox.data.local.datastore.SettingPreferences
import com.example.segarbox.data.local.static.Code
import com.example.segarbox.data.remote.response.ProductItem
import com.example.segarbox.data.repository.RetrofitRepository
import com.example.segarbox.databinding.ActivityDetailBinding
import com.example.segarbox.helper.formatProductSize
import com.example.segarbox.helper.formatToRupiah
import com.example.segarbox.helper.tokenFormat
import com.example.segarbox.ui.viewmodel.DetailViewModel
import com.example.segarbox.ui.viewmodel.PrefViewModel
import com.example.segarbox.ui.viewmodel.PrefViewModelFactory
import com.example.segarbox.ui.viewmodel.RetrofitViewModelFactory

private val Context.dataStore by preferencesDataStore(name = "settings")

class DetailActivity : AppCompatActivity(), View.OnClickListener {

    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding!!
    private var productItem: ProductItem? = null
    private var quantity = 0
    private val detailViewModel by viewModels<DetailViewModel> {
        RetrofitViewModelFactory.getInstance(RetrofitRepository())
    }
    private val prefViewModel by viewModels<PrefViewModel> {
        PrefViewModelFactory.getInstance(SettingPreferences.getInstance(dataStore))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        getIntentDetail()
        setToolbar()
        setDetail(productItem)
        observeData()
        binding.toolbar.ivBack.setOnClickListener(this)
        binding.toolbar.ivCart.setOnClickListener(this)
        binding.content.counter.ivAdd.setOnClickListener(this)
        binding.content.counter.ivRemove.setOnClickListener(this)
    }

    private fun getIntentDetail() {
        productItem = intent.getParcelableExtra(Code.KEY_DETAIL_VALUE)
        val productQtyFromCart = intent?.getIntExtra(Code.KEY_PRODUCT_QTY, 0)
        productQtyFromCart?.let {
            detailViewModel.saveQuantity(it)
        }
    }

    private fun setToolbar() {
        binding.toolbar.apply {
            ivBack.isVisible = true
            ivCart.isVisible = true
            tvTitle.text = productItem?.label ?: ""
        }
    }

    private fun setDetail(item: ProductItem?) {
        binding.content.apply {
            item?.let { it ->
                Glide.with(this@DetailActivity)
                    .load(it.image)
                    .into(ivItem)
                tvItemName.text = it.label
                tvSize.text = it.size.formatProductSize(this@DetailActivity)
                tvPrice.text = it.price.formatToRupiah()
                tvItemDescription.text = it.detail
            }
        }
    }

    private fun observeData() {

        productItem?.let {
            detailViewModel.getProductById(it.id)
        }

        detailViewModel.productById.observe(this) { productById ->
            productById.data?.let {
                binding.content.tvStock.text = getString(R.string.stock, it.qty.toString())
                binding.content.counter.ivAdd.isEnabled = quantity < it.qty
            }
        }

        detailViewModel.quantity.observe(this) { qty ->
            quantity = qty
            binding.content.counter.apply {
                tvCount.text = qty.toString()
                ivRemove.isEnabled = qty != 0
            }

            binding.btnAddToCart.isEnabled = qty != 0
        }

        prefViewModel.getToken().observe(this) { token ->

            binding.btnAddToCart.setOnClickListener {
                if (token.isEmpty()) {
                    startActivity(Intent(this, LoginActivity::class.java))
                } else {
                    productItem?.let {
                        detailViewModel.addCart(token = token.tokenFormat(),
                            productId = it.id,
                            productQty = quantity)
                    }
                }
            }
        }

        detailViewModel.addCartResponse.observe(this) { addCartResponse ->
            if (addCartResponse.message != null) {
                Toast.makeText(this, addCartResponse.message, Toast.LENGTH_SHORT).show()
            } else {
                addCartResponse.info?.let {
                    Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                }
            }
        }

        detailViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.isVisible = isLoading
            binding.content.counter.apply {
                ivRemove.isClickable = !isLoading
                ivAdd.isClickable = !isLoading
            }
        }


    }

    override fun onNewIntent(intent: Intent?) {
        productItem = intent?.getParcelableExtra(Code.KEY_DETAIL_VALUE)
        val productQtyFromCart = intent?.getIntExtra(Code.KEY_PRODUCT_QTY, 0)
        productItem?.let {
            binding.toolbar.tvTitle.text = it.label
            detailViewModel.getProductById(it.id)
            setDetail(it)
        }
        productQtyFromCart?.let {
            detailViewModel.saveQuantity(it)
        }
        super.onNewIntent(intent)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> {
                finish()
            }
            R.id.iv_cart -> {
                startActivity(Intent(this, CartActivity::class.java))
            }
            R.id.iv_add -> {
                productItem?.let {
                    detailViewModel.getProductById(it.id)
                }
                detailViewModel.saveQuantity(quantity + 1)
            }
            R.id.iv_remove -> {
                productItem?.let {
                    detailViewModel.getProductById(it.id)
                }
                detailViewModel.saveQuantity(quantity - 1)
            }
        }
    }

}
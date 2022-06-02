package com.example.segarbox.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import com.example.segarbox.data.remote.response.UserCartItem
import com.example.segarbox.data.repository.RetrofitRepository
import com.example.segarbox.databinding.ActivityDetailBinding
import com.example.segarbox.helper.*
import com.example.segarbox.ui.viewmodel.DetailViewModel
import com.example.segarbox.ui.viewmodel.PrefViewModel
import com.example.segarbox.ui.viewmodel.PrefViewModelFactory
import com.example.segarbox.ui.viewmodel.RetrofitViewModelFactory

private val Context.dataStore by preferencesDataStore(name = "settings")

class DetailActivity : AppCompatActivity(), View.OnClickListener {

    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding!!
    private var productItem: ProductItem? = null
    private var userCartItem: UserCartItem? = null
    private var fromActivity: String? = null
    private var token = ""
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
        // From Home
        productItem = intent.getParcelableExtra(Code.KEY_DETAIL_VALUE)

        // From Cart
        fromActivity = intent.getStringExtra(Code.KEY_FROM_ACTIVITY)

        fromActivity?.let {
            userCartItem = intent.getParcelableExtra(Code.KEY_USERCART_VALUE)
            userCartItem?.let { userCartItem ->
                productItem = userCartItem.product
                detailViewModel.saveQuantity(userCartItem.productQty)
            }
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
                if (it.qty < quantity) {
                    detailViewModel.saveQuantity(it.qty)
                }
                binding.content.tvStock.text = getString(R.string.stock, it.qty.toString())
                binding.content.counter.ivAdd.isEnabled = quantity < it.qty
            }
        }

        detailViewModel.quantity.observe(this) { qty ->
            quantity = qty
            binding.btnAddToCart.isEnabled = qty != 0
            binding.content.counter.apply {
                tvCount.text = qty.toString()
                ivRemove.isEnabled = qty != 0
            }

            // Set Button jika dari Cart
            fromActivity?.let {
                if (it == Code.CART_ACTIVITY) {
                    binding.btnAddToCart.isEnabled = true
                    if (qty > 0) {
                        binding.btnAddToCart.backgroundTintList = this.getColorStateListPrimary()
                        binding.btnAddToCart.text = getString(R.string.update_cart)
                    } else {
                        binding.btnAddToCart.backgroundTintList = this.getColorStateListRed()
                        binding.btnAddToCart.text = getString(R.string.delete_from_cart)
                    }
                }
            }
        }

        prefViewModel.getToken().observe(this) { token ->
            this.token = token

            // Menampilkan Badge
            if (token.isNotEmpty()) {
                detailViewModel.getUserCart(token.tokenFormat())
            }

            // Saat menekan button addToCart
            binding.btnAddToCart.setOnClickListener {
                if (token.isEmpty()) {
                    startActivity(Intent(this, LoginActivity::class.java))
                } else {

                    // Set Jika dari Cart atau tidak
                    if (fromActivity != null && fromActivity == Code.CART_ACTIVITY) {
                        // Jika quantity > 0
                        userCartItem?.let { userCartItem ->
                            if (quantity > 0) {
                                detailViewModel.updateUserCart(token.tokenFormat(),
                                    userCartItem.id,
                                    userCartItem.product.id,
                                    quantity,
                                    userCartItem.isChecked)
                            }
                            else {
                                detailViewModel.deleteUserCart(token.tokenFormat(), userCartItem.id)
                            }
                        }
                    } else {
                        productItem?.let {
                            detailViewModel.addCart(token = token.tokenFormat(),
                                productId = it.id,
                                productQty = quantity)
                        }
                    }

                }
            }
        }

        detailViewModel.addCartResponse.observe(this) { addCartResponse ->
            if (addCartResponse.message != null) {
                Toast.makeText(this, addCartResponse.message, Toast.LENGTH_SHORT).show()
            } else {
                if (token.isNotEmpty()) {
                    detailViewModel.getUserCart(token.tokenFormat())
                }
                addCartResponse.info?.let {
                    Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                }
            }
        }

        detailViewModel.updateUserCartResponse.observe(this) { updateCartResponse ->
            if (updateCartResponse.message != null ) {
                Toast.makeText(this, updateCartResponse.message, Toast.LENGTH_SHORT).show()
                productItem?.let {
                    detailViewModel.getProductById(it.id)
                }
            } else {
                updateCartResponse.info?.let {
                    Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, CartActivity::class.java))
                    finish()
                }
            }
        }

        detailViewModel.deleteUserCartResponse.observe(this) { deleteCartResponse ->
            if (deleteCartResponse.message != null ) {
                Toast.makeText(this, deleteCartResponse.message, Toast.LENGTH_SHORT).show()
            } else {
                deleteCartResponse.info?.let {
                    Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, CartActivity::class.java))
                    finish()
                }
            }
        }

        detailViewModel.userCart.observe(this) { userCartResponse ->
            userCartResponse.meta?.let { meta ->
                binding.toolbar.ivCart.badgeValue = meta.total
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
                finish()
            }
            R.id.iv_add -> {
                detailViewModel.saveQuantity(quantity + 1)
                productItem?.let {
                    detailViewModel.getProductById(it.id)
                }
            }
            R.id.iv_remove -> {
                detailViewModel.saveQuantity(quantity - 1)
                productItem?.let {
                    detailViewModel.getProductById(it.id)
                }
            }
        }
    }

}
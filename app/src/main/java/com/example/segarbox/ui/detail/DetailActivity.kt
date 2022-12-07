package com.example.segarbox.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.datastore.preferences.preferencesDataStore
import com.bumptech.glide.Glide
import com.example.core.data.source.local.datastore.SettingPreferences
import com.example.core.data.source.remote.response.ProductItem
import com.example.core.data.source.remote.response.UserCartItem
import com.example.core.utils.*
import com.example.segarbox.R
import com.example.segarbox.databinding.ActivityDetailBinding
import com.example.segarbox.ui.cart.CartActivity
import com.example.segarbox.ui.login.LoginActivity
import com.example.segarbox.ui.viewmodel.PrefViewModel
import com.example.segarbox.ui.viewmodel.PrefViewModelFactory
import com.example.segarbox.ui.viewmodel.RetrofitViewModelFactory
import com.google.android.material.snackbar.Snackbar

private val Context.dataStore by preferencesDataStore(name = "settings")

class DetailActivity : AppCompatActivity(), View.OnClickListener {

    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding!!
    private var productId: Int = 0
    private var userCartItem: UserCartItem? = null
    private var fromActivity: String? = null
    private var token = ""
    private var quantity = 0
    private val detailViewModel by viewModels<DetailViewModel> {
        RetrofitViewModelFactory.getInstance(com.example.core.data.RetrofitRepository())
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
        observeData()
        binding.toolbar.ivBack.setOnClickListener(this)
        binding.toolbar.ivCart.setOnClickListener(this)
        binding.content.counter.ivAdd.setOnClickListener(this)
        binding.content.counter.ivRemove.setOnClickListener(this)
        binding.btnAddToCart.setOnClickListener(this)
    }

    private fun getIntentDetail() {
        // From Home
        productId = intent.getIntExtra(Code.KEY_DETAIL_VALUE, 0)

        // From Cart
        fromActivity = intent.getStringExtra(Code.KEY_FROM_ACTIVITY)

        fromActivity?.let {
            userCartItem = intent.getParcelableExtra(Code.KEY_USERCART_VALUE)
            userCartItem?.let {
                productId = it.product.id
                detailViewModel.saveQuantity(it.productQty)
            }
        }
    }


    private fun setDetail(item: ProductItem) {
        binding.toolbar.apply {
            ivBack.isVisible = true
            ivCart.isVisible = true
            tvTitle.text = item.label
        }
        binding.content.apply {
            Glide.with(this@DetailActivity)
                .load(item.image)
                .into(ivItem)
            tvItemName.text = item.label
            tvSize.text = item.size.formatProductSize(this@DetailActivity)
            tvPrice.text = item.price.formatToRupiah()
            tvItemDescription.text = item.detail
        }
    }

    private fun observeData() {

        detailViewModel.getProductById(productId)

        detailViewModel.productById.observe(this) { productById ->
            productById.data?.let {
                if (it.qty < quantity) {
                    detailViewModel.saveQuantity(it.qty)
                }
                setDetail(it)
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
        }


        detailViewModel.addCartResponse.observe(this) { addCartResponse ->
            if (addCartResponse.message != null) {
                Snackbar.make(binding.root, addCartResponse.message.toString(), Snackbar.LENGTH_SHORT).setAction("OK"){}.show()
            } else {
                if (token.isNotEmpty()) {
                    detailViewModel.getUserCart(token.tokenFormat())
                }
                addCartResponse.info?.let {
                    Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).setAction("OK"){}.show()
                }
            }
        }

        detailViewModel.updateUserCartResponse.observe(this) { updateCartResponse ->
            if (updateCartResponse.message != null) {
                Snackbar.make(binding.root, updateCartResponse.message.toString(), Snackbar.LENGTH_SHORT).setAction("OK"){}.show()
                detailViewModel.getProductById(productId)
            } else {
                updateCartResponse.info?.let {
                    val intent = Intent(this, CartActivity::class.java)
                    intent.putExtra(Code.SNACKBAR_VALUE, it)
                    startActivity(intent)

                    finish()
                }
            }
        }

        detailViewModel.deleteUserCartResponse.observe(this) { deleteCartResponse ->
            if (deleteCartResponse.message != null) {
                Snackbar.make(binding.root, deleteCartResponse.message.toString(), Snackbar.LENGTH_SHORT).setAction("OK"){}.show()
            } else {
                deleteCartResponse.info?.let {
                    val intent = Intent(this, CartActivity::class.java)
                    intent.putExtra(Code.SNACKBAR_VALUE, it)
                    startActivity(intent)

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
                detailViewModel.getProductById(productId)
            }
            R.id.iv_remove -> {
                detailViewModel.saveQuantity(quantity - 1)
                detailViewModel.getProductById(productId)
            }
            R.id.btn_add_to_cart -> {
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
                            } else {
                                detailViewModel.deleteUserCart(token.tokenFormat(), userCartItem.id)
                            }
                        }
                    } else {
                        detailViewModel.addCart(token = token.tokenFormat(),
                            productId = productId,
                            productQty = quantity)
                    }

                }
            }
        }
    }

}
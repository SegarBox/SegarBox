package com.example.segarbox.ui.detail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.core.data.Resource
import com.example.core.domain.model.Cart
import com.example.core.domain.model.Product
import com.example.core.utils.*
import com.example.segarbox.R
import com.example.segarbox.databinding.ActivityDetailBinding
import com.example.segarbox.ui.cart.CartActivity
import com.example.segarbox.ui.login.LoginActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity(), View.OnClickListener {

    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding!!
    private var productId: Int = 0
    private var cart: Cart? = null
    private var fromActivity: String? = null
    private var token = ""
    private var quantity = 0
    private val viewModel: DetailViewModel by viewModels()

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
            cart = intent.getParcelableExtra(Code.KEY_USERCART_VALUE)
            cart?.let {
                productId = it.product.id
                viewModel.saveQuantity(it.productQty)
            }
        }
    }


    private fun setDetail(item: Product) {
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

        viewModel.getToken().observe(this) { event ->
            event.getContentIfNotHandled()?.let {
                this.token = it
                // Menampilkan Badge
                if (token.isNotEmpty()) {
                    viewModel.getCart(token.tokenFormat())
                }
            }
        }

        viewModel.getCartResponse.observe(this) { event ->
            event.getContentIfNotHandled()?.let { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        viewModel.setLoading(true)
                    }

                    is Resource.Success -> {
                        resource.data?.let { listCart ->
                            listCart[0].total?.let { total ->
                                binding.toolbar.ivCart.badgeValue = total
                            }
                            viewModel.setLoading(false)
                        }
                    }

                    is Resource.Empty -> {
                        binding.toolbar.ivCart.badgeValue = 0
                        viewModel.setLoading(false)
                    }

                    else -> {
                        resource.message?.let {
                            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT)
                                .setAction("OK") {}.show()
                            viewModel.setLoading(false)
                        }
                    }
                }
            }

        }

        viewModel.getProductById(productId)

        viewModel.getProductByIdResponse.observe(this) { event ->
            event.getContentIfNotHandled()?.let { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        viewModel.setLoading(true)
                    }

                    is Resource.Success -> {
                        resource.data?.let {
                            // Pengecekan tambahan
                            if (it.qty < quantity) {
                                viewModel.saveQuantity(it.qty)
                            }
                            setDetail(it)
                            binding.content.tvStock.text =
                                getString(R.string.stock, it.qty.toString())
                            // Pengecekan jika stok masih ada
                            binding.content.counter.ivAdd.isEnabled = quantity < it.qty
                            viewModel.setLoading(false)

                        }
                    }

                    else -> {
                        resource.message?.let {
                            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT)
                                .setAction("OK") {}.show()
                            viewModel.setLoading(false)
                        }
                    }
                }
            }
        }

        viewModel.quantity.observe(this) { qty ->
            quantity = qty
//            binding.btnAddToCart.isEnabled = qty != 0
            binding.content.counter.apply {
                tvCount.text = qty.toString()
                ivRemove.isEnabled = qty != 0
            }

            // Set Button jika dari Cart
            fromActivity?.let {
                if (it == Code.CART_ACTIVITY) {
//                    binding.btnAddToCart.isEnabled = true
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

        viewModel.isLoading.observe(this) { event ->
            event.getContentIfNotHandled()?.let { isLoading ->
                // Set Button Jika dari Cart
                if (fromActivity != null && fromActivity == Code.CART_ACTIVITY) {
                    binding.btnAddToCart.isEnabled = !isLoading
                } else {
                    binding.btnAddToCart.isEnabled = !isLoading && quantity != 0
                }

                binding.progressBar.isVisible = isLoading
                binding.content.counter.apply {
                    ivRemove.isClickable = !isLoading
                    ivAdd.isClickable = !isLoading
                }
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
//                finish()
            }
            R.id.iv_add -> {
                viewModel.saveQuantity(quantity + 1)
                viewModel.getProductById(productId)
            }
            R.id.iv_remove -> {
                viewModel.saveQuantity(quantity - 1)
                viewModel.getProductById(productId)
            }
            R.id.btn_add_to_cart -> {
                if (token.isEmpty()) {
                    startActivity(Intent(this, LoginActivity::class.java))
                } else {
                    // Set Jika dari Cart atau tidak
                    if (fromActivity != null && fromActivity == Code.CART_ACTIVITY) {

                        // Jika quantity > 0
                        cart?.let { cart ->
                            if (quantity > 0) {
                                viewModel.updateCart(
                                    token.tokenFormat(),
                                    cart.id,
                                    cart.product.id,
                                    quantity,
                                    cart.isChecked,
                                ).observe(this) { event ->
                                    event.getContentIfNotHandled()?.let { resource ->
                                        when (resource) {
                                            is Resource.Loading -> {
                                                viewModel.setLoading(true)
                                            }

                                            is Resource.Success -> {
                                                resource.data?.let {
                                                    viewModel.setLoading(false)
                                                    val intent =
                                                        Intent(this, CartActivity::class.java)
                                                    intent.putExtra(Code.SNACKBAR_VALUE, it)
                                                    startActivity(intent)
                                                    finish()
                                                }
                                            }

                                            else -> {
                                                resource.message?.let {
                                                    Snackbar.make(binding.root,
                                                        it,
                                                        Snackbar.LENGTH_SHORT).setAction("OK") {}
                                                        .show()
                                                    viewModel.getProductById(productId)
                                                    viewModel.setLoading(false)
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                viewModel.deleteCart(token.tokenFormat(), cart.id)
                                    .observe(this) { event ->
                                        event.getContentIfNotHandled()?.let { resource ->
                                            when (resource) {
                                                is Resource.Loading -> {
                                                    viewModel.setLoading(true)
                                                }

                                                is Resource.Success -> {
                                                    resource.data?.let {
                                                        viewModel.setLoading(false)
                                                        val intent =
                                                            Intent(this, CartActivity::class.java)
                                                        intent.putExtra(Code.SNACKBAR_VALUE, it)
                                                        startActivity(intent)
                                                        finish()
                                                    }
                                                }

                                                else -> {
                                                    resource.message?.let {
                                                        Snackbar.make(binding.root,
                                                            it,
                                                            Snackbar.LENGTH_SHORT)
                                                            .setAction("OK") {}
                                                            .show()
                                                        viewModel.setLoading(false)
                                                    }
                                                }
                                            }
                                        }
                                    }
                            }
                        }

                    } else {
                        viewModel.addCart(
                            token = token.tokenFormat(),
                            productId = productId,
                            productQty = quantity,
                        ).observe(this) { event ->
                            event.getContentIfNotHandled()?.let { resource ->
                                when (resource) {
                                    is Resource.Loading -> {
                                        viewModel.setLoading(true)
                                    }

                                    is Resource.Success -> {
                                        resource.data?.let {
                                            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT)
                                                .setAction("OK") {}.show()
                                            if (token.isNotEmpty()) {
                                                viewModel.getCart(token.tokenFormat())
                                            }
                                            viewModel.setLoading(false)
                                        }
                                    }

                                    else -> {
                                        resource.message?.let {
                                            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT)
                                                .setAction("OK") {}.show()
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }
    }

}
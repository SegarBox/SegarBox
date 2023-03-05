package com.example.segarbox.ui.cart

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.core.data.Resource
import com.example.core.domain.model.Cart
import com.example.core.ui.CartAdapter
import com.example.core.utils.Code
import com.example.core.utils.formatToRupiah
import com.example.core.utils.tokenFormat
import com.example.segarbox.R
import com.example.segarbox.databinding.ActivityCartBinding
import com.example.segarbox.ui.checkout.CheckoutActivity
import com.example.segarbox.ui.detail.DetailActivity
import com.example.segarbox.ui.login.LoginActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartActivity : AppCompatActivity(), View.OnClickListener,
    CartAdapter.OnItemCartClickCallback {

    private var _binding: ActivityCartBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CartViewModel by viewModels()
    private val cartAdapter = CartAdapter(this)
    private var token = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        setToolbar()
        observeData()
        setAdapter()
        scrollToTopListAdapter()
        binding.bottomPaymentInfo.btnCheckout.text = getString(R.string.checkout)
        binding.toolbar.ivBack.setOnClickListener(this)
        binding.bottomPaymentInfo.btnCheckout.setOnClickListener(this)
    }

    private fun setAdapter() {
        binding.content.rvCart.apply {
            layoutManager =
                LinearLayoutManager(this@CartActivity, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = cartAdapter
        }
    }

    private fun setToolbar() {
        binding.toolbar.apply {
            ivBack.isVisible = true
            ivCart.isVisible = false
            tvTitle.text = getString(R.string.cart)
        }
    }

    private fun observeData() {
        viewModel.getToken().observe(this) { event ->
            event.getContentIfNotHandled()?.let { token ->
                this.token = token
                if (token.isNotEmpty()) {
                    viewModel.getCart(token.tokenFormat())
                } else {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
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
                        resource.data?.let {
                            binding.ivEmptycart.isVisible = false
                            binding.tvEmptycart.isVisible = false
                            binding.bottomPaymentInfo.root.isVisible = true

                            cartAdapter.submitList(it)
                            viewModel.getCartDetail(token.tokenFormat(), 0)
                            viewModel.setLoading(false)
                        }
                    }

                    is Resource.Empty -> {
                        binding.ivEmptycart.isVisible = true
                        binding.tvEmptycart.isVisible = true
                        binding.content.rvCart.isVisible = false
                        binding.bottomPaymentInfo.root.isVisible = false
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

        viewModel.getCartDetailResponse.observe(this) { event ->
            event.getContentIfNotHandled()?.let { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        viewModel.setLoading(true)
                    }

                    is Resource.Success -> {
                        resource.data?.let {
                            binding.bottomPaymentInfo.tvPrice.text =
                                it.subtotalProducts.formatToRupiah()
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

        viewModel.updateCartResponse.observe(this) { event ->
            event.getContentIfNotHandled()?.let { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        viewModel.setLoading(true)
                    }

                    is Resource.Success -> {
                        resource.data?.let {
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
                            if (token.isNotEmpty()) {
                                viewModel.getCart(token.tokenFormat())
                            }
                            viewModel.setLoading(false)
                        }
                    }
                }
            }
        }

        viewModel.isLoading.observe(this) { event ->
            event.getContentIfNotHandled()?.let { isLoading ->
                binding.progressBar.isVisible = isLoading
                binding.bottomPaymentInfo.btnCheckout.isEnabled = !isLoading
            }
        }

    }

    private fun scrollToTopListAdapter() {
        cartAdapter.registerAdapterDataObserver(object :
            RecyclerView.AdapterDataObserver() {
            override fun onChanged() {}

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {}

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {}

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                binding.content.rvCart.scrollToPosition(0)
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {}

            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {}
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val snackbarValue = intent?.getStringExtra(Code.SNACKBAR_VALUE)
        snackbarValue?.let {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).setAction("OK") {}.show()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> {
                finish()
            }
            R.id.btn_checkout -> {
                if (token.isNotEmpty()) {
                    viewModel.getCheckedCart(token.tokenFormat()).observe(this) { event ->
                        event.getContentIfNotHandled()?.let { resource ->
                            when (resource) {
                                is Resource.Loading -> {
                                    viewModel.setLoading(true)
                                }

                                is Resource.Success -> {
                                    resource.data?.let {
                                        var passTest = true
                                        for (i in it.indices) {
                                            if (it[i].productQty > it[i].product.qty) {
                                                passTest = false
                                                Snackbar.make(binding.root,
                                                    "Some items are out of stocks, items will be automatically updated",
                                                    Snackbar.LENGTH_SHORT).setAction("OK") {}.show()
                                                break
                                            }
                                        }
                                        when {
                                            passTest -> {
                                                startActivity(Intent(this,
                                                    CheckoutActivity::class.java))
                                            }
                                            else -> {
                                                if (token.isNotEmpty())
                                                    viewModel.getCart(token.tokenFormat())
                                            }
                                        }
                                    }
                                    viewModel.setLoading(false)
                                }

                                is Resource.Empty -> {
                                    Snackbar.make(binding.root,
                                        "You haven't selected any items yet", Snackbar.LENGTH_SHORT)
                                        .setAction("OK") {}.show()
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
                }
            }
        }
    }

    override fun onCheckboxClicked(item: Cart) {
        if (token.isNotEmpty()) {
            val newIsChecked = if (item.isChecked == 1) 0 else 1
            viewModel.updateCart(token.tokenFormat(),
                item.id,
                item.productId,
                item.productQty,
                newIsChecked)
        }
    }

    override fun onRemoveClicked(item: Cart) {
        if (token.isNotEmpty()) {
            val newProductQty = item.productQty - 1
            viewModel.updateCart(token.tokenFormat(),
                item.id,
                item.productId,
                newProductQty,
                item.isChecked)
        }
    }

    override fun onAddClicked(item: Cart) {
        if (token.isNotEmpty()) {
            val newProductQty = item.productQty + 1
            viewModel.updateCart(token.tokenFormat(),
                item.id,
                item.productId,
                newProductQty,
                item.isChecked)
        }
    }

    override fun onStashClicked(item: Cart) {
        if (token.isNotEmpty()) {
            viewModel.deleteCart(token.tokenFormat(), item.id).observe(this) { event ->
                event.getContentIfNotHandled()?.let { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            viewModel.setLoading(true)
                        }

                        is Resource.Success -> {
                            resource.data?.let {
                                Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT)
                                    .setAction("OK") {}.show()
                                if (token.isNotEmpty()) viewModel.getCart(token.tokenFormat())
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
        }
    }

    override fun onItemProductQtyChanged(item: Cart) {
        if (token.isNotEmpty()) {
            viewModel.updateCart(token.tokenFormat(),
                item.id,
                item.productId,
                item.productQty,
                item.isChecked)
        }
    }

    override fun onRootClicked(item: Cart) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(Code.KEY_FROM_ACTIVITY, Code.CART_ACTIVITY)
        intent.putExtra(Code.KEY_USERCART_VALUE, item)
        startActivity(intent)
    }

}
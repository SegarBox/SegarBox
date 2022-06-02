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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.segarbox.R
import com.example.segarbox.data.local.datastore.SettingPreferences
import com.example.segarbox.data.local.static.Code
import com.example.segarbox.data.remote.response.ProductItem
import com.example.segarbox.data.remote.response.UserCartItem
import com.example.segarbox.data.repository.RetrofitRepository
import com.example.segarbox.databinding.ActivityCartBinding
import com.example.segarbox.helper.formatToRupiah
import com.example.segarbox.helper.tokenFormat
import com.example.segarbox.ui.adapter.CartAdapter
import com.example.segarbox.ui.viewmodel.CartViewModel
import com.example.segarbox.ui.viewmodel.PrefViewModel
import com.example.segarbox.ui.viewmodel.PrefViewModelFactory
import com.example.segarbox.ui.viewmodel.RetrofitViewModelFactory

private val Context.dataStore by preferencesDataStore(name = "settings")

class CartActivity : AppCompatActivity(), View.OnClickListener,
    CartAdapter.OnItemCartClickCallback {

    private var _binding: ActivityCartBinding? = null
    private val binding get() = _binding!!
    private val cartAdapter = CartAdapter(this)
    private var token: String? = null
    private val prefViewModel by viewModels<PrefViewModel> {
        PrefViewModelFactory.getInstance(SettingPreferences.getInstance(dataStore))
    }
    private val cartViewModel by viewModels<CartViewModel> {
        RetrofitViewModelFactory.getInstance(RetrofitRepository())
    }

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
        setupView()
        scrollToTopListAdapter()
        binding.toolbar.ivBack.setOnClickListener(this)
        binding.bottomPaymentInfo.checkoutLayout.setOnClickListener(this)
    }

    private fun setupView() {
        binding.bottomPaymentInfo.tvButton.text = "Checkout"
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
            tvTitle.text = "Cart"
        }
    }

    private fun observeData() {
        prefViewModel.getToken().observe(this) { token ->
            if (token.isEmpty()) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                this.token = token
            }
        }

        cartViewModel.deleteUserCartResponse.observe(this) { deleteCartResponse ->
            deleteCartResponse.data?.let {
                token?.let { token ->
                    cartViewModel.getUserCart(token.tokenFormat())
                }
            }

            deleteCartResponse.message?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }

        cartViewModel.updateUserCartResponse.observe(this) { updateCartResponse ->
            updateCartResponse.data?.let {
                token?.let { token ->
                    cartViewModel.getUserCart(token.tokenFormat())
                }
            }

            updateCartResponse.message?.let {
                token?.let { token ->
                    cartViewModel.getUserCart(token.tokenFormat())
                }
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }

        cartViewModel.userCart.observe(this) { userCartResponse ->
            userCartResponse.data?.let {
                cartAdapter.submitList(it)
                token?.let { token ->
                    cartViewModel.getCartDetail(token.tokenFormat(), 0)
                }
            }
        }

        cartViewModel.cartDetailResponse.observe(this) { cartDetailResponse ->
            binding.bottomPaymentInfo.tvPrice.text =
                cartDetailResponse.subtotalProducts.formatToRupiah()
        }

        cartViewModel.isCheckedUserCart.observe(this) { isCheckedCartResponse ->
            isCheckedCartResponse.data?.let { listCart ->
                when {
                    listCart.isEmpty() -> {
                        Toast.makeText(this,
                            "You haven't selected any items yet",
                            Toast.LENGTH_SHORT)
                            .show()
                    }
                    else -> {
                        var passTest = true
                        for (i in listCart.indices) {
                            if (listCart[i].productQty > listCart[i].product.qty) {
                                passTest = false
                                Toast.makeText(this,
                                    "Some items are out of stocks, items will be automatically updated",
                                    Toast.LENGTH_SHORT).show()
                                break
                            }
                        }

                        when {
                            passTest -> {
                                startActivity(Intent(this, CheckoutActivity::class.java))
                            }
                            else -> {
                                token?.let {
                                    cartViewModel.getUserCart(it.tokenFormat())
                                }
                            }
                        }
                    }
                }
            }
        }

        cartViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.isVisible = isLoading
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

    override fun onResume() {
        super.onResume()

        token?.let {
            cartViewModel.getUserCart(it.tokenFormat())
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
            R.id.checkout_layout -> {
                token?.let {
                    cartViewModel.getIsCheckedUserCart(it.tokenFormat())
                }
            }
        }
    }

    override fun onCheckboxClicked(item: UserCartItem) {
        token?.let {
            var newIsChecked = 1
            newIsChecked = if (item.isChecked == 1) 0 else 1
            cartViewModel.updateUserCart(it.tokenFormat(),
                item.id,
                item.productId,
                item.productQty,
                newIsChecked)
        }
    }

    override fun onRemoveClicked(item: UserCartItem) {
        token?.let {
            val newProductQty = item.productQty - 1
            cartViewModel.updateUserCart(it.tokenFormat(),
                item.id,
                item.productId,
                newProductQty,
                item.isChecked)
        }
    }

    override fun onAddClicked(item: UserCartItem) {
        token?.let {
            val newProductQty = item.productQty + 1
            cartViewModel.updateUserCart(it.tokenFormat(),
                item.id,
                item.productId,
                newProductQty,
                item.isChecked)
        }
    }

    override fun onStashClicked(item: UserCartItem) {
        token?.let {
            cartViewModel.deleteUserCart(it.tokenFormat(), item.id)
        }
    }

    override fun onItemProductQtyChanged(item: UserCartItem) {
        token?.let {
            cartViewModel.updateUserCart(it.tokenFormat(),
                item.id,
                item.productId,
                item.productQty,
                item.isChecked)
        }
    }

    override fun onRootClicked(item: UserCartItem) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(Code.KEY_FROM_ACTIVITY, Code.CART_ACTIVITY)
        intent.putExtra(Code.KEY_USERCART_VALUE, item)
        startActivity(intent)
    }

}
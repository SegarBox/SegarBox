package com.example.segarbox.ui.cart

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.datastore.preferences.preferencesDataStore
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.core.data.source.local.datastore.SettingPreferences
import com.example.core.data.source.remote.response.UserCartItem
import com.example.core.ui.CartAdapter
import com.example.core.utils.Code
import com.example.core.utils.formatToRupiah
import com.example.core.utils.tokenFormat
import com.example.segarbox.R
import com.example.segarbox.databinding.ActivityCartBinding
import com.example.segarbox.ui.checkout.CheckoutActivity
import com.example.segarbox.ui.detail.DetailActivity
import com.example.segarbox.ui.login.LoginActivity
import com.example.segarbox.ui.viewmodel.PrefViewModel
import com.example.segarbox.ui.viewmodel.PrefViewModelFactory
import com.example.segarbox.ui.viewmodel.RetrofitViewModelFactory
import com.google.android.material.snackbar.Snackbar

private val Context.dataStore by preferencesDataStore(name = "settings")

class CartActivity : AppCompatActivity(), View.OnClickListener,
    CartAdapter.OnItemCartClickCallback {

    private var _binding: ActivityCartBinding? = null
    private val binding get() = _binding!!
    private val cartAdapter = CartAdapter(this)
    private var token = ""
    private val prefViewModel by viewModels<PrefViewModel> {
        PrefViewModelFactory.getInstance(SettingPreferences.getInstance(dataStore))
    }
    private val cartViewModel by viewModels<CartViewModel> {
        RetrofitViewModelFactory.getInstance(com.example.core.data.RetrofitRepository())
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
        binding.bottomPaymentInfo.tvButton.text = getString(R.string.checkout)
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
            tvTitle.text = getString(R.string.cart)
        }
    }

    private fun observeData() {
        prefViewModel.getToken().observe(this) { token ->
            this.token = token
            if (token.isEmpty()) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

        cartViewModel.deleteUserCartResponse.observe(this) { deleteCartResponse ->
            deleteCartResponse.data?.let {
                if (token.isNotEmpty()) {
                    cartViewModel.getUserCart(token.tokenFormat())
                }
            }

            deleteCartResponse.message?.let {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).setAction("OK"){}.show()
            }
        }

        cartViewModel.updateUserCartResponse.observe(this) { updateCartResponse ->
            updateCartResponse.data?.let {
                if (token.isNotEmpty()) {
                    cartViewModel.getUserCart(token.tokenFormat())
                }
            }

            updateCartResponse.message?.let {
                if (token.isNotEmpty()) {
                    cartViewModel.getUserCart(token.tokenFormat())
                }
                Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).setAction("OK"){}.show()
            }
        }

        cartViewModel.userCart.observe(this) { userCartResponse ->
            userCartResponse.data?.let {
                binding.ivEmptycart.isVisible = it.isEmpty()
                binding.tvEmptycart.isVisible = it.isEmpty()
                binding.bottomPaymentInfo.root.isVisible = it.isNotEmpty()

                cartAdapter.submitList(it)
                if (token.isNotEmpty()) {
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
                        Snackbar.make(binding.root,
                            "You haven't selected any items yet",
                            Snackbar.LENGTH_SHORT)
                            .setAction("OK"){}.show()
                    }
                    else -> {
                        var passTest = true
                        for (i in listCart.indices) {
                            if (listCart[i].productQty > listCart[i].product.qty) {
                                passTest = false
                                Snackbar.make(binding.root,
                                    "Some items are out of stocks, items will be automatically updated",
                                    Snackbar.LENGTH_SHORT).setAction("OK"){}.show()
                                break
                            }
                        }

                        when {
                            passTest -> {
                                startActivity(Intent(this, CheckoutActivity::class.java))
                            }
                            else -> {
                                if (token.isNotEmpty()) {
                                    cartViewModel.getUserCart(token.tokenFormat())
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

        if (token.isNotEmpty()) {
            cartViewModel.getUserCart(token.tokenFormat())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val snackbarValue = intent?.getStringExtra(Code.SNACKBAR_VALUE)
        snackbarValue?.let {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).setAction("OK"){}.show()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> {
                finish()
            }
            R.id.checkout_layout -> {
                if (token.isNotEmpty()) {
                    cartViewModel.getIsCheckedUserCart(token.tokenFormat())
                }
            }
        }
    }

    override fun onCheckboxClicked(item: UserCartItem) {
        if (token.isNotEmpty()) {
            val newIsChecked = if (item.isChecked == 1) 0 else 1
            cartViewModel.updateUserCart(token.tokenFormat(),
                item.id,
                item.productId,
                item.productQty,
                newIsChecked)
        }
    }

    override fun onRemoveClicked(item: UserCartItem) {
        if (token.isNotEmpty()) {
            val newProductQty = item.productQty - 1
            cartViewModel.updateUserCart(token.tokenFormat(),
                item.id,
                item.productId,
                newProductQty,
                item.isChecked)
        }
    }

    override fun onAddClicked(item: UserCartItem) {
        if (token.isNotEmpty()) {
            val newProductQty = item.productQty + 1
            cartViewModel.updateUserCart(token.tokenFormat(),
                item.id,
                item.productId,
                newProductQty,
                item.isChecked)
        }
    }

    override fun onStashClicked(item: UserCartItem) {
        if (token.isNotEmpty()) {
            cartViewModel.deleteUserCart(token.tokenFormat(), item.id)
        }
    }

    override fun onItemProductQtyChanged(item: UserCartItem) {
        if (token.isNotEmpty()) {
            cartViewModel.updateUserCart(token.tokenFormat(),
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
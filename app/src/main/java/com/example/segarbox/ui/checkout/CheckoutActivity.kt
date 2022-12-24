package com.example.segarbox.ui.checkout

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.core.data.source.local.datastore.SettingPreferences
import com.example.core.data.source.remote.response.AddressItem
import com.example.core.data.source.remote.response.CartDetailResponse
import com.example.core.domain.body.MakeOrderBody
import com.example.core.domain.model.ProductTransactions
import com.example.core.domain.model.ShippingModel
import com.example.core.ui.CheckoutDetailsAdapter
import com.example.core.utils.*
import com.example.segarbox.R
import com.example.segarbox.databinding.ActivityCheckoutBinding
import com.example.segarbox.ui.address.AddressActivity
import com.example.segarbox.ui.invoice.InvoiceActivity
import com.example.segarbox.ui.shipping.ShippingActivity
import com.example.segarbox.ui.viewmodel.PrefViewModel
import com.example.segarbox.ui.viewmodel.PrefViewModelFactory
import com.example.segarbox.ui.viewmodel.RetrofitViewModelFactory
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckoutActivity : AppCompatActivity(), View.OnClickListener {

    private var _binding: ActivityCheckoutBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CheckoutViewModel by viewModels()
    private var addressItem: AddressItem? = null
    private var costs: CartDetailResponse? = null
    private val listProductTransactions: ArrayList<ProductTransactions> = arrayListOf()
    private var token = ""
    private var isShippingCostAdded = false
    private val checkoutDetailsAdapter = CheckoutDetailsAdapter()
    private var isGotToken = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        setToolbar()
        observeData()
        setAdapter()
        binding.bottomPaymentInfo.tvButton.text = getString(R.string.make_order)
        binding.toolbar.ivBack.setOnClickListener(this)
        binding.content.btnChooseAddress.setOnClickListener(this)
        binding.content.layoutShipping.setOnClickListener(this)
        binding.bottomPaymentInfo.checkoutLayout.setOnClickListener(this)
    }

    private fun setToolbar() {
        binding.toolbar.apply {
            tvTitle.text = getString(R.string.checkout)
            ivBack.isVisible = true
        }
    }

    private fun setAdapter() {
        binding.content.rvCheckoutItem.apply {
            layoutManager =
                LinearLayoutManager(this@CheckoutActivity, LinearLayoutManager.VERTICAL, false)
            adapter = checkoutDetailsAdapter
        }
    }

    private fun observeData() {

        // TOKEN DIAMBIL TERUS TIAP ONRESUME -_-
        prefViewModel.getToken().observe(this) { token ->
            this.token = token

            if (!this.isGotToken && token.isNotEmpty()) {
                this.isGotToken = true
                viewModel.getCheckoutDetails(token.tokenFormat())

                if (costs != null) {
                    costs?.let {
                        viewModel.getCosts(token.tokenFormat(), it.shippingCost)
                    }
                }
                else {
                    viewModel.getCosts(token.tokenFormat(), 0)
                }
            }
        }

        viewModel.checkoutDetails.observe(this) { userCartResponse ->
            userCartResponse.data?.let {
                checkoutDetailsAdapter.submitList(it)

                listProductTransactions.clear()
                it.forEach { userCartItem ->
                    listProductTransactions.add(ProductTransactions(
                        userCartItem.product.id,
                        userCartItem.productQty
                    ))
                }
            }
        }

        viewModel.costs.observe(this) { costs ->
            this.costs = costs

            binding.content.apply {
                tvProductsSubtotal.text = costs.subtotalProducts.formatToRupiah()
                tvShippingCost.text = costs.shippingCost.formatToRupiah()
                tvTotalPrice.text = costs.totalPrice.formatToRupiah()
            }
            binding.bottomPaymentInfo.tvPrice.text = costs.totalPrice.formatToRupiah()
        }

        viewModel.makeOrderResponse.observe(this) { makeOrderResponse ->
            makeOrderResponse.info?.let { info ->
                makeOrderResponse.data?.let { makeOrderResponse ->
                    val intent = Intent(this, InvoiceActivity::class.java)
                    intent.putExtra(Code.KEY_TRANSACTION_ID, makeOrderResponse.id)
                    intent.putExtra(Code.SNACKBAR_VALUE, info)
                    startActivity(intent)
                    finish()
                }
            }

            makeOrderResponse.message?.let {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).setAction("OK"){}.show()
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.isVisible = isLoading
        }

    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when {
                result.resultCode == Code.RESULT_SAVE_ADDRESS && result.data != null -> {

                    // Jika tambah address, otomatis shipping cost ke reset
                    binding.content.contentShipping.root.isVisible = false
                    if (token.isNotEmpty()) {
                        viewModel.getCosts(token.tokenFormat(), 0)
                    }

                    addressItem =
                        result.data?.getParcelableExtra(Code.ADDRESS_VALUE)

                    addressItem?.let {
                        isShippingCostAdded = false
                        binding.content.tvAddress.apply {
                            isVisible = true
                            text = it.street
                        }
                    }

                }

                result.resultCode == Code.RESULT_SAVE_SHIPPING && result.data != null -> {

                    val shippingModel =
                        result.data?.getParcelableExtra<ShippingModel>(Code.SHIPPING_VALUE)

                    shippingModel?.let {
                        if (token.isNotEmpty()) {
                            viewModel.getCosts(token.tokenFormat(), it.price)
                        }


                        binding.content.contentShipping.apply {
                            isShippingCostAdded = true
                            root.isVisible = true
                            when (it.code) {
                                Code.TIKI.uppercase() -> {
                                    Glide.with(this@CheckoutActivity)
                                        .load(R.drawable.tiki)
                                        .into(ivKurir)

                                    tvEtd.text = it.etd.tidyUpJneAndTikiEtd(this@CheckoutActivity)
                                }
                                Code.JNE.uppercase() -> {
                                    Glide.with(this@CheckoutActivity)
                                        .load(R.drawable.jne)
                                        .into(ivKurir)

                                    tvEtd.text = it.etd.tidyUpJneAndTikiEtd(this@CheckoutActivity)
                                }
                                else -> {
                                    Glide.with(this@CheckoutActivity)
                                        .load(R.drawable.pos)
                                        .into(ivKurir)

                                    tvEtd.text = it.etd.tidyUpPosEtd(this@CheckoutActivity)
                                }
                            }
                            tvKurir.text = it.code
                            tvService.text = it.service
                            tvPrice.text = it.price.formatToRupiah()
                        }


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
            R.id.iv_back -> finish()

            R.id.btn_choose_address -> {
                resultLauncher.launch(Intent(this, AddressActivity::class.java))
            }

            R.id.layout_shipping -> {
                if (addressItem != null) {
                    val intent = Intent(this, ShippingActivity::class.java)
                    intent.putExtra(Code.ADDRESS_VALUE, addressItem)
                    resultLauncher.launch(intent)
                } else {
                    Snackbar.make(binding.root,
                        "Please add your delivery address first!",
                        Snackbar.LENGTH_SHORT).setAction("OK"){}.show()
                }
            }

            R.id.checkout_layout -> {
                if (isShippingCostAdded) {
                    if (token.isNotEmpty() && addressItem != null && costs != null && listProductTransactions.isNotEmpty()) {

                        val makeOrderBody = MakeOrderBody(
                            token.tokenFormat(),
                            addressItem!!.id,
                            costs!!.qtyTransaction,
                            costs!!.subtotalProducts,
                            costs!!.totalPrice,
                            costs!!.shippingCost,
                            listProductTransactions
                        )

                        viewModel.makeOrderTransaction(
                            token.tokenFormat(),
                            makeOrderBody
                        )
                    }

                } else {
                    Snackbar.make(binding.root,
                        "Please add your shipping method first",
                        Snackbar.LENGTH_SHORT).setAction("OK"){}.show()
                }
            }
        }
    }
}
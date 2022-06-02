package com.example.segarbox.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.datastore.preferences.preferencesDataStore
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.segarbox.R
import com.example.segarbox.data.local.datastore.SettingPreferences
import com.example.segarbox.data.local.model.DummyAddress
import com.example.segarbox.data.local.model.ShippingModel
import com.example.segarbox.data.local.static.Code
import com.example.segarbox.data.repository.RetrofitRepository
import com.example.segarbox.databinding.ActivityCheckoutBinding
import com.example.segarbox.helper.*
import com.example.segarbox.ui.adapter.CheckoutDetailsAdapter
import com.example.segarbox.ui.viewmodel.CheckoutViewModel
import com.example.segarbox.ui.viewmodel.PrefViewModel
import com.example.segarbox.ui.viewmodel.PrefViewModelFactory
import com.example.segarbox.ui.viewmodel.RetrofitViewModelFactory
import com.google.android.material.snackbar.Snackbar

private val Context.dataStore by preferencesDataStore(name = "settings")
class CheckoutActivity : AppCompatActivity(), View.OnClickListener {

    private var _binding: ActivityCheckoutBinding? = null
    private val binding get() = _binding!!
    private var address: DummyAddress? = null
    private var token: String? = null
    private var isShippingCostAdded = false
    private val checkoutDetailsAdapter = CheckoutDetailsAdapter()
    private val checkoutViewModel by viewModels<CheckoutViewModel> {
        RetrofitViewModelFactory.getInstance(RetrofitRepository())
    }
    private val prefViewModel by viewModels<PrefViewModel> {
        PrefViewModelFactory.getInstance(SettingPreferences.getInstance(dataStore))
    }

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
        binding.bottomPaymentInfo.tvButton.text = "Make Order"
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
            layoutManager = LinearLayoutManager(this@CheckoutActivity, LinearLayoutManager.VERTICAL, false)
            adapter = checkoutDetailsAdapter
        }
    }

    private fun observeData() {

        prefViewModel.getToken().observe(this) { token ->
            if (token.isNotEmpty()) {
                this.token = token
                checkoutViewModel.getCheckoutDetails(token.tokenFormat())
                checkoutViewModel.getCosts(token.tokenFormat(), 0)
            }
        }

        checkoutViewModel.checkoutDetails.observe(this) { userCartResponse ->
            userCartResponse.data?.let {
                checkoutDetailsAdapter.submitList(it)
            }
        }

        checkoutViewModel.costs.observe(this) { costs ->
            binding.content.apply {
                tvProductsSubtotal.text = costs.subtotalProducts.formatToRupiah()
                tvShippingCost.text = costs.shippingCost.toInt().formatToRupiah()
                tvTotalPrice.text = costs.totalPrice.formatToRupiah()
            }
            binding.bottomPaymentInfo.tvPrice.text = costs.totalPrice.formatToRupiah()
        }

    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when {
                result.resultCode == Code.RESULT_SAVE_ADDRESS && result.data != null -> {
                    val dummyAddress =
                        result.data?.getParcelableExtra<DummyAddress>(Code.ADDRESS_VALUE)
                    dummyAddress?.let { address ->
                        this.address = address
                        binding.content.tvAddress.apply {
                            isVisible = true
                            text = dummyAddress.address
                        }
                    }
                    binding.content.contentShipping.root.isVisible = false
                    binding.content.tvShippingCost.text = ""
                }

                result.resultCode == Code.RESULT_SAVE_SHIPPING && result.data != null -> {
                    val shippingModel =
                        result.data?.getParcelableExtra<ShippingModel>(Code.SHIPPING_VALUE)
                    shippingModel?.let {
                        binding.content.contentShipping.apply {
                            isShippingCostAdded = true
                            root.isVisible = true
                            when (it.code) {
                                Code.TIKI.uppercase() -> {
                                    Glide.with(this@CheckoutActivity)
                                        .load(R.drawable.tiki)
                                        .into(ivKurir)

                                    tvEtd.text = it.etd.tidyUpTikiEtd(this@CheckoutActivity)
                                }
                                Code.JNE.uppercase() -> {
                                    Glide.with(this@CheckoutActivity)
                                        .load(R.drawable.jne)
                                        .into(ivKurir)

                                    tvEtd.text = it.etd.tidyUpJneEtd(this@CheckoutActivity)
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

                        token?.let { token ->
                            checkoutViewModel.getCosts(token.tokenFormat(), it.price )
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
                if (address != null) {
                    val intent = Intent(this, ShippingActivity::class.java)
                    intent.putExtra(Code.ADDRESS_VALUE, address)
                    resultLauncher.launch(intent)
                } else {
                    Snackbar.make(binding.root, "Please add your delivery address first!", Snackbar.LENGTH_SHORT).show()
//                    Toast.makeText(this,
//                        "Please add your delivery address first!",
//                        Toast.LENGTH_SHORT).show()
                }
            }

            R.id.checkout_layout -> {
                if (isShippingCostAdded) {
                    startActivity(Intent(this, InvoiceActivity::class.java))
                    finish()
                }
                else {
                    Toast.makeText(this, "Please add your shipping method first", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
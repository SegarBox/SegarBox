package com.example.segarbox.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.segarbox.R
import com.example.segarbox.data.local.model.DummyAddress
import com.example.segarbox.data.local.model.DummyModelCart
import com.example.segarbox.data.local.model.ShippingModel
import com.example.segarbox.data.local.static.Code
import com.example.segarbox.databinding.ActivityCheckoutBinding
import com.example.segarbox.helper.formatToRupiah
import com.example.segarbox.helper.tidyUpJneEtd
import com.example.segarbox.helper.tidyUpPosEtd
import com.example.segarbox.helper.tidyUpTikiEtd
import com.example.segarbox.ui.adapter.DummyAdapterCheckout

class CheckoutActivity : AppCompatActivity(), View.OnClickListener {

    private var _binding: ActivityCheckoutBinding? = null
    private val binding get() = _binding!!
    private var address: DummyAddress? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        setToolbar()
        setAdapter()
        binding.toolbar.ivBack.setOnClickListener(this)
        binding.content.btnChooseAddress.setOnClickListener(this)
        binding.content.layoutShipping.setOnClickListener(this)
    }

    private fun setToolbar() {
        binding.toolbar.apply {
            tvTitle.text = getString(R.string.checkout)
            ivBack.isVisible = true
        }
    }

    private fun setAdapter() {
        val listItem = arrayListOf(
            DummyModelCart(),
            DummyModelCart(),
            DummyModelCart(),
            DummyModelCart(),
            DummyModelCart(),
            DummyModelCart(),
            DummyModelCart()
        )

        val adapterCheckout = DummyAdapterCheckout()
        adapterCheckout.submitList(listItem)

        binding.content.rvCheckoutItem.apply {
            layoutManager = LinearLayoutManager(this@CheckoutActivity)
            adapter = adapterCheckout
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

                        binding.content.tvShippingCost.text = it.price.formatToRupiah()
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

            R.id.btn_choose_address -> resultLauncher.launch(Intent(this,
                AddressActivity::class.java))

            R.id.layout_shipping -> {
                if (address != null) {
                    val intent = Intent(this, ShippingActivity::class.java)
                    intent.putExtra(Code.ADDRESS_VALUE, address)
                    resultLauncher.launch(intent)
                } else {
                    Toast.makeText(this,
                        "Please add your delivery address first!",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
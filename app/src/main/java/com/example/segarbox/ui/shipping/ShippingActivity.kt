package com.example.segarbox.ui.shipping

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.core.data.Resource
import com.example.core.data.source.remote.response.AddressItem
import com.example.core.domain.model.ShippingModel
import com.example.core.ui.ShippingAdapter
import com.example.core.utils.Code
import com.example.core.utils.tidyUpJneAndTikiEtd
import com.example.segarbox.R
import com.example.segarbox.databinding.ActivityShippingBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShippingActivity : AppCompatActivity(), View.OnClickListener,
    ShippingAdapter.OnItemClickCallback {

    private var _binding: ActivityShippingBinding? = null
    private val binding get() = _binding!!
    private var city: String? = null
    private var type: String? = null
    private val listShipment = arrayListOf<ShippingModel>()
    private val shippingAdapter = ShippingAdapter(this)
    private val viewModel: ShippingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityShippingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        getAddress()
        setToolbar()
        setAdapter()
        observeData()
        binding.toolbar.ivBack.setOnClickListener(this)
    }

    private fun setToolbar() {
        binding.toolbar.apply {
            ivBack.isVisible = true
            tvTitle.text = getString(R.string.shipment_details)
        }
    }

    private fun getAddress() {
        val address = intent.getParcelableExtra<AddressItem>(Code.ADDRESS_VALUE)?.city

        address?.let {
            val split = it.split(" ")
            type = split[0]

            var cityName = ""
            for (i in 1 until split.size) {

                cityName += if (i == 1) {
                    split[i]
                } else {
                    " " + split[i]
                }
            }

            city = cityName
        }
    }

    private fun observeData() {
        when {
            !city.isNullOrEmpty() && !type.isNullOrEmpty() -> {
                viewModel.getCity(city.toString(), type.toString()).observe(this) { event ->
                    event.getContentIfNotHandled()?.let { resource ->
                        when (resource) {
                            is Resource.Loading -> {
                                viewModel.setLoading(true)
                            }

                            is Resource.Success -> {
                                resource.data?.let {
                                    viewModel.setLoading(false)
                                    viewModel.setDestinationId(it[0].cityId)
                                }
                            }

                            is Resource.Empty -> {
                                viewModel.setLoading(false)
                                Snackbar.make(binding.root,
                                    Code.LOCATION_CANT_BE_REACHED,
                                    Snackbar.LENGTH_SHORT).setAction("OK") {}.show()
                            }

                            else -> {
                                resource.message?.let {
                                    viewModel.setLoading(false)
                                    Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT)
                                        .setAction("OK") {}.show()
                                }
                            }
                        }
                    }
                }
            }

            else -> Snackbar.make(binding.root,
                Code.LOCATION_CANT_BE_REACHED,
                Snackbar.LENGTH_SHORT).setAction("OK") {}.show()
        }


        viewModel.destinationId.observe(this) { event ->
            event.getContentIfNotHandled()?.let {
                viewModel.getShippingCosts(it, "1000")
            }
        }

        viewModel.getShippingCostsResponse.observe(this) { event ->
            event.getContentIfNotHandled()?.let { resource ->
                when(resource) {
                    is Resource.Loading -> {
                        viewModel.setLoading(true)
                    }

                    is Resource.Success -> {
                        resource.data?.let { listShipping ->
                            viewModel.setLoading(false)
                            listShipping.forEach { shipping ->
                                shipping.costs.forEach { costs ->
                                    val shippingModel = ShippingModel()
                                    shippingModel.code = shipping.code.uppercase()
                                    shippingModel.service = costs.service

                                    costs.cost.forEach { cost ->
                                        shippingModel.price = cost.value
                                        shippingModel.etd = cost.etd
                                    }

                                    // Cek Maksimal 1 Hari
                                    when (shippingModel.code) {
                                        Code.TIKI.uppercase() -> {
                                            val etd = shippingModel.etd.tidyUpJneAndTikiEtd(this)
                                                .split(" ")[0].toInt()
                                            if (etd <= 1) {
                                                listShipment.add(shippingModel)
                                            }
                                        }
                                        Code.JNE.uppercase() -> {
                                            val etd = shippingModel.etd.tidyUpJneAndTikiEtd(this)
                                                .split(" ")[0].toInt()
                                            if (etd <= 1) {
                                                listShipment.add(shippingModel)
                                            }
                                        }
                                        else -> {
                                            val etd = shippingModel.etd.tidyUpJneAndTikiEtd(this)
                                                .split(" ")[0].toInt()
                                            if (etd <= 1) {
                                                listShipment.add(shippingModel)
                                            }
                                        }
                                    }
                                    shippingAdapter.submitList(listShipment)
                                }
                            }
                        }
                    }

                    is Resource.Empty -> {
                        viewModel.setLoading(false)
                        Snackbar.make(binding.root, Code.LOCATION_CANT_BE_REACHED, Snackbar.LENGTH_SHORT)
                            .setAction("OK") {}.show()
                    }

                    else -> {
                        resource.message?.let {
                            viewModel.setLoading(false)
                            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).setAction("OK") {}.show()
                        }
                    }
                }
            }
        }

        viewModel.isLoading.observe(this) { event ->
            event.getContentIfNotHandled()?.let { isLoading ->
                binding.progressBar.isVisible = isLoading
            }
        }
    }

    private fun setAdapter() {
        val rv = binding.rvShipping
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = shippingAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> finish()
        }
    }

    override fun onItemClicked(shippingModel: ShippingModel) {
        val intent = Intent()
        intent.putExtra(Code.SHIPPING_VALUE, shippingModel)
        setResult(Code.RESULT_SAVE_SHIPPING, intent)
        finish()
    }
}
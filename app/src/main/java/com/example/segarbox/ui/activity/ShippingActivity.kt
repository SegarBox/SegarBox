package com.example.segarbox.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.segarbox.R
import com.example.segarbox.data.local.model.DummyAddress
import com.example.segarbox.data.local.model.ShippingModel
import com.example.segarbox.data.local.static.Code
import com.example.segarbox.data.repository.RetrofitRepository
import com.example.segarbox.data.repository.RoomRepository
import com.example.segarbox.databinding.ActivityShippingBinding
import com.example.segarbox.ui.adapter.ShippingAdapter
import com.example.segarbox.ui.viewmodel.RetrofitRoomViewModelFactory
import com.example.segarbox.ui.viewmodel.ShippingViewModel
import kotlin.math.cos

class ShippingActivity : AppCompatActivity(), View.OnClickListener,
    ShippingAdapter.OnItemClickCallback {

    private var _binding: ActivityShippingBinding? = null
    private val binding get() = _binding!!
    private var city: String? = null
    private var type: String? = null
    private val listShipment = arrayListOf<ShippingModel>()
    private val shippingAdapter = ShippingAdapter(this)
    private val shippingViewModel by viewModels<ShippingViewModel> {
        RetrofitRoomViewModelFactory.getInstance(RoomRepository(application), RetrofitRepository())
    }

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
        val address = intent.getParcelableExtra<DummyAddress>(Code.ADDRESS_VALUE)?.city

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
        city?.let { city ->
            type?.let { type ->
                when {
                    city.isNotEmpty() && type.isNotEmpty() -> {
                        shippingViewModel.getCity(city, type).observe(this) { list ->

                            when {
                                list.isNotEmpty() -> {
                                    shippingViewModel.setDestinationId(list[0].cityId)
                                }
                                else -> {
                                    Toast.makeText(this,
                                        Code.LOCATION_CANT_BE_REACHED,
                                        Toast.LENGTH_SHORT).show()
                                }
                            }

                        }
                    }
                }
            }
        }

        shippingViewModel.destinationId.observe(this) { destination ->
            shippingViewModel.getShippingCosts(destination, "1000")
        }

        shippingViewModel.shippingCosts.observe(this) { listShipping ->
            listShipping.forEach { shippingResponse ->

                shippingResponse.rajaongkir?.let { shipping ->

                    shipping.results.forEach { shippingResults ->

                        shippingResults.costs.forEach { costs ->
                            val shippingModel = ShippingModel()
                            shippingModel.code = shippingResults.code.uppercase()
                            shippingModel.service = costs.service

                            costs.cost.forEach { cost ->
                                shippingModel.price = cost.value
                                shippingModel.etd = cost.etd
                            }
                            listShipment.add(shippingModel)
                        }
                    }
                }
            }

            shippingAdapter.submitList(listShipment)
        }


        shippingViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.isVisible = isLoading
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
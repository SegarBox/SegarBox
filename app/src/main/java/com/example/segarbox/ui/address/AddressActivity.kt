package com.example.segarbox.ui.address

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.core.data.Resource
import com.example.core.domain.model.Address
import com.example.core.ui.address.AddressAdapter
import com.example.core.utils.Code
import com.example.core.utils.tokenFormat
import com.example.segarbox.R
import com.example.segarbox.databinding.ActivityAddressBinding
import com.example.segarbox.ui.maps.MapsActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddressActivity : AppCompatActivity(), View.OnClickListener,
    AddressAdapter.OnItemAddressClickCallback {

    private var _binding: ActivityAddressBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddressViewModel by viewModels()
    private val addressAdapter = AddressAdapter(this)
    private var token = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        setToolbar()
        setAdapter()
        scrollToTopListAdapter()
        observeData()
        binding.toolbar.ivBack.setOnClickListener(this)
        binding.content.btnAddAddress.setOnClickListener(this)
    }

    private fun setToolbar() {
        binding.toolbar.apply {
            ivBack.isVisible = true
            ivCart.isVisible = false
            tvTitle.text = getString(R.string.address)
        }
    }

    private fun setAdapter() {
        binding.content.rvAddress.apply {
            layoutManager = LinearLayoutManager(this@AddressActivity)
            adapter = addressAdapter
        }
    }

    private fun scrollToTopListAdapter() {
        addressAdapter.registerAdapterDataObserver(object :
            RecyclerView.AdapterDataObserver() {
            override fun onChanged() {}

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {}

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {}

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                binding.content.rvAddress.scrollToPosition(0)
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {}

            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {}
        })
    }

    private fun observeData() {
        viewModel.getToken().observe(this) { event ->
            event.getContentIfNotHandled()?.let { token ->
                this.token = token
                if (token.isNotEmpty())
                    viewModel.getUserAddresses(token.tokenFormat())
            }
        }

        viewModel.getUserAddressesResponse.observe(this) { event ->
            event.getContentIfNotHandled()?.let { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        viewModel.setLoading(true)
                    }

                    is Resource.Success -> {
                        resource.data?.let {
                            binding.ivEmptymap.isVisible = false
                            binding.tvEmptymap.isVisible = false
                            addressAdapter.submitList(it)
                            viewModel.setLoading(false)
                        }
                    }
                    is Resource.Empty -> {
                        binding.ivEmptymap.isVisible = true
                        binding.tvEmptymap.isVisible = true
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

        viewModel.isLoading.observe(this) { event ->
            event.getContentIfNotHandled()?.let { isLoading ->
                binding.progressBar.isVisible = isLoading
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when {
                result.resultCode == Code.RESULT_SNACKBAR && result.data != null -> {
                    val snackbarValue = result.data?.getStringExtra(Code.SNACKBAR_VALUE)
                    snackbarValue?.let {
                        Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).setAction("OK") {}
                            .show()
                    }
                }
            }
        }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> finish()

            R.id.btn_add_address -> {
                resultLauncher.launch(Intent(this, MapsActivity::class.java))
            }
        }
    }

    override fun onAddressClicked(address: Address) {
        val intent = Intent()
        intent.putExtra(Code.ADDRESS_VALUE, address)
        setResult(Code.RESULT_SAVE_ADDRESS, intent)
        finish()
    }

    override fun onStashClicked(address: Address) {
        if (token.isNotEmpty()) {
            viewModel.deleteAddress(token.tokenFormat(), address.id).observe(this) { event ->
                event.getContentIfNotHandled()?.let { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            viewModel.setLoading(true)
                        }

                        is Resource.Success -> {
                            resource.data?.let {
                                Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT)
                                    .setAction("OK") {}.show()
                            }
                            viewModel.getUserAddresses(token.tokenFormat())
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
package com.example.segarbox.ui.address

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.datastore.preferences.preferencesDataStore
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.core.data.RetrofitRepository
import com.example.core.data.source.local.datastore.SettingPreferences
import com.example.core.data.source.remote.response.AddressItem
import com.example.core.ui.address.AddressAdapter
import com.example.core.utils.Code
import com.example.core.utils.tokenFormat
import com.example.segarbox.R
import com.example.segarbox.databinding.ActivityAddressBinding
import com.example.segarbox.ui.maps.MapsActivity
import com.example.segarbox.ui.viewmodel.PrefViewModel
import com.example.segarbox.ui.viewmodel.PrefViewModelFactory
import com.example.segarbox.ui.viewmodel.RetrofitViewModelFactory
import com.google.android.material.snackbar.Snackbar

private val Context.dataStore by preferencesDataStore(name = "settings")
class AddressActivity : AppCompatActivity(), View.OnClickListener,
    AddressAdapter.OnItemAddressClickCallback {

    private var _binding: ActivityAddressBinding? = null
    private val binding get() = _binding!!
    private val addressAdapter = AddressAdapter(this)
    private var token = ""
    private val prefViewModel by viewModels<PrefViewModel> {
        PrefViewModelFactory.getInstance(SettingPreferences.getInstance(dataStore))
    }
    private val addressViewModel by viewModels<AddressViewModel> {
        RetrofitViewModelFactory.getInstance(RetrofitRepository())
    }

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
        prefViewModel.getToken().observe(this) { token ->
            this.token = token
        }

        addressViewModel.addressResponse.observe(this) { addressResponse ->
            addressResponse.data?.let {
                binding.ivEmptymap.isVisible = it.isEmpty()
                binding.tvEmptymap.isVisible = it.isEmpty()
                addressAdapter.submitList(it)
            }
        }

        addressViewModel.deleteAddressResponse.observe(this) { deleteAddressResponse ->
            deleteAddressResponse.info?.let {
                if (token.isNotEmpty()) {
                    addressViewModel.getUserAddresses(token.tokenFormat())
                }
                Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).setAction("OK"){}.show()
            }

            deleteAddressResponse.message?.let {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).setAction("OK"){}.show()
            }
        }

        addressViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.isVisible = isLoading
        }

    }

    override fun onResume() {
        super.onResume()
        if (token.isNotEmpty()) {
            addressViewModel.getUserAddresses(token.tokenFormat())
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
                        Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).setAction("OK"){}.show()
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

    override fun onAddressClicked(addressItem: AddressItem) {
        val intent = Intent()
        intent.putExtra(Code.ADDRESS_VALUE, addressItem)
        setResult(Code.RESULT_SAVE_ADDRESS, intent)
        finish()
    }

    override fun onStashClicked(addressItem: AddressItem) {
        if (token.isNotEmpty()) {
            addressViewModel.deleteAddress(token.tokenFormat(), addressItem.id)
        }
    }
}
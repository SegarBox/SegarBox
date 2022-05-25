package com.example.segarbox.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.segarbox.R
import com.example.segarbox.data.local.model.DummyAddress
import com.example.segarbox.data.local.static.Code
import com.example.segarbox.databinding.ActivityAddressBinding
import com.example.segarbox.ui.adapter.DummyAdapterAddress

class AddressActivity : AppCompatActivity(), View.OnClickListener {

    private var _binding: ActivityAddressBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        setToolbar()
        setAdapter()
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
        val listItem = arrayListOf(
            DummyAddress(),
            DummyAddress(),
            DummyAddress(),
            DummyAddress(),
            DummyAddress(),
            DummyAddress(),
            DummyAddress(),
            DummyAddress(),
            DummyAddress(),
            DummyAddress(),
            DummyAddress(),
            DummyAddress(),
        )

        val adapterAddress = DummyAdapterAddress()
        adapterAddress.submitList(listItem)

        binding.content.rvAddress.apply {
            layoutManager = LinearLayoutManager(this@AddressActivity)
            adapter = adapterAddress
        }
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Code.RESULT_SAVE_ADDRESS && result.data != null) {
                val dummyAddress = result.data?.getParcelableExtra<DummyAddress>(Code.ADDRESS_VALUE)
                dummyAddress?.let {
                    val intent = Intent()
                    intent.putExtra(Code.ADDRESS_VALUE, it)
                    setResult(Code.RESULT_SAVE_ADDRESS, intent)
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

            R.id.btn_add_address -> {
                resultLauncher.launch(Intent(this, MapsActivity::class.java))
            }
        }
    }
}
package com.example.segarbox.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.datastore.preferences.preferencesDataStore
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.segarbox.R
import com.example.segarbox.data.local.datastore.SettingPreferences
import com.example.segarbox.data.local.model.DummyModelCart
import com.example.segarbox.databinding.ActivityCartBinding
import com.example.segarbox.ui.adapter.DummyAdapterCart
import com.example.segarbox.ui.viewmodel.PrefViewModel
import com.example.segarbox.ui.viewmodel.PrefViewModelFactory

private val Context.dataStore by preferencesDataStore(name = "settings")
class CartActivity : AppCompatActivity(), View.OnClickListener {

    private var _binding: ActivityCartBinding? = null
    private val binding get() = _binding!!
    private val prefViewModel by viewModels<PrefViewModel> {
        PrefViewModelFactory.getInstance(SettingPreferences.getInstance(dataStore))
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
        binding.toolbar.ivBack.setOnClickListener(this)
    }

    private fun setupView(){
        binding.bottomPaymentInfo.tvButton.text = "Confirm"
    }

    private fun setAdapter() {
        val listItem = arrayListOf(
            DummyModelCart(),
            DummyModelCart(),
//            DummyModelCart(),
//            DummyModelCart(),
//            DummyModelCart(),
//            DummyModelCart()
        )

        val adapterCart = DummyAdapterCart()
        adapterCart.submitList(listItem)

        binding.content.rvCart.apply {
            layoutManager = LinearLayoutManager(this@CartActivity, LinearLayoutManager.VERTICAL, false)
            binding.content.rvCart.setHasFixedSize(true)
            binding.content.rvCart.adapter = adapterCart
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
            Toast.makeText(this, token, Toast.LENGTH_SHORT).show()

            if (token.isEmpty()) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.iv_back -> {
                finish()
            }
        }
    }

}
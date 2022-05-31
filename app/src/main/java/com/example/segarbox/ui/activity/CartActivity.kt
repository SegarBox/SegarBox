package com.example.segarbox.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.datastore.preferences.preferencesDataStore
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.segarbox.R
import com.example.segarbox.data.local.datastore.SettingPreferences
import com.example.segarbox.data.repository.RetrofitRepository
import com.example.segarbox.databinding.ActivityCartBinding
import com.example.segarbox.helper.tokenFormat
import com.example.segarbox.ui.adapter.CartAdapter
import com.example.segarbox.ui.viewmodel.CartViewModel
import com.example.segarbox.ui.viewmodel.PrefViewModel
import com.example.segarbox.ui.viewmodel.PrefViewModelFactory
import com.example.segarbox.ui.viewmodel.RetrofitViewModelFactory

private val Context.dataStore by preferencesDataStore(name = "settings")
class CartActivity : AppCompatActivity(), View.OnClickListener {

    private var _binding: ActivityCartBinding? = null
    private val binding get() = _binding!!
    private val cartAdapter = CartAdapter()
    private val prefViewModel by viewModels<PrefViewModel> {
        PrefViewModelFactory.getInstance(SettingPreferences.getInstance(dataStore))
    }
    private val cartViewModel by viewModels<CartViewModel> {
        RetrofitViewModelFactory.getInstance(RetrofitRepository())
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
        binding.content.rvCart.apply {
            layoutManager = LinearLayoutManager(this@CartActivity, LinearLayoutManager.VERTICAL, false)
            binding.content.rvCart.setHasFixedSize(true)
            binding.content.rvCart.adapter = cartAdapter
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
            if (token.isEmpty()) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            else {
                Toast.makeText(this, token, Toast.LENGTH_SHORT).show()
                cartViewModel.getUserCart(token.tokenFormat())
            }
        }

        cartViewModel.userCart.observe(this) { userCartResponse ->
            userCartResponse.data?.let {
                cartAdapter.submitList(it)
            }
        }

//        cartViewModel.isLoading.observe(this) { isLoading ->
//
//        }
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
package com.example.segarbox.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.datastore.preferences.preferencesDataStore
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.segarbox.R
import com.example.segarbox.data.local.datastore.SettingPreferences
import com.example.segarbox.data.local.static.Code
import com.example.segarbox.data.remote.response.UserCartItem
import com.example.segarbox.data.repository.RetrofitRepository
import com.example.segarbox.databinding.ActivityCartBinding
import com.example.segarbox.helper.tokenFormat
import com.example.segarbox.ui.adapter.CartAdapter
import com.example.segarbox.ui.viewmodel.CartViewModel
import com.example.segarbox.ui.viewmodel.PrefViewModel
import com.example.segarbox.ui.viewmodel.PrefViewModelFactory
import com.example.segarbox.ui.viewmodel.RetrofitViewModelFactory

private val Context.dataStore by preferencesDataStore(name = "settings")
class CartActivity : AppCompatActivity(), View.OnClickListener,
    CartAdapter.OnItemCartClickCallback {

    private var _binding: ActivityCartBinding? = null
    private val binding get() = _binding!!
    private val cartAdapter = CartAdapter(this)
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
                cartViewModel.getUserCart(token.tokenFormat())
            }
        }

        cartViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.isVisible = isLoading
        }
    }

    override fun onResume() {
        super.onResume()
        cartViewModel.userCart.observe(this) { userCartResponse ->
            userCartResponse.data?.let {
                cartAdapter.submitList(it)
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

    override fun onCheckboxClicked(item: UserCartItem) {
        TODO("Not yet implemented")
    }

    override fun onRemoveClicked(item: UserCartItem) {
        TODO("Not yet implemented")
    }

    override fun onAddClicked(item: UserCartItem) {
        TODO("Not yet implemented")
    }

    override fun onStashClicked(item: UserCartItem) {
        Toast.makeText(this, "DELETE", Toast.LENGTH_SHORT).show()
    }

    override fun onRootClicked(item: UserCartItem) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(Code.KEY_DETAIL_VALUE, item.product)
        startActivity(intent)
    }

}
package com.example.segarbox.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.datastore.preferences.preferencesDataStore
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.segarbox.R
import com.example.segarbox.data.local.datastore.SettingPreferences
import com.example.segarbox.data.local.static.Code
import com.example.segarbox.data.repository.RetrofitRepository
import com.example.segarbox.databinding.ActivityRatingBinding
import com.example.segarbox.helper.tokenFormat
import com.example.segarbox.ui.adapter.RatingAdapter
import com.example.segarbox.ui.viewmodel.PrefViewModel
import com.example.segarbox.ui.viewmodel.PrefViewModelFactory
import com.example.segarbox.ui.viewmodel.RatingViewModel
import com.example.segarbox.ui.viewmodel.RetrofitViewModelFactory
import com.google.android.material.snackbar.Snackbar

private val Context.dataStore by preferencesDataStore(name = "settings")
class RatingActivity : AppCompatActivity(), View.OnClickListener,
    RatingAdapter.OnItemRatingClickCallback {

    private var _binding: ActivityRatingBinding? = null
    private val binding get() = _binding!!
    private var token = ""
    private val ratingAdapter = RatingAdapter(this)
    private val prefViewModel by viewModels<PrefViewModel> {
        PrefViewModelFactory.getInstance(SettingPreferences.getInstance(dataStore))
    }
    private val ratingViewModel by viewModels<RatingViewModel> {
        RetrofitViewModelFactory.getInstance(RetrofitRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRatingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        setToolbar()
        setAdapter()
        observeData()
        binding.toolbar.ivBack.setOnClickListener(this)
        binding.toolbar.ivCart.setOnClickListener(this)
    }

    private fun setToolbar() {
        binding.toolbar.apply {
            ivBack.isVisible = true
            ivCart.isVisible = true
            tvTitle.text = getString(R.string.rating)
        }
    }

    private fun setAdapter() {
        binding.content.rvCart.apply {
            layoutManager = LinearLayoutManager(this@RatingActivity, LinearLayoutManager.VERTICAL, false)
            adapter = ratingAdapter
        }

    }

    private fun observeData() {

        prefViewModel.getToken().observe(this) { token ->
            this.token = token

        }

        ratingViewModel.ratingResponse.observe(this) { ratingResponse ->
            ratingResponse.data?.let {
                ratingAdapter.submitList(it)
            }
        }

        ratingViewModel.userCart.observe(this) { userCartResponse ->
            userCartResponse.meta?.let {
                binding.toolbar.ivCart.badgeValue = it.total
            }
        }

        ratingViewModel.saveRatingResponse.observe(this) { saveRatingResponse ->
            saveRatingResponse.info?.let {
                if (token.isNotEmpty()) {
                    ratingViewModel.getRatings(token.tokenFormat())
                    Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
                }
            }

            saveRatingResponse.message?.let {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
            }
        }

        ratingViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.isVisible = isLoading
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        if (token.isNotEmpty()) {
            ratingViewModel.getRatings(token.tokenFormat())
            ratingViewModel.getUserCart(token.tokenFormat())
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.iv_back -> {
                finish()
            }

            R.id.iv_cart -> {
                startActivity(Intent(this, CartActivity::class.java))
            }
        }
    }

    override fun onRate(ratingId: Int, transactionId: Int, productId: Int, rating: Double) {
        if (token.isNotEmpty()) {
            val formattedRating = (rating + rating).toInt()
            ratingViewModel.saveRating(token.tokenFormat(), ratingId, transactionId, productId, formattedRating)
        }
    }

    override fun onRootClicked(transactionId: Int) {
        val intent = Intent(this, InvoiceActivity::class.java)
        intent.putExtra(Code.KEY_TRANSACTION_ID, transactionId)
        startActivity(intent)
    }
}
package com.example.segarbox.ui.rating

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.core.data.Resource
import com.example.core.ui.RatingAdapter
import com.example.core.utils.Code
import com.example.core.utils.tokenFormat
import com.example.segarbox.R
import com.example.segarbox.databinding.ActivityRatingBinding
import com.example.segarbox.ui.cart.CartActivity
import com.example.segarbox.ui.invoice.InvoiceActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RatingActivity : AppCompatActivity(), View.OnClickListener,
    RatingAdapter.OnItemRatingClickCallback {

    private var _binding: ActivityRatingBinding? = null
    private val binding get() = _binding!!
    private var token = ""
    private val ratingAdapter = RatingAdapter(this)
    private val viewModel: RatingViewModel by viewModels()

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
            layoutManager =
                LinearLayoutManager(this@RatingActivity, LinearLayoutManager.VERTICAL, false)
            adapter = ratingAdapter
        }

    }

    private fun observeData() {

        viewModel.getTokenResponse.observe(this) { event ->
            event.getContentIfNotHandled()?.let { token ->
                this.token = token
                if (token.isNotEmpty()) {
                    viewModel.getRatings(token.tokenFormat())
                    viewModel.getCart(token.tokenFormat())
                } else {
                    binding.toolbar.ivCart.badgeValue = 0
                }
            }
        }

        viewModel.getRatingResponse.observe(this) { event ->
            event.getContentIfNotHandled()?.let { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        viewModel.setLoading(true)
                    }

                    is Resource.Success -> {
                        resource.data?.let {
                            binding.ivEmptyrating.isVisible = false
                            binding.tvEmptyrating.isVisible = false
                            ratingAdapter.submitList(it)
                            viewModel.setLoading(false)
                        }
                    }

                    is Resource.Empty -> {
                        binding.ivEmptyrating.isVisible = true
                        binding.tvEmptyrating.isVisible = true
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

        viewModel.getCartResponse.observe(this) { event ->
            event.getContentIfNotHandled()?.let { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        viewModel.setLoading(true)
                    }

                    is Resource.Success -> {
                        resource.data?.let { listData ->
                            listData[0].total?.let { total ->
                                binding.toolbar.ivCart.badgeValue = total
                            }
                            viewModel.setLoading(false)
                        }
                    }

                    is Resource.Empty -> {
                        binding.toolbar.ivCart.badgeValue = 0
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

    override fun onClick(v: View?) {
        when (v?.id) {
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
            viewModel.saveRating(token.tokenFormat(),
                ratingId,
                transactionId,
                productId,
                formattedRating).observe(this) { event ->
                event.getContentIfNotHandled()?.let { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            viewModel.setLoading(true)
                        }

                        is Resource.Success -> {
                            resource.data?.let {
                                if (token.isNotEmpty()) {
                                    viewModel.getRatings(token.tokenFormat())
                                    Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT)
                                        .setAction("OK") {}.show()
                                }
                                viewModel.setLoading(false)
                            }
                        }

                        else -> {
                            resource.message?.let {
                                Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT)
                                    .setAction("OK") {}.show()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onRootClicked(transactionId: Int) {
        val intent = Intent(this, InvoiceActivity::class.java)
        intent.putExtra(Code.KEY_TRANSACTION_ID, transactionId)
        startActivity(intent)
    }
}
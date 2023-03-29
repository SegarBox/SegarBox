package com.example.segarbox.ui.invoice

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.core.data.Resource
import com.example.core.domain.body.ProductId
import com.example.core.domain.body.UpdateStatusBody
import com.example.core.ui.InvoiceAdapter
import com.example.core.utils.*
import com.example.segarbox.R
import com.example.segarbox.databinding.ActivityInvoiceBinding
import com.example.segarbox.ui.detail.DetailActivity
import com.example.segarbox.ui.home.MainActivity
import com.example.segarbox.ui.rating.RatingActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InvoiceActivity : AppCompatActivity(), View.OnClickListener,
    InvoiceAdapter.OnItemInvoiceClickCallback {

    private var _binding: ActivityInvoiceBinding? = null
    private val binding get() = _binding!!
    private val invoiceAdapter = InvoiceAdapter(this)
    private var token = ""
    private val listProductId: ArrayList<ProductId> = arrayListOf()
    private val viewModel: InvoiceViewModel by viewModels()

    private val getTransactionId
        get(): Int {
            return intent.getIntExtra(Code.KEY_TRANSACTION_ID, 0)
        }

    private val getSnackbarValue
        get(): String? {
            return intent.getStringExtra(Code.SNACKBAR_VALUE)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityInvoiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        setToolbar()
        setAdapter()
        setupView()
        observeData()
        getSnackbarValue?.let {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).setAction("OK") {}.show()
        }
        binding.toolbar.ivBack.setOnClickListener(this)
        binding.bottomPaymentInfo.btnCheckout.setOnClickListener(this)
        binding.content.btnHome.setOnClickListener(this)
        binding.content.btnRating.setOnClickListener(this)
    }

    private fun setupView() {
        binding.bottomPaymentInfo.btnCheckout.text = getString(R.string.complete)
    }

    private fun setToolbar() {
        binding.toolbar.apply {
            tvTitle.text = getString(R.string.invoice)
            ivCart.isVisible = false
            ivBack.isVisible = true
        }
    }

    private fun setAdapter() {
        binding.content.rvCheckoutItem.apply {
            layoutManager = LinearLayoutManager(this@InvoiceActivity)
            adapter = invoiceAdapter
        }
    }

    private fun observeData() {
        viewModel.getToken().observe(this) { event ->
            event.getContentIfNotHandled()?.let {
                this.token = it
                if (token.isNotEmpty()) {
                    if (getTransactionId != 0) {
                        viewModel.getTransactionById(token.tokenFormat(), getTransactionId)
                        viewModel.getUser(token.tokenFormat())
                    }
                }
            }
        }

        viewModel.getUserResponse.observe(this) { event ->
            event.getContentIfNotHandled()?.let { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        viewModel.setLoading(true)
                    }

                    is Resource.Success -> {
                        resource.data?.let {
                            viewModel.setLoading(false)
                            binding.content.tvUserName.text = it.name
                        }
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

        viewModel.getTransactionByIdResponse.observe(this) { event ->
                event.getContentIfNotHandled()?.let { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            viewModel.setLoading(true)
                        }

                        is Resource.Success -> {
                            resource.data?.let {
                                viewModel.setLoading(false)
                                listProductId.clear()
                                it.productTransactions.forEach { productTransactionItem ->
                                    listProductId.add(ProductId(productTransactionItem.productId))
                                }
                                binding.content.apply {
                                    tvInv.text = it.invoiceNumber
                                    tvStatus.text = it.status.formatStatus()
                                    tvDate.text = it.createdAt.formatDateTime()
                                    tvAddress.text = it.address.street
                                    invoiceAdapter.submitList(it.productTransactions)
                                    tvProductsSubtotal.text =
                                        it.subtotalProducts.formatToRupiah()
                                    tvShippingCost.text = it.shippingCost.formatToRupiah()
                                    tvTotalPrice.text = it.totalPrice.formatToRupiah()

                                }
                                if (it.status == "inprogress") {
                                    binding.bottomPaymentInfo.root.isVisible = true
                                    binding.content.btnRating.isVisible = false
                                } else {
                                    binding.bottomPaymentInfo.root.isVisible = false
                                    binding.content.btnRating.isVisible = true
                                }
                                binding.bottomPaymentInfo.tvPrice.text =
                                    it.totalPrice.formatToRupiah()
                            }
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
            R.id.iv_back -> finish()

            R.id.btn_checkout -> {
                if (token.isNotEmpty() && listProductId.isNotEmpty()) {
                    viewModel.updateTransactionStatus(token.tokenFormat(),
                        getTransactionId,
                        UpdateStatusBody(listProductId)).observe(this) { event ->
                        event.getContentIfNotHandled()?.let { resource ->
                            when(resource) {
                                is Resource.Loading -> {
                                    viewModel.setLoading(true)
                                }

                                is Resource.Success -> {
                                    resource.data?.let { message ->
                                        viewModel.setLoading(false)
                                        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).setAction("OK") {}.show()
                                        viewModel.getTransactionById(token.tokenFormat(), getTransactionId)
                                    }
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
                }
            }

            R.id.btn_home -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

            R.id.btn_rating -> {
                startActivity(Intent(this, RatingActivity::class.java))
                finish()
            }

        }
    }

    override fun onItemClicked(productId: Int) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(Code.KEY_DETAIL_VALUE, productId)
        startActivity(intent)
    }


}
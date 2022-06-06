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
import com.example.segarbox.data.local.model.ProductId
import com.example.segarbox.data.local.model.UpdateStatusBody
import com.example.segarbox.data.local.static.Code
import com.example.segarbox.data.repository.RetrofitRepository
import com.example.segarbox.databinding.ActivityInvoiceBinding
import com.example.segarbox.helper.formatDateTime
import com.example.segarbox.helper.formatStatus
import com.example.segarbox.helper.formatToRupiah
import com.example.segarbox.helper.tokenFormat
import com.example.segarbox.ui.adapter.InvoiceAdapter
import com.example.segarbox.ui.viewmodel.InvoiceViewModel
import com.example.segarbox.ui.viewmodel.PrefViewModel
import com.example.segarbox.ui.viewmodel.PrefViewModelFactory
import com.example.segarbox.ui.viewmodel.RetrofitViewModelFactory
import com.google.android.material.snackbar.Snackbar

private val Context.dataStore by preferencesDataStore(name = "settings")

class InvoiceActivity : AppCompatActivity(), View.OnClickListener,
    InvoiceAdapter.OnItemInvoiceClickCallback {

    private var _binding: ActivityInvoiceBinding? = null
    private val binding get() = _binding!!
    private val invoiceAdapter = InvoiceAdapter(this)
    private var token = ""
    private val listProductId: ArrayList<ProductId> = arrayListOf()
    private val prefViewModel by viewModels<PrefViewModel> {
        PrefViewModelFactory.getInstance(SettingPreferences.getInstance(dataStore))
    }
    private val invoiceViewModel by viewModels<InvoiceViewModel> {
        RetrofitViewModelFactory.getInstance(RetrofitRepository())
    }

    private val getTransactionId
        get(): Int {
            return intent.getIntExtra(Code.KEY_TRANSACTION_ID, 0)
        }

    private val getSnackbarValue get(): String? {
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
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).setAction("OK"){}.show()
        }
        binding.toolbar.ivBack.setOnClickListener(this)
        binding.bottomPaymentInfo.checkoutLayout.setOnClickListener(this)
        binding.content.btnHome.setOnClickListener(this)
        binding.content.btnRating.setOnClickListener(this)
    }

    private fun setupView() {
        binding.bottomPaymentInfo.tvButton.text = getString(R.string.complete)
    }

    private fun setToolbar() {
        binding.toolbar.apply {
            tvTitle.text = getString(R.string.invoice)
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
        prefViewModel.getToken().observe(this) { token ->
            this.token = token
            if (token.isNotEmpty()) {
                if (getTransactionId != 0) {
                    invoiceViewModel.getTransactionById(token.tokenFormat(), getTransactionId)
                    invoiceViewModel.getUser(token.tokenFormat())
                }
            }
        }

        invoiceViewModel.transactionById.observe(this) { response ->
            response.data?.let {
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
                    tvProductsSubtotal.text = it.subtotalProducts.formatToRupiah()
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
                binding.bottomPaymentInfo.tvPrice.text = it.totalPrice.formatToRupiah()
            }
        }

        invoiceViewModel.userResponse.observe(this) { userResponse ->
            userResponse.data?.let {
                binding.content.tvUserName.text = it.name
            }
        }

        invoiceViewModel.updateTransactionStatusResponse.observe(this) { response ->
            response.info?.let {
                if (token.isNotEmpty()) {
                    invoiceViewModel.getTransactionById(token.tokenFormat(), getTransactionId)
                    Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).setAction("OK"){}.show()
                }
            }

            response.message?.let {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).setAction("OK"){}.show()
            }
        }

        invoiceViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.isVisible = isLoading
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> finish()

            R.id.checkout_layout -> {
                if (token.isNotEmpty() && listProductId.isNotEmpty()) {
                    Log.e("LIST PRODUCT ID", listProductId.toString())
                    invoiceViewModel.updateTransactionStatus(token.tokenFormat(), getTransactionId, UpdateStatusBody(listProductId))
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
        finish()
    }


}
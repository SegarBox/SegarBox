package com.example.segarbox.ui.checkout

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.core.data.Resource
import com.example.core.domain.body.MakeOrderBody
import com.example.core.domain.body.ProductTransactions
import com.example.core.domain.model.Address
import com.example.core.domain.model.CartDetail
import com.example.core.domain.model.ShippingModel
import com.example.core.domain.model.User
import com.example.core.ui.CheckoutDetailsAdapter
import com.example.core.utils.Code
import com.example.core.utils.formatToRupiah
import com.example.core.utils.tidyUpJneAndTikiEtd
import com.example.core.utils.tokenFormat
import com.example.segarbox.BuildConfig
import com.example.segarbox.R
import com.example.segarbox.databinding.ActivityCheckoutBinding
import com.example.segarbox.ui.address.AddressActivity
import com.example.segarbox.ui.invoice.InvoiceActivity
import com.example.segarbox.ui.shipping.ShippingActivity
import com.google.android.material.snackbar.Snackbar
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback
import com.midtrans.sdk.corekit.core.MidtransSDK
import com.midtrans.sdk.corekit.core.TransactionRequest
import com.midtrans.sdk.corekit.core.UIKitCustomSetting
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme
import com.midtrans.sdk.corekit.models.BillingAddress
import com.midtrans.sdk.corekit.models.CustomerDetails
import com.midtrans.sdk.corekit.models.ItemDetails
import com.midtrans.sdk.corekit.models.ShippingAddress
import com.midtrans.sdk.corekit.models.snap.TransactionResult
import com.midtrans.sdk.uikit.SdkUIFlowBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckoutActivity : AppCompatActivity(), View.OnClickListener, TransactionFinishedCallback {

    private var _binding: ActivityCheckoutBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CheckoutViewModel by viewModels()
    private var address: Address? = null
    private var costs: CartDetail? = null
    private val listProductTransactions: ArrayList<ProductTransactions> = arrayListOf()
    private val listMidtransItemDetails: ArrayList<ItemDetails> = arrayListOf()
    private var user: User? = null
    private var makeOrderId: Int = 0
    private var token = ""
    private var isShippingCostAdded = false
    private val checkoutDetailsAdapter = CheckoutDetailsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        setToolbar()
        observeData()
        initMidtransSdk()
        setAdapter()
        binding.bottomPaymentInfo.btnCheckout.text = getString(R.string.make_order)
        binding.toolbar.ivBack.setOnClickListener(this)
        binding.content.btnChooseAddress.setOnClickListener(this)
        binding.content.layoutShipping.setOnClickListener(this)
        binding.bottomPaymentInfo.btnCheckout.setOnClickListener(this)
    }

    private fun setToolbar() {
        binding.toolbar.apply {
            tvTitle.text = getString(R.string.checkout)
            ivCart.isVisible = false
            ivBack.isVisible = true
        }
    }

    private fun initMidtransSdk() {
        SdkUIFlowBuilder.init()
            .setMerchantBaseUrl(BuildConfig.MERCHANT_BASE_CHECKOUT_URL) //set merchant url
            .setClientKey(BuildConfig.MERCHANT_CLIENT_KEY) // client_key is mandatory
            .setContext(applicationContext) // context is mandatory
            .setTransactionFinishedCallback(this) // set transaction finish callback (sdk callback)
            .setUIkitCustomSetting(uiKitCustomSetting())
            .enableLog(true) // enable sdk log
            .setColorTheme(CustomColorTheme(
                "#02B80E",
                "#02B80E",
                "#02B80E")) // will replace theme on snap theme on MAP
            .setLanguage("en")
            .buildSDK()
    }

    private fun uiKitCustomSetting(): UIKitCustomSetting {
        val uIKitCustomSetting = UIKitCustomSetting()
        uIKitCustomSetting.isSkipCustomerDetailsPages = true
        uIKitCustomSetting.isShowPaymentStatus = true
        return uIKitCustomSetting
    }

    private fun initShippingAddress(
        address: String,
        name: String,
        phone: String,
        city: String,
        postalCode: String,
    ): ShippingAddress {
        val shippingAddress = ShippingAddress()
        shippingAddress.address = address
        shippingAddress.firstName = name
        shippingAddress.phone = phone
        shippingAddress.city = city
        shippingAddress.postalCode = postalCode
        return shippingAddress
    }

    private fun initBillingAddress(
        address: String,
        name: String,
        phone: String,
        city: String,
        postalCode: String,
    ): BillingAddress {
        val billingAddress = BillingAddress()
        billingAddress.address = address
        billingAddress.firstName = name
        billingAddress.phone = phone
        billingAddress.city = city
        billingAddress.postalCode = postalCode
        return billingAddress
    }

    private fun setAdapter() {
        binding.content.rvCheckoutItem.apply {
            layoutManager =
                LinearLayoutManager(this@CheckoutActivity, LinearLayoutManager.VERTICAL, false)
            adapter = checkoutDetailsAdapter
        }
    }

    private fun observeData() {

        viewModel.getTokenResponse.observe(this) { event ->
            event.getContentIfNotHandled()?.let { token ->
                this.token = token
                if (token.isNotEmpty()) {
                    viewModel.getCheckedCart(token.tokenFormat())

                    // Get User buat Midtrans
                    viewModel.getUser(token.tokenFormat())

                    if (costs != null) {
                        costs?.let { cartDetail ->
                            viewModel.getCartDetail(
                                token = token.tokenFormat(),
                                shippingCost = cartDetail.shippingCost
                            )
                        }
                    } else {
                        viewModel.getCartDetail(
                            token = token.tokenFormat(),
                            shippingCost = 0
                        )
                    }
                }
            }
        }

        viewModel.getUserResponse.observe(this) { event ->
            event.getContentIfNotHandled()?.let { resource ->
                when (resource) {
                    is Resource.Loading -> viewModel.setLoading(true)

                    is Resource.Success -> {
                        resource.data?.let { user ->
                            this.user = user
                        }
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

        viewModel.getCheckedCartResponse.observe(this) { event ->
            event.getContentIfNotHandled()?.let { resource ->
                when (resource) {
                    is Resource.Loading -> viewModel.setLoading(true)

                    is Resource.Success -> {
                        resource.data?.let {
                            checkoutDetailsAdapter.submitList(it)
                            listProductTransactions.clear()
                            listMidtransItemDetails.clear()
                            it.forEach { cart ->
                                // Buat Segarbox API
                                listProductTransactions.add(
                                    ProductTransactions(
                                        cart.product.id,
                                        cart.productQty
                                    )
                                )

                                // Buat Midtrans API
                                listMidtransItemDetails.add(
                                    ItemDetails(
                                        cart.product.id.toString(),
                                        cart.product.price.toDouble(),
                                        cart.productQty,
                                        cart.product.label
                                    )
                                )
                            }
                            viewModel.setLoading(false)
                        }
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

        viewModel.getCartDetailResponse.observe(this) { event ->
            event.getContentIfNotHandled()?.let { resource ->
                when (resource) {
                    is Resource.Loading -> viewModel.setLoading(true)

                    is Resource.Success -> {
                        resource.data?.let {
                            this.costs = it
                            binding.content.apply {
                                tvProductsSubtotal.text = it.subtotalProducts.formatToRupiah()
                                tvShippingCost.text = it.shippingCost.formatToRupiah()
                                tvTotalPrice.text = it.totalPrice.formatToRupiah()
                            }
                            binding.bottomPaymentInfo.tvPrice.text = it.totalPrice.formatToRupiah()
                            viewModel.setLoading(false)
                        }
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
                binding.bottomPaymentInfo.btnCheckout.isEnabled = !isLoading
            }
        }

    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when {
                result.resultCode == Code.RESULT_SAVE_ADDRESS && result.data != null -> {

                    // Jika tambah address, otomatis shipping cost ke reset
                    binding.content.contentShipping.root.isVisible = false
                    if (token.isNotEmpty()) {
                        viewModel.getCartDetail(token.tokenFormat(), 0)
                    }

                    address =
                        result.data?.getParcelableExtra(Code.ADDRESS_VALUE)

                    address?.let {
                        isShippingCostAdded = false
                        binding.content.tvAddress.apply {
                            isVisible = true
                            text = it.street
                        }
                    }

                }

                result.resultCode == Code.RESULT_SAVE_SHIPPING && result.data != null -> {

                    val shippingModel =
                        result.data?.getParcelableExtra<ShippingModel>(Code.SHIPPING_VALUE)

                    shippingModel?.let {
                        if (token.isNotEmpty()) {
                            viewModel.getCartDetail(token.tokenFormat(), it.price)
                        }

                        binding.content.contentShipping.apply {
                            isShippingCostAdded = true
                            root.isVisible = true
                            when (it.code) {
                                Code.TIKI.uppercase() -> {
                                    Glide.with(this@CheckoutActivity)
                                        .load(R.drawable.tiki)
                                        .into(ivKurir)

                                    tvEtd.text = it.etd.tidyUpJneAndTikiEtd(this@CheckoutActivity)
                                }
                                Code.JNE.uppercase() -> {
                                    Glide.with(this@CheckoutActivity)
                                        .load(R.drawable.jne)
                                        .into(ivKurir)

                                    tvEtd.text = it.etd.tidyUpJneAndTikiEtd(this@CheckoutActivity)
                                }
                                else -> {
                                    Glide.with(this@CheckoutActivity)
                                        .load(R.drawable.pos)
                                        .into(ivKurir)

                                    tvEtd.text = it.etd.tidyUpJneAndTikiEtd(this@CheckoutActivity)
                                }
                            }
                            tvKurir.text = it.code
                            tvService.text = it.service
                            tvPrice.text = it.price.formatToRupiah()
                        }

                    }
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

            R.id.btn_choose_address -> {
                resultLauncher.launch(Intent(this, AddressActivity::class.java))
            }

            R.id.layout_shipping -> {
                if (address != null) {
                    val intent = Intent(this, ShippingActivity::class.java)
                    intent.putExtra(Code.ADDRESS_VALUE, address)
                    resultLauncher.launch(intent)
                } else {
                    Snackbar.make(binding.root,
                        "Please add your delivery address first!",
                        Snackbar.LENGTH_SHORT).setAction("OK") {}.show()
                }
            }

            R.id.btn_checkout -> {
                if (isShippingCostAdded) {
                    if (token.isNotEmpty() &&
                        address != null &&
                        costs != null &&
                        user != null &&
                        listProductTransactions.isNotEmpty() &&
                        listMidtransItemDetails.isNotEmpty()
                    ) {
                        // Customer Detail for Midtrans
                        val mCustomerDetails = CustomerDetails()
                        mCustomerDetails.firstName = user!!.name
                        mCustomerDetails.email = user!!.email
                        mCustomerDetails.phone = user!!.phone
                        mCustomerDetails.shippingAddress = initShippingAddress(
                            address!!.street,
                            user!!.name,
                            user!!.phone,
                            address!!.city,
                            address!!.postalCode
                        )
                        mCustomerDetails.billingAddress = initBillingAddress(
                            address!!.street,
                            user!!.name,
                            user!!.phone,
                            address!!.city,
                            address!!.postalCode
                        )

                        // Make Order Segarbox API
                        val makeOrderBody = MakeOrderBody(
                            token.tokenFormat(),
                            address!!.id,
                            costs!!.qtyTransaction,
                            costs!!.subtotalProducts,
                            costs!!.totalPrice,
                            costs!!.shippingCost,
                            listProductTransactions
                        )

                        viewModel.makeOrder(token.tokenFormat(), makeOrderBody)
                            .observe(this) { event ->
                                event.getContentIfNotHandled()?.let { resource ->
                                    when (resource) {
                                        is Resource.Loading -> {
                                            viewModel.setLoading(true)
                                        }

                                        is Resource.Success -> {
                                            resource.data?.let { makeOrder ->
                                                this.makeOrderId = makeOrder.id

                                                // Start open Midtrans Activity
                                                val transactionRequest =
                                                    TransactionRequest(makeOrder.id.toString(),
                                                        costs!!.totalPrice.toDouble())
                                                transactionRequest.customerDetails =
                                                    mCustomerDetails
                                                // Add Shipping Cost to list item
                                                listMidtransItemDetails.add(
                                                    ItemDetails(
                                                        "Shipping",
                                                        costs!!.shippingCost.toDouble(),
                                                        1,
                                                        "Shipping Cost"
                                                    )
                                                )
                                                transactionRequest.itemDetails =
                                                    listMidtransItemDetails

                                                val midtransSDK = MidtransSDK.getInstance()
                                                midtransSDK.transactionRequest = transactionRequest
                                                midtransSDK.startPaymentUiFlow(this)

                                                viewModel.setLoading(false)

                                            }
                                        }

                                        else -> {
                                            resource.message?.let {
                                                Snackbar.make(binding.root,
                                                    it,
                                                    Snackbar.LENGTH_SHORT).setAction("OK") {}.show()
                                                viewModel.setLoading(false)
                                            }
                                        }
                                    }
                                }
                            }
                    }

                } else {
                    Snackbar.make(binding.root,
                        "Please add your shipping method first",
                        Snackbar.LENGTH_SHORT).setAction("OK") {}.show()
                }
            }
        }
    }

    override fun onTransactionFinished(result: TransactionResult?) {
        if (this.makeOrderId != 0) {
            val intent = Intent(this, InvoiceActivity::class.java)
            intent.putExtra(Code.KEY_TRANSACTION_ID, this.makeOrderId)
            intent.putExtra(Code.SNACKBAR_VALUE, "Successfully making order!")
            startActivity(intent)
            finish()
        }
    }
}
package com.example.segarbox.ui.dev

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.segarbox.BuildConfig
import com.example.segarbox.R
import com.example.segarbox.databinding.ActivityMidtransBinding
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

class MidtransActivity : AppCompatActivity(), TransactionFinishedCallback, View.OnClickListener {

    private var _binding: ActivityMidtransBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMidtransBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        initMidtransSdk()
    }

    private fun init() {
        binding.btnPesan.setOnClickListener(this)
    }

    private fun initMidtransSdk() {
        val clientKey: String = BuildConfig.MERCHANT_CLIENT_KEY
        val baseUrl: String = BuildConfig.MERCHANT_BASE_CHECKOUT_URL
        val sdkUIFlowBuilder: SdkUIFlowBuilder = SdkUIFlowBuilder.init()
            .setClientKey("SB-Mid-client-5qnz8GGU1cEM80I1") // client_key is mandatory
            .setContext(this) // context is mandatory
            .setTransactionFinishedCallback(this) // set transaction finish callback (sdk callback)
            .setMerchantBaseUrl("https://segarbox-midtrans.000webhostapp.com/midtrans/charge/") //set merchant url
            .setUIkitCustomSetting(uiKitCustomSetting())
            .enableLog(true) // enable sdk log
            .setColorTheme(CustomColorTheme("#FFE51255",
                "#B61548",
                "#FFE51255")) // will replace theme on snap theme on MAP
            .setLanguage("en")
        sdkUIFlowBuilder.buildSDK()
    }

    private fun uiKitCustomSetting(): UIKitCustomSetting {
        val uIKitCustomSetting = UIKitCustomSetting()
        uIKitCustomSetting.isSkipCustomerDetailsPages = true
        uIKitCustomSetting.isShowPaymentStatus = true
        return uIKitCustomSetting
    }

    private fun initShippingAddress(): ShippingAddress {
        val shippingAddress = ShippingAddress()
        shippingAddress.address =  "Jl Kenangan Terindah 18b"
        shippingAddress.firstName = "Stanley"
        shippingAddress.lastName = "Mesa"
        shippingAddress.phone = "085310102020"
        shippingAddress.city = "Semarang"
        shippingAddress.countryCode = "+62"
        shippingAddress.postalCode = "50144"
        return shippingAddress
    }

    private fun initBillingAddress(): BillingAddress {
        val billingAddress = BillingAddress()
        billingAddress.address =  "Jl Kenangan Terindah 18b"
        billingAddress.firstName = "Stanley"
        billingAddress.lastName = "Mesa"
        billingAddress.phone = "085310102020"
        billingAddress.city = "Semarang"
        billingAddress.countryCode = "+62"
        billingAddress.postalCode = "50144"
        return billingAddress
    }

    private fun initCustomerDetails(): CustomerDetails {
        //define customer detail (mandatory for coreflow)
        val mCustomerDetails = CustomerDetails()
        mCustomerDetails.customerIdentifier = "identifier"
        mCustomerDetails.firstName = "Stanley"
        mCustomerDetails.lastName = "Mesa"
        mCustomerDetails.email = "mail@mail.com"
        mCustomerDetails.phone = "085310102020"
        mCustomerDetails.shippingAddress = initShippingAddress()
        mCustomerDetails.billingAddress = initBillingAddress()
        return mCustomerDetails
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onTransactionFinished(p0: TransactionResult?) {
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_pesan -> {
                val itemDetails = arrayListOf(
                    ItemDetails("idbarang1", 25000.0, 1, "Sayur Bayam"),
                    ItemDetails("idbarang2", 25000.0, 1, "Tomat"),
                )
                val transactionRequest =
                    TransactionRequest("SegarBox-" + System.currentTimeMillis().toString(), 50000.0)
                transactionRequest.customerDetails = initCustomerDetails()
                transactionRequest.itemDetails = itemDetails

                val midtransSDK = MidtransSDK.getInstance()
                midtransSDK.transactionRequest = transactionRequest
                midtransSDK.startPaymentUiFlow(this)

            }
        }
    }
}
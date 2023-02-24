package com.example.segarbox.ui.dev

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.core.utils.getHelperColor
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

    private fun initShippingAddress(): ShippingAddress {
        val shippingAddress = ShippingAddress()
        shippingAddress.address = "Jl Kenangan Terindah 18b"
        shippingAddress.firstName = "Stanley"
        shippingAddress.lastName = "Mesa"
        shippingAddress.phone = "085310102020"
        shippingAddress.city = "Semarang"
        shippingAddress.postalCode = "50144"
        return shippingAddress
    }

    private fun initBillingAddress(): BillingAddress {
        val billingAddress = BillingAddress()
        billingAddress.address = "Jl Kenangan Terindah 18b"
        billingAddress.firstName = "Stanley"
        billingAddress.lastName = "Mesa"
        billingAddress.phone = "085310102020"
        billingAddress.city = "Semarang"
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

    override fun onTransactionFinished(result: TransactionResult?) {
        if (result != null) {
            if (result.isTransactionCanceled) {
                Toast.makeText(this, "Transaction Canceled", Toast.LENGTH_LONG).show()
            } else {
                when (result.status) {
                    TransactionResult.STATUS_SUCCESS -> Toast.makeText(this,
                        "Transaction Finished. ID: " + result.response.transactionId,
                        Toast.LENGTH_LONG).show()
                    TransactionResult.STATUS_PENDING -> Toast.makeText(this,
                        "Transaction Pending. ID: " + result.response.transactionId,
                        Toast.LENGTH_LONG).show()
                    TransactionResult.STATUS_FAILED -> Toast.makeText(this,
                        "Transaction Failed. ID: " + result.response.transactionId.toString() + ". Message: " + result.response.statusMessage,
                        Toast.LENGTH_LONG).show()
                    TransactionResult.STATUS_INVALID -> Toast.makeText(this,
                        "Transaction Invalid",
                        Toast.LENGTH_LONG).show()
                }
            }
        } else {
            Toast.makeText(this, "Transaction Finished with failure.", Toast.LENGTH_LONG).show()
        }
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
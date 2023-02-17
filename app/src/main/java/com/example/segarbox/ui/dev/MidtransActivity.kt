package com.example.segarbox.ui.dev

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.segarbox.BuildConfig
import com.example.segarbox.R
import com.example.segarbox.databinding.ActivityMidtransBinding
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback
import com.midtrans.sdk.corekit.core.TransactionRequest
import com.midtrans.sdk.corekit.core.UIKitCustomSetting
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme
import com.midtrans.sdk.corekit.models.snap.CustomerDetails
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
            .setClientKey(clientKey) // client_key is mandatory
            .setContext(this) // context is mandatory
            .setTransactionFinishedCallback(this) // set transaction finish callback (sdk callback)
            .setMerchantBaseUrl(baseUrl) //set merchant url
            .setUIkitCustomSetting(uiKitCustomSetting())
            .enableLog(true) // enable sdk log
            .setColorTheme(CustomColorTheme("#FFE51255", "#B61548", "#FFE51255")) // will replace theme on snap theme on MAP
            .setLanguage("en")
        sdkUIFlowBuilder.buildSDK()
    }

    private fun uiKitCustomSetting(): UIKitCustomSetting {
        val uIKitCustomSetting = UIKitCustomSetting()
        uIKitCustomSetting.isSkipCustomerDetailsPages = true
        uIKitCustomSetting.isShowPaymentStatus = true
        return uIKitCustomSetting
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onTransactionFinished(p0: TransactionResult?) {
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btn_pesan -> {
                val transactionRequest = TransactionRequest("SegarBox-" + System.currentTimeMillis().toString(), 50000.0)
            }
        }
    }
}
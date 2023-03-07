package com.example.segarbox.ui.dev

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import com.example.core.data.Resource
import com.example.core.domain.model.MidtransStatus
import com.example.core.utils.Event
import com.example.core.domain.usecase.InvoiceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DevViewModel @Inject constructor(private val invoiceUseCase: InvoiceUseCase): ViewModel() {
    fun getMidtransStatus(orderId: String): LiveData<Event<Resource<MidtransStatus>>> =
        invoiceUseCase.getMidtransStatus(orderId).asLiveData().map {
            Event(it)
        }
}
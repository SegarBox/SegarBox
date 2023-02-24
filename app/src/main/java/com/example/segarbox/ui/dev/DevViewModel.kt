package com.example.segarbox.ui.dev

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.core.data.Resource
import com.example.core.domain.model.Product
import com.example.core.domain.usecase.HomeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DevViewModel @Inject constructor(private val homeUseCase: HomeUseCase): ViewModel() {
    fun getAllProducts(): LiveData<Resource<List<Product>>> = homeUseCase.getAllProducts(1, 20).asLiveData()
}
package com.example.segarbox.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.RetrofitRepository
import com.example.core.data.source.remote.response.RegisterResponse
import kotlinx.coroutines.launch

class RegisterViewModel(private val retrofitRepository: RetrofitRepository) : ViewModel() {

    private val _registerResponse = MutableLiveData<RegisterResponse>()
    val registerResponse: LiveData<RegisterResponse> = _registerResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun register(
        name: String,
        email: String,
        phone: String,
        password: String,
        password_confirmation: String
    ) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val response =
                retrofitRepository.register(name, email, phone, password, password_confirmation)
            _registerResponse.postValue(response)
            _isLoading.postValue(false)
        }
    }
}
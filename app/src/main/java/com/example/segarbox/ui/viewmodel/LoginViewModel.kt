package com.example.segarbox.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.segarbox.data.remote.response.LoginResponse
import com.example.segarbox.data.repository.RetrofitRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val retrofitRepository: RetrofitRepository): ViewModel() {

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val response = retrofitRepository.login(email, password)
            _loginResponse.postValue(response)
            _isLoading.postValue(false)
        }
    }
}
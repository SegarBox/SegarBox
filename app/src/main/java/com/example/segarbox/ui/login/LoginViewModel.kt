package com.example.segarbox.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.segarbox.core.data.source.remote.response.LoginResponse
import com.example.segarbox.core.data.RetrofitRepository
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
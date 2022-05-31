package com.example.segarbox.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.segarbox.data.remote.response.UserResponse
import com.example.segarbox.data.repository.RetrofitRepository
import kotlinx.coroutines.launch

class ProfileViewModel (private val retrofitRepository: RetrofitRepository): ViewModel() {
    private val _userResponse = MutableLiveData<UserResponse>()
    val userResponse: LiveData<UserResponse> = _userResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun user(token: String) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val response = retrofitRepository.getUser(token)
            _userResponse.postValue(response)
            _isLoading.postValue(false)
        }
    }
}
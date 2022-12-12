package com.example.segarbox.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.RetrofitRepository
import com.example.core.data.source.remote.response.UserCartResponse
import com.example.core.data.source.remote.response.UserResponse
import com.example.core.utils.Event
import kotlinx.coroutines.launch

class ProfileViewModel (private val retrofitRepository: RetrofitRepository): ViewModel() {
    private val _userResponse = MutableLiveData<Event<UserResponse>>()
    val userResponse: LiveData<Event<UserResponse>> = _userResponse

    private var _userCart = MutableLiveData<Event<UserCartResponse>>()
    val userCart: LiveData<Event<UserCartResponse>> = _userCart

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getUser(token: String) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val response = retrofitRepository.getUser(token)
            _userResponse.postValue(Event(response))
            _isLoading.postValue(false)
        }
    }

    fun getUserCart(token: String) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val request = retrofitRepository.getUserCart(token)
            _userCart.postValue(Event(request))
            _isLoading.postValue(false)
        }
    }

    fun logout(token: String) {
        viewModelScope.launch {
            val response = retrofitRepository.logout(token)
        }
    }
}
package com.example.segarbox.ui.profile

import androidx.lifecycle.*
import com.example.core.data.Repository
import com.example.core.data.Resource
import com.example.core.data.source.remote.response.UserCartResponse
import com.example.core.data.source.remote.response.UserResponse
import com.example.core.domain.model.Cart
import com.example.core.domain.model.User
import com.example.core.domain.usecase.ProfileUseCase
import com.example.core.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor (private val profileUseCase: ProfileUseCase): ViewModel() {

    fun getUser(token: String): LiveData<Event<Resource<User>>> =
        profileUseCase.getUser(token).asLiveData().map {
            Event(it)
        }

    fun getCart(token: String): LiveData<Event<Resource<List<Cart>>>> =
        profileUseCase.getCart(token).asLiveData().map {
            Event(it)
        }

    fun logout(token: String): LiveData<Event<Resource<String>>> =
        profileUseCase.logout(token).asLiveData().map {
            Event(it)
        }

//    private val _userResponse = MutableLiveData<Event<UserResponse>>()
//    val userResponse: LiveData<Event<UserResponse>> = _userResponse
//
//    private var _userCart = MutableLiveData<Event<UserCartResponse>>()
//    val userCart: LiveData<Event<UserCartResponse>> = _userCart
//
//    private val _isLoading = MutableLiveData<Boolean>()
//    val isLoading: LiveData<Boolean> = _isLoading
//
//    fun getUser(token: String) {
//        viewModelScope.launch {
//            _isLoading.postValue(true)
//            val response = retrofitRepository.getUser(token)
//            _userResponse.postValue(Event(response))
//            _isLoading.postValue(false)
//        }
//    }
//
//    fun getUserCart(token: String) {
//        viewModelScope.launch {
//            _isLoading.postValue(true)
//            val request = retrofitRepository.getUserCart(token)
//            _userCart.postValue(Event(request))
//            _isLoading.postValue(false)
//        }
//    }
//
//    fun logout(token: String) {
//        viewModelScope.launch {
//            val response = retrofitRepository.logout(token)
//        }
//    }
}
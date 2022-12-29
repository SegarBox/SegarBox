package com.example.segarbox.ui.profile

import androidx.lifecycle.*
import com.example.core.data.Resource
import com.example.core.domain.model.Cart
import com.example.core.domain.model.User
import com.example.core.domain.usecase.ProfileUseCase
import com.example.core.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor (private val profileUseCase: ProfileUseCase): ViewModel() {

    private val _getUserResponse = MutableLiveData<Event<Resource<User>>>()
    val getUserResponse: LiveData<Event<Resource<User>>> = _getUserResponse

    private val _getCartResponse = MutableLiveData<Event<Resource<List<Cart>>>>()
    val getCartResponse: LiveData<Event<Resource<List<Cart>>>> = _getCartResponse

    private val _isLoading = MutableLiveData<Event<Boolean>>()
    val isLoading: LiveData<Event<Boolean>> = _isLoading

    fun getUser(token: String) =
        profileUseCase.getUser(token).asLiveData().map {
            _getUserResponse.postValue(Event(it))
        }

    fun getCart(token: String) =
        profileUseCase.getCart(token).asLiveData().map {
            _getCartResponse.postValue(Event(it))
        }

    fun logout(token: String): LiveData<Event<Resource<String>>> =
        profileUseCase.logout(token).asLiveData().map {
            Event(it)
        }

    fun deleteToken() =
        profileUseCase.deleteToken()

    fun getToken(): LiveData<Event<String>> =
        profileUseCase.getToken().asLiveData().map {
            Event(it)
        }

    fun getTheme(): LiveData<Event<Boolean>> =
        profileUseCase.getTheme().asLiveData().map {
            Event(it)
        }

    fun saveTheme(isDarkMode: Boolean) =
        profileUseCase.saveTheme(isDarkMode)

    fun setLoading(isLoading: Boolean) =
        _isLoading.postValue(Event(isLoading))

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
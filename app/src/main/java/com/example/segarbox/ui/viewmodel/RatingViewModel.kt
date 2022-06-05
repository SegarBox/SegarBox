package com.example.segarbox.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.segarbox.data.remote.response.*
import com.example.segarbox.data.repository.RetrofitRepository
import kotlinx.coroutines.launch

class RatingViewModel(private val retrofitRepository: RetrofitRepository): ViewModel() {

    private val _ratingResponse = MutableLiveData<RatingResponse>()
    val ratingResponse: LiveData<RatingResponse> = _ratingResponse

    private val _saveRatingResponse = MutableLiveData<SaveRatingResponse>()
    val saveRatingResponse: LiveData<SaveRatingResponse> = _saveRatingResponse

    private var _userCart = MutableLiveData<UserCartResponse>()
    val userCart: LiveData<UserCartResponse> = _userCart

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getRatings(token: String) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val request = retrofitRepository.getRatings(token)
            _ratingResponse.postValue(request)
            _isLoading.postValue(false)
        }
    }

    fun getUserCart(token: String) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val request = retrofitRepository.getUserCart(token)
            _userCart.postValue(request)
            _isLoading.postValue(false)
        }
    }

    fun saveRating(token: String, ratingId: Int, transactionId: Int, productId: Int, rating: Int) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val request = retrofitRepository.saveRating(token, ratingId, transactionId, productId, rating)
            _saveRatingResponse.postValue(request)
            _isLoading.postValue(false)
        }
    }

}
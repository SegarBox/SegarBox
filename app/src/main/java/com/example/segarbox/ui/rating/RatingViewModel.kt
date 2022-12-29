package com.example.segarbox.ui.rating

import androidx.lifecycle.*
import com.example.core.data.Resource
import com.example.core.domain.model.Cart
import com.example.core.domain.model.Rating
import com.example.core.domain.usecase.RatingUseCase
import com.example.core.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RatingViewModel @Inject constructor(private val ratingUseCase: RatingUseCase) : ViewModel() {

    private val _getRatingsResponse = MutableLiveData<Event<Resource<List<Rating>>>>()
    val getRatingResponse: LiveData<Event<Resource<List<Rating>>>> = _getRatingsResponse

    private val _getCartResponse = MutableLiveData<Event<Resource<List<Cart>>>>()
    val getCartResponse: LiveData<Event<Resource<List<Cart>>>> = _getCartResponse

    private val _isLoading = MutableLiveData<Event<Boolean>>()
    val isLoading: LiveData<Event<Boolean>> = _isLoading

    fun getRatings(token: String) =
        ratingUseCase.getRatings(token).asLiveData().map {
            _getRatingsResponse.postValue(Event(it))
        }

    fun getCart(token: String) =
        ratingUseCase.getCart(token).asLiveData().map {
            _getCartResponse.postValue(Event(it))
        }

    fun saveRating(
        token: String,
        ratingId: Int,
        transactionId: Int,
        productId: Int,
        rating: Int,
    ): LiveData<Event<Resource<String>>> =
        ratingUseCase.saveRating(token, ratingId, transactionId, productId, rating).asLiveData()
            .map {
                Event(it)
            }

    fun getToken(): LiveData<Event<String>> =
        ratingUseCase.getToken().asLiveData().map {
            Event(it)
        }

    fun setLoading(isLoading: Boolean) =
        _isLoading.postValue(Event(isLoading))

//    private val _ratingResponse = MutableLiveData<RatingResponse>()
//    val ratingResponse: LiveData<RatingResponse> = _ratingResponse
//
//    private val _saveRatingResponse = MutableLiveData<SaveRatingResponse>()
//    val saveRatingResponse: LiveData<SaveRatingResponse> = _saveRatingResponse
//
//    private var _userCart = MutableLiveData<UserCartResponse>()
//    val userCart: LiveData<UserCartResponse> = _userCart
//
//    private val _isLoading = MutableLiveData<Boolean>()
//    val isLoading: LiveData<Boolean> = _isLoading
//
//    fun getRatings(token: String) {
//        viewModelScope.launch {
//            _isLoading.postValue(true)
//            val request = retrofitRepository.getRatings(token)
//            _ratingResponse.postValue(request)
//            _isLoading.postValue(false)
//        }
//    }
//
//    fun getUserCart(token: String) {
//        viewModelScope.launch {
//            _isLoading.postValue(true)
//            val request = retrofitRepository.getUserCart(token)
//            _userCart.postValue(request)
//            _isLoading.postValue(false)
//        }
//    }
//
//    fun saveRating(token: String, ratingId: Int, transactionId: Int, productId: Int, rating: Int) {
//        viewModelScope.launch {
//            _isLoading.postValue(true)
//            val request = retrofitRepository.saveRating(token, ratingId, transactionId, productId, rating)
//            _saveRatingResponse.postValue(request)
//            _isLoading.postValue(false)
//        }
//    }

}
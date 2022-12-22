package com.example.segarbox.ui.rating

import androidx.lifecycle.*
import com.example.core.data.Repository
import com.example.core.data.Resource
import com.example.core.data.source.remote.response.RatingResponse
import com.example.core.data.source.remote.response.SaveRatingResponse
import com.example.core.data.source.remote.response.UserCartResponse
import com.example.core.domain.model.Cart
import com.example.core.domain.model.Rating
import com.example.core.domain.usecase.RatingUseCase
import com.example.core.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RatingViewModel @Inject constructor(private val ratingUseCase: RatingUseCase) : ViewModel() {

    fun getRatings(token: String): LiveData<Event<Resource<List<Rating>>>> =
        ratingUseCase.getRatings(token).asLiveData().map {
            Event(it)
        }

    fun getCart(token: String): LiveData<Event<Resource<List<Cart>>>> =
        ratingUseCase.getCart(token).asLiveData().map {
            Event(it)
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
package com.example.segarbox.ui.rating

import android.util.Log
import androidx.lifecycle.*
import com.example.core.data.Resource
import com.example.core.domain.model.Cart
import com.example.core.domain.model.Rating
import com.example.core.domain.usecase.RatingUseCase
import com.example.core.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RatingViewModel @Inject constructor(private val ratingUseCase: RatingUseCase) : ViewModel() {

    private val _getTokenResponse = MutableLiveData<Event<String>>()
    val getTokenResponse: LiveData<Event<String>> = _getTokenResponse

    private val _getRatingsResponse = MutableLiveData<Event<Resource<List<Rating>>>>()
    val getRatingResponse: LiveData<Event<Resource<List<Rating>>>> = _getRatingsResponse

    private val _getCartResponse = MutableLiveData<Event<Resource<List<Cart>>>>()
    val getCartResponse: LiveData<Event<Resource<List<Cart>>>> = _getCartResponse

    private val _isLoading = MutableLiveData<Event<Boolean>>()
    val isLoading: LiveData<Event<Boolean>> = _isLoading

    init {
        getToken()
    }

    fun getRatings(token: String) = viewModelScope.launch(Dispatchers.IO) {
        ratingUseCase.getRatings(token).collect {
            _getRatingsResponse.postValue(Event(it))
        }
    }

    fun getCart(token: String) = viewModelScope.launch(Dispatchers.IO) {
        ratingUseCase.getCart(token).collect {
            _getCartResponse.postValue(Event(it))
        }
    }

    fun saveRating(
        token: String,
        ratingId: Int,
        transactionId: Int,
        productId: Int,
        rating: Int,
    ): LiveData<Event<Resource<String>>> =
        ratingUseCase.saveRating(token, ratingId, transactionId, productId, rating).asLiveData()
            .map { Event(it) }

    fun getToken() = viewModelScope.launch(Dispatchers.IO) {
        ratingUseCase.getToken().collect {
            _getTokenResponse.postValue(Event(it))
        }
    }

    fun setLoading(isLoading: Boolean) =
        _isLoading.postValue(Event(isLoading))

}
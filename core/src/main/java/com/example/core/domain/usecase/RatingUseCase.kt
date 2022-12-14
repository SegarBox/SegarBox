package com.example.core.domain.usecase

import com.example.core.data.Resource
import com.example.core.domain.model.Cart
import com.example.core.domain.model.Rating
import kotlinx.coroutines.flow.Flow

interface RatingUseCase {

    fun getRatings(token: String): Flow<Resource<List<Rating>>>

    fun getCart(token: String): Flow<Resource<List<Cart>>>

    fun saveRating(token: String, ratingId: Int, transactionId: Int, productId: Int, rating: Int): Flow<Resource<String>>

}
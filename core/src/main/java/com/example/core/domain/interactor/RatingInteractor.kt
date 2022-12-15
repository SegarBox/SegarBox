package com.example.core.domain.interactor

import com.example.core.data.Resource
import com.example.core.domain.body.MakeOrderBody
import com.example.core.domain.body.UpdateStatusBody
import com.example.core.domain.model.*
import com.example.core.domain.repository.IRepository
import com.example.core.domain.usecase.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RatingInteractor @Inject constructor(private val repository: IRepository) : RatingUseCase {

    override fun getRatings(token: String): Flow<Resource<List<Rating>>> =
        repository.getRatings(token)

    override fun getCart(token: String): Flow<Resource<List<Cart>>> = repository.getCart(token)

    override fun saveRating(
        token: String,
        ratingId: Int,
        transactionId: Int,
        productId: Int,
        rating: Int,
    ): Flow<Resource<String>> =
        repository.saveRating(token, ratingId, transactionId, productId, rating)

}
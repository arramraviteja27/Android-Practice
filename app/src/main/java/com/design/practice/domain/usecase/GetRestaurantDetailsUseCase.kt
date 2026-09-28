package com.design.practice.domain.usecase

import com.design.practice.domain.model.Restaurant
import com.design.practice.domain.repository.RestaurantRepository
import jakarta.inject.Inject

class GetRestaurantDetailsUseCase  @Inject constructor(
    private val repository: RestaurantRepository
) {

    suspend operator fun invoke(
        placeId: String
    ): Result<Restaurant> {

        return repository.getRestaurantDetails(
            placeId
        )
    }
}
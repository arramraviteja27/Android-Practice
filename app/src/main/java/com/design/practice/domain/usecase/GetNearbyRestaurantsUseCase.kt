package com.design.practice.domain.usecase

import com.design.practice.domain.model.Restaurant
import com.design.practice.domain.repository.RestaurantRepository
import jakarta.inject.Inject

class GetNearbyRestaurantsUseCase  @Inject constructor(
    private val repository: RestaurantRepository
) {

    suspend operator fun invoke(
        latitude: Double,
        longitude: Double
    ): Result<List<Restaurant>> {

        return repository.getNearbyRestaurants(
            latitude,
            longitude
        )
    }
}
package com.design.practice.domain.repository

import com.design.practice.domain.model.Restaurant

interface RestaurantRepository {

    suspend fun getNearbyRestaurants(
        latitude: Double,
        longitude: Double
    ): Result<List<Restaurant>>

    suspend fun getRestaurantDetails(
        placeId: String
    ): Result<Restaurant>
}
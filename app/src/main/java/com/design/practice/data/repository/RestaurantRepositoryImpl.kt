package com.design.practice.data.repository

import com.design.practice.data.remote.datasource.PlacesDataSource
import com.design.practice.domain.model.Restaurant
import com.design.practice.domain.repository.RestaurantRepository
import jakarta.inject.Inject

class RestaurantRepositoryImpl @Inject constructor(
    private val placesDataSource: PlacesDataSource
) : RestaurantRepository {
    override suspend fun getNearbyRestaurants(
        latitude: Double,
        longitude: Double
    ): Result<List<Restaurant>> {

        return placesDataSource.getNearbyRestaurants(
            latitude,
            longitude
        )
    }

    override suspend fun getRestaurantDetails(
        placeId: String
    ): Result<Restaurant> {

        return placesDataSource
            .getRestaurantDetails(placeId)
    }
}
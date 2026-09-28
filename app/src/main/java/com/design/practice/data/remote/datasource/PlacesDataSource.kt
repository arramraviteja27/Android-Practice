package com.design.practice.data.remote.datasource

import com.design.practice.domain.model.Restaurant
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.CircularBounds
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchResolvedPhotoUriRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.api.net.SearchNearbyRequest
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await

class PlacesDataSource @Inject constructor(
    private val placesClient: PlacesClient
) {

    suspend fun getNearbyRestaurants(
        latitude: Double,
        longitude: Double
    ): Result<List<Restaurant>> {

        return try {

            val location = LatLng(
                latitude,
                longitude
            )

            val circle = CircularBounds.newInstance(
                location,
                5000.0
            )

            val fields = listOf(
                Place.Field.ID,
                Place.Field.DISPLAY_NAME,
                Place.Field.FORMATTED_ADDRESS,
                Place.Field.LOCATION,
                Place.Field.RATING,
                Place.Field.PRICE_LEVEL,
                Place.Field.PHOTO_METADATAS,
                Place.Field.OPENING_HOURS
            )

            val request =
                SearchNearbyRequest
                    .builder(
                        circle,
                        fields
                    )
                    .setIncludedTypes(
                        listOf("restaurant")
                    )
                    .build()

            val response =
                placesClient
                    .searchNearby(request)
                    .await()

            val restaurants = response.places.mapNotNull { place ->

                val location = place.location
                    ?: return@mapNotNull null

                val photoUri = place.photoMetadatas
                    ?.firstOrNull()
                    ?.let { photoMetadata ->

                        try {

                            val photoRequest =
                                FetchResolvedPhotoUriRequest
                                    .builder(photoMetadata)
                                    .build()

                            placesClient
                                .fetchResolvedPhotoUri(photoRequest)
                                .await()
                                .uri
                                ?.toString()

                        } catch (e: Exception) {

                            null
                        }
                    }

                Restaurant(
                    placeId = place.id.orEmpty(),

                    name = place.displayName,

                    address = place.formattedAddress,

                    latitude = location.latitude,

                    longitude = location.longitude,

                    rating = place.rating,

                    priceLevel = place.priceLevel,

                    photoUri = photoUri,

                    isOpen = true
                )
            }
            Result.success(restaurants)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }


    suspend fun getRestaurantDetails(
        placeId: String
    ): Result<Restaurant> {

        return try {

            val fields = listOf(

                Place.Field.ID,

                Place.Field.DISPLAY_NAME,

                Place.Field.FORMATTED_ADDRESS,

                Place.Field.LOCATION,

                Place.Field.RATING,

                Place.Field.PRICE_LEVEL,

                Place.Field.PHOTO_METADATAS,

                Place.Field.CURRENT_OPENING_HOURS,

                Place.Field.INTERNATIONAL_PHONE_NUMBER,

                Place.Field.WEBSITE_URI
            )

            val request =
                FetchPlaceRequest
                    .builder(
                        placeId,
                        fields
                    )
                    .build()

            val response =
                placesClient
                    .fetchPlace(request)
                    .await()

            val place = response.place

            val location =
                place.location
                    ?: return Result.failure(
                        Exception(
                            "Location unavailable"
                        )
                    )

            val photoUri =
                place.photoMetadatas
                    ?.firstOrNull()
                    ?.let { metadata ->

                        try {

                            val photoRequest =
                                FetchResolvedPhotoUriRequest
                                    .builder(metadata)
                                    .build()

                            placesClient
                                .fetchResolvedPhotoUri(
                                    photoRequest
                                )
                                .await()
                                .uri
                                ?.toString()

                        } catch (e: Exception) {
                            null
                        }
                    }

            val restaurant = Restaurant(

                placeId =
                    place.id.orEmpty(),

                name =
                    place.displayName,

                address =
                    place.formattedAddress,

                latitude =
                    location.latitude,

                longitude =
                    location.longitude,

                rating =
                    place.rating,

                priceLevel =
                    place.priceLevel,

                photoUri =
                    photoUri,

                isOpen =
                    true
            )

            Result.success(
                restaurant
            )

        } catch (e: Exception) {

            Result.failure(e)
        }
    }
}
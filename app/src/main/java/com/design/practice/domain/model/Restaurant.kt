package com.design.practice.domain.model

import com.google.android.libraries.places.api.model.PhotoMetadata

data class Restaurant(
    val placeId: String,
    val name: String,
    val address: String?,
    val latitude: Double,
    val longitude: Double,
    val rating: Double?,
    val priceLevel: Int?,
    val photoUri: String?,
    val isOpen: Boolean?
)
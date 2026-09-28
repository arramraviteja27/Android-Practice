package com.design.practice.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.design.practice.core.common.ApiState
import com.design.practice.domain.model.Restaurant
import com.design.practice.domain.usecase.GetNearbyRestaurantsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


@HiltViewModel
class RestaurantViewModel @Inject constructor(
    private val getNearbyRestaurantsUseCase: GetNearbyRestaurantsUseCase
) : ViewModel(){



        private val _restaurantState =
            MutableStateFlow<ApiState<List<Restaurant>>>(
                ApiState.Idle
            )

        val restaurantState =
            _restaurantState.asStateFlow()

        fun getNearbyRestaurants(
            latitude: Double,
            longitude: Double
        ) {
            viewModelScope.launch {

                _restaurantState.value = ApiState.Loading

                getNearbyRestaurantsUseCase(
                    latitude = latitude,
                    longitude = longitude
                ).onSuccess { restaurants ->

                    _restaurantState.value =
                        ApiState.Success(restaurants)

                }.onFailure { exception ->

                    _restaurantState.value =
                        ApiState.Failure(
                            exception.message
                                ?: "Unable to load restaurants"
                        )
                }
            }
        }
    }


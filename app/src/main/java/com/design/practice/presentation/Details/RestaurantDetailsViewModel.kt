package com.design.practice.presentation.Details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.design.practice.core.common.ApiState
import com.design.practice.domain.model.Restaurant
import com.design.practice.domain.usecase.GetRestaurantDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class RestaurantDetailsViewModel @Inject constructor(
    private val getRestaurantDetailsUseCase:
    GetRestaurantDetailsUseCase
) : ViewModel() {

    private val _restaurantState =
        MutableStateFlow<ApiState<Restaurant>>(
            ApiState.Idle
        )

    val restaurantState =
        _restaurantState.asStateFlow()

    fun getRestaurantDetails(
        placeId: String
    ) {

        viewModelScope.launch {

            _restaurantState.value =
                ApiState.Loading

            getRestaurantDetailsUseCase(
                placeId
            )
                .onSuccess { restaurant ->

                    _restaurantState.value =
                        ApiState.Success(
                            restaurant
                        )
                }
                .onFailure { exception ->

                    _restaurantState.value =
                        ApiState.Failure(
                            exception.message
                                ?: "Unable to load restaurant"
                        )
                }
        }
    }
}
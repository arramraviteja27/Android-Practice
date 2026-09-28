package com.design.practice.core.common

sealed interface ApiState<out T> {

    /**
     * Initial state before making an API call.
     */
    data object Idle : ApiState<Nothing>

    /**
     * Indicates the API request is in progress.
     */
    data object Loading : ApiState<Nothing>

    /**
     * Represents a successful API response.
     */
    data class Success<T>(
        val data: T
    ) : ApiState<T>

    /**
     * Represents an API failure.
     */
    data class Failure(
        val message: String
    ) : ApiState<Nothing>
}
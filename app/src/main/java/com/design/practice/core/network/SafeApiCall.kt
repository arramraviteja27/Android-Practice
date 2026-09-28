package com.design.practice.core.network


import com.design.practice.core.common.ApiState
import retrofit2.HttpException
import java.io.IOException

abstract class SafeApiCall {

    suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): ApiState<T> {

        return try {

            ApiState.Success(apiCall())

        } catch (e: HttpException) {

            ApiState.Failure(
                message = e.message ?: "Something went wrong"

            )

        } catch (e: IOException) {

            ApiState.Failure(
                message = "Please check your internet connection."
            )

        } catch (e: Exception) {

            ApiState.Failure(
                message = e.localizedMessage ?: "Unknown Error"
            )

        }

    }

}
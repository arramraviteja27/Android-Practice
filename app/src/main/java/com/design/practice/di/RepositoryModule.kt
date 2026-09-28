package com.design.practice.di

import com.design.practice.data.repository.AuthRepositoryImpl
import com.design.practice.data.repository.RestaurantRepositoryImpl
import com.design.practice.domain.repository.AuthRepository
import com.design.practice.domain.repository.RestaurantRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
   abstract fun bindRestaurantRepository(
        restaurantRepositoryImpl: RestaurantRepositoryImpl
    ): RestaurantRepository

}
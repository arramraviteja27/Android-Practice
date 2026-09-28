package com.design.practice.di

import com.design.practice.data.repository.ChatRepositoryImpl
import com.design.practice.domain.repository.ChatRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ChatModule {

    @Binds
    @Singleton
    abstract fun bindChatRepository(
        repositoryImpl: ChatRepositoryImpl
    ): ChatRepository
}
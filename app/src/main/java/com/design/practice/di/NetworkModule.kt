package com.design.practice.di


import com.design.practice.data.remote.api.AuthApi
import com.design.practice.data.repository.PostRepositoryImpl
import com.design.practice.data.repository.RestaurantRepositoryImpl
import com.design.practice.data.repository.TodoRepositoryImpl
import com.design.practice.domain.repository.PostRepository
import com.design.practice.domain.repository.RestaurantRepository
import com.design.practice.domain.repository.TodoRepository
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {

        return HttpLoggingInterceptor().apply {

            level = HttpLoggingInterceptor.Level.BODY

        }

    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        interceptor: HttpLoggingInterceptor
    ): OkHttpClient {

        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

    }

    @Provides
    @Singleton
    fun provideGson(): Gson {

        return GsonBuilder()
            .setLenient()
            .create()

    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {

        return Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): AuthApi {

        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRepository(
        api: AuthApi
    ): PostRepository {

        return PostRepositoryImpl(api)
    }


    @Provides
    @Singleton
    fun provideTodoRepository(
        api: AuthApi
    ): TodoRepository {

        return TodoRepositoryImpl(api)
    }






}
package com.example.segarbox.core.di

import com.example.segarbox.BuildConfig
import com.example.segarbox.core.data.source.remote.network.ApiServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideSegarBoxApiService(client: OkHttpClient): ApiServices {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL_SEGARBOX)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiServices::class.java)
    }

    @Singleton
    @Provides
    fun provideMapsApiService(client: OkHttpClient): ApiServices {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL_GOOGLE_MAPS)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiServices::class.java)
    }

    @Singleton
    @Provides
    fun provideRajaOngkirApiService(client: OkHttpClient): ApiServices {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL_RAJAONGKIR)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiServices::class.java)
    }

    @Singleton
    @Provides
    fun provideFlaskApiService(client: OkHttpClient): ApiServices {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL_SEGARBOX_FLASK)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiServices::class.java)
    }

}
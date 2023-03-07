package com.example.core.di

import com.example.core.BuildConfig
import com.example.core.data.source.remote.network.*
import com.example.core.utils.DYNAMIC_BASE_URL
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
    fun provideSegarBoxApiService(client: OkHttpClient): SegarBoxApiServices {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL_SEGARBOX)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(SegarBoxApiServices::class.java)
    }

    @Singleton
    @Provides
    fun provideMapsApiService(client: OkHttpClient): MapsApiServices {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL_GOOGLE_MAPS)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(MapsApiServices::class.java)
    }

    @Singleton
    @Provides
    fun provideRajaOngkirApiService(client: OkHttpClient): RajaOngkirApiServices {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL_RAJAONGKIR)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(RajaOngkirApiServices::class.java)
    }

    @Singleton
    @Provides
    fun provideFlaskApiService(client: OkHttpClient): FlaskApiServices {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL_SEGARBOX_FLASK)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(FlaskApiServices::class.java)
    }

    @Singleton
    @Provides
    fun provideMidtransApiService(client: OkHttpClient): MidtransApiServices {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL_MIDTRANS)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(MidtransApiServices::class.java)
    }

}
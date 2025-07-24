package com.danp.bletracker.di

import android.content.Context
import com.danp.bletracker.data.network.SupabaseService
import com.danp.bletracker.utils.KeyProvider
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        moshi: Moshi
    ): Retrofit = Retrofit.Builder()
        .baseUrl("https://ubcrdhmupvbyvopuuqqd.supabase.co/")
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    @Provides
    @Singleton
    fun provideSupabaseService(retrofit: Retrofit): SupabaseService =
        retrofit.create(SupabaseService::class.java)

    @Provides
    @Singleton
    @Named("supabaseApiKey")
    fun provideSupabaseApiKey(@ApplicationContext context: Context): String =
        KeyProvider.getSupabaseApiKey(context)
}
package com.danp.bletracker.di

import com.danp.bletracker.data.local.ContactoCercanoDao
import com.danp.bletracker.data.network.SupabaseService
import com.danp.bletracker.data.repository.ContactoRepositoryImpl
import com.danp.bletracker.data.repository.LocalContactoRepositoryImpl
import com.danp.bletracker.domain.repository.ContactoRepository
import com.danp.bletracker.domain.repository.LocalContactoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideLocalContactoRepository(
        dao: ContactoCercanoDao
    ): LocalContactoRepository = LocalContactoRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideContactoRepository(
        localRepo: LocalContactoRepository,
        api: SupabaseService,
        @Named("supabaseApiKey") apiKey: String
    ): ContactoRepository = ContactoRepositoryImpl(localRepo, api, apiKey)
}
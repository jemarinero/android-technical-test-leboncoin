package fr.leboncoin.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fr.leboncoin.data.repository.AlbumRepositoryImpl
import fr.leboncoin.domain.repositories.AlbumRepository

@Module(includes = [RepositoryModule.RepositorySubModule::class])
@InstallIn(SingletonComponent::class)
internal object RepositoryModule {

    @Module
    @InstallIn(SingletonComponent::class)
    internal interface RepositorySubModule {

        @Binds
        fun bindInformationRepository(impl: AlbumRepositoryImpl): AlbumRepository
    }
}
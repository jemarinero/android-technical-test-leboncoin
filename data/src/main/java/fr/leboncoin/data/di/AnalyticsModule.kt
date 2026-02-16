package fr.leboncoin.data.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fr.leboncoin.data.repository.AnalyticsRepositoryImpl
import fr.leboncoin.domain.repositories.AnalyticsRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AnalyticsModule {

    @Singleton
    @Provides
    fun provideAnalyticsRepository(
        @ApplicationContext context: Context
    ): AnalyticsRepository {
        return AnalyticsRepositoryImpl(context)
    }
}

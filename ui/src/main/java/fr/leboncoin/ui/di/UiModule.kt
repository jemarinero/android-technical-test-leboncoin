package fr.leboncoin.ui.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fr.leboncoin.domain.dispatcher.DispatcherProvider
import fr.leboncoin.ui.dispatcher.DefaultDispatcherProvider

@Module
@InstallIn(SingletonComponent::class)
interface UiModule {

    @Binds
    fun bindDispatcherProvider(imp: DefaultDispatcherProvider): DispatcherProvider
}
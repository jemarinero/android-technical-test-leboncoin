package fr.leboncoin.ui.dispatcher

import fr.leboncoin.domain.dispatcher.DispatcherProvider
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class DefaultDispatcherProvider @Inject constructor(): DispatcherProvider {

    override fun main(): CoroutineDispatcher = Dispatchers.Main

    override fun default(): CoroutineDispatcher = Dispatchers.Default

    override fun io(): CoroutineDispatcher = Dispatchers.IO

    override fun unconfined(): CoroutineDispatcher = Dispatchers.Unconfined
}
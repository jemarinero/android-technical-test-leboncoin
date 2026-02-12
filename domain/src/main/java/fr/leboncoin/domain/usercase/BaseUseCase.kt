package fr.leboncoin.domain.usercase

import fr.leboncoin.domain.dispatcher.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

abstract class BaseUseCase<T, R>(protected open val dispatcherProvider: DispatcherProvider) {

    protected open fun dispatcher(): CoroutineDispatcher = dispatcherProvider.io()

    fun execute(param: T) = configure(param).flowOn(dispatcher())

    protected abstract fun configure(param: T): Flow<R>
}
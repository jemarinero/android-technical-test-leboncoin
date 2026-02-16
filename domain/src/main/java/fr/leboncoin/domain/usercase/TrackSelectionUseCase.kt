package fr.leboncoin.domain.usercase

import fr.leboncoin.domain.dispatcher.DispatcherProvider
import fr.leboncoin.domain.repositories.AnalyticsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TrackSelectionUseCase
@Inject
constructor(
    private val analyticsRepository: AnalyticsRepository,
    dispatcherProvider: DispatcherProvider
) : BaseUseCase<TrackSelectionParams, Unit>(dispatcherProvider) {

    override fun configure(param: TrackSelectionParams): Flow<Unit> = flow {
        analyticsRepository.trackSelection(param.itemId)
        emit(Unit)
    }
}

data class TrackSelectionParams(val itemId: String)

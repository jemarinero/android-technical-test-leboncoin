package fr.leboncoin.domain.usercase

import fr.leboncoin.domain.dispatcher.DispatcherProvider
import fr.leboncoin.domain.repositories.AnalyticsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TrackScreenViewUseCase
@Inject
constructor(
    private val analyticsRepository: AnalyticsRepository,
    dispatcherProvider: DispatcherProvider
) : BaseUseCase<TrackScreenViewParams, Unit>(dispatcherProvider) {

    override fun configure(param: TrackScreenViewParams): Flow<Unit> = flow {
        analyticsRepository.trackScreenView(param.screenName)
        emit(Unit)
    }
}

data class TrackScreenViewParams(val screenName: String)

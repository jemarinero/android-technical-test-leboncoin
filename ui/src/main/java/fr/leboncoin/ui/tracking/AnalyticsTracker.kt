package fr.leboncoin.ui.tracking

import fr.leboncoin.domain.usercase.TrackScreenViewParams
import fr.leboncoin.domain.usercase.TrackScreenViewUseCase
import fr.leboncoin.domain.usercase.TrackSelectionParams
import fr.leboncoin.domain.usercase.TrackSelectionUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import javax.inject.Inject

class AnalyticsTracker
@Inject
constructor(
    private val trackScreenViewUseCase: TrackScreenViewUseCase,
    private val trackSelectionUseCase: TrackSelectionUseCase
) {

    fun trackScreenViewed(screenName: String, scope: CoroutineScope) {
        trackScreenViewUseCase.execute(TrackScreenViewParams(screenName))
            .launchIn(scope)
    }

    fun trackItemSelected(itemId: String, scope: CoroutineScope) {
        trackSelectionUseCase.execute(TrackSelectionParams(itemId))
            .launchIn(scope)
    }
}

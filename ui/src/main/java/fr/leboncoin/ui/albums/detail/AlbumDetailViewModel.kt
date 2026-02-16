package fr.leboncoin.ui.albums.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.leboncoin.domain.models.AlbumModel
import fr.leboncoin.domain.models.ErrorType
import fr.leboncoin.domain.models.ResultOf
import fr.leboncoin.domain.usercase.GetAlbumDetailParams
import fr.leboncoin.domain.usercase.GetAlbumDetailUseCase
import fr.leboncoin.ui.models.ErrorUiModel
import fr.leboncoin.ui.models.toUiModel
import fr.leboncoin.ui.navigation.ARG_ALBUM_ID
import fr.leboncoin.ui.navigation.ARG_ID
import fr.leboncoin.ui.tracking.AnalyticsTracker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AlbumDetailViewModel
@Inject
constructor(
    private val getAlbumDetailUseCase: GetAlbumDetailUseCase,
    private val analyticsTracker: AnalyticsTracker,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val id: Int =
        checkNotNull(savedStateHandle[ARG_ID])

    private val albumId: Int =
        checkNotNull(savedStateHandle[ARG_ALBUM_ID])

    private val _state = MutableStateFlow(AlbumDetailUiState())
    val state: StateFlow<AlbumDetailUiState> = _state

    init {
        loadAlbum()
    }

    private fun loadAlbum() {

        getAlbumDetailUseCase
            .execute(GetAlbumDetailParams(id, albumId))

            .onStart {
                _state.update {
                    it.copy(isLoading = true, error = null)
                }
            }

            .catch {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = ErrorType.UnknownError.toUiModel()
                    )
                }
            }

            .onEach { result ->

                _state.update { current ->

                    when (result) {

                        is ResultOf.Success -> {
                            current.copy(
                                isLoading = false,
                                album = result.value,
                                error = null
                            )
                        }

                        is ResultOf.Failure -> {
                            current.copy(
                                isLoading = false,
                                error = result.errorType.toUiModel()
                            )
                        }
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun onScreenViewed(screenName: String) = analyticsTracker.trackScreenViewed(screenName, viewModelScope)

    fun onErrorClose() {
        _state.update { it.copy(error = null) }
    }
}

data class AlbumDetailUiState(
    val isLoading: Boolean = false,
    val album: AlbumModel? = null,
    val error: ErrorUiModel? = null
)


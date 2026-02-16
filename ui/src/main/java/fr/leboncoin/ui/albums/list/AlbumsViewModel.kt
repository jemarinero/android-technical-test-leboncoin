package fr.leboncoin.ui.albums.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.leboncoin.domain.models.AlbumModel
import fr.leboncoin.domain.models.ErrorType
import fr.leboncoin.domain.models.doIfFailure
import fr.leboncoin.domain.models.doIfSuccess
import fr.leboncoin.domain.usercase.GetAlbumsParams
import fr.leboncoin.domain.usercase.GetAlbumsUseCase
import fr.leboncoin.ui.models.ErrorUiModel
import fr.leboncoin.ui.models.toUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AlbumsViewModel
@Inject
constructor(
    private val getAlbumsUseCase: GetAlbumsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    init {
        loadAlbums()
    }

    private fun loadAlbums(forceRefresh: Boolean = false) {

        getAlbumsUseCase
            .execute(GetAlbumsParams(forceRefresh))

            .onStart {
                _uiState.update { it.copy(isLoading = true, error = null) }
            }
            .catch {
                _uiState.update {
                    it.copy(
                        error = ErrorType.UnknownError.toUiModel(),
                        isLoading = false
                    )
                }
            }
            .onEach { result ->
                result.doIfSuccess { albums ->
                    _uiState.update {
                        it.copy(albums = albums, isLoading = false)
                    }
                }
                result.doIfFailure { errorType ->
                    _uiState.update {
                        it.copy(
                            error = errorType?.toUiModel(),
                            isLoading = false
                        )
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun refresh() {
        loadAlbums(forceRefresh = true)
    }

    fun onErrorClose() {
        _uiState.update { UiState() }
    }

}

data class UiState(
    val albums: List<AlbumModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: ErrorUiModel? = null
)
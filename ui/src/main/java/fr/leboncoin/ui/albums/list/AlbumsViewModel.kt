package fr.leboncoin.ui.albums.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.leboncoin.domain.models.AlbumModel
import fr.leboncoin.domain.models.doIfFailure
import fr.leboncoin.domain.models.doIfSuccess
import fr.leboncoin.domain.usercase.GetAlbumsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class AlbumsViewModel
@Inject
constructor(
    private val getAlbumsUseCase: GetAlbumsUseCase,
) : ViewModel() {

    private val _albums = MutableStateFlow<List<AlbumModel>>(emptyList())
    val albums: StateFlow<List<AlbumModel>> = _albums

    init {
        loadAlbums()
    }

    private fun loadAlbums() {
        getAlbumsUseCase
            .execute(Unit)
            .catch { }
            .onEach { result ->
                result.doIfSuccess {
                    _albums.emit(it)
                }
                result.doIfFailure {
                    /* TODO: Handle errors */
                }
            }
            .launchIn(viewModelScope)
    }
}
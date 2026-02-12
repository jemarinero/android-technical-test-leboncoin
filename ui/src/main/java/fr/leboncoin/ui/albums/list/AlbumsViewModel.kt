package fr.leboncoin.ui.albums.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.leboncoin.domain.models.AlbumModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumsViewModel
@Inject
constructor(
//    private val repository: AlbumRepository,
) : ViewModel() {

    private val _albums = MutableStateFlow<List<AlbumModel>>(emptyList())
    val albums: StateFlow<List<AlbumModel>> = _albums

    init {
        loadAlbums()
    }

    private fun loadAlbums() = viewModelScope.launch {
        try {
//                _albums.emit(repository.getAllAlbums())
            _albums.emit(
                listOf(
                    AlbumModel(
                        id = 1,
                        albumId = 1,
                        title = "Album 1",
                        url = "https://via.placeholder.com/600/92c952",
                        thumbnailUrl = "https://via.placeholder.com/150/92c952"
                    ),
                    AlbumModel(
                        id = 2,
                        albumId = 2,
                        title = "Album 1",
                        url = "https://via.placeholder.com/600/92c952",
                        thumbnailUrl = "https://via.placeholder.com/150/92c952"
                    ),
                    AlbumModel(
                        id = 3,
                        albumId = 3,
                        title = "Album 1",
                        url = "https://via.placeholder.com/600/92c952",
                        thumbnailUrl = "https://via.placeholder.com/150/92c952"
                    ),
                    AlbumModel(
                        id = 4,
                        albumId = 4,
                        title = "Album 1",
                        url = "https://via.placeholder.com/600/92c952",
                        thumbnailUrl = "https://via.placeholder.com/150/92c952"
                    ),
                    AlbumModel(
                        id = 5,
                        albumId = 5,
                        title = "Album 1",
                        url = "https://via.placeholder.com/600/92c952",
                        thumbnailUrl = "https://via.placeholder.com/150/92c952"
                    ),
                )
            )
        } catch (_: Exception) { /* TODO: Handle errors */
        }
    }
}
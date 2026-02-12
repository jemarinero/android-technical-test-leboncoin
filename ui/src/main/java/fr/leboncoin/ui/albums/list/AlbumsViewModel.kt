package fr.leboncoin.ui.albums.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

//@OptIn(DelicateCoroutinesApi::class)
class AlbumsViewModel(
//    private val repository: AlbumRepository,
) : ViewModel() {

//    private val _albums = MutableSharedFlow<List<AlbumDto>>()
//    val albums: SharedFlow<List<AlbumDto>> = _albums
//
//    fun loadAlbums() {
//        GlobalScope.launch {
//            try {
//                _albums.emit(repository.getAllAlbums())
//            } catch (_: Exception) { /* TODO: Handle errors */ }
//        }
//    }
//
//    class Factory(
//        private val repository: AlbumRepository,
//    ) : ViewModelProvider.Factory {
//        @Suppress("UNCHECKED_CAST")
//        override fun <T : ViewModel> create(modelClass: Class<T>): T {
//            return AlbumsViewModel(repository) as T
//        }
//    }
}
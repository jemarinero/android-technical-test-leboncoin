package fr.leboncoin.domain.repositories

import fr.leboncoin.domain.models.AlbumModel
import fr.leboncoin.domain.models.ResultOf

interface AlbumRepository {
    suspend fun getAlbums(forceRefresh: Boolean = false): ResultOf<List<AlbumModel>>
    fun getAlbumDetail(id: Int, albumId: Int): ResultOf<AlbumModel>
}
package fr.leboncoin.domain.repositories

import fr.leboncoin.domain.models.AlbumModel
import fr.leboncoin.domain.models.ResultOf

interface AlbumRepository {
    suspend fun getAlbums(): ResultOf<List<AlbumModel>>
}
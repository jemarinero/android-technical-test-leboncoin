package fr.leboncoin.domain.repositories

import fr.leboncoin.domain.models.AlbumModel
import fr.leboncoin.domain.models.ResultOf
import kotlinx.coroutines.flow.Flow

interface AlbumRepository {
    fun getAlbums(): Flow<ResultOf<List<AlbumModel>>>
}
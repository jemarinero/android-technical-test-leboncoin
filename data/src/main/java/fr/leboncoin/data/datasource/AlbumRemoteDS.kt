package fr.leboncoin.data.datasource

import fr.leboncoin.data.common.FailureFactory
import fr.leboncoin.data.common.safeCall
import fr.leboncoin.data.network.api.AlbumApiService
import fr.leboncoin.data.network.model.AlbumDto
import fr.leboncoin.domain.models.ResultOf
import javax.inject.Inject

class AlbumRemoteDS
@Inject
constructor(
    private val albumApiService: AlbumApiService,
){

    suspend fun getRemoteAlbums(): ResultOf<List<AlbumDto>> =
        try {
            albumApiService.getAlbums().safeCall({ response ->
               response
            })
        } catch (ex: Exception) {
            FailureFactory().handleException(ex)
        }

}
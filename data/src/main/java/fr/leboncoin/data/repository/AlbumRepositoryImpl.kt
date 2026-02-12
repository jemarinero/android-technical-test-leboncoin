package fr.leboncoin.data.repository

import fr.leboncoin.data.datasource.AlbumRemoteDS
import fr.leboncoin.data.mappers.AlbumMapper
import fr.leboncoin.domain.models.AlbumModel
import fr.leboncoin.domain.models.ErrorType
import fr.leboncoin.domain.models.ResultOf
import fr.leboncoin.domain.models.doIfFailure
import fr.leboncoin.domain.models.doIfSuccess
import fr.leboncoin.domain.repositories.AlbumRepository
import javax.inject.Inject

class AlbumRepositoryImpl
@Inject
constructor(
    private val remoteDS: AlbumRemoteDS,
    private val albumMapper: AlbumMapper
) : AlbumRepository {
    
    override suspend fun getAlbums(): ResultOf<List<AlbumModel>> {
        val result = remoteDS.getRemoteAlbums()
        var response: ResultOf<List<AlbumModel>> = ResultOf.Failure(ErrorType.UnknownError)

        result.doIfSuccess { data ->
            response = ResultOf.Success(data.map { albumMapper.mapToDomain(it) })
        }
        result.doIfFailure {
            response = ResultOf.Failure(it ?: ErrorType.UnknownError)
        }
        return response

    }
}
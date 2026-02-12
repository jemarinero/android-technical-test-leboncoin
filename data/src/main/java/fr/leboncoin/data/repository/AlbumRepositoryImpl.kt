package fr.leboncoin.data.repository

import fr.leboncoin.data.datasource.AlbumLocalDS
import fr.leboncoin.data.datasource.AlbumRemoteDS
import fr.leboncoin.data.mappers.AlbumMapper
import fr.leboncoin.domain.models.AlbumModel
import fr.leboncoin.domain.models.ErrorType
import fr.leboncoin.domain.models.ResultOf
import fr.leboncoin.domain.models.doIfFailure
import fr.leboncoin.domain.models.doIfSuccess
import fr.leboncoin.domain.repositories.AlbumRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AlbumRepositoryImpl
@Inject
constructor(
    private val remoteDS: AlbumRemoteDS,
    private val localDS: AlbumLocalDS,
    private val albumMapper: AlbumMapper
) : AlbumRepository {

    override fun getAlbums(): Flow<ResultOf<List<AlbumModel>>> = flow {
        val local = localDS.getAll().first()
        if (local.isEmpty()) {
            val remote = remoteDS.getRemoteAlbums()
            remote.doIfSuccess {
                localDS.insertAll(it.map(albumMapper::mapToEntity))
            }
            remote.doIfFailure {
                emit(ResultOf.Failure(it ?: ErrorType.UnknownError))
            }
        }
        emitAll(localDS.getAll()
            .map { entities ->
                ResultOf.Success(
                    entities.map(albumMapper::mapToDomain)
                )
            }
        )
    }
}
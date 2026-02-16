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
import javax.inject.Inject

class AlbumRepositoryImpl
@Inject
constructor(
    private val remoteDS: AlbumRemoteDS,
    private val localDS: AlbumLocalDS,
    private val albumMapper: AlbumMapper
) : AlbumRepository {

    override suspend fun getAlbums(
        forceRefresh: Boolean
    ): ResultOf<List<AlbumModel>> {

        val local = localDS.getAll()

        if (local.isEmpty() || forceRefresh) {

            val remote = remoteDS.getRemoteAlbums()

            remote.doIfSuccess {
                localDS.clearAll()
                localDS.insertAll(it.map(albumMapper::mapToEntity))
            }
            remote.doIfFailure {
                return ResultOf.Failure(it ?: ErrorType.UnknownError)
            }
        }

        return ResultOf.Success(
            localDS.getAll()
                .map { albumMapper.mapToDomain(it) }
        )
    }

    override fun getAlbumDetail(
        id: Int,
        albumId: Int
    ): ResultOf<AlbumModel> {
        try {
            val result = localDS.getAlbumDetail(id, albumId)
            return ResultOf.Success(albumMapper.mapToDomain(result))
        } catch (_: Exception) {
            return ResultOf.Failure(ErrorType.UnknownError)
        }
    }
}
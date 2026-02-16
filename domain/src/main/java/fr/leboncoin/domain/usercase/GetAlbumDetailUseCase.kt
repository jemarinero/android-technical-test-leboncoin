package fr.leboncoin.domain.usercase

import fr.leboncoin.domain.dispatcher.DispatcherProvider
import fr.leboncoin.domain.models.AlbumModel
import fr.leboncoin.domain.models.ResultOf
import fr.leboncoin.domain.repositories.AlbumRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class GetAlbumDetailUseCase
@Inject
constructor(
    private val albumRepository: AlbumRepository,
    dispatcherProvider: DispatcherProvider
) : BaseUseCase<GetAlbumDetailParams, ResultOf<AlbumModel>>(dispatcherProvider) {

    override fun configure(param: GetAlbumDetailParams): Flow<ResultOf<AlbumModel>> = flow {
        emit(albumRepository.getAlbumDetail(param.id, param.albumId))
    }
}

data class GetAlbumDetailParams(
    val id: Int,
    val albumId: Int
)
package fr.leboncoin.domain.usercase

import fr.leboncoin.domain.dispatcher.DispatcherProvider
import fr.leboncoin.domain.models.AlbumModel
import fr.leboncoin.domain.models.ResultOf
import fr.leboncoin.domain.repositories.AlbumRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAlbumsUseCase
@Inject
constructor(
    private val albumRepository: AlbumRepository,
    dispatcherProvider: DispatcherProvider
) : BaseUseCase<GetAlbumsParams, ResultOf<List<AlbumModel>>>(dispatcherProvider) {

    override fun configure(param: GetAlbumsParams): Flow<ResultOf<List<AlbumModel>>> = flow {
        emit(albumRepository.getAlbums(param.forceRefresh))
    }
}


data class GetAlbumsParams(
    val forceRefresh: Boolean = false
)
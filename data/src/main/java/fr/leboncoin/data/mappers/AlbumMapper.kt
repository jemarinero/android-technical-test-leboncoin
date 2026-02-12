package fr.leboncoin.data.mappers

import fr.leboncoin.data.network.model.AlbumDto
import fr.leboncoin.domain.models.AlbumModel
import javax.inject.Inject

class AlbumMapper
@Inject
constructor() {

    fun mapToDomain(dto: AlbumDto): AlbumModel {
        return AlbumModel(
            id = dto.id,
            albumId = dto.albumId,
            title = dto.title,
            url = dto.url,
            thumbnailUrl = dto.thumbnailUrl
        )
    }
}
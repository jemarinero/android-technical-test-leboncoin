package fr.leboncoin.data.mappers

import fr.leboncoin.data.database.entity.AlbumEntity
import fr.leboncoin.data.network.model.AlbumDto
import fr.leboncoin.domain.models.AlbumModel
import javax.inject.Inject

class AlbumMapper
@Inject
constructor() {

    fun mapToDomain(entity: AlbumEntity): AlbumModel {
        return AlbumModel(
            id = entity.id,
            albumId = entity.albumId,
            title = entity.title,
            url = entity.url,
            thumbnailUrl = entity.thumbnailUrl
        )
    }

    fun mapToEntity(dto: AlbumDto): AlbumEntity {
        return AlbumEntity(
            id = dto.id,
            albumId = dto.albumId,
            title = dto.title,
            url = dto.url,
            thumbnailUrl = dto.thumbnailUrl
        )
    }
}
package fr.leboncoin.domain.models

data class AlbumModel(
    val id: Int,
    val albumId: Int,
    val title: String,
    val url: String,
    val thumbnailUrl: String
)

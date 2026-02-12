package fr.leboncoin.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "album", primaryKeys = ["albumId", "id"])
data class AlbumEntity(
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "albumId") val albumId: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "thumbnailUrl") val thumbnailUrl: String
)
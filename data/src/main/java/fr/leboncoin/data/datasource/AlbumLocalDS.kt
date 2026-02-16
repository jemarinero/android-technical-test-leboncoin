package fr.leboncoin.data.datasource

import fr.leboncoin.data.database.dao.AlbumDao
import fr.leboncoin.data.database.entity.AlbumEntity
import javax.inject.Inject

class AlbumLocalDS
@Inject
constructor(private val itemDao: AlbumDao) {

    fun insertAll(items: List<AlbumEntity>) = itemDao.insertAll(items)
    fun getAll() = itemDao.getAll()
    fun clearAll() = itemDao.deleteAll()
    fun getAlbumDetail(id: Int, albumId: Int) = itemDao.getAlbumDetail(id, albumId)

}
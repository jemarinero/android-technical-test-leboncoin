package fr.leboncoin.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fr.leboncoin.data.database.entity.AlbumEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AlbumDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(items: List<AlbumEntity>)

    @Query("SELECT * FROM album")
    fun getAll() : Flow<List<AlbumEntity>>

    @Query("DELETE FROM album")
    fun deleteAll()
}
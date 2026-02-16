package fr.leboncoin.data.mappers

import fr.leboncoin.data.database.entity.AlbumEntity
import fr.leboncoin.data.network.model.AlbumDto
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test


class AlbumMapperTest {

    private lateinit var mapper: AlbumMapper

    @Before
    fun setup() {
        mapper = AlbumMapper()
    }

    private fun createEntity(
        id: Int = 1,
        albumId: Int = 100,
        title: String = "Test Album",
        url: String = "http://example.com/album",
        thumbnailUrl: String = "http://example.com/thumb"
    ) = AlbumEntity(id, albumId, title, url, thumbnailUrl)

    private fun createDto(
        id: Int = 1,
        albumId: Int = 100,
        title: String = "Test Album",
        url: String = "http://example.com/album",
        thumbnailUrl: String = "http://example.com/thumb"
    ) = AlbumDto(id, albumId, title, url, thumbnailUrl)

    @Test
    fun `mapToDomain maps all fields correctly`() {
        val entity = createEntity()
        val model = mapper.mapToDomain(entity)

        assertEquals(entity.id, model.id)
        assertEquals(entity.albumId, model.albumId)
        assertEquals(entity.title, model.title)
        assertEquals(entity.url, model.url)
        assertEquals(entity.thumbnailUrl, model.thumbnailUrl)
    }

    @Test
    fun `mapToEntity maps all fields correctly`() {
        val dto = createDto()
        val entity = mapper.mapToEntity(dto)

        assertEquals(dto.id, entity.id)
        assertEquals(dto.albumId, entity.albumId)
        assertEquals(dto.title, entity.title)
        assertEquals(dto.url, entity.url)
        assertEquals(dto.thumbnailUrl, entity.thumbnailUrl)
    }

    @Test
    fun `mapToDomain handles edge cases`() {
        val entity = createEntity(id = 0, albumId = -1, title = "", url = "", thumbnailUrl = "")
        val model = mapper.mapToDomain(entity)

        assertEquals(0, model.id)
        assertEquals(-1, model.albumId)
        assertEquals("", model.title)
        assertEquals("", model.url)
        assertEquals("", model.thumbnailUrl)
    }

    @Test
    fun `mapToEntity handles edge cases`() {
        val dto = createDto(id = Int.MAX_VALUE, albumId = Int.MIN_VALUE, title = " ", url = "", thumbnailUrl = "")
        val entity = mapper.mapToEntity(dto)

        assertEquals(Int.MAX_VALUE, entity.id)
        assertEquals(Int.MIN_VALUE, entity.albumId)
        assertEquals(" ", entity.title)
        assertEquals("", entity.url)
        assertEquals("", entity.thumbnailUrl)
    }
}

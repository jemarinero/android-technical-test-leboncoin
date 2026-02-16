package fr.leboncoin.data.datasource

import fr.leboncoin.data.database.dao.AlbumDao
import fr.leboncoin.data.database.entity.AlbumEntity
import fr.leboncoin.testutils.MainCoroutinesRule
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AlbumLocalDSTest {

    @get:Rule
    val coroutinesRule = MainCoroutinesRule()

    private val dao: AlbumDao = mockk()
    private lateinit var datasource: AlbumLocalDS

    @Before
    fun setup() {
        datasource = AlbumLocalDS(dao)
    }

    private fun createAlbum(id: Int, albumId: Int) = AlbumEntity(
        id = id,
        albumId = albumId,
        title = "reprehenderit est deserunt velit ipsam",
        url = "https://placehold.co/600x600/771796/white/png",
        thumbnailUrl = "https://placehold.co/150x150/771796/white/png"
    )

    @Test
    fun `insertAll with valid list persists data correctly`() = coroutinesRule.runTest {
        val list = listOf(createAlbum(1, 101), createAlbum(2, 102))
        every { dao.insertAll(list) } just Runs

        datasource.insertAll(list)

        verify(exactly = 1) { dao.insertAll(list) }
    }

    @Test
    fun `insertAll with empty list handles gracefully`() = coroutinesRule.runTest {
        val emptyList = emptyList<AlbumEntity>()
        every { dao.insertAll(emptyList) } just Runs

        datasource.insertAll(emptyList)

        verify(exactly = 1) { dao.insertAll(emptyList) }
    }

    @Test
    fun `insertAll handles large dataset insertion`() = coroutinesRule.runTest {
        val largeList = (1..1000).map { createAlbum(it, it + 1000) }
        every { dao.insertAll(largeList) } just Runs

        datasource.insertAll(largeList)

        verify { dao.insertAll(largeList) }
    }

    @Test
    fun `getAll returns empty list when table is empty`() = coroutinesRule.runTest {
        every { dao.getAll() } returns emptyList()

        val result = datasource.getAll()

        assertEquals(0, result.size)
    }

    @Test
    fun `getAll retrieves all persisted records`() = coroutinesRule.runTest {
        val list = listOf(createAlbum(1, 101), createAlbum(2, 102))
        every { dao.getAll() } returns list

        val result = datasource.getAll()

        assertEquals(list, result)
    }

    @Test
    fun `clearAll removes all records from the table`() = coroutinesRule.runTest {
        every { dao.deleteAll() } just Runs
        datasource.clearAll()
        verify { dao.deleteAll() }
    }

    @Test
    fun `getAlbumDetail returns correct entity for valid IDs`() = coroutinesRule.runTest {
        val album = createAlbum(1, 101)
        every { dao.getAlbumDetail(1, 101) } returns album

        val result = datasource.getAlbumDetail(1, 101)

        assertEquals(album, result)
    }

    @Test
    fun `getAlbumDetail returns null for non existent IDs`() = coroutinesRule.runTest {
        every { dao.getAlbumDetail(99, 999) } returns null

        val result = datasource.getAlbumDetail(99, 999)

        assertNull(result)
    }

    @Test
    fun `getAlbumDetail with negative ID values`() = coroutinesRule.runTest {
        every { dao.getAlbumDetail(-1, -101) } returns null

        val result = datasource.getAlbumDetail(-1, -101)

        assertNull(result)
    }

    @Test
    fun `getAlbumDetail with boundary integer values`() = coroutinesRule.runTest {
        val album = createAlbum(Int.MAX_VALUE, Int.MIN_VALUE)
        every { dao.getAlbumDetail(Int.MAX_VALUE, Int.MIN_VALUE) } returns album

        val result = datasource.getAlbumDetail(Int.MAX_VALUE, Int.MIN_VALUE)

        assertEquals(album, result)
    }

    @Test
    fun `Data integrity across multiple operations`() = coroutinesRule.runTest {
        val album = createAlbum(1, 101)
        val list = listOf(album)

        every { dao.insertAll(list) } just Runs
        every { dao.getAlbumDetail(1, 101) } returns album andThen null
        every { dao.deleteAll() } just Runs

        datasource.insertAll(list)
        val retrieved = datasource.getAlbumDetail(1, 101)
        datasource.clearAll()
        val afterClear = datasource.getAlbumDetail(1, 101)

        assertEquals(album, retrieved)
        assertNull(afterClear)
    }
}
package fr.leboncoin.data.repository

import fr.leboncoin.data.database.entity.AlbumEntity
import fr.leboncoin.data.datasource.AlbumLocalDS
import fr.leboncoin.data.datasource.AlbumRemoteDS
import fr.leboncoin.data.mappers.AlbumMapper
import fr.leboncoin.data.network.model.AlbumDto
import fr.leboncoin.domain.models.AlbumModel
import fr.leboncoin.domain.models.ErrorType
import fr.leboncoin.domain.models.ResultOf
import fr.leboncoin.domain.repositories.AlbumRepository
import fr.leboncoin.testutils.MainCoroutinesRule
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class AlbumRepositoryImplTest {

    @get:Rule
    val coroutinesRule = MainCoroutinesRule()

    private val remoteDS: AlbumRemoteDS = mockk()
    private val localDS: AlbumLocalDS = mockk()
    private val mapper: AlbumMapper = mockk()
    private lateinit var repository: AlbumRepository

    @Before
    fun setup() {
        repository = AlbumRepositoryImpl(
            remoteDS,
            localDS,
            mapper
        )
    }

    private fun entity(id: Int) =
        AlbumEntity(id, id + 100, "title$id", "url$id", "thumb$id")

    private fun dto(id: Int) =
        AlbumDto(id, id + 100, "title$id", "url$id", "thumb$id")

    private fun model(id: Int) =
        AlbumModel(id, id + 100, "title$id", "url$id", "thumb$id")

    @Test
    fun `getAlbums returns local data when available and no force refresh`() = coroutinesRule.runTest {

        val entities = listOf(entity(1))
        val models = listOf(model(1))

        every { localDS.getAll() } returnsMany listOf(entities, entities)

        every { mapper.mapToDomain(entities[0]) } returns models[0]

        val result = repository.getAlbums(forceRefresh = false)

        assertTrue(result is ResultOf.Success)
        assertEquals(models, (result as ResultOf.Success<List<AlbumModel>>).value)

        coVerify(exactly = 0) { remoteDS.getRemoteAlbums() }
    }

    @Test
    fun `getAlbums fetches remote when local is empty`() = coroutinesRule.runTest {

        val dtos = listOf(dto(1))
        val entities = listOf(entity(1))
        val models = listOf(model(1))

        every { localDS.getAll() } returnsMany listOf(emptyList(), entities)

        coEvery { remoteDS.getRemoteAlbums() } returns ResultOf.Success(dtos)

        every { mapper.mapToEntity(dtos[0]) } returns entities[0]
        every { mapper.mapToDomain(entities[0]) } returns models[0]

        every { localDS.clearAll() } just Runs
        every { localDS.insertAll(entities) } just Runs

        val result = repository.getAlbums(false)

        assertTrue(result is ResultOf.Success)
        assertEquals(models, (result as ResultOf.Success<List<AlbumModel>>).value)

        coVerify { remoteDS.getRemoteAlbums() }
        verify { localDS.clearAll() }
        verify { localDS.insertAll(entities) }
    }

    @Test
    fun `getAlbums fetches remote when forceRefresh true`() = coroutinesRule.runTest {

        val localEntities = listOf(entity(1))
        val dtos = listOf(dto(2))
        val newEntities = listOf(entity(2))
        val models = listOf(model(2))

        every { localDS.getAll() } returnsMany listOf(localEntities, newEntities)

        coEvery { remoteDS.getRemoteAlbums() } returns ResultOf.Success(dtos)

        every { mapper.mapToEntity(dtos[0]) } returns newEntities[0]
        every { mapper.mapToDomain(newEntities[0]) } returns models[0]

        every { localDS.clearAll() } just Runs
        every { localDS.insertAll(newEntities) } just Runs

        val result = repository.getAlbums(true)

        assertTrue(result is ResultOf.Success)
        assertEquals(models, (result as ResultOf.Success<List<AlbumModel>>).value)

        coVerify { remoteDS.getRemoteAlbums() }
    }

    @Test
    fun `getAlbums returns failure when remote fails`() = coroutinesRule.runTest {

        val error = ErrorType.ConnectivityError

        every { localDS.getAll() } returns emptyList()

        coEvery {
            remoteDS.getRemoteAlbums()
        } returns ResultOf.Failure(error)

        val result = repository.getAlbums(false)

        assertTrue(result is ResultOf.Failure)
        assertEquals(error, (result as ResultOf.Failure).errorType)

        verify(exactly = 0) { localDS.insertAll(any()) }
    }

    @Test
    fun `getAlbumDetail returns success when found`() {

        val entity = entity(1)
        val model = model(1)

        every {
            localDS.getAlbumDetail(1, 101)
        } returns entity

        every {
            mapper.mapToDomain(entity)
        } returns model

        val result = repository.getAlbumDetail(1, 101)

        assertTrue(result is ResultOf.Success)
        assertEquals(model, (result as ResultOf.Success<List<AlbumModel>>).value)
    }

    @Test
    fun `getAlbumDetail returns failure when not found`() {

        every {
            localDS.getAlbumDetail(1, 101)
        } returns null

        val result = repository.getAlbumDetail(1, 101)

        assertTrue(result is ResultOf.Failure)
    }

    @Test
    fun `getAlbumDetail returns failure when exception thrown`() {

        every {
            localDS.getAlbumDetail(any(), any())
        } throws RuntimeException()

        val result = repository.getAlbumDetail(1, 101)

        assertTrue(result is ResultOf.Failure)
    }

}
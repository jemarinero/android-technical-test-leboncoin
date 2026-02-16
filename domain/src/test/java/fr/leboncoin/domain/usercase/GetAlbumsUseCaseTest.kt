package fr.leboncoin.domain.usercase

import fr.leboncoin.domain.dispatcher.DispatcherProvider
import fr.leboncoin.domain.models.AlbumModel
import fr.leboncoin.domain.models.ErrorType
import fr.leboncoin.domain.models.ResultOf
import fr.leboncoin.domain.repositories.AlbumRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class GetAlbumsUseCaseTest {

    private val testDispatcher = StandardTestDispatcher()

    private val dispatcherProvider = object : DispatcherProvider {
        override fun main() = testDispatcher
        override fun io() = testDispatcher
        override fun default() = testDispatcher
        override fun unconfined() = testDispatcher
    }

    private val repository: AlbumRepository = mockk()
    private lateinit var useCase: GetAlbumsUseCase

    private fun model(id: Int) =
        AlbumModel(id, id + 100, "title$id", "url$id", "thumb$id")

    @Before
    fun setup() {
        useCase = GetAlbumsUseCase(repository, dispatcherProvider)
    }

    @Test
    fun `should emit list of albums when repository returns success`() = runTest(testDispatcher) {
        val albums = listOf(
            model(id = 1),
            model(id = 2)
        )

        coEvery { repository.getAlbums(forceRefresh = false) } returns ResultOf.Success(albums)

        val result = useCase.execute(GetAlbumsParams()).first()

        assertTrue(result is ResultOf.Success)
        assertEquals(albums, (result as ResultOf.Success).value)

        coVerify { repository.getAlbums(forceRefresh = false) }
    }

    @Test
    fun `should emit error when repository returns failure`() = runTest(testDispatcher) {
        coEvery { repository.getAlbums(forceRefresh = true) } returns ResultOf.Failure(ErrorType.ConnectivityError)

        val result = useCase.execute(GetAlbumsParams(forceRefresh = true)).first()

        assertTrue(result is ResultOf.Failure)
        assertEquals(ErrorType.ConnectivityError, (result as ResultOf.Failure).errorType)

        coVerify { repository.getAlbums(forceRefresh = true) }
    }
}

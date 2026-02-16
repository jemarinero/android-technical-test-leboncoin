package fr.leboncoin.domain.usercase

import app.cash.turbine.test
import fr.leboncoin.domain.dispatcher.DispatcherProvider
import fr.leboncoin.domain.models.AlbumModel
import fr.leboncoin.domain.models.ResultOf
import fr.leboncoin.domain.repositories.AlbumRepository
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class GetAlbumDetailUseCaseTest {

    private val testDispatcher = StandardTestDispatcher()

    private val dispatcherProvider = object : DispatcherProvider {
        override fun main() = testDispatcher
        override fun io() = testDispatcher
        override fun default() = testDispatcher
        override fun unconfined() = testDispatcher
    }

    private val repository: AlbumRepository = mockk()

    private lateinit var useCase: GetAlbumDetailUseCase

    @Before
    fun setup() {
        useCase = GetAlbumDetailUseCase(repository, dispatcherProvider)
    }

    @Test
    fun `should emit album detail`() = runTest(testDispatcher) {

        val album = AlbumModel(
            id = 1,
            albumId = 10,
            title = "Test Album",
            url = "https://example.com/image.jpg",
            thumbnailUrl = "https://example.com/thumbnail.jpg"
        )

        coEvery {
            repository.getAlbumDetail(1, 10)
        } returns ResultOf.Success(album)

        useCase.execute(GetAlbumDetailParams(1, 10)).test {

            val item = awaitItem()

            assertEquals(ResultOf.Success(album), item)

            awaitComplete()
        }
    }

}

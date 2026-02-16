package fr.leboncoin.data.datasource

import fr.leboncoin.data.network.api.AlbumApiService
import fr.leboncoin.data.network.model.AlbumDto
import fr.leboncoin.domain.models.ResultOf
import fr.leboncoin.testutils.MainCoroutinesRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import org.junit.Assert.assertEquals
import retrofit2.Response
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class AlbumRemoteDSTest {

    @get:Rule
    val coroutinesRule = MainCoroutinesRule()

    val albumApiService: AlbumApiService = mockk()
    private lateinit var albumRemoteDS: AlbumRemoteDS

    private fun createAlbumDto(id: Int, albumId: Int) = AlbumDto(
        id = id,
        albumId = albumId,
        title = "reprehenderit est deserunt velit ipsam",
        url = "https://placehold.co/600x600/771796/white/png",
        thumbnailUrl = "https://placehold.co/150x150/771796/white/png"
    )

    @Before
    fun setup() {
        albumRemoteDS = AlbumRemoteDS(albumApiService)
    }

    @Test
    fun `getRemoteAlbums returns success result when API call succeeds`() = coroutinesRule.runTest {
        val albumList = Response.success (listOf(createAlbumDto(1, 1), createAlbumDto(1, 2)))

        coEvery { albumApiService.getAlbums() } returns albumList

        val result = albumRemoteDS.getRemoteAlbums()

        assertTrue(result is ResultOf.Success)
        assertEquals(albumList.body(), (result as ResultOf.Success).value)

        coVerify(exactly = 1) { albumApiService.getAlbums() }
    }

    @Test
    fun `getRemoteAlbums returns failure when API returns error response`() = coroutinesRule.runTest {
        val errorResponse = Response.error<List<AlbumDto>>(404, okhttp3.ResponseBody.create(null, "Not Found"))

        coEvery { albumApiService.getAlbums() } returns errorResponse

        val result = albumRemoteDS.getRemoteAlbums()

        assertTrue(result is ResultOf.Failure)

        coVerify(exactly = 1) { albumApiService.getAlbums() }
    }


    @Test
    fun `getRemoteAlbums returns failure when API call throws exception`() = coroutinesRule.runTest {
        val exception = RuntimeException("Network error")
        coEvery { albumApiService.getAlbums() } throws exception

        val result = albumRemoteDS.getRemoteAlbums()

        assertTrue(result is ResultOf.Failure)

        coVerify(exactly = 1) { albumApiService.getAlbums() }
    }

    @Test
    fun `getRemoteAlbums returns success with empty list`() = coroutinesRule.runTest {
        val emptyResponse = Response.success(emptyList<AlbumDto>())
        coEvery { albumApiService.getAlbums() } returns emptyResponse

        val result = albumRemoteDS.getRemoteAlbums()

        assertTrue(result is ResultOf.Success)
        assertTrue((result as ResultOf.Success).value.isEmpty())

        coVerify { albumApiService.getAlbums() }
    }


}
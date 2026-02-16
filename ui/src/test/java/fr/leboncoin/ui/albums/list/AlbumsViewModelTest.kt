package fr.leboncoin.ui.albums.list


import app.cash.turbine.test
import fr.leboncoin.domain.models.AlbumModel
import fr.leboncoin.domain.models.ErrorType
import fr.leboncoin.domain.models.ResultOf
import fr.leboncoin.domain.usercase.GetAlbumsParams
import fr.leboncoin.domain.usercase.GetAlbumsUseCase
import fr.leboncoin.testutils.MainCoroutinesRule
import fr.leboncoin.ui.models.toUiModel
import fr.leboncoin.ui.tracking.AnalyticsTracker
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AlbumsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainCoroutinesRule()
    private val getAlbumsUseCase: GetAlbumsUseCase = mockk()
    private val analyticsTracker: AnalyticsTracker = mockk(relaxed = true)
    private val fakealbum = AlbumModel(
        id = 1,
        albumId = 1,
        title = "reprehenderit est deserunt velit ipsam",
        url = "https://placehold.co/600x600/771796/white/png",
        thumbnailUrl = "https://placehold.co/150x150/771796/white/png"
    )

    private lateinit var viewModel: AlbumsViewModel

    @Before
    fun setUp() {
        coEvery { getAlbumsUseCase.execute(any()) } returns emptyFlow()
    }

    @Test
    fun `loadAlbums success updates uiState with albums`() = mainDispatcherRule.runTest {
        val albums = listOf(fakealbum)
        coEvery { getAlbumsUseCase.execute(GetAlbumsParams(false)) } returns flowOf(ResultOf.Success(albums))

        initViewModel()

        viewModel.uiState.test {
            skipItems(1)
            assertEquals(true, awaitItem().isLoading)

            val state = awaitItem()
            val expectedState = UiState(albums = albums, isLoading = false, error = null)
            assertEquals(expectedState, state)
        }
    }

    @Test
    fun `loadAlbums success updates uiState with empty list`() = mainDispatcherRule.runTest {
        coEvery { getAlbumsUseCase.execute(GetAlbumsParams(false)) } returns flowOf(ResultOf.Success(emptyList()))

        initViewModel()

        viewModel.uiState.test {
            skipItems(1)
            assertEquals(true, awaitItem().isLoading)

            val state = awaitItem()
            val expectedState = UiState()
            assertEquals(expectedState, state)
        }
    }

    @Test
    fun `loadAlbums failure updates uiState UnknownError`() = mainDispatcherRule.runTest {
        coEvery { getAlbumsUseCase.execute(GetAlbumsParams(false)) } returns flowOf(ResultOf.Failure(ErrorType.UnknownError))

        initViewModel()

        viewModel.uiState.test {
            skipItems(1)
            assertEquals(true, awaitItem().isLoading)

            val state = awaitItem()
            val expectedState = UiState(error = ErrorType.UnknownError.toUiModel(), isLoading = false)
            assertEquals(expectedState, state)
        }
    }

    @Test
    fun `loadAlbums failure updates uiState ConnectivityError`() = mainDispatcherRule.runTest {
        coEvery { getAlbumsUseCase.execute(GetAlbumsParams(false)) } returns flowOf(ResultOf.Failure(ErrorType.ConnectivityError))

        initViewModel()

        viewModel.uiState.test {
            skipItems(1)
            assertEquals(true, awaitItem().isLoading)

            val state = awaitItem()
            val expectedState = UiState(error = ErrorType.ConnectivityError.toUiModel(), isLoading = false)
            assertEquals(expectedState, state)
        }
    }

    @Test
    fun `refresh calls loadAlbums with forceRefresh true`() = mainDispatcherRule.runTest {
        initViewModel()

        viewModel.refresh()

        coVerify { getAlbumsUseCase.execute(GetAlbumsParams(forceRefresh = true)) }
    }

    @Test
    fun `analytics tracking functions are called`() = mainDispatcherRule.runTest {
        val screenName = "AlbumsScreen"
        val itemId = "1"

        initViewModel()

        viewModel.onScreenViewed(screenName)
        viewModel.onItemSelected(itemId)

        verify { analyticsTracker.trackScreenViewed(screenName, any()) }
        verify { analyticsTracker.trackItemSelected(itemId, any()) }
    }

    private fun initViewModel() {
        viewModel = AlbumsViewModel(getAlbumsUseCase, analyticsTracker)
    }
}

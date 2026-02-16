package fr.leboncoin.ui.albums.detail

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import fr.leboncoin.domain.models.AlbumModel
import fr.leboncoin.domain.models.ErrorType
import fr.leboncoin.domain.models.ResultOf
import fr.leboncoin.domain.usercase.GetAlbumDetailParams
import fr.leboncoin.domain.usercase.GetAlbumDetailUseCase
import fr.leboncoin.testutils.MainCoroutinesRule
import fr.leboncoin.ui.models.toUiModel
import fr.leboncoin.ui.navigation.ARG_ALBUM_ID
import fr.leboncoin.ui.navigation.ARG_ID
import fr.leboncoin.ui.tracking.AnalyticsTracker
import io.mockk.coEvery
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
class AlbumDetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainCoroutinesRule()

    private val getAlbumDetailUseCase: GetAlbumDetailUseCase = mockk()
    private val analyticsTracker: AnalyticsTracker = mockk(relaxed = true)

    private lateinit var viewModel: AlbumDetailViewModel
    private val savedStateHandle = SavedStateHandle(
        mapOf(
            ARG_ID to 1,
            ARG_ALBUM_ID to 1
        )
    )

    private val fakeAlbum = AlbumModel(
        id = 1,
        albumId = 1,
        title = "reprehenderit est deserunt velit ipsam",
        url = "https://placehold.co/600x600/771796/white/png",
        thumbnailUrl = "https://placehold.co/150x150/771796/white/png"
    )

    @Before
    fun setUp() {
        coEvery { getAlbumDetailUseCase.execute(any()) } returns emptyFlow()
    }

    private fun initViewModel() {
        viewModel = AlbumDetailViewModel(getAlbumDetailUseCase, analyticsTracker, savedStateHandle)
    }

    @Test
    fun `loadAlbum success updates state with album`() = mainDispatcherRule.runTest {
        coEvery { getAlbumDetailUseCase.execute(GetAlbumDetailParams(1, 1)) } returns flowOf(ResultOf.Success(fakeAlbum))

        initViewModel()

        viewModel.state.test {
            skipItems(1)
            val loadingState = awaitItem()
            assertEquals(true, loadingState.isLoading)

            val finalState = awaitItem()
            val expectedState = AlbumDetailUiState(isLoading = false, album = fakeAlbum, error = null)
            assertEquals(expectedState, finalState)
        }
    }

    @Test
    fun `loadAlbum failure updates state UnknownError`() = mainDispatcherRule.runTest {
        coEvery { getAlbumDetailUseCase.execute(GetAlbumDetailParams(1, 1)) } returns flowOf(ResultOf.Failure(
            ErrorType.UnknownError))

        initViewModel()

        viewModel.state.test {
            skipItems(1)
            val loadingState = awaitItem()
            assertEquals(true, loadingState.isLoading)

            val finalState = awaitItem()
            val expectedState = AlbumDetailUiState(isLoading = false, album = null, error = ErrorType.UnknownError.toUiModel())
            assertEquals(expectedState, finalState)
        }
    }

    @Test
    fun `loadAlbum failure updates state ConnectivityError`() = mainDispatcherRule.runTest {
        coEvery { getAlbumDetailUseCase.execute(GetAlbumDetailParams(1, 1)) } returns flowOf(ResultOf.Failure(ErrorType.ConnectivityError))

        initViewModel()

        viewModel.state.test {
            skipItems(1)
            val loadingState = awaitItem()
            assertEquals(true, loadingState.isLoading)

            val finalState = awaitItem()
            val expectedState = AlbumDetailUiState(isLoading = false, album = null, error = ErrorType.ConnectivityError.toUiModel())
            assertEquals(expectedState, finalState)
        }
    }

    @Test
    fun `onErrorClose clears error`() = mainDispatcherRule.runTest {
        coEvery { getAlbumDetailUseCase.execute(GetAlbumDetailParams(1, 1)) } returns flowOf(ResultOf.Failure(ErrorType.UnknownError))

        initViewModel()

        viewModel.state.test {
            skipItems(1)
            assertEquals(true, awaitItem().isLoading)
            val finalState = awaitItem()
            assertEquals(ErrorType.UnknownError.toUiModel(), finalState.error)

            viewModel.onErrorClose()
            val clearedState = awaitItem()
            assertEquals(null, clearedState.error)
        }
    }

    @Test
    fun `analytics tracking functions are called`() = mainDispatcherRule.runTest {
        initViewModel()
        val screenName = "AlbumDetailScreen"

        viewModel.onScreenViewed(screenName)

        verify { analyticsTracker.trackScreenViewed(screenName, any()) }
    }
}

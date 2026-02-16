package fr.leboncoin.domain.usercase

import fr.leboncoin.domain.dispatcher.DispatcherProvider
import fr.leboncoin.domain.repositories.AnalyticsRepository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TrackScreenViewUseCaseTest {

    private val testDispatcher = StandardTestDispatcher()

    private val dispatcherProvider = object : DispatcherProvider {
        override fun main() = testDispatcher
        override fun io() = testDispatcher
        override fun default() = testDispatcher
        override fun unconfined() = testDispatcher
    }

    private val analyticsRepository: AnalyticsRepository = mockk()
    private lateinit var useCase: TrackScreenViewUseCase

    @Before
    fun setup() {
        useCase = TrackScreenViewUseCase(analyticsRepository, dispatcherProvider)
    }

    @Test
    fun `should call trackScreenView and emit Unit`() = runTest(testDispatcher) {
        val screenName = "HomeScreen"
        coEvery { analyticsRepository.trackScreenView(screenName) } just Runs

        val result = useCase.execute(TrackScreenViewParams(screenName)).first()

        assertEquals(Unit, result)
        coVerify { analyticsRepository.trackScreenView(screenName) }
    }
}

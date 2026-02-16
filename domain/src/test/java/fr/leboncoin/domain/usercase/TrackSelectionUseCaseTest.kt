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
class TrackSelectionUseCaseTest {

    private val testDispatcher = StandardTestDispatcher()

    private val dispatcherProvider = object : DispatcherProvider {
        override fun main() = testDispatcher
        override fun io() = testDispatcher
        override fun default() = testDispatcher
        override fun unconfined() = testDispatcher
    }

    private val analyticsRepository: AnalyticsRepository = mockk()
    private lateinit var useCase: TrackSelectionUseCase

    @Before
    fun setup() {
        useCase = TrackSelectionUseCase(analyticsRepository, dispatcherProvider)
    }

    @Test
    fun `should call trackSelection and emit Unit`() = runTest(testDispatcher) {
        val itemId = "item_123"
        coEvery { analyticsRepository.trackSelection(itemId) } just Runs

        val result = useCase.execute(TrackSelectionParams(itemId)).first()

        assertEquals(Unit, result)
        coVerify { analyticsRepository.trackSelection(itemId) }
    }
}

package info.jukov.rijksmuseum.feature.art.details.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import info.jukov.rijksmuseum.Const
import info.jukov.rijksmuseum.RxTestSchedulersRule
import info.jukov.rijksmuseum.feature.art.details.domain.ArtDetailsRepository
import info.jukov.rijksmuseum.feature.art.details.domain.model.ArtDetails
import info.jukov.rijksmuseum.feature.art.details.presentation.model.ArtDetailsUiState
import info.jukov.rijksmuseum.util.error.AppException
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Single
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeUnit

class ArtDetailsViewModelTest {

    @JvmField
    @Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @JvmField
    @Rule
    val rxTestSchedulersRule = RxTestSchedulersRule()

    private val scheduler = rxTestSchedulersRule.scheduler

    private val repository: ArtDetailsRepository = mockk()

    private val savedStateHandle = SavedStateHandle().apply {
        set(Const.Keys.ITEM_ID, id)
    }

    private val viewModel = ArtDetailsViewModel(savedStateHandle, repository)

    @Test
    fun init() {
        assertEquals(
            ArtDetailsUiState.Progress,
            viewModel.model.value
        )
    }

    @Test
    fun load() {
        every { repository.get(id) } returns Single.just(artDetails)

        viewModel.init()

        scheduler.advanceTimeBy(1L, TimeUnit.MILLISECONDS)

        assertEquals(
            ArtDetailsUiState.Content(artDetails),
            viewModel.model.value
        )
        verify(exactly = 1) { repository.get(id) }
    }

    @Test
    fun reload() {
        every { repository.get(id) } returns Single.error(AppException(123))

        viewModel.init()

        scheduler.advanceTimeBy(1L, TimeUnit.MILLISECONDS)

        assertEquals(
            ArtDetailsUiState.Error(123),
            viewModel.model.value
        )

        every { repository.get(id) } returns Single.just(artDetails)

        viewModel.reload()

        assertEquals(
            ArtDetailsUiState.Progress,
            viewModel.model.value
        )

        scheduler.advanceTimeBy(1L, TimeUnit.MILLISECONDS)

        assertEquals(
            ArtDetailsUiState.Content(artDetails),
            viewModel.model.value
        )

        verify(exactly = 2) { repository.get(id) }
    }

    @Test
    fun `load with error`() {
        every { repository.get(id) } returns Single.error(AppException(123))

        viewModel.init()

        scheduler.advanceTimeBy(1L, TimeUnit.MILLISECONDS)

        assertEquals(
            ArtDetailsUiState.Error(123),
            viewModel.model.value
        )
        verify(exactly = 1) { repository.get(id) }
    }

    @Test
    fun `load with error unknown`() {
        every { repository.get(id) } returns Single.error(IllegalStateException("test"))

        viewModel.init()

        scheduler.advanceTimeBy(1L, TimeUnit.MILLISECONDS)

        assertEquals(
            ArtDetailsUiState.Error(null),
            viewModel.model.value
        )
        verify(exactly = 1) { repository.get(id) }
    }

    companion object {
        const val id = "id"

        val artDetails = ArtDetails(
            id,
            "title",
            emptyList(),
            null,
            null,
            null,
            emptyList(),
            emptyList(),
            emptyList(),
            null,
            null
        )
    }
}
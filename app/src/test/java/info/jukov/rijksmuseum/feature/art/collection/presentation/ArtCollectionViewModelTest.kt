package info.jukov.rijksmuseum.feature.art.collection.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import info.jukov.rijksmuseum.RxTestSchedulersRule
import info.jukov.rijksmuseum.feature.art.collection.domain.ArtCollectionRepository
import info.jukov.rijksmuseum.feature.art.collection.domain.model.ArtCollectionItem
import info.jukov.rijksmuseum.feature.art.collection.presentation.model.ArtCollectionUiModel
import info.jukov.rijksmuseum.feature.art.collection.presentation.model.ArtCollectionUiState
import info.jukov.rijksmuseum.feature.art.collection.presentation.model.PageState
import info.jukov.rijksmuseum.util.error.AppException
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Single
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import java.util.Optional
import java.util.concurrent.TimeUnit


class ArtCollectionViewModelTest {

    @JvmField
    @Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    @JvmField
    @Rule
    val rxTestSchedulersRule = RxTestSchedulersRule()

    private val scheduler = rxTestSchedulersRule.scheduler

    private val repository: ArtCollectionRepository = mockk()

    private val viewModel = ArtCollectionViewModel(repository, pageSize = 5)

    @Test
    fun `first model is progress`() {
        assertEquals(
            ArtCollectionUiState.EmptyProgress,
            viewModel.model.value
        )
        verify(exactly = 0) { repository.get(any()) }
    }

    @Test
    fun `initial load with success`() {
        every { repository.get(1) } returns Single.just(emptyList())

        viewModel.init()

        assertEquals(
            ArtCollectionUiState.EmptyProgress,
            viewModel.model.value
        )

        scheduler.advanceTimeBy(1L, TimeUnit.MILLISECONDS)

        assertEquals(
            ArtCollectionUiState.Content(
                false,
                PageState.None,
                false,
                1,
                emptyList()
            ),
            viewModel.model.value
        )
        verify(exactly = 1) { repository.get(1) }
    }

    @Test
    fun `initial load with error`() {
        every { repository.get(1) } returns Single.error(AppException(123))

        viewModel.init()

        scheduler.advanceTimeBy(1L, TimeUnit.MILLISECONDS)

        assertEquals(
            ArtCollectionUiState.EmptyError(123),
            viewModel.model.value
        )
        verify(exactly = 1) { repository.get(1) }
    }

    @Test
    fun `initial load with error unmapped`() {
        every { repository.get(1) } returns Single.error(IllegalStateException())

        viewModel.init()

        scheduler.advanceTimeBy(1L, TimeUnit.MILLISECONDS)

        assertEquals(
            ArtCollectionUiState.EmptyError(null),
            viewModel.model.value
        )
        verify(exactly = 1) { repository.get(1) }
    }

    @Test
    fun `initial load mapper unique authors`() {
        every { repository.get(1) } returns Single.just(items_UniqueAuthors_Page1)

        viewModel.init()

        scheduler.advanceTimeBy(1L, TimeUnit.MILLISECONDS)

        assertEquals(
            ArtCollectionUiState.Content(
                refreshing = false,
                newPageState = PageState.None,
                hasNext = true,
                lastLoadedPage = 1,
                items = uiModels_UniqueAuthors_Page1
            ),
            viewModel.model.value
        )
        verify(exactly = 1) { repository.get(1) }
    }

    @Test
    fun `initial load mapper one author`() {
        every { repository.get(1) } returns Single.just(items_OneAuthor_Page1)

        viewModel.init()

        scheduler.advanceTimeBy(1L, TimeUnit.MILLISECONDS)

        assertEquals(
            ArtCollectionUiState.Content(
                refreshing = false,
                newPageState = PageState.None,
                hasNext = true,
                lastLoadedPage = 1,
                items = uiModels_OneAuthor_Page1
            ),
            viewModel.model.value
        )
        verify(exactly = 1) { repository.get(1) }
    }

    @Test
    fun `load more mapper unique authors`() {
        every { repository.get(1) } returns Single.just(items_UniqueAuthors_Page1)
        every { repository.get(2) } returns Single.just(items_UniqueAuthors_Page2)

        viewModel.init()

        scheduler.advanceTimeBy(1L, TimeUnit.MILLISECONDS)

        viewModel.loadMore()

        scheduler.advanceTimeBy(1L, TimeUnit.MILLISECONDS)

        assertEquals(
            ArtCollectionUiState.Content(
                refreshing = false,
                newPageState = PageState.None,
                hasNext = true,
                lastLoadedPage = 2,
                items = uiModels_UniqueAuthors_Page1 + uiModels_UniqueAuthors_Page2
            ),
            viewModel.model.value
        )
        verify(exactly = 1) { repository.get(1) }
        verify(exactly = 1) { repository.get(2) }
    }

    @Test
    fun `load more with reload`() {
        every { repository.get(1) } returns Single.just(items_UniqueAuthors_Page1)
        every { repository.get(2) } returns Single.error(AppException(123))

        viewModel.init()

        scheduler.advanceTimeBy(1L, TimeUnit.MILLISECONDS)

        viewModel.loadMore()

        assertEquals(
            ArtCollectionUiState.Content(
                refreshing = false,
                newPageState = PageState.Progress,
                hasNext = true,
                lastLoadedPage = 1,
                items = uiModels_UniqueAuthors_Page1
            ),
            viewModel.model.value
        )

        scheduler.advanceTimeBy(1L, TimeUnit.MILLISECONDS)

        every { repository.get(2) } returns Single.just(items_UniqueAuthors_Page2)

        viewModel.reloadPage()

        scheduler.advanceTimeBy(1L, TimeUnit.MILLISECONDS)

        assertEquals(
            ArtCollectionUiState.Content(
                refreshing = false,
                newPageState = PageState.None,
                hasNext = true,
                lastLoadedPage = 2,
                items = uiModels_UniqueAuthors_Page1 + uiModels_UniqueAuthors_Page2
            ),
            viewModel.model.value
        )
        verify(exactly = 1) { repository.get(1) }
        verify(exactly = 2) { repository.get(2) }
    }

    @Test
    fun `load more mapper single author`() {
        every { repository.get(1) } returns Single.just(items_OneAuthor_Page1)
        every { repository.get(2) } returns Single.just(items_OneAuthor_Page2)

        viewModel.init()

        scheduler.advanceTimeBy(1L, TimeUnit.MILLISECONDS)

        viewModel.loadMore()

        scheduler.advanceTimeBy(1L, TimeUnit.MILLISECONDS)

        assertEquals(
            ArtCollectionUiState.Content(
                refreshing = false,
                newPageState = PageState.None,
                hasNext = true,
                lastLoadedPage = 2,
                items = uiModels_OneAuthor_Page1 + uiModels_OneAuthor_Page2
            ),
            viewModel.model.value
        )
        verify(exactly = 1) { repository.get(1) }
        verify(exactly = 1) { repository.get(2) }
    }

    @Test
    fun `load more doesn't have next`() {
        every { repository.get(1) } returns Single.just(emptyList())

        viewModel.init()

        scheduler.advanceTimeBy(1L, TimeUnit.MILLISECONDS)

        viewModel.loadMore()

        assertEquals(
            ArtCollectionUiState.Content(
                refreshing = false,
                newPageState = PageState.None,
                hasNext = false,
                lastLoadedPage = 1,
                items = emptyList()
            ),
            viewModel.model.value
        )
        verify(exactly = 1) { repository.get(1) }
    }

    @Test
    fun `load more last page`() {
        every { repository.get(1) } returns Single.just(items_UniqueAuthors_Page1)
        every { repository.get(2) } returns Single.just(emptyList())

        viewModel.init()

        scheduler.advanceTimeBy(1L, TimeUnit.MILLISECONDS)

        viewModel.loadMore()

        scheduler.advanceTimeBy(1L, TimeUnit.MILLISECONDS)

        assertEquals(
            ArtCollectionUiState.Content(
                refreshing = false,
                newPageState = PageState.None,
                hasNext = false,
                lastLoadedPage = 2,
                items = uiModels_UniqueAuthors_Page1
            ),
            viewModel.model.value
        )
        verify(exactly = 1) { repository.get(1) }
        verify(exactly = 1) { repository.get(2) }
    }

    @Test
    fun `load more with error`() {
        every { repository.get(1) } returns Single.just(items_UniqueAuthors_Page1)
        every { repository.get(2) } returns Single.error(AppException(123))

        viewModel.init()

        scheduler.advanceTimeBy(1L, TimeUnit.MILLISECONDS)

        viewModel.loadMore()

        scheduler.advanceTimeBy(1L, TimeUnit.MILLISECONDS)

        assertEquals(
            ArtCollectionUiState.Content(
                refreshing = false,
                newPageState = PageState.Error(123),
                hasNext = true,
                lastLoadedPage = 1,
                items = uiModels_UniqueAuthors_Page1
            ),
            viewModel.model.value
        )
        verify(exactly = 1) { repository.get(1) }
        verify(exactly = 1) { repository.get(2) }
    }

    @Test
    fun `load more with error unmapped`() {
        every { repository.get(1) } returns Single.just(items_UniqueAuthors_Page1)
        every { repository.get(2) } returns Single.error(IllegalStateException())

        viewModel.init()

        scheduler.advanceTimeBy(1L, TimeUnit.MILLISECONDS)

        viewModel.loadMore()

        scheduler.advanceTimeBy(1L, TimeUnit.MILLISECONDS)

        assertEquals(
            ArtCollectionUiState.Content(
                refreshing = false,
                newPageState = PageState.Error(null),
                hasNext = true,
                lastLoadedPage = 1,
                items = uiModels_UniqueAuthors_Page1
            ),
            viewModel.model.value
        )
        verify(exactly = 1) { repository.get(1) }
        verify(exactly = 1) { repository.get(2) }
    }

    @Test
    fun reload() {
        every { repository.get(1) } returns Single.just(items_UniqueAuthors_Page1)
        every { repository.get(2) } returns Single.error(AppException(123))

        viewModel.init()

        scheduler.advanceTimeBy(1L, TimeUnit.MILLISECONDS)

        viewModel.loadMore()

        scheduler.advanceTimeBy(1L, TimeUnit.MILLISECONDS)

        viewModel.reload()

        assertEquals(
            ArtCollectionUiState.EmptyProgress,
            viewModel.model.value
        )

        scheduler.advanceTimeBy(1L, TimeUnit.MILLISECONDS)

        assertEquals(
            ArtCollectionUiState.Content(
                refreshing = false,
                newPageState = PageState.None,
                hasNext = true,
                lastLoadedPage = 1,
                items = uiModels_UniqueAuthors_Page1
            ),
            viewModel.model.value
        )
        verify(exactly = 2) { repository.get(1) }
        verify(exactly = 1) { repository.get(2) }
    }

    @Test
    fun refresh() {
        every { repository.get(1) } returns Single.just(items_UniqueAuthors_Page1)
        every { repository.get(2) } returns Single.just(items_UniqueAuthors_Page2)

        viewModel.init()

        scheduler.advanceTimeBy(1L, TimeUnit.MILLISECONDS)

        viewModel.loadMore()

        scheduler.advanceTimeBy(1L, TimeUnit.MILLISECONDS)

        viewModel.refresh()

        assertEquals(
            ArtCollectionUiState.Content(
                refreshing = true,
                newPageState = PageState.None,
                hasNext = true,
                lastLoadedPage = 2,
                items = uiModels_UniqueAuthors_Page1 + uiModels_UniqueAuthors_Page2
            ),
            viewModel.model.value
        )

        scheduler.advanceTimeBy(1L, TimeUnit.MILLISECONDS)

        assertEquals(
            ArtCollectionUiState.Content(
                refreshing = false,
                newPageState = PageState.None,
                hasNext = true,
                lastLoadedPage = 1,
                items = uiModels_UniqueAuthors_Page1
            ),
            viewModel.model.value
        )
        verify(exactly = 2) { repository.get(1) }
        verify(exactly = 1) { repository.get(2) }
    }

    @Test
    fun `refresh with error`() {
        every { repository.get(1) } returns Single.just(items_UniqueAuthors_Page1)
        every { repository.get(2) } returns Single.just(items_UniqueAuthors_Page2)

        viewModel.init()

        scheduler.advanceTimeBy(1L, TimeUnit.MILLISECONDS)

        viewModel.loadMore()

        scheduler.advanceTimeBy(1L, TimeUnit.MILLISECONDS)

        every { repository.get(1) } returns Single.error(AppException(123))

        viewModel.refresh()

        scheduler.advanceTimeBy(1L, TimeUnit.MILLISECONDS)

        assertEquals(
            ArtCollectionUiState.Content(
                refreshing = false,
                newPageState = PageState.None,
                hasNext = true,
                lastLoadedPage = 2,
                items = uiModels_UniqueAuthors_Page1 + uiModels_UniqueAuthors_Page2
            ),
            viewModel.model.value
        )
        assertEquals(
            Optional.of(123),
            viewModel.error.value
        )
        verify(exactly = 2) { repository.get(1) }
        verify(exactly = 1) { repository.get(2) }
    }

    companion object {
        private val items_UniqueAuthors_Page1 = (1..5).map { index ->
            ArtCollectionItem(
                id = index.toString(),
                title = index.toString(),
                longTitle = index.toString(),
                author = index.toString(),
                imageUrl = null,
                imageAspectRatio = null
            )
        }
        private val uiModels_UniqueAuthors_Page1 = (1..5).flatMap { index ->
            listOf(
                ArtCollectionUiModel.Header(index.toString()),
                ArtCollectionUiModel.Item(
                    ArtCollectionItem(
                        id = index.toString(),
                        title = index.toString(),
                        longTitle = index.toString(),
                        author = index.toString(),
                        imageUrl = null,
                        imageAspectRatio = null
                    )
                )
            )
        }
        private val items_UniqueAuthors_Page2 = (6..10).map { index ->
            ArtCollectionItem(
                id = index.toString(),
                title = index.toString(),
                longTitle = index.toString(),
                author = index.toString(),
                imageUrl = null,
                imageAspectRatio = null
            )
        }
        private val uiModels_UniqueAuthors_Page2 = (6..10).flatMap { index ->
            listOf(
                ArtCollectionUiModel.Header(index.toString()),
                ArtCollectionUiModel.Item(
                    ArtCollectionItem(
                        id = index.toString(),
                        title = index.toString(),
                        longTitle = index.toString(),
                        author = index.toString(),
                        imageUrl = null,
                        imageAspectRatio = null
                    )
                )
            )
        }
        private val items_OneAuthor_Page1 = (1..5).map { index ->
            ArtCollectionItem(
                id = index.toString(),
                title = index.toString(),
                longTitle = index.toString(),
                author = "author",
                imageUrl = null,
                imageAspectRatio = null
            )
        }
        private val uiModels_OneAuthor_Page1 =
            mutableListOf<ArtCollectionUiModel>(ArtCollectionUiModel.Header("author"))
                .apply {
                    addAll(
                        (1..5).flatMap { index ->
                            listOf(
                                ArtCollectionUiModel.Item(
                                    ArtCollectionItem(
                                        id = index.toString(),
                                        title = index.toString(),
                                        longTitle = index.toString(),
                                        author = "author",
                                        imageUrl = null,
                                        imageAspectRatio = null
                                    )
                                )
                            )
                        }
                    )
                }
        private val items_OneAuthor_Page2 = (6..10).map { index ->
            ArtCollectionItem(
                id = index.toString(),
                title = index.toString(),
                longTitle = index.toString(),
                author = "author",
                imageUrl = null,
                imageAspectRatio = null
            )
        }
        private val uiModels_OneAuthor_Page2 =
            (6..10).flatMap { index ->
                listOf(
                    ArtCollectionUiModel.Item(
                        ArtCollectionItem(
                            id = index.toString(),
                            title = index.toString(),
                            longTitle = index.toString(),
                            author = "author",
                            imageUrl = null,
                            imageAspectRatio = null
                        )
                    )
                )
            }

    }
}
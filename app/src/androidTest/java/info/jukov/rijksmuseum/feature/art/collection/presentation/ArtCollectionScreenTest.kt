package info.jukov.rijksmuseum.feature.art.collection.presentation

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.lifecycle.MutableLiveData
import info.jukov.rijksmuseum.R
import info.jukov.rijksmuseum.feature.art.collection.domain.model.ArtCollectionItem
import info.jukov.rijksmuseum.feature.art.collection.presentation.model.ArtCollectionUiModel
import info.jukov.rijksmuseum.feature.art.collection.presentation.model.ArtCollectionUiState
import info.jukov.rijksmuseum.feature.art.collection.presentation.model.PageState
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Optional

class ArtCollectionScreenTest {

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val viewModel = mockk<ArtCollectionViewModel>()

    @Before
    fun setUp() {
        every { viewModel.error } returns MutableLiveData(Optional.empty())
        every { viewModel.loadMore() } returns Unit
    }

    @Test
    fun progressState() {
        every { viewModel.model } returns MutableLiveData(ArtCollectionUiState.EmptyProgress)

        composeTestRule.setContent {
            ArtCollectionScreen(
                onItemClick = { _, _ -> },
                viewModel = viewModel
            )
        }

        composeTestRule
            .onNodeWithTag("ArtCollectionEmptyProgress")
            .assertExists()
    }

    @Test
    fun errorState() {
        every { viewModel.model } returns MutableLiveData(ArtCollectionUiState.EmptyError(R.string.error_network))

        composeTestRule.setContent {
            ArtCollectionScreen(
                onItemClick = { _, _ -> },
                viewModel = viewModel
            )
        }

        composeTestRule
            .onNodeWithTag("ErrorState")
            .assertExists()
    }

    @Test
    fun contentState() {
        every { viewModel.model } returns MutableLiveData(
            ArtCollectionUiState.Content(
                refreshing = false,
                PageState.None,
                false,
                1,
                uiModels
            )
        )

        composeTestRule.setContent {
            ArtCollectionScreen(
                onItemClick = { _, _ -> },
                viewModel = viewModel
            )
        }

        composeTestRule
            .onNodeWithTag("ArtCollectionContent")
            .assertExists()
    }

    @Test
    fun contentState_PageProgress() {
        every { viewModel.model } returns MutableLiveData(
            ArtCollectionUiState.Content(
                refreshing = false,
                PageState.Progress,
                false,
                1,
                uiModels
            )
        )

        composeTestRule.setContent {
            ArtCollectionScreen(
                onItemClick = { _, _ -> },
                viewModel = viewModel
            )
        }

        composeTestRule
            .onNodeWithTag("ArtCollectionPageProgress")
            .assertExists()
    }

    @Test
    fun contentState_PageError() {
        every { viewModel.model } returns MutableLiveData(
            ArtCollectionUiState.Content(
                refreshing = false,
                PageState.Error(R.string.error_network),
                false,
                1,
                uiModels
            )
        )

        composeTestRule.setContent {
            ArtCollectionScreen(
                onItemClick = { _, _ -> },
                viewModel = viewModel
            )
        }

        composeTestRule
            .onNodeWithTag("ArtCollectionPageError")
            .assertExists()
    }

    companion object {
        private val uiModels = (1..2).flatMap { index ->
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
    }
}
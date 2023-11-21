package info.jukov.rijksmuseum.feature.art.collection.presentation.model

import info.jukov.rijksmuseum.util.UiState

sealed class ArtCollectionUiState {

    abstract val uiState: UiState

    data class Content(
        val refreshing: Boolean,
        val newPageState: PageState,
        val hasNext: Boolean,
        val lastLoadedPage: Int,
        val items: List<ArtCollectionUiModel>
    ) : ArtCollectionUiState() {
        override val uiState: UiState = UiState.Content
    }

    object EmptyProgress : ArtCollectionUiState() {
        override val uiState: UiState = UiState.Progress
    }

    data class EmptyError(
        val message: String?
    ) : ArtCollectionUiState() {
        override val uiState: UiState = UiState.Error
    }
}
package info.jukov.rijksmuseum.feature.art.collection.presentation.model

import androidx.annotation.StringRes

sealed class ArtCollectionUiState {

    data class Content(
        val refreshing: Boolean,
        val newPageState: PageState,
        val hasNext: Boolean,
        val lastLoadedPage: Int,
        val items: List<ArtCollectionUiModel>
    ) : ArtCollectionUiState()

    object EmptyProgress : ArtCollectionUiState()

    data class EmptyError(@StringRes val message: Int?) : ArtCollectionUiState()
}
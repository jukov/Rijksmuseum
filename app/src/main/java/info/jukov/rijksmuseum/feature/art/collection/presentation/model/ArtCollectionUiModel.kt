package info.jukov.rijksmuseum.feature.art.collection.presentation.model

import info.jukov.rijksmuseum.feature.art.collection.domain.model.ArtCollectionItem

sealed class ArtCollectionUiModel {

    data class Content(
        val refreshing: Boolean,
        val newPageState: PageState,
        val hasNext: Boolean,
        val lastLoadedPage: Int,
        val items: List<ArtCollectionItem>
    ) : ArtCollectionUiModel()

    object EmptyProgress : ArtCollectionUiModel()

    data class EmptyError(
        val message: String?
    ) : ArtCollectionUiModel()
}
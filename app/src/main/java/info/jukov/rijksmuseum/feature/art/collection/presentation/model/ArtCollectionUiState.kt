package info.jukov.rijksmuseum.feature.art.collection.presentation.model

sealed class ArtCollectionUiState {

    data class Content(
        val refreshing: Boolean,
        val newPageState: PageState,
        val hasNext: Boolean,
        val lastLoadedPage: Int,
        val items: List<ArtCollectionUiModel>
    ) : ArtCollectionUiState()

    object EmptyProgress : ArtCollectionUiState()

    data class EmptyError(
        val message: String?
    ) : ArtCollectionUiState()
}
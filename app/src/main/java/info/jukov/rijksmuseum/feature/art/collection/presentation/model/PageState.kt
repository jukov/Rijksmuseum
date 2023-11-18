package info.jukov.rijksmuseum.feature.art.collection.presentation.model

sealed class PageState {

    object None: PageState()

    object Loading: PageState()

    data class Error(val message: String?): PageState()
}
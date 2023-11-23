package info.jukov.rijksmuseum.feature.art.collection.presentation.model

import androidx.annotation.StringRes

sealed class PageState {

    object None: PageState()

    object Progress: PageState()

    data class Error(@StringRes val message: Int?): PageState()
}
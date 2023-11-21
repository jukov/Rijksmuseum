package info.jukov.rijksmuseum.feature.art.details.presentation.model

import androidx.annotation.StringRes
import info.jukov.rijksmuseum.feature.art.details.domain.model.ArtDetails

sealed class ArtDetailsUiModel {

    object Progress : ArtDetailsUiModel()

    data class Content(val data: ArtDetails) : ArtDetailsUiModel()

    data class Error(@StringRes val message: Int?) : ArtDetailsUiModel()
}
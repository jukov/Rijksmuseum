package info.jukov.rijksmuseum.feature.art.details.presentation.model

import info.jukov.rijksmuseum.feature.art.details.domain.model.ArtDetails

sealed class ArtDetailsUiModel {
    object Progress: ArtDetailsUiModel()

    data class Content(val data: ArtDetails): ArtDetailsUiModel()

    data class Error(val message: String?): ArtDetailsUiModel()
}
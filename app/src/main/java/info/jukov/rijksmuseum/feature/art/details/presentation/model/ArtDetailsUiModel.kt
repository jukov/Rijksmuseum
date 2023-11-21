package info.jukov.rijksmuseum.feature.art.details.presentation.model

import info.jukov.rijksmuseum.feature.art.details.domain.model.ArtDetails
import info.jukov.rijksmuseum.util.UiState

sealed class ArtDetailsUiModel {

    abstract val uiState: UiState

    object Progress: ArtDetailsUiModel() {
        override val uiState: UiState = UiState.Progress
    }

    data class Content(val data: ArtDetails): ArtDetailsUiModel() {
        override val uiState: UiState = UiState.Content
    }

    data class Error(val message: String?): ArtDetailsUiModel() {
        override val uiState: UiState = UiState.Error
    }
}
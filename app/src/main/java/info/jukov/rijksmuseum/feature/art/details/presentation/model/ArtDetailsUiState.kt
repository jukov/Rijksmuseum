package info.jukov.rijksmuseum.feature.art.details.presentation.model

import androidx.annotation.StringRes
import info.jukov.rijksmuseum.feature.art.details.domain.model.ArtDetails

sealed class ArtDetailsUiState {

    object Progress : ArtDetailsUiState()

    data class Content(val data: ArtDetails) : ArtDetailsUiState()

    data class Error(@StringRes val message: Int?) : ArtDetailsUiState()
}
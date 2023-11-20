package info.jukov.rijksmuseum.feature.art.collection.presentation.model

import info.jukov.rijksmuseum.feature.art.collection.domain.model.ArtCollectionItem

sealed class ArtCollectionUiModel {

    data class Item(val item: ArtCollectionItem): ArtCollectionUiModel()

    data class Header(val title: String): ArtCollectionUiModel()
}
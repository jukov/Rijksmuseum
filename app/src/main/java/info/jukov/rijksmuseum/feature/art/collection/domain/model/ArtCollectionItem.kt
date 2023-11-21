package info.jukov.rijksmuseum.feature.art.collection.domain.model

data class ArtCollectionItem(
    val id: String,
    val title: String,
    val longTitle: String,
    val author: String?,
    val imageUrl: String?,
    val imageAspectRatio: Float?
)
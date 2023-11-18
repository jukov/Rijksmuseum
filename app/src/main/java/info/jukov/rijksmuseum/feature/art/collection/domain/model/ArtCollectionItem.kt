package info.jukov.rijksmuseum.feature.art.collection.domain.model

data class ArtCollectionItem(
    val id: String,
    val name: String,
    val description: String?,
    val author: String?
)
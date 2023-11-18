package info.jukov.rijksmuseum.feature.list.domain.model

data class CollectionItem(
    val id: String,
    val name: String,
    val description: String?,
    val author: String?
)
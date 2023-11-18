package info.jukov.rijksmuseum.feature.art.details.domain.model

data class ArtDetails(
    val id: String,
    val title: String,
    val author: String?,
    val description: String?,
    val longTitle: String?,
    val subTitle: String?,
    val scLabelLine: String?
)
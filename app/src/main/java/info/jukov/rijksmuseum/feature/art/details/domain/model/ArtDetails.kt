package info.jukov.rijksmuseum.feature.art.details.domain.model

data class ArtDetails(
    val id: String,
    val title: String,
    val authors: List<String>,
    val placesOfCreation: List<String>?,
    val dateOfCreation: String?,
    val description: String?,
    val materials: List<String>,
    val techniques: List<String>,
    val dimensions: List<String>,
    val imageUrl: String?
)
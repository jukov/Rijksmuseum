package info.jukov.rijksmuseum.feature.art.details.data

import info.jukov.rijksmuseum.feature.art.details.domain.ArtDetailsRepository
import info.jukov.rijksmuseum.feature.art.details.domain.model.ArtDetails
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class ArtDetailsRepositoryTestImpl @Inject constructor(): ArtDetailsRepository {

    override fun get(id: String): Single<ArtDetails> =
        Single.just(
            ArtDetails(
                id = "id",
                title = "title",
                authors = listOf("author1", "author2"),
                placesOfCreation = listOf("place1", "place2"),
                dateOfCreation = "date",
                description = "description",
                materials = listOf("material1", "material2"),
                techniques = listOf("technique1", "technique2"),
                dimensions = listOf("dimension1", "dimension2"),
                imageUrl = null,
                imageAspectRatio = null
            )
        )
}

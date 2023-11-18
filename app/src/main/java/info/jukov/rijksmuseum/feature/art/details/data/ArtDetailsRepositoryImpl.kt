package info.jukov.rijksmuseum.feature.art.details.data

import info.jukov.rijksmuseum.feature.art.details.domain.ArtDetailsRepository
import info.jukov.rijksmuseum.feature.art.details.domain.model.ArtDetails
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class ArtDetailsRepositoryImpl @Inject constructor(
    private val artDetailsApiService: ArtDetailsApiService
) : ArtDetailsRepository {

    override fun get(id: String): Single<ArtDetails> =
        artDetailsApiService.getArtDetails(id = id)
            .map { dto ->
                with(requireNotNull(dto.artObject)) {
                    ArtDetails(
                        requireNotNull(this.id),
                        requireNotNull(title),
                        principalOrFirstMaker,
                        plaqueDescriptionEnglish,
                        longTitle,
                        subTitle,
                        scLabelLine
                    )
                }
            }
            .subscribeOn(Schedulers.io())

}
package info.jukov.rijksmuseum.feature.art.details.data

import info.jukov.rijksmuseum.feature.art.details.data.model.ArtDetailsDto.ArtObject.Maker
import info.jukov.rijksmuseum.feature.art.details.domain.ArtDetailsRepository
import info.jukov.rijksmuseum.feature.art.details.domain.model.ArtDetails
import info.jukov.rijksmuseum.util.error.ErrorMapper
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class ArtDetailsRepositoryImpl @Inject constructor(
    private val artDetailsApiService: ArtDetailsApiService,
    private val errorMapper: ErrorMapper
) : ArtDetailsRepository {

    override fun get(id: String): Single<ArtDetails> =
        artDetailsApiService.getArtDetails(id = id)
            .map { dto ->
                with(requireNotNull(dto.artObject)) {
                    val makers: List<Maker> = makers ?: emptyList()
                    val principalMakers: List<Maker> = principalMakers ?: emptyList()
                    val width = webImage?.width
                    val height = webImage?.height
                    val aspectRatio = if (width != null && height != null) {
                        width.toFloat() / height.toFloat()
                    } else {
                        null
                    }
                    ArtDetails(
                        requireNotNull(this.id),
                        requireNotNull(title),
                        (makers + principalMakers).mapNotNull { maker ->
                            buildString {
                                if (maker.roles?.isNotEmpty() == true) {
                                    append(maker.roles.joinToString())
                                    append(" – ")
                                }
                                append(maker.name ?: return@mapNotNull null)
                            }
                        },
                        productionPlaces ?: emptyList(),
                        dating?.presentingDate,
                        description,
                        materials ?: emptyList(),
                        techniques ?: emptyList(),
                        dimensions?.mapNotNull { dimension ->
                            buildString {
                                append(dimension.type ?: return@mapNotNull null)
                                append(": ")
                                append(dimension.value ?: return@mapNotNull null)
                                append(' ')
                                append(dimension.unit ?: return@mapNotNull null)
                            }
                        } ?: emptyList(),
                        webImage?.url,
                        aspectRatio
                    )
                }
            }
            .onErrorResumeNext { throwable ->
                Single.error(errorMapper.map(throwable))
            }
            .subscribeOn(Schedulers.io())

}
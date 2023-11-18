package info.jukov.rijksmuseum.feature.art.details.domain

import info.jukov.rijksmuseum.feature.art.details.domain.model.ArtDetails
import io.reactivex.rxjava3.core.Single

interface ArtDetailsRepository {

    fun get(id: String): Single<ArtDetails>
}
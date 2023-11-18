package info.jukov.rijksmuseum.feature.art.collection.domain

import info.jukov.rijksmuseum.feature.art.collection.domain.model.ArtCollectionItem
import io.reactivex.rxjava3.core.Single

interface ArtCollectionRepository {

    fun get(): Single<List<ArtCollectionItem>>

}
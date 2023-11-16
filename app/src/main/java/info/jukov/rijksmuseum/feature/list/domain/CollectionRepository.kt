package info.jukov.rijksmuseum.feature.list.domain

import info.jukov.rijksmuseum.feature.list.domain.model.CollectionItem
import io.reactivex.rxjava3.core.Single

interface CollectionRepository {

    fun get(): Single<List<CollectionItem>>

}
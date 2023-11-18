package info.jukov.rijksmuseum.feature.list.data

import info.jukov.rijksmuseum.feature.list.domain.CollectionRepository
import info.jukov.rijksmuseum.feature.list.domain.model.CollectionItem
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class CollectionRepositoryImpl @Inject constructor(
    private val collectionApiService: CollectionApiService
) : CollectionRepository {

    override fun get(): Single<List<CollectionItem>> =
        collectionApiService
            .getCollection(
                page = 0
            )
            .map { dto ->
                dto.artObjects?.mapNotNull { artObject ->
                    CollectionItem(
                        artObject?.id ?: return@mapNotNull null,
                        artObject.title ?: return@mapNotNull null,
                        artObject.longTitle,
                        artObject.principalOrFirstMaker
                    )
                } ?: emptyList()
            }
            .subscribeOn(Schedulers.io())
}
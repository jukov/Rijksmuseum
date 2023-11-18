package info.jukov.rijksmuseum.feature.art.collection.data

import info.jukov.rijksmuseum.feature.art.collection.domain.ArtCollectionRepository
import info.jukov.rijksmuseum.feature.art.collection.domain.model.ArtCollectionItem
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class ArtCollectionRepositoryImpl @Inject constructor(
    private val apiService: ArtCollectionApiService
) : ArtCollectionRepository {

    override fun get(): Single<List<ArtCollectionItem>> =
        apiService
            .getCollection(
                page = 0
            )
            .map { dto ->
                dto.artObjects?.mapNotNull { artObject ->
                    ArtCollectionItem(
                        artObject?.id ?: return@mapNotNull null,
                        artObject.title ?: return@mapNotNull null,
                        artObject.longTitle,
                        artObject.principalOrFirstMaker
                    )
                } ?: emptyList()
            }
            .subscribeOn(Schedulers.io())
}
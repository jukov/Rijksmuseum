package info.jukov.rijksmuseum.feature.art.details.data

import info.jukov.rijksmuseum.feature.art.collection.domain.ArtCollectionRepository
import info.jukov.rijksmuseum.feature.art.collection.domain.model.ArtCollectionItem
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class ArtCollectionRepositoryTestImpl @Inject constructor() : ArtCollectionRepository {

    override fun get(page: Int): Single<List<ArtCollectionItem>> = Single.just(
        (((page - 1) * 5) until ((page - 1) * 5 + 5)).map { index ->
            ArtCollectionItem(
                id = index.toString(),
                title = index.toString(),
                longTitle = index.toString(),
                author = "author",
                imageUrl = null,
                imageAspectRatio = null
            )
        }
    )
}

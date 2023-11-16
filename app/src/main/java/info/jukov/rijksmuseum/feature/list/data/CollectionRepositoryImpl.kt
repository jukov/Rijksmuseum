package info.jukov.rijksmuseum.feature.list.data

import info.jukov.rijksmuseum.feature.list.domain.CollectionRepository
import info.jukov.rijksmuseum.feature.list.domain.model.CollectionItem
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class CollectionRepositoryImpl @Inject constructor(): CollectionRepository {

    override fun get(): Single<List<CollectionItem>> =
        Single.just(
            (1..20).map { index ->
                CollectionItem(
                    index,
                    "Painting $index",
                    "Painting from famous author $index"
                )
            }
        )
            .subscribeOn(Schedulers.io())
}
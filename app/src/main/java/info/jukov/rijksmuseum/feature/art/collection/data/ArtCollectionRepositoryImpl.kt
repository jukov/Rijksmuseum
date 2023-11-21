package info.jukov.rijksmuseum.feature.art.collection.data

import info.jukov.rijksmuseum.feature.art.collection.domain.ArtCollectionRepository
import info.jukov.rijksmuseum.feature.art.collection.domain.model.ArtCollectionItem
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class ArtCollectionRepositoryImpl @Inject constructor(
    private val apiService: ArtCollectionApiService
) : ArtCollectionRepository {

    override fun get(page: Int): Single<List<ArtCollectionItem>> =
        apiService
            .getCollection(page = page)
            .map { dto ->
                dto.artObjects?.mapNotNull { artObject ->
                    val width = artObject?.webImage?.width
                    val height = artObject?.webImage?.height
                    val aspectRatio = if (width != null && height != null) {
                        width.toFloat() / height.toFloat()
                    } else {
                        null
                    }
                    ArtCollectionItem(
                        artObject?.objectNumber ?: return@mapNotNull null,
                        artObject.title ?: return@mapNotNull null,
                        artObject.longTitle ?: return@mapNotNull null,
                        artObject.principalOrFirstMaker,
                        artObject.webImage?.url,
                        aspectRatio
                    )
                } ?: emptyList()
            }
            .subscribeOn(Schedulers.io())
}
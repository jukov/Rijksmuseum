package info.jukov.rijksmuseum.feature.art.collection.data

import info.jukov.rijksmuseum.BuildConfig
import info.jukov.rijksmuseum.Const
import info.jukov.rijksmuseum.feature.art.collection.data.model.ArtCollectionDto
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ArtCollectionApiService {

    @GET("api/{culture}/collection")
    fun getCollection(
        @Path("culture") culture: String = Const.Network.CULTURE,
        @Query("key") key: String = BuildConfig.API_KEY,
        @Query("p") page: Int = 0,
        @Query("ps") pageSize: Int = Const.Network.PAGE_SIZE
    ): Single<ArtCollectionDto>
}
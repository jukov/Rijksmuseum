package info.jukov.rijksmuseum.feature.list.data

import info.jukov.rijksmuseum.BuildConfig
import info.jukov.rijksmuseum.Const
import info.jukov.rijksmuseum.feature.list.data.model.CollectionDto
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CollectionApiService {

    @GET("api/{culture}/collection?p=0&ps=20")
    fun getCollection(
        @Path("culture") culture: String = Const.Network.CULTURE,
        @Query("key") key: String = BuildConfig.API_KEY,
        @Query("p") page: Int = 0,
        @Query("ps") pageSize: Int = Const.Network.PAGE_SIZE
    ): Single<CollectionDto>
}
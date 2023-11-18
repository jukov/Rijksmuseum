package info.jukov.rijksmuseum.feature.art.details.data

import info.jukov.rijksmuseum.BuildConfig
import info.jukov.rijksmuseum.Const
import info.jukov.rijksmuseum.feature.art.details.data.model.ArtDetailsDto
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ArtDetailsApiService {

    @GET("api/{culture}/collection/{id}")
    fun getArtDetails(
        @Path("culture") culture: String = Const.Network.CULTURE,
        @Path("id") id: String,
        @Query("key") key: String = BuildConfig.API_KEY,
    ): Single<ArtDetailsDto>
}
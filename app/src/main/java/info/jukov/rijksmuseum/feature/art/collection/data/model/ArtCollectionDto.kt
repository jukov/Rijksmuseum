package info.jukov.rijksmuseum.feature.art.collection.data.model

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class ArtCollectionDto(
    @Json(name = "artObjects") val artObjects: List<ArtObject?>?,
    @Json(name = "count") val count: Int?,
    @Json(name = "countFacets") val countFacets: CountFacets?,
    @Json(name = "elapsedMilliseconds") val elapsedMilliseconds: Int?
) {
    @Keep
    @JsonClass(generateAdapter = true)
    data class ArtObject(
        @Json(name = "hasImage") val hasImage: Boolean?,
        @Json(name = "headerImage") val headerImage: HeaderImage?,
        @Json(name = "id") val id: String?,
        @Json(name = "links") val links: Links?,
        @Json(name = "longTitle") val longTitle: String?,
        @Json(name = "objectNumber") val objectNumber: String?,
        @Json(name = "permitDownload") val permitDownload: Boolean?,
        @Json(name = "principalOrFirstMaker") val principalOrFirstMaker: String?,
        @Json(name = "productionPlaces") val productionPlaces: List<Any?>?,
        @Json(name = "showImage") val showImage: Boolean?,
        @Json(name = "title") val title: String?,
        @Json(name = "webImage") val webImage: WebImage?
    ) {
        @Keep
        @JsonClass(generateAdapter = true)
        data class HeaderImage(
            @Json(name = "guid") val guid: String?,
            @Json(name = "height") val height: Int?,
            @Json(name = "offsetPercentageX") val offsetPercentageX: Int?,
            @Json(name = "offsetPercentageY") val offsetPercentageY: Int?,
            @Json(name = "url") val url: String?,
            @Json(name = "width") val width: Int?
        )

        @Keep
        @JsonClass(generateAdapter = true)
        data class Links(
            @Json(name = "self") val self: String?,
            @Json(name = "web") val web: String?
        )

        @Keep
        @JsonClass(generateAdapter = true)
        data class WebImage(
            @Json(name = "guid") val guid: String?,
            @Json(name = "height") val height: Int?,
            @Json(name = "offsetPercentageX") val offsetPercentageX: Int?,
            @Json(name = "offsetPercentageY") val offsetPercentageY: Int?,
            @Json(name = "url") val url: String?,
            @Json(name = "width") val width: Int?
        )
    }

    @Keep
    @JsonClass(generateAdapter = true)
    data class CountFacets(
        @Json(name = "hasimage") val hasimage: Int?,
        @Json(name = "ondisplay") val ondisplay: Int?
    )
}
package info.jukov.rijksmuseum.feature.art.details.data.model


import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class ArtDetailsDto(
    @Json(name = "artObject") val artObject: ArtObject?,
    @Json(name = "elapsedMilliseconds") val elapsedMilliseconds: Int?
) {
    @Keep
    @JsonClass(generateAdapter = true)
    data class ArtObject(
        @Json(name = "artistRole") val artistRole: Any?,
        @Json(name = "associations") val associations: List<Any?>?,
        @Json(name = "catRefRPK") val catRefRPK: List<Any?>?,
        @Json(name = "copyrightHolder") val copyrightHolder: Any?,
        @Json(name = "description") val description: Any?,
        @Json(name = "documentation") val documentation: List<String?>?,
        @Json(name = "exhibitions") val exhibitions: List<Any?>?,
        @Json(name = "hasImage") val hasImage: Boolean?,
        @Json(name = "historicalPersons") val historicalPersons: List<Any?>?,
        @Json(name = "id") val id: String?,
        @Json(name = "inscriptions") val inscriptions: List<Any?>?,
        @Json(name = "labelText") val labelText: Any?,
        @Json(name = "language") val language: String?,
        @Json(name = "location") val location: String?,
        @Json(name = "longTitle") val longTitle: String?,
        @Json(name = "makers") val makers: List<Any?>?,
        @Json(name = "materials") val materials: List<String?>?,
        @Json(name = "materialsThesaurus") val materialsThesaurus: List<Any?>?,
        @Json(name = "normalized32Colors") val normalized32Colors: List<Any?>?,
        @Json(name = "objectCollection") val objectCollection: List<String?>?,
        @Json(name = "objectNumber") val objectNumber: String?,
        @Json(name = "objectTypes") val objectTypes: List<String?>?,
        @Json(name = "physicalMedium") val physicalMedium: String?,
        @Json(name = "physicalProperties") val physicalProperties: List<Any?>?,
        @Json(name = "plaqueDescriptionDutch") val plaqueDescriptionDutch: String?,
        @Json(name = "plaqueDescriptionEnglish") val plaqueDescriptionEnglish: String?,
        @Json(name = "principalMaker") val principalMaker: String?,
        @Json(name = "principalOrFirstMaker") val principalOrFirstMaker: String?,
        @Json(name = "priref") val priref: String?,
        @Json(name = "productionPlaces") val productionPlaces: List<Any?>?,
        @Json(name = "productionPlacesThesaurus") val productionPlacesThesaurus: List<Any?>?,
        @Json(name = "scLabelLine") val scLabelLine: String?,
        @Json(name = "showImage") val showImage: Boolean?,
        @Json(name = "subTitle") val subTitle: String?,
        @Json(name = "techniques") val techniques: List<Any?>?,
        @Json(name = "techniquesThesaurus") val techniquesThesaurus: List<Any?>?,
        @Json(name = "title") val title: String?,
        @Json(name = "titles") val titles: List<String?>?,
        @Json(name = "webImage") val webImage: WebImage?
    ) {

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
}
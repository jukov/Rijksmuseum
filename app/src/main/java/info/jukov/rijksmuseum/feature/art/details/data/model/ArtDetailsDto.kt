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
        @Json(name = "acquisition") val acquisition: Acquisition?,
        @Json(name = "artistRole") val artistRole: Any?,
        @Json(name = "associations") val associations: List<Any?>?,
        @Json(name = "catRefRPK") val catRefRPK: List<String?>?,
        @Json(name = "classification") val classification: Classification?,
        @Json(name = "colors") val colors: List<Color?>?,
        @Json(name = "colorsWithNormalization") val colorsWithNormalization: List<ColorsWithNormalization?>?,
        @Json(name = "copyrightHolder") val copyrightHolder: Any?,
        @Json(name = "dating") val dating: Dating?,
        @Json(name = "description") val description: String?,
        @Json(name = "dimensions") val dimensions: List<Dimension>?,
        @Json(name = "documentation") val documentation: List<Any?>?,
        @Json(name = "exhibitions") val exhibitions: List<Any?>?,
        @Json(name = "hasImage") val hasImage: Boolean?,
        @Json(name = "historicalPersons") val historicalPersons: List<String?>?,
        @Json(name = "id") val id: String?,
        @Json(name = "inscriptions") val inscriptions: List<Any?>?,
        @Json(name = "label") val label: Label?,
        @Json(name = "labelText") val labelText: Any?,
        @Json(name = "language") val language: String?,
        @Json(name = "links") val links: Links?,
        @Json(name = "location") val location: Any?,
        @Json(name = "longTitle") val longTitle: String?,
        @Json(name = "makers") val makers: List<Maker>?,
        @Json(name = "materials") val materials: List<String>?,
        @Json(name = "materialsThesaurus") val materialsThesaurus: List<Any?>?,
        @Json(name = "normalized32Colors") val normalized32Colors: List<Normalized32Color?>?,
        @Json(name = "normalizedColors") val normalizedColors: List<NormalizedColor?>?,
        @Json(name = "objectCollection") val objectCollection: List<String?>?,
        @Json(name = "objectNumber") val objectNumber: String?,
        @Json(name = "objectTypes") val objectTypes: List<String?>?,
        @Json(name = "physicalMedium") val physicalMedium: String?,
        @Json(name = "physicalProperties") val physicalProperties: List<Any?>?,
        @Json(name = "plaqueDescriptionDutch") val plaqueDescriptionDutch: Any?,
        @Json(name = "plaqueDescriptionEnglish") val plaqueDescriptionEnglish: Any?,
        @Json(name = "principalMaker") val principalMaker: String?,
        @Json(name = "principalMakers") val principalMakers: List<Maker>?,
        @Json(name = "principalOrFirstMaker") val principalOrFirstMaker: String?,
        @Json(name = "priref") val priref: String?,
        @Json(name = "productionPlaces") val productionPlaces: List<String>?,
        @Json(name = "productionPlacesThesaurus") val productionPlacesThesaurus: List<Any?>?,
        @Json(name = "scLabelLine") val scLabelLine: String?,
        @Json(name = "showImage") val showImage: Boolean?,
        @Json(name = "subTitle") val subTitle: String?,
        @Json(name = "techniques") val techniques: List<String>?,
        @Json(name = "techniquesThesaurus") val techniquesThesaurus: List<Any?>?,
        @Json(name = "title") val title: String?,
        @Json(name = "titles") val titles: List<String?>?,
        @Json(name = "webImage") val webImage: WebImage?
    ) {
        @Keep
        @JsonClass(generateAdapter = true)
        data class Acquisition(
            @Json(name = "creditLine") val creditLine: Any?,
            @Json(name = "date") val date: String?,
            @Json(name = "method") val method: String?
        )

        @Keep
        @JsonClass(generateAdapter = true)
        data class Classification(
            @Json(name = "events") val events: List<Any?>?,
            @Json(name = "iconClassDescription") val iconClassDescription: List<Any?>?,
            @Json(name = "iconClassIdentifier") val iconClassIdentifier: List<Any?>?,
            @Json(name = "motifs") val motifs: List<Any?>?,
            @Json(name = "objectNumbers") val objectNumbers: List<String?>?,
            @Json(name = "people") val people: List<String?>?,
            @Json(name = "periods") val periods: List<Any?>?,
            @Json(name = "places") val places: List<Any?>?
        )

        @Keep
        @JsonClass(generateAdapter = true)
        data class Color(
            @Json(name = "hex") val hex: String?,
            @Json(name = "percentage") val percentage: Int?
        )

        @Keep
        @JsonClass(generateAdapter = true)
        data class ColorsWithNormalization(
            @Json(name = "normalizedHex") val normalizedHex: String?,
            @Json(name = "originalHex") val originalHex: String?
        )

        @Keep
        @JsonClass(generateAdapter = true)
        data class Dating(
            @Json(name = "period") val period: Int?,
            @Json(name = "presentingDate") val presentingDate: String?,
            @Json(name = "sortingDate") val sortingDate: Int?,
            @Json(name = "yearEarly") val yearEarly: Int?,
            @Json(name = "yearLate") val yearLate: Int?
        )

        @Keep
        @JsonClass(generateAdapter = true)
        data class Dimension(
            @Json(name = "type") val type: String?,
            @Json(name = "unit") val unit: String?,
            @Json(name = "value") val value: String?
        )

        @Keep
        @JsonClass(generateAdapter = true)
        data class Label(
            @Json(name = "date") val date: Any?,
            @Json(name = "description") val description: Any?,
            @Json(name = "makerLine") val makerLine: Any?,
            @Json(name = "notes") val notes: Any?,
            @Json(name = "title") val title: Any?
        )

        @Keep
        @JsonClass(generateAdapter = true)
        data class Links(
            @Json(name = "search") val search: String?
        )


        @Keep
        @JsonClass(generateAdapter = true)
        data class Maker(
            @Json(name = "biography") val biography: Any?,
            @Json(name = "dateOfBirth") val dateOfBirth: String?,
            @Json(name = "dateOfBirthPrecision") val dateOfBirthPrecision: String?,
            @Json(name = "dateOfDeath") val dateOfDeath: String?,
            @Json(name = "dateOfDeathPrecision") val dateOfDeathPrecision: String?,
            @Json(name = "labelDesc") val labelDesc: String?,
            @Json(name = "name") val name: String?,
            @Json(name = "nationality") val nationality: Any?,
            @Json(name = "occupation") val occupation: List<String?>?,
            @Json(name = "placeOfBirth") val placeOfBirth: String?,
            @Json(name = "placeOfDeath") val placeOfDeath: String?,
            @Json(name = "productionPlaces") val productionPlaces: List<String?>?,
            @Json(name = "qualification") val qualification: String?,
            @Json(name = "roles") val roles: List<String?>?,
            @Json(name = "unFixedName") val unFixedName: String?
        )

        @Keep
        @JsonClass(generateAdapter = true)
        data class Normalized32Color(
            @Json(name = "hex") val hex: String?,
            @Json(name = "percentage") val percentage: Int?
        )

        @Keep
        @JsonClass(generateAdapter = true)
        data class NormalizedColor(
            @Json(name = "hex") val hex: String?,
            @Json(name = "percentage") val percentage: Int?
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
    data class ArtObjectPage(
        @Json(name = "adlibOverrides") val adlibOverrides: AdlibOverrides?,
        @Json(name = "audioFile1") val audioFile1: Any?,
        @Json(name = "audioFileLabel1") val audioFileLabel1: Any?,
        @Json(name = "audioFileLabel2") val audioFileLabel2: Any?,
        @Json(name = "createdOn") val createdOn: String?,
        @Json(name = "id") val id: String?,
        @Json(name = "lang") val lang: String?,
        @Json(name = "objectNumber") val objectNumber: String?,
        @Json(name = "plaqueDescription") val plaqueDescription: Any?,
        @Json(name = "similarPages") val similarPages: List<Any?>?,
        @Json(name = "tags") val tags: List<Any?>?,
        @Json(name = "updatedOn") val updatedOn: String?
    ) {
        @Keep
        @JsonClass(generateAdapter = true)
        data class AdlibOverrides(
            @Json(name = "etiketText") val etiketText: Any?,
            @Json(name = "maker") val maker: Any?,
            @Json(name = "titel") val titel: Any?
        )
    }
}
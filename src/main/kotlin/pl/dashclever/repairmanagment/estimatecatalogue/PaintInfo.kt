package pl.dashclever.repairmanagment.estimatecatalogue

import jakarta.persistence.Embeddable
import jakarta.validation.constraints.Size
import pl.dashclever.publishedlanguage.SIZE_MAX

@Embeddable
data class PaintInfo(
    @field:Size(max = 24, message = "$SIZE_MAX;24")
    val baseColorWithCode: String,
    @field:Size(max = 512, message = "$SIZE_MAX;512")
    val varnishingPaintInfo: String
)

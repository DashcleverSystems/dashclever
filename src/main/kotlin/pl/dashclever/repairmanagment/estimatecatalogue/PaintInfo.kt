package pl.dashclever.repairmanagment.estimatecatalogue

import jakarta.persistence.Embeddable
import jakarta.validation.constraints.Size

@Embeddable
data class PaintInfo(
    @field:Size(max = 24)
    val baseColorWithCode: String,
    @field:Size(max = 512)
    val varnishingPaintInfo: String,
)

package pl.dashclever.repairmanagment.estimatecatalogue

import jakarta.persistence.Embeddable
import jakarta.validation.constraints.Size
import pl.dashclever.commons.exception.SIZE_MAX

@Embeddable
data class VehicleInfo(
    @field:Size(max = 24, message = "$SIZE_MAX;24")
    val registration: String,
    @field:Size(max = 24, message = "$SIZE_MAX;24")
    val brand: String,
    @field:Size(max = 24, message = "$SIZE_MAX;24")
    val model: String
)

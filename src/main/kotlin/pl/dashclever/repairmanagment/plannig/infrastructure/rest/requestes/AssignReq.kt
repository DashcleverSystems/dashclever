package pl.dashclever.repairmanagment.plannig.infrastructure.rest.requestes

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import java.time.LocalDate

@Suppress("MagicNumber")
internal data class AssignReq(
    val to: String,
    val at: LocalDate,
    @field:Min(8) @field:Max(24)
    val hour: Int?
)

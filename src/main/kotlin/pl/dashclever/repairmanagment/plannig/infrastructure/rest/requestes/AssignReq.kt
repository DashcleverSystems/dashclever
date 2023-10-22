package pl.dashclever.repairmanagment.plannig.infrastructure.rest.requestes

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import pl.dashclever.publishedlanguage.NUMBER_MAX
import pl.dashclever.publishedlanguage.NUMBER_MIN
import java.time.LocalDate

@Suppress("MagicNumber")
internal data class AssignReq(
    val to: String,
    val at: LocalDate,
    @field:Min(value = 8, message = "$NUMBER_MIN;8")
    @field:Max(value = 24, message = "$NUMBER_MAX;24")
    val hour: Int?
)

package pl.dashclever.accountresources.account.infrastructure.rest.requests

import jakarta.validation.constraints.Size
import pl.dashclever.publishedlanguage.SIZE_BETWEEN

internal data class CreateWorkshopReq(
    @field:Size(min = 3, max = 36, message = "$SIZE_BETWEEN;3;36")
    val displayName: String,
)

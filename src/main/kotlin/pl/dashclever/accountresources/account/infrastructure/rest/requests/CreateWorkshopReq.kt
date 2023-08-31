package pl.dashclever.accountresources.account.infrastructure.rest.requests

import jakarta.validation.constraints.Size

internal data class CreateWorkshopReq(
    @field:Size(min = 3, max = 36)
    val displayName: String
)

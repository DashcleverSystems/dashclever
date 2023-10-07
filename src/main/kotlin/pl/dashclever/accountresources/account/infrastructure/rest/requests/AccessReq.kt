package pl.dashclever.accountresources.account.infrastructure.rest.requests

import java.util.UUID

internal data class AccessReq(
    val workshopId: UUID,
    val employeeId: UUID?,
)

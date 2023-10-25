package pl.dashclever.accountresources.account.readmodel

import java.util.UUID

data class WorkshopAccessesDto(
    val workshopId: UUID,
    val workshopName: String,
    val accesses: Set<AccessDto>
)

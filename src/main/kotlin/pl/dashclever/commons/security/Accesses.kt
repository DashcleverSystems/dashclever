package pl.dashclever.commons.security

import pl.dashclever.commons.security.WithAuthorities.Authority
import java.util.UUID

interface WithAccountId {

    val accountId: UUID
}

interface WithWorkshopId {
    val workshopId: UUID
}

interface WithAuthorities {

    val authorities: Set<Authority>

    enum class Authority {
        MANAGE_STAFF,
        INSIGHT_REPAIR,
        REPAIR_PROCESS;
    }
}

data class WorkshopOwner(
    override val accountId: UUID,
    override val workshopId: UUID
) : WithAccountId, WithAuthorities, WithWorkshopId {

    override val authorities: Set<Authority> = Authority.values().toSet()
}

data class WorkshopEmployee(
    override val accountId: UUID,
    override val workshopId: UUID,
    val employeeId: UUID,
    override val authorities: Set<Authority>
) : WithAccountId, WithAuthorities, WithWorkshopId

data class SystemOnBehalfOfWorkshop(
    override val workshopId: UUID
) : WithWorkshopId

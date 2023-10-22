package pl.dashclever.commons.security

import pl.dashclever.commons.security.Access.WithAuthorities.Authority
import java.util.UUID

interface Access {

    val accountId: UUID

    sealed interface WithAuthorities : Access {

        val authorities: Set<Authority>

        enum class Authority {
            MANAGE_STAFF,
            INSIGHT_REPAIR,
            REPAIR_PROCESS
        }
    }

    data class WorkshopOwnerAccess(
        override val accountId: UUID,
        val workshopId: UUID
    ) : Access, WithAuthorities {

        override val authorities: Set<Authority> = Authority.values().toSet()
    }

    data class WorkshopEmployeeAccess(
        override val accountId: UUID,
        val workshopId: UUID,
        val employeeId: UUID,
        override val authorities: Set<Authority>
    ) : Access, WithAuthorities
}

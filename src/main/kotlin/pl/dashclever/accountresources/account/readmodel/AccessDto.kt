package pl.dashclever.accountresources.account.readmodel

import pl.dashclever.accountresources.account.readmodel.Authority.INSIGHT_REPAIR
import pl.dashclever.accountresources.account.readmodel.Authority.MANAGE_STAFF
import pl.dashclever.accountresources.account.readmodel.Authority.REPAIR_PROCESS
import pl.dashclever.accountresources.employee.Workplace
import java.util.UUID

data class AccessDto(
    val workshopId: UUID,
    val workshopName: String,
    val isOwnerAccess: Boolean,
    val employeeId: UUID?,
    val employeeFirstName: String?,
    val authorities: Set<Authority>,
)

enum class Role(val authorities: Set<Authority>) {

    OWNER(setOf(MANAGE_STAFF, INSIGHT_REPAIR, REPAIR_PROCESS)),
    SUPERVISOR(setOf(INSIGHT_REPAIR, REPAIR_PROCESS)),
    EMPLOYEE(setOf(REPAIR_PROCESS))
    ;

    companion object {

        fun from(workplace: Workplace): Role {
            return when (workplace) {
                Workplace.LABOUR -> EMPLOYEE
                Workplace.SUPERVISOR -> SUPERVISOR
            }
        }
    }
}

enum class Authority {
    MANAGE_STAFF,
    INSIGHT_REPAIR,
    REPAIR_PROCESS
}

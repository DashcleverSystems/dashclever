package pl.dashclever.accountresources.account.readmodel

import pl.dashclever.accountresources.account.readmodel.AuthorityDto.INSIGHT_REPAIR
import pl.dashclever.accountresources.account.readmodel.AuthorityDto.MANAGE_STAFF
import pl.dashclever.accountresources.account.readmodel.AuthorityDto.REPAIR_PROCESS
import pl.dashclever.accountresources.employee.Workplace
import pl.dashclever.commons.security.WithAuthorities.Authority
import java.util.UUID

data class AccessDto(
    val workshopId: UUID,
    val workshopName: String,
    val isOwnerAccess: Boolean,
    val employeeId: UUID?,
    val employeeFirstName: String?,
    val authorities: Set<AuthorityDto>
)

enum class Role(val authorities: Set<AuthorityDto>) {

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

enum class AuthorityDto {
    MANAGE_STAFF,
    INSIGHT_REPAIR,
    REPAIR_PROCESS;

    companion object {

        fun from(authority: Authority): AuthorityDto {
            return when (authority) {
                Authority.INSIGHT_REPAIR -> INSIGHT_REPAIR
                Authority.MANAGE_STAFF -> MANAGE_STAFF
                Authority.REPAIR_PROCESS -> REPAIR_PROCESS
            }
        }
    }
}

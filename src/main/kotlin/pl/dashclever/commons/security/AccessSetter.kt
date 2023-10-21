package pl.dashclever.commons.security

import pl.dashclever.commons.security.Access.WorkshopEmployeeAccess
import pl.dashclever.commons.security.Access.WorkshopOwnerAccess
import java.util.UUID

interface AccessSetter {

    fun setOwnerAccess(workshopId: UUID): WorkshopOwnerAccess

    fun setEmployeeAccess(employeeId: UUID): WorkshopEmployeeAccess
}
